package suggestions;

import java.awt.Frame;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Larios
 */

public class Suggestions {
    private Trie t;
    
    public Suggestions() {
        t = new Trie();
    }
    
    public void showSuggestions(JTextPane txt, int cursorPosition, int x, int y, KeyEvent e, Frame main) {  
        if (Character.isLetter(e.getKeyChar()) || Character.isDigit(e.getKeyChar()) ||
                e.getKeyCode() == 45 || // _
                e.getKeyCode() == KeyEvent.VK_DOWN || 
                e.getKeyCode() == KeyEvent.VK_UP || 
                e.getKeyCode() == KeyEvent.VK_ENTER ||
                e.getKeyCode() == KeyEvent.VK_SHIFT ||
                e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP || 
                    e.getKeyCode() == KeyEvent.VK_ENTER ) {
                return;
            }
            
            String lastWord = getLastWord(txt.getText(), cursorPosition - 1);
            ArrayList<String> suggestions = t.getSuggestions(lastWord);
            if (suggestions != null) {
                if (!suggestions.isEmpty()) {
                    if (PopupUtil.isVisible())
                        PopupUtil.hidePopup();
                    
                    //y = y + txt.getHeight() + 20;
                    
                    Point where = new Point(x, y);
                    SwingUtilities.convertPointToScreen(where, txt);

                    PopupUtil.showPopup(new PopupComponent(txt, cursorPosition, lastWord, suggestions), main, where.x, where.y, true, txt.getHeight());
                }
                
                if (suggestions.isEmpty()) {
                    PopupUtil.hidePopup();
                }
            } else {
                PopupUtil.hidePopup();
            }
            
            if (PopupUtil.isVisible()/* && !select*/) 
                txt.requestFocus();
        }else {
            PopupUtil.hidePopup();
        }
    }
    
    private String getLastWord(String txt, int cursorPosition) {
        String word = "";
        int counter = 0;
        StringTokenizer token = new StringTokenizer(txt, "\r\n\t +-*/(){}<>;:.,[]=&|\\\"", true);
        while (token.hasMoreElements()) {
            String t =  token.nextToken();
            counter = counter + t.length();
            if (counter == cursorPosition + 1) {
                return t;
            }
        }

        return "";
    }
    
//    public void loadFile(String path) {
//    
//    }
    
    public void setWords(ArrayList<String> words) {
        words.forEach(word -> {
            t.add(word);
        });
    }
}
