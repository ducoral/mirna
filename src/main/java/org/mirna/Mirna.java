package org.mirna;

import org.mirna.core.MirnaException;
import org.mirna.core.Record;

import java.io.BufferedWriter;
import java.io.Writer;
import java.util.List;
import java.util.Objects;

public final class Mirna {

    private Mirna() {
    }

    public static void write(Writer writer, List<?> mirnaRecords) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            for (Object mirnaRecord : mirnaRecords) {
                Objects.requireNonNull(mirnaRecord);
                bufferedWriter.write(new Record(mirnaRecord.getClass()).toText(mirnaRecord));
                bufferedWriter.newLine();
            }
        } catch (Exception e) {
            throw new MirnaException(e.getMessage(), e);
        }
    }
}
