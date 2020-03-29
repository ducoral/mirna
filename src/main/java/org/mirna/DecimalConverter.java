package org.mirna;

import java.math.BigDecimal;

class DecimalConverter extends StringConverter {

    public DecimalConverter(Mapping mapping) {
        super(mapping);
    }

    @Override
    public String toText(Object value) {
        String txt = new BigDecimal(String.valueOf(value))
                .setScale(mapping.decimals(), BigDecimal.ROUND_DOWN)
                .toString();
        if (mapping.separator() == '\0')
            txt = txt.replace(".", "");
        else if (mapping.separator() != '.')
            txt = txt.replace(".", String.valueOf(mapping.separator()));
        return super.toText(txt);
    }

    @Override
    public Object fromText(String text) {
        setRemoveFill(mapping.fill() != '0' || mapping.align() == Align.LEFT || mapping.decimals() == 0);
        String value = (String) super.fromText(text);
        if (mapping.separator() == '\0')
            value = new StringBuilder(value)
                    .insert(value.length() - mapping.decimals(), '.')
                    .toString();
        else if (mapping.separator() != '.')
            value = value.replace(mapping.separator(), '.');
        Class<?> type = mapping.field().getType();
        if (type == Float.TYPE)
            return Float.parseFloat(value);
        else if (type == Float.class)
            return Float.valueOf(value);
        else if (type == Double.TYPE)
            return Double.parseDouble(value);
        else if (type == Double.class)
            return Double.valueOf(value);
        else
            return new BigDecimal(value);
    }
}
