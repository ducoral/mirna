package org.mirna.sample;

import org.mirna.*;

import java.math.BigDecimal;
import java.util.Date;

public class Main {
    @Line(identifier = "1")
    public static class LineType1 {

        @FieldInt(position = 1, length = 5, fill = '0')
        int field1;

        @FieldStr(position = 2, length = 20)
        String field2;

        @FieldDec(position = 3, length = 5, fill = '0')
        BigDecimal field3;

        public LineType1() {
        }

        public LineType1(int field1, String field2, BigDecimal field3) {
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
        }
    }

    @Line(identifier = "2")
    public static class LineType2 {

        @FieldStr(position = 1, length = 10)
        String field1;

        @FieldDtm(position = 2)
        Date field2;

        @FieldStr(position = 3, length = 12)
        String field3;

        public LineType2() {
        }

        public LineType2(String field1, Date field2, String field3) {
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
        }
    }

    @Line(identifier = "3")
    public static class LineType3 {

        @FieldInt(position = 1, length = 10)
        Integer field1;

        @FieldDtm(position = 2, format = "ddMMyy")
        Date field2;

        @FieldStr(position = 3, length = 14, align = Align.RIGHT, fill = '*')
        String field3;

        public LineType3() {
        }

        public LineType3(Integer field1, Date field2, String field3) {
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
        }
    }

    @Document
    public static class DocumentCase {

        @Header
        LineType1 header;

        @Item
        LineType2 item;

        @Footer
        LineType3 footer;

        public DocumentCase() {
        }

        public DocumentCase(LineType1 header, LineType2 item, LineType3 footer) {
            this.header = header;
            this.item = item;
            this.footer = footer;
        }
    }

    public static void main(String[] args) {

        Mirna.report(DocumentCase.class);

    }
}
