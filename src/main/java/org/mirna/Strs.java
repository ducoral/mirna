package org.mirna;

import java.util.stream.IntStream;

import static org.mirna.Utils.resource;

public enum Strs {

    MSG_INTERNAL_ERROR("msg.internal.error"),
    MSG_INVALID_PARAMETER("msg.invalid.parameter"),
    MSG_ANNOTATION_NOT_PRESENT("msg.annotation.not.present"),
    MSG_MISSING_POSITION_CONFIG("msg.missing.position.config"),
    MSG_MISSING_FIELD_CONFIG("msg.missing.field.config"),
    MSG_INVALID_FIELD_TYPE("msg.invalid.field.type"),

    RECORD_IDENTIFIER("record.identifier"),

    REPORT_FIELD("config.report.field"),
    REPORT_POSITION("config.report.position"),
    REPORT_FROM("config.report.from"),
    REPORT_TO("config.report.to"),
    REPORT_SIZE("config.report.size"),
    REPORT_VALUE("config.report.value");

    public String format(Object... args) {
        IntStream
                .range(0, args.length)
                .forEach(index -> args[index] = args[index].toString());
        return String.format(toString(), args);
    }

    public String toString() {
        return resource().getString(key);
    }

    private final String key;

    Strs(String key) {
        this.key = key;
    }
}
