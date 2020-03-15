package org.mirna.converters;

import org.mirna.Descriptor;
import org.mirna.MirnaException;
import org.mirna.Strs;
import org.mirna.Utils;
import org.mirna.annotations.StringField;

import java.lang.reflect.Field;

public class StringConverter implements Converter {

    private final Field field;

    private boolean removeFill = true;

    public StringConverter(Field field) {
        this.field = field;
    }

    @Override
    public String toText(Object value) {
        if (!Descriptor.isValid(value, StringField.class))
            throw new MirnaException(Strs.MSG_INVALID_PARAMETER, value);
        Descriptor des = descriptor();
        return Utils.fixStr(String.valueOf(value), des.length, des.fill, des.align);
    }

    @Override
    public Object fromText(String text) {
        Descriptor des = descriptor();
        int begin = 0;
        int end = text.length();
        if (removeFill)
            switch (des.align) {
                case RIGHT:
                    while (begin < end - 1 && text.charAt(begin) == des.fill)
                        begin++;
                    break;
                case LEFT:
                    while (end > 1 && text.charAt(end - 1) == des.fill)
                        end--;
                    break;
            }
        return text.substring(begin, end);
    }

    protected Descriptor descriptor() {
        return Descriptor.create(field);
    }

    protected void setRemoveFill(boolean removeFill) {
        this.removeFill = removeFill;
    }
}
