package us.sparkedhost.forgeserverjar.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

@UtilityClass
public class SparkedLock {
    @SneakyThrows
    public boolean isSparkedServer() {
        String nodeIp = fetchIp();
        boolean isSparkedServer = false;

        try {
            URL site = new URL("http://" + nodeIp);
            BufferedReader br = new BufferedReader(new InputStreamReader(site.openStream()));

            String inputLine;

            while ((inputLine = br.readLine()) != null) {
                if (inputLine.contains("https://sparkedhost.com/assets/img/websitelogo.png")) {
                    isSparkedServer = true;
                }
            }
            br.close();
        } catch (Exception ignored) {
        }
        return isSparkedServer;
    }

    @SneakyThrows
    public String fetchIp() {
        try {
            URL awsIP = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(awsIP.openStream()));
            String ip = in.readLine();
            in.close();
            return ip;
        } catch (Exception ignored) {
            ErrorReporter.error("01", true);
        }
        return "0.0.0.0";
    }
}
