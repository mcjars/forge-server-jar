package us.sparkedhost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;

public class SparkedLock {

    public static boolean isSparkedServer() throws IOException {
        URL awsIP = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(awsIP.openStream()));
        String ip = in.readLine();
        in.close();

        if(!checkHostOnline(ip)) {
            return false;
        }

        URL site = new URL("http://" + ip);
        BufferedReader br = new BufferedReader(new InputStreamReader(site.openStream()));

        String inputLine;
        boolean isSparkedServer = false;

        while ((inputLine = br.readLine()) != null) {
            if(inputLine.contains("https://sparkedhost.com/assets/img/websitelogo.png")) {
                isSparkedServer = true;
            }
        }
        br.close();

        return isSparkedServer;
    }

    private static boolean checkHostOnline(String address) {
        try (Socket ignored = new Socket(address, 80)) {
            return true;
        } catch (IOException ex) {
            /* ignore */
        }
        return false;
    }

}
