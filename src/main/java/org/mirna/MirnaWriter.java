package org.mirna;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class MirnaWriter {

    private List<Object> records = new ArrayList<>();

    public MirnaWriter add(Object record) {
        this.records.add(record);
        return this;
    }

    public MirnaWriter write(Writer writer) {
        return this;
    }
}
