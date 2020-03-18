package org.mirna.core;

import org.mirna.annotations.MirnaRecord;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.mirna.core.Mapping.annotation;

class Record {

    private final List<Mapping> columns = new ArrayList<>();

    private Record(Class<?> mirnaRecord) {
        validate(mirnaRecord, this::add);
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

    static boolean match(Class<?> mirnaRecord, String textRecord) {
        return textRecord.startsWith(new Mapping(mirnaRecord).identifier());
    }

    static void validate(Class<?> mirnaRecord, Consumer<Mapping> action) {
        List<Mapping> maps = new ArrayList<>();
        mappings(mirnaRecord, maps::add);
        if (maps.isEmpty())
            throw new MirnaException(
                    Strs.MSG_MISSING_CONFIGURATION, mirnaRecord.getName());

        if (maps.get(0).identifier().isEmpty())
            throw new MirnaException(
                    Strs.MSG_ANNOTATION_NOT_PRESENT, MirnaRecord.class.getName(), mirnaRecord.getName());

        if (maps.size() == 1)
            throw new MirnaException(
                    Strs.MSG_MISSING_FIELD_CONFIG, mirnaRecord.getName());

        for (int i = 1; i < maps.size(); i++) {
            Mapping mapping = maps.get(i);
            Field field = (Field) mapping.target();
            if (Mapping.isMapped(field) && !Mapping.isTypeSupported(field))
                throw new MirnaException(
                        Strs.MSG_INVALID_FIELD_TYPE,
                        field.getType().getName(),
                        mapping.configuration());
        }

        for (int pos = 0; pos < maps.size(); pos++) {
            int expected = maps.get(pos).position();
            if (pos > expected)
                throw new MirnaException(
                        Strs.MSG_DUPLICATE_POSITION_CONFIG,
                        expected,
                        maps.get(0).target().getClass().getName());
            else if (pos < expected)
                throw new MirnaException(
                        Strs.MSG_MISSING_POSITION_CONFIG,
                        pos,
                        maps.get(0).target().getClass().getName());

        }
    }

    static void mappings(Class<?> mirnaRecord, Consumer<Mapping> action) {
        annotation(mirnaRecord, annotation -> action.accept(new Mapping(annotation)));
        Arrays.stream(mirnaRecord.getDeclaredFields())
                .filter(Mapping::isMapped)
                .forEach(field -> annotation(field, annotation -> action.accept(new Mapping(annotation))));
    }

}


