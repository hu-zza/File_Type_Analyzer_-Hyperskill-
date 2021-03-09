package hu.zza.hyperskill.filetype.analyzer;

import java.util.stream.Stream;


class LazySearchAlgorithm implements SearchAlgorithm {

  @Override
  public boolean contains(Stream<String> lines, String pattern) {
    return lines.anyMatch(s -> s.contains(pattern));
  }
}
