package org.mirna.core;

import org.mirna.annotations.MirnaRecord;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

class Record {

    private final List<Mapping> columns = new ArrayList<>();

    Record(Class<?> mirnaRecordClass) {
        validate(mirnaRecordClass, this::add);
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

    String toText(Object mirnaRecord) {
        StringBuilder text = new StringBuilder(columns.get(0).identifier());
        for (int pos = 1; pos < columns.size(); pos++) {
            Mapping mapping = columns.get(pos);
            Converter converter = mapping.converter();
            Object value = value(mirnaRecord, mapping);
            text.append(converter.toText(value));
        }
        return text.toString();
    }

    Object value(Object mirnaRecord, Mapping mapping) {
        try {
            Field field = mapping.field();
            field.setAccessible(true);
            return field.get(mirnaRecord);
        } catch (IllegalAccessException e) {
            throw new MirnaException(e.getMessage(), e);
        }
    }

    static boolean match(Class<?> mirnaRecordClass, String textRecord) {
        return textRecord.startsWith(new Mapping(mirnaRecordClass).identifier());
    }

    static void validate(Class<?> mirnaClass, Consumer<Mapping> action) {
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
        maps.forEach(action);
    }

    static void mappings(Class<?> mirnaRecordClass, Consumer<Mapping> action) {
        action.accept(new Mapping(mirnaRecordClass));
        Arrays.stream(mirnaRecordClass.getDeclaredFields())
                .filter(Mapping::isMapped)
                .forEach(field -> action.accept(new Mapping(field)));
    }
}


