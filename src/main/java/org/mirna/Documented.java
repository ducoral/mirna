package org.mirna;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.mirna.Utils.getValue;
import static org.mirna.Utils.setValue;

class Documented<T> {

    private final T instance;

    private final List<Field> items = new ArrayList<>();

    Documented(Class<T> documentClass) {
        this(instantiate(documentClass));
    }

    Documented(T document) {
        this.instance = document;
        Arrays.stream(document.getClass().getDeclaredFields()).forEach(this::add);
    }

    Documented<T> types(Consumer<Class<?>> action) {
        items(item -> types(item, action));
        return this;
    }

    private void types(Object item, Consumer<Class<?>> action) {
        if (item instanceof Field)
            if (((Field) item).getType() == List.class)
                types(((ParameterizedType) ((Field) item).getGenericType()).getActualTypeArguments()[0], action);
            else
                types(((Field) item).getType(), action);
        else if (item instanceof Class<?>) {
            action.accept((Class<?>) item);
            new Documented<>((Class<?>) item).types(action);
        }
    }

    void lines(Consumer<Object> action) {
        items(item -> lines(item, action));
    }

    private void lines(Object item, Consumer<Object> action) {
        if (item instanceof Field)
            item = getValue(instance, (Field) item);
        if (item == null)
            return;
        if (item instanceof List) {
            List<?> subItems = (List<?>) item;
            subItems.forEach(subItem -> lines(subItem, action));
        } else {
            action.accept(item);
            new Documented<>(item).lines(action);
        }
    }

    void accept(Object line) {
        // TODO implementing accept

        throw new Oops("parse exception");
    }

    T parse(List<Object> lines) {
        initialize(instance);
        lines.forEach(this::accept);
        return instance;
    }

    boolean hasHeader() {
        return index(Rule.HEADER_ORDER) > -1;
    }

    boolean hasFooter() {
        return index(Rule.FOOTER_ORDER) > -1;
    }

    private void items(Consumer<Field> action) {
        int headerIndex = index(Rule.HEADER_ORDER);
        if (headerIndex > -1)
            action.accept(items.get(headerIndex));
        for (int index = headerIndex + 1; index < items.size(); index++)
            action.accept(items.get(index));
        int footerIndex = index(Rule.FOOTER_ORDER);
        if (footerIndex > -1)
            action.accept(items.get(footerIndex));
    }

    private int index(int order) {
        for (int index = 0; index < items.size(); index++)
            if (getAnnotation(items.get(index)).order() == order)
                return index;
        return -1;
    }

    private void add(Field field) {
        Item item = getAnnotation(field);
        if (item == null)
            return;
        int index = 0;
        while (index < items.size()) {
            Item compared = getAnnotation(items.get(index));
            if (compared != null && item.order() < compared.order())
                break;
            index++;
        }
        items.add(index, field);
    }

    private Item getAnnotation(AnnotatedElement element) {
        if (element.isAnnotationPresent(Header.class))
            return getAnnotation(element.getAnnotation(Header.class).annotationType());
        else if (element.isAnnotationPresent(Footer.class))
            return getAnnotation(element.getAnnotation(Footer.class).annotationType());
        else
            return element.getAnnotation(Item.class);
    }

    private static <T> T instantiate(Class<T> documentClass) {
        try {
            return documentClass.newInstance();
        } catch (Exception e) {
            throw new Oops(e.getMessage(), e);
        }
    }

    private static void initialize(Object instance) {
        Rule.items(instance, item -> setValue(instance, item, item.getType() == List.class ? new ArrayList<>() : null));
    }
}
