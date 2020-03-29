package org.mirna;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mirna.Utils.validate;

public final class Mirna {

    private final Map<Class<?>, Record> records = new HashMap<>();

    private final List<Object> mirnaRecords = new ArrayList<>();

    Mirna() {
    }

    void writeRecords(Writer writer) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            for (Object mirnaRecord : mirnaRecords) {
                bufferedWriter.write(records.get(mirnaRecord.getClass()).toText(mirnaRecord));
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
                Record record = getRecord(text);
                if (record == null)
                    throw new MirnaException(Strs.MSG_UNMAPPED_RECORD, line, text);
                list.add(record.fromText(text));
                text = bufferedReader.readLine();
            }
        } catch (IOException e) {
            throw new MirnaException(e.getMessage(), e);
        }
        return list;
    }

    Record getRecord(String text) {
        for (Map.Entry<Class<?>, Record> entry : records.entrySet())
            if (Utils.match(entry.getKey(), text))
                return entry.getValue();
        return null;
    }

    void register(Object mirnaRecord) {
        mirnaRecords.add(mirnaRecord);
        register(mirnaRecord.getClass());
    }

    void register(Class<?> mirnaRecordClass) {
        if (records.containsKey(mirnaRecordClass))
            return;
        validate(mirnaRecordClass);
        MirnaRecord config = mirnaRecordClass.getAnnotation(MirnaRecord.class);
        for (Map.Entry<Class<?>, Record> entry : records.entrySet())
            if (config.identifier().equals(entry.getValue().identifier()))
                throw new MirnaException(
                        Strs.MSG_DUPLICATE_RECORD_IDENTIFIER,
                        config.identifier(), mirnaRecordClass.getSimpleName(), entry.getKey().getSimpleName());
        records.put(mirnaRecordClass, new Record(mirnaRecordClass));
    }
}
