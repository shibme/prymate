package me.shib.tools.prymate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

class PryMateDataStore {

    private static transient final Gson gson = new Gson();
    private static transient final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH");

    private BlockingQueue<PryMateRequest> requests;
    private File dataStoreDir;

    PryMateDataStore(File dir, String host) throws IOException {
        this.dataStoreDir = new File(dir.getPath() + File.separator + host);
        this.dataStoreDir.mkdirs();
        this.requests = new LinkedBlockingDeque<>(readFromFile());
    }

    private synchronized void writeToFile(String content) throws FileNotFoundException {
        File file = new File(dataStoreDir.getPath() + File.separator + dateFormat.format(new Date()));
        PrintWriter pw = new PrintWriter(file);
        pw.append(content);
        pw.close();
    }

    private List<PryMateRequest> readFromFile() throws IOException {
        File file = new File(dataStoreDir.getPath() + File.separator + dateFormat.format(new Date()));
        StringBuilder content = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            content.append(line).append("\n");
        }
        br.close();
        Type type = new TypeToken<List<PryMateRequest>>() {
        }.getType();
        return gson.fromJson(content.toString(), type);
    }

    void flush() throws FileNotFoundException {
        List<PryMateRequest> flushableRequests = new ArrayList<>();
        requests.drainTo(flushableRequests);
        writeToFile(gson.toJson(flushableRequests));
    }

    void put(PryMateRequest request) {
        requests.add(request);
    }

}
