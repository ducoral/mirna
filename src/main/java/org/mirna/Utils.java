package org.mirna;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;

final class Utils {

    private Utils() {
    }

    static String chars(int length, char fill) {
        return new String(new char[length]).replace('\0', fill);
    }

    static String fixStr(String str, int len, char fil, Align ali) {
        return ali == Align.RIGHT ? fixRight(str, len, fil) : fixLeft(str, len, fil);
    }

    static String fixRight(String str, int length, char fill) {
        return str.length() >= length
                ? str.substring(0, length)
                : chars(length - str.length(), fill) + str;
    }

    static String fixLeft(String str, int length, char fill) {
        return str.length() >= length
                ? str.substring(str.length() - length)
                : str + chars(length - str.length(), fill);
    }

    static List<String> strList(Object... objects) {
        List<String> list = new ArrayList<>();
        Arrays.stream(objects).forEach(o -> list.add(String.valueOf(o)));
        return list;
    }

    static ResourceBundle resource() {
        ClassLoader loader = Utils.class.getClassLoader();
        URL resource = loader.getResource(String.format("strs_%s.properties", Locale.getDefault()));
        if (resource == null)
            resource = loader.getResource("strs.properties");
        if (resource == null)
            throw new MirnaException("Could not load message file");
        try {
            return new PropertyResourceBundle(new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new MirnaException(e.getMessage(), e);
        }
    }

    static void validate(Class<?> mirnaClass) {
        List<Mapping> maps = new ArrayList<>();
        mappings(mirnaClass, maps::add);
        if (maps.isEmpty())
            throw new MirnaException(
                    Strs.MSG_MISSING_CONFIGURATION, mirnaClass.getSimpleName());

        if (maps.get(0).identifier().isEmpty())
            throw new MirnaException(
                    Strs.MSG_ANNOTATION_NOT_PRESENT, MirnaRecord.class.getSimpleName(), mirnaClass.getSimpleName());

        if (maps.size() == 1)
            throw new MirnaException(
                    Strs.MSG_MISSING_FIELD_CONFIG, mirnaClass.getSimpleName());

        for (int i = 1; i < maps.size(); i++) {
            Mapping mapping = maps.get(i);
            Field field = mapping.field();
            if (Mapping.isMapped(field) && !Mapping.isTypeSupported(field))
                throw new MirnaException(
                        Strs.MSG_INVALID_FIELD_TYPE,
                        field.getType().getSimpleName(),
                        mapping.configuration().getSimpleName());
        }

        for (int pos = 0; pos < maps.size(); pos++) {
            int expected = maps.get(pos).position();
            if (pos > expected)
                throw new MirnaException(
                        Strs.MSG_DUPLICATE_POSITION_CONFIG,
                        expected,
                        maps.get(pos).field());
            else if (pos < expected)
                throw new MirnaException(
                        Strs.MSG_MISSING_POSITION_CONFIG,
                        pos,
                        maps.get(pos).field());
        }
    }

    static boolean match(Class<?> mirnaRecordClass, String textRecord) {
        MirnaRecord record = Objects.requireNonNull(mirnaRecordClass.getAnnotation(MirnaRecord.class));
        return Objects.requireNonNull(textRecord).startsWith(record.identifier());
    }

    static void mappings(Class<?> mirnaRecordClass, Consumer<Mapping> action) {
        action.accept(new Mapping(mirnaRecordClass));
        Arrays.stream(mirnaRecordClass.getDeclaredFields())
                .filter(Mapping::isMapped)
                .forEach(field -> action.accept(new Mapping(field)));
    }
}
