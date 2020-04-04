package org.mirna;

import java.util.ArrayList;
import java.util.List;

import static org.mirna.Rule.fields;
import static org.mirna.Utils.getField;
import static org.mirna.Utils.setField;

class Linner {

    private final List<Fielded> fields = new ArrayList<>();

    private final Class<?> LineClass;

    Linner(Class<?> LineClass) {
        fields(LineClass, this::add);
        this.LineClass = LineClass;
    }

    String identifier() {
        return fields.get(0).identifier();
    }

    String toText(Object line) {
        StringBuilder text = new StringBuilder(fields.get(0).identifier());
        for (int pos = 1; pos < fields.size(); pos++) {
            Fielded fielded = fields.get(pos);
            Converter converter = fielded.converter();
            Object value = getField(line, fielded.field());
            text.append(converter.toText(value));
        }
        return text.toString();
    }

    Object fromText(String text) {
        Object line = line();
        Fielded fielded = fields.get(0);
        text = text.substring(fielded.length());
        for (int pos = 1; pos < fields.size(); pos++) {
            fielded = fields.get(pos);
            String substring = text.substring(0, fielded.length());
            try {
                Object value = fielded.converter().fromText(substring);
                setField(line, fielded.field(), value);
            } catch (Exception e) {
                throw new Oops(e, Strs.MSG_ERROR_PARSING_FIELD, substring, fielded.field().getName());
            }
            text = text.substring(fielded.length());
        }
        return line;
    }

    private Object line() {
        try {
            return LineClass.newInstance();
        } catch (Exception e) {
            throw new Oops(e.getMessage(), e);
        }
    }

    private void add(Fielded fielded) {
        int index = 0;
        while (index < fields.size())
            if (fielded.position() < fields.get(index).position()) {
                fields.add(index, fielded);
                return;
            } else index++;
        fields.add(index, fielded);
    }
}


