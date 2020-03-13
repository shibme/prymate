package me.shib.tools.prymate;

import io.netty.handler.codec.http.HttpRequest;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

final class PryMateQueue {

    private BlockingQueue<PryMateRequest.Builder> queue;

    PryMateQueue() {
        this.queue = new LinkedBlockingDeque<>();
    }

    void put(HttpRequest request, HttpMessageContents contents, HttpMessageInfo info) {
        queue.add(new PryMateRequest.Builder(request, contents.getTextContents(), info));
    }

    PryMateRequest.Builder next() throws IOException {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            PryMateDataManager.exit();
            return null;
        }
    }

}
