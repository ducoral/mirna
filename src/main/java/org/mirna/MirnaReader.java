package org.mirna;

import java.io.Reader;
import java.util.List;

public interface MirnaReader {

    List<?> read(Reader reader);
}
