package codigo;

import javax.swing.table.DefaultTableModel;
import static codigo.Metodos.llenarTabla;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author jesus
 */
public class TablaSimbolos extends javax.swing.JFrame {

    private DefaultTableModel modelo = new DefaultTableModel();
    private String[][] palRes, vest, func, opAsig, opAgr, opRel, opArt, sigPunt, tipoDato, estCont;
    DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();

    public TablaSimbolos(Compilador compilador) {
        initComponents();
        inicializar(compilador);

    }

    private void inicializar(Compilador compilador) {
        setResizable(false);
        tblSimbolos.setFillsViewportHeight(true);
        headerRenderer.setBackground(Color.decode("#74da77"));

        setTitle("Tabla de símbolos");
        modelo = (DefaultTableModel) tblSimbolos.getModel();

        palRes = new String[][]{{"ident", "bloque_ident"},
        {"cuerpo", "bloque_cuerpo"}
        };

        vest = new String[][]{{"SERVO_1", "vest_servo1"},
        {"SERVO_2", "vest_servo2"},
        {"SERVO_3", "vest_servo3"},
        {"SERVO_4", "vest_servo4"},
        {"SPROX_NORTE", "vest_sproxN"},
        {"SPROX_SUR", "vest_sproxS"},
        {"SPROX_OESTE", "vest_sproxO"},
        {"SPROX_ESTE", "vest_sproxE"},
        {"CENTIM", "vest_centim"},
        {"METRO", "vest_metro"},
        {"PIE", "vest_pie"},
        {"VERDAD", "vest_verdad"},
        {"FALSO", "vest_falso"}
        };

        func = new String[][]{{"girar", "fun_girar"},
        {"dist", "fun_dist"},
        {"agarrar", "fun_agarrar"},
        {"soltar", "fun_soltar"},
        {"reset", "fun_reset"}};

        opAsig = new String[][]{{"=", "opAsig"}
        };

        opAgr = new String[][]{{"{", "llave_a"},
        {"}", "llave_c"},
        {"(", "parent_a"},
        {")", "parent_c"}
        };

        opRel = new String[][]{{">", "opRel_may"},
        {"<", "opRel_men"},
        {">=", "opRel_mayIg"},
        {"<=", "opRel_menIg"},
        {"==", "opRel_ig"}
        };

        opArt = new String[][]{{"-", "opArt_resta"},
        {"+", "opArt_suma"},
        {"*", "opArt_mult"},
        {"/", "opArt_div"}};

        sigPunt = new String[][]{{";", "punto_coma"},
        {",", "coma"}};

        tipoDato = new String[][]{{"grado", "tipoDato_grado"},
        {"num", "tipoDato_num"},
        {"bool", "tipoDato_bool"},
        {"sprox", "tipoDato_sprox"},
        {"servo", "tipoDato_servo"}};

        estCont = new String[][]{{"ciclo", "estCont_ciclo"},
        {"bucle", "estCont_bucle"}};

        cmbOpcion.addItem("Selecciona una opción");
        String[] menu = {"Palabras reservadas", "Variables estáticas", "Funciones", "Operadores de Asignación",
            "Operadores de Agrupación", "Operadores relacionales", "Operadores aritméticos",
            "Signos de puntuación", "Tipos de dato", "Estructuras de control"};
        for (String item : menu) {
            cmbOpcion.addItem(item);
        }

        setLocation(compilador.getX(), compilador.getY() + 50);
        setAlwaysOnTop(true);

        addWindowListener(new WindowAdapter() {//Cuando presiona la "X" de la esquina superior derecha
            @Override
            public void windowClosing(WindowEvent e) {
                Compilador.TS = null;
            }
        });
        try {
            setIconImage(new ImageIcon(getClass().getResource("/iconos/logoC&C.png")).getImage());
        } catch (Exception e) {
            System.out.println("Error al colocar el logo en la ventana, se colocará el icono por defecto.");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        cmbOpcion = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSimbolos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setBackground(new java.awt.Color(63, 63, 63));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cmbOpcion.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        cmbOpcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbOpcionActionPerformed(evt);
            }
        });
        jPanel1.add(cmbOpcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        tblSimbolos.setBackground(new java.awt.Color(0, 0, 0));
        tblSimbolos.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        tblSimbolos.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        tblSimbolos.setForeground(new java.awt.Color(255, 255, 255));
        tblSimbolos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Columna 1", "Columna 2"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblSimbolos);
        if (tblSimbolos.getColumnModel().getColumnCount() > 0) {
            tblSimbolos.getColumnModel().getColumn(0).setResizable(false);
            tblSimbolos.getColumnModel().getColumn(1).setResizable(false);
        }

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 350, 320));

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbOpcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbOpcionActionPerformed
        String[] nc = {"Lexema", "Componente léxico"};
        switch (cmbOpcion.getSelectedIndex()) {
            case 0:
                modelo.setRowCount(0);
                modelo.setColumnCount(0);
                break;
            case 1:
                llenarTabla(modelo, palRes, nc);
                for (int i = 0; i < tblSimbolos.getModel().getColumnCount(); i++) {
                    tblSimbolos.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
                }
                break;
            case 2:
                llenarTabla(modelo, vest, nc);
                for (int i = 0; i < tblSimbolos.getModel().getColumnCount(); i++) {
                    tblSimbolos.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
                }
                break;
            case 3:
                llenarTabla(modelo, func, nc);
                for (int i = 0; i < tblSimbolos.getModel().getColumnCount(); i++) {
                    tblSimbolos.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
                }
                break;
            case 4:
                llenarTabla(modelo, opAsig, nc);
                for (int i = 0; i < tblSimbolos.getModel().getColumnCount(); i++) {
                    tblSimbolos.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
                }
                break;
            case 5:
                llenarTabla(modelo, opAgr, nc);
                for (int i = 0; i < tblSimbolos.getModel().getColumnCount(); i++) {
                    tblSimbolos.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
                }
                break;
            case 6:
                llenarTabla(modelo, opRel, nc);
                for (int i = 0; i < tblSimbolos.getModel().getColumnCount(); i++) {
                    tblSimbolos.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
                }
                break;
            case 7:
                llenarTabla(modelo, opArt, nc);
                for (int i = 0; i < tblSimbolos.getModel().getColumnCount(); i++) {
                    tblSimbolos.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
                }
                break;
            case 8:
                llenarTabla(modelo, sigPunt, nc);
                for (int i = 0; i < tblSimbolos.getModel().getColumnCount(); i++) {
                    tblSimbolos.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
                }
                break;
            case 9:
                llenarTabla(modelo, tipoDato, nc);
                for (int i = 0; i < tblSimbolos.getModel().getColumnCount(); i++) {
                    tblSimbolos.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
                }
                break;
            case 10:
                llenarTabla(modelo, estCont, nc);
                for (int i = 0; i < tblSimbolos.getModel().getColumnCount(); i++) {
                    tblSimbolos.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
                }
                break;
        }
    }//GEN-LAST:event_cmbOpcionActionPerformed

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
    private javax.swing.JComboBox<String> cmbOpcion;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblSimbolos;
    // End of variables declaration//GEN-END:variables
}
