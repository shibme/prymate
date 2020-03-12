package me.shib.tools.prymate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class PryMateDataManager extends Thread {

    private static final transient int flushTime = 5;
    private static transient PryMateDataManager dataManager;
    private transient File pryMateData;
    private transient Map<String, PryMateDataStore> storeMap;

    private PryMateDataManager(Set<String> hosts) throws IOException {
        this.pryMateData = new File("prymate-data");
        this.pryMateData.mkdirs();
        this.storeMap = new HashMap<>();
        for (String host : hosts) {
            this.storeMap.put(host, new PryMateDataStore(pryMateData, host));
        }
    }

    static synchronized PryMateDataManager getInstance(Set<String> hosts) throws IOException {
        if (dataManager == null) {
            dataManager = new PryMateDataManager(hosts);
        }
        return dataManager;
    }

    static synchronized void exit() throws FileNotFoundException {
        dataManager.flush();
        System.exit(1);
    }

    PryMateDataStore get(String host) {
        return storeMap.get(host);
    }

    private synchronized void flush() throws FileNotFoundException {
        for (PryMateDataStore dataStore : storeMap.values()) {
            dataStore.flush();
        }
    }

    void flushCycle() throws FileNotFoundException, InterruptedException {
        Date next = new Date(0);
        while (true) {
            if (next.getTime() < new Date().getTime()) {
                next = new Date(new Date().getTime() + flushTime * 60000);
                flush();
            }
            Thread.sleep(1000);
        }
    }

}
