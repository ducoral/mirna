package org.mirna.core;

class CustomConverter extends StringConverter {

    private final Converter user;

    public CustomConverter(Mapping mapping, Converter user) {
        super(mapping);
        this.user = user;
    }

    @Override
    public String toText(Object value) {
        return super.toText(user.toText(value));
    }

    @Override
    public Object fromText(String text) {
        return user.fromText((String) super.fromText(text));
    }
}
