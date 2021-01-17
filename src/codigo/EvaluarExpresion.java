package codigo;

import codigo.ThreadExec;
import codigo.cuadruplos.ArbolExpresion;
import codigo.cuadruplos.Nodo;
import java.util.StringTokenizer;

/**
    *
    * @author larios
    */
   
public class EvaluarExpresion extends ArbolExpresion {
    private boolean resultado = false;
    private ThreadExec x;
    
    public Boolean getRes(String expresion, ThreadExec x) {
        super.tAcero();
        this.x = x;
        if (expresion.equals("VERDAD")) return true;
        if (expresion.equals("FALSO")) return false;
        super.crearArbol("bool x=" + expresion);
        System.out.println(expresion + " -> " + resultado);
        return resultado;
    }
    
    @Override
    public void crear(Nodo nodo) {
        super.contador++;
        String nI = nodo.getIzquierdo().getValor();
        String nD = nodo.getDerecho().getValor();
        String op = nodo.getValor();
        
        if (esIdentificador(nI)) {
            if (esIdentificador(nD)) {
                evaluar(this.x.getValor(nI), this.x.getValor(nD), op);
            } else {
                evaluar(this.x.getValor(nI), nD, op);
            }
        } else {
            if (esIdentificador(nD)) {
                evaluar(nI, this.x.getValor(nD), op);
            } else {
                evaluar(nI, nD, op);
            }
        }
    }
    
    private void evaluar(String nI, String nD, String op) {
        double _nI = Double.parseDouble(nI);
        double _nD = Double.parseDouble(nD);
        
        switch (op) {
            case ">":
                System.out.println(_nI + ">" + _nD);
                resultado = _nI > _nD;
                break;
            case "<":
                System.out.println(_nI + "<" + _nD);
                resultado = _nI < _nD;
                break;
            case "<=":
                System.out.println(_nI + "<=" + _nD);
                resultado = _nI <= _nD;
                break;
            case ">=":
                System.out.println(_nI + ">=" + _nD);
                resultado = _nI >= _nD;
                break;
            case "==":
                System.out.println(_nI + "==" + _nD);
                resultado = _nI == _nD;
                break;
            default:
                break;
        }
    }

    private boolean esIdentificador(String e) {
        try {
            Double.parseDouble(e);
            return false;
        } catch(NumberFormatException ex) {
            return true;
        }
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
                
                String fun = String.valueOf(this.x.funDist(this.x.getValor(arg1), arg2));
                System.out.println("Valor retornado por funcion ->" + fun);
                return fun;
            } else {
                arbolAnalisis(t);
                arbolAnalisis(temporal);
                return "";
            }
        }
        return t;
    }
}