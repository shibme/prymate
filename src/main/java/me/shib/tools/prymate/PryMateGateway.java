package me.shib.tools.prymate;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

final class PryMateGateway implements RequestFilter {

    private PryMateQueue queue;

    PryMateGateway(PryMateQueue queue) {
        this.queue = queue;
    }

    @Override
    public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents, HttpMessageInfo info) {
        queue.put(request, contents, info);
        return null;
    }
}
