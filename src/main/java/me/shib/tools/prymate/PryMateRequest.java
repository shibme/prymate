package me.shib.tools.prymate;

import io.netty.handler.codec.http.HttpRequest;
import net.lightbody.bmp.util.HttpMessageInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

final class PryMateRequest {

    private long time;
    private String method;
    private String host;
    private String path;
    private List<Header> headers;
    private String body;

    private PryMateRequest(Date time, HttpRequest request, String body, HttpMessageInfo info) throws MalformedURLException {
        this.time = time.getTime();
        this.method = request.getMethod().name();
        URL url = new URL(info.getOriginalUrl());
        this.host = url.getHost();
        this.path = url.getPath();
        this.headers = new ArrayList<>();
        for (Map.Entry<String, String> header : request.headers()) {
            this.headers.add(new Header(header.getKey(), header.getValue()));
        }
        this.body = body;
    }

    long getTime() {
        return time;
    }

    String getMethod() {
        return method;
    }

    String getHost() {
        return host;
    }

    String getPath() {
        return path;
    }

    List<Header> getHeaders() {
        return headers;
    }

    String getBody() {
        return body;
    }

    static class Builder {
        private transient Date time;
        private transient HttpRequest request;
        private transient String body;
        private transient HttpMessageInfo info;

        Builder(HttpRequest request, String body, HttpMessageInfo info) {
            this.time = new Date();
            this.request = request;
            this.body = body;
            this.info = info;
        }

        void build(PryMateDataManager dataManager) {
            try {
                URL url = new URL(info.getUrl());
                PryMateDataStore dataStore = dataManager.get(url.getHost());
                if (dataStore != null) {
                    dataStore.put(new PryMateRequest(time, request, body, info));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    class Header {
        private String name;
        private String value;

        private Header(String name, String value) {
            this.name = name;
            this.value = value;
        }

        String getName() {
            return name;
        }

        String getValue() {
            return value;
        }
    }
}
