package com.github.ducoral.mirna;

class Oops extends RuntimeException {

    public Oops(String msg) {
        super(msg);
    }

    public Oops(String msg, Throwable thr) {
        super(msg, thr);
    }

    public Oops(Strs msg, Object... args) {
        super(msg.format(args));
    }

    public Oops(Throwable thr, Strs msg, Object... args) {
        super(msg.format(args), thr);
    }

}
