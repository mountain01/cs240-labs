import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Matt on 1/6/2015.
 */

public class ImageEditor {
    private Pixel[][] picture;
    private int width,height,maxColor;
    private Scanner in;
    private FileWriter out;
    public static void main(String[] args){
        ImageEditor ie = new ImageEditor();
        if(args.length < 3 || args.length > 4){
            ie.usage();
        }
        else {
            try{
                ie.in = new Scanner(new File(args[0]));
                ie.out = new FileWriter(new File(args[1]));
                String type = args[2];
                int blurNum = type.equals("motionblur") ? Integer.parseInt(args[3]):0;
                ie.generatePic();
                ie.transform(type,blurNum);
                ie.writeOut();
            } catch (FileNotFoundException e) {
                ie.usage();
            } catch (IOException e) {
                ie.usage();
            } catch (IndexOutOfBoundsException e){
                ie.usage();
            }
        }
    }

    private void transform(String type, int blurNum) throws IOException {
        for(int i = height-1;i>-1;i--){
            for(int k = 0;k<width;k++){
                Pixel pixel = picture[i][k];
                if(type.equals("invert")){
                    pixel.inverse();
                } else if(type.equals("grayscale")){
                    pixel.grayscale();
                } else if(type.equals("emboss")){
                    if(i<1||k<1){
                        pixel.emboss(128);
                    } else{
                        pixel.emboss(embossNum(pixel,picture[i-1][k-1]));
                    }
                } else if(type.equals("motionblur")){
                    if(blurNum < 1){
                        throw new IOException();
                    }
                    pixel.motionBlur(blur(getBlurPixels(i, k, blurNum)));
                }
                else{
                    throw new IOException();
                }
            }
        }
    }

    private Pixel blur(ArrayList<Pixel> blurPixels) {
        int reds = 0;
        int greens = 0;
        int blues = 0;
        int len = blurPixels.size();
        for(Pixel p : blurPixels){
            reds += p.red;
            greens += p.green;
            blues += p.blue;
        }
        return new Pixel(reds/len,greens/len,blues/len);
    }

    private ArrayList<Pixel> getBlurPixels(int i, int k, int blurNum) {
        ArrayList<Pixel> list = new ArrayList<Pixel>();
        int blur =  k + blurNum >= width ? width - k : blurNum;
        for(int j = blur - 1; j >= 0; j--){
            list.add(picture[i][k+j]);
        }
        return list;
    }

    private int embossNum(Pixel p1, Pixel p2) {
        int redDiff = p1.red - p2.red;
        int greenDiff = p1.green - p2.green;
        int blueDiff = p1.blue - p2.blue;
        int maxDiff = Math.abs(redDiff) >= Math.abs(greenDiff) ? redDiff:greenDiff;
        maxDiff = Math.abs(maxDiff) >= Math.abs(blueDiff) ? maxDiff:blueDiff;
        maxDiff += 128;
        if(maxDiff < 0){
            maxDiff = 0;
        }else if(maxDiff>255){
            maxDiff=255;
        }
        return maxDiff;
    }

    private void writeOut() throws IOException {
        StringBuilder outPut = new StringBuilder();
        outPut.append("P3\n").append(width).append(" ").append(height).append(" ").append(maxColor).append(" ");
        for(Pixel[] row:picture){
            for(Pixel pixel:row){
                outPut.append(pixel.toString());
            }
        }
        out.write(outPut.toString());
        out.close();
    }

    private void generatePic() {
        skipComment();
        in.next();
        skipComment();
        width = nextInt();
        height = nextInt();
        maxColor = nextInt();
        picture = new Pixel[height][width];
        for(int i = 0; i < height; i++){
            for (int k = 0;k<width;k++){
                picture[i][k] = new Pixel(nextInt(),nextInt(),nextInt());
            }
        }
        in.close();
    }

    private void skipComment(){
        while(in.hasNext("#[^\\n]*")){
            in.nextLine();
        }
    }

    private int nextInt(){
        skipComment();
        return in.nextInt();
    }

    private void usage(){
        System.out.println("USAGE: java ImageEditor inputFileName outputFileName {grayscale|invert|emboss|motionblur blurLength }");
    }


    private class Pixel {
        public int red,green,blue;

        public Pixel(int red, int green, int blue){
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        public void inverse() {
            this.red = 255 - this.red;
            this.green = 255 - this.green;
            this.blue = 255 - this.blue;
        }

        public void grayscale() {
            int avg = (this.red+this.green+this.blue)/3;
            this.red=this.green=this.blue=avg;
        }

        public String toString(){
            return this.red + " " + this.green+" "+this.blue+"\n";
        }

        public void emboss(int i) {
            this.red=this.green=this.blue=i;
        }

        public void motionBlur(Pixel p) {
            this.red = p.red;
            this.green = p.green;
            this.blue = p.blue;
        }
    }
}

