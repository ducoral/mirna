package org.mirna;

import org.mirna.annotations.*;
import org.mirna.converters.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mirna.Align.LEFT;

public class Descriptor {

    public final String name;

    public final int position;

    public final int length;

    public final char fill;

    public final String format;

    public final char separator;

    public final int decimals;

    public final String value;

    public final Align align;

    public final Class<? extends Converter> converter;

    private static final List<Class<? extends Annotation>> ANNOTATIONS = Arrays.asList(
            IntegerField.class, DecimalField.class, DateTimeField.class,
            StringField.class, CustomField.class);

    public static boolean isAnnotated(Field fld) {
        for (Class<? extends Annotation> ann : ANNOTATIONS)
            if (fld.isAnnotationPresent(ann))
                return true;
        return false;
    }

    public static boolean isValid(Object value, Class<? extends Annotation> annotation) {
        if (annotation == IntegerField.class)
            return value instanceof Byte
                    || value instanceof Short
                    || value instanceof Integer
                    || value instanceof Long
                    || value instanceof BigInteger;
        if (annotation == DecimalField.class)
            return value instanceof Float
                    || value instanceof Double
                    || value instanceof BigDecimal;
        if (annotation == DateTimeField.class)
            return value instanceof Date;
        if (annotation == StringField.class)
            return value instanceof Character
                    || value instanceof String;
        return annotation == CustomField.class;
    }

    public static Descriptor create(Field field) {
        for (Class<? extends Annotation> ann : ANNOTATIONS)
            if (field.isAnnotationPresent(ann))
                return create(field.getName(), field.getAnnotation(ann));
        throw new MirnaException(Strs.INVALID_PARAMETER, field);
    }

    public static Descriptor create(Annotation annotation) {
        return create("", annotation);
    }

    public static Descriptor create(String name, Annotation annotation) {
        if (annotation instanceof MirnaRecord) {
            MirnaRecord rec = (MirnaRecord) annotation;
            return new Descriptor(name, 0, rec.identifier().length(), (char) 0, "", '\0', 0, "'" + rec.identifier() + "'", LEFT, null);
        } else if (annotation instanceof IntegerField) {
            IntegerField intF = (IntegerField) annotation;
            return new Descriptor(name, intF.position(), intF.length(), intF.fill(), "", '\0', 0, "<integer>", intF.align(), IntegerConverter.class);
        } else if (annotation instanceof DecimalField) {
            DecimalField decF = (DecimalField) annotation;
            return new Descriptor(name, decF.position(), decF.length(), decF.fill(), "", decF.separator(), decF.decimals(), "<decimal>", decF.align(), DecimalConverter.class);
        } else if (annotation instanceof DateTimeField) {
            DateTimeField dtmF = (DateTimeField) annotation;
            return new Descriptor(name, dtmF.position(), dtmF.format().length(), '\0', dtmF.format(), '\0', 0, "<date time>", LEFT, DateTimeConverter.class);
        } else if (annotation instanceof StringField) {
            StringField strF = (StringField) annotation;
            return new Descriptor(name, strF.position(), strF.length(), strF.fill(), "", '\0', 0, "<string>", strF.align(), StringConverter.class);
        } else if (annotation instanceof CustomField) {
            CustomField ctmF = (CustomField) annotation;
            return new Descriptor(name, ctmF.position(), ctmF.length(), ctmF.fill(), "", '\0', 0, "<custom>", ctmF.align(), ctmF.converter());
        } else
            throw new MirnaException(Strs.INVALID_PARAMETER, annotation);
    }

    private Descriptor(
            String name, int position, int length, char fill, String format, char separator,
            int decimals, String value, Align align, Class<? extends Converter> converter) {
        this.name = name;
        this.position = position;
        this.length = length;
        this.fill = fill;
        this.format = format;
        this.separator = separator;
        this.decimals = decimals;
        this.value = value;
        this.converter = converter;
        this.align = align;
    }
}
