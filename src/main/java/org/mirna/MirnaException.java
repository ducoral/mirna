package org.mirna;

public class MirnaException extends RuntimeException {

    public MirnaException(String msg, Throwable thr) {
        super(msg, thr);
    }

    public MirnaException(Strs msg, Throwable thr, Object... args) {
        super(msg.getStr(args), thr);
    }

    public MirnaException(Strs msg, Object... args) {
        this(msg, null, args);
    }
}
