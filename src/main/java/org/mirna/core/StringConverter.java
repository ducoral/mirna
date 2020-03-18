package org.mirna.core;

class StringConverter implements Converter {

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
            if (mapping.align() == Align.RIGHT)
                while (begin < end - 1 && text.charAt(begin) == mapping.fill()) {
                    begin++;
                }
            else
                while (end > 1 && text.charAt(end - 1) == mapping.fill())
                    end--;
        return text.substring(begin, end);
    }

    protected void setRemoveFill(boolean removeFill) {
        this.removeFill = removeFill;
    }
}
