package codigo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import static codigo.Metodos.macro;
import static codigo.Metodos.colorTextPane;
import static codigo.Metodos.buscarPalabra;
import static codigo.Metodos.esExpVal;
import static codigo.Metodos.evalExp;
import static codigo.Metodos.strCount;
import codigo.cuadruplos.ArbolExpresion;
import codigo.cuadruplos.Cuadruplo;
import codigo.generacionCodigoObjeto.GeneracionCodigoObjeto;
import codigo.semantico.AnalisisSemantico;
import com.placeholder.PlaceHolder;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import suggestions.PopupUtil;
import suggestions.Suggestions;
import sv.Servidor;

public class Compilador extends javax.swing.JFrame {

    Directorio dir;
    DefaultTableModel modelo;
    NumeroLinea numerolinea;
    static TablaIdent TI = null;
    static TablaSimbolos TS = null;
    static ArrayList<Token> tokens;
    public static ArrayList<Cuadruplo> cuadruplo;
    static ArrayList<ErrorLSSL> errores;
    static ArrayList<TextoColor> textoC;
    DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
    static ArrayList<Object[]> identificadores;
    private ArbolExpresion arbolCuad;
    private Servidor servidor;
    private final Border borderRadius = new LineBorder(Color.black, 2, true),
            borderRadiusF = new LineBorder(Color.black, 2, false);
    static float s = 0.35f;
    static int posCursor = 0;
    Timer t;
    private final String titulo = "COM&COM";
    private Suggestions suggestion;
    private ArrayList<Produccion> bloque_cuerpo;
    public static ThreadExec threadExec;

    public Compilador() {
        initComponents();
        inicializar();
    }

    @SuppressWarnings("empty-statement")
    private void inicializar() {
        setLocationRelativeTo(null);
        setTitle(titulo);
       // this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        inicializarBtnTxt();
        jtpCode.requestFocus();
        AbstractDocument doc = (AbstractDocument) jtpCode.getDocument();
        doc.setDocumentFilter(new Indentacion());
        modelo = (DefaultTableModel) tblTokens.getModel();
        dir = new Directorio();
        numerolinea = new NumeroLinea(jtpCode);
        jScrollPane1.setRowHeaderView(numerolinea);
        tokens = new ArrayList();
        errores = new ArrayList();
        textoC = new ArrayList();
        identificadores = new ArrayList();
        cuadruplo = new ArrayList();
        arbolCuad = new ArbolExpresion();
        suggestion = new Suggestions();
        bloque_cuerpo = new ArrayList();
        threadExec = new ThreadExec(bloque_cuerpo);
        servidor = new Servidor(jtpCode);
        servidor.start();
        threadExec.start();
        inicializarSugerencias();
        t = new Timer((int) (s * 1000), (ActionEvent e) -> {
            t.stop();
            AnalisisColor();
        });
        tblTokens.setFillsViewportHeight(true); //se usa para el color de la tabla
        headerRenderer.setBackground(Color.decode("#74da77"));
        for (int i = 0; i < tblTokens.getModel().getColumnCount(); i++) {
            tblTokens.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        Compilador ct = this;
        addWindowListener(new WindowAdapter() {//Cuando presiona la "X" de la esquina superior derecha
            @Override
            public void windowClosing(WindowEvent e) {
                int z = 0;
                if (threadExec.activo()) {
                    z = JOptionPane.showConfirmDialog(ct, "El brazo se encuentra en ejecución, ¿desea detenerlo?", "¿Detener ejecución del brazo?", 2);
                }
                if (z == 0) {
                    dir.Salir(ct);
                    servidor.cerrarConexion();
                    threadExec.detener();
                    dispose();
                    if (TI != null) {
                        TI.dispose();
                    }
                    if (TS != null) {
                        TS.dispose();
                    }
                    new Menu().setVisible(true);
                }
            }

            @Override
            public void windowIconified(WindowEvent e) {
                if (TI != null) {
                    TI.setState(JFrame.ICONIFIED);
                }
                if (TS != null) {
                    TS.setState(JFrame.ICONIFIED);
                }

            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                if (TI != null) {
                    TI.setState(JFrame.NORMAL);
                }
                if (TS != null) {
                    TS.setState(JFrame.NORMAL);
                }
            }
        });
        try {
            setIconImage(new ImageIcon(getClass().getResource("/iconos/logoC&C.png")).getImage());
        } catch (Exception e) {
            System.out.println("Error al colocar el logo en la ventana, se colocará el icono por defecto.");
        }
    }

    public void resetearComp() {
        modelo.setRowCount(0);
        jtaSalida.setText("");
        if (TI != null) {
            identificadores.clear();
            TI.llenarTablaIdent(errores.size());
        }
    }

    private void inicializarSugerencias() {
        ArrayList<String> words = new ArrayList<>();
        words.add("ident");
        words.add("cuerpo");

        words.add("num");
        words.add("servo");
        words.add("grado");
        words.add("sprox");
        words.add("bool");

        words.add("SERVO_1");
        words.add("SERVO_2");
        words.add("SERVO_3");
        words.add("SERVO_4");
        words.add("SPROX_NORTE");
        words.add("SPROX_SUR");
        words.add("SPROX_ESTE");
        words.add("SPROX_OESTE");
        words.add("CENTIM");
        words.add("METRO");
        words.add("PIE");
        words.add("VERDAD");
        words.add("FALSO");

        words.add("dist");
        words.add("girar");
        words.add("reset");
        words.add("agarrar");
        words.add("soltar");

        words.add("ciclo");
        words.add("bucle");

        suggestion.setWords(words);
    }

    private void inicializarBtnTxt() {
        PlaceHolder pb = new PlaceHolder(txtBuscar, " Palabra a buscar");
        PlaceHolder pr = new PlaceHolder(txtRemplazar, " Palabra a remplazar");
        mnAbrir.setToolTipText("Abrir un archivo creado de " + titulo);
        mnNuevo.setToolTipText("Crear un archivo nuevo de " + titulo);
        mnGuardar.setToolTipText("Guardar el archivo actual");
        mnGuardarComo.setToolTipText("Guardar el archivo actual con otro nombre, formato o dirección");
        mnPalabrasR.setToolTipText("Mostrar las palabras reservadas del lenguaje");
        mnIdent.setToolTipText("Mostrar los identificadores del lenguaje");
        mnSalir.setToolTipText("Salir del compilador");
        btnAbrir.setToolTipText("Abrir un archivo creado de " + titulo);
        btnNuevo.setToolTipText("Crear un archivo nuevo de " + titulo);
        btnGuardar.setToolTipText("Guardar el archivo actual");
        btnCompilar.setToolTipText("Iniciar el proceso de compilación del código");
        mnCopiar.setToolTipText("Copiar el texto seleccionado del código");
        mnCortar.setToolTipText("Cortar el texto seleccionado del código");
        mnPegar.setToolTipText("Pegar el texto previamente cortado o copiado del código");
        btnCopiar.setToolTipText("Copiar el texto seleccionado del código");
        btnCortar.setToolTipText("Cortar el texto seleccionado del código");
        btnPegar.setToolTipText("Pegar el texto previamente cortado o copiado del código");
        btnRemplazar.setToolTipText("Remplazar texto");
        btnLimpiar.setToolTipText("Limpiar la consola");
        btnCopiarCon.setToolTipText("Copiar el texto seleccionado de la consola");
        btnEjecutarBrazo.setToolTipText("Ejecutar el brazo");
        btnDetenerBrazo.setToolTipText("Detener el brazo");
        lblLogo.setToolTipText("Compilador " + titulo);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtpCode = new javax.swing.JTextPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblTokens = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jtaSalida = new javax.swing.JTextArea();
        btnRemplazar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtRemplazar = new javax.swing.JTextField();
        P_botones_comp = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnAbrir = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnCompilar = new javax.swing.JButton();
        btnEjecutarBrazo = new javax.swing.JButton();
        btnDetenerBrazo = new javax.swing.JButton();
        P_botones_editar = new javax.swing.JPanel();
        btnCopiar = new javax.swing.JButton();
        btnCortar = new javax.swing.JButton();
        btnPegar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnLimpiar = new javax.swing.JButton();
        btnCopiarCon = new javax.swing.JButton();
        lblLogo = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        mnArchivo = new javax.swing.JMenu();
        mnNuevo = new javax.swing.JMenuItem();
        mnAbrir = new javax.swing.JMenuItem();
        mnGuardar = new javax.swing.JMenuItem();
        mnGuardarComo = new javax.swing.JMenuItem();
        mnSalir = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        mnCopiar = new javax.swing.JMenuItem();
        mnPegar = new javax.swing.JMenuItem();
        mnCortar = new javax.swing.JMenuItem();
        mnTablaSim = new javax.swing.JMenu();
        mnPalabrasR = new javax.swing.JMenuItem();
        mnIdent = new javax.swing.JMenuItem();
        mnTablaSim1 = new javax.swing.JMenu();
        mnIp = new javax.swing.JMenuItem();
        mnServidor = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setBackground(new java.awt.Color(61, 61, 61));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtpCode.setBackground(new java.awt.Color(25, 25, 25));
        jtpCode.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(116, 218, 119), 2));
        jtpCode.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jtpCode.setOpaque(false);
        jtpCode.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jtpCodeCaretUpdate(evt);
            }
        });
        jtpCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtpCodeKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtpCodeKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtpCodeKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(jtpCode);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 990, 330));

        tblTokens.setBackground(new java.awt.Color(0, 0, 0));
        tblTokens.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(116, 218, 119), 2, true));
        tblTokens.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        tblTokens.setForeground(new java.awt.Color(255, 255, 255));
        tblTokens.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Lexema", "Componente léxico"
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
        jScrollPane3.setViewportView(tblTokens);
        if (tblTokens.getColumnModel().getColumnCount() > 0) {
            tblTokens.getColumnModel().getColumn(0).setResizable(false);
            tblTokens.getColumnModel().getColumn(1).setResizable(false);
            tblTokens.getColumnModel().getColumn(1).setPreferredWidth(35);
        }

        jPanel1.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 20, 340, 640));

        jtaSalida.setEditable(false);
        jtaSalida.setBackground(new java.awt.Color(0, 0, 0));
        jtaSalida.setColumns(20);
        jtaSalida.setFont(new java.awt.Font("Consolas", 0, 13)); // NOI18N
        jtaSalida.setForeground(new java.awt.Color(255, 255, 255));
        jtaSalida.setRows(5);
        jtaSalida.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(116, 218, 119), 2));
        jScrollPane4.setViewportView(jtaSalida);

        jPanel1.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 482, 920, 180));

        btnRemplazar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/remplazar32.png"))); // NOI18N
        btnRemplazar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        btnRemplazar.setContentAreaFilled(false);
        btnRemplazar.setFocusable(false);
        btnRemplazar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemplazar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemplazar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRemplazarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRemplazarMouseExited(evt);
            }
        });
        btnRemplazar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemplazarActionPerformed(evt);
            }
        });
        jPanel1.add(btnRemplazar, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 80, 50, 51));

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Remplazar por:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 100, -1, -1));

        txtRemplazar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtRemplazar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jPanel1.add(txtRemplazar, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 90, 180, 32));

        P_botones_comp.setBackground(new java.awt.Color(153, 153, 153));
        P_botones_comp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/guardar232.png"))); // NOI18N
        btnGuardar.setBorder(null);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setContentAreaFilled(false);
        btnGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGuardarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGuardarMouseExited(evt);
            }
        });
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnAbrir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/adjuntarC232.png"))); // NOI18N
        btnAbrir.setBorder(null);
        btnAbrir.setBorderPainted(false);
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

        btnNuevo.setBackground(new java.awt.Color(51, 51, 51));
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/nuevodoc32.png"))); // NOI18N
        btnNuevo.setBorder(null);
        btnNuevo.setBorderPainted(false);
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

        btnCompilar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/compilar432.png"))); // NOI18N
        btnCompilar.setBorderPainted(false);
        btnCompilar.setContentAreaFilled(false);
        btnCompilar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCompilarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCompilarMouseExited(evt);
            }
        });
        btnCompilar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCompilarActionPerformed(evt);
            }
        });

        btnEjecutarBrazo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/brazo32.png"))); // NOI18N
        btnEjecutarBrazo.setBorderPainted(false);
        btnEjecutarBrazo.setContentAreaFilled(false);
        btnEjecutarBrazo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEjecutarBrazoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEjecutarBrazoMouseExited(evt);
            }
        });
        btnEjecutarBrazo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEjecutarBrazoActionPerformed(evt);
            }
        });

        btnDetenerBrazo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/brazooff32.png"))); // NOI18N
        btnDetenerBrazo.setBorderPainted(false);
        btnDetenerBrazo.setContentAreaFilled(false);
        btnDetenerBrazo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDetenerBrazoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDetenerBrazoMouseExited(evt);
            }
        });
        btnDetenerBrazo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetenerBrazoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout P_botones_compLayout = new javax.swing.GroupLayout(P_botones_comp);
        P_botones_comp.setLayout(P_botones_compLayout);
        P_botones_compLayout.setHorizontalGroup(
            P_botones_compLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, P_botones_compLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAbrir, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCompilar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEjecutarBrazo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDetenerBrazo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        P_botones_compLayout.setVerticalGroup(
            P_botones_compLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(P_botones_compLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(P_botones_compLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                    .addComponent(btnAbrir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(P_botones_compLayout.createSequentialGroup()
                        .addGroup(P_botones_compLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnDetenerBrazo)
                            .addComponent(btnEjecutarBrazo)
                            .addComponent(btnCompilar))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jPanel1.add(P_botones_comp, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 310, 50));

        P_botones_editar.setBackground(new java.awt.Color(153, 153, 153));
        P_botones_editar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        btnCopiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/copiar32.png"))); // NOI18N
        btnCopiar.setContentAreaFilled(false);
        btnCopiar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCopiarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCopiarMouseExited(evt);
            }
        });
        btnCopiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCopiarActionPerformed(evt);
            }
        });

        btnCortar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/cortar32.png"))); // NOI18N
        btnCortar.setToolTipText("");
        btnCortar.setBorderPainted(false);
        btnCortar.setContentAreaFilled(false);
        btnCortar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCortarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCortarMouseExited(evt);
            }
        });
        btnCortar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCortarActionPerformed(evt);
            }
        });

        btnPegar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/pegar232.png"))); // NOI18N
        btnPegar.setBorderPainted(false);
        btnPegar.setContentAreaFilled(false);
        btnPegar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPegarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPegarMouseExited(evt);
            }
        });
        btnPegar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPegarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout P_botones_editarLayout = new javax.swing.GroupLayout(P_botones_editar);
        P_botones_editar.setLayout(P_botones_editarLayout);
        P_botones_editarLayout.setHorizontalGroup(
            P_botones_editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(P_botones_editarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCopiar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCortar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnPegar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        P_botones_editarLayout.setVerticalGroup(
            P_botones_editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnPegar, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
            .addComponent(btnCortar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnCopiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel1.add(P_botones_editar, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 20, 170, -1));

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/limpiar32.png"))); // NOI18N
        btnLimpiar.setBorderPainted(false);
        btnLimpiar.setContentAreaFilled(false);
        btnLimpiar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLimpiarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLimpiarMouseExited(evt);
            }
        });
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });
        jPanel2.add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 40, -1));

        btnCopiarCon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/copiarC232.png"))); // NOI18N
        btnCopiarCon.setBorderPainted(false);
        btnCopiarCon.setContentAreaFilled(false);
        btnCopiarCon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCopiarConMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCopiarConMouseExited(evt);
            }
        });
        btnCopiarCon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCopiarConActionPerformed(evt);
            }
        });
        jPanel2.add(btnCopiarCon, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 40, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 480, 60, 180));

        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/LogoC&C.png"))); // NOI18N
        jPanel1.add(lblLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 10, -1, -1));

        txtBuscar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtBuscar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
            }
        });
        jPanel1.add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 90, 180, 32));

        getContentPane().add(jPanel1);

        jMenuBar1.setBackground(new java.awt.Color(255, 255, 255));
        jMenuBar1.setBorderPainted(false);

        mnArchivo.setBackground(new java.awt.Color(102, 102, 102));
        mnArchivo.setText("Archivo");
        mnArchivo.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N

        mnNuevo.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        mnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/nuevodoc24.png"))); // NOI18N
        mnNuevo.setText("Nuevo");
        mnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnNuevoActionPerformed(evt);
            }
        });
        mnArchivo.add(mnNuevo);

        mnAbrir.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        mnAbrir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/adjuntarC224.png"))); // NOI18N
        mnAbrir.setText("Abrir");
        mnAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAbrirActionPerformed(evt);
            }
        });
        mnArchivo.add(mnAbrir);

        mnGuardar.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        mnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/guardar224.png"))); // NOI18N
        mnGuardar.setText("Guardar");
        mnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnGuardarActionPerformed(evt);
            }
        });
        mnArchivo.add(mnGuardar);

        mnGuardarComo.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        mnGuardarComo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/guardarComo24.png"))); // NOI18N
        mnGuardarComo.setText("Guardar como");
        mnGuardarComo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnGuardarComoActionPerformed(evt);
            }
        });
        mnArchivo.add(mnGuardarComo);

        mnSalir.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        mnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/salir24.png"))); // NOI18N
        mnSalir.setText("Salir");
        mnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnSalirActionPerformed(evt);
            }
        });
        mnArchivo.add(mnSalir);

        jMenuBar1.add(mnArchivo);

        jMenu1.setText("Editar");
        jMenu1.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N

        mnCopiar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        mnCopiar.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        mnCopiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/copiar24.png"))); // NOI18N
        mnCopiar.setText("Copiar");
        mnCopiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnCopiarActionPerformed(evt);
            }
        });
        jMenu1.add(mnCopiar);

        mnPegar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        mnPegar.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        mnPegar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/pegar224.png"))); // NOI18N
        mnPegar.setText("Pegar");
        mnPegar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPegarActionPerformed(evt);
            }
        });
        jMenu1.add(mnPegar);

        mnCortar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        mnCortar.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        mnCortar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/cortar24.png"))); // NOI18N
        mnCortar.setText("Cortar");
        mnCortar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnCortarActionPerformed(evt);
            }
        });
        jMenu1.add(mnCortar);

        jMenuBar1.add(jMenu1);

        mnTablaSim.setText("Tabla de símbolos");
        mnTablaSim.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N

        mnPalabrasR.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        mnPalabrasR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/tabla224.png"))); // NOI18N
        mnPalabrasR.setText("Palabras reservadas");
        mnPalabrasR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPalabrasRActionPerformed(evt);
            }
        });
        mnTablaSim.add(mnPalabrasR);

        mnIdent.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        mnIdent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/tabla24.png"))); // NOI18N
        mnIdent.setText("Identificadores");
        mnIdent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnIdentActionPerformed(evt);
            }
        });
        mnTablaSim.add(mnIdent);

        jMenuBar1.add(mnTablaSim);

        mnTablaSim1.setText("Acerca de");
        mnTablaSim1.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N

        mnIp.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        mnIp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/IP.png"))); // NOI18N
        mnIp.setText("Mi IP");
        mnIp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnIpActionPerformed(evt);
            }
        });
        mnTablaSim1.add(mnIp);

        mnServidor.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        mnServidor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/servidor.png"))); // NOI18N
        mnServidor.setText("Servidor");
        mnServidor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnServidorActionPerformed(evt);
            }
        });
        mnTablaSim1.add(mnServidor);

        jMenuBar1.add(mnTablaSim1);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnNuevoActionPerformed
        if (!threadExec.activo()) {
            dir.Nuevo(this);
        } else {
            JOptionPane.showMessageDialog(this, "No se puede crear un nuevo código ya que el brazo está en ejecución", "Acción interrumpida", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_mnNuevoActionPerformed

    private void mnAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAbrirActionPerformed
        if (!threadExec.activo()) {
            if (dir.Abrir(this)) {
                AnalisisColorEnter();
                jtpCode.setCaretPosition(0);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se puede abrir otro código ya que el brazo está en ejecución", "Acción interrumpida", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_mnAbrirActionPerformed

    private void mnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnGuardarActionPerformed
        if (!threadExec.activo()) {
            dir.Guardar(this);
        } else {
            JOptionPane.showMessageDialog(this, "No se puede guardar el código ya que el brazo está en ejecución", "Acción interrumpida", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_mnGuardarActionPerformed

    private void mnGuardarComoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnGuardarComoActionPerformed
        if (!threadExec.activo()) {
            dir.GuardarC(this);
        } else {
            JOptionPane.showMessageDialog(this, "No se puede guardar el código ya que el brazo está en ejecución", "Acción interrumpida", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_mnGuardarComoActionPerformed

    private void mnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnSalirActionPerformed
        int z = 0;
        if (threadExec.activo()) {
            z = JOptionPane.showConfirmDialog(this, "El brazo se encuentra en ejecución, ¿desea detenerlo?", "¿Detener ejecución del brazo?", 2);
        }
        if (z == 0) {
            dir.Salir(this);
            servidor.cerrarConexion();
            threadExec.detener();
            dispose();
            if (TI != null) {
                TI.dispose();
            }
            if (TS != null) {
                TS.dispose();
            }
            new Menu().setVisible(true);
        }
    }//GEN-LAST:event_mnSalirActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        if (!threadExec.activo()) {
            dir.Nuevo(this);
        } else {
            JOptionPane.showMessageDialog(this, "No se puede crear un nuevo código ya que el brazo está en ejecución", "Acción interrumpida", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbrirActionPerformed
        if (!threadExec.activo()) {
            if (dir.Abrir(this)) {
                AnalisisColorEnter();
                jtpCode.setCaretPosition(0);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se puede abrir otro código ya que el brazo está en ejecución", "Acción interrumpida", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnAbrirActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        if (!threadExec.activo()) {
            dir.Guardar(this);
        } else {
            JOptionPane.showMessageDialog(this, "No se puede guardar el código ya que el brazo está en ejecución", "Acción interrumpida", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void jtpCodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtpCodeKeyReleased
        int keyCode = evt.getKeyCode();
        if (keyCode == 10) {
            if (!getTitle().contains("*")) {
                setTitle(getTitle() + "*");
            }
            macro(jtpCode, "importar codigo", "EstPrincipal.pl");
            AnalisisColorEnter();
        } else if ((keyCode >= 65 && keyCode <= 90) || (keyCode >= 48 && keyCode <= 57)
                || (keyCode >= 97 && keyCode <= 122) || (keyCode != 27 && !(keyCode >= 37
                && keyCode <= 40) && !(keyCode >= 16
                && keyCode <= 18) && keyCode != 524
                && keyCode != 20)) {
            if (!getTitle().contains("*")) {
                setTitle(getTitle() + "*");
            }
            t.restart();
        }
        suggestion.showSuggestions(jtpCode, jtpCode.getCaretPosition(), coord.x, coord.y, evt, this);
    }//GEN-LAST:event_jtpCodeKeyReleased

    private void btnCompilarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCompilarActionPerformed
        if (!threadExec.activo()) {
            String title = getTitle();
            if (title.equals(titulo) || title.contains("*")) {
                if (dir.Guardar(this)) {
                    jtaSalida.setText(" Compilando " + getTitle() + " ...\n\n");
                    AnalisisLexico();
                    AnalisisSint();
                    llenarTabla();
                    if (TI != null) {
                        TI.llenarTablaIdent(errores.size());
                    }
                    imprimirConsola();
                }
            } else {
                jtaSalida.setText(" Compilando " + getTitle() + " ...\n\n");
                AnalisisLexico();
                AnalisisSint();
                llenarTabla();
                if (TI != null) {
                    TI.llenarTablaIdent(errores.size());
                }
                imprimirConsola();
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se puede compilar el código ya que el brazo está en ejecución", "No se ejecutó la compilación", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnCompilarActionPerformed

    private void mnPalabrasRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPalabrasRActionPerformed
        if (TS != null) {
            TS.dispose();
        }
        TS = new TablaSimbolos(this);
        TS.setVisible(true);
    }//GEN-LAST:event_mnPalabrasRActionPerformed

    private void mnIdentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnIdentActionPerformed
        if (TI != null) {
            TI.dispose();
        }
        TI = new TablaIdent(this);
        TI.llenarTablaIdent(errores.size());
        TI.setVisible(true);
    }//GEN-LAST:event_mnIdentActionPerformed

    private void mnCopiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnCopiarActionPerformed
        jtpCode.copy();
    }//GEN-LAST:event_mnCopiarActionPerformed

    private void mnPegarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPegarActionPerformed
        jtpCode.paste();
        AnalisisColorEnter();
    }//GEN-LAST:event_mnPegarActionPerformed

    private void mnCortarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnCortarActionPerformed
        jtpCode.cut();
        AnalisisColorEnter();
    }//GEN-LAST:event_mnCortarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        jtaSalida.setText("");
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnCopiarConActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopiarConActionPerformed
        jtaSalida.copy();
    }//GEN-LAST:event_btnCopiarConActionPerformed

    private void btnCopiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopiarActionPerformed
        jtpCode.copy();
    }//GEN-LAST:event_btnCopiarActionPerformed

    private void btnPegarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPegarActionPerformed
        jtpCode.paste();
        AnalisisColorEnter();
    }//GEN-LAST:event_btnPegarActionPerformed

    private void btnCortarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCortarActionPerformed
        jtpCode.cut();
        AnalisisColorEnter();
    }//GEN-LAST:event_btnCortarActionPerformed

    private void btnRemplazarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemplazarActionPerformed
        jtpCode.setText(jtpCode.getText().replaceAll(txtBuscar.getText(), txtRemplazar.getText()));
        AnalisisColorEnter();
        txtBuscar.setText("");
        txtRemplazar.setText("");
        if (posCursor != -1)
            jtpCode.setCaretPosition(posCursor);
    }//GEN-LAST:event_btnRemplazarActionPerformed

    private void btnNuevoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNuevoMouseEntered
        btnNuevo.setOpaque(true);
        btnNuevo.setContentAreaFilled(true);
        btnNuevo.setBackground(Color.decode("#74da77"));
    }//GEN-LAST:event_btnNuevoMouseEntered

    private void btnNuevoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNuevoMouseExited
        btnNuevo.setOpaque(false);
        btnNuevo.setContentAreaFilled(false);
    }//GEN-LAST:event_btnNuevoMouseExited

    private void btnAbrirMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAbrirMouseEntered
        btnAbrir.setOpaque(true);
        btnAbrir.setContentAreaFilled(true);
        btnAbrir.setBackground(Color.decode("#74da77"));
    }//GEN-LAST:event_btnAbrirMouseEntered

    private void btnAbrirMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAbrirMouseExited
        btnAbrir.setOpaque(false);
        btnAbrir.setContentAreaFilled(false);
    }//GEN-LAST:event_btnAbrirMouseExited

    private void btnGuardarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarMouseEntered
        btnGuardar.setOpaque(true);
        btnGuardar.setContentAreaFilled(true);
        btnGuardar.setBackground(Color.decode("#74da77"));
    }//GEN-LAST:event_btnGuardarMouseEntered

    private void btnGuardarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarMouseExited
        btnGuardar.setOpaque(false);
        btnGuardar.setContentAreaFilled(false);
    }//GEN-LAST:event_btnGuardarMouseExited

    private void btnCompilarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCompilarMouseEntered
        btnCompilar.setOpaque(true);
        btnCompilar.setContentAreaFilled(true);
        btnCompilar.setBackground(Color.decode("#74da77"));
    }//GEN-LAST:event_btnCompilarMouseEntered

    private void btnCompilarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCompilarMouseExited
        btnCompilar.setOpaque(false);
        btnCompilar.setContentAreaFilled(false);
    }//GEN-LAST:event_btnCompilarMouseExited

    private void btnCopiarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCopiarMouseEntered
        btnCopiar.setOpaque(true);
        btnCopiar.setContentAreaFilled(true);
        btnCopiar.setBackground(Color.decode("#74da77"));
    }//GEN-LAST:event_btnCopiarMouseEntered

    private void btnCopiarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCopiarMouseExited
        btnCopiar.setOpaque(false);
        btnCopiar.setContentAreaFilled(false);
    }//GEN-LAST:event_btnCopiarMouseExited

    private void btnPegarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPegarMouseEntered
        btnPegar.setOpaque(true);
        btnPegar.setContentAreaFilled(true);
        btnPegar.setBackground(Color.decode("#74da77"));
    }//GEN-LAST:event_btnPegarMouseEntered

    private void btnPegarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPegarMouseExited
        btnPegar.setOpaque(false);
        btnPegar.setContentAreaFilled(false);
    }//GEN-LAST:event_btnPegarMouseExited

    private void btnCortarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCortarMouseEntered
        btnCortar.setOpaque(true);
        btnCortar.setContentAreaFilled(true);
        btnCortar.setBackground(Color.decode("#74da77"));
    }//GEN-LAST:event_btnCortarMouseEntered

    private void btnCortarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCortarMouseExited
        btnCortar.setOpaque(false);
        btnCortar.setContentAreaFilled(false);
    }//GEN-LAST:event_btnCortarMouseExited

    private void btnLimpiarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimpiarMouseEntered
        btnLimpiar.setOpaque(true);
        btnLimpiar.setContentAreaFilled(true);
        btnLimpiar.setBackground(Color.decode("#74da77"));
    }//GEN-LAST:event_btnLimpiarMouseEntered

    private void btnLimpiarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimpiarMouseExited
        btnLimpiar.setOpaque(false);
        btnLimpiar.setContentAreaFilled(false);
    }//GEN-LAST:event_btnLimpiarMouseExited

    private void btnCopiarConMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCopiarConMouseEntered
        btnCopiarCon.setOpaque(true);
        btnCopiarCon.setContentAreaFilled(true);
        btnCopiarCon.setBackground(Color.decode("#74da77"));
    }//GEN-LAST:event_btnCopiarConMouseEntered

    private void btnCopiarConMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCopiarConMouseExited
        btnCopiarCon.setOpaque(false);
        btnCopiarCon.setContentAreaFilled(false);
    }//GEN-LAST:event_btnCopiarConMouseExited

    private void btnRemplazarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRemplazarMouseEntered
        btnRemplazar.setOpaque(true);
        btnRemplazar.setBorder(borderRadiusF);
        btnRemplazar.setBackground(Color.decode("#74da77"));
    }//GEN-LAST:event_btnRemplazarMouseEntered

    private void btnRemplazarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRemplazarMouseExited
        btnRemplazar.setOpaque(false);
        btnRemplazar.setBorder(borderRadius);
    }//GEN-LAST:event_btnRemplazarMouseExited

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
        if (!txtBuscar.getText().replace(" ", "").equals(""))
            buscarPalabra(jtpCode, txtBuscar.getText(), Color.GREEN);
    }//GEN-LAST:event_txtBuscarKeyReleased

    private Rectangle coord;

    private void jtpCodeCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jtpCodeCaretUpdate
        posCursor = jtpCode.getCaretPosition();
        servidor.setPosicion(posCursor);
        int dot = evt.getDot();
        try {
            coord = jtpCode.modelToView(dot);
        } catch (BadLocationException ex) {
            Logger.getLogger(Compilador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jtpCodeCaretUpdate

    private void jtpCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtpCodeKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER
                | evt.getKeyCode() == KeyEvent.VK_DOWN
                | evt.getKeyCode() == KeyEvent.VK_UP) {
            if (PopupUtil.isVisible()) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    PopupUtil.selectThat();
                } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
                    PopupUtil.moveDown();
                } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
                    PopupUtil.moveUp();
                }
                evt.consume();
            }
        }
    }//GEN-LAST:event_jtpCodeKeyPressed

    private void jtpCodeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtpCodeKeyTyped
//        if (evt.getKeyCode() == KeyEvent.VK_SHIFT) {
//            evt.consume();
//        }
    }//GEN-LAST:event_jtpCodeKeyTyped

    private void mnIpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnIpActionPerformed
        String localIp;
        try {
            localIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            localIp = "El equipo no está conectado a una red";
        }
        JOptionPane.showMessageDialog(this, localIp + ":" + servidor.PUERTO, "IP", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_mnIpActionPerformed

    private void mnServidorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnServidorActionPerformed
        if (servidor.serverSocket == null) {
            JOptionPane.showMessageDialog(this, "Servidor inactivo", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, (servidor.serverSocket.isClosed() ? "Servidor apagado" : "Servidor encendido"), "Atención", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_mnServidorActionPerformed

    private void btnEjecutarBrazoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEjecutarBrazoMouseEntered
        btnEjecutarBrazo.setOpaque(true);
        btnEjecutarBrazo.setContentAreaFilled(true);
        btnEjecutarBrazo.setBackground(Color.decode("#74da77"));
    }//GEN-LAST:event_btnEjecutarBrazoMouseEntered

    private void btnEjecutarBrazoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEjecutarBrazoMouseExited
        btnEjecutarBrazo.setOpaque(false);
        btnEjecutarBrazo.setContentAreaFilled(false);
    }//GEN-LAST:event_btnEjecutarBrazoMouseExited

    private void btnEjecutarBrazoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEjecutarBrazoActionPerformed
        if (errores.size() > 0) {
            JOptionPane.showMessageDialog(this, "No se puede ejecutar el brazo ya que hay errores de compilación", "Ejecución interrumpida", JOptionPane.WARNING_MESSAGE);
        } else if (bloque_cuerpo.size() > 0) {
            if (!threadExec.activo()) {
                threadExec.iniciar();
                JOptionPane.showMessageDialog(this, "Se ha iniciado la ejecución del brazo", "Ejecución iniciada", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "El brazo ya se encuentra en ejecución", "Acción interrumpida", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Favor de compilar el proyecto para ejecutar el brazo", "Ejecución interrumpida", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnEjecutarBrazoActionPerformed

    private void btnDetenerBrazoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDetenerBrazoMouseEntered
        btnDetenerBrazo.setOpaque(true);
        btnDetenerBrazo.setContentAreaFilled(true);
        btnDetenerBrazo.setBackground(Color.decode("#74da77"));
    }//GEN-LAST:event_btnDetenerBrazoMouseEntered

    private void btnDetenerBrazoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDetenerBrazoMouseExited
        btnDetenerBrazo.setOpaque(false);
        btnDetenerBrazo.setContentAreaFilled(false);
    }//GEN-LAST:event_btnDetenerBrazoMouseExited

    private void btnDetenerBrazoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetenerBrazoActionPerformed
        if (threadExec.activo()) {
            threadExec.detener();
            JOptionPane.showMessageDialog(this, "Se ha detenido la ejecución del brazo", "Ejecución detenida", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "El brazo no se encuentra en ejecución", "Acción innecesaria", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnDetenerBrazoActionPerformed

    public void AnalisisColor() {
        textoC.clear();
        LexerC lexer;
        try {
            File codigo = new File("color.encrypter");
            FileOutputStream output = new FileOutputStream(codigo);
            byte[] bytesText = jtpCode.getText().getBytes();
            output.write(bytesText);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(new FileInputStream(codigo), "UTF8"));
            lexer = new LexerC(entrada);
            while (true) {
                if (lexer.yylex() == null) {
                    break;
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("El archivo no pudo ser encontrado... " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Error al escribir en el archivo... " + ex.getMessage());
        }
        colorTextPane(textoC, jtpCode, new Color(40, 40, 40));
    }

    public void AnalisisColorEnter() {
        int cursor = jtpCode.getCaretPosition();
        jtpCode.setText(jtpCode.getText().replaceAll("\\r", ""));
        jtpCode.setCaretPosition(cursor);
        AnalisisColor();
    }

    private void AnalisisLexico() {
        tokens.clear();
        errores.clear();
        identificadores.clear();
        cuadruplo.clear();
        Lexer lexer;
        try {
            File codigo = new File("codigo.encrypter");
            FileOutputStream output = new FileOutputStream(codigo);
            byte[] bytesText = jtpCode.getText().getBytes();
            output.write(bytesText);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(new FileInputStream(codigo), "UTF8"));
            lexer = new Lexer(entrada);
            while (true) {
                if (lexer.yylex() == null) {
                    break;
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("El archivo no pudo ser encontrado... " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Error al escribir en el archivo... " + ex.getMessage());
        }

        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            String compLexico = token.getComp_lexico();
            switch (compLexico) {
                case "ident":
                    String ident = token.getLexema();
                    if (ident.matches("[0-9]+[A-Za-zÑñÁÉÍÓÚáéíóúÜü_]+([0-9]|[A-Za-zÑñÁÉÍÓÚáéíóúÜü_])*")) {
                        errores.add(new ErrorLSSL(9, " × Error léxico {}: Identificador inválido, consulte la documentación para ver la sintaxis de declaración [#, %] => []", token));
                    }
                    break;
                case "coment_inc":
                    errores.add(new ErrorLSSL(10, " × Error léxico {}: Falta '*/' para el cierre del comentario [#, %]", token));
                    tokens.remove(i);
                    i = i - 1;
                    break;
                default:
                    break;
            }
        }
    }

//Ejecutar análisis sintáctico
    private void AnalisisSint() {
        Gramatica gramatica = new Gramatica(tokens, errores);
        gramatica.desactivarMen();
        gramatica.desactivarVal();

        // Agrupaciones generales
        gramatica.agrupar("vest_servo", "(vest_servo1|vest_servo2|vest_servo3|vest_servo4)");
        gramatica.agrupar("vest_sprox", "(vest_sproxN|vest_sproxS|vest_sproxE|vest_sproxO)");
        gramatica.agrupar("vest_medida", "(vest_centim|vest_metro|vest_pie)");
        gramatica.agrupar("vest_bool", "(vest_verdad|vest_falso)");
        gramatica.agrupar("tipo_dato", "(tipoDato_grado|tipoDato_num|tipoDato_bool|tipoDato_sprox|tipoDato_servo)");
        gramatica.agrupar("opRel", "(opRel_may|opRel_men|opRel_mayIg|opRel_menIg|opRel_ig)");
        gramatica.agrupar("opArit", "(opArt_resta|opArt_suma|opArt_mult|opArt_div)");

        // Agrupaciones posibles de función distancia con sus parámetros
        ArrayList<Produccion> funDist = new ArrayList();
        gramatica.agrupar("fun_dist_com", "fun_dist parent_a (ident|vest_sprox) coma vest_medida parent_c", funDist);
        gramatica.agrupar("fun_dist_com", "fun_dist (ident|vest_sprox) coma vest_medida parent_c",
                1, " × Error sintáctico {}: falta el paréntesis que abre en la función distancia [#, %]");
        gramatica.agrupar("fun_dist_com", "fun_dist parent_a coma vest_medida parent_c",
                2, " × Error sintáctico {}: falta el primer parámetro en la función distancia \"[]\" [#, %]");
        gramatica.agrupar("fun_dist_com", "fun_dist parent_a (ident|vest_sprox) vest_medida parent_c",
                3, " × Error sintáctico {}: falta una coma en la función distancia [#, %]");
        gramatica.agrupar("fun_dist_com", "fun_dist parent_a (ident|vest_sprox) coma parent_c",
                4, " × Error sintáctico {}: falta el segundo parámetro en la función distancia \"[]\" [#, %]");
        gramatica.lineaColumnaFin();
        gramatica.agrupar("fun_dist_com", "fun_dist parent_a (ident|vest_sprox) coma vest_medida",
                5, " × Error sintáctico {}: falta el paréntesis que cierra en la función distancia \"[]\" [#, %]");
        gramatica.lineaColumaIni();

        // Agrupaciones de comparaciones entre números
        ArrayList<Produccion> compEntNum = new ArrayList();
        gramatica.agrupar("booleano", "(numero|ident|fun_dist_com) opRel (numero|ident|fun_dist_com)", compEntNum);

        ArrayList<Produccion> funGirar = new ArrayList();
        // Agurpaciones de funcion girar
        gramatica.agrupar("funcion_inc", "fun_girar parent_a (grado|ident) coma (vest_servo|ident) parent_c", funGirar);
        gramatica.agrupar("funcion_inc", "fun_girar (grado|ident) coma (vest_servo|ident) parent_c",
                6, " × Error sintáctico {}: falta el paréntesis que abre en la función girar [#, %]");
        gramatica.agrupar("funcion_inc", "fun_girar parent_a coma (vest_servo|ident) parent_c",
                7, " × Error sintáctico {}: falta el primer parámetro en la función girar \"[]\" [#, %]");
        gramatica.agrupar("funcion_inc", "fun_girar parent_a (grado|ident) (vest_servo|ident) parent_c",
                8, " × Error sintáctico {}: falta la coma entre el primer y segundo parámetro de la función girar [#, %]");
        gramatica.agrupar("funcion_inc", "fun_girar parent_a (grado|ident) coma  parent_c",
                9, " × Error sintáctico {}: falta el segundo parámetro en la función girar \"[]\" [#, %]");
        gramatica.agrupar("funcion_inc", "fun_girar parent_a (grado|ident) coma (vest_servo|ident)",
                10, " × Error sintáctico {}: falta el paréntesis que cierra en la función girar \"[]\" [#, %]");

        // Agrupaciones de función agarrar, soltar y reset
        gramatica.agrupar("funcion_inc", "(fun_agarrar|fun_soltar|fun_reset) parent_a parent_c");
        gramatica.agrupar("funcion_inc", "(fun_agarrar|fun_soltar|fun_reset) parent_c",
                11, " × Error sintáctico {}: falta el paréntesis que abre en la función \"[]\" [#, %]");
        gramatica.agrupar("funcion_inc", "(fun_agarrar|fun_soltar|fun_reset) parent_a",
                12, " × Error sintáctico {}: falta el paréntesis que cierra en la función \"[]\" [#, %]");

        ArrayList<Produccion> funcion = new ArrayList();
        // Punto y coma al final de las funciones
        gramatica.agrupar("funcion", "funcion_inc punto_coma", funcion);
        gramatica.lineaColumnaFin();
        gramatica.agrupar("funcion", "funcion_inc", 13,
                " × Error sintáctico {}: falta el punto y coma al final de la función [#, %]");
        gramatica.lineaColumaIni();

        // Agrupar ciclo
        ArrayList<Produccion> ciclo = new ArrayList();
        gramatica.agrupar("ciclo", "estCont_ciclo parent_a (numero|ident) coma (numero|ident) coma (numero|ident) parent_c", ciclo);
        gramatica.agrupar("ciclo", "estCont_ciclo (numero|ident) coma (numero|ident) coma (numero|ident) parent_c",
                14, " × Error sintáctico {}: falta el paréntesis que abre en el ciclo [#, %]");
        gramatica.agrupar("ciclo", "estCont_ciclo parent_a coma (numero|ident) coma (numero|ident) parent_c",
                15, " × Error sintáctico {}: falta el primer parámetro en el ciclo \"[]\" [#, %]");
        gramatica.agrupar("ciclo", "estCont_ciclo parent_a (numero|ident) (numero|ident) coma (numero|ident) parent_c",
                16, " × Error sintáctico {}: falta una coma después del primer parámetro en el ciclo [#, %]");
        gramatica.agrupar("ciclo", "estCont_ciclo parent_a (numero|ident) coma coma (numero|ident) parent_c",
                17, " × Error sintáctico {}: falta el segundo parámetro en el ciclo \"[]\" [#, %]");
        gramatica.agrupar("ciclo", "estCont_ciclo parent_a (numero|ident) coma (numero|ident) (numero|ident) parent_c",
                18, " × Error sintáctico {}: falta una coma después del segundo parámetro en el ciclo [#, %]");
        gramatica.agrupar("ciclo", "estCont_ciclo parent_a (numero|ident) coma (numero|ident) coma parent_c",
                19, " × Error sintáctico {}: falta el tercer parámetro en el ciclo \"[]\" [#, %]");
        gramatica.lineaColumnaFin();
        gramatica.agrupar("ciclo", "estCont_ciclo parent_a (numero|ident) coma (numero|ident) coma (numero|ident)",
                20, " × Error sintáctico {}: falta el paréntesis que cierra al final del ciclo \"[]\" [#, %]");
        gramatica.lineaColumaIni();
        gramatica.eliminar("estCont_ciclo", 21,
                " × Error sintáctico {}: sentencia de ciclo incompleta, proceda a eliminar o cooregirla [#, %]");

        // Agrupar bucle
        ArrayList<Produccion> bucle = new ArrayList();
        gramatica.agrupar("bucle", "estCont_bucle parent_a (ident|booleano|vest_bool) parent_c", bucle);
        gramatica.agrupar("bucle", "estCont_bucle (ident|booleano|vest_bool) parent_c",
                22, " × Error sintáctico {}: falta el paréntesis que abre en el bucle [#, %]");
        gramatica.agrupar("bucle", "estCont_bucle parent_a parent_c",
                23, " × Error sintáctico {}: falta el parámetro en el bucle \"[]\" [#, %]");
        gramatica.agrupar("bucle", "estCont_bucle parent_a (ident|booleano|vest_bool)",
                24, " × Error sintáctico {}: falta el paréntesis que cierra en el bucle \"[]\"[#, %]");
        gramatica.eliminar("estCont_bucle", 25,
                " × Error sintáctico {}: sentencia de bucle incompleta, proceda a eliminar o cooregirla [#, %]");

        // Agrupar expresiones aritméticas
        ArrayList<Produccion> expAritm = new ArrayList();
        gramatica.agrupar("exp_arit", "(numero|opArit|parent_a|parent_c)+", expAritm);

        // Agrupaciones de asignaciones
        ArrayList<Produccion> asignacion = new ArrayList();
        gramatica.agrupar("asignacion_inc", "tipo_dato ident opAsig (vest_servo|vest_sprox|vest_bool|numero|grado|booleano|exp_arit)", asignacion);
        gramatica.agrupar("asignacion_inc", "ident opAsig (vest_servo|vest_sprox|vest_bool|numero|grado|booleano|exp_arit)",
                26, " × Error sintáctico {}: falta especificar el tipo de dato en la asignación \"[]\" [#, %]");
        gramatica.agrupar("asignacion_inc", "tipo_dato opAsig (vest_servo|vest_sprox|vest_bool|numero|grado|booleano|exp_arit)",
                27, " × Error sintáctico {}: falta el identificador en la asignación \"[]\" [#, %]");
        gramatica.agrupar("asignacion_inc", "tipo_dato ident (vest_servo|vest_sprox|vest_bool|numero|grado|booleano|exp_arit)",
                28, " × Error sintáctico {}: falta el operador \"=\" en la asignación [#, %]");
        gramatica.lineaColumnaFin();
        gramatica.agrupar("asignacion_inc", "tipo_dato ident opAsig",
                29, " × Error sintáctico {}: falta especificar el valor en la asignación \"[]\" [#, %]");

        //Punto y coma al final de las asignaciones
        gramatica.agrupar("asignacion", "asignacion_inc punto_coma");
        gramatica.agrupar("asignacion", "asignacion_inc", 30,
                " × Error sintáctico {}: falta el punto y coma al final de la asignación [#, %]");
        gramatica.lineaColumaIni();

        // Agrupar de bloque de identificadores y llave que abre
        gramatica.agrupar("bloque_ident_inc", "bloque_ident llave_a");
        gramatica.agrupar("bloque_ident_inc", "bloque_ident", 31,
                " × Error sintáctico {}: falta la llave que abre al final del bloque \"[]\" [#, %]");

        // Agrupar de bloque cuerpo y llave que abre
        gramatica.agrupar("bloque_cuerpo_inc", "bloque_cuerpo llave_a");
        gramatica.agrupar("bloque_cuerpo_inc", "bloque_cuerpo", 32,
                " × Error sintáctico {}: falta la llave que abre al final del bloque \"[]\" [#, %]");

        // Agrupar ciclo y llave que abre
        gramatica.agrupar("estCont_ciclo_inc", "ciclo llave_a");
        gramatica.agrupar("estCont_ciclo_inc", "ciclo", 33,
                " × Error sintáctico {}: falta la llave que abre al inicio de la sentencia de control \"[]\" [#, %]");

        // Agrupar bucle y llave que abre
        gramatica.agrupar("estCont_bucle_inc", "bucle llave_a");
        gramatica.agrupar("estCont_bucle_inc", "bucle", 34,
                " × Error sintáctico {}: falta la llave que abre al inicio de la sentencia de control \"[]\" [#, %]");

        // Eliminar tokens o producciones sobrantes
        gramatica.eliminar("vest_servo", 34,
                " × Error sintáctico {}: la variable estática [] no está contenida en una función o declaración [#, %]");
        gramatica.eliminar("vest_sprox", 35,
                " × Error sintáctico {}: la variable estática [] no está contenida en una función o declaración [#, %]");
        gramatica.eliminar("vest_bool", 36,
                " × Error sintáctico {}: la variable estática [] no está contenida en una sentencia de control o declaración [#, %]");
        gramatica.eliminar("vest_medida", 37,
                " × Error sintáctico {}: la variable estática [] no está contenida en alguna función de medida [#, %]");
        gramatica.eliminar("tipo_dato", 38,
                " × Error sintáctico {}: el tipo de dato \"[]\" no está contenido en alguna declaración [#, %]");
        gramatica.eliminar("ident", 39,
                " × Error sintáctico {}: el identificador \"[]\" no está contenido en alguna declaración [#, %]");
        gramatica.eliminar("opAsig", 40,
                " × Error sintáctico {}: el operador [] no está contenido en alguna declaración [#, %]");
        gramatica.eliminar("opRel", 41,
                " × Error sintáctico {}: el operador relacional [] no está contenido en alguna comparación [#, %]");
        gramatica.agrupar("fun_elim", "(fun_girar|fun_dist|fun_agarrar|fun_soltar|fun_reset)");
        gramatica.eliminar("fun_elim", 42,
                " × Error sintáctico {}: función \"[]\" fuera de lugar, proceda a eliminarla o corregirla [#, %]");
        gramatica.eliminar("grado", 43,
                " × Error sintáctico {}: el valor grado \"[]\" no está contenido en alguna declaración o función [#, %]");
        gramatica.eliminar("booleano", 44,
                " × Error sintáctico {}:4el valor booleano \"[]\" no está contenido en alguna declaración [#, %]");
        gramatica.eliminar("coma", 45,
                " × Error sintáctico {}: la coma no está contenida en alguna función o sentencia de control [#, %]");
        gramatica.eliminar("punto_coma", 46,
                " × Error sintáctico {}: el punto y coma no está al final de alguna sentencia [#, %]");
        gramatica.eliminar("fun_dist_com", 47,
                " × Error sintáctico {}: la función de distancia debe estar contenida en una comparación [#, %]");
        gramatica.eliminar("llave_a", 48,
                " × Error sintáctico {}: la llave que abre no se encuentra al inicio de alguna sentencia de control o bloque [#, %]");
        gramatica.eliminar("SYMBOL", 49);
        gramatica.eliminar("exp_arit", 53, " × Error sintáctico {}: No se esperaba encontrar \"[]\" [#, %]"); // >> Ref. a producción "expAritm" >>
        erroresExp(expAritm);

        // Agrupar asignaciones
        gramatica.agrupar("asignaciones", "(asignacion)+");

        // Agrupación del bloque de identificadores
        gramatica.agrupar("bloque_ident", "bloque_ident_inc asignaciones llave_c");
        gramatica.agrupar("bloque_ident", "bloque_ident_inc llave_c");
        gramatica.lineaColumnaFin();
        gramatica.agrupar("bloque_ident", "bloque_ident_inc asignaciones", 54,
                " × Error sintáctico {}: falta la llave que cierra al final del bloque de identificadores [#, %]", 1);
        gramatica.agrupar("bloque_ident", "bloque_ident_inc", 54,
                " × Error sintáctico {}: falta la llave que cierra al final del bloque de identificadores [#, %]");
        gramatica.lineaColumaIni();
        gramatica.eliminar("asignaciones", 55,
                " × Error sintáctico {}: las asignaciones no están contenidas en el bloque de identificadores [#, %]");

        //Agrupar funciones
        gramatica.agrupar("funciones", "(funcion)+");

        //Agrupación del bloque ciclo
        gramatica.agrupar("estCont_ciclo", "estCont_ciclo_inc funciones llave_c");
        gramatica.agrupar("estCont_ciclo", "estCont_ciclo_inc llave_c");
        gramatica.lineaColumnaFin();
        gramatica.agrupar("estCont_ciclo", "estCont_ciclo_inc funciones", 56,
                " × Error sintáctico {}: falta la llave que cierra al final de la sentencia de control ciclo [#, %]", 1);
        gramatica.agrupar("estCont_ciclo", "estCont_ciclo_inc", 56,
                " × Error sintáctico {}: falta la llave que cierra al final de la sentencia de control ciclo [#, %]");
        gramatica.lineaColumaIni();

        //Agrupación del bloque bucle
        gramatica.agrupar("estCont_bucle", "estCont_bucle_inc funciones llave_c");
        gramatica.agrupar("estCont_bucle", "estCont_bucle_inc llave_c");
        gramatica.lineaColumnaFin();
        gramatica.agrupar("estCont_bucle", "estCont_bucle_inc funciones", 57,
                " × Error sintáctico {}: falta la llave que cierra al final de la sentencia de control bucle [#, %]", 1);
        gramatica.agrupar("estCont_bucle", "estCont_bucle_inc", 57,
                " × Error sintáctico {}: falta la llave que cierra al final de la sentencia de control bucle [#, %]");
        gramatica.lineaColumaIni();

        // Agrupación de bucles, funciones y ciclos
        gramatica.agrupar("funbucic", "(funciones|estCont_bucle|estCont_ciclo)+");

        bloque_cuerpo.clear();
        // Agrupación del bloque de cuerpo
        gramatica.agrupar("bloque_cuerpo", "bloque_cuerpo_inc funbucic llave_c", bloque_cuerpo);
        gramatica.agrupar("bloque_cuerpo", "bloque_cuerpo_inc llave_c");
        gramatica.lineaColumnaFin();
        gramatica.agrupar("bloque_cuerpo", "bloque_cuerpo_inc funbucic", 58,
                " × Error sintáctico {}: falta la llave que cierra al final del bloque cuerpo [#, %]", 1);
        gramatica.agrupar("bloque_cuerpo", "bloque_cuerpo_inc", 58,
                " × Error sintáctico {}: falta la llave que cierra al final del bloque cuerpo [#, %]");
        gramatica.lineaColumaIni();
        gramatica.eliminar("funbucic", 59,
                " × Error sintáctico {}: las funciones y/o sentencias de control no están contenidas en el bloque cuerpo [#, %]");

        // Agrupación de bloque cuerpo e identificadores
        gramatica.agrupar("completo", "(bloque_ident bloque_cuerpo|bloque_cuerpo bloque_ident)");
        gramatica.agrupar("completo", "bloque_ident", 60,
                " × Error sintáctico {}: falta el bloque cuerpo antes o después del bloque de identificadores [#, %]");
        gramatica.agrupar("completo", "bloque_cuerpo", 61,
                " × Error sintáctico {}: falta el bloque de identificadores antes o después del bloque cuerpo [#, %]");

        // Eliminar tokens o producciones sobrantes
        gramatica.eliminar("llave_c", 62,
                " × Error sintáctico {}: la llave que cierra no se encuentra al final de alguna sentencia de control o bloque [#, %]");
        gramatica.eliminar("bloque_ident");
        gramatica.eliminar("bloque_cuerpo");

        // gramatica.mostrar();
        if (errores.isEmpty()) {
            llenarTablaIdent(asignacion);
            analisisSemant(funDist, compEntNum, funGirar, ciclo, bucle, asignacion);
            if(errores.isEmpty()) {
                codigoIntermedio(asignacion, funcion);
                generarCodigoObjeto();
            }
        }

    }
    
    private void generarCodigoObjeto() {
        GeneracionCodigoObjeto gco = new GeneracionCodigoObjeto(cuadruplo);
        String dir = System.getProperty("user.dir");
        Metodos.exportFileEncoding(dir.substring(0, dir.length() - 19), "assembly.asm", gco.getCodigoGenerado(), StandardCharsets.UTF_8);
        // System.out.println(gco.getCodigoGenerado());
    }

    private void analisisSemant(ArrayList<Produccion> funDist, ArrayList<Produccion> compEntNum,
            ArrayList<Produccion> funGirar, ArrayList<Produccion> ciclo, ArrayList<Produccion> bucle,
            ArrayList<Produccion> asignacion) {

        AnalisisSemantico.setArrayErrores(errores, identificadores);

        ArrayList<Produccion> todo = new ArrayList<>();
        todo.addAll(funDist);
        todo.addAll(compEntNum);
        todo.addAll(funGirar);
        todo.addAll(ciclo);
        todo.addAll(bucle);
        todo.addAll(asignacion);

        todo.forEach((p) -> {
            AnalisisSemantico.analizar(p.rangoLexema(0, -1), p);
//            System.out.println(p.rangoLexema(0, -1));
        });

//        for (Produccion fDist : funDist) {
//            System.out.println(fDist.rangoLexema(0, -1));
//        }
//        System.out.println("***************************************************************");
//
//        System.out.println("***************************************************************");
//        for (int i = 0; i < identificadores.size(); i++) {
//            Object[] ident = identificadores.get(i);
//            Produccion pIdent = asignacion.get(i);
//            String tipoDato = ident[0].toString();
//            String nombreIdent = ident[1].toString();
//            String valor = ident[2].toString();
//            System.out.println(tipoDato + " " + nombreIdent + "=" + valor);
//            System.out.println(pIdent.rangoLexema(0, -1));
//            System.out.println("-----------");
//            errores.add(new ErrorLSSL(100, "× Error semántico {}: tipo de dato incompatible en [] [#, %]", pIdent, true));
//        }
    }

    /*
    private void ejecutar() {
        brazoEnEjec = true;
        Produccion cuerpo = bloque_cuerpo.get(0);
        for (int i = 0; i < cuerpo.getTamañoTokens(); i++) {
            if (!brazoEnEjec) {
                break;
            }
            String cad = cuerpo.rangoLexema(i);
            if (cad.equals("girar")) {
                System.out.println(cuerpo.rangoLexema(i, i + 5));
                i += 6;
            } else if (cad.equals("agarrar") | cad.equals("soltar") | cad.equals("reset")) {
                System.out.println(cuerpo.rangoLexema(i, i + 2));
                i += 3;
            } else if (cad.equals("ciclo")) {
                double p1 = Double.parseDouble(cuerpo.rangoLexema(i + 2));
                double p2 = Double.parseDouble(cuerpo.rangoLexema(i + 4));
                double p3 = Double.parseDouble(cuerpo.rangoLexema(i + 6));
                i += 8;
                int c = -1;
                for (int j = i; j < cuerpo.getTamañoTokens(); j++) {
                    if (cuerpo.rangoLexema(j).equals("}")) {
                        c = j - 1;
                        break;
                    }
                }
                String[] sent = cuerpo.rangoLexema(i, c).split(";");
                for (double k = p1; k <= p2; k += p3) {
                    if (!brazoEnEjec) {
                        break;
                    }
                    System.out.println("****");
                    for (int l = 0; l < sent.length; l++) {
                        System.out.println(sent[l]);
                    }
                    System.out.println("****");
                }
                i = c + 1;
            } else if (cad.equals("bucle")) {
                int c = -1;
                for (int j = i; j < cuerpo.getTamañoTokens(); j++) {
                    if (cuerpo.rangoLexema(j).equals("{")) {
                        c = j - 2;
                        break;
                    }
                }
                String cond = cuerpo.rangoLexema(i + 2, c);
                System.out.println("~~" + cond + "~~");
                i = c + 2;
                c = -1;
                for (int j = i; j < cuerpo.getTamañoTokens(); j++) {
                    if (cuerpo.rangoLexema(j).equals("}")) {
                        c = j - 1;
                        break;
                    }
                }
                String[] sent = cuerpo.rangoLexema(i, c).split(";");
                int m = 0;
                while (m < 6) {
                    if (!brazoEnEjec) {
                        break;
                    }
                    System.out.println("---");
                    for (int l = 0; l < sent.length; l++) {
                        System.out.println(sent[l]);
                    }
                    System.out.println("---");
                    m += 1;
                }
                i = c + 1;
            }
        }
        bloque_cuerpo.clear();
        brazoEnEjec = false;
    }
     */
    private void erroresExp(ArrayList<Produccion> expAritm) {
        for (Produccion exp : expAritm) {
            String cadExp = exp.rangoLexema(0, -1);
            if (cadExp.equals("(") || cadExp.equals(")")) {
                errores.add(new ErrorLSSL(50, "× Error sintáctico {}: el paréntesis \"[]\" no está contenido en alguna función o expresión aritmética [#, %]", exp, true));
            } else if (cadExp.equals("()")) {
                errores.add(new ErrorLSSL(51, "× Error sintáctico {}: los paréntesis \"()\" no están contenidos en alguna función [#, %]", exp, true));
            } else if (!esExpVal(cadExp)) {
                errores.add(new ErrorLSSL(52, "× Error sintáctico {}: expresión aritmética incorrecta, favor de corregirla -> \"[]\" [#, %]", exp, true));
            }
        }
    }

    private void codigoIntermedio(ArrayList<Produccion> asignacion, ArrayList<Produccion> funcion) {
        arbolCuad.tAcero();
        ArrayList<Produccion> todo = new ArrayList<>();
        todo.addAll(asignacion);
        todo.addAll(funcion);
        Collections.sort(todo, (Produccion p1, Produccion p2)
                -> new Integer(p1.getLinea()).compareTo(p2.getLinea())
        );

//        long inicio = System.currentTimeMillis();

        for (Produccion p : todo) {
            String prod = p.rangoLexema(0, -1);
            if (prod.contains("=")) {
                arbolCuad.crearArbol(prod);
            } else {
                String[] f = prod.trim().split("\\(|\\)|,");
                if (prod.contains(",")) {
                    cuadruplo.add(new Cuadruplo(f[0], f[1], f[2], ""));
                } else {
                    cuadruplo.add(new Cuadruplo(f[0], "", "", ""));
                }
            }
        }

//        long fin = System.currentTimeMillis();
//        long tiempo = fin - inicio;
//
//        System.out.println(tiempo + " milisegundos");
//        cuadruplo.forEach(System.out::println);
    }

    private void llenarTablaIdent(ArrayList<Produccion> asignacion) {
        for (Produccion id : asignacion) {
            String cadId = id.rangoLexema(0, -1);
            int countEq = strCount('=', cadId);
            Object[] o = new Object[3];
            String[] splitId = cadId.split(" |=");
            o[0] = splitId[0];
            o[1] = splitId[1];
            if (countEq == 1) {
                if (esExpVal(splitId[2])) {
                    o[2] = evalExp(splitId[2]);
                } else {
                    o[2] = splitId[2];
                }
            } else if (countEq == 2) {
                o[2] = splitId[2] + "=" + splitId[3];
            } else {
                o[2] = splitId[2] + "==" + splitId[4];
            }

            identificadores.add(o);
        }
    }

    private void llenarTabla() {
        modelo.setRowCount(0);

        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            Object[] obj = new Object[2];
            obj[0] = token.getLexema();
            obj[1] = token.getComp_lexico();
            modelo.addRow(obj);
        }
    }

    private void imprimirConsola() {
        int cantErrores = errores.size();
        if (cantErrores > 0) {
            quicksortErrorLinea(errores, 0, cantErrores - 1);
            ordenarErrorCol();
            String cadErrores = "";
            for (int i = 0; i < cantErrores; i++) {
                String error = String.valueOf(errores.get(i));
                cadErrores += error + "\n";
            } //for

            jtaSalida.setText(jtaSalida.getText() + cadErrores + "\n");
            jtaSalida.setText(jtaSalida.getText() + " La compilación terminó con errores y/o advertencias...\n\n");
        }
        jtaSalida.setText(jtaSalida.getText() + " ===================================================== Compilación completa"
                + " =====================================================");
        jtaSalida.setForeground(new Color(211, 211, 255));
        jtaSalida.setCaretPosition(0);
    }

    //Ordenar los errores por número de línea
    private void quicksortErrorLinea(ArrayList<ErrorLSSL> E, int izq, int der) {
        ErrorLSSL pivote = E.get(izq); // tomamos primer elemento como pivote
        int i = izq; // i realiza la búsqueda de izquierda a derecha
        int j = der; // j realiza la búsqueda de derecha a izquierda
        while (i < j) { // mientras no se crucen las búsquedas
            while (E.get(i).getLinea() <= pivote.getLinea() && i < j) {
                i++; // busca elemento mayor que pivote
            }
            while (E.get(j).getLinea() > pivote.getLinea()) {
                j--; // busca elemento menor que pivote
            }
            if (i < j) { // si no se han cruzado                      
                ErrorLSSL e = E.get(i); // los intercambia
                E.set(i, E.get(j));
                E.set(j, e);
            }
        }
        E.set(izq, E.get(j)); // se coloca el pivote en su lugar, de forma que tendremos
        E.set(j, pivote); // los menores a su izquierda y los mayores a su derecha
        if (izq < j - 1) {
            quicksortErrorLinea(E, izq, j - 1); // ordenamos subarray izquierdo
        }
        if (j + 1 < der) {
            quicksortErrorLinea(E, j + 1, der); // ordenamos subarray derecho
        }
    }

    //Ordenar los errores por número de columna
    private void quicksortErrorCol(ArrayList<ErrorLSSL> E, int izq, int der) {
        ErrorLSSL pivote = E.get(izq); // tomamos primer elemento como pivote
        int i = izq; // i realiza la búsqueda de izquierda a derecha
        int j = der; // j realiza la búsqueda de derecha a izquierda
        while (i < j) { // mientras no se crucen las búsquedas
            while (E.get(i).getColumna() <= pivote.getColumna() && i < j) {
                i++; // busca elemento mayor que pivote
            }
            while (E.get(j).getColumna() > pivote.getColumna()) {
                j--; // busca elemento menor que pivote
            }
            if (i < j) {// si no se han cruzado                      
                ErrorLSSL e = E.get(i); // los intercambia
                E.set(i, E.get(j));
                E.set(j, e);
            }
        }
        E.set(izq, E.get(j)); // se coloca el pivote en su lugar, de forma que tendremos
        E.set(j, pivote); // los menores a su izquierda y los mayores a su derecha
        if (izq < j - 1) {
            quicksortErrorCol(E, izq, j - 1); // ordenamos subarray izquierdo
        }
        if (j + 1 < der) {
            quicksortErrorCol(E, j + 1, der); // ordenamos subarray derecho
        }
    }

    //Ordenar los errores por columna (solamente aquellos que pertenezcan a la misma línea)
    private void ordenarErrorCol() {
        int tamaño = errores.size();

        for (int i = 0; i < tamaño; i++) {
            ErrorLSSL errorI = errores.get(i);
            int izq = i, der = -1;
            for (int j = i + 1; j < tamaño; j++) {
                ErrorLSSL errorF = errores.get(j);
                if (errorI.getLinea() == errorF.getLinea()) {
                    der = j;
                } else if (der != -1) {
                    quicksortErrorCol(errores, izq, der);
                    i = der;
                    der = -1;
                    break;
                } else {
                    break;
                }
            }//for del j
            if (der != -1) {
                quicksortErrorCol(errores, izq, der);
            }
        }//for del i
    }

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
            java.util.logging.Logger.getLogger(Compilador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Compilador().setVisible(true);
        });
    }

    /**
     * @param area
     *
     * @param
     * texto***********************************************************************
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel P_botones_comp;
    private javax.swing.JPanel P_botones_editar;
    private javax.swing.JButton btnAbrir;
    private javax.swing.JButton btnCompilar;
    private javax.swing.JButton btnCopiar;
    private javax.swing.JButton btnCopiarCon;
    private javax.swing.JButton btnCortar;
    private javax.swing.JButton btnDetenerBrazo;
    private javax.swing.JButton btnEjecutarBrazo;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnPegar;
    private javax.swing.JButton btnRemplazar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jtaSalida;
    public javax.swing.JTextPane jtpCode;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JMenuItem mnAbrir;
    private javax.swing.JMenu mnArchivo;
    private javax.swing.JMenuItem mnCopiar;
    private javax.swing.JMenuItem mnCortar;
    private javax.swing.JMenuItem mnGuardar;
    private javax.swing.JMenuItem mnGuardarComo;
    private javax.swing.JMenuItem mnIdent;
    private javax.swing.JMenuItem mnIp;
    private javax.swing.JMenuItem mnNuevo;
    private javax.swing.JMenuItem mnPalabrasR;
    private javax.swing.JMenuItem mnPegar;
    private javax.swing.JMenuItem mnSalir;
    private javax.swing.JMenuItem mnServidor;
    private javax.swing.JMenu mnTablaSim;
    private javax.swing.JMenu mnTablaSim1;
    private javax.swing.JTable tblTokens;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtRemplazar;
    // End of variables declaration//GEN-END:variables

}
