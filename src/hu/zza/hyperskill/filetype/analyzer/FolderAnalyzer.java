package hu.zza.hyperskill.filetype.analyzer;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;


public class FolderAnalyzer
{
    private final Path            folderPath;
    private final FilePatternList filePatternList;
    private final SearchEngine    searchEngine;
    
    public FolderAnalyzer(String folderPath, FilePatternList filePatternList)
    {
        this(Path.of(folderPath), filePatternList);
    }
    
    public FolderAnalyzer(Path folderPath, FilePatternList filePatternList)
    {
        this(folderPath, filePatternList, "KMP");
    }
    
    public FolderAnalyzer(Path folderPath, FilePatternList filePatternList, String searchAlgorithm)
    {
        this(folderPath, filePatternList, new SearchEngine(searchAlgorithm));
    }
    
    public FolderAnalyzer(Path folderPath, FilePatternList filePatternList, SearchEngine searchEngine)
    {
        this.folderPath      = folderPath;
        this.filePatternList = filePatternList;
        this.searchEngine    = searchEngine;
    }
    
    
    public void analyzeAndPrint(PrintStream printStream)
    {
        var                    executorService = Executors.newFixedThreadPool(3);
        List<Future<String[]>> resultList      = List.of();
        
        try
        {
            resultList = Files
                                 .list(folderPath)
                                 .map(path -> executorService.submit(() -> analyzeFile(path.toString())))
                                 .collect(Collectors.toList());
            
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        var index = resultList.size() - 1;
        while (resultList.size() > 0)
        {
            if (resultList
                        .get(index)
                        .isDone())
            {
                try
                {
                    String[] result = resultList
                                              .get(index)
                                              .get();
                    printStream.printf("%s: %s%n", result[0], result[1]);
                    resultList.remove(index);
                }
                catch (InterruptedException | ExecutionException e)
                {
                    e.printStackTrace();
                }
            }
            
            if (--index < 0)
            {
                index = resultList.size() - 1;
            }
        }
        
        executorService.shutdown();
    }
    
    
    // LOW-LEVEL
    
    private String[] analyzeFile(String filePath)
    {
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        
        var bestMatch = "Unknown file type";
        int priority  = 0;
        
        for (var pattern : filePatternList.getKeys())
        {
            try
            {
                if (searchEngine.contains(filePath, pattern))
                {
                    if (priority < filePatternList.getPriority(pattern))
                    {
                        bestMatch = filePatternList.getType(pattern);
                        priority  = filePatternList.getPriority(pattern);
                    }
                }
            }
            catch (IOException e)
            {
                bestMatch = "Unknown file type due to IOException";
                priority  = Integer.MAX_VALUE;
                e.printStackTrace();
            }
        }
        
        return new String[] {fileName, bestMatch};
    }
}
