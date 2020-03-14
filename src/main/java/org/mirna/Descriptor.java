package org.mirna;

import org.mirna.annotations.*;
import org.mirna.converters.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.mirna.Align.LEFT;

public class Descriptor {

    public final String name;

    public final int position;

    public final int length;

    public final char fill;

    public final String format;

    public final boolean separator;

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

    public static Descriptor create(Field fld) {
        for (Class<? extends Annotation> ann : ANNOTATIONS)
            if (fld.isAnnotationPresent(ann))
                return create(fld.getName(), fld.getAnnotation(ann));
        throw new MirnaException(Strs.INVALID_PARAMETER, fld);
    }

    public static Descriptor create(Annotation ann) {
        return create("", ann);
    }

    public static Descriptor create(String name, Annotation ann) {
        if (ann instanceof MirnaRecord) {
            MirnaRecord mr = (MirnaRecord) ann;
            return new Descriptor(name, 0, mr.identifier().length(), (char) 0, "", false, 0, "'" + mr.identifier() + "'", LEFT, null);
        } else if (ann instanceof IntegerField) {
            IntegerField intF = (IntegerField) ann;
            return new Descriptor(name, intF.position(), intF.length(), intF.fill(), "", false, 0, "<integer>", intF.align(), IntegerConverter.class);
        } else if (ann instanceof DecimalField) {
            DecimalField decF = (DecimalField) ann;
            return new Descriptor(name, decF.position(), decF.length(), decF.fill(), "", decF.separator(), decF.decimals(), "<decimal>", decF.align(), DecimalConverter.class);
        } else if (ann instanceof DateTimeField) {
            DateTimeField dtmF = (DateTimeField) ann;
            return new Descriptor(name, dtmF.position(), dtmF.format().length(), '\0', dtmF.format(), false, 0, "<date time>", LEFT, DateTimeConverter.class);
        } else if (ann instanceof StringField) {
            StringField strF = (StringField) ann;
            return new Descriptor(name, strF.position(), strF.length(), strF.fill(), "", false, 0, "<string>", strF.align(), StringConverter.class);
        } else if (ann instanceof CustomField) {
            CustomField ctmF = (CustomField) ann;
            return new Descriptor(name, ctmF.position(), ctmF.length(), ctmF.fill(), "", false, 0, "<custom>", ctmF.align(), ctmF.converter());
        } else
            throw new MirnaException(Strs.INVALID_PARAMETER, ann);
    }

    private Descriptor(
            String name, int pos, int len, char fil, String fmt,
            boolean sep, int dec, String val, Align ali, Class<? extends Converter> con) {
        this.name = name;
        this.position = pos;
        this.length = len;
        this.fill = fil;
        this.format = fmt;
        this.separator = sep;
        this.decimals = dec;
        this.value = val;
        this.converter = con;
        this.align = ali;
    }
}
