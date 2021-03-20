package hu.zza.hyperskill.filetype.analyzer;

import java.util.stream.Stream;

public interface SearchAlgorithm {

  boolean contains(Stream<String> lines, String pattern);
}
