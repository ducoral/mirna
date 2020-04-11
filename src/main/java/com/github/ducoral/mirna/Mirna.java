package com.github.ducoral.mirna;

import java.io.*;
import java.util.*;

import static com.github.ducoral.mirna.Align.LEFT;
import static com.github.ducoral.mirna.Align.RIGHT;
import static com.github.ducoral.mirna.Rule.*;
import static com.github.ducoral.mirna.Strs.*;
import static com.github.ducoral.mirna.Utils.*;

public final class Mirna {

    private final Map<Class<?>, Linner> linners = new HashMap<>();

    private final List<Object> lines = new ArrayList<>();

    Mirna() {
    }

    public static String toText(Object document) {
        Objects.requireNonNull(document);
        StringWriter writer = new StringWriter();
        writeDocument(document, writer);
        return writer.toString();
    }

    public static void writeDocument(Object document, Writer writer) {
        Objects.requireNonNull(document);
        Objects.requireNonNull(writer);
        validateDocument(document.getClass());
        Mirna mirna = new Mirna();
        new Documented(document).lines(mirna::register);
        mirna.writeLines(writer);
    }

    public static <T> T fromText(Class<T> documentClass, String text) {
        Objects.requireNonNull(documentClass);
        Objects.requireNonNull(text);
        return readDocument(documentClass, new StringReader(text));
    }

    public static <T> T readDocument(Class<T> documentClass, Reader reader) {
        Objects.requireNonNull(documentClass);
        Objects.requireNonNull(reader);
        validateDocument(documentClass);
        try {
            Mirna mirna = new Mirna();
            T document = documentClass.newInstance();
            new Documented(document).types(mirna::register).parse(mirna.readLines(reader));
            return document;
        } catch (Exception e) {
            throw new Oops(e.getMessage(), e);
        }
    }

    void writeLines(Writer writer) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            for (Object line : lines) {
                bufferedWriter.write(linners.get(line.getClass()).toText(line));
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            throw new Oops(e.getMessage(), e);
        }
    }

    List<?> readLines(Reader reader) {
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
                REPORT_LENGTH.toString(), REPORT_FILL.toString(), REPORT_ALIGN.toString(),
                REPORT_FORMAT.toString(), REPORT_DECIMALS.toString(), REPORT_SEPARATOR.toString(),
                REPORT_VALUE.toString()));
        int from = 1;
        for (Fielded fielded : new Linner(lineClass).fieldeds) {
            String name = fielded.field() == null ? LINE_IDENTIFIER.toString() : fielded.field().getName();
            String value = fielded.field() == null ? fielded.identifier() : fielded.field().getType().getSimpleName();
            table.add(Arrays.asList(
                    name,
                    String.valueOf(from),
                    String.valueOf(from + fielded.length() - 1),
                    String.valueOf(fielded.length()),
                    stringOf(fielded.fill()),
                    String.valueOf(fielded.align()),
                    String.valueOf(fielded.format()),
                    String.valueOf(fielded.decimals()),
                    stringOf(fielded.separator()),
                    value));
            from += fielded.length();
        }
        return table;
    }

    private static String stringOf(char value) {
        if (value == '\0')
            return "'\\0'";
        else
            return "'" + value + "'";
    }

    private static int[] lengths(List<List<String>> table) {
        int[] lengths = null;
        for (List<String> row : table) {
            if (lengths == null)
                lengths = new int[row.size()];
            for (int col = 0; col < lengths.length; col++)
                lengths[col] = Math.max(lengths[col], row.get(col).length() + 2);
        }
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
        Align[] aligns = {LEFT, RIGHT, RIGHT, RIGHT, LEFT, LEFT, LEFT, RIGHT, LEFT, LEFT};

        for (Class<?> type : types) {
            List<List<String>> table = table(type);
            int[] lengths = lengths(table);
            int width = Arrays.stream(lengths).reduce(lengths.length - 1, Integer::sum);

            print("+", chars(width, '-'), "+\n");
            print("|#g#", fixLeft(" " + type.getName(), width, ' '), "#0#|\n");
            printRowBorder(lengths);

            List<String> row = table.get(0);
            print("|");
            for (int index = 0; index < row.size(); index++)
                print("#b#", fixStr(" " + row.get(index) + " ", lengths[index], ' ', aligns[index]) + "#0#|");
            print("\n");
            printRowBorder(lengths);
            for (int i = 1; i < table.size(); i++) {
                row = table.get(i);
                print("|");
                for (int index = 0; index < row.size(); index++)
                    print(fixStr(" " + row.get(index) + " ", lengths[index], ' ', aligns[index]) + "|");
                print("\n");
                printRowBorder(lengths);
            }
            print("\n");
        }
    }

    private static void printRowBorder(int[] lengths) {
        print("+");
        for (int length : lengths)
            print(chars(length, '-'), "+");
        print("\n");
    }
}
