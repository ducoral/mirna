package org.mirna.annotations;

import org.mirna.converters.Converter;
import org.mirna.converters.DecimalConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DecimalField {

    int pos();

    int len();

    char fil() default ' ';

    int dec() default 2;

    boolean sep() default false;

    Class<? extends Converter> con() default DecimalConverter.class;
}
