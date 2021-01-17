package test;

import codigo.EvaluarExpresion;
import codigo.Produccion;
import codigo.ThreadExec;
import java.util.ArrayList;

/**
 *
 * @author Larios
 */
public class TestExpresion {
    public static void main(String[] arg) {
        EvaluarExpresion ee = new EvaluarExpresion();
        System.out.println(ee.getRes("dist(spN,CENTIM)>=200", new ThreadExec(new ArrayList<Produccion>())));
    }
}
