package us.sparkedhost.forgeserverjar.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorReporter {

    public void error(String id, boolean crash) {
        error(id, null, crash);
    }

    public void error(String id, String message, boolean crash) {
        System.out.println("\033[1;33mAn error has occurred while starting the server (err-" + id + ")\033[0m");
        System.out.println("\033[1;33mUnexpected? Contact us @ sparkedhost.com" +
                (message != null ? " and let them know this: " + message : "")
                + "\033[0m");
        if (crash) {
            System.exit(0);
        }
    }
}
