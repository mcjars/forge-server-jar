package dev.mcvapi.forgeserverjar.utils;

import lombok.experimental.UtilityClass;

import java.util.HashMap;

@UtilityClass
public class ErrorReporter {
    HashMap<String, String> errorMap;
    static {
        errorMap = new HashMap<>();
        errorMap.put("07", "Failed to start the server.");
        errorMap.put("08", "No directories found in libraries. Is Forge installed correctly?");
        errorMap.put("09", "Libraries is not empty, but Forge was not found. Is Forge installed correctly?");
    }

    public void error(String id, boolean crash) {
        error(id, errorMap.get(id), crash);
    }

    public void error(String id, String message, boolean crash) {
        System.out.println(message);
        if (crash) {
            boolean isPipeline = System.getenv().get("GITHUB_ACTIONS") != null;
            System.exit(isPipeline ? 0 : 1);
        }
    }
}
