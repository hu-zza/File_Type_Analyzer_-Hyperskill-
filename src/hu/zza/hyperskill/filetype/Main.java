package hu.zza.hyperskill.filetype;

import hu.zza.hyperskill.filetype.analyzer.FilePatternList;
import hu.zza.hyperskill.filetype.analyzer.FolderAnalyzer;

import java.io.FileNotFoundException;


public class Main
{
    public static void main(String[] args)
    {
        if (args.length == 2)
        {
            try
            {
                var filePatternList = new FilePatternList(args[1]);
                var folderAnalyzer  = new FolderAnalyzer(args[0], filePatternList);
                folderAnalyzer.analyzeAndPrint(System.out);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            System.err.println("Initial arguments needed: <folder with files to analyze> <database file of patterns>");
        }
    }
}
