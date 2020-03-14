package org.mirna;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

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

    String getStr(Object... args) {
        try {
            InputStream stream = Objects.requireNonNull(getClass().getClassLoader().getResource("strs.properties")).openStream();
            PropertyResourceBundle res = new PropertyResourceBundle(new InputStreamReader(stream, StandardCharsets.ISO_8859_1));
            return String.format(ResourceBundle.getBundle("strs").getString(key), args);
        } catch (Exception e) {
            throw new MirnaException(e.getMessage(), e);
        }
    }

    public String toString() {
        return getStr();
    }

    private String key;

    Strs(String key) {
        this.key = key;
    }
}
