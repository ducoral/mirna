package org.mirna;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

class Mapping {

    static final List<Class<? extends Annotation>> SUPPORT = Arrays.asList(
            Line.class, StringField.class, IntegerField.class,
            DecimalField.class, DateTimeField.class, CustomField.class
    );

    static final String IDENTIFIER = "identifier";
    static final String POSITION = "position";
    static final String LENGTH = "length";
    static final String FILL = "fill";
    static final String FORMAT = "format";
    static final String SEPARATOR = "separator";
    static final String DECIMALS = "decimals";
    static final String ALIGN = "align";
    static final String CONVERTER = "converter";

    private final Map<String, Object> properties = new HashMap<>();

    private Class<? extends Annotation> configuration;

    private final Field field;

    public Mapping(Object target) {
        field = target instanceof Field ? (Field) target : null;
        annotation(target, annotation -> {
            this.configuration = annotation.annotationType();
            attributes(annotation, properties::put);
        });
        if (configuration == Line.class) properties.put(LENGTH, identifier().length());
        else if (configuration == StringField.class) properties.put(CONVERTER, StringConverter.class);
        else if (configuration == IntegerField.class) properties.put(CONVERTER, IntegerConverter.class);
        else if (configuration == DecimalField.class) properties.put(CONVERTER, DecimalConverter.class);
        else if (configuration == DateTimeField.class) {
            properties.put(CONVERTER, DateTimeConverter.class);
            properties.put(LENGTH, format().length());
        }
    }

    public Class<? extends Annotation> configuration() {
        return configuration;
    }

    public void put(String key, Object value) {
        properties.put(key, value);
    }

    public boolean isEmpty() {
        return properties.isEmpty();
    }

    public String identifier() {
        return (String) properties.getOrDefault(IDENTIFIER, "");
    }

    public Field field() {
        return field;
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

    public Converter converter() {
        Class<?> converterClass = (Class<?>) properties.get(CONVERTER);
        try {
            if (configuration == CustomField.class)
                return new CustomConverter(this, (Converter) converterClass.newInstance());
            else
                return (Converter) converterClass.getConstructor(Mapping.class).newInstance(this);
        } catch (Exception e) {
            throw new MirnaException(e.getMessage(), e);
        }
    }

    static boolean isMapped(AnnotatedElement element) {
        return SUPPORT.stream().anyMatch(element::isAnnotationPresent);
    }

    static boolean isTypeSupported(Field target) {
        final Class<?> type = target.getType();
        return SUPPORT.stream()
                .filter(target::isAnnotationPresent)
                .allMatch(annotation -> {
                    if (annotation == StringField.class)
                        return Stream.of(Character.TYPE, Character.class, String.class).anyMatch(type::isAssignableFrom);
                    if (annotation == IntegerField.class)
                        return Stream
                                .of(Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE,
                                    Byte.class, Short.class, Integer.class, Long.class, BigInteger.class)
                                .anyMatch(type::isAssignableFrom);
                    if (annotation == DecimalField.class)
                        return Stream
                                .of(Float.TYPE, Float.class, Double.TYPE, Double.class, BigDecimal.class)
                                .anyMatch(type::isAssignableFrom);
                    if (annotation == DateTimeField.class)
                        return Stream.of(Date.class).anyMatch(type::isAssignableFrom);
                    return annotation == CustomField.class;
                });
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
