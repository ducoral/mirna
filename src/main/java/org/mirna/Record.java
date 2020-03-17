package org.mirna;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.mirna.Mapping.annotation;

public class Record {

    private List<Mapping> columns = new ArrayList<>();

    private Record(Class<?> mirnaRecord) {
        mappings(mirnaRecord, this::add);
    }

    private void add(Mapping mapping) {
        int index = 0;
        while (index < columns.size())
            if (mapping.position() < columns.get(index).position()) {
                columns.add(index, mapping);
                return;
            } else index++;
        columns.add(index, mapping);
    }

    void validate() {
    }

    String toText(Object record) {
        return "";
    }

    Object fromText(String text) {
        return null;
    }

    static boolean match(Class<?> mirnaRecord, String textRecord) {
        return false;
    }

    static void validate(Class<?> mirnaRecord) {
        new Record(mirnaRecord).validate();
    }

    static void mappings(Class<?> mirnaRecord, Consumer<Mapping> action) {
        action.accept(new Mapping(mirnaRecord).put(Mapping.POSITION, 0));
        Arrays.stream(
                mirnaRecord.getDeclaredFields()
        ).forEach(field -> annotation(field, annotation -> action.accept(new Mapping(annotation))));
    }

}


