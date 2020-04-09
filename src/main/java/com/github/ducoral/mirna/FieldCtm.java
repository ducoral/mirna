package com.github.ducoral.mirna;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface FieldCtm {

    int position();

    int length();

    char fill() default ' ';

    Align align() default Align.LEFT;

    Class<? extends Converter> converter();
}
