package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import tw.edu.ntub.birc.common.util.CollectionUtils;
import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.bean.ChartBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.ChartDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.ChartDashboardDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.ChartGroupDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Chart;
import tw.edu.ntub.imd.birc.sodd.dto.PythonScript;
import tw.edu.ntub.imd.birc.sodd.service.ChartService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.ChartTransformer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChartServiceImpl extends BaseServiceImpl<ChartBean, Chart, Integer> implements ChartService {
    private final ChartDAO chartDAO;
    private final ChartGroupDAO chartGroupDAO;
    private final ChartDashboardDAO chartDashboardDAO;
    private final ChartTransformer transformer;

    public ChartServiceImpl(ChartDAO chartDAO,
                            ChartGroupDAO chartGroupDAO,
                            ChartDashboardDAO chartDashboardDAO,
                            ChartTransformer transformer) {
        super(chartDAO, transformer);
        this.chartDAO = chartDAO;
        this.chartGroupDAO = chartGroupDAO;
        this.chartDashboardDAO = chartDashboardDAO;
        this.transformer = transformer;
    }

    @Override
    public ChartBean save(ChartBean chartBean) {
        return null;
    }

    @Override
    public List<ChartBean> searchByDashboardId(Integer dashboardId) {
        return chartDashboardDAO.findByDashboardIdAndAvailableIsTrue(dashboardId)
                .stream()
                .map(chartDashboard ->
                        transformer.transferToBean(chartDAO.getById(chartDashboard.getChartId())))
                .map(chartBean -> {
                    try {
                        chartBean.setChart(genrateChart(chartBean.getScriptPath()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return chartBean;
                })
                .collect(Collectors.toList());
    }

    private Resource genrateChart(String filePath) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("python", filePath);
        Process process = pb.start();
        // 等待 Python 腳本執行完成
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 讀取 Python 腳本的輸出
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        StringBuilder output = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        String fileName = String.join(PythonScript.of(filePath).getFileName(), LocalDateTime.now().toString());
        Path htmlFilePath = Paths.get("src/python/file/" + fileName + ".html");
        return new FileSystemResource(htmlFilePath.toFile());
    }
}
