package org.mirna.annotations;

import org.mirna.converters.Converter;
import org.mirna.converters.IntegerConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IntegerField {

    int pos();

    int len();

    char fil() default ' ';

    Class<? extends Converter> con() default IntegerConverter.class;
}
