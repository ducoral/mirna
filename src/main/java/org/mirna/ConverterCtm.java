package org.mirna;

class ConverterCtm extends ConverterStr {

    private final Converter custom;

    public ConverterCtm(Fielded fielded, Converter custom) {
        super(fielded);
        this.custom = custom;
    }

    @Override
    public String toText(Object value) {
        return super.toText(custom.toText(value));
    }

    @Override
    public Object fromText(String text) {
        return custom.fromText((String) super.fromText(text));
    }

    public Converter custom() {
        return custom;
    }
}
