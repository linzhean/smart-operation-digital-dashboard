package tw.edu.ntub.imd.birc.sodd.enumerate.python;

import lombok.Getter;

@Getter
public enum PythonScript {
    NO_SUCH_SCRIPT("", ""),
    TEXT_SCRIPT("test.py", "C:\\Users\\Jerrylin\\IdeaProjects\\sodd-backend\\src\\python\\script\\Test.py"),
    AI_ASSISTANT("ai_assistant.py", "C:\\Users\\Jerrylin\\IdeaProjects\\sodd-backend\\python\\llama3_ai\\ai_assistant.py");

    private final String fileName;
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
