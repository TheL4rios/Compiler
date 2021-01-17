package codigo.semantico;

import codigo.ErrorLSSL;
import codigo.Produccion;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author Larios
 */
public class AnalisisSemantico {
    public static ArrayList<ErrorLSSL> errores;
    private static ArrayList<Object[]> identificadores;
    private static Produccion p;
    
    public static void setArrayErrores(ArrayList<ErrorLSSL> errores, ArrayList<Object[]> identificadores) {
        AnalisisSemantico.errores = errores;
        AnalisisSemantico.identificadores = identificadores;
    }
    
    public static void analizar(String expresion, Produccion p) {
        AnalisisSemantico.p = p;
        if (expresion.contains("dist")) {
            analizarFunDistancia(expresion, p);
        } else if (expresion.contains("girar")){
            analizarFunGirar(expresion, p);
        } else if (expresion.contains("ciclo")) {
            analizarCiclo(expresion, p);
        } else if (expresion.contains("bucle")) {
            analizarBucle(expresion, p);
        } else {
            if (esExpresionBool(expresion) && !tieneDeclaracion(expresion)) {
                return;
            }
            validarExpresion(expresion);
        }
    }
    
    private static void analizarBucle(String expresion, Produccion p) {
        StringTokenizer token = new StringTokenizer(expresion.trim(), "()", true);
        token.nextToken();
        token.nextToken();
        String arg = token.nextToken();
        if (arg.contains(">") || arg.contains("<") || arg.contains("=")) {
            ArbolExpresionSemantica a = new ArbolExpresionSemantica(p);
            a.crearArbol("bool c=" + arg);
        } else {
            String tipo = queEs(arg);
            if (tipo != null) {
                if (!tipo.equals("bool")) {
                    errores.add(new ErrorLSSL(2, "× Error semántico {}: tipo de dato incompatible, se esperaba un tipo de dato 'bool' en [] [#, %]", p, true));
                }
            }
        }
    }
    
    private static void analizarCiclo(String expresion, Produccion p) {
        StringTokenizer token = new StringTokenizer(expresion.trim(), "(),", true);
        token.nextToken();
        token.nextToken();
        String arg1 = queEs(token.nextToken());
        token.nextToken();
        String arg2 = queEs(token.nextToken());
        token.nextToken();
        String arg3 = queEs(token.nextToken());
        
        if (arg1 != null) {
            if (!arg1.equals("num"))
                errores.add(new ErrorLSSL(2, "× Error semántico {}: tipo de dato incompatible, se esperaba un tipo de dato 'num' en [] [#, %]", p, true));
        }
        
        if (arg2 != null) {
            if (!arg2.equals("num"))
                errores.add(new ErrorLSSL(2, "× Error semántico {}: tipo de dato incompatible, se esperaba un tipo de dato 'num' en [] [#, %]", p, true));
        }
        
        if (arg3 != null) {
            if (!arg3.equals("num"))
                errores.add(new ErrorLSSL(2, "× Error semántico {}: tipo de dato incompatible, se esperaba un tipo de dato 'num' en [] [#, %]", p, true));
        }
    }
    
    private static void analizarFunGirar(String expresion, Produccion p) {
        StringTokenizer token = new StringTokenizer(expresion.trim(), "(),", true);
        token.nextToken();
        token.nextToken();
        String arg1 = queEs(token.nextToken());
        token.nextToken();
        String arg2 = queEs(token.nextToken());
        
        if (arg1 != null) {
            if (!arg1.equals("grado"))
                errores.add(new ErrorLSSL(2, "× Error semántico {}: tipo de dato incompatible, se esperaba un tipo de dato 'grado' en [] [#, %]", p, true));
        }
        
        if (arg2 != null) {
            if (!arg2.equals("servo"))
                errores.add(new ErrorLSSL(2, "× Error semántico {}: tipo de dato incompatible, se esperaba un tipo de dato 'servo' en [] [#, %]", p, true));
        }
    }
    
//    dist(d,CENTIM)
//    dist(d,CENTIM)>=d
//    bool cond=dist(d,CENTIM)>=d
    
    private static void analizarFunDistancia(String expresion, Produccion p) {
        if (expresion.contains("=") || expresion.contains("<") || expresion.contains(">")) {
            if (tieneDeclaracion(expresion)) {
                validarExpresion(expresion);
            } 
        } else {
            StringTokenizer token = new StringTokenizer(expresion, "(),<>= ", true);
            String t = token.nextToken();
            if (!t.trim().isEmpty()) {
                if (t.equals("dist")) {
                    token.nextToken();
                    String arg1 = token.nextToken();
                    if (esVariable(arg1)) {
                        String tipo = existeVariable(arg1);
                        if (tipo == null) {
                            errores.add(new ErrorLSSL(1, "× Error semántico {}: variable inexistente (" + arg1 + ") en [] [#, %]", p, true));
                        } else {
                            if (!tipo.equals("sprox")) {
                                errores.add(new ErrorLSSL(2, "× Error semántico {}: tipo de dato incompatible, se esperaba un dato sprox en [] [#, %]", p, true));
                            }
                        }
                    }
                    token.nextToken();
                    String arg2 = token.nextToken();
                    if (!esVariableCEstatica(arg2)) {
                        errores.add(new ErrorLSSL(3, "× Error semántico {}: se esperaba una variable estatica de conversión en [] [#, %]", p, true));
                    }
                }
            }
        }
    }
    
    // dist(d,CENTIM)>=d
    private static void validarExpresion(String e) {
        ArbolExpresionSemantica a = new ArbolExpresionSemantica(p);
        a.crearArbol(e);
    }
    
    public static String existeVariable(String t) {
        for (Object[] i: identificadores) {
            if (i[1].equals(t)) {
                return i[0].toString();
            }
        }
        
        return null;
    }
    
    private static boolean esVariableCEstatica(String t) {
        return (t.equals("CENTIM") || t.equals("METRO") || t.equals("PIE"));
    }
    
    private static boolean tieneDeclaracion(String t) {
        return (t.contains("grado") || t.contains("num") || t.contains("bool") || t.contains("sprox") || t.contains("servo"));
    }
    
    public static boolean esVariable(String t) {
        try {
            Double.parseDouble(t);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }
    
    public static String queEs(String x) {
        if (x == null) {
            return x;
        }
        
        if (AnalisisSemantico.esVariable(x)) {
            if (esTipoDato(x)) {
                return x;
            } else if (x.equals("SERVO_1") || x.equals("SERVO_2") || x.equals("SERVO_3") || x.equals("SERVO_4")) {
                return "servo";
            } else if (x.equals("SPROX_NORTE") || x.equals("SPROX_SUR") || x.equals("SPROX_OESTE") || x.equals("SPROX_ESTE")) {
                return "sprox";
            } else if (x.equals("VERDAD") || x.equals("FALSO")) {
                return "bool";
            } else if (x.contains("°")) {
                return "grado";
            }
            
            String tipo = AnalisisSemantico.existeVariable(x);
            if (tipo == null) {
                AnalisisSemantico.errores.add(new ErrorLSSL(100, "× Error semántico {}: variable inexistente (" + x + ") en [] [#, %]", p, true));
            }
            return tipo;
        } 
        
        return "num";
    }
    
    public static boolean esTipoDato(String x) {
        return (x.equals("grado") || x.equals("num") || x.equals("bool") || x.equals("sprox") || x.equals("servo"));
    }
    
    public static boolean esExpresionBool(String x) {
        return (x.contains(">") || x.contains("<") || x.contains("=="));
    }
}
