package org.mirna;

import java.io.*;
import java.util.*;

import static org.mirna.Align.LEFT;
import static org.mirna.Align.RIGHT;
import static org.mirna.Rule.*;
import static org.mirna.Strs.*;
import static org.mirna.Utils.*;

public final class Mirna {

    private final Map<Class<?>, Linner> linners = new HashMap<>();

    private final List<Object> lines = new ArrayList<>();

    Mirna() {
    }

    public static void write(Object document, Writer writer) {
        Objects.requireNonNull(document);
        Objects.requireNonNull(writer);
        validateDocument(document.getClass());
        Mirna mirna = new Mirna();
        new Documented(document).lines(mirna::register);
        mirna.write(writer);
    }

    public static <T> T read(Class<T> documentClass, Reader reader) {
        Objects.requireNonNull(documentClass);
        Objects.requireNonNull(reader);
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
                    throw new Oops(MSG_UNMAPPED_LINE, line, text);
                try {
                    list.add(linner.fromText(text));
                } catch (Exception e) {
                    throw new Oops(MSG_ERROR_PARSING_LINE, line);
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
            if (match(entry.getKey(), text))
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
        validateLine(lineClass);
        Line config = lineClass.getAnnotation(Line.class);
        for (Map.Entry<Class<?>, Linner> entry : linners.entrySet())
            if (config.identifier().equals(entry.getValue().identifier()))
                throw new Oops(
                        MSG_DUPLICATE_LINE_IDENTIFIER,
                        config.identifier(), lineClass.getSimpleName(), entry.getKey().getSimpleName());
        linners.put(lineClass, new Linner(lineClass));
    }

    private static List<List<String>> table(Class<?> lineClass) {
        List<List<String>> table = new ArrayList<>();
        table.add(Arrays.asList(
                REPORT_FIELD.toString(), REPORT_FROM.toString(), REPORT_TO.toString(),
                REPORT_SIZE.toString(), REPORT_VALUE.toString()));
        int from = 1;
        for (Fielded fielded : new Linner(lineClass).fieldeds) {
            String name = fielded.field() == null ? LINE_IDENTIFIER.toString() : fielded.field().getName();
            String value = fielded.field() == null ? fielded.identifier() : fielded.field().getType().getSimpleName();
            table.add(Arrays.asList(
                    name,
                    String.valueOf(from),
                    String.valueOf(from + fielded.length() - 1),
                    String.valueOf(fielded.length()),
                    value));
            from += fielded.length();
        }
        return table;
    }

    private static int[] lengths(List<List<String>> table) {
        int[] lengths = {0, 0, 0, 0, 0};
        for (List<String> row : table)
            for (int col = 0; col < lengths.length; col++)
                lengths[col] = Math.max(lengths[col], row.get(col).length() + 2);
        return lengths;
    }

    public static void report(Class<?> documentClass) {
        validateDocument(documentClass);

        String str = fixRight("(v" + VERSION.toString() + ")", 8, ' ');
        str =
                "              _\n" +
                "    _ __ ___ (_)_ __ _ __   __ _\n" +
                "   | '_ ` _ \\| | '__| '_ \\ / _` |\n" +
                "   | | | | | | | |  | | | | (_| |\n" +
                "   |_| |_| |_|_|_|  |_| |_|\\__,_|\n" +
                "   #t#:: flat-file parser ::" + str + "#0#\n\n" +
                "=== #p#" + fixLeft(REPORT.toString() + "#0# ", 34, '=') + "\n\n";
        print(str);

        Documented documented = new Documented(create(documentClass));
        print("#c#", documentClass.getName(), "#y# document#0#\n");
        documented.report(Documented.INDENT, Utils::print);
        print("\n");

        List<Class<?>> types = new ArrayList<>();
        documented.types(types::add);
        for (Class<?> type : types) {
            List<List<String>> table = table(type);
            int[] lengths = lengths(table);
            Align[] aligns = {LEFT, RIGHT, RIGHT, RIGHT, LEFT};
            int width = Arrays.stream(lengths).reduce(lengths.length - 1, Integer::sum);

            print("+", chars(width, '-'), "+\n");
            print("|#g#", fixLeft(" " + type.getName(), width, ' '), "#0#|\n");
            printRow(lengths);

            List<String> row = table.get(0);
            print("|");
            for (int index = 0; index < row.size(); index++)
                print("#b#", fixStr(" " + row.get(index) + " ", lengths[index], ' ', aligns[index]) + "#0#|");
            print("\n");
            printRow(lengths);
            for (int i = 1; i < table.size(); i++) {
                row = table.get(i);
                print("|");
                for (int index = 0; index < row.size(); index++)
                    print(fixStr(" " + row.get(index) + " ", lengths[index], ' ', aligns[index]) + "|");
                print("\n");
                printRow(lengths);
            }
            print("\n");
        }
    }

    private static void printRow(int[] lengths) {
        print("+");
        for (int length : lengths)
            print(chars(length, '-'), "+");
        print("\n");
    }
}
