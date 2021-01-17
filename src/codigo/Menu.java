package codigo;

import java.awt.Color;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 *
 * @author Yisus, DigitalMonster
 */
public class Menu extends javax.swing.JFrame {

    JFileChooser seleccionado = new JFileChooser();
    File archivo;
    final String titulo = "RoboTec";
    private final Border borderRadius = new LineBorder(Color.black, 2, true),
            borderRadiusF = new LineBorder(Color.black, 2, false);

    //Compilador CF = new Compilador();

    public Menu() {
        initComponents();
        inicializar();
    }

    private void inicializar() {
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle(titulo);
        lblLogo.setToolTipText("Compilador " + titulo);
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
        btnNuevo = new javax.swing.JButton();
        btnAbrir = new javax.swing.JButton();
        lblLogo = new javax.swing.JLabel();
        lblNuevo = new javax.swing.JLabel();
        lblAbrir = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setBackground(new java.awt.Color(63, 63, 63));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnNuevo.setFont(new java.awt.Font("Arial Black", 1, 11)); // NOI18N
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/nuevodoc32.png"))); // NOI18N
        btnNuevo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        btnNuevo.setContentAreaFilled(false);
        btnNuevo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNuevoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnNuevoMouseExited(evt);
            }
        });
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        jPanel1.add(btnNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 50, 48));

        btnAbrir.setFont(new java.awt.Font("Arial Black", 1, 11)); // NOI18N
        btnAbrir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/adjuntar232.png"))); // NOI18N
        btnAbrir.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        btnAbrir.setContentAreaFilled(false);
        btnAbrir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAbrirMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAbrirMouseExited(evt);
            }
        });
        btnAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirActionPerformed(evt);
            }
        });
        jPanel1.add(btnAbrir, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 30, 50, 48));

        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/LogoC&C.png"))); // NOI18N
        jPanel1.add(lblLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 70, 70));

        lblNuevo.setFont(new java.awt.Font("Consolas", 1, 12)); // NOI18N
        lblNuevo.setForeground(new java.awt.Color(255, 255, 255));
        lblNuevo.setText("Nuevo archivo");
        jPanel1.add(lblNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        lblAbrir.setFont(new java.awt.Font("Consolas", 1, 12)); // NOI18N
        lblAbrir.setForeground(new java.awt.Color(255, 255, 255));
        lblAbrir.setText("Abrir archivo");
        jPanel1.add(lblAbrir, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 90, -1, -1));

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        dispose();
        new Compilador().setVisible(true);
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbrirActionPerformed
        Directorio dir = new Directorio();
        Compilador compF = new Compilador();
        compF.dir = dir;
        if (dir.Abrir(compF)) {
            compF.setVisible(true);
            compF.AnalisisColorEnter();
            compF.jtpCode.setCaretPosition(0);
            dispose();
        }
    }//GEN-LAST:event_btnAbrirActionPerformed

    private void btnNuevoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNuevoMouseEntered
        btnNuevo.setOpaque(true);
        btnNuevo.setBorder(borderRadiusF);
        btnNuevo.setBackground(Color.decode("#74da77"));
    }//GEN-LAST:event_btnNuevoMouseEntered

    private void btnAbrirMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAbrirMouseEntered
        btnAbrir.setOpaque(true);
        btnAbrir.setBorder(borderRadiusF);
        btnAbrir.setBackground(Color.decode("#74da77"));
    }//GEN-LAST:event_btnAbrirMouseEntered

    private void btnNuevoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNuevoMouseExited
        btnNuevo.setOpaque(false);
        btnNuevo.setBorder(borderRadius);
    }//GEN-LAST:event_btnNuevoMouseExited

    private void btnAbrirMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAbrirMouseExited
        btnAbrir.setOpaque(false);
        btnAbrir.setBorder(borderRadius);
    }//GEN-LAST:event_btnAbrirMouseExited
    
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
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
                System.out.println("Error en LookAndFeel: " + e.getMessage());
            }
            new Menu().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbrir;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblAbrir;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblNuevo;
    // End of variables declaration//GEN-END:variables
}
