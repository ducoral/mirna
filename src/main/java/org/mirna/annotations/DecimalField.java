package org.mirna.annotations;

import org.mirna.core.Align;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface DecimalField {

    int position();

    int length();

    char fill() default ' ';

    int decimals() default 2;

    char separator() default '\0';

    Align align() default Align.RIGHT;
}
