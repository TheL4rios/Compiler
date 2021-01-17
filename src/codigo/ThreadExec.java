package codigo;

import java.util.ArrayList;
import jssc.SerialPortException;
import org.openide.util.Exceptions;

/**
 *
 * @author jesus
 */
public class ThreadExec extends Thread {

    private ArrayList<Produccion> bloque_cuerpo;
    private ArrayList<Object[]> identificadores;
    private boolean activo, voz;
    private int mov;

    public ThreadExec(ArrayList<Produccion> bloque_cuerpo) {
        this.bloque_cuerpo = bloque_cuerpo;
        identificadores = Compilador.identificadores;
        activo = false;
        voz = false;
        mov = 0;
        J_A.conectarAlArduino();
        try {
            sleep(2000);
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public void run() {
        while (true) {
            try {
                
                sleep(1000);
            } catch (InterruptedException ex) {
                System.out.println("Error en la sentencia sleep: " + ex.getMessage());
            }
            if (activo && voz) {
                switch (mov) {
                    case 1:
                        System.out.println("Mover a la derecha...");
                        /* MANDAR A LLAMAR FUNCIÓN DE ARDUINO CON LOS PARÁMETROS */
                        derecha();
                        break;
                    case 2:
                        System.out.println("Mover a la izquierda...");
                        /* MANDAR A LLAMAR FUNCIÓN DE ARDUINO CON LOS PARÁMETROS */
                        izquierda();
                        break;
                    case 3:
                        System.out.println("Mover hacia arriba...");
                        /* MANDAR A LLAMAR FUNCIÓN DE ARDUINO CON LOS PARÁMETROS */
                        arriba();
                        break;
                    case 4:
                        System.out.println("Mover hacia abajo...");
                        /* MANDAR A LLAMAR FUNCIÓN DE ARDUINO CON LOS PARÁMETROS */
                        abajo();
                        break;
                    case 5:
                        System.out.println("Agarrar objeto...");
                        /* MANDAR A LLAMAR FUNCIÓN DE ARDUINO CON LOS PARÁMETROS */
                        J_A.agarrar();
                        break;
                    case 6:
                        System.out.println("Soltar objeto...");
                        /* MANDAR A LLAMAR FUNCIÓN DE ARDUINO CON LOS PARÁMETROS */
                        J_A.soltar();
                        break;
                    case 7:
                        System.out.println("Devolviendo brazo a su posición original...");
                        /* MANDAR A LLAMAR FUNCIÓN DE ARDUINO CON LOS PARÁMETROS */
                        reset();
                        break;
                }
                try {
                    sleep(3000);
                } catch (InterruptedException ex) {
                    System.out.println("Error en la sentencia sleep: " + ex.getMessage());
                }
                voz = false;
                activo = false;
                mov = 0;
            }
            if (activo && !voz) {
                Produccion cuerpo = bloque_cuerpo.get(0);
                for (int i = 0; i < cuerpo.getTamTokens(); i++) {
                    if (!activo) {
                        break;
                    }
                    String cad = cuerpo.rangoLexema(i);
                    if (cad.equals("girar")) {
                        System.out.println(cuerpo.rangoLexema(i, i + 5)); // AQUÍ
                        String p1 = getValor(cuerpo.rangoLexema(i + 2)),
                                p2 = getValor(cuerpo.rangoLexema(i + 4));
                        System.out.println("Parámetro 1: " + p1 + "\nParámetro 2:" + p2); //IMPRIMIENDO PARÁMETROS
                        /* MANDAR A LLAMAR FUNCIÓN DE ARDUINO CON LOS PARÁMETROS */
                        girar(p1, p2);
                        try {
                            sleep(3000);
                        } catch (InterruptedException ex) {
                            System.out.println("Error en la sentencia sleep: " + ex.getMessage());
                        }
                        i += 6;
                    } else if (cad.equals("agarrar") | cad.equals("soltar") | cad.equals("reset")) {
                        System.out.println(cuerpo.rangoLexema(i, i + 2)); // AQUÍ
                        if (cad.equals("agarrar")) {
                            System.out.println("Agarrar objeto...");
                            /* MANDAR A LLAMAR FUNCIÓN DE ARDUINO */
                            J_A.agarrar();
                        } else if (cad.equals("soltar")) {
                            System.out.println("Soltar objeto...");
                            /* MANDAR A LLAMAR FUNCIÓN DE ARDUINO */
                            J_A.soltar();
                        } else { //reset
                            System.out.println("Devolviendo brazo a su posición original...");
                            /* MANDAR A LLAMAR FUNCIÓN DE ARDUINO */
                            reset();
                        }
                        try {
                            sleep(3000);
                        } catch (InterruptedException ex) {
                            System.out.println("Error en la sentencia sleep: " + ex.getMessage());
                        }
                        i += 3;
                    } else if (cad.equals("ciclo")) {
                        double p1 = Double.parseDouble(getValor(cuerpo.rangoLexema(i + 2)));
                        double p2 = Double.parseDouble(getValor(cuerpo.rangoLexema(i + 4)));
                        double p3 = Double.parseDouble(getValor(cuerpo.rangoLexema(i + 6)));
                        i += 9;
                        int c = -1;
                        for (int j = i; j < cuerpo.getTamTokens(); j++) {
                            if (cuerpo.rangoLexema(j).equals("}")) {
                                c = j - 1;
                                break;
                            }
                        }
                        String[] sent = cuerpo.rangoLexema(i, c).split(";");
                        for (double k = p1; k <= p2; k += p3) {
                            if (!activo) {
                                break;
                            }
                            System.out.println("****");
                            for (int l = 0; l < sent.length; l++) {
                                if (!activo) {
                                    break;
                                }
                                System.out.println(sent[l]); // AQUÍ
                                if (sent[l].startsWith("girar")) {
                                    String[] sentT = sent[l].substring(6, sent[l].length() - 1).split(",");
                                    String pst1 = getValor(sentT[0]),
                                            pst2 = getValor(sentT[1]);
                                    System.out.println("Parámetro 1: " + pst1 + "\nParámetro 2:" + pst2); //IMPRIMIENDO PARÁMETROS
                                    /* MANDAR A LLAMAR FUNCIÓN DE ARDUINO CON LOS PARÁMETROS */
                                    girar(pst1, pst2);
                                } else {
                                    if (sent[l].startsWith("agarrar")) {
                                        System.out.println("Agarrar objeto...");
                                        /* MANDAR A LLAMAR FUNCIÓN DE ARDUINO */
                                        J_A.agarrar();
                                    } else if (sent[l].startsWith("soltar")) {
                                        System.out.println("Soltar objeto...");
                                        /* MANDAR A LLAMAR FUNCIÓN DE ARDUINO */
                                        J_A.soltar();
                                    } else {
                                        System.out.println("Devolviendo brazo a su posición original...");
                                        /* MANDAR A LLAMAR FUNCIÓN DE ARDUINO */
                                        reset();
                                    }
                                }
                                try {
                                    sleep(3000);
                                } catch (InterruptedException ex) {
                                    System.out.println("Error en la sentencia sleep: " + ex.getMessage());
                                }
                            }
                            System.out.println("****");
                        }
                        i = c + 1;
                    } else if (cad.equals("bucle")) {
                        int c = -1;
                        for (int j = i; j < cuerpo.getTamTokens(); j++) {
                            if (cuerpo.rangoLexema(j).equals("{")) {
                                c = j - 2;
                                break;
                            }
                        }
                        String cond = getValor(cuerpo.rangoLexema(i + 2, c));
                        System.out.println("~~" + cond + "~~"); // AQUÍ ESTÁ LA CONDICIÓN
                        i = c + 3;
                        c = -1;
                        for (int j = i; j < cuerpo.getTamTokens(); j++) {
                            if (cuerpo.rangoLexema(j).equals("}")) {
                                c = j - 1;
                                break;
                            }
                        }
                        String[] sent = cuerpo.rangoLexema(i, c).split(";");
                        /* INSTRUCCIONES PARA LA CONDICIÓN */
                        int m = 0;
                        EvaluarExpresion ee = new EvaluarExpresion();
                        while (ee.getRes(cond, this)) { // (m < 1) {
                            if (!activo) {
                                break;
                            }
                            System.out.println("---");
                            for (int l = 0; l < sent.length; l++) {
                                if (!activo) {
                                    break;
                                }
                                System.out.println(sent[l]); // AQUÍ
                                if (sent[l].startsWith("girar")) {
                                    String[] sentT = sent[l].substring(6, sent[l].length() - 1).split(",");
                                    String pst1 = getValor(sentT[0]),
                                            pst2 = getValor(sentT[1]);
                                    System.out.println("Parámetro 1: " + pst1 + "\nParámetro 2:" + pst2); //IMPRIMIENDO PARÁMETROS
                                    /* MANDAR A LLAMAR FUNCIÓN DE ARDUINO CON LOS PARÁMETROS */
                                    girar(pst1, pst2);
                                } else {
                                    if (sent[l].startsWith("agarrar")) {
                                        System.out.println("Agarrar objeto...");
                                        /* MANDAR A LLAMAR FUNCIÓN DE ARDUINO */
                                        J_A.agarrar();
                                    } else if (sent[l].startsWith("soltar")) {
                                        System.out.println("Soltar objeto...");
                                        /* MANDAR A LLAMAR FUNCIÓN DE ARDUINO */
                                        J_A.soltar();
                                    } else {
                                        System.out.println("Devolviendo brazo a su posición original...");
                                        /* MANDAR A LLAMAR FUNCIÓN DE ARDUINO */
                                        reset();
                                    }
                                }
                                try {
                                    sleep(3000);
                                } catch (InterruptedException ex) {
                                    System.out.println("Error en la sentencia sleep: " + ex.getMessage());
                                }
                            }
                            System.out.println("---");
                            m += 1;
                        }
                        i = c + 1;
                    }
                }
                bloque_cuerpo.clear();
                activo = false;
            }
        }
    }

    public void iniciar() {
        activo = true;
    }

    public void detener() {
        activo = false;
    }

    public boolean activo() {
        return activo;
    }

    public void mover(int mov) {
        this.mov = mov;
        voz = true;
        activo = true;
    }

    public String getValor(String valor) {
        if (valor.matches("[A-Za-zÑñ_ÁÉÍÓÚáéíóúÜü]([A-Za-zÑñ_ÁÉÍÓÚáéíóúÜü]|[0-9])*")) {
            for (Object[] id : identificadores) {
                if (valor.equals(id[1].toString())) {
                    return id[2].toString();
                }
            }
        }
        return valor;
    }

    public double funDist(String p1, String p2) {
        String p1m = "", p2m = "";
        switch (p1) {
            case "SPROX_NORTE":
                p1m = "Norte";
                break;
            case "SPROX_SUR":
                p1m = "Sur";
                break;
            case "SPROX_ESTE":
                p1m = "Este";
                break;
            case "SPROX_OESTE":
                p1m = "Oeste";
                break;
        }
        switch (p2) {
            case "CENTIM":
                p2m = "cm";
                break;
            case "METRO":
                p2m = "m";
                break;
            case "PIE":
                p2m = "ft";
                break;
        }
        System.out.println("Distamcia: "+J_A.dist(p1m, p2m));
        return J_A.dist(p1m, p2m);
    }

    public void girar(String p1, String p2) {
        int p1m = Integer.parseInt(p1.substring(0, p1.length() - 1));
        String p2m = "";
        switch (p2) {
            case "SERVO_1":
                p2m = "Servo1";
                break;
            case "SERVO_2":
                p2m = "Servo2";
                break;
            case "SERVO_3":
                p2m = "Servo3";
                break;
            case "SERVO_4":
                p2m = "Servo4";
                break;
        }
        try {
            J_A.girar(p1m, p2m);
            try {
                sleep(3000);
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            }
        } catch (SerialPortException ex) {
            System.out.println("Error en el puerto serial: " + ex.getMessage());
        }
    }

    public void derecha() {
        try {
            J_A.derecha();
        } catch (SerialPortException ex) {
            System.err.println("Error en el puerto serial: " + ex.getMessage());
        }
    }

    public void izquierda() {
        try {
            J_A.izquierda();
        } catch (SerialPortException ex) {
            System.err.println("Error en el puerto serial: " + ex.getMessage());
        }
    }

    public void arriba() {
        try {
            J_A.arriba();
        } catch (SerialPortException ex) {
            System.err.println("Error en el puerto serial: " + ex.getMessage());
        }
    }

    public void abajo() {
        try {
            J_A.abajo();
        } catch (SerialPortException ex) {
            System.err.println("Error en el puerto serial: " + ex.getMessage());
        }
    }

    public void reset() {
        try {
            J_A.reset();
            sleep(3000);
        } catch (SerialPortException ex) {
            System.err.println("Error en el puerto serial: " + ex.getMessage());
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
