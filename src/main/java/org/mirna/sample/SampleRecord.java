package org.mirna.sample;

import org.mirna.annotations.*;
import org.mirna.converters.CustomConverter;

import java.util.Date;

@MirnaRecord(identifier = "1")
public class SampleRecord {

    @IntegerField(pos = 1, len = 5)
    private Integer integerField;

    @DecimalField(pos = 2, len = 10)
    private Float decimalField;

    @StringField(pos = 3, len = 20)
    private String stringField;

    @DateTimeField(pos = 4)
    private Date dateField;

    @CustomField(pos = 5, len = 15, con = CustomConverter.class)
    private Object object;
}
