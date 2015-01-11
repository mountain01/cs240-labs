package spell;

/**
 * Created by Matt on 1/10/2015.
 */
public class myTrie implements Trie {

    private Node root = new Node();
    private int wordCount=0;
    private int nodeCount = 0;
    /**
     * Adds the specified word to the trie (if necessary) and increments the word's frequency count
     *
     * @param word The word being added to the trie
     */
    @Override
    public void add(String word) {
        Node temp = root;
        for (char c : word.toLowerCase().toCharArray()) {
            int index = c -'a';
            if (temp.nodes[index] == null) {
                temp.nodes[index] = new Node();
                nodeCount++;
            }
            temp = temp.nodes[index];
        }
        temp.setValue(temp.getValue() + 1);
        wordCount++;
    }

    /**
     * Searches the trie for the specified word
     *
     * @param word The word being searched for
     * @return A reference to the trie node that represents the word,
     * or null if the word is not in the trie
     */
    @Override
    public Node find(String word) {
        Node temp = root;
        for(char c : word.toLowerCase().toCharArray()){
            int index = c-'a';
            if(temp.nodes[index] != null){
                temp = temp.nodes[index];
            } else {
                return null;
            }
        }
        return temp.getValue() > 0 ? temp:null;
    }

    /**
     * Returns the number of unique words in the trie
     *
     * @return The number of unique words in the trie
     */
    @Override
    public int getWordCount() {
        return wordCount;
    }

    /**
     * Returns the number of nodes in the trie
     *
     * @return The number of nodes in the trie
     */
    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    public class Node implements Trie.Node {

        private Node[] nodes = new Node[26];
        private int wordCount = 0;
        /**
         * Returns the frequency count for the word represented by the node
         *
         * @return The frequency count for the word represented by the node
         */
        @Override
        public int getValue() {
            return wordCount;
        }

        public void setValue(int number){
            wordCount = number;
        }
    }
}
