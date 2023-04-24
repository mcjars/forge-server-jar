package us.sparkedhost.forgeserverjar;

import us.sparkedhost.forgeserverjar.config.Configuration;
import us.sparkedhost.forgeserverjar.server.ServerBootstrap;
import us.sparkedhost.forgeserverjar.utils.ErrorReporter;
import us.sparkedhost.forgeserverjar.utils.SparkedLock;

import java.io.*;
import java.lang.management.ManagementFactory;

public class ForgeServerJAR {
    private static final File CONFIG_FILE = new File(new File("."), "startup.properties");
    private static final Configuration config = new Configuration(CONFIG_FILE);

    public static void main(final String[] args) throws IOException {
        config.load();

        // Kill server if either the config is manually overwritten, or the server is
        // not hosted with Sparked.
        if (config.getServerCheck().equalsIgnoreCase("true") && !SparkedLock.isSparkedServer()) {
            ErrorReporter.error("02", true);
        }

        String forgeCompatibility = System.getenv().get("FORGE_COMPATIBILITY");
        switch (forgeCompatibility) {
            case "0":
            case "1":
                System.out.println("\n\033[1;33mThe server was detected as Forge!\033[0m");
                break;
            default:
                ErrorReporter.error("03", forgeCompatibility, true);
        }

        // Startup process
        if (!CONFIG_FILE.exists()) {
            ErrorReporter.error("04", true);
        }

        if (config.getForgeVersion().equalsIgnoreCase("unknown")) {
            ErrorReporter.error("05", true);
        }

        String[] vmArgs = ManagementFactory.getRuntimeMXBean().getInputArguments().toArray(new String[0]);
        String[] cmd = new String[vmArgs.length + 2];
        cmd[0] = "java";

        System.arraycopy(vmArgs, 0, cmd, 1, vmArgs.length);

        String forgeVersion = config.getForgeVersion();

        if (new File("libraries/net/minecraftforge/forge/" + forgeVersion + "/unix_args.txt").notExists()) {
            ErrorReporter.error("06", true);
        }

        cmd[1 + vmArgs.length] = "@libraries/net/minecraftforge/forge/" + forgeVersion + "/unix_args.txt";
        String cmdStr = String.join(" ", cmd);

        System.out.println("\n\033[1;33mStarting Forge 1.17+ server..\033[0m");
        System.out.println("\n\033[1;33mcustomer@sparkedhost:~$\033[0m " + cmdStr);

        new ServerBootstrap().startServer(cmd);
    }
}
