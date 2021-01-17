package test;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Element;

/**
 *
 * @author Larios
 */
public class TestAutoIndentacion extends DocumentFilter{
    public void insertarString(FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {
        if("\n".equals(str)) {
            str = addWhiteSpace(fb.getDocument(), offs);
        }
        
        super.insertString(fb, offs, str, a);
    }
    
    @Override
    public void replace(FilterBypass fb, int offs, int length, String str, AttributeSet a) throws BadLocationException {
        if("\n".equals(str)) {
            str = addWhiteSpace(fb.getDocument(), offs);
        }
        
        super.insertString(fb, offs, str, a);
    }
    
    private String addWhiteSpace(Document doc, int offset) throws BadLocationException {
        StringBuilder whiteSpace = new StringBuilder("\n");
        Element rootElement = doc.getDefaultRootElement();
        int line = rootElement.getElementIndex(offset);
        int i = rootElement.getElement(line).getStartOffset();
        
        while(true) {
            String temp = doc.getText(i, 1);
            if (temp.equals(" ") || temp.equals("\t")) { 
                whiteSpace.append(temp);
                i++;
            } else {
                break;
            }
        }
        
        return whiteSpace.toString();
    }
    
    private static void createAndShowUI() {
        JTextArea txt = new JTextArea(5, 50);
        AbstractDocument doc = (AbstractDocument) txt.getDocument();
        doc.setDocumentFilter(new TestAutoIndentacion());
        
        JFrame frame = new JFrame("test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JScrollPane(txt));
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
    
    public static void main(String[] a) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                createAndShowUI();
            }
        });
    }
}
