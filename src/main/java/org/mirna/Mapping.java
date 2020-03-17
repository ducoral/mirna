package org.mirna;

import org.mirna.annotations.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Mapping {

    static final List<Class<? extends Annotation>> SUPPORT = Arrays.asList(
            MirnaRecord.class, StringField.class, IntegerField.class,
            DecimalField.class, DateTimeField.class, CustomField.class
    );

    public static final String IDENTIFIER = "identifier";
    public static final String FIELD = "field";
    public static final String POSITION = "position";
    public static final String LENGTH = "length";
    public static final String FILL = "fill";
    public static final String FORMAT = "format";
    public static final String SEPARATOR = "separator";
    public static final String DECIMALS = "decimals";
    public static final String ALIGN = "align";
    public static final String CONVERTER = "converter";

    private Map<String, Object> properties = new HashMap<>();

    private Object target;

    public Mapping(Object target) {
        this.target = target;
        annotation(target, annotation -> attributes(annotation, (name, value) -> properties.put(name, value)));
    }

    public boolean isEmpty() {
        return properties.isEmpty();
    }

    public Object target() {
        return target;
    }

    public String identifier() {
        return (String) properties.getOrDefault(IDENTIFIER, "");
    }

    public String field() {
        return (String) properties.getOrDefault(FIELD, "");
    }

    public int position() {
        return (int) properties.getOrDefault(POSITION, 0);
    }

    public int length() {
        return (int) properties.getOrDefault(LENGTH, 0);
    }

    public char fill() {
        return (char) properties.getOrDefault(FILL, '\0');
    }

    public String format() {
        return (String) properties.getOrDefault(FORMAT, "");
    }

    public char separator() {
        return (char) properties.getOrDefault(SEPARATOR, '\0');
    }

    public int decimals() {
        return (int) properties.getOrDefault(DECIMALS, 0);
    }

    public Align align() {
        return (Align) properties.getOrDefault(ALIGN, Align.LEFT);
    }

    public Class<?> converter() {
        return (Class<?>) properties.getOrDefault(CONVERTER, null);
    }

    public Mapping put(String property, Object value) {
        properties.put(property, value);
        return this;
    }

    static void attributes(Annotation annotation, BiConsumer<String, Object> action) {
        Arrays.stream(annotation.annotationType().getDeclaredMethods()).forEach(method -> {
            try {
                action.accept(method.getName(), method.invoke(annotation));
            } catch (Exception e) {
                throw new MirnaException(e.getMessage(), e);
            }
        });
    }

    static void annotation(Object target, Consumer<Annotation> action) {
        AnnotatedElement element = target instanceof AnnotatedElement
                ? (AnnotatedElement) target
                : target.getClass();
        SUPPORT.forEach(annotation -> {
            if (element.isAnnotationPresent(annotation))
                action.accept(element.getAnnotation(annotation));
        });
    }
}
