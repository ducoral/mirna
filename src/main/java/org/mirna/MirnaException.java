package org.mirna;

public class MirnaException extends RuntimeException {

    public MirnaException(String msg) {
        super(msg);
    }

    public MirnaException(String msg, Throwable thr) {
        super(msg, thr);
    }

    public MirnaException(Strs msg, Object... args) {
        super(msg.format(args));
    }

}
