package Utility;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v135.network.Network;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Optional;

public class NetworkUtility {
    public static boolean isHostReachable(String host) {
        try {
            InetAddress inet = InetAddress.getByName(host);
            return inet.isReachable(3000);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void captureNetworkRequests(ChromeDriver driver) {
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        devTools.addListener(Network.requestWillBeSent(), request -> {
            System.out.println("Request URL: " + request.getRequest().getUrl());
        });
    }
}
