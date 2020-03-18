package org.mirna.core;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

final class Utils {

    private Utils() {
    }

    public static String chars(int length, char fill) {
        return new String(new char[length]).replace('\0', fill);
    }

    public static String fixStr(String str, int len, char fil, Align ali) {
        return ali == Align.RIGHT ? fixRight(str, len, fil) : fixLeft(str, len, fil);
    }

    public static String fixRight(String str, int length, char fill) {
        return str.length() >= length
                ? str.substring(0, length)
                : chars(length - str.length(), fill) + str;
    }

    public static String fixLeft(String str, int length, char fill) {
        return str.length() >= length
                ? str.substring(str.length() - length)
                : str + chars(length - str.length(), fill);
    }

    public static List<String> strList(Object... objects) {
        List<String> list = new ArrayList<>();
        Arrays.stream(objects).forEach(o -> list.add(String.valueOf(o)));
        return list;
    }

    public static ResourceBundle resource() {
        ClassLoader loader = Utils.class.getClassLoader();
        URL resource = loader.getResource(String.format("strs_%s.properties", Locale.getDefault()));
        if (resource == null)
            resource = loader.getResource("strs.properties");
        if (resource == null)
            throw new MirnaException("Não foi possível carregar o arquivo de mensagens");
        try {
            return new PropertyResourceBundle(new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new MirnaException(e.getMessage(), e);
        }
    }

    private static List<String> update(List<String> list, Map<Integer, Integer> columns) {
        int column = columns.size();
        list.forEach(str -> columns.put(column, Math.max(str.length(), columns.getOrDefault(column, 0))));
        return list;
    }
}
