package suggestions;

import java.util.ArrayList;

/**
 *
 * @author Larios
 */

public class Trie {
    
    private Node root;
    private String word;
    private int wordSize;
    
    public Trie() {
        root = new Node();
    }
    
    public Node getRoot() {
        return root;
    }
    
    public void add(String word) {
        Node current = root;
        for (char l: word.toCharArray()) {
            current = current.node.computeIfAbsent(l, c -> new Node());
            current.words.add(word);
        }
        current.endWord = true;
    }
    
    public ArrayList<String> getSuggestions(String word) {
        Node current = root;
        for (char l: word.toCharArray()) {
            if (current.node.containsKey(l)) {
                current = current.node.get(l);
            } else {
                return null;
            }
        }
        return current.words;
    }
    
    public void showAll(Node t, String word) {
        if(t.endWord) {
            System.out.println(word);
        }

        t.node.entrySet().forEach((entry) -> {
            showAll(entry.getValue(), word + entry.getKey());
        });
    }
}
