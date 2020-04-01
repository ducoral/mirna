package org.mirna;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

import static org.mirna.Utils.*;

class Documented {

    @Line(identifier = "id1")
    static class Linha1 {
    }

    @Line(identifier = "id2detail")
    static class Linha2Detail {
    }

    @Line(identifier = "id2")
    static class Linha2 {
        @Item(order = 2)
        Linha2Detail detail;
    }

    @Line(identifier = "id3")
    static class Linha3 {
    }

    @Document
    static class Teste {
        @Header
        Linha1 header;

        @Item
        List<Linha2> detalhes;

        @Item(order = 1)
        Linha2Detail detail;

        @Footer
        Linha3 footer;
    }

    public static void main(String[] args) {
        List<String> items1 = Arrays.asList("um", "dois", "tres");
        List<String> items2 = Arrays.asList("um", "dois", "tres");

        System.out.println(items1.equals(items2));
        System.out.println(Objects.deepEquals(items1, items2));
    }

    public static void main3(String[] args) throws Exception {
        Annotation header = Teste.class.getDeclaredField("header").getAnnotation(Header.class).annotationType().getAnnotation(Item.class);
        Annotation footer = Teste.class.getDeclaredField("footer").getAnnotation(Footer.class).annotationType().getAnnotation(Item.class);

        System.out.println(header);
        System.out.println(footer);
    }

    public static void main2(String[] args) throws Exception {
        Field field = Teste.class.getDeclaredField("detalhes");
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        Type typeArgument = type.getActualTypeArguments()[0];
        if (typeArgument instanceof Class<?>) {
            Class<?> genericType = (Class<?>) typeArgument;
            System.out.println(genericType);
            System.out.println(genericType.isAnnotationPresent(Line.class));
        } else {
            System.out.println("não é");
        }
    }

    private final Object instance;

    private final List<Field> items = new ArrayList<>();

    Documented(Object instance) {
        this.instance = instance;
        Arrays.stream(instance.getClass().getDeclaredFields()).forEach(this::add);
    }

    List<Object> getLines() {
        List<Object> lines = new ArrayList<>();
        items.forEach(item -> loadLines(lines, item));
        return lines;
    }

    private void loadLines(List<Object> lines, Field field) {
        Object value = getValue(instance, field);
        if (value == null)
            return;
        if (value instanceof List) {
            ((List<?>) value).forEach(item -> lines.addAll(new Documented(item).getLines()));
        } else
            lines.add(value);
    }

    void setLines(List<Object> lines) {

    }

    boolean hasHeader() {
        return index(Rule.HEADER_ORDER) > -1;
    }

    boolean hasFooter() {
        return index(Rule.FOOTER_ORDER) > -1;
    }

    private int index(int order) {
        for (int index = 0; index < items.size(); index++)
            if (item(items.get(index)).order() == order)
                return index;
        return -1;
    }

    private void add(Field field) {
        Item item = item(field);
        if (item == null)
            return;
        int index = 0;
        while (index < items.size()) {
            Item compared = item(items.get(index));
            if (compared != null && item.order() < compared.order())
                break;
            index++;
        }
        items.add(index, field);
    }

    private Item item(AnnotatedElement element) {
        if (element.isAnnotationPresent(Header.class))
            return item(element.getAnnotation(Header.class).annotationType());
        else if (element.isAnnotationPresent(Footer.class))
            return item(element.getAnnotation(Footer.class).annotationType());
        else
            return element.getAnnotation(Item.class);
    }
}
