package com.github.ducoral.mirna;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface FieldDec {

    int position();

    int length();

    char fill() default ' ';

    int decimals() default 2;

    char separator() default '\0';

    Align align() default Align.RIGHT;
}
