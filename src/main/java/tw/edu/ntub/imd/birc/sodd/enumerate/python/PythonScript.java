package tw.edu.ntub.imd.birc.sodd.enumerate.python;

import lombok.Getter;

@Getter
public enum PythonScript {
    NO_SUCH_SCRIPT("", ""),
    AI_ASSISTANT("ai_assistant.py", "C:\\Users\\Jerrylin" +
            "\\IdeaProjects\\sodd-backend\\python\\llama3_ai\\ai_assistant.py"),
    AI_CHAT("ai_chat.py", "C:\\Users\\Jerrylin" +
            "\\IdeaProjects\\sodd-backend\\python\\llama3_ai\\ai_chat.py"),
    YIELD_ACHIEVEMENT_RATE("yield_achievement_rate.py", "C:\\Users\\Jerrylin" +
            "\\IdeaProjects\\sodd-backend\\UploadFile\\file\\script\\產能達成率\\yield_achievement_rate.py");

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
