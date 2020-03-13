package me.shib.tools.prymate;

import com.google.gson.Gson;

import java.io.*;
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
        this.requests = new LinkedBlockingDeque<>(getRequestsFromLog());
    }

    private File currentLogFile() {
        return new File(dataStoreDir.getPath() + File.separator + dateFormat.format(new Date()) + ".log");
    }

    private void appendToFile(String content) throws IOException {
        File file = currentLogFile();
        BufferedWriter out = new BufferedWriter(
                new FileWriter(file, true));
        out.write(content);
        out.close();
    }

    private StringBuilder getContentsFromLog() throws IOException {
        File file = currentLogFile();
        StringBuilder content = new StringBuilder();
        if (file.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while (line != null) {
                content.append(line);
                line = br.readLine();
                if (line != null) {
                    content.append("\n");
                }
            }
            br.close();
        }
        return content;
    }

    private List<PryMateRequest> getRequestsFromLog() throws IOException {
        File file = currentLogFile();
        List<PryMateRequest> requests = new ArrayList<>();
        if (file.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    requests.add(gson.fromJson(line, PryMateRequest.class));
                }
            }
            br.close();
        }
        return requests;
    }

    synchronized void flush() throws IOException {
        List<PryMateRequest> requestsToFlush = new ArrayList<>();
        requests.drainTo(requestsToFlush);
        StringBuilder log = new StringBuilder();
        for (PryMateRequest request : requestsToFlush) {
            log.append(gson.toJson(request)).append("\n");
        }
        appendToFile(log.toString());
    }

    void put(PryMateRequest request) {
        requests.add(request);
    }

}
