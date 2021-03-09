package hu.zza.hyperskill.filetype.analyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Wrong idea...
// Without else-if skips parts.
// With it works even worse...
// I keep it only for a memento... :-D


final class AbsolutelyWrongSearchAlgorithm //implements SearchAlgorithm
{

  public boolean contains(String fileName, String pattern) {
    final int[] patternArray = pattern
        .chars()
        .toArray();

    final int[] sample = new int[patternArray.length];
    int index = 0;

    try (var br = new BufferedReader(new FileReader(fileName))
    ) {
      while (br.ready()) {
        sample[index] = br.read();

        if (sample[index] == patternArray[index]) {
          if (++index == patternArray.length) {
            return true;
          }
        } else if (sample[index] == patternArray[0]) {
          sample[0] = sample[index];
          index = 1;
        } else {
          index = 0;
        }
      }
    } catch (IOException e) {
      System.err.printf(
          "%ncontains(String fileName, String pattern) @ NaiveSearchAlgorithm.class%n%s%n%n", e);
    }

    return false;
  }
}
