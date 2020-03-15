package org.mirna.converters;

import org.mirna.Align;
import org.mirna.Descriptor;
import org.mirna.MirnaException;
import org.mirna.Strs;
import org.mirna.annotations.DecimalField;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class DecimalConverter extends StringConverter {

    public DecimalConverter(Field field) {
        super(field);
    }

    @Override
    public String toText(Object value) {
        if (!Descriptor.isValid(value, DecimalField.class))
            throw new MirnaException(Strs.INVALID_PARAMETER, value);
        Descriptor des = descriptor();
        String txt = new BigDecimal(String.valueOf(value))
                .setScale(des.decimals, BigDecimal.ROUND_DOWN)
                .toString();
        if (des.separator == '\0')
            txt = txt.replace(".", "");
        else if (des.separator != '.')
            txt = txt.replace(".", String.valueOf(des.separator));
        return super.toText(txt);
    }

    @Override
    public Object fromText(String text) {
        Descriptor des = descriptor();
        setRemoveFill(des.fill != '0' || des.align == Align.LEFT || des.decimals == 0);
        String value = (String) super.fromText(text);
        if (des.separator == '\0')
            value = new StringBuilder(value)
                    .insert(value.length() - des.decimals, '.')
                    .toString();
        else if (des.separator != '.')
            value = value.replace(des.separator, '.');
        return new BigDecimal(value);
    }
}
