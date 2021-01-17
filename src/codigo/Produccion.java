package codigo;

import java.util.ArrayList;
/**
 *
 * @author jesus
 */
public class Produccion {
    private String nombre;
    private final ArrayList<Token> tokens;
    private int linea, columna, lineaF, columnaF;
    
    public Produccion(){
        nombre = null;
        tokens = new ArrayList();
        linea = columna = lineaF = columnaF = 0;
    }
    
    public Produccion(Token tk){
        tokens = new ArrayList();
        nombre = tk.getComp_lexico();
        tokens.add(tk);
        linea = lineaF =  tk.getLinea();
        columna = columnaF = tk.getColumna();       
    }
        
    public String getNombre() {
        return nombre;
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }

    public int getLineaF() {
        return lineaF;
    }

    public int getColumnaF() {
        return columnaF;
    }
    
    public int getTamTokens(){
        return tokens.size();
    }

    public void agregarTokens(Produccion prod){
        ArrayList<Token> tks = prod.getTokens();
        tks.forEach((tk) -> {
            tokens.add(tk);
        });
        if(tks.size()>0){
            Token tk = tks.get(0);
            if(nombre == null)
                nombre = tk.getComp_lexico().toUpperCase();
            tk = tks.get(tks.size()-1);
            lineaF = tk.getLinea();
            columnaF = tk.getColumna();
        }        
        if(tokens.size()>0){
            Token tk = tokens.get(0);
            linea = tk.getLinea();
            columna = tk.getColumna();
        }
    }
    
    public String rangoLexema(int i){
        int indice = i, tam = tokens.size();        
        if(indice >= 0){
            if(indice > tam-1){
                System.out.println("\n"+Metodos.ANSI_RED_BLACK+"El índice excede el tamaño de tokens de la producción: "+indice+">"+(tam-1));
                return null;
            }
        }
        else if(indice < -tam){
            System.out.println("\n"+Metodos.ANSI_RED_BLACK+"El índice es inferior al tamaño de tokens de la producción: "+indice+"<-"+tam);
            return null;
        }
        if(indice<0)
            indice = indice+tam;
        
        return tokens.get(indice).getLexema();
    }
    
    public String rangoLexema(int i, int j){
        int indiceI = i, indiceF = j, tam = tokens.size();
        if(indiceI >= 0){
            if(indiceI > tam-1){
                System.out.println("\n"+Metodos.ANSI_RED_BLACK+"El índice inicial excede el tamaño de tokens de la producción: "+indiceI+">"+(tam-1));
                return null;
            }
        }
        else if(indiceI < -tam){
            System.out.println("\n"+Metodos.ANSI_RED_BLACK+"El índice inicial es inferior al tamaño de tokens de la producción: "+indiceI+"<-"+tam);
            return null;
        }
        if(indiceF >= 0){
            if(indiceF > tam-1){
                System.out.println("\n"+Metodos.ANSI_RED_BLACK+"El índice final excede el tamaño de tokens de la producción: "+indiceF+">"+(tam-1));
                return null;
            }
        }
        else if(indiceF < -tam){
            System.out.println("\n"+Metodos.ANSI_RED_BLACK+"El índice final es inferior al tamaño de tokens de la producción: "+indiceF+"<-"+tam);
            return null;
        }
        if(indiceI<0 && indiceF>=0)
            indiceI = indiceI+tam;
        else if(indiceI>=0 && indiceF<0)
            indiceF = indiceF+tam;
        
        if(indiceI>indiceF){
            System.out.println("\n"+Metodos.ANSI_RED_BLACK+"El índice inicial debe de ser menor que el índice final: "+indiceI+">"+indiceF);
            return null;
        }
        if(indiceI<0)
            indiceI = indiceI+tam;
        if(indiceF<0)
            indiceF = indiceF+tam;
        
        String lexemas = "";
        for(int k = indiceI; k<=indiceF; k++)
            if(k != indiceF)
                lexemas += tokens.get(k).getLexema()+" ";
            else
                lexemas += tokens.get(k).getLexema();
        
        return eliminarEspContExcept(lexemas, "[A-Za-zÑñÁÉÍÓÚáéíóúÜü_]","[A-Za-zÑñÁÉÍÓÚáéíóúÜü_]");
    }
    
    private String eliminarEspContExcept(String cadena, String expInicial, String expFinal){
        String cadenaT = "";
        for(int i=0; i<cadena.length(); i++){
            String car = cadena.substring(i, i+1);
            if(car.equals(" ")){
                String carAnt = cadena.substring(i-1,i),
                       carPost = cadena.substring(i+1, i+2);
                if(carAnt.matches(expInicial) && carPost.matches(expFinal))
                    cadenaT += car;     
            }
            else cadenaT += car;
        }
        return cadenaT;
    }
    
    public boolean nombreIgualA(String nombre){
        return this.nombre.equals(nombre); 
    }
    
    public boolean nombreIgualA(String[] nombres){
        for(String nom: nombres)
            if(nom.equals(nombre))
                return true;     
        
        return false;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
  
    @Override
    public String toString(){
        String tks = "";
        int cantTokens = tokens.size(); 
        for(int i=0; i<cantTokens; i++)
            if(i != cantTokens-1)
                tks += tokens.get(i)+",\n";
            else
                tks += tokens.get(i);
        
        return "Produccion("+nombre+", "+linea+", "+columna+", "+lineaF+", "+columnaF+", [\n"+tks+"])";
    }
}