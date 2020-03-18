package org.mirna.sample;

import org.mirna.annotations.*;
import org.mirna.core.Converter;

import java.util.Date;

@MirnaRecord(identifier = "1")
public class SampleRecord {

    public static class SampleConverter implements Converter {
        @Override
        public String toText(Object value) {
            return value.toString();
        }

        @Override
        public Object fromText(String text) {
            return text;
        }
    }

    @IntegerField(position = 1, length = 5)
    private Integer integerField;

    @DecimalField(position = 2, length = 10)
    private Float decimalField;

    @StringField(position = 3, length = 20)
    private String stringField;

    @DateTimeField(position = 4)
    private Date dateField;

    @CustomField(position = 5, length = 15, converter = SampleConverter.class)
    private Object object;
}
