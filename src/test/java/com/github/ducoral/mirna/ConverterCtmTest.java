package com.github.ducoral.mirna;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConverterCtmTest {

    static class CustomObject {
        String string = "string";
        Integer integer = 10;
    }

    static class CustomConverterCase implements Converter {

        @Override
        public String toText(Object value) {
            CustomObject obj = (CustomObject) value;
            return obj.string + obj.integer;
        }

        @Override
        public Object fromText(String text) {
            CustomObject obj = new CustomObject();
            obj.string = text.substring(0, 6);
            obj.integer = Integer.valueOf(text.substring(6));
            return obj;
        }
    }

    static class LineCase {

        @FieldCtm(position = 1, length = 10, converter = CustomConverterCase.class)
        CustomObject fieldCase1;

        @FieldCtm(position = 2, length = 20, fill = '*', align = Align.RIGHT, converter = CustomConverterCase.class)
        CustomObject fieldCase2;
    }

    @Test
    void toText() {
        Object obj = new CustomObject();
        Assertions.assertEquals("string10  ", converter("fieldCase1").toText(obj));
        Assertions.assertEquals("************string10", converter("fieldCase2").toText(obj));
    }

    @Test
    void fromText() {
        CustomObject obj = (CustomObject) converter("fieldCase1").fromText("string10  ");
        assertEquals("string", obj.string);
        assertEquals(10, obj.integer);

        obj = (CustomObject) converter("fieldCase2").fromText("************string10");
        assertEquals("string", obj.string);
        assertEquals(10, obj.integer);
    }

    ConverterCtm converter(String field) {
        return new ConverterCtm(new Fielded(getField(field)), new CustomConverterCase());
    }

    Field getField(String field) {
        try {
            return LineCase.class.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            throw new Oops(Strs.MSG_INTERNAL_ERROR);
        }
    }

}