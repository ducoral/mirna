package org.mirna;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mirna.Utils.*;

public final class Mirna {

    private final Map<Class<?>, Linner> linners = new HashMap<>();

    private final List<Object> lines = new ArrayList<>();

    Mirna() {
    }

    public static void write(Object document, Writer writer) {
        Rule.validateDocument(document.getClass());
        Mirna mirna = new Mirna();
        new Documented(document).lines(mirna::register);
        mirna.write(writer);
    }

    public static <T> T read(Class<T> documentClass, Reader reader) {
        try {
            Mirna mirna = new Mirna();
            T document = documentClass.newInstance();
            new Documented(document).types(mirna::register).parse(mirna.lines);
            return document;
        } catch (Exception e) {
            throw new Oops(e.getMessage(), e);
        }
    }

    void write(Writer writer) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            for (Object line : lines) {
                bufferedWriter.write(linners.get(line.getClass()).toText(line));
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            throw new Oops(e.getMessage(), e);
        }
    }

    List<?> read(Reader reader) {
        List<Object> list = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            int line = 0;
            String text = bufferedReader.readLine();
            while (text != null) {
                line++;
                Linner linner = liner(text);
                if (linner == null)
                    throw new Oops(Strs.MSG_UNMAPPED_LINE, line, text);
                try {
                    list.add(linner.fromText(text));
                } catch (Exception e) {
                    throw new Oops(Strs.MSG_ERROR_PARSING_LINE, line);
                }
                text = bufferedReader.readLine();
            }
        } catch (IOException e) {
            throw new Oops(e.getMessage(), e);
        }
        return list;
    }

    Linner liner(String text) {
        for (Map.Entry<Class<?>, Linner> entry : linners.entrySet())
            if (Rule.match(entry.getKey(), text))
                return entry.getValue();
        return null;
    }

    void register(Object line) {
        lines.add(line);
        register(line.getClass());
    }

    void register(Class<?> lineClass) {
        if (linners.containsKey(lineClass))
            return;
        Rule.validateLine(lineClass);
        Line config = lineClass.getAnnotation(Line.class);
        for (Map.Entry<Class<?>, Linner> entry : linners.entrySet())
            if (config.identifier().equals(entry.getValue().identifier()))
                throw new Oops(
                        Strs.MSG_DUPLICATE_LINE_IDENTIFIER,
                        config.identifier(), lineClass.getSimpleName(), entry.getKey().getSimpleName());
        linners.put(lineClass, new Linner(lineClass));
    }

    public static void report(Class<?> documentClass) {
        String str = fixRight("(v" + Strs.VERSION.toString() + ")", 8, ' ');
        str =
                "              _                  \n" +
                "    _ __ ___ (_)_ __ _ __   __ _ \n" +
                "   | '_ ` _ \\| | '__| '_ \\ / _` |\n" +
                "   | | | | | | | |  | | | | (_| |\n" +
                "   |_| |_| |_|_|_|  |_| |_|\\__,_|\n" +
                "   #t#:: flat-file parser ::" + str + "#0#\n\n" +
                "=== #p#" + fixLeft(Strs.REPORT.toString() + "#0# ", 34, '=') + "\n\n";
        print(str);

        List<Class<?>> items = new ArrayList<>();
    }

    public static void main(String[] args) {
        report(Temp.class);
        new Documented(new Temp()).types(Mirna::printclass);
    }

    private static void printclass(Class<?> type) {
        System.out.println(type);
    }

    @Document
    static class Temp {

        @Header
        Line1 line1;

        @Item
        List<Line2> line2s;

        @Footer
        Line3 line3;
    }

    @Line(identifier = "line1")
    static class Line1 {

    }

    @Line(identifier = "line2")
    static class Line2 {

    }

    @Line(identifier = "line3")
    static class Line3 {

        @Item
        Line4 line4;
    }

    @Line(identifier = "line4")
    static class Line4 {

    }

}
