package org.mirna.converters;

import org.mirna.Descriptor;
import org.mirna.Utils;

import java.lang.reflect.Field;

public class StringConverter implements Converter {

    private final Field field;

    public StringConverter(Field field) {
        this.field = field;
    }

    @Override
    public String toStr(Object value) {
        Descriptor des = descriptor();
        return Utils.fixStr(String.valueOf(value), des.length, des.fill, des.align);
    }

    @Override
    public Object fromStr(String value) {
        Descriptor des = descriptor();
        int begin = 0;
        int end = value.length();
        switch (des.align) {
            case RIGHT:
                while (begin < end - 1 && value.charAt(begin) == des.fill)
                    begin++;
                break;
            case LEFT:
                while (end > 1 && value.charAt(end - 1) == des.fill)
                    end--;
                break;
        }
        return value.substring(begin, end);
    }

    protected Descriptor descriptor() {
        return Descriptor.create(field);
    }
}
