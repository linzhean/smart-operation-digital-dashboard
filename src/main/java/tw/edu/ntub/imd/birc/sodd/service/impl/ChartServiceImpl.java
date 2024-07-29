package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import tw.edu.ntub.birc.common.exception.ProjectException;
import tw.edu.ntub.birc.common.util.CollectionUtils;
import tw.edu.ntub.imd.birc.sodd.bean.ChartBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.ChartDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.ChartDashboardDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.ChartGroupDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.DashboardDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Chart;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Dashboard;
import tw.edu.ntub.imd.birc.sodd.dto.PythonScript;
import tw.edu.ntub.imd.birc.sodd.service.ChartService;
import tw.edu.ntub.imd.birc.sodd.service.GroupService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.ChartTransformer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ChartServiceImpl extends BaseServiceImpl<ChartBean, Chart, Integer> implements ChartService {
    private final ChartDAO chartDAO;
    private final ChartGroupDAO chartGroupDAO;
    private final ChartDashboardDAO chartDashboardDAO;
    private final DashboardDAO dashboardDAO;
    private final GroupService groupService;
    private final ChartTransformer transformer;

    public ChartServiceImpl(ChartDAO chartDAO,
                            ChartGroupDAO chartGroupDAO,
                            DashboardDAO dashboardDAO,
                            ChartDashboardDAO chartDashboardDAO,
                            GroupService groupService,
                            ChartTransformer transformer) {
        super(chartDAO, transformer);
        this.chartDAO = chartDAO;
        this.chartGroupDAO = chartGroupDAO;
        this.dashboardDAO = dashboardDAO;
        this.groupService = groupService;
        this.chartDashboardDAO = chartDashboardDAO;
        this.transformer = transformer;
    }

    @Override
    public ChartBean save(ChartBean chartBean) {
        Chart chart = transformer.transferToEntity(chartBean);
        return transformer.transferToBean(chartDAO.save(chart));
    }

    @Override
    public List<ChartBean> searchByDashboardId(Integer dashboardId) {
        return chartDashboardDAO.findByDashboardIdAndAvailableIsTrue(dashboardId)
                .stream()
                .map(chartDashboard ->
                        transformer.transferToBean(chartDAO.getById(chartDashboard.getChartId())))
                .map(chartBean -> {
                    try {
                        chartBean.setChart(generateChart(chartBean.getScriptPath()));
                    } catch (IOException e) {
                        throw new ProjectException("圖表生成錯誤") {
                            @Override
                            public String getErrorCode() {
                                return "Chart - Generate Fail";
                            }
                        };
                    }
                    return chartBean;
                })
                .collect(Collectors.toList());
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
        for (ChartBean chartBean : allCharts) {
            for (Chart chart : observableCharts) {
                chartBean.setObservable(Objects.equals(chartBean.getId(), chart.getId()));
            }
        }
        return allCharts;
    }

    private Resource generateChart(String filePath) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("python", filePath);
        Process process = pb.start();
        InputStream inputStream = null;
        // 等待 Python 腳本執行完成
        try {
            process.waitFor();
            inputStream = process.waitFor() != 0 ? process.getErrorStream() : process.getInputStream();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 讀取 Python 腳本的輸出
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder output = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        String fileName = String.join(PythonScript.of(filePath).getFileName(), LocalDateTime.now().toString());
        Path htmlFilePath = Paths.get("src/python/file/" + fileName + ".html");
        return new FileSystemResource(htmlFilePath.toFile());
    }

    @Override
    public String getChartSuggestion(Integer id, Integer dashboardId) throws IOException {
//        String chart_data = "日期       | 生產線 | 產品編號 | 計劃產量 | 實際產量 " +
//                "2024-07-01 | A      | P001     | 1000     | 950 " +
//                "2024-07-01 | B      | P002     | 1500     | 1600 " +
//                "2024-07-02 | A      | P003     | 2000     | 2100 " +
//                "2024-07-02 | B      | P001     | 1200     | 1100 " +
//                "2024-07-03 | A      | P002     | 1800     | 1750 ";
        String chart_data = "| 季度     | 銷售額 (美元) | 客戶數量 | 新增客戶數 | 客戶流失率 | 主要產品銷售額 (美元) |" +
                "|---------|-------------|---------|-----------|-----------|------------------| " +
                "| 2023 Q1 | 200,000     | 255     | 20        | 15%       | 30,000           | " +
                "| 2023 Q2 | 250,000     | 270     | 25        | 10%       | 40,000           | " +
                "| 2023 Q3 | 180,000     | 240     | 15        | 20%       | 20,000           | " +
                "| 2023 Q4 | 220,000     | 260     | 30        | 12%       | 35,000           |";
        // TODO 之後有圖表程式的時候才會開啟
//        Chart chart = chartDAO.findById(id)
//                .orElseThrow(() -> new NotFoundException("查無此圖表"));
//        String filePath = chart.getScriptPath();
        String description = dashboardDAO.findById(dashboardId)
                .map(Dashboard::getDescription)
                .orElse("");
        ProcessBuilder pb = new ProcessBuilder("python",
                PythonScript.AI_ASSISTANT.getFilePath(), chart_data, description);
        Process process = pb.start();
        InputStream inputStream = null;
        // 等待 Python 腳本執行完成
        try {
            process.waitFor();
            inputStream = process.waitFor() != 0 ? process.getErrorStream() : process.getInputStream();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 讀取 Python 腳本的輸出
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder output = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        return output.toString();
    }
}
