package org.mirna;

import org.mirna.annotations.MirnaRecord;
import org.mirna.core.Record;

import java.io.BufferedWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.*;

public final class Mirna implements MirnaWriter, MirnaReader {

    private final Map<Class<?>, Record> records = new HashMap<>();

    private final List<?> mirnaRecords;

    private Mirna(List<?> mirnaRecords) {
        this.mirnaRecords = mirnaRecords;
    }

    public static MirnaWriter writer(List<?> mirnaRecords) {
        Mirna mirna = new Mirna(mirnaRecords);
        for (Object mirnaRecord : mirnaRecords)
            mirna.register(mirnaRecord.getClass());
        return mirna;
    }

    public static MirnaReader reader(Class<?>... mirnaRecordClasses) {
        Mirna mirna = new Mirna(Collections.emptyList());
        for (Class<?> mirnaRecordClass : mirnaRecordClasses)
            mirna.register(mirnaRecordClass);
        return mirna;
    }

    @Override
    public List<?> read(Reader reader) {
        return null;
    }

    @Override
    public void write(Writer writer) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            for (Object mirnaRecord : mirnaRecords) {
                Objects.requireNonNull(mirnaRecord);
                bufferedWriter.write(records.get(mirnaRecord.getClass()).toText(mirnaRecord));
                bufferedWriter.newLine();
            }
        } catch (Exception e) {
            throw new MirnaException(e.getMessage(), e);
        }
    }

    private void register(Class<?> mirnaRecordClass) {
        if (records.containsKey(mirnaRecordClass))
            return;
        Record.validate(mirnaRecordClass);
        MirnaRecord config = mirnaRecordClass.getAnnotation(MirnaRecord.class);
        for (Map.Entry<Class<?>, Record> entry : records.entrySet())
            if (config.identifier().equals(entry.getValue().identifier()))
                throw new MirnaException(
                        Strs.MSG_DUPLICATE_RECORD_IDENTIFIER,
                        config.identifier(), mirnaRecordClass.getSimpleName(), entry.getKey().getSimpleName());
        records.put(mirnaRecordClass, new Record(mirnaRecordClass));
    }
}
