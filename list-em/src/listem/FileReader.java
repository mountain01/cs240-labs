package listem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by Matt on 1/13/2015.
 */
abstract public class FileReader {
    public void readFile(File directory, String pattern, Boolean recursive) {
        File[] fileList = directory.listFiles();
        if (fileList != null) {
            for (File file : fileList) {
                if (file.isDirectory() && recursive) {
                    readFile(file, pattern, recursive);
                } else if (file.isFile() && file.getName().matches(pattern)) {
                    Scanner in = null;
                    try {
                        in = new Scanner(file);
                        process(file, in);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    abstract void process(File file, Scanner in);
}
