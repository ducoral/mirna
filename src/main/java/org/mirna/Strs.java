package org.mirna;

import static org.mirna.Utils.resource;

public enum Strs {

    INTERNAL_ERROR("internal.error"),
    INVALID_PARAMETER("invalid.parameter"),

    CONFIG_REPORT_IDENTIFIER("config.report.identifier"),
    CONFIG_REPORT_FIELD("config.report.field"),
    CONFIG_REPORT_POSITION("config.report.position"),
    CONFIG_REPORT_FROM("config.report.from"),
    CONFIG_REPORT_TO("config.report.to"),
    CONFIG_REPORT_SIZE("config.report.size"),
    CONFIG_REPORT_VALUE("config.report.value");

    public String formatString(Object... args) {
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
