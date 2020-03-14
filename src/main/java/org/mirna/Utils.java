package org.mirna;

import org.mirna.annotations.MirnaRecord;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.mirna.Strs.*;

public final class Utils {

    private Utils() {}

    public static String chars(int len, char fil) {
        return new String(new char[len]).replace('\0', fil);
    }

    public static String fixStr(String str, int len, char fil, Align ali) {
        return ali == Align.RIGHT ? fixRight(str, len, fil) : fixLeft(str, len, fil);
    }

    public static String fixRight(String str, int len, char fil) {
        return str.length() >= len
                ? str.substring(0, len)
                : chars(len - str.length(), fil) + str;
    }

    public static String fixLeft(String str, int len, char fill) {
        return str.length() >= len
                ? str.substring(str.length() - len)
                : str + chars(len - str.length(), fill);
    }

    public static List<String> strList(Object... objs) {
        List<String> list = new ArrayList<>();
        Arrays.stream(objs).forEach(o -> list.add(String.valueOf(o)));
        return list;
    }

    public static ResourceBundle resource() {
        ClassLoader loader = Utils.class.getClassLoader();
        URL resource = loader.getResource(String.format("strs_%s.properties", Locale.getDefault()));
        if (resource == null)
            resource = loader.getResource("strs.properties");
        if (resource == null)
            throw new MirnaException(INTERNAL_ERROR);
        try {
            return new PropertyResourceBundle(new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new MirnaException(e.getMessage(), e);
        }
    }

    public static List<Descriptor> descriptors(Class<?> mirnaRecord) {
        List<Descriptor> list = new ArrayList<>();
        if (mirnaRecord.isAnnotationPresent(MirnaRecord.class))
            list.add(Descriptor.create(CONFIG_REPORT_IDENTIFIER.toString(), mirnaRecord.getAnnotation(MirnaRecord.class)));
        for (Field field : mirnaRecord.getDeclaredFields())
            if (Descriptor.isAnnotated(field))
                addInOrder(list, Descriptor.create(field));
        return list;
    }

    private static void addInOrder(List<Descriptor> list, Descriptor descriptor) {
        int index = 0;
        while (index < list.size())
            if (descriptor.position < list.get(index).position) break;
            else index++;
        list.add(index, descriptor);
    }

    public static String report(Class<?> cla) {
        List<List<String>> table = new ArrayList<>();
        List<String> row = strList(
                CONFIG_REPORT_FIELD, CONFIG_REPORT_POSITION, CONFIG_REPORT_FROM,
                CONFIG_REPORT_TO, CONFIG_REPORT_SIZE, CONFIG_REPORT_VALUE);
        table.add(row);

        Map<Integer, Integer> cols = new LinkedHashMap<>();
        for (int index = 0; index < row.size(); index++)
            cols.put(index, row.get(index).length());

        int col = 0;
        for (Descriptor des : descriptors(cla)) {
            String name = des.name.replaceAll("[A-Z][^A-Z ]", " $0").toLowerCase();
            table.add(row = strList(name, des.position, col + 1, col + des.length, des.length, des.value));
            for (int index = 0; index < row.size(); index++)
                cols.put(index, Math.max(row.get(index).length(), cols.get(index)));
            col += des.length;
        }

        String lineSeparator = cols
                .values()
                .stream()
                .reduce("+", (par, len) -> par + chars(len + 2, '-') + '+', String::concat);

        StringBuilder report = new StringBuilder(lineSeparator);
        table.forEach(line -> report
                .append("\n| ").append(fixLeft(line.get(0), cols.get(0), ' '))
                .append(" | ").append(fixRight(line.get(1), cols.get(1), ' '))
                .append(" | ").append(fixRight(line.get(2), cols.get(2), ' '))
                .append(" | ").append(fixRight(line.get(3), cols.get(3), ' '))
                .append(" | ").append(fixRight(line.get(4), cols.get(4), ' '))
                .append(" | ").append(fixLeft(line.get(5), cols.get(5), ' '))
                .append(" |\n").append(lineSeparator));
        return report.toString();
    }
}
