package org.mirna;

class CustomConverter extends StringConverter {

    private final Converter custom;

    public CustomConverter(Mapping mapping, Converter custom) {
        super(mapping);
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
