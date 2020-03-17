package org.mirna;

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
    REPORT_VALUE("config.report.value"),

    TEST_MSG_WITH_PARAMETERS("test.msg.with.parameters");

    public String format(Object... args) {
        String message = toString();
        for (int i = 0; i < args.length; i++)
                message = message.replaceAll(String.format("\\{%d\\}", i), args[i].toString());
        return message;
    }

    public String toString() {
        return resource().getString(key);
    }

    private final String key;

    Strs(String key) {
        this.key = key;
    }
}
