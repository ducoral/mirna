package org.mirna;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.mirna.Utils.mappings;

class Parser {

    private final List<Mapping> columns = new ArrayList<>();

    private final Class<?> LineClass;

    public Parser(Class<?> LineClass) {
        mappings(LineClass, this::add);
        this.LineClass = LineClass;
    }

    public String identifier() {
        return columns.get(0).identifier();
    }

    public String toText(Object Line) {
        StringBuilder text = new StringBuilder(columns.get(0).identifier());
        for (int pos = 1; pos < columns.size(); pos++) {
            Mapping mapping = columns.get(pos);
            Converter converter = mapping.converter();
            Object value = getValue(Line, mapping);
            text.append(converter.toText(value));
        }
        return text.toString();
    }

    public Object fromText(String text) {
        Object record = Line();
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

    private Object Line() {
        try {
            return LineClass.newInstance();
        } catch (Exception e) {
            throw new MirnaException(e.getMessage(), e);
        }
    }

    private Object getValue(Object Line, Mapping mapping) {
        try {
            Field field = mapping.field();
            field.setAccessible(true);
            return field.get(Line);
        } catch (IllegalAccessException e) {
            throw new MirnaException(e.getMessage(), e);
        }
    }

    private void setValue(Object Line, Mapping mapping, Object value) {
        try {
            Field field = mapping.field();
            field.setAccessible(true);
            field.set(Line, value);
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
}


