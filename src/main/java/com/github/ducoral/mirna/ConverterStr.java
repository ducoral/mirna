package com.github.ducoral.mirna;

class ConverterStr implements Converter {

    protected final Fielded fielded;

    private boolean removeFill = true;

    public ConverterStr(Fielded fielded) {
        this.fielded = fielded;
    }

    @Override
    public String toText(Object value) {
        return Utils.fixStr(String.valueOf(value), fielded.length(), fielded.fill(), fielded.align());
    }

    @Override
    public Object fromText(String text) {
        int begin = 0;
        int end = text.length();
        if (removeFill)
            if (fielded.align() == Align.RIGHT)
                while (begin < end - 1 && text.charAt(begin) == fielded.fill()) {
                    begin++;
                }
            else
                while (end > 1 && text.charAt(end - 1) == fielded.fill())
                    end--;
        String result = text.substring(begin, end);
        Class<?> type = fielded.field().getType();
        if (type == Character.TYPE || type == Character.class)
            return result.charAt(0);
        return result;
    }

    protected void setRemoveFill(boolean removeFill) {
        this.removeFill = removeFill;
    }
}
