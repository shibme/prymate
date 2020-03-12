package me.shib.tools.prymate;

import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

final class PryMateConfig {

    private static final transient String PRYMATE_CONFIG_URL = "PRYMATE_CONFIG_URL";
    private static final transient File pryMateConfigFile = new File("prymate.json");
    private static final transient int defaultPort = 7796;
    private static final transient int defaultThreads = 2;
    private static final transient Gson gson = new Gson();

    private static transient PryMateConfig config;

    private Set<String> hosts;
    private Integer port;
    private Integer threads;

    private PryMateConfig() {
        this.hosts = new HashSet<>();
        init();
    }

    private static String getConfigFromURL() throws IOException {
        String pryMateConfigUrl = System.getenv(PRYMATE_CONFIG_URL);
        if (pryMateConfigUrl == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        URL url = new URL(pryMateConfigUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line).append("\n");
        }
        rd.close();
        return result.toString();
    }

    private static String getConfigFromFile() throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(pryMateConfigFile));
        String line;
        while ((line = br.readLine()) != null) {
            contentBuilder.append(line).append("\n");
        }
        br.close();
        return contentBuilder.toString();
    }

    static synchronized PryMateConfig getConfig() {
        if (config == null) {
            try {
                String json = getConfigFromURL();
                if (json == null) {
                    System.out.println(PRYMATE_CONFIG_URL + " was not set. Looking for prymate.json");
                    json = getConfigFromFile();
                }
                if (!json.isEmpty()) {
                    config = gson.fromJson(json, PryMateConfig.class);
                    config.init();
                }
            } catch (IOException ignored) {
            }
            System.out.println("No config file was found. Using default config.");
            config = new PryMateConfig();
        }
        return config;
    }

    private void init() {
        if (this.port == null) {
            this.port = defaultPort;
        }
        if (this.threads == null) {
            this.threads = defaultThreads;
        }
    }

    Set<String> getHosts() {
        return hosts;
    }

    Integer getPort() {
        return port;
    }

    Integer getThreads() {
        return threads;
    }
}
