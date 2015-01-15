package spell;

import java.util.Arrays;

/**
 * Created by Matt on 1/10/2015.
 */
public class myTrie implements ITrie {

    private Node root = new Node();
    private int wordCount=0;
    private int nodeCount = 1;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof myTrie)) return false;

        myTrie myTrie = (myTrie) o;

        if (nodeCount == myTrie.nodeCount && wordCount == myTrie.wordCount){
            return root.equals(myTrie.root);
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = root != null ? root.hashCode() : 0;
        result = 31 * result + wordCount;
        result = 31 * result + nodeCount;
        return result;
    }

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
        temp.setWord(word.toLowerCase());
    }

    /**
     * Searches the trie for the specified word
     *
     * @param word The word being searched for
     * @return A reference to the trie node that represents the word,
     * or null if the word is not in the trie
     */
    @Override
    public INode find(String word) {
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

    public String toString(){
        StringBuilder string;
        string = getString(root);
        return string.toString();
    }

    private StringBuilder getString(Node root) {
        StringBuilder string = new StringBuilder();
        for(Node node:root.nodes){
            if(node != null){
                if(node.wordCount >0){
                    string.append(node.word).append("\n");
                }
                string.append(getString(node));
            }
        }
        return string;
    }

    public class Node implements INode {

        private Node[] nodes = new Node[26];
        private int wordCount = 0;
        private String word;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node)) return false;

            Node node = (Node) o;

            if (wordCount != node.wordCount) return false;
            if (!Arrays.equals(nodes, node.nodes)) return false;
            if (word != null ? !word.equals(node.word) : node.word != null) return false;
            for(int i = 0;i<26;i++){
                if(this.nodes[i] == null && node.nodes[i] != null){
                    return false;
                }
                if(this.nodes[i] != null && node.nodes[i] == null){
                    return false;
                }
                if(this.nodes[i] == null && node.nodes[i] == null){
                    continue;
                }
                if(!this.nodes[i].equals(node.nodes[i])){
                    return false;
                }
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = nodes != null ? Arrays.hashCode(nodes) : 0;
            result = 31 * result + wordCount;
            result = 31 * result + (word != null ? word.hashCode() : 0);
            return result;
        }

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

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }
    }
}
