package us.sparkedhost.forgeserverjar.server;

import us.sparkedhost.forgeserverjar.utils.ErrorReporter;

import java.io.IOException;

public class ServerBootstrap {
    public void startServer(String[] cmd) {
        try {
            Process process = new ProcessBuilder(cmd)
                    .command(cmd)
                    .inheritIO()
                    .start();

            Runtime.getRuntime().addShutdownHook(new Thread(process::destroy));

            while (process.isAlive()) {
                try {
                    process.waitFor();
                    break;
                } catch (InterruptedException ignore) {
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            ErrorReporter.error("07", true);
        }
    }
}
