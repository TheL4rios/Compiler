package codigo.semantico;

import codigo.ErrorLSSL;
import codigo.Produccion;
import codigo.cuadruplos.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author Larios
 */

public class ArbolExpresionSemantica extends ArbolExpresion {
    private Produccion p;
    
    public ArbolExpresionSemantica(Produccion p) {
        super.tAcero();
        this.p = p;
    }
    
    @Override
    public void asignar(String valor) {
        if (!super.tipoDato.equals(AnalisisSemantico.queEs(valor))) {
            AnalisisSemantico.errores.add(new ErrorLSSL(2, "× Error semántico {}: tipo de dato incompatible, se esperaba un tipo: " + super.tipoDato + " en [] [#, %]", p, true));
        }
    }
    
    @Override
    public void crear(Nodo nodo) {
        super.contador++;
        String nI = AnalisisSemantico.queEs(nodo.getIzquierdo().getValor());
        String nD = AnalisisSemantico.queEs(nodo.getDerecho().getValor());
        if (nI == null || nD == null) {
            nodo.setValor(null);
        } else {
            if (nI.equals(nD)) {
                if (AnalisisSemantico.esExpresionBool(nodo.getValor())) {
                    if (!nI.equals("num")) {
                        AnalisisSemantico.errores.add(new ErrorLSSL(4, "× Error semántico {}: tipos de dato incompatible en la expresión dentro de [], sólo se pueden comparar tipos de dato numéricos [#, %]", p, true));
                        nodo.setValor(null);
                    } else {
                        nodo.setValor("bool");
                    }
                } else {
                    nodo.setValor(nD);
                }
            } else {
                AnalisisSemantico.errores.add(new ErrorLSSL(2, "× Error semántico {}: tipos de dato incompatible en la expresión dentro de [] [#, %]", p, true));
                nodo.setValor(null);
            }
        }
        
//        if (super.contador == super.operaciones) {
//            if (!super.tipoDato.equals(super.getRaiz().getValor())) {
//                //AnalisisSemantico.errores.add(new ErrorLSSL(102, "× Error semántico {}: tipo de dato incompatible, se esperaba un tipo '" + super.tipoDato + "' en [] [#, %]", p, true));
//            }
//        }
    }
    
    @Override
    public String existeFuncion(String t, StringTokenizer token) {
        if (t.equals("dist")) {
            String temporal = token.nextToken();
            //System.out.println(temporal);
            if (temporal.equals("(")) {
                String arg1 = token.nextToken();
                token.nextToken();
                String arg2 = token.nextToken();
                token.nextToken();
                return "num";
            } else {
                super.arbolAnalisis(t);
                super.arbolAnalisis(temporal);
                return "";
            }
        }
        return t;
    }
}
