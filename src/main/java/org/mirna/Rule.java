package org.mirna;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

final class Rule {

    static final int HEADER_ORDER = -1;
    static final int FOOTER_ORDER = -2;

    private Rule() {
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
            if (Fielded.isMapped(field) && !Fielded.isTypeSupported(field))
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
                .filter(Fielded::isMapped)
                .forEach(field -> action.accept(new Fielded(field)));
    }

    static boolean match(Class<?> LineClass, String lineText) {
        Line line = Objects.requireNonNull(LineClass.getAnnotation(Line.class));
        return Objects.requireNonNull(lineText).startsWith(line.identifier());
    }
}
