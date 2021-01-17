package suggestions;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;

/**
 *
 * @author Dusan Balek, modified by Larios
 */
public class PopupComponent extends JPanel {
    
    private JLabel jLabel;
    private JList jList;
    private JScrollPane jScrollPane;
    private JTextPane txt;
    private int cursorPosition;
    private String lastWord;
    
    public PopupComponent(JTextPane txt, int cursorPosition, String lastWord, ArrayList<String> suggestions) {
        this.cursorPosition = cursorPosition;
        this.lastWord = lastWord;
        this.txt = txt;
        initComponents();
        setFocusable(false);
        setNextFocusableComponent(jList);
        setBackground(jList.getBackground());
        jScrollPane.setBackground(jList.getBackground());
        jList.setModel(createModel(suggestions));
        jList.setSelectedIndex(0);
        jList.setVisibleRowCount(suggestions.size() > 16 ? 16 : suggestions.size());
        jList.setCellRenderer(new Renderer(jList));
        jList.grabFocus();
        jList.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                PopupUtil.hidePopup();
            }
        });
    }
    
    private void initComponents() {
        jLabel = new JLabel();
        jList = new JList();
        jScrollPane = new JScrollPane();
        
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setLayout(new java.awt.BorderLayout());

        jScrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        jList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                invokeSelected();
            }
        });
        jList.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                listMouseMoved(evt);
            }
        });
//        jList.addKeyListener(new java.awt.event.KeyAdapter() {
//            @Override
//            public void keyPressed(java.awt.event.KeyEvent evt) {
//                listKeyPressed(evt);
//            }
//        });
        jScrollPane.setViewportView(jList);

        add(jScrollPane, java.awt.BorderLayout.CENTER);
    }
    
    private DefaultListModel createModel(ArrayList<String> generators) {
        DefaultListModel model = new DefaultListModel();
        for (String generator : generators)
            model.addElement(generator);
        return model;
    }
    
    private void listMouseMoved(java.awt.event.MouseEvent evt) {
        int i = jList.locationToIndex(evt.getPoint());
        if (i != jList.getSelectedIndex())
            jList.setSelectedIndex(i);
    }
    
    public void moveDown() {
        int size = jList.getModel().getSize();
        if (size > 0) {
            int idx = (jList.getSelectedIndex() + 1) % size;
            if (idx == size)
                idx = 0;
            jList.setSelectedIndex(idx);
            jList.ensureIndexIsVisible(idx);
        }   
    }
    
    public void moveUp() {
        int size = jList.getModel().getSize();
        if (size > 0) {
            int idx = (jList.getSelectedIndex() - 1 + size) % size;
            jList.setSelectedIndex(idx);
            jList.ensureIndexIsVisible(idx); 
        }    
    }
    
//    private void listKeyPressed(java.awt.event.KeyEvent evt) {
//        KeyStroke ks = KeyStroke.getKeyStrokeForEvent(evt);
//        switch (ks.getKeyCode()) {
//            case KeyEvent.VK_ENTER:
//                invokeSelected();
//                break;
//            case KeyEvent.VK_DOWN:
//                {
//                    int size = jList.getModel().getSize();
//                    if (size > 0) {
//                        int idx = (jList.getSelectedIndex() + 1) % size;
//                        if (idx == size)
//                            idx = 0;
//                        jList.setSelectedIndex(idx);
//                        jList.ensureIndexIsVisible(idx);
//                    }   
//                    evt.consume();
//                    break;
//                }
//            case KeyEvent.VK_UP:
//                {
//                    int size = jList.getModel().getSize();
//                    if (size > 0) {
//                        int idx = (jList.getSelectedIndex() - 1 + size) % size;
//                        jList.setSelectedIndex(idx);
//                        jList.ensureIndexIsVisible(idx); 
//                    }    
//                    evt.consume();
//                    break;
//                }
//            default:
//                {
//                    if (Character.isLetter(evt.getKeyCode()) | Character.isDigit(evt.getKeyCode())) {
//                        this.txt.requestFocus();
//                    }
//                }
//                break;
//        }
//    }
    
    public void invokeSelected() {
        PopupUtil.hidePopup();
        Object value = jList.getSelectedValue();
        
        txt.setText(txt.getText().substring(0, cursorPosition - lastWord.length()) + 
                    value + 
                    txt.getText().substring(cursorPosition, txt.getText().length()));

        txt.requestFocus();
        txt.setCaretPosition(cursorPosition - lastWord.length() + value.toString().length());
        //System.out.println(value.toString());
    }
    
    private static class Renderer extends DefaultListCellRenderer {
        
        private static int DARKER_COLOR_COMPONENT = 5;
        
        private Color fgColor;
        private Color bgColor;
        private Color bgColorDarker;
        private Color bgSelectionColor;
        private Color fgSelectionColor;
        
        public Renderer(JList list) {
            setFont(list.getFont());
            fgColor = list.getForeground();
            bgColor = list.getBackground();
            bgColorDarker = new Color(Math.abs(bgColor.getRed() - DARKER_COLOR_COMPONENT),
                    Math.abs(bgColor.getGreen() - DARKER_COLOR_COMPONENT),
                    Math.abs(bgColor.getBlue() - DARKER_COLOR_COMPONENT));
            bgSelectionColor = list.getSelectionBackground();
            fgSelectionColor = list.getSelectionForeground();
        }
        
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index,
                boolean isSelected, boolean hasFocus) {
            if (isSelected) {
                setForeground(Color.WHITE);
                setBackground(Color.BLUE);
            } else {
                setForeground(fgColor);
                setBackground(index % 2 == 0 ? bgColor : bgColorDarker);
            }            
            setText(value instanceof String ? ((String)value) : value.toString());
            return this;
        }        
    }
}
