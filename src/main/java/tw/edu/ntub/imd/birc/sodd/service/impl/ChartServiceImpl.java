package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tw.edu.ntub.birc.common.util.CollectionUtils;
import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.bean.ChartBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.*;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.AssignedTaskSponsor;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Chart;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Dashboard;
import tw.edu.ntub.imd.birc.sodd.dto.FileMultipartFile;
import tw.edu.ntub.imd.birc.sodd.dto.file.uploader.MultipartFileUploader;
import tw.edu.ntub.imd.birc.sodd.dto.file.uploader.UploadResult;
import tw.edu.ntub.imd.birc.sodd.exception.ChartException;
import tw.edu.ntub.imd.birc.sodd.exception.NotFoundException;
import tw.edu.ntub.imd.birc.sodd.service.ChartService;
import tw.edu.ntub.imd.birc.sodd.service.GroupService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.ChartTransformer;
import tw.edu.ntub.imd.birc.sodd.util.python.PythonUtils;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ChartServiceImpl extends BaseServiceImpl<ChartBean, Chart, Integer> implements ChartService {
    private final ChartDAO chartDAO;
    private final ChartGroupDAO chartGroupDAO;
    private final ChartDashboardDAO chartDashboardDAO;
    private final DashboardDAO dashboardDAO;
    private final AssignedTaskSponsorDAO sponsorDAO;
    private final GroupService groupService;
    private final ChartTransformer transformer;
    private final MultipartFileUploader multipartFileUploader;
    private final PythonUtils pythonUtils = new PythonUtils();

    public ChartServiceImpl(ChartDAO chartDAO,
                            ChartGroupDAO chartGroupDAO,
                            DashboardDAO dashboardDAO,
                            ChartDashboardDAO chartDashboardDAO,
                            AssignedTaskSponsorDAO sponsorDAO,
                            GroupService groupService,
                            ChartTransformer transformer,
                            MultipartFileUploader multipartFileUploader) {
        super(chartDAO, transformer);
        this.chartDAO = chartDAO;
        this.chartGroupDAO = chartGroupDAO;
        this.dashboardDAO = dashboardDAO;
        this.sponsorDAO = sponsorDAO;
        this.groupService = groupService;
        this.chartDashboardDAO = chartDashboardDAO;
        this.transformer = transformer;
        this.multipartFileUploader = multipartFileUploader;
    }

    @Override
    public ChartBean save(ChartBean chartBean) {
        Chart chart = transformer.transferToEntity(chartBean);
        try {
            Resource photoResource = pythonUtils.genHTML(chartBean.getScriptPath());
            checkFileOutput(photoResource.getFile());
            UploadResult showcaseImageResult = multipartFileUploader.upload(
                    chartBean.getImageFile(), "showcaseImage" + chartBean.getName());
            chart.setShowcaseImage(showcaseImageResult.getUrl());
            UploadResult scriptFileResult = multipartFileUploader.upload(
                    chartBean.getScriptFile(), "script" + chartBean.getName());
            chart.setScriptPath(scriptFileResult.getFilePath().toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return transformer.transferToBean(chartDAO.save(chart));
    }

    private void checkFileOutput(File file) {
        String photoError = pythonUtils.isValidImageFile(file);
        if (StringUtils.isNotBlank(photoError)) {
            throw new ChartException(photoError);
        }
        String htmlError = pythonUtils.isValidHTMLFile(file);
        if (StringUtils.isNotBlank(htmlError)) {
            throw new ChartException(htmlError);
        }
    }

    @Override
    public List<ChartBean> searchByDashboardId(Integer dashboardId) {
        return chartDashboardDAO.findByDashboardIdAndAvailableIsTrue(dashboardId)
                .stream()
                .map(chartDashboard ->
                        transformer.transferToBean(chartDAO.getById(chartDashboard.getChartId())))
                .peek(chartBean -> chartBean.setChartImage(genChartPhoto(chartBean)))
                .collect(Collectors.toList());
    }

    private String genChartPhoto(ChartBean chartBean) {
        try {
            Resource resource = pythonUtils.genPNG(chartBean.getScriptPath());
            File file = resource.getFile();
            if (file.exists()) {
                MultipartFile multipartFile = new FileMultipartFile(resource.getFile());
                UploadResult uploadResult = multipartFileUploader.upload(
                        multipartFile, "chart photo" + chartBean.getName());
                if (!file.delete()) {
                    throw new ChartException("無法刪除圖表檔案：" + file.getAbsolutePath());
                }
                return uploadResult.getUrl();
            } else {
                throw new ChartException("圖表檔案未生成");
            }
        } catch (IOException e) {
            throw new ChartException("圖表生成錯誤");
        }
    }

    @Override
    public List<Integer> searchChartIdsByDashboardId(Integer dashboardId) {
        return chartDashboardDAO.findByDashboardIdAndAvailableIsTrue(dashboardId)
                .stream()
                .map(chartDashboard -> chartDAO.getById(chartDashboard.getChartId()).getId())
                .collect(Collectors.toList());
    }

    @Override
    public List<ChartBean> searchByUser(String userId) {
        List<ChartBean> allCharts = CollectionUtils.map(chartDAO.findByAvailableIsTrue(), transformer::transferToBean);
        List<Chart> observableCharts = groupService.searchByUserId(userId)
                .stream()
                .flatMap(groupBean -> chartGroupDAO.findByGroupIdAndAvailableIsTrue(groupBean.getId()).stream())
                .map(chartGroup -> chartDAO.getById(chartGroup.getChartId()))
                .collect(Collectors.toList());
        List<AssignedTaskSponsor> canAssignCharts = sponsorDAO.findBySponsorUserIdAndAvailableIsTrue(userId);
        for (ChartBean chartBean : allCharts) {
            for (Chart chart : observableCharts) {
                chartBean.setObservable(Objects.equals(chartBean.getId(), chart.getId()));
            }
            for (AssignedTaskSponsor sponsor : canAssignCharts) {
                chartBean.setCanAssign(Objects.equals(chartBean.getId(), sponsor.getChartId()));
            }
        }
        return allCharts;
    }

    @Override
    public String getChartSuggestion(Integer id, Integer dashboardId) throws IOException {
//        String chart_data = "日期       | 生產線 | 產品編號 | 計劃產量 | 實際產量 " +
//                "2024-07-01 | A      | P001     | 1000     | 950 " +
//                "2024-07-01 | B      | P002     | 1500     | 1600 " +
//                "2024-07-02 | A      | P003     | 2000     | 2100 " +
//                "2024-07-02 | B      | P001     | 1200     | 1100 " +
//                "2024-07-03 | A      | P002     | 1800     | 1750 ";
        String chartData = "| 季度     | 銷售額 (美元) | 客戶數量 | 新增客戶數 | 客戶流失率 | 主要產品銷售額 (美元) |" +
                "|---------|-------------|---------|-----------|-----------|------------------| " +
                "| 2023 Q1 | 200,000     | 255     | 20        | 15%       | 30,000           | " +
                "| 2023 Q2 | 250,000     | 270     | 25        | 10%       | 40,000           | " +
                "| 2023 Q3 | 180,000     | 240     | 15        | 20%       | 20,000           | " +
                "| 2023 Q4 | 220,000     | 260     | 30        | 12%       | 35,000           |";
        // TODO 之後有圖表程式的時候才會開啟
        Chart chart = chartDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("查無此圖表"));
        String description = dashboardDAO.findById(dashboardId)
                .map(Dashboard::getDescription)
                .orElse("");
        return pythonUtils.genAISuggestion(chart.getScriptPath(), chartData, description);
    }
}
