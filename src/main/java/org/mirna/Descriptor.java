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
    public final Class<? extends Annotation> configuration;
    public final Class<? extends Converter> converter;

    private static final List<Class<? extends Annotation>> CONFIGURATIONS = Arrays.asList(
            IntegerField.class, DecimalField.class, DateTimeField.class, StringField.class, CustomField.class);

    private static final Class<?>[] INTEGER_TYPES = {
            Byte.class, Short.class, Integer.class, Long.class, BigInteger.class};

    private static final Class<?>[] DECIMAL_TYPES = {Float.class, Double.class, BigDecimal.class};

    private static final Class<?>[] DATE_TIME_TYPES = {Date.class};

    private static final Class<?>[] STRING_TYPES = {Character.class, String.class};

    public static boolean isAnnotated(Field fld) {
        for (Class<? extends Annotation> ann : CONFIGURATIONS)
            if (fld.isAnnotationPresent(ann))
                return true;
        return false;
    }

    public static void validate(Class<?> record) {
        if (!record.isAnnotationPresent(MirnaRecord.class))
            throw new MirnaException(Strs.MSG_ANNOTATION_NOT_PRESENT, MirnaRecord.class, record);

        List<Descriptor> descs = Utils.descriptors(record);

        if (descs.size() == 1)
            throw new MirnaException(Strs.MSG_MISSING_FIELD_CONFIG, record);

        descs.remove(0);
        int position = 0;
        for (Descriptor des : descs) {
            if (des.position != ++position)
                throw new MirnaException(Strs.MSG_MISSING_POSITION_CONFIG, position, record);
            try {
                Field field = record.getDeclaredField(des.name);
                if (!isValid(field.getType(), des.configuration))
                    throw new MirnaException(Strs.MSG_INVALID_FIELD_TYPE, field.getType(), des.configuration);
            } catch (NoSuchFieldException e) {
                throw new MirnaException(Strs.MSG_INTERNAL_ERROR);
            }
        }
    }

    public static boolean isValid(Class<?> type, Class<? extends Annotation> configuration) {
        if (configuration == IntegerField.class) return isAssignableFromOneOf(type, INTEGER_TYPES);
        if (configuration == DecimalField.class) return isAssignableFromOneOf(type, DECIMAL_TYPES);
        if (configuration == DateTimeField.class) return isAssignableFromOneOf(type, DATE_TIME_TYPES);
        if (configuration == StringField.class) return isAssignableFromOneOf(type, STRING_TYPES);
        return configuration == CustomField.class;
    }

    public static boolean isValid(Object value, Class<? extends Annotation> configuration) {
        return value != null && isValid(value.getClass(), configuration);
    }

    private static boolean isAssignableFromOneOf(Class<?> checked, Class<?>... types) {
        for (Class<?> type : types)
            if (checked.isAssignableFrom(type))
                return true;
        return false;
    }

    public static Descriptor create(Field field) {
        for (Class<? extends Annotation> config : CONFIGURATIONS)
            if (field.isAnnotationPresent(config))
                return create(field.getName(), field.getAnnotation(config));
        throw new MirnaException(Strs.MSG_INVALID_PARAMETER, field);
    }

    public static Descriptor create(Annotation annotation) {
        return create("", annotation);
    }

    public static Descriptor create(String name, Annotation annotation) {
        if (annotation instanceof MirnaRecord) {
            MirnaRecord rec = (MirnaRecord) annotation;
            return new Descriptor(
                    name, 0, rec.identifier().length(), (char) 0, "",
                    '\0', 0, "'" + rec.identifier() + "'", LEFT,  MirnaRecord.class, null);
        } else if (annotation instanceof IntegerField) {
            IntegerField intF = (IntegerField) annotation;
            return new Descriptor(
                    name, intF.position(), intF.length(), intF.fill(), "",
                    '\0', 0, "<integer>", intF.align(), IntegerField.class, IntegerConverter.class);
        } else if (annotation instanceof DecimalField) {
            DecimalField decF = (DecimalField) annotation;
            return new Descriptor(
                    name, decF.position(), decF.length(), decF.fill(), "",
                    decF.separator(), decF.decimals(), "<decimal>", decF.align(), DecimalField.class, DecimalConverter.class);
        } else if (annotation instanceof DateTimeField) {
            DateTimeField dtmF = (DateTimeField) annotation;
            return new Descriptor(
                    name, dtmF.position(), dtmF.format().length(), '\0', dtmF.format(),
                    '\0', 0, "<date time>", LEFT, DateTimeField.class, DateTimeConverter.class);
        } else if (annotation instanceof StringField) {
            StringField strF = (StringField) annotation;
            return new Descriptor(
                    name, strF.position(), strF.length(), strF.fill(), "",
                    '\0', 0, "<string>", strF.align(), StringField.class, StringConverter.class);
        } else if (annotation instanceof CustomField) {
            CustomField ctmF = (CustomField) annotation;
            return new Descriptor(
                    name, ctmF.position(), ctmF.length(), ctmF.fill(), "",
                    '\0', 0, "<custom>", ctmF.align(), CustomField.class, ctmF.converter());
        } else
            throw new MirnaException(Strs.MSG_INVALID_PARAMETER, annotation);
    }

    private Descriptor(
            String name, int position, int length, char fill, String format, char separator,
            int decimals, String value, Align align, Class<? extends Annotation> configuration, Class<? extends Converter> converter) {
        this.name = name;
        this.position = position;
        this.length = length;
        this.fill = fill;
        this.format = format;
        this.separator = separator;
        this.decimals = decimals;
        this.value = value;
        this.align = align;
        this.configuration = configuration;
        this.converter = converter;
    }

}
