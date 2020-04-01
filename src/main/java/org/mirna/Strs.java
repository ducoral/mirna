package org.mirna;

import static org.mirna.Utils.resource;

enum Strs {
    LINE_IDENTIFIER("line.identifier"),

    MSG_ANY_FIELD_ANNOTATION("msg.any.field.annotation"),
    MSG_INTERNAL_ERROR("msg.internal.error"),
    MSG_INVALID_PARAMETER("msg.invalid.parameter"),
    MSG_INVALID_FIELD_TYPE("msg.invalid.field.type"),
    MSG_ANNOTATION_NOT_PRESENT("msg.annotation.not.present"),
    MSG_MISSING_CONFIGURATION("msg.missing.configuration"),
    MSG_MISSING_POSITION_CONFIG("msg.missing.position.config"),
    MSG_MISSING_FIELD_CONFIG("msg.missing.field.config"),
    MSG_DUPLICATE_POSITION_CONFIG("msg.duplicate.position_config"),
    MSG_DUPLICATE_LINE_IDENTIFIER("msg.duplicate.line.identifier"),
    MSG_UNMAPPED_LINE("msg.unmapped.line"),
    MSG_ERROR_PARSING_LINE("msg.error.parsing.line"),
    MSG_ERROR_PARSING_FIELD("msg.error.parsing.field"),


    REPORT("report"),
    REPORT_FIELD("report.field"),
    REPORT_POSITION("report.position"),
    REPORT_FROM("report.from"),
    REPORT_TO("report.to"),
    REPORT_SIZE("report.size"),
    REPORT_VALUE("report.value"),

    TEST_MSG_WITH_PARAMETERS("test.msg.with.parameters"),

    VERSION("version");

    public String format(Object... args) {
        String message = toString();
        for (int i = 0; i < args.length; i++)
            message = message.replace(String.format("{%d}", i), args[i].toString());
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
