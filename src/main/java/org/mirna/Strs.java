package org.mirna;

import org.mirna.sample.Main;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
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
        return String.format(res.getString(key), args);
    }

    public String toString() {
        return getStr();
    }

    private String key;
    private ResourceBundle res;

    Strs(String key) {
        this.key = key;

        URL url = Main.class.getClassLoader().getResource(String.format("strings_%s.properties", Locale.getDefault()));
        if (url == null) url = Main.class.getClassLoader().getResource("strings.properties");
        if (url == null) throw new RuntimeException("FUNKING INTERNAL ERROR");

        try {
            URLConnection con = url.openConnection();
            con.setUseCaches(false);
            res = new PropertyResourceBundle(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new MirnaException(e.getMessage(), e);
        }
    }
}
