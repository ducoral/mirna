package org.mirna;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.mirna.Utils.generic;

final class Rule {
    static final int HEADER_ORDER = -1;
    static final int FOOTER_ORDER = -2;

    static final List<Class<? extends Annotation>> FIELD_SUPPORT =
            Arrays.asList(Line.class, FieldStr.class, FieldInt.class, FieldDec.class, FieldDtm.class, FieldCtm.class);

    static final List<Class<? extends Annotation>> ITEM_SUPPORT =
            Arrays.asList(Header.class, Footer.class, Item.class);

    private Rule() {
    }

    static void validateDocument(Class<?> documentClass) {
        boolean hasHeader = false;
        boolean hasFooter = false;
        for (Field field : documentClass.getDeclaredFields()) {
            Class<?> type = field.getType();
            if (isSupported(type, Arrays.asList(Header.class, Footer.class)) && type == List.class)
                throw new Oops(Strs.MSG_INVALID_CONFIGURATION, type.getName(), Strs.MSG_HEADER_FOOTER_NOT_ALLOWED);
            if (type == List.class)
                type = generic(field);
            if (!isItemSupported(type))
                throw new Oops(Strs.MSG_ANNOTATION_NOT_PRESENT, Strs.MSG_ANY_ITEM_ANNOTATION, field.getName());
            validateLine(type);
        }
    }

    static void validateLine(Class<?> mirnaClass) {
        List<Fielded> maps = new ArrayList<>();
        fields(mirnaClass, maps::add);
        if (maps.isEmpty())
            throw new Oops(
                    Strs.MSG_MISSING_CONFIGURATION, mirnaClass.getSimpleName());

        if (maps.get(0).identifier().isEmpty())
            throw new Oops(
                    Strs.MSG_ANNOTATION_NOT_PRESENT, Line.class.getSimpleName(), mirnaClass.getSimpleName());

        if (maps.size() == 1)
            throw new Oops(
                    Strs.MSG_MISSING_FIELD_CONFIG, mirnaClass.getSimpleName());

        for (int i = 1; i < maps.size(); i++) {
            Fielded fielded = maps.get(i);
            Field field = fielded.field();
            if (isFieldSupported(field) && !isFieldTypeSupported(field))
                throw new Oops(
                        Strs.MSG_INVALID_FIELD_TYPE,
                        field.getType().getSimpleName(),
                        fielded.configuration().getSimpleName());
        }

        for (int pos = 0; pos < maps.size(); pos++) {
            int expected = maps.get(pos).position();
            if (pos > expected)
                throw new Oops(
                        Strs.MSG_DUPLICATE_POSITION_CONFIG,
                        expected,
                        maps.get(pos).field());
            else if (pos < expected)
                throw new Oops(
                        Strs.MSG_MISSING_POSITION_CONFIG,
                        pos,
                        maps.get(pos).field());
        }
    }

    static void fields(Class<?> LineClass, Consumer<Fielded> action) {
        action.accept(new Fielded(LineClass));
        Arrays.stream(LineClass.getDeclaredFields())
                .filter(Rule::isFieldSupported)
                .forEach(field -> action.accept(new Fielded(field)));
    }

    static boolean match(Class<?> LineClass, String lineText) {
        Line line = Objects.requireNonNull(LineClass.getAnnotation(Line.class));
        return Objects.requireNonNull(lineText).startsWith(line.identifier());
    }

    static boolean isSupported(AnnotatedElement element, List<Class<? extends Annotation>> support) {
        return support.stream().anyMatch(element::isAnnotationPresent);
    }

    static boolean isFieldSupported(AnnotatedElement element) {
        return isSupported(element, FIELD_SUPPORT);
    }

    static boolean isItemSupported(AnnotatedElement element) {
        return isSupported(element, ITEM_SUPPORT);
    }

    static boolean isFieldTypeSupported(Field target) {
        final Class<?> type = target.getType();
        return FIELD_SUPPORT
                .stream()
                .filter(target::isAnnotationPresent)
                .allMatch(annotation -> {
                    if (annotation == FieldStr.class)
                        return Stream.of(Character.TYPE, Character.class, String.class).anyMatch(type::isAssignableFrom);
                    if (annotation == FieldInt.class)
                        return Stream
                                .of(Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE,
                                    Byte.class, Short.class, Integer.class, Long.class, BigInteger.class)
                                .anyMatch(type::isAssignableFrom);
                    if (annotation == FieldDec.class)
                        return Stream
                                .of(Float.TYPE, Float.class, Double.TYPE, Double.class, BigDecimal.class)
                                .anyMatch(type::isAssignableFrom);
                    if (annotation == FieldDtm.class)
                        return Stream.of(Date.class).anyMatch(type::isAssignableFrom);
                    return annotation == FieldCtm.class;
                });
    }
}
