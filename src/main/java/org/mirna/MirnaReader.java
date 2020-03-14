package org.mirna;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MirnaReader {

    private List<Class<?>> types;

    private MirnaReader(List<Class<?>> types) {
        this.types = types;
    }

    public static MirnaReader newReader(Class<?>[] types) {
        return new MirnaReader(Arrays.asList(types));
    }

    public List<?> read(Reader reader) {
        return new ArrayList<>();
    }
}
