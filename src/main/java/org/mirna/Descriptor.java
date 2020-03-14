package org.mirna;

import org.mirna.annotations.*;
import org.mirna.converters.Converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class Descriptor {

    public final String name;

    public final int pos;

    public final int len;

    public final char fil;

    public final String fmt;

    public final boolean sep;

    public final int dec;

    public final String val;

    public final Class<? extends Converter> con;

    private static List<Class<? extends Annotation>> ANNOTATIONS = Arrays.asList(
            MirnaRecord.class, IntegerField.class, DecimalField.class,
            DateTimeField.class, StringField.class, CustomField.class);

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
            return new Descriptor(name, 0, mr.identifier().length(), (char) 0, "", false, 0, mr.identifier(), null);
        } else if (ann instanceof IntegerField) {
            IntegerField intF = (IntegerField) ann;
            return new Descriptor(name, intF.pos(), intF.len(), intF.fil(), "", false, 0, "<integer>", intF.con());
        } else if (ann instanceof DecimalField) {
            DecimalField decF = (DecimalField) ann;
            return new Descriptor(name, decF.pos(), decF.len(), decF.fil(), "", decF.sep(), decF.dec(), "<decimal>", decF.con());
        } else if (ann instanceof DateTimeField) {
            DateTimeField dtmF = (DateTimeField) ann;
            return new Descriptor(name, dtmF.pos(), dtmF.fmt().length(), '\0', dtmF.fmt(), false, 0, "<date time>", dtmF.con());
        } else if (ann instanceof StringField) {
            StringField strF = (StringField) ann;
            return new Descriptor(name, strF.pos(), strF.len(), strF.fil(), "", false, 0, "<string>", strF.con());
        } else if (ann instanceof CustomField) {
            CustomField ctmF = (CustomField) ann;
            return new Descriptor(name, ctmF.pos(), ctmF.len(), ctmF.fil(), "", false, 0, "<custom>", ctmF.con());
        } else
            throw new MirnaException(Strs.INVALID_PARAMETER, ann);
    }

    private Descriptor(
            String name, int pos, int len, char fil, String fmt,
            boolean sep, int dec, String val, Class<? extends Converter> con) {
        this.name = name;
        this.pos = pos;
        this.len = len;
        this.fil = fil;
        this.fmt = fmt;
        this.sep = sep;
        this.dec = dec;
        this.val = val;
        this.con = con;
    }
}
