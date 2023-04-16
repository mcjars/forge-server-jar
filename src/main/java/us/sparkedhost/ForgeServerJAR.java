package us.sparkedhost;

import java.io.*;
import java.lang.management.ManagementFactory;

public class ForgeServerJAR {
    private static final File CONFIG_FILE = new File(new File("."), "startup.properties");
    private static final Configuration config = new Configuration(CONFIG_FILE);

    public static void main(final String[] args) throws IOException {

        config.load();

        // Kill server if either the config is manually overwritten, or the server is not hosted with Sparked.

        if(config.getServerCheck().equalsIgnoreCase("true") && !SparkedLock.isSparkedServer()) {
            System.out.println("\n\033[1;33mIt seems that this server is not hosted by Sparked Host.\033[0m\n\033[1;33mGet your own server today @ https://sparkedhost.com\033[0m");
            System.exit(0);
        }

        // Startup process

        if (!CONFIG_FILE.exists() || config.getForgeVersion().equalsIgnoreCase("unknown")) {
            System.out.println("\n\033[1;33mIt seems that the startup loader configuration file is missing or invalid.\033[0m\n\033[1;33mPlease contact our support team @ https://sparked.host/ticket/technical if you need further assistance.\033[0m");
            System.exit(0);
        }

        String[] vmArgs = ManagementFactory.getRuntimeMXBean().getInputArguments().toArray(new String[0]);

        String[] cmd = new String[vmArgs.length + 2];
        cmd[0] = "java";

        System.arraycopy(vmArgs, 0, cmd, 1, vmArgs.length);

        String forgeVersion = config.getForgeVersion();

        cmd[1 + vmArgs.length] = "@libraries/net/minecraftforge/forge/" + forgeVersion + "/unix_args.txt";
        String cmdStr = String.join(" ", cmd);

        System.out.println("\n\033[1;33mStarting Forge 1.17+ server..\033[0m");
        System.out.println("\n\033[1;33mcustomer@sparkedhost:~$\033[0m " + cmdStr);

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
        }
    }
}
