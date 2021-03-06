package com.github.ducoral.mirna;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

final class Utils {

    private Utils() {
    }

    static String chars(int length, char fill) {
        return new String(new char[length]).replace('\0', fill);
    }

    static String fixStr(String str, int len, char fil, Align ali) {
        return ali == Align.RIGHT ? fixRight(str, len, fil) : fixLeft(str, len, fil);
    }

    static String fixRight(String str, int length, char fill) {
        return str.length() >= length
                ? str.substring(0, length)
                : chars(length - str.length(), fill) + str;
    }

    static String fixLeft(String str, int length, char fill) {
        return str.length() >= length
                ? str.substring(str.length() - length)
                : str + chars(length - str.length(), fill);
    }

    static List<String> strList(Object... objects) {
        List<String> list = new ArrayList<>();
        Arrays.stream(objects).forEach(o -> list.add(String.valueOf(o)));
        return list;
    }

    static ResourceBundle resource() {
        ClassLoader loader = Utils.class.getClassLoader();
        URL resource = loader.getResource(String.format("strs_%s.properties", Locale.getDefault()));
        if (resource == null)
            resource = loader.getResource("strs.properties");
        if (resource == null)
            throw new Oops("Could not load message file");
        try {
            return new PropertyResourceBundle(new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new Oops(e.getMessage(), e);
        }
    }

    static void set(Object instance, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(instance, value);
        } catch (Exception e) {
            throw new Oops(e.getMessage(), e);
        }
    }

    static Object get(Object instance, Field field) {
        try {
            field.setAccessible(true);
            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw new Oops(e.getMessage(), e);
        }
    }

    static boolean isNull(Object instance, Field field) {
        return get(instance, field) == null;
    }

    static Class<?> generic(Field field) {
        return (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
    }

    static void attributes(Annotation annotation, BiConsumer<String, Object> action) {
        Arrays.stream(annotation.annotationType().getDeclaredMethods()).forEach(method -> {
            try {
                action.accept(method.getName(), method.invoke(annotation));
            } catch (Exception e) {
                throw new Oops(e.getMessage(), e);
            }
        });
    }

    static void fieldAnnotations(Object target, Consumer<Annotation> action) {
        AnnotatedElement element = target instanceof AnnotatedElement
                ? (AnnotatedElement) target
                : target.getClass();
        Support.FIELD.stream().forEach(annotation -> {
            if (element.isAnnotationPresent(annotation))
                action.accept(element.getAnnotation(annotation));
        });
    }

    static void print(String... texts) {
        StringBuilder str = new StringBuilder();
        for (String s : texts)
            str.append(s);
        String text = str.toString();
        List<String> colors = Arrays.asList(
                "#0#", "\033[0m",       // reset
                "#r#", "\033[38;5;9m",  // red
                "#g#", "\033[38;5;10m", // greem
                "#y#", "\033[38;5;11m", // yellow
                "#b#", "\033[38;5;12m", // blue
                "#p#", "\033[38;5;13m", // pink
                "#c#", "\033[38;5;14m", // cyan
                "#t#", "\033[38;5;6m",  // teal
                "#s#", "\033[38;5;7m",  // silver
                "#a#", "\033[38;5;8m"); // gray
        for (int index = 0; index < colors.size() - 1; index += 2)
            text = text.replaceAll(colors.get(index), colors.get(index + 1));
        System.out.print(text);
    }

    static <T> T create(Class<T> type) {
        try {
            return type.newInstance();
        } catch (Exception e) {
            throw new Oops(e.getMessage(), e);
        }
    }

    static String toString(Object line) {
        Class<?> type = line.getClass();
        if (type.isAnnotationPresent(Line.class)) {
            StringBuilder desc = new StringBuilder(type.getSimpleName() + "{");
            for (Field field : type.getDeclaredFields())
                desc.append(desc.charAt(desc.length() - 1) == '{' ? "" : ", ")
                        .append(field.getName())
                        .append(": ")
                        .append(toString(line, field));
            return desc.append('}').toString();
        } else
            return String.valueOf(line);
    }

    private static String toString(Object line, Field field) {
        Object value = get(line, field);
        if (value instanceof List) {
            List<?> items = (List<?>) value;
            StringBuilder str = new StringBuilder("[");
            if (!items.isEmpty()) {
                str.append(toString(items.get(0)));
                for (int index = 1; index < items.size(); index++)
                    str.append(", ").append(toString(items.get(index)));
            }
            return str.append(']').toString();
        } else if (Rule.isItemSupported(field))
            return toString(value);
        else
            return String.valueOf(value);
    }
}
