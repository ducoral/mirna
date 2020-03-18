package org.mirna.core;

import org.mirna.annotations.MirnaRecord;
import org.mirna.annotations.StringField;

/*
    List<Mapping> maps = new ArrayList<>();
        mappings(mirnaRecord, maps::add);
        if (maps.isEmpty())
            throw new MirnaException(
                    Strs.MSG_MISSING_CONFIGURATION, mirnaRecord.getName());
        if (maps.get(0).identifier().isEmpty())
            throw new MirnaException(
                    Strs.MSG_ANNOTATION_NOT_PRESENT, MirnaRecord.class.getName(), mirnaRecord.getName());
        if (maps.size() == 1)
            throw new MirnaException(
                    Strs.MSG_MISSING_FIELD_CONFIG, mirnaRecord.getName());
        for (int i = 1; i < maps.size(); i++) {
            Mapping mapping = maps.get(i);
            Field field = (Field) mapping.target();
            if (Mapping.isMapped(field) && !Mapping.isTypeSupported(field))
                throw new MirnaException(
                        Strs.MSG_INVALID_FIELD_TYPE,
                        field.getType().getName(),
                        mapping.configuration());
        }
        for (int pos = 0; pos < maps.size(); pos++) {
            int expected = maps.get(pos).position();
            if (pos > expected)
                throw new MirnaException(
                        Strs.MSG_DUPLICATE_POSITION_CONFIG,
                        expected,
                        maps.get(0).target().getClass().getName());
            else if (pos < expected)
                throw new MirnaException(
                        Strs.MSG_MISSING_POSITION_CONFIG,
                        pos,
                        maps.get(0).target().getClass().getName());

 */

class RecordTest {

    @MirnaRecord(identifier = "id")
    static class MirnaRecordValidCase {
        @StringField(position = 1, length = 10)
        String fieldCase1;
    }


}