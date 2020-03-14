package org.mirna;

import org.mirna.annotations.MirnaRecord;

import java.lang.reflect.Field;
import java.util.*;

import static org.mirna.Strs.*;

public final class Utils {

    private Utils() {}

    public static String chars(int len, char fil) {
        return new String(new char[len]).replace('\0', fil);
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

    public static String report(Class<?> cla) {
        List<List<String>> table = new ArrayList<>();
        table.add(strList(
                CONFIG_REPORT_FIELD, CONFIG_REPORT_POSITION, CONFIG_REPORT_FROM,
                CONFIG_REPORT_TO, CONFIG_REPORT_SIZE, CONFIG_REPORT_VALUE));

        Descriptor des = Descriptor.create(Strs.CONFIG_REPORT_IDENTIFIER.getStr(), cla.getAnnotation(MirnaRecord.class));
        table.add(strList(des.name, des.pos, 1, des.len, des.len, "'" + des.val + "'"));
        int col = des.len;
        for (Field field : cla.getDeclaredFields())
            if (Descriptor.isAnnotated(field)) {
                des = Descriptor.create(field);
                String name = des.name.replaceAll("[A-Z]", " $0").toLowerCase();
                table.add(strList(name, des.pos, col + 1, col + des.len, des.len, des.val));
                col += des.len;
        }

        Map<Integer, Integer> cols = new LinkedHashMap<>();
        table.forEach(row -> {
            for (int i = 0; i < row.size(); i++)
                cols.put(i, Math.max(row.get(i).length(), cols.getOrDefault(i, 0)));
        });

        String lineSeparator = cols
                .values()
                .stream()
                .reduce("+", (par, len) -> par + chars(len + 2, '-') + '+', String::concat);

        StringBuilder report = new StringBuilder(lineSeparator);
        table.forEach(row -> report
                .append("\n| ").append(fixLeft(row.get(0), cols.get(0), ' '))
                .append(" | ").append(fixRight(row.get(1), cols.get(1), ' '))
                .append(" | ").append(fixRight(row.get(2), cols.get(2), ' '))
                .append(" | ").append(fixRight(row.get(3), cols.get(3), ' '))
                .append(" | ").append(fixRight(row.get(4), cols.get(4), ' '))
                .append(" | ").append(fixLeft(row.get(5), cols.get(5), ' '))
                .append(" |\n").append(lineSeparator));
        return report.toString();
    }

}
