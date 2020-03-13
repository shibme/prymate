package me.shib.tools.prymate;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class PryMateDataManager extends Thread {

    private static final transient String pryMateDataDirName = "prymate-data";
    private static final transient int flushTime = 5;
    private static transient PryMateDataManager dataManager;
    private transient Map<String, PryMateDataStore> storeMap;

    private PryMateDataManager(Set<String> hosts) throws IOException {
        File pryMateDataDir = new File(pryMateDataDirName);
        pryMateDataDir.mkdirs();
        this.storeMap = new HashMap<>();
        for (String host : hosts) {
            this.storeMap.put(host, new PryMateDataStore(pryMateDataDir, host));
        }
    }

    static synchronized PryMateDataManager getInstance(Set<String> hosts) throws IOException {
        if (dataManager == null) {
            dataManager = new PryMateDataManager(hosts);
        }
        return dataManager;
    }

    static synchronized void exit() throws IOException {
        dataManager.flush();
        System.exit(1);
    }

    PryMateDataStore get(String host) {
        return storeMap.get(host);
    }

    private synchronized void flush() throws IOException {
        for (PryMateDataStore dataStore : storeMap.values()) {
            dataStore.flush();
        }
    }

    void flushCycle() throws IOException, InterruptedException {
        Date next = new Date(0);
        while (true) {
            if (next.getTime() < new Date().getTime()) {
                next = new Date(new Date().getTime() + flushTime * 1000);
                flush();
            }
            Thread.sleep(1000);
        }
    }

}
