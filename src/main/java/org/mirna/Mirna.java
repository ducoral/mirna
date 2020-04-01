package org.mirna;

import java.io.*;
import java.util.*;

import static org.mirna.Utils.*;

public final class Mirna {


    private final Map<Class<?>, Linner> linners = new HashMap<>();

    private final List<Object> lines = new ArrayList<>();

    Mirna() {
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

    List<Object> readLines(Reader reader) {
        List<Object> list = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            int line = 0;
            String text = bufferedReader.readLine();
            while (text != null) {
                line++;
                Linner linner = getLinner(text);
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

    Linner getLinner(String text) {
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
                "   #t#:: flat-file parser ::#c#" + str + "#0#\n\n" +
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
