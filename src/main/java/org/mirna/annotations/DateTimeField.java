package org.mirna.annotations;

import org.mirna.converters.Converter;
import org.mirna.converters.DateTimeConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DateTimeField {

    int pos();

    String fmt() default "ddMMyyyy";

    Class<? extends Converter> con() default DateTimeConverter.class;
}
