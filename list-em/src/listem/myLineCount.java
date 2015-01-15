package listem;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Matt on 1/14/2015.
 */
public class myLineCount extends FileReader implements LineCounter {

    private Map<File, Integer> result;
    /**
     * Count the number of lines in files whose names match a given pattern.
     *
     * @param directory            The base directory to look at files from
     * @param fileSelectionPattern Pattern for selecting file names
     * @param recursive            Recursively search through directories
     * @return A Map containing files whose lines were counted. Each file is mapped
     * to an integer which is the number of lines counted in the file.
     */
    @Override
    public Map<File, Integer> countLines(File directory, String fileSelectionPattern, boolean recursive) {
        result = new HashMap<File, Integer>();
        readFile(directory,fileSelectionPattern,recursive);
        return result;
    }

    @Override
    void process(File file, Scanner in) {
        int lineCount = 0;
        while(in.hasNextLine()){
            lineCount++;
            in.nextLine();
        }
        result.put(file,lineCount);
    }
}
