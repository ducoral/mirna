package com.github.ducoral.mirna;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DocumentedTest {

    @Line(identifier = "1")
    static class LineCase1 {

        @FieldDtm(position = 1)
        Date fieldCase1;

        LineCase1() {
        }

        LineCase1(int year, int month, int day) {
            fieldCase1 = new GregorianCalendar(year, month, day).getTime();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LineCase1 lineCase1 = (LineCase1) o;
            return Objects.equals(fieldCase1, lineCase1.fieldCase1);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fieldCase1);
        }

        @Override
        public String toString() {
            return String.format("L1[%s]", new SimpleDateFormat("dd/MM/yy").format(fieldCase1));
        }
    }

    @Line(identifier = "2")
    static class LineCase2 {

        @FieldInt(position = 1, length = 10)
        int fieldCase1;

        LineCase2() {
        }

        LineCase2(int fieldCase1) {
            this.fieldCase1 = fieldCase1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LineCase2 lineCase2 = (LineCase2) o;
            return fieldCase1 == lineCase2.fieldCase1;
        }

        @Override
        public int hashCode() {
            return Objects.hash(fieldCase1);
        }

        @Override
        public String toString() {
            return String.format("L2[%s]", "" + fieldCase1);
        }
    }

    @Line(identifier = "3")
    static class LineCase3 {

        @FieldStr(position = 1, length = 20)
        String fieldCase1;

        @Item
        LineCase2 itemCase1;

        LineCase3() {
        }

        LineCase3(String fieldCase1, LineCase2 itemCase1) {
            this.fieldCase1 = fieldCase1;
            this.itemCase1 = itemCase1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LineCase3 lineCase3 = (LineCase3) o;
            return Objects.equals(fieldCase1, lineCase3.fieldCase1) &&
                    Objects.equals(itemCase1, lineCase3.itemCase1);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fieldCase1, itemCase1);
        }

        @Override
        public String toString() {
            return String.format("L3[%s; %s]", "" + fieldCase1, "" + itemCase1);
        }
    }

    @Line(identifier = "4")
    static class LineCase4 {

        @FieldDec(position = 1, length = 15)
        BigDecimal fieldCase1;

        @Item(order = 1)
        List<LineCase1> itemsCase1;

        @Item(order = 2)
        LineCase3 itemCase2;

        LineCase4() {
        }

        LineCase4(BigDecimal fieldCase1, LineCase3 itemCase2, LineCase1... itemsCase1) {
            this.fieldCase1 = fieldCase1;
            this.itemsCase1 = Arrays.asList(itemsCase1);
            this.itemCase2 = itemCase2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LineCase4 lineCase4 = (LineCase4) o;
            return Objects.equals(fieldCase1, lineCase4.fieldCase1) &&
                    Objects.equals(itemsCase1, lineCase4.itemsCase1);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fieldCase1, itemsCase1);
        }

        @Override
        public String toString() {
            return String.format("L4[%s; %s]", "" + fieldCase1, "" + itemsCase1);
        }
    }

    @Document
    static class DocumentCase1 {

        @Header
        LineCase1 itemCase1;

        @Item
        List<LineCase2> itemCase2;

        @Footer
        LineCase1 itemCase3;

        DocumentCase1() {
        }

        DocumentCase1(LineCase1 itemCase1, LineCase1 itemCase3, LineCase2... itemsCase2) {
            this.itemCase1 = itemCase1;
            this.itemCase2 = Arrays.asList(itemsCase2);
            this.itemCase3 = itemCase3;
        }
    }

    @Document
    static class DocumentCase2 {

        @Item(order = 2)
        LineCase3 itemCase1;

        @Item(order = 1)
        LineCase4 itemCase2;

        DocumentCase2() {
        }

        DocumentCase2(LineCase3 itemCase1, LineCase4 itemCase2) {
            this.itemCase1 = itemCase1;
            this.itemCase2 = itemCase2;
        }
    }

    static Documented newTestedCase1() {
        return new Documented(new DocumentCase1(
                new LineCase1(2020, 4, 1),
                new LineCase1(2021, 5, 2),
                new LineCase2(10), new LineCase2(20), new LineCase2(30)));
    }

    static Documented newTestedCase2() {
        return new Documented(new DocumentCase2(
                new LineCase3("lineCase1", new LineCase2(10)),
                new LineCase4(
                        new BigDecimal("123.456"),
                        new LineCase3("lineCase2", new LineCase2(50)),
                        new LineCase1(2020, 4, 1),
                        new LineCase1(2021, 5, 2),
                        new LineCase1(2022, 6, 3),
                        new LineCase1(2023, 7, 4))));
    }

    @Test
    void getLinesCase1() {
        List<Object> expectedItems = Arrays.asList(
                new LineCase1(2020, 4, 1),
                new LineCase2(10),
                new LineCase2(20),
                new LineCase2(30),
                new LineCase1(2021, 5, 2)
        );

        List<Object> lines = new ArrayList<>();
        newTestedCase1().lines(lines::add);
        assertEquals(expectedItems, lines);
    }

    @Test
    void getLinesCase2() {
        List<Object> expectedItems = Arrays.asList(
                new LineCase4(
                        new BigDecimal("123.456"),
                        new LineCase3("lineCase2", new LineCase2(50)),
                        new LineCase1(2020, 4, 1),
                        new LineCase1(2021, 5, 2),
                        new LineCase1(2022, 6, 3),
                        new LineCase1(2023, 7, 4)),
                new LineCase1(2020, 4, 1),
                new LineCase1(2021, 5, 2),
                new LineCase1(2022, 6, 3),
                new LineCase1(2023, 7, 4),
                new LineCase3("lineCase2", new LineCase2(50)),
                new LineCase2(50),
                new LineCase3("lineCase1", new LineCase2(10)),
                new LineCase2(10)
        );

        List<Object> lines = new ArrayList<>();
        newTestedCase2().lines(lines::add);
        assertEquals(expectedItems, lines);
    }

    @Test
    void hasHeader() {
        assertTrue(newTestedCase1().hasHeader());
        assertFalse(newTestedCase2().hasHeader());
    }

    @Test
    void hasFooter() {
        assertTrue(newTestedCase1().hasFooter());
        assertFalse(newTestedCase2().hasFooter());
    }
}