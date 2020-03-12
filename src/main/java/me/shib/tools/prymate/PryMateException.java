package me.shib.tools.prymate;

import java.io.IOException;

public class PryMateException extends IOException {
    public PryMateException(String message) {
        super(message);
    }

    public PryMateException(Exception e) {
        super(e.getMessage(), e.getCause());
    }
}