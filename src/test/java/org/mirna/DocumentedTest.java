package org.mirna;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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
    }

    @Line(identifier = "4")
    static class LineCase4 {

        @FieldDec(position = 1, length = 15)
        BigDecimal fieldCase1;

        @Item
        List<LineCase1> itemsCase1;

        LineCase4() {
        }

        LineCase4(BigDecimal fieldCase1, LineCase1... itemsCase1) {
            this.fieldCase1 = fieldCase1;
            this.itemsCase1 = Arrays.asList(itemsCase1);
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
        LineCase3 fieldCase1;

        @Item(order = 1)
        LineCase4 fieldCase2;

        DocumentCase2() {
        }

        DocumentCase2(LineCase3 fieldCase1, LineCase4 fieldCase2) {
            this.fieldCase1 = fieldCase1;
            this.fieldCase2 = fieldCase2;
        }
    }

    static Documented newTestedCase1() {
        return new Documented(new DocumentCase1(
                new LineCase1(2020, 4, 1),
                new LineCase1(2020, 4, 2),
                new LineCase2(10), new LineCase2(20), new LineCase2(30)));
    }

    static Documented newTestedCase2() {
        return new Documented(new DocumentCase2(
                new LineCase3("lineCase1", new LineCase2(10)),
                new LineCase4(
                        new BigDecimal("123.456"),
                        new LineCase1(2020, 4, 1),
                        new LineCase1(2020, 4, 2),
                        new LineCase1(2020, 4, 3),
                        new LineCase1(2020, 4, 4))));
    }

    @Test
    void getLinesCase1() {
        List<Object> expectedItems = Arrays.asList(
                new LineCase1(2020, 4, 1),
                new LineCase2(10),
                new LineCase2(20),
                new LineCase2(30),
                new LineCase1(2020, 4, 2)
        );

        assertEquals(expectedItems, newTestedCase1().getLines());
    }

    @Test
    void getLinesCase2() {
        List<Object> expectedItems = Arrays.asList(
                new LineCase3("lineCase1", new LineCase2(10)),
                new LineCase4(
                        new BigDecimal("123.456"),
                        new LineCase1(2020, 4, 1),
                        new LineCase1(2020, 4, 2),
                        new LineCase1(2020, 4, 3),
                        new LineCase1(2020, 4, 4)),
                new LineCase1(2020, 4, 1),
                new LineCase1(2020, 4, 2),
                new LineCase1(2020, 4, 3),
                new LineCase1(2020, 4, 4)
        );

        assertEquals(expectedItems, newTestedCase2().getLines());
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