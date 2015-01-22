package hangman;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by Matt on 1/16/2015.
 */
public class Hangman {

    public static int remainingGuesses;
    public static String dictionary;
    public static int wordLength;
    public static Set<String> words;
    public static String word;
    public static myHangman game;

    public static void main(String[] args){
        remainingGuesses = Integer.parseInt(args[2]);
        wordLength = Integer.parseInt(args[1]);
        dictionary = args[0];
        Boolean win = false;

        game = new myHangman();
        game.startGame(new File(dictionary),wordLength);
        word = game.getGuessedWord();
        while (remainingGuesses > 0 && !win){
            printStuff();
            System.out.print("Enter guess: ");
            Scanner in = new Scanner(System.in);
            char guess = in.next().charAt(0);
            while(!Character.isAlphabetic(guess)){
                System.out.println("Guess must be a letter. Try again.");
                System.out.print("Enter guess: ");
                guess = in.next().charAt(0);
            }
            try {
                words = game.makeGuess(guess);
            } catch (IEvilHangmanGame.GuessAlreadyMadeException e) {
                System.out.println("You already guessed that letter.\n");
                continue;
            }
            if(word.equals(game.getGuessedWord())){
               System.out.println("Sorry, there are no "+guess+"'s\n");
                remainingGuesses--;
            }else{
                int count = 0;
                word = game.getGuessedWord();
                for(char c:word.toCharArray()){
                    if(c==guess){
                        count++;
                    }
                }
                String response = count > 1 ? "are "+count+" "+guess+"'s":"is 1 " + guess;
                if(!word.contains("-")){
                    win = true;
                    break;
                }
                System.out.println("There "+response+"\n");
            }

        }
        if(!win){
            for(String string:words){
                word = string;
                break;
            }
            System.out.println("You lose!");
            System.out.println("The word was: "+word);
        }else{
            System.out.println("You Win!\nThe word was "+word);
        }
    }

    private static void usage() {
        System.out.println("Usage: java [your main class name] dictionary wordLength guesses\n");
    }

    private static void printStuff() {
        String guesses =  remainingGuesses == 1 ? " guess left":" guesses left";
        System.out.println("You have " + remainingGuesses +guesses);
        //letters guessed
        System.out.println("Used letters: "+game.getUsedLetters());
        //word so far
        System.out.println("Word: "+word);

    }
}
