package com.github.ducoral.mirna;

enum Strs {
    LINE_IDENTIFIER("line.identifier"),

    MSG_ANNOTATION_NOT_PRESENT("msg.annotation.not.present"),
    MSG_ANY_FIELD_ANNOTATION("msg.any.field.annotation"),
    MSG_ANY_ITEM_ANNOTATION("msg.any.item.annotation"),
    MSG_DOCUMENT_ANNOTATION("msg.document.annotation"),
    MSG_DUPLICATE_FOOTER_CONFIG("msg.duplicate.footer.config"),
    MSG_DUPLICATE_HEADER_CONFIG("msg.duplicate.header.config"),
    MSG_DUPLICATE_LINE_IDENTIFIER("msg.duplicate.line.identifier"),
    MSG_DUPLICATE_POSITION_CONFIG("msg.duplicate.position.config"),
    MSG_DUPLICATE_TYPE_CONFIG("msg.duplicate.type.config"),
    MSG_ERROR_PARSING_FIELD("msg.error.parsing.field"),
    MSG_ERROR_PARSING_LINE("msg.error.parsing.line"),
    MSG_HEADER_FOOTER_NOT_ALLOWED("msg.header.footer.not.allowed"),
    MSG_INTERNAL_ERROR("msg.internal.error"),
    MSG_INVALID_CONFIGURATION("msg.invalid.configuration"),
    MSG_INVALID_FIELD_TYPE("msg.invalid.field.type"),
    MSG_INVALID_LINE("msg.invalid.line"),
    MSG_INVALID_PARAMETER("msg.invalid.parameter"),
    MSG_MISSING_CONFIGURATION("msg.missing.configuration"),
    MSG_MISSING_DEFAULT_CONSTRUCTOR("msg.missing.default.constructor"),
    MSG_MISSING_FIELD_CONFIG("msg.missing.field.config"),
    MSG_MISSING_POSITION_CONFIG("msg.missing.position.config"),
    MSG_UNMAPPED_LINE("msg.unmapped.line"),

    REPORT("report"),
    REPORT_FIELD("report.field"),
    REPORT_POSITION("report.position"),
    REPORT_FROM("report.from"),
    REPORT_TO("report.to"),
    REPORT_LENGTH("report.length"),
    REPORT_FILL("report.fill"),
    REPORT_ALIGN("report.align"),
    REPORT_FORMAT("report.format"),
    REPORT_DECIMALS("report.decimals"),
    REPORT_SEPARATOR("report.separator"),
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
        return Utils.resource().getString(key);
    }

    private final String key;

    Strs(String key) {
        this.key = key;
    }
}
