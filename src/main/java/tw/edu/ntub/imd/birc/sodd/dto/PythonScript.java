package tw.edu.ntub.imd.birc.sodd.dto;

import lombok.Getter;
import org.springframework.security.core.parameters.P;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.Identity;

public enum PythonScript {
    NO_SUCH_SCRIPT("", ""),
    TEXT_SCRIPT("test.py", "C:\\Users\\Jerrylin\\IdeaProjects\\sodd-backend\\src\\python\\script\\Test.py");

    @Getter
    private final String fileName;
    @Getter
    private final String filePath;

    PythonScript(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public static PythonScript of(String filePath) {
        for (PythonScript pythonScript : PythonScript.values()) {
            if (pythonScript.getFilePath().equals(filePath)) {
                return pythonScript;
            }
        }
        return PythonScript.NO_SUCH_SCRIPT;
    }

    public static String getFileName(PythonScript pythonScript) {
        return "[" + pythonScript.getFileName() + "]";
    }
}
