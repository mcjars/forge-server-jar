package dev.mcvapi.forgeserverjar.server;

import java.io.IOException;

public class ServerBootstrap {
    public void startServer(String[] cmd) throws ServerStartupException {
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
        } catch (IOException exception) {
            throw new ServerStartupException("Failed to start the Forge server.", exception);
        }
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    public static class ServerStartupException extends Exception {
        ServerStartupException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
