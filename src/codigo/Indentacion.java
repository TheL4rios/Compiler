package codigo;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Element;

/**
 *
 * @author Larios
 */
public class Indentacion extends DocumentFilter {
    public void insertarString(DocumentFilter.FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {
        if("\n".equals(str)) {
            str = addWhiteSpace(fb.getDocument(), offs);
        }
        
        super.insertString(fb, offs, str, a);
    }
    
    @Override
    public void replace(DocumentFilter.FilterBypass fb, int offs, int length, String str, AttributeSet a) throws BadLocationException {
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
}
