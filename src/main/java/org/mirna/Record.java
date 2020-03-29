package org.mirna;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.mirna.Utils.mappings;

class Record {

    private final List<Mapping> columns = new ArrayList<>();

    private final Class<?> mirnaRecordClass;

    public Record(Class<?> mirnaRecordClass) {
        mappings(mirnaRecordClass, this::add);
        this.mirnaRecordClass = mirnaRecordClass;
    }

    public String identifier() {
        return columns.get(0).identifier();
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
}


