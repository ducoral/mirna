package org.mirna;

import java.util.ArrayList;
import java.util.List;

import static org.mirna.Rule.fields;
import static org.mirna.Utils.*;

class Linner {

    private final List<Fielded> fields = new ArrayList<>();

    private final Class<?> LineClass;

    public Linner(Class<?> LineClass) {
        fields(LineClass, this::add);
        this.LineClass = LineClass;
    }

    public String identifier() {
        return fields.get(0).identifier();
    }

    public String toText(Object Line) {
        StringBuilder text = new StringBuilder(fields.get(0).identifier());
        for (int pos = 1; pos < fields.size(); pos++) {
            Fielded fielded = fields.get(pos);
            Converter converter = fielded.converter();
            Object value = getValue(Line, fielded.field());
            text.append(converter.toText(value));
        }
        return text.toString();
    }

    public Object fromText(String text) {
        Object record = Line();
        Fielded fielded = fields.get(0);
        text = text.substring(fielded.length());
        for (int pos = 1; pos < fields.size(); pos++) {
            fielded = fields.get(pos);
            String substring = text.substring(0, fielded.length());
            try {
                Object value = fielded.converter().fromText(substring);
                setValue(record, fielded.field(), value);
            } catch (Exception e) {
                throw new Oops(e, Strs.MSG_ERROR_PARSING_FIELD, substring, fielded.field().getName());
            }
            text = text.substring(fielded.length());
        }
        return record;
    }

    private Object Line() {
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


