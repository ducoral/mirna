package org.mirna.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();

        Stream.of("um", "dois", "tres").forEach(str -> list.set(list.size(), str));

        System.out.println(list);

    }
}
