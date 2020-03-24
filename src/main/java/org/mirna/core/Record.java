package org.mirna.core;

import org.mirna.MirnaException;
import org.mirna.Strs;
import org.mirna.annotations.MirnaRecord;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Record {

    private final List<Mapping> columns = new ArrayList<>();

    private final Class<?> mirnaRecordClass;

    public Record(Class<?> mirnaRecordClass) {
        validate(mirnaRecordClass);
        mappings(mirnaRecordClass, this::add);
        this.mirnaRecordClass = mirnaRecordClass;
    }

    public String identifier() {
        return columns.get(0).identifier();
    }

    public static boolean match(Class<?> mirnaRecordClass, String textRecord) {
        return textRecord.startsWith(new Mapping(mirnaRecordClass).identifier());
    }

    public static void validate(Class<?> mirnaClass) {
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

    public String toText(Object mirnaRecord) {
        StringBuilder text = new StringBuilder(columns.get(0).identifier());
        for (int pos = 1; pos < columns.size(); pos++) {
            Mapping mapping = columns.get(pos);
            Converter converter = mapping.converter();
            Object value = getValue(mirnaRecord, mapping);
            text.append(converter.toText(value));
        }
        return text.toString();
    }

    public Object fromText(String text) {
        Object record = mirnaRecord();
        Mapping mapping = columns.get(0);
        text = text.substring(mapping.length());
        for (int pos = 1; pos < columns.size(); pos++) {
            mapping = columns.get(pos);
            String substring = text.substring(0, mapping.length());
            Object value = mapping.converter().fromText(substring);
            setValue(record, mapping, value);
            text = text.substring(mapping.length());
        }
        return record;
    }

    private Object mirnaRecord() {
        try {
            return mirnaRecordClass.newInstance();
        } catch (Exception e) {
            throw new MirnaException(e.getMessage(), e);
        }
    }

    private Object getValue(Object mirnaRecord, Mapping mapping) {
        try {
            Field field = mapping.field();
            field.setAccessible(true);
            return field.get(mirnaRecord);
        } catch (IllegalAccessException e) {
            throw new MirnaException(e.getMessage(), e);
        }
    }

    private void setValue(Object mirnaRecord, Mapping mapping, Object value) {
        try {
            Field field = mapping.field();
            field.setAccessible(true);
            field.set(mirnaRecord, value);
        } catch (Exception e) {
            throw new MirnaException(e.getMessage(), e);
        }
    }

    private void add(Mapping mapping) {
        int index = 0;
        while (index < columns.size())
            if (mapping.position() < columns.get(index).position()) {
                columns.add(index, mapping);
                return;
            } else index++;
        columns.add(index, mapping);
    }

    private static void mappings(Class<?> mirnaRecordClass, Consumer<Mapping> action) {
        action.accept(new Mapping(mirnaRecordClass));
        Arrays.stream(mirnaRecordClass.getDeclaredFields())
                .filter(Mapping::isMapped)
                .forEach(field -> action.accept(new Mapping(field)));
    }
}


