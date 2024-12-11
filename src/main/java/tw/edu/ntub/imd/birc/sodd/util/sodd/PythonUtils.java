package tw.edu.ntub.imd.birc.sodd.util.sodd;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import tw.edu.ntub.imd.birc.sodd.enumerate.python.PythonGenType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class PythonUtils {
    @Value("${sodd.python.file-path}")
    private String path;

    public Resource genHTML(String filePath, String chartName, String data) throws IOException {
        String fileName = path + chartName + ".html";
        String output = callPythonScript(new ProcessBuilder(
                "python", filePath, PythonGenType.CHART_HTML.getType(), fileName), data);
        Path htmlFilePath = Paths.get(fileName);
        return new FileSystemResource(htmlFilePath.toFile());
    }

    public Resource genPNG(String filePath, String chartName) throws IOException {
        String fileName = path + chartName + ".png";
        callPythonScript(new ProcessBuilder(
                "python", filePath, PythonGenType.CHART_PNG.getType(), fileName));
        Path htmlFilePath = Paths.get(fileName);
        return new FileSystemResource(htmlFilePath.toFile());
    }

    public String genAISuggestion(String filePath, String chartData, String description) throws IOException {
        return callPythonScript(new ProcessBuilder(
                "python", filePath, PythonGenType.AI.getType(), description), chartData);
    }

    public String genAIChat(String filePath, String messages) throws IOException {
        return callPythonScript(new ProcessBuilder(
                "python", filePath, PythonGenType.AI.getType(), messages));
    }

    private String callPythonScript(ProcessBuilder pb) throws IOException {
        return getProcessResponse(pb.start());
    }

    private String callPythonScript(ProcessBuilder pb, String json) throws IOException {
        System.out.println("srgv: " + pb.command());
        Process process = pb.start();
        try (OutputStream os = process.getOutputStream()) {
            os.write(json.getBytes());
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getProcessResponse(process);
    }

    private String getProcessResponse(Process process) throws IOException {
        InputStream inputStream = null;
        // 等待 Python 腳本執行完成
        try {
            process.waitFor();
            inputStream = process.waitFor() != 0 ? process.getErrorStream() : process.getInputStream();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 讀取 Python 腳本的輸出
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String line;
        StringBuilder output = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        process.destroy();
        return output.toString().trim();
    }

    public String isValidImageFile(File file) {
        if (!file.exists()) {
            return "此程式未能輸出靜態圖表檔案";
        }
        try {
            BufferedImage image = ImageIO.read(file); // 如果能成功讀取且不為 null，則是有效圖像文件D
            if (image == null) {
                return "此程式未能輸出靜態圖表檔案";
            }
        } catch (IOException e) {
            return "無法讀取程式輸出圖檔";
        }
        return "";
    }

    public String isValidHTMLFile(File file) {
        if (!file.exists()) {
            return "此程式未能輸出HTML格式的互動式圖表檔案";
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

            String htmlContent = content.toString().toLowerCase();

            // 基本結構檢查
            if (!htmlContent.contains("<html>") || !htmlContent.contains("<head>") || !htmlContent.contains("<body>")) {
                return "未能輸出有效的HTML檔案";
            }
        } catch (IOException e) {
            return "無法讀取程式輸出之HTML檔案";
        }
        return "";
    }
}
