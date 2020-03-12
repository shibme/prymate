package me.shib.tools.prymate;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;

import java.io.IOException;

public final class PryMateInit {

    public static void main(String[] args) throws IOException, InterruptedException {
        PryMateConfig config = PryMateConfig.getConfig();
        PryMateQueue queue = new PryMateQueue();
        PryMateDataManager dataManager = PryMateDataManager.getInstance(config.getHosts());
        for (int i = 0; i < config.getThreads(); i++) {
            new PryMateProcessor(queue, dataManager).start();
        }
        PryMateGateway pryMateGateway = new PryMateGateway(queue);
        BrowserMobProxy pryMateProxy = new BrowserMobProxyServer();
        pryMateProxy.addRequestFilter(pryMateGateway);
        System.out.println("PryMate listening on port: " + config.getPort());
        pryMateProxy.start(config.getPort());
        dataManager.flushCycle();
    }

}
