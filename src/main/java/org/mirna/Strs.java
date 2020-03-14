package org.mirna;

import org.mirna.sample.Main;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Properties;

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
        return String.format(prop.getProperty(key), args);
    }

    public String toString() {
        return getStr();
    }

    private String key;
    private Properties prop = new Properties();

    Strs(String key) {
        this.key = key;

        URL url = Main.class.getClassLoader().getResource(String.format("strings_%s.properties", Locale.getDefault()));
        if (url == null) url = Main.class.getClassLoader().getResource("strings.properties");
        if (url == null) throw new RuntimeException("FUNKING INTERNAL ERROR");

        try {
            prop.load(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new MirnaException(e.getMessage(), e);
        }
    }
}
