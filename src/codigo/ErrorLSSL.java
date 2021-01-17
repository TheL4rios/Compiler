package codigo;

import static codigo.Metodos.formatoCadena;
/**
 * @author jesus
 */
public class ErrorLSSL {
    private String desc, lexemas;
    private final int numero, linea, columna;

    public ErrorLSSL(int numero, String desc, Token token) {
        this.numero = numero;
        this.desc = desc;
        lexemas = token.getLexema();
        linea = token.getLinea();
        columna = token.getColumna();
    }

    public ErrorLSSL(int numero, String desc, Produccion produccion, boolean lineaColInicial) {
        this.numero = numero;
        this.desc = desc;
        lexemas = produccion.rangoLexema(0, -1);
        if(lineaColInicial){
            linea = produccion.getLinea();
            columna = produccion.getColumna();
        }
        else{
            linea = produccion.getLineaF();
            columna = produccion.getColumnaF();
        }
    }      

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }

    @Override
    public String toString(){
        if(lexemas == null)
            lexemas = "~VALOR NULO~";
        if(desc == null)
            desc = "~DESCRIPCIÓN NULA~";
        String error;        
        error = formatoCadena(desc, "[]", lexemas);
        error = formatoCadena(error, "{}", String.valueOf(numero));
        error = formatoCadena(error, "#", String.valueOf(linea));
        error = formatoCadena(error, "%", String.valueOf(columna));
        
        return error;
    }
}
