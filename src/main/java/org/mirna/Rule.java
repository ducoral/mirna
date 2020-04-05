package org.mirna;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static org.mirna.Primitive.*;
import static org.mirna.Strs.*;
import static org.mirna.Support.*;
import static org.mirna.Utils.generic;

final class Rule {

    static final int HEADER_ORDER = -1;

    static final int FOOTER_ORDER = -2;

    private Rule() {
    }

    static void validateDocument(Class<?> documentClass) {
        if (!documentClass.isAnnotationPresent(Document.class))
            throw new Oops(MSG_ANNOTATION_NOT_PRESENT, MSG_DOCUMENT_ANNOTATION, documentClass.getName());

        boolean hasHeader = false;
        boolean hasFooter = false;

        List<Class<?>> types = new ArrayList<>();

        for (Field field : documentClass.getDeclaredFields()) {
            Class<?> type = field.getType();

            if (isSupported(field, HEADER_FOOTER) && type == List.class) {
                throw new Oops(MSG_INVALID_CONFIGURATION, type.getName(), MSG_HEADER_FOOTER_NOT_ALLOWED);
            }

            hasHeader = validateDuplicate(field, Header.class, hasHeader, MSG_DUPLICATE_HEADER_CONFIG);
            hasFooter = validateDuplicate(field, Footer.class, hasFooter, MSG_DUPLICATE_FOOTER_CONFIG);

            if (type == List.class)
                type = generic(field);

            validateType(types, type);

            if (!isItemSupported(field))
                throw new Oops(MSG_ANNOTATION_NOT_PRESENT, MSG_ANY_ITEM_ANNOTATION, field.getName());

            validateLine(type);
        }
    }

    private static void validateType(List<Class<?>> types, Class<?> type) {
        if (types.contains(type))
            throw new Oops(MSG_INVALID_CONFIGURATION, type, MSG_DUPLICATE_TYPE_CONFIG);
        types.add(type);
        Stream.of(type.getDeclaredFields())
                .filter(Rule::isItemSupported)
                .forEach(item -> validateType(types, item.getType()));
    }

    private static boolean validateDuplicate(
            Field field,
            Class<? extends Annotation> annotation,
            boolean throwIfPresent,
            Strs exceptMessage
    ) {
        if (field.isAnnotationPresent(annotation))
            if (throwIfPresent)
                throw new Oops(MSG_INVALID_CONFIGURATION, field.getName(), exceptMessage);
            else
                return true;
        return false;
    }

    static void validateLine(Class<?> mirnaClass) {
        List<Fielded> maps = new ArrayList<>();
        fields(mirnaClass, maps::add);
        if (maps.isEmpty())
            throw new Oops(
                    MSG_MISSING_CONFIGURATION, mirnaClass.getSimpleName());

        if (maps.get(0).identifier().isEmpty())
            throw new Oops(
                    MSG_ANNOTATION_NOT_PRESENT, Line.class.getSimpleName(), mirnaClass.getSimpleName());

        if (maps.size() == 1)
            throw new Oops(
                    MSG_MISSING_FIELD_CONFIG, mirnaClass.getSimpleName());

        for (int i = 1; i < maps.size(); i++) {
            Fielded fielded = maps.get(i);
            Field field = fielded.field();
            if (isFieldSupported(field) && !isFieldTypeSupported(field))
                throw new Oops(
                        MSG_INVALID_FIELD_TYPE,
                        field.getType().getSimpleName(),
                        fielded.configuration().getSimpleName());
        }

        for (int pos = 0; pos < maps.size(); pos++) {
            int expected = maps.get(pos).position();
            if (pos > expected)
                throw new Oops(
                        MSG_DUPLICATE_POSITION_CONFIG,
                        expected,
                        maps.get(pos).field());
            else if (pos < expected)
                throw new Oops(
                        MSG_MISSING_POSITION_CONFIG,
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
        return requireNonNull(lineText).startsWith(line.identifier());
    }

    static boolean isSupported(AnnotatedElement element, Support support) {
        return support.stream().anyMatch(element::isAnnotationPresent);
    }

    static boolean isFieldSupported(AnnotatedElement element) {
        return isSupported(element, FIELD);
    }

    static boolean isItemSupported(AnnotatedElement element) {
        return isSupported(element, ITEM);
    }

    static boolean isFieldTypeSupported(Field target) {
        final Class<?> type = target.getType();
        return FIELD.stream()
                .filter(target::isAnnotationPresent)
                .allMatch(annotation -> {
                    if (annotation == FieldStr.class)
                        return STRING.stream().anyMatch(type::isAssignableFrom);
                    if (annotation == FieldInt.class)
                        return INTEGER.stream().anyMatch(type::isAssignableFrom);
                    if (annotation == FieldDec.class)
                        return DECIMAL.stream().anyMatch(type::isAssignableFrom);
                    if (annotation == FieldDtm.class)
                        return DATETIME.stream().anyMatch(type::isAssignableFrom);
                    return annotation == FieldCtm.class;
                });
    }
}
