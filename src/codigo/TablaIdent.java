package codigo;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import static codigo.Metodos.eliminarIdentRep;
/**
 * @author jesus
 */
public class TablaIdent extends javax.swing.JFrame {
    static public DefaultTableModel modelo = new DefaultTableModel();
    
    JTableHeader header;
    DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();

    public TablaIdent(Compilador compilador) {
        initComponents();  
        inicializar(compilador);
        
    }
      
    private void inicializar(Compilador compilador){
        setResizable(false);
        setTitle("Tabla de identificadores");
        modelo = (DefaultTableModel) tblIdent.getModel();
        tblIdent.setFillsViewportHeight(true);//se usa para el color de la tabla
        headerRenderer.setBackground(Color.decode("#74da77"));
        for (int i = 0; i < tblIdent.getModel().getColumnCount(); i++) {
            tblIdent.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        
        setLocation(compilador.getX()+compilador.getSize().width-getSize().width, compilador.getY()+50);
        setAlwaysOnTop(true);
        
        addWindowListener(new WindowAdapter(){//Cuando presiona la "X" de la esquina superior derecha
            @Override
            public void windowClosing(WindowEvent e){         
                Compilador.TI = null;
            }
         });
        try{
            setIconImage(new ImageIcon(getClass().getResource("/iconos/logoC&C.png")).getImage());
        }catch(Exception e){
            System.out.println("Error al colocar el logo en la ventana, se colocará el icono por defecto.");
        }
    }
    
    public void llenarTablaIdent(int cantErrores){
        modelo.setRowCount(0);
//        if(cantErrores == 0){
            ArrayList<Object[]> identificadores = Compilador.identificadores;
            eliminarIdentRep(identificadores);
            for(int i=0; i<identificadores.size(); i++)
                modelo.addRow(identificadores.get(i));
//        }
    }    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblIdent = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setBackground(new java.awt.Color(63, 63, 63));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblIdent.setBackground(new java.awt.Color(0, 0, 0));
        tblIdent.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        tblIdent.setForeground(new java.awt.Color(255, 255, 255));
        tblIdent.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tipo de dato", "Nombre", "Valor(es)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblIdent);
        if (tblIdent.getColumnModel().getColumnCount() > 0) {
            tblIdent.getColumnModel().getColumn(0).setResizable(false);
            tblIdent.getColumnModel().getColumn(0).setPreferredWidth(1);
            tblIdent.getColumnModel().getColumn(1).setResizable(false);
            tblIdent.getColumnModel().getColumn(1).setPreferredWidth(10);
            tblIdent.getColumnModel().getColumn(2).setResizable(false);
        }

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 510, 320));

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TablaSimbolos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblIdent;
    // End of variables declaration//GEN-END:variables
}
