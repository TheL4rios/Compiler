package sv;

import codigo.Compilador;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;
import org.openide.util.Exceptions;

/**
 *
 * @author Larios
 */
public class Servidor extends Thread {

    public final int PUERTO = 8080;
    private BufferedReader entrada;
    private Socket socket;
    public ServerSocket serverSocket;
    private JTextComponent txt;
     private int posicion = 0;
    private Generador g;

    public Servidor(JTextComponent txt) {
        // this.txt = txt;
        this.g = new Generador();
    }

    public Servidor() {
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PUERTO);

            while (true) {
                socket = serverSocket.accept();
                entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String mensajeRecibido = entrada.readLine();
                accionar(g.getCodigo(mensajeRecibido));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cerrarConexion() {
        if (serverSocket == null) {
            return;
        }
        try {
            serverSocket.close();
        } catch (IOException ex) {
        }
    }

    public void accionar(String text) {
        if (text == null) {
            JOptionPane.showMessageDialog(null, "El comando ingresado no existe", "Atención", JOptionPane.ERROR_MESSAGE);
            return;
        }

        switch (text) {
            case "MoverDerecha": {
                Compilador.threadExec.mover(1);
            }
            break;
            case "MoverIzquierda": {
                Compilador.threadExec.mover(2);
            }
            break;
            case "MoverArriba": {
                Compilador.threadExec.mover(3);
            }
            break;
            case "MoverAbajo": {
                Compilador.threadExec.mover(4);
            }
            break;
            case "Agarrar": {
                Compilador.threadExec.mover(5);
            }
            break;
            case "Soltar": {
                Compilador.threadExec.mover(6);
            }
            break;
            case "Reset": {
                Compilador.threadExec.mover(7);
            }
            break;
        }

//        txt.requestFocus();
//        txt.setText(txt.getText().substring(0, posicion) + 
//                    text + 
//                    txt.getText().substring(posicion, txt.getText().length()));
        //txt.setCaretPosition(posicion + text.length());
    }

//    public static void main(String[] args) {
//        new Servidor().start();
//    }
}
