/**
 * Created by Matt on 1/6/2015.
 */

public class ImageEditor {
    public static void main(String[] args){
        System.out.println(args.length);
        if(args.length < 3 || args.length > 4){
            System.out.println("Invalid use. Correct Usage:\n");
            System.out.println("java ImageEditor inputFileName outputFileName {grayscale|invert|emboss|motionblur blurLength }");
        }
        else {
            System.out.println("correct");
        }
    }
}

