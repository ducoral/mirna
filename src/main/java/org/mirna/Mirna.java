package org.mirna;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static org.mirna.Utils.*;

public final class Mirna {

    @Line(identifier = "id1")
    static class Linha1 { }

    @Line(identifier = "id2detail")
    static class Linha2Detail {
    }

    @Line(identifier = "id2")
    static class Linha2 {
        @Detail(order = 2)
        Linha2Detail detail;
    }

    @Line(identifier = "id3")
    static class Linha3 { }

    @Document
    static class Teste {
        @Header
        Linha1 header;

        @Item
        List<Linha2> detalhes;

        @Item(order = 2)
        Linha2Detail detail;

        @Footer
        Linha3 footer;
    }

    public static void main(String[] args) throws Exception {
        report(null);
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

    private final Map<Class<?>, Parser> parsers = new HashMap<>();

    private final List<Object> Lines = new ArrayList<>();

    Mirna() {
    }


    void writeRecords(Writer writer) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            for (Object Line : Lines) {
                bufferedWriter.write(parsers.get(Line.getClass()).toText(Line));
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            throw new MirnaException(e.getMessage(), e);
        }
    }

    List<Object> readRecords(Reader reader) {
        List<Object> list = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            int line = 0;
            String text = bufferedReader.readLine();
            while (text != null) {
                line++;
                Parser parser = getParser(text);
                if (parser == null)
                    throw new MirnaException(Strs.MSG_UNMAPPED_LINE, line, text);
                try {
                    list.add(parser.fromText(text));
                } catch (Exception e) {
                    throw new MirnaException(Strs.MSG_ERROR_PARSING_LINE, line);
                }
                text = bufferedReader.readLine();
            }
        } catch (IOException e) {
            throw new MirnaException(e.getMessage(), e);
        }
        return list;
    }

    Parser getParser(String text) {
        for (Map.Entry<Class<?>, Parser> entry : parsers.entrySet())
            if (Utils.match(entry.getKey(), text))
                return entry.getValue();
        return null;
    }

    void register(Object Line) {
        Lines.add(Line);
        register(Line.getClass());
    }

    void register(Class<?> LineClass) {
        if (parsers.containsKey(LineClass))
            return;
        validate(LineClass);
        Line config = LineClass.getAnnotation(Line.class);
        for (Map.Entry<Class<?>, Parser> entry : parsers.entrySet())
            if (config.identifier().equals(entry.getValue().identifier()))
                throw new MirnaException(
                        Strs.MSG_DUPLICATE_LINE_IDENTIFIER,
                        config.identifier(), LineClass.getSimpleName(), entry.getKey().getSimpleName());
        parsers.put(LineClass, new Parser(LineClass));
    }

    public static void report(Class<?> documentClass) {
        String str = fixRight("(v" + Strs.VERSION.toString() + ")", 8, ' ');
        str =
                "              _                  \n" +
                "    _ __ ___ (_)_ __ _ __   __ _ \n" +
                "   | '_ ` _ \\| | '__| '_ \\ / _` |\n" +
                "   | | | | | | | |  | | | | (_| |\n" +
                "   |_| |_| |_|_|_|  |_| |_|\\__,_|\n" +
                "   #t#:: flat-file parser ::#c#" + str +"#0#\n\n" +
                "=== #p#" + fixLeft(Strs.REPORT.toString() + "#0# ", 33, '=') + "\n\n";

        print(str);
    }

    private static void print(String text) {
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
}
