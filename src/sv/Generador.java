package sv;

import java.io.BufferedReader;
// import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
// import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Larios
 */
public class Generador {
    private final ArrayList<String[]> codigo;
    
    public Generador() {
        codigo = new ArrayList<>();
        load();
    }
    
    public final void load() {
        codigo.add(new String[]{"(.*)mover(.*)derecha(.*)", 
                                "MoverDerecha"});
        codigo.add(new String[]{"(.*)muevelo(.*)derecha(.*)", 
                                "MoverDerecha"});
        codigo.add(new String[]{"(.*)mueve(.*)derecha(.*)", 
                                "MoverDerecha"});
        
        codigo.add(new String[]{"(.*)mover(.*)izquierda(.*)", 
                                "MoverIzquierda"});
        codigo.add(new String[]{"(.*)muevelo(.*)izquierda(.*)", 
                                "MoverIzquierda"});
        codigo.add(new String[]{"(.*)mueve(.*)izquierda(.*)", 
                                "MoverIzquierda"});
        
        codigo.add(new String[]{"(.*)mover(.*)abajo(.*)", 
                                "MoverAbajo"});
        codigo.add(new String[]{"(.*)muevelo(.*)abajo(.*)", 
                                "MoverAbajo"});
        codigo.add(new String[]{"(.*)mueve(.*)abajo(.*)", 
                                "MoverAbajo"});
        
        codigo.add(new String[]{"(.*)mover(.*)arriba(.*)", 
                                "MoverArriba"});
        codigo.add(new String[]{"(.*)muevelo(.*)arriba(.*)", 
                                "MoverArriba"});
        codigo.add(new String[]{"(.*)mueve(.*)arriba(.*)", 
                                "MoverArriba"});
        
        codigo.add(new String[]{"(.*)agarrar(.*)", 
                                "Agarrar"});
        codigo.add(new String[]{"(.*)agarra(.*)", 
                                "Agarrar"});
        codigo.add(new String[]{"(.*)tomar(.*)", 
                                "Agarrar"});
        codigo.add(new String[]{"(.*)toma(.*)", 
                                "Agarrar"});
        
        codigo.add(new String[]{"(.*)soltar(.*)", 
                                "Soltar"});
        codigo.add(new String[]{"(.*)suelta(.*)", 
                                "Soltar"});
        codigo.add(new String[]{"(.*)liberar(.*)", 
                                "Soltar"});
        codigo.add(new String[]{"(.*)libera(.*)", 
                                "Soltar"});
        
        codigo.add(new String[]{"(.*)reset(.*)", 
                                "Reset"});
        codigo.add(new String[]{"(.*)restablecer(.*)", 
                                "Reset"});
        codigo.add(new String[]{"(.*)restablece(.*)", 
                                "Reset"});
        codigo.add(new String[]{"(.*)restaurar(.*)", 
                                "Reset"});
        codigo.add(new String[]{"(.*)posición(.*)original(.*)", 
                                "Reset"});
    }
    
    private Pattern p;
    private Matcher m;
    
    public String getCodigo(String comando) {
        comando = comando.toLowerCase();
        for (String[] c : codigo) {
            p = Pattern.compile(c[0]);
            m = p.matcher(comando);
            
            if (m.matches()) {
                return c[1];
            }
        }
        return null;
    }
    
    private String getPlantilla(String f) {
        String cadena, temporal = "";
        try {
            FileReader archivo = new FileReader(System.getProperty("user.dir") + "/src/plantillas/" + f);
            try (BufferedReader buffer = new BufferedReader(archivo)) {
                while ((cadena = buffer.readLine()) != null) {
                    if (cadena.contains("\\t")) {
                        cadena = cadena.replace("\\t", "");
                        temporal += "\t" + cadena + "\n";
                    } else {
                        temporal += cadena + "\n";
                    }
                }
                return temporal;
            }
        } catch (IOException ex) {
            System.out.println("No se puede localizar el archivo " + f);
        }
        return null;
    }
}
