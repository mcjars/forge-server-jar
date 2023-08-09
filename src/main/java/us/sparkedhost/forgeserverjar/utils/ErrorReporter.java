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
        errorMap.put("04", "This server is missing a critical configuration file. You may have to reinstall it.");
        errorMap.put("05", "This server has a malformed configuration file. You may have to reinstall it.");
        errorMap.put("06", "Old server.jar file present.");
        errorMap.put("07", "Failed to start the server.");

    }

    public void error(String id, boolean crash) {
        error(id, errorMap.get(id), crash);
    }

    public void error(String id, String message, boolean crash) {
        System.out.println("\033[1;33mAn error has occurred while starting the server (err-" + id + "):\033[0m");
        System.out.println("\033[1;33m" + message + "\033[0m");
        System.out.println("\033[1;33mUnexpected, or unsure what to do? Contact us @ sparkedhost.com.\033[0m");
        if (crash) {
            boolean isPipeline = System.getenv().get("IS_GITLAB_PIPELINE") != null;
            System.exit(isPipeline ? 0 : 1);
        }
    }
}
