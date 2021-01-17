package codigo;

import comunicacionserial.ArduinoExcepcion;
import comunicacionserial.ComunicacionSerial_Arduino;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 *
 * @author kevin
 */
public class J_A {

    static ComunicacionSerial_Arduino conn = new ComunicacionSerial_Arduino();
    static String cad = "";

    static SerialPortEventListener listen = new SerialPortEventListener() {
        @Override

        public void serialEvent(SerialPortEvent spe) {
            try {
                if (conn.isMessageAvailable()) {
                    cad = conn.printMessage();
                }
            } catch (SerialPortException ex) {
                System.out.println("Error en el puerto serial...");
            } catch (ArduinoExcepcion ex) {
                System.out.println("Error en el arduino..."+ex);
            }
        }
    };
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    static String nombre = "";

    public static void conectarAlArduino() {
        try {
            conn.arduinoRXTX("COM3", 9600, listen);
        } catch (ArduinoExcepcion ex) {
            System.out.println("Error en el arduino...");
        }
    }

    public static void enviar(String e) throws SerialPortException {
        try {
            conn.sendData(e);
        } catch (ArduinoExcepcion ex) {
            System.out.println("Error en el arduino...");
        }
    }

public static double dist(String sensor, String medida) {
        String cadenas[] = cad.split("\\|");
        double m;
        for (String a : cadenas) {
            String arg[] = a.split(":");
            
            if (arg[0].equalsIgnoreCase(sensor)) {
                m = Double.parseDouble(arg[1]);
               
                switch (medida) {
                    case "cm":
                         
                        return m;
                    case "m":
                        return (m / 100);
                    case "ft":
                        return (m / 30.48);
                    default:
                        break;
                }
            }
        }
        return -1;
    }
    public static void agarrar() {

        try {
            conn.sendData("0");
        } catch (ArduinoExcepcion ex) {
            System.out.println("Error en el arduino...");
        } catch (SerialPortException ex) {
            System.out.println("Error en el puerto serial...");
        }

    }

    public static void soltar() {
        try {
            conn.sendData("1");
        } catch (ArduinoExcepcion ex) {
            System.out.println("Error en el arduino...");
        } catch (SerialPortException ex) {
            System.out.println("Error en el puerto serial...");
        }

    }

    public static void girar(int n, String s) throws SerialPortException {
//************GIRAR BRAZO COMPLETO***************************************
        if (n == 90 && s.equalsIgnoreCase("Servo1")) {
            enviar("2");
        }
        if (n == 0 && s.equalsIgnoreCase("Servo1")) {
            enviar("3");
        }
//***************GIRAR ARTICULACIÓN*************************************
        if (n == 0 && s.equalsIgnoreCase("Servo2")) {
            enviar("4");
        }
        if (n == 90 && s.equalsIgnoreCase("Servo2")) {
            enviar("5");
        }
        if (n == 180 && s.equalsIgnoreCase("Servo2")) {
            enviar("6");
        }
//***********GIRAR BASE**************************************************
        if (n == 90 && s.equalsIgnoreCase("Servo3")) {
            enviar("7");
        }
        if (n == 0 && s.equalsIgnoreCase("Servo3")) {
            enviar("8");
        }
    }

    public static void reset() throws SerialPortException {
        //**PONER EN 90 GRADOS TODO
        enviar("2");
        enviar("5");
        enviar("7");
    }

    public static void derecha() throws SerialPortException {
        enviar("8");
    }

    public static void izquierda() throws SerialPortException {
        enviar("7");
    }

    public static void arriba() throws SerialPortException {
        enviar("3");
    }

    public static void abajo() throws SerialPortException {
        enviar("2");
    }

    public static void main(String[] args) {
        /*try {
            conectarAlArduino();
            while (true) {
                Thread.sleep(1000);
                System.out.print(dist("Norte", "m") + "\n");
            }
            /*
            for(int i=0;i<3;i++){
            Thread.sleep(3000);
            agarrar();
        
            Thread.sleep(3000);
        
            soltar();
            Thread.sleep(3000);
            }
            
            girar(0, "Servo2");
            Thread.sleep(3000);*

        } catch (InterruptedException ex) {
            Logger.getLogger(J_A.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SerialPortException ex) {
            Logger.getLogger(J_A.class.getName()).log(Level.SEVERE, null, ex); 
        }catch (SerialPortException ex) {
            Logger.getLogger(J_A.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
}
