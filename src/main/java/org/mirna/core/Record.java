package org.mirna.core;

import org.mirna.annotations.MirnaRecord;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

class Record {

    private final List<Mapping> columns = new ArrayList<>();

    Record(Class<?> mirnaRecord) {
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
                    Strs.MSG_MISSING_CONFIGURATION, mirnaRecord.getSimpleName());

        if (maps.get(0).identifier().isEmpty())
            throw new MirnaException(
                    Strs.MSG_ANNOTATION_NOT_PRESENT, MirnaRecord.class.getSimpleName(), mirnaRecord.getSimpleName());

        if (maps.size() == 1)
            throw new MirnaException(
                    Strs.MSG_MISSING_FIELD_CONFIG, mirnaRecord.getSimpleName());

        for (int i = 1; i < maps.size(); i++) {
            Mapping mapping = maps.get(i);
            Field field = (Field) mapping.target();
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
                        ((Field)maps.get(pos).target()).getName());
            else if (pos < expected)
                throw new MirnaException(
                        Strs.MSG_MISSING_POSITION_CONFIG,
                        pos,
                        ((Field)maps.get(pos).target()).getName());
        }
    }

    static void mappings(Class<?> mirnaRecord, Consumer<Mapping> action) {
        action.accept(new Mapping(mirnaRecord));
        Arrays.stream(mirnaRecord.getDeclaredFields())
                .filter(Mapping::isMapped)
                .forEach(field -> action.accept(new Mapping(field)));
    }

}


