package suggestions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Larios
 */

public class Node {
    public Map<Character, Node> node;
    public boolean endWord;
    public ArrayList<String> words;
    
    public Node() {
        node = new HashMap<>();
        endWord = false;
        words = new ArrayList<>();
    }
}
