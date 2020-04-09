package com.github.ducoral.mirna;

import java.util.ArrayList;
import java.util.List;

import static com.github.ducoral.mirna.Rule.fields;
import static com.github.ducoral.mirna.Utils.get;
import static com.github.ducoral.mirna.Utils.set;

class Linner {

    final List<Fielded> fieldeds = new ArrayList<>();

    final Class<?> type;

    Linner(Class<?> type) {
        fields(type, this::add);
        this.type = type;
    }

    String identifier() {
        return fieldeds.get(0).identifier();
    }

    String toText(Object line) {
        StringBuilder text = new StringBuilder(fieldeds.get(0).identifier());
        for (int pos = 1; pos < fieldeds.size(); pos++) {
            Fielded fielded = fieldeds.get(pos);
            Converter converter = fielded.converter();
            Object value = get(line, fielded.field());
            text.append(converter.toText(value));
        }
        return text.toString();
    }

    Object fromText(String text) {
        Object line = line();
        Fielded fielded = fieldeds.get(0);
        text = text.substring(fielded.length());
        for (int pos = 1; pos < fieldeds.size(); pos++) {
            fielded = fieldeds.get(pos);
            String substring = text.substring(0, fielded.length());
            try {
                Object value = fielded.converter().fromText(substring);
                set(line, fielded.field(), value);
            } catch (Exception e) {
                throw new Oops(e, Strs.MSG_ERROR_PARSING_FIELD, substring, fielded.field().getName());
            }
            text = text.substring(fielded.length());
        }
        return line;
    }

    private Object line() {
        try {
            return type.newInstance();
        } catch (Exception e) {
            throw new Oops(e.getMessage(), e);
        }
    }

    private void add(Fielded fielded) {
        int index = 0;
        while (index < fieldeds.size())
            if (fielded.position() < fieldeds.get(index).position()) {
                fieldeds.add(index, fielded);
                return;
            } else index++;
        fieldeds.add(index, fielded);
    }
}


