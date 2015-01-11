package spell;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Matt on 1/10/2015.
 */
public class Spell implements SpellCorrector {

    private Trie dictionary = new myTrie();
    /**
     * Tells this <code>SpellCorrector</code> to use the given file as its dictionary
     * for generating suggestions.
     *
     * @param dictionaryFileName File containing the words to be used
     * @throws java.io.IOException If the file cannot be read
     */
    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        Scanner in = new Scanner(new File(dictionaryFileName));
        while(in.hasNext()){
            dictionary.add(in.next());
        }
        in.close();
    }

    /**
     * Suggest a word from the dictionary that most closely matches
     * <code>inputWord</code>
     *
     * @param inputWord
     * @return The suggestion
     * @throws spell.SpellCorrector.NoSimilarWordFoundException If no similar word is in the dictionary
     */
    @Override
    public String suggestSimilarWord(String inputWord) throws NoSimilarWordFoundException {
        Trie.Node temp = dictionary.find(inputWord);
        if(temp != null){
            return inputWord;
        }
        ArrayList<String> list = getEditWords(inputWord);
        Map<String,Integer> words = validate(list);

        if(words.size() == 0){
            words = validateAgain(list);
            if(words.size() == 0){
                throw new NoSimilarWordFoundException();
            }
        }

        int maxLen = 0;
        for(String word:words.keySet()){
            maxLen = words.get(word) > maxLen ? words.get(word):maxLen;
        }

        ArrayList<String> validList = new ArrayList<String>();
        for(String word :words.keySet()){
            if(words.get(word)==maxLen){
                validList.add(word);
            }
        }

        Collections.sort(validList);
        return validList.get(0);
    }

    private Map<String, Integer> validateAgain(ArrayList<String> list) {
        ArrayList<String> newList = new ArrayList<String>();
        for(String word:list){
            newList.addAll(getEditWords(word));
        }
        return validate(newList);
    }

    private Map<String, Integer> validate(ArrayList<String> list) {
        Map<String,Integer> map = new HashMap<String, Integer>();
        Trie.Node temp;
        for(String word :list){
            temp = dictionary.find(word);
            if(temp != null){
                map.put(word,temp.getValue());
            }
        }
        return map;
    }

    private ArrayList<String> getEditWords(String inputWord) {
        ArrayList<String> list = new ArrayList<String>();
        list.addAll(deleteWords(inputWord));
        list.addAll(transpositionWords(inputWord));
        list.addAll(insAltWords(inputWord, 1));
        list.addAll(insAltWords(inputWord, 0));
        return list;
    }

    private ArrayList<String> transpositionWords(String inputWord) {
        ArrayList<String> list = new ArrayList<String>();
        for(int i = 0;i<inputWord.length()-1;i++){
            list.add(inputWord.substring(0,i)+inputWord.charAt(i+1)+inputWord.charAt(i)+inputWord.substring(i+2));
        }
        return list;
    }

    private ArrayList<String> insAltWords(String inputWord, int ins) {
        ArrayList<String> list = new ArrayList<String>();
        for(int j = 0;j<26;j++){
            char c = (char) ('a' + j);
            for(int i = 0;i<inputWord.length();i++) {
                list.add(inputWord.substring(0, i) + c + inputWord.substring(i + ins));
            }
            if(ins == 0){
                list.add(inputWord + c);
            }
        }
        return list;
    }

    private ArrayList<String> deleteWords(String inputWord) {
        ArrayList<String> list = new ArrayList<String>();
        for(int i = 0;i<inputWord.length();i++){
            list.add(inputWord.substring(0,i)+inputWord.substring(i+1));
        }
        return list;
    }
}
