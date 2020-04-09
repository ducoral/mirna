package com.github.ducoral.mirna;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

class Fielded {

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

    public Fielded(Object target) {
        field = target instanceof Field ? (Field) target : null;
        Utils.fieldAnnotations(target, annotation -> {
            this.configuration = annotation.annotationType();
            Utils.attributes(annotation, properties::put);
        });
        if (configuration == Line.class) properties.put(LENGTH, identifier().length());
        else if (configuration == FieldStr.class) properties.put(CONVERTER, ConverterStr.class);
        else if (configuration == FieldInt.class) properties.put(CONVERTER, ConverterInt.class);
        else if (configuration == FieldDec.class) properties.put(CONVERTER, ConverterDec.class);
        else if (configuration == FieldDtm.class) {
            properties.put(CONVERTER, ConverterDtm.class);
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
            if (configuration == FieldCtm.class)
                return new ConverterCtm(this, (Converter) converterClass.newInstance());
            else
                return (Converter) converterClass.getConstructor(Fielded.class).newInstance(this);
        } catch (Exception e) {
            throw new Oops(e.getMessage(), e);
        }
    }
}
