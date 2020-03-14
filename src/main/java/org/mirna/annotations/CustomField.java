package org.mirna.annotations;

import org.mirna.Align;
import org.mirna.converters.Converter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface CustomField {

    int position();

    int length();

    char fill() default ' ';

    Align align() default Align.LEFT;

    Class<? extends Converter> converter();
}
