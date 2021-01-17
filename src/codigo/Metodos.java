package codigo;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 *
 * @author jesus, Larios
 */
public class Metodos {

    /*Colores estáticos usados en el system.out.println*/
    public static String ANSI_BLACK = "\u001B[30m",
            ANSI_RED = "\u001B[31m",
            ANSI_RED_BLACK = "\u001B[31;2m",
            ANSI_GREEN = "\u001B[32m",
            ANSI_GREEN_BLACK = "\u001B[32;2m",
            ANSI_YELLOW = "\u001B[33m",
            ANSI_YELLOW_BLACK = "\u001B[33;2m",
            ANSI_BLUE = "\u001B[34m",
            ANSI_BLUE_BLACK = "\u001B[34;2m",
            ANSI_PURPLE = "\u001B[35m",
            ANSI_PURPLE_BLACK = "\u001B[35;2m",
            ANSI_RESET = "\u001B[0m";

    /*Llenado de tablas con datos y columnas especificosas
    ~Created by Yisus FBI~
     */
    public static void llenarTabla(DefaultTableModel modelo, String[][] arrays, String[] nameColumns) {
        modelo.setRowCount(0);
        modelo.setColumnCount(0);

        for (String nameCol : nameColumns) {
            modelo.addColumn(nameCol);
        }

        for (String[] ar : arrays) {
            Object[] o = new Object[nameColumns.length];
            int i = 0;
            for (String ele : ar) {
                o[i] = ele;
                i += 1;
            }
            modelo.addRow(o);
        }
    }

    /*Sustitución de macros   ~Created by Larios~*/
    public static void macro(JTextPane jtpCode, String macro, String plantilla) {
        if (jtpCode.getText().contains(macro)) {
            File file = new File(System.getProperty("user.dir") + "/src/plantillas/" + plantilla);
            String cadena, temporal = "";
            try {
                FileReader archivo = new FileReader(file);
                try (BufferedReader buffer = new BufferedReader(archivo)) {
                    while ((cadena = buffer.readLine()) != null) {
                        temporal += cadena;
                    }
                }
            } catch (IOException ex) {
                System.out.println("No se puede localizar el archivo " + plantilla + ", se cargará el archivo por defecto.");
                temporal = "Lexico:/n"
                        + "/tDeclaracion{/n"
                        + "/t/n/t/n/t}/n/n"
                        + "/tTerminales{/n"
                        + "/t/n/t/n/t}/n/n"
                        + "Sintactico:/n"
                        + "/tNoTerminales{/n"
                        + "/t/t<>,<>,<>;/n"
                        + "/t/n/t}/n/n"
                        + "/tIniciaCon <>/n/n"
                        + "/tProducciones{/n"
                        + "/t/t<> ::= ;/n"
                        + "/t/n/t}/n/n"
                        + "/tErrores{/n"
                        + "/t/n/t/n/t}/n"
                        + "Semantico:/n"
                        + "/tsi <>("
                        + "/n/t[],"
                        + "/n/t[],"
                        + "/n/t[]"
                        + "/n/t)/n"
                        + "/tsino -> ¡linea{} columna[]!";
            }

            temporal = temporal.replaceAll("/t", "\t").replaceAll("/n", "\n");
            jtpCode.setText(jtpCode.getText().replaceAll("importar codigo", temporal));
        }
    }

    /* Sustitución temporal de token de expresión regular para identificadores entre llaves (en nuestra sintaxis las expresiones regulares
    llevan referencias a identificadores (ejemplo: {D}, {ident }, { ident }, etc), el problema es que al validar si es una expresión regular 
    válida con match, lo marca como error, por ello se los quitamos temporalmente) 
    ~Created by Yisus FBI~
     */
    public static String identExpReg(String expReg) {
        String ER = "\\{\\s*[A-Za-z_ÑñÁÉÍÓÚáéíóúÜü][A-Za-z_ÑñÁÉÍÓÚáéíóúÜü0-9]*\\s*\\}";

        if (expReg.matches(ER)) {
            return expReg.substring(1, expReg.length() - 1);
        }

        ArrayList<String> splits = new ArrayList(), ident = new ArrayList(), idents = new ArrayList();
        String palOr = "", expRegOr = expReg;
        String[] split = expReg.split(ER);

        for (String s : split) {
            if (!s.isEmpty()) {
                splits.add(s);
            }
        }

        for (int i = 0; i < splits.size(); i++) {
            String s = splits.get(i);
            int index = expReg.indexOf(s);
            ident.add(expReg.substring(0, index));
            expReg = expReg.substring(index + s.length(), expReg.length());
            if (i == splits.size() - 1 && !expReg.isEmpty()) {
                ident.add(expReg);
            }
        }

        for (String i : ident) {
            if (!i.isEmpty()) {
                idents.add(i);
            }
        }

        if (idents.isEmpty() && splits.size() == 1) {
            return expRegOr;
        }

        int m;
        if (idents.size() > splits.size()) {
            m = idents.size();
        } else {
            m = splits.size();
        }

        boolean splitB = false;
        for (int i = 0; i < m; i++)
        try {
            if (expRegOr.startsWith(idents.get(0))) {
                if (splitB) {
                    palOr += idents.get(i);
                    splitB = false;
                } else {
                    palOr += idents.get(i).replace("{", "").replace("}", "").replace(" ", "");
                }
                if (splits.get(i).matches(".*\\\\+")) {
                    splitB = true;
                }
                palOr += splits.get(i);
            } else {
                palOr += splits.get(i);
                if (splits.get(i).matches(".*\\\\+")) {
                    palOr += idents.get(i);
                } else {
                    palOr += idents.get(i).replace("{", "").replace("}", "").replace(" ", "");
                }
            }
        } catch (java.lang.IndexOutOfBoundsException e) {/*Ignore*/
        }

        return palOr;
    }

    /*
  ***Colorea los caracteres que estén dentro de un rango de un determinado color***
  Recibe como parámetro el arreglo de las posiciones y el color, el TextPane y el color por defecto
  de aquellos caracteres que no estén dentro del rango.
  ~Created by Yisus FBI~
     */
    public static void colorTextPane(ArrayList<TextoColor> textoC, JTextPane jtpCode, Color colorOrig) {
        StyledDocument style = jtpCode.getStyledDocument();
        StyleContext cont = StyleContext.getDefaultStyleContext();
        AttributeSet colorExp = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, colorOrig);

        style.setCharacterAttributes(0, style.getLength(), colorExp, true);
        for (int i = 0; i < textoC.size(); i++) {
            colorExp = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, textoC.get(i).getColor());
            style.setCharacterAttributes(textoC.get(i).getInicio(), textoC.get(i).getTamaño(), colorExp, true);
        }
    }

    /* Método que recibe como parámetro el JTextArea y el texto. Buscará el texto en el TextArea
  y lo marcará de un detemrinado color.
  ~Created by Larios~  
     */
    public static void buscarPalabra(JTextPane area, String texto, Color color) {
        if (texto.length() >= 1) {
            DefaultHighlighter.DefaultHighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(color);
            Highlighter h = area.getHighlighter();
            h.removeAllHighlights();
            String text = area.getText();
            String caracteres = texto;
            Pattern p = Pattern.compile("(?i)" + caracteres);
            Matcher m = p.matcher(text);
            while (m.find()) {

                try {
                    h.addHighlight(m.start(), m.end(), highlightPainter);
                } catch (BadLocationException ex) {
                    System.out.println("BadLocationException en el método buscarPalabra... " + ex.getMessage());
                }
            }
        }
    }

    /* Método utilizado para eliminar de la tabla identificadores repetidos. En caso de que uno se repita,
    solamente tomará en cuenta a el de la última posición. 
    ~Created by Yisus FBI~
     */
    public static void eliminarIdentRep(ArrayList<Object[]> idents) {
        for (int i = 0; i < idents.size(); i++) {
            String id = String.valueOf((idents.get(i)[1]));
            for (int j = i + 1; j < idents.size(); j++) {
                String ida = String.valueOf((idents.get(j)[1]));
                if (id.equals(ida)) {
                    idents.remove(i);
                    i = -1;
                    break;
                }
            }
        }
    }

    /*Método que retorna una cadena multiplicada n veces
    ~Created by Yisus FBI~
     */
    public static String multStr(String cad, int n) {
        String cadT = "";
        if (n <= 0) {
            return "";
        }
        for (int i = 1; i <= n; i++) {
            cadT += cad;
        }

        return cadT;
    }

    /*Método que centra una cadena entre caracteres especificados a la izquierda y derecha
    ~Created by Yisus FBI~
     */
    public static String centrarPal(String palabra, String cadContI, String cadContF, int cant) {
        int tamaño = palabra.length();
        if (cant <= tamaño) {
            return palabra;
        }
        cant = cant - tamaño;
        if (cant % 2 == 0) {
            cant = cant / 2;
            String contI = multStr(cadContI, cant), contF = multStr(cadContF, cant);
            return contI + palabra + contF;
        } else {
            cant = (cant - 1) / 2;
            String contI = multStr(cadContI, cant + 1), contF = multStr(cadContF, cant);
            return contI + palabra + contF;
        }
    }

    /* Método que realiza el remplazo de una cadena por otra cadena, con la condición de que no esté
       precedida de el caracter de escape '\'
       ~Created by Yisus FBI~
     */
    public static String formatoCadena(String cadena, String formato, String remplazo) {
        String Adesc = "";
        while (true) {
            int indice = cadena.indexOf("\\" + formato);
            if (indice != -1) {
                Adesc += cadena.substring(0, indice).replace(formato, remplazo) + formato;
                cadena = cadena.substring(indice + formato.length() + 1, cadena.length());
            } else {
                Adesc += cadena.substring(0, cadena.length()).replace(formato, remplazo);
                break;
            }
        }

        return Adesc;
    }

    /* Método que retorna verdadero si una cadena es un número */
    public static boolean esNumero(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /* Método que retorna verdadero si una cadena es una expresión aritmética válida */
    public static boolean esExpVal(String exp) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        try {
            engine.eval(exp);
            return true;
        } catch (ScriptException ex) {
            return false;
        }
    }

    /* Método que evalúa y retorna el valor de una expresión aritmética */
    public static String evalExp(String exp) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        try {
            double num = Double.parseDouble(engine.eval(exp).toString());
            if (num - ((int) num) != 0) {
                return String.valueOf(num);
            } else {
                return String.valueOf((int) num);
            }
        } catch (ScriptException ex) {
            return null;
        }
    }

    /*Método que retorna la cantidad de veces que se repite un caracter en una cadena*/
    public static int strCount(char car, String cad) {
        int c = 0;
        for (int i = 0; i < cad.length(); i++) {
            char car2 = cad.charAt(i);
            if (car == car2) {
                c++;
            }
        }
        return c;
    }
    
    public static void exportFileEncoding(String directory, String fileName, String data, Charset format) {
        String directoryFile = directory + "\\" + fileName;
        Writer out;
        try {
            out = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(directoryFile), format));
            out.write(data);
            out.close();
        } catch (IOException ex) {
            System.out.println("Error al codificar el archivo: " + ex.getMessage());
        }
    }
}


/*Esta clase contiene diversos métodos que son de utilidad para el compilador. Si le es de utilidad, copie
el método que necesite y no se olvide de poner los créditos que están en la parte de abajo. Te lo agradeceremos
mucho. Te ha hablado Dross y te deseo buenas noches :v*/

 /*Created by Yisus & Larios */
