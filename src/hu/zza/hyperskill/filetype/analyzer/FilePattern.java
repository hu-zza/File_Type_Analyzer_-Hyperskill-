package hu.zza.hyperskill.filetype.analyzer;

class FilePattern
{
    private final String pattern;
    private final String type;
    
    FilePattern(String pattern, String type)
    {
        this.pattern = pattern;
        this.type    = type;
    }
    
    String getPattern()
    {
        return pattern;
    }
    
    String getType()
    {
        return type;
    }
}