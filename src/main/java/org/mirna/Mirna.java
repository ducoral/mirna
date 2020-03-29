package org.mirna;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mirna.Utils.validate;

public final class Mirna {

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
                    throw new MirnaException(Strs.MSG_ERROR_PARSING_TEXT, line);
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
}
