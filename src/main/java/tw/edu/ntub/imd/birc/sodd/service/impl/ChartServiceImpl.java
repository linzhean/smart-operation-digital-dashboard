package tw.edu.ntub.imd.birc.sodd.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tw.edu.ntub.birc.common.util.CollectionUtils;
import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.bean.ChartBean;
import tw.edu.ntub.imd.birc.sodd.bean.ChartDashboardBean;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.*;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.AssignedTaskSponsor;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Chart;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.ChartDashboard;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.ChartDataSource;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.views.CalJsonToInfo;
import tw.edu.ntub.imd.birc.sodd.dto.FileMultipartFile;
import tw.edu.ntub.imd.birc.sodd.dto.file.uploader.MultipartFileUploader;
import tw.edu.ntub.imd.birc.sodd.dto.file.uploader.UploadResult;
import tw.edu.ntub.imd.birc.sodd.exception.ChartException;
import tw.edu.ntub.imd.birc.sodd.exception.NotFoundException;
import tw.edu.ntub.imd.birc.sodd.service.ChartService;
import tw.edu.ntub.imd.birc.sodd.service.GroupService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.ChartTransformer;
import tw.edu.ntub.imd.birc.sodd.util.sodd.PythonUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ChartServiceImpl extends BaseServiceImpl<ChartBean, Chart, Integer> implements ChartService {
    private final ChartDAO chartDAO;
    private final ChartGroupDAO chartGroupDAO;
    private final ChartDashboardDAO chartDashboardDAO;
    private final AssignedTaskSponsorDAO sponsorDAO;
    private final DataSourceDAO dataSourceDAO;
    private final GroupService groupService;
    private final ChartTransformer transformer;
    private final MultipartFileUploader multipartFileUploader;
    private final PythonUtils pythonUtils = new PythonUtils();

    public ChartServiceImpl(ChartDAO chartDAO,
                            ChartGroupDAO chartGroupDAO,
                            ChartDashboardDAO chartDashboardDAO,
                            AssignedTaskSponsorDAO sponsorDAO,
                            DataSourceDAO dataSourceDAO,
                            GroupService groupService,
                            ChartTransformer transformer,
                            MultipartFileUploader multipartFileUploader) {
        super(chartDAO, transformer);
        this.chartDAO = chartDAO;
        this.chartGroupDAO = chartGroupDAO;
        this.sponsorDAO = sponsorDAO;
        this.dataSourceDAO = dataSourceDAO;
        this.groupService = groupService;
        this.chartDashboardDAO = chartDashboardDAO;
        this.transformer = transformer;
        this.multipartFileUploader = multipartFileUploader;
    }

    @Override
    public ChartBean save(ChartBean chartBean) {
//        Chart chart = transformer.transferToEntity(chartBean);
//        try {
//            UploadResult scriptFileResult = multipartFileUploader.upload(
//                    chartBean.getScriptFile(), "script", chartBean.getName());
//            chart.setScriptPath(scriptFileResult.getUrl());
//            UploadResult showcaseImageResult = multipartFileUploader.upload(
//                    chartBean.getImageFile(), "showcaseImage", chartBean.getName());
//            chart.setShowcaseImage(showcaseImageResult.getUrl());
//            Resource photoResource = pythonUtils.genPNG(chart.getScriptPath(), chart.getName());
//            Resource htmlResource = pythonUtils.genHTML(chart.getScriptPath(), chart.getName());
//            checkFileOutput(photoResource.getFile(), htmlResource.getFile());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return transformer.transferToBean(chartDAO.save(chart));
        return null;
    }

    private void checkFileOutput(File photoFile, File htmlFile) {
        String photoError = pythonUtils.isValidImageFile(photoFile);
        if (StringUtils.isNotBlank(photoError)) {
            throw new ChartException(photoError);
        }
        String htmlError = pythonUtils.isValidHTMLFile(htmlFile);
        if (StringUtils.isNotBlank(htmlError)) {
            throw new ChartException(htmlError);
        }
    }

    @Override
    public List<ChartBean> searchByDashboardId(Integer dashboardId) {
        List<ChartBean> chartBeans = chartDashboardDAO.findByDashboardIdAndAvailableIsTrue(dashboardId)
                .stream()
                .map(chartDashboard ->
                        transformer.transferToBean(chartDAO.findById(
                                chartDashboard.getChartId()).orElseThrow(() -> new NotFoundException("查無此圖表"))))
                .collect(Collectors.toList());
        List<AssignedTaskSponsor> canAssignCharts =
                sponsorDAO.findBySponsorUserIdAndAvailableIsTrue(SecurityUtils.getLoginUserAccount());
        for (ChartBean chartBean : chartBeans) {
            chartBean.setChartImage(genChartHTML(chartBean));
            for (AssignedTaskSponsor sponsor : canAssignCharts) {
                if (Objects.equals(chartBean.getId(), sponsor.getChartId())) {
                    chartBean.setCanAssign(true);
                    break;
                } else {
                    chartBean.setCanAssign(false);
                }
            }
        }
        return chartBeans;
    }


    @Override
    public String genChartHTML(ChartBean chartBean) {
        try {
            String jsonData = dataSourceDAO.getJsonData(ChartDataSource.of(chartBean.getDataSource()));
            CalJsonToInfo calJsonToInfo = ChartDataSource.getCalJsonToInfo(ChartDataSource.of(chartBean.getDataSource()));
            Gson gson = new Gson();
            Type mapType = new TypeToken<Map<String, List<Object>>>() {
            }.getType();
            Map<String, List<Object>> calculatedData = gson.fromJson(jsonData, mapType);
            Map<String, List<Object>> newInfoData = calJsonToInfo.calJsonToInfo(calculatedData);
            calculatedData.putAll(newInfoData);
            String calculatedJson = gson.toJson(calculatedData);
            Resource resource = pythonUtils.genHTML(chartBean.getScriptPath(), chartBean.getName(), calculatedJson);
            File file = resource.getFile();
            if (file.exists()) {
                MultipartFile multipartFile = new FileMultipartFile(resource.getFile());
                UploadResult uploadResult = multipartFileUploader.upload(
                        multipartFile, "chart_html", chartBean.getName());
                if (!file.delete()) {
                    throw new ChartException("無法刪除圖表檔案：" + file.getAbsolutePath());
                }
                return uploadResult.getUrl();
            } else {
                throw new ChartException("圖表檔案未生成");
            }
        } catch (IOException e) {
            throw new ChartException("圖表生成錯誤");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ChartBean> searchByUser(String userId, Integer dashboardId) {
        List<ChartBean> allCharts = CollectionUtils.map(chartDAO.findByAvailableIsTrue(), transformer::transferToBean);
        List<Chart> observableCharts = groupService.searchByUserId(userId)
                .stream()
                .flatMap(groupBean -> chartGroupDAO.findByGroupIdAndAvailableIsTrue(groupBean.getId()).stream())
                .map(chartGroup -> chartDAO.findByIdAndAvailableIsTrue(chartGroup.getChartId()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<ChartDashboard> addedCharts = new ArrayList<>();
        if (dashboardId != null) {
            addedCharts = chartDashboardDAO.findByDashboardIdAndAvailableIsTrue(dashboardId);
        }
        for (ChartBean chartBean : allCharts) {
            for (Chart chart : observableCharts) {
                if (Objects.equals(chartBean.getId(), chart.getId()) && chart.getId() != null) {
                    chartBean.setObservable(true);
                    break;
                } else {
                    chartBean.setObservable(false);
                }
            }
            for (ChartDashboard chartDashboard : addedCharts) {
                if (Objects.equals(chartBean.getId(), chartDashboard.getChartId())) {
                    chartBean.setIsAdded(true);
                    break;
                } else {
                    chartBean.setIsAdded(false);
                }
            }
        }
        return allCharts;
    }


    @Override
    public List<ChartBean> searchByAvailable(Boolean available) {
        return CollectionUtils.map(chartDAO.findByAvailable(available), transformer::transferToBean);
    }
}
