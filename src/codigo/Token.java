package codigo;
/**
 * @author jesus
 */
public class Token {
    private final String lexema, comp_lexico; 
    private final int linea, columna;
   
    public Token(String lexema, String comp_lexico, int linea, int columna) {
        this.lexema = lexema;
        this.comp_lexico = comp_lexico;
        this.linea = linea+1;
        this.columna = columna+1;

        if(this.lexema == null)
            System.out.println("\n"+Metodos.ANSI_RED_BLACK + "Se ha creado un token que contiene un valor nulo como lexema, esto podría llegar a generar errores ó\n"+
                                    Metodos.ANSI_RED_BLACK + "resultados incorrectos. El token referido es el siguiente:\n"+ Metodos.ANSI_BLUE_BLACK+this);   
        else if(this.lexema.equals(""))
            System.out.println("\n"+Metodos.ANSI_RED_BLACK + "Se ha creado un token que contiene una cadena vacía como lexema, esto podría llegar a generar errores\n"+
                                    Metodos.ANSI_RED_BLACK + "ó resultados incorrectos. El token referido es el siguiente:\n"+Metodos.ANSI_BLUE_BLACK+this);    
        if(this.comp_lexico == null)
            System.out.println("\n"+Metodos.ANSI_RED_BLACK + "Se ha creado un token que contiene un valor nulo como componente léxico, esto generará conflictos ó\n"+
                                    Metodos.ANSI_RED_BLACK + "resultados incorrectos al momento de agrupar las producciones, proceda a corregirlo. El token referido\n"+
                                    Metodos.ANSI_RED_BLACK+  "es el siguiente:\n"+Metodos.ANSI_BLUE_BLACK+this);
        else if(this.comp_lexico.contains(" "))
            System.out.println("\n"+Metodos.ANSI_RED_BLACK + "Se ha creado un token que contiene uno o más espacios en el componente léxico, esto generará conflictos\n"+
                                    Metodos.ANSI_RED_BLACK + "ó resultados incorrectos al momento de agrupar las producciones, proceda a corregirlo. El token referido\n"+
                                    Metodos.ANSI_RED_BLACK + "es el siguiente:\n"+Metodos.ANSI_BLUE_BLACK+this);
        else if(this.comp_lexico.equals(""))
            System.out.println("\n"+Metodos.ANSI_RED_BLACK + "Se ha creado un token que contiene una cadena vacía como componente léxico, esto generará conflictos ó\n"+
                                    Metodos.ANSI_RED_BLACK + "resultados incorrectos al momento de agrupar las producciones, proceda a corregirlo. El token referido\n"+
                                    Metodos.ANSI_RED_BLACK + "es el siguiente:\n"+Metodos.ANSI_BLUE_BLACK+this);
        if(this.linea<=0 | this.columna <=0)
            System.out.println("\n"+Metodos.ANSI_RED_BLACK + "Se ha creado un token que contiene un número de línea y/o columna menor o igual que 0, lo cuál es ilógico, esto\n"+
                                    Metodos.ANSI_RED_BLACK + "podría llegar a generar errores ó resultados incorrectos, proceda a corregirlo. El token referido es el\n"+
                                    Metodos.ANSI_RED_BLACK + "siguiente:\n"+Metodos.ANSI_BLUE_BLACK+this);
    }

    public String getLexema() {
        return lexema;
    }

    public String getComp_lexico() {
        return comp_lexico;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }
    @Override
    public String toString(){
        return "Token("+lexema+", "+comp_lexico+", "+linea+", "+columna+")";
    }   
}
