package org.mirna.sample;

public class Main {

    static class Teste {
        char field1;
        Character field2;

    }

    public static void main(String[] args) throws Exception {

        Class<?> tipo1 = Teste.class.getDeclaredField("field1").getType();
        Class<?> tipo2 = Teste.class.getDeclaredField("field2").getType();
        Class<?> tipo3 = Character.TYPE;

        System.out.println(tipo1.getName());
        System.out.println(tipo2.getName());
        System.out.println(tipo3);

    }
}
