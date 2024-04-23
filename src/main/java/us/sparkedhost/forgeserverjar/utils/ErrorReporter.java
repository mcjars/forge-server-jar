package us.sparkedhost.forgeserverjar.utils;

import lombok.experimental.UtilityClass;

import java.util.HashMap;

@UtilityClass
public class ErrorReporter {
    HashMap<String, String> errorMap;
    static {
        errorMap = new HashMap<>();
        errorMap.put("01", "Unable to get server IP address, you will need to contact us.");
        errorMap.put("02", "Unused, if you see this, open a ticket.");
        errorMap.put("03", "Your server is set to the wrong type (e.g. BungeeCord), please go to Settings and change " +
                "it to Minecraft: Java Edition.");
        errorMap.put("04", "Unused, if you see this, open a ticket.");
        errorMap.put("05", "Unused, if you see this, open a ticket.");
        errorMap.put("06", "Unused, if you see this, open a ticket.");
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
