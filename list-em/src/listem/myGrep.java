package listem;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Matt on 1/13/2015.
 */
public class myGrep extends FileReader implements Grep {

    private Map<File,List<String>> result;
    String searchString;
    @Override
    void process(File file, Scanner in) {
        List<String> list = new ArrayList<String>();
        Pattern p = Pattern.compile(searchString);
        while(in.hasNextLine()){
            String input = in.nextLine();
            Matcher m = p.matcher(input);
            if(m.find()){
                list.add(input);
            }
        }
        if(list.size()>0){
            result.put(file,list);
        }
    }

    /**
     * Find lines that match a given pattern in files whose names match another
     * pattern
     *
     * @param directory                 The base directory to look at files from
     * @param fileSelectionPattern      Pattern for selecting file names
     * @param substringSelectionPattern Pattern to search for in lines of a file
     * @param recursive                 Recursively search through directories
     * @return A Map containing files that had at least one match found inside them.
     * Each file is mapped to a list of strings which are the exact strings from
     * the file where the <code>substringSelectionPattern</code> was found.
     */
    @Override
    public Map<File, List<String>> grep(File directory, String fileSelectionPattern, String substringSelectionPattern, boolean recursive) {
        result = new HashMap<File, List<String>>();
        searchString = substringSelectionPattern;
        readFile(directory,fileSelectionPattern,recursive);
        return result;
    }
}
