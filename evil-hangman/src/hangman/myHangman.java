package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by Matt on 1/16/2015.
 */
public class myHangman implements IEvilHangmanGame {

    private Set<String> wordList = new HashSet<String>();
    private Set<Character> guessedLetters = new TreeSet<Character>();

    public String getGuessedWord() {
        return guessedWord;
    }

    private String guessedWord;
    /**
     * Starts a new game of evil hangman using words from <code>dictionary</code>
     * with length <code>wordLength</code>.
     * <p/>
     * This method should set up everything required to play the game,
     * but should not actually play the game. (ie. There should not be
     * a loop to prompt for input from the user.)
     *
     * @param dictionary Dictionary of words to use for the game
     * @param wordLength Number of characters in the word to guess
     */
    @Override
    public void startGame(File dictionary, int wordLength) {
        try {
            Scanner in = new Scanner(dictionary);
            while(in.hasNext()){
                String word = in.next();
                if(word.length() == wordLength){
                    wordList.add(word);
                }
            }
            StringBuilder string = new StringBuilder();
            for(int i = 0;i<wordLength;i++){
                string.append("-");
            }
            guessedWord = string.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getUsedLetters(){
        StringBuilder string = new StringBuilder();
        for(char c:guessedLetters){
            string.append(c).append(" ");
        }
        return string.toString();
    }

    /**
     * Make a guess in the current game.
     *
     * @param guess The character being guessed
     * @return The set of strings that satisfy all the guesses made so far
     * in the game, including the guess made in this call. The game could claim
     * that any of these words had been the secret word for the whole game.
     * @throws hangman.IEvilHangmanGame.GuessAlreadyMadeException If the character <code>guess</code>
     *                                   has already been guessed in this game.
     */
    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        if(guessedLetters.contains(guess)){
            throw new GuessAlreadyMadeException();
        }else{
            guessedLetters.add(guess);
        }
        Map<String,Set<String>> sets = new HashMap<String,Set<String>>();
        for(String word:wordList){
            String key = generateKey(guess,word);
            if(!sets.keySet().contains(key)){
                sets.put(key,new HashSet<String>());
            }
            sets.get(key).add(word);
        }
        sets = getLargestSets(sets);
        wordList = prioritySet(sets,guess);
        return wordList;
    }

    private Set<String> prioritySet(Map<String, Set<String>> sets, char guess) {
        int max = Integer.MAX_VALUE;
        String myKey="";
        for(String key:sets.keySet()){
            int count = 0;
            int weight =1;
            int total = 0;
            for(int i = key.length()-1;i>=0;i--){
                if(key.charAt(i)==guess){
                    total+=++count*weight;
                }
                weight*=2;
            }
            if(total < max){
                myKey = key;
                max = total;
            }
        }
        guessedWord = myKey;
        return sets.get(myKey);
    }

    private Map<String, Set<String>> getLargestSets(Map<String, Set<String>> sets) {
        Map<String,Set<String>> newSet = new HashMap<String, Set<String>>();
        int max = 0;
        for(Set<String> set:sets.values()){
            max = set.size() > max ? set.size():max;
        }

        for(String key:sets.keySet()){
            Set<String> set = sets.get(key);
            if(set.size()==max){
                newSet.put(key,set);
            }
        }
        return newSet;
    }

    private String generateKey(char guess, String word) {
        StringBuilder key = new StringBuilder();
        for(int i = 0;i<word.length();i++){
            char c = word.charAt(i);
            if(guess==c){
                key.append(guess);
            }else{
                key.append(guessedWord.charAt(i));
            }
        }
        return key.toString();
    }
}
