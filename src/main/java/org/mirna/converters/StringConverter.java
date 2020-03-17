package org.mirna.converters;

import org.mirna.Mapping;
import org.mirna.Utils;

public class StringConverter implements Converter {

    protected final Mapping mapping;

    private boolean removeFill = true;

    public StringConverter(Mapping mapping) {
        this.mapping = mapping;
    }

    @Override
    public String toText(Object value) {
        return Utils.fixStr(String.valueOf(value), mapping.length(), mapping.fill(), mapping.align());
    }

    @Override
    public Object fromText(String text) {
        int begin = 0;
        int end = text.length();
        if (removeFill)
            switch (mapping.align()) {
                case RIGHT:
                    while (begin < end - 1 && text.charAt(begin) == mapping.fill())
                        begin++;
                    break;
                case LEFT:
                    while (end > 1 && text.charAt(end - 1) == mapping.fill())
                        end--;
                    break;
            }
        return text.substring(begin, end);
    }

    protected void setRemoveFill(boolean removeFill) {
        this.removeFill = removeFill;
    }
}
