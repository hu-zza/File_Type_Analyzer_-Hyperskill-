package hu.zza.hyperskill.filetype.analyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


class SearchEngine
{
    private static final HashMap<String, SearchAlgorithm> searchAlgorithmMap = new HashMap<>();
    
    static
    {
        searchAlgorithmMap.put("naive", new LazySearchAlgorithm());
        searchAlgorithmMap.put("KMP", new KmpSearchAlgorithm());
    }
    
    private SearchAlgorithm searchAlgorithm;
    
    
    // CONSTRUCTORS
    
    SearchEngine(String nameFlag)
    {
        this(getSearchAlgorithmByNameFlag(nameFlag));
    }
    
    private SearchEngine(SearchAlgorithm searchAlgorithm)
    {
        this.searchAlgorithm = searchAlgorithm;
    }
    
    
    // INSTANCE METHODS
    
    boolean contains(String fileName, String pattern)
    throws IOException
    {
        try (var br = new BufferedReader(new FileReader(fileName)))
        {
            return this.searchAlgorithm.contains(br.lines(), pattern);
        }
    }
    
    void changeSearchAlgorithm(String nameFlag)
    {
        this.searchAlgorithm = getSearchAlgorithmByNameFlag(nameFlag);
    }
    
    
    // LOW-LEVEL
    
    private static SearchAlgorithm getSearchAlgorithmByNameFlag(String nameFlag)
    {
        String[] resolvedNameFlag = resolveNameFlag(nameFlag);
        
        if ("1".equals(resolvedNameFlag[0]))
        {
            System.err.println("getSearchAlgorithmByNameFlag(String nameFlag) @ SearchEngine.class");
            System.err.println("\tMethod returns with the default SearchAlgorithm.");
        }
        
        return searchAlgorithmMap.get(resolvedNameFlag[1]);
    }
    
    private static String[] resolveNameFlag(String nameFlag)
    {
        // [0] status - 0 : success | 1 : fail, returns default
        // [1] resolved nameFlag
        String[] result = {"1", "KMP"}; // set to fallback
        String   key;
        
        if (nameFlag.startsWith("--"))
        {
            key = nameFlag.substring(2);
        }
        else
        {
            key = nameFlag;
        }
        
        if (searchAlgorithmMap.containsKey(key))
        {
            result[0] = "0";
            result[1] = key;
        }
        else
        {
            System.err.println("resolveNameFlag(String nameFlag) @ SearchEngine.class");
            System.err.println("\tUnknown nameFlag.");
        }
        
        if ("1".equals(result[0]))
        {
            System.err.println("\tThe method returns with 'KMP' as result[1].");
        }
        
        return result;
    }
}
