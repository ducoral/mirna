package com.github.ducoral.mirna;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldInt {

    int position();

    int length();

    char fill() default ' ';

    Align align() default Align.RIGHT;
}
