package hu.zza.hyperskill.filetype.analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


class KmpSearchAlgorithm implements SearchAlgorithm {

  private static boolean kmpContains(String text, String pattern) {
    return 0 < kmpSearch(text, pattern).size();
  }

  // LOW-LEVEL

  private static List<Integer> kmpSearch(String text, String pattern) {
    int[] prefixArr = prefixFunction(pattern);
    var occurrences = new ArrayList<Integer>();
    int j = 0;

    for (int i = 0; i < text.length(); i++) {

      while (j > 0 && text.charAt(i) != pattern.charAt(j)) {
        j = prefixArr[j - 1];
      }

      if (text.charAt(i) == pattern.charAt(j)) {
        j += 1;
      }

      if (j == pattern.length()) {
        occurrences.add(i - j + 1);
        j = prefixArr[j - 1];
      }
    }

    return occurrences;
  }

  private static int[] prefixFunction(String str) {
    int[] prefixArr = new int[str.length()];

    for (int i = 1; i < str.length(); i++) {
      int j = prefixArr[i - 1];

      while (j > 0 && str.charAt(i) != str.charAt(j)) {
        j = prefixArr[j - 1];
      }

      if (str.charAt(i) == str.charAt(j)) {
        j += 1;
      }

      prefixArr[i] = j;
    }

    return prefixArr;
  }

  @Override
  public boolean contains(Stream<String> lines, String pattern) {
    return lines.anyMatch(s -> kmpContains(s, pattern));
  }
}
