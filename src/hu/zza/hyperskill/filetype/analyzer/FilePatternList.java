package hu.zza.hyperskill.filetype.analyzer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class FilePatternList {

  private static final String EXC_CONSTRUCTOR_BASE =
      "%n%nFilePatternList's constructor needs a regular, readable file.%n";
  private static final String EXC_CONSTRUCTOR_HANDLE =
      "However, during the processing an IOException occurs:%n%s%n";
  private static final String EXC_CONSTRUCTOR_IO = "The constructor can not handle this:%n%s%n";
  private final HashMap<String, String> patternMap = new HashMap<>();
  private final HashMap<String, Integer> priorityMap = new HashMap<>();

  public FilePatternList(FilePattern... patterns) {
    for (var p : patterns) {
      patternMap.put(p.getPattern(), p.getType());
      priorityMap.put(p.getPattern(), 1);
    }
  }

  public FilePatternList(String filePatternDataBase) throws FileNotFoundException {
    Path path = Path.of(filePatternDataBase);

    if (Files.isRegularFile(path)) {
      try (var br = new BufferedReader(new FileReader(filePatternDataBase))) {
        br.lines()
            .map(line -> line.split("\"?;\"?"))
            .forEach(
                arr -> {
                  if (arr[2].endsWith("\"")) {
                    arr[2] = arr[2].substring(0, arr[2].length() - 1);
                  }

                  patternMap.put(arr[1], arr[2]);
                  priorityMap.put(arr[1], Integer.valueOf(arr[0]));
                });
      } catch (IOException e) {
        var errorMessage = String.format(EXC_CONSTRUCTOR_BASE + EXC_CONSTRUCTOR_IO, e);
        throw new FileNotFoundException(errorMessage);
      }
    } else {
      var errorMessage =
          String.format(EXC_CONSTRUCTOR_BASE + EXC_CONSTRUCTOR_HANDLE, filePatternDataBase);
      throw new FileNotFoundException(errorMessage);
    }
  }

  String getType(String pattern) {
    return patternMap.get(pattern);
  }

  int getPriority(String pattern) {
    return priorityMap.get(pattern);
  }

  String[] getKeys() {
    return patternMap.keySet().toArray(new String[0]);
  }
}
