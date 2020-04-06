package org.mirna;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.mirna.Utils.*;

class Documented {

    static final int INDENT = 4;

    private final Object document;

    private final List<Field> items = new ArrayList<>();

    Documented(Object document) {
        this.document = document;
        Arrays.stream(document.getClass().getDeclaredFields()).forEach(this::add);
    }

    Documented types(Consumer<Class<?>> action) {
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
            new Documented(create((Class<?>) item)).types(action);
        }
    }

    void lines(Consumer<Object> action) {
        items(item -> lines(item, action));
    }

    private void lines(Object item, Consumer<Object> action) {
        if (item instanceof Field)
            item = get(document, (Field) item);
        if (item == null)
            return;
        if (item instanceof List) {
            List<?> subItems = (List<?>) item;
            subItems.forEach(subItem -> lines(subItem, action));
        } else {
            action.accept(item);
            new Documented(item).lines(action);
        }
    }

    @SuppressWarnings("unchecked")
    private boolean accept(Field field, Object line) {
        Class<?> type = line.getClass();
        if (field.getType() == type)
            if (isNull(document, field)) {
                set(document, field, line);
                return true;
            } else
                throw new Oops(Strs.MSG_INVALID_LINE, line);
        else if (field.getType() == List.class) {
            if (type == generic(field)) {
                List<Object> list = (List<Object>) get(document, field);
                if (list == null) {
                    list = new ArrayList<>();
                    set(document, field, list);
                }
                list.add(line);
                return true;
            }
        } else if (!isNull(document, field)) {
            Documented documented = new Documented(get(document, field));
            List<Field> items = new ArrayList<>();
            documented.items(items::add);
            for (Field item : items)
                if (documented.accept(item, line))
                    return true;
        }
        return false;
    }

    private void accept(Object line) {
        boolean accepted = false;
        List<Field> fields = new ArrayList<>();
        items(fields::add);
        for (Field field : fields)
            if (accept(field, line)) {
                accepted = true;
                break;
            }
        if (!accepted)
            throw new Oops(Strs.MSG_INVALID_LINE, line);
    }

    void parse(List<?> lines) {
        items(item -> set(document, item, item.getType() == List.class ? new ArrayList<>() : null));
        int position = 0;
        try {
            for (Object line : lines) {
                position++;
                accept(line);
            }
        } catch (Exception e) {
            throw new Oops(e, Strs.MSG_ERROR_PARSING_LINE, position);
        }
    }

    void report(int indent, Consumer<String[]> action) {
        items(field -> report(indent, field, action));
    }

    void report(int indent, Field field, Consumer<String[]> action) {
        String desc = "#y#"
                + (field.getType() == List.class ? "list<" : "")
                + (field.isAnnotationPresent(Header.class)
                        ? "header"
                        : field.isAnnotationPresent(Footer.class)
                            ? "footer"
                            : "item")
                + (field.getType() == List.class ? ">" : "")
                + "#0#";
        report(indent, field.getType() == List.class ? generic(field) : field.getType(), desc, action);
    }

    private void report(int indent, Class<?> type, String desc, Consumer<String[]> action) {
        action.accept(new String[]{chars(indent, ' '), type.getName(), " ", desc, "\n"});
        new Documented(create(type)).report(indent + INDENT, action);
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
}
