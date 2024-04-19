package us.sparkedhost.forgeserverjar;

import us.sparkedhost.forgeserverjar.server.ServerBootstrap;
import us.sparkedhost.forgeserverjar.utils.ErrorReporter;

import java.io.*;
import java.lang.management.ManagementFactory;

public class ForgeServerJAR {
    public static void main(final String[] args) {
        String forgeCompatibility = System.getenv().get("FORGE_COMPATIBILITY");
        if (forgeCompatibility == null) {
            ErrorReporter.error("03", true);
            return;
        }

        switch (forgeCompatibility) {
            case "0":
            case "1":
                System.out.println("\n\033[1;33mThe server was detected as Forge!\033[0m");
                break;
            default:
                ErrorReporter.error("03", true);
        }

        // Determine unix_args.txt location
        String directoryPath = "libraries/net/minecraftforge/forge";
        String forgeVersion = null;
        File directory = new File(directoryPath);
        File[] filesAndDirs = directory.listFiles();

        if (filesAndDirs == null) {
            ErrorReporter.error("08", true);
        }

        assert filesAndDirs != null;
        for (File fileOrDir : filesAndDirs) {
            if (fileOrDir.isDirectory()) {
                forgeVersion = fileOrDir.getName();
            }
        }

        if(forgeVersion == null) {
            ErrorReporter.error("09", true);
        }

        // Server startup
        String[] vmArgs = ManagementFactory.getRuntimeMXBean().getInputArguments().toArray(new String[0]);
        String[] cmd = new String[vmArgs.length + 2];
        cmd[0] = "java";

        System.arraycopy(vmArgs, 0, cmd, 1, vmArgs.length);

        cmd[1 + vmArgs.length] = "@libraries/net/minecraftforge/forge/" + forgeVersion + "/unix_args.txt";
        String cmdStr = String.join(" ", cmd);

        System.out.println("\n\033[1;33mStarting Forge 1.17+ server..\033[0m");
        System.out.println("\n\033[1;33mcustomer@apollopanel:~$\033[0m " + cmdStr);

        try {
            new ServerBootstrap().startServer(cmd);
        } catch (ServerBootstrap.ServerStartupException exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }
}
