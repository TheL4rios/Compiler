package codigo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author jesus
 */
public class LoadJFlex {

    public static void main(String omega[]) {
        try {
            Path rutaLexer = Paths.get(System.getProperty("user.dir") + "/src/codigo/Lexer.java");
            if (Files.exists(rutaLexer)) {
                Files.delete(rutaLexer);
            }
            jflex.Main.generate(new File(System.getProperty("user.dir") + "/src/codigo/Lexer.flex"));

            Path rutaLexerC = Paths.get(System.getProperty("user.dir") + "/src/codigo/LexerC.java");
            if (Files.exists(rutaLexerC)) {
                Files.delete(rutaLexerC);
            }
            jflex.Main.generate(new File(System.getProperty("user.dir") + "/src/codigo/LexerC.flex"));
            /*
            Path rutaLexerCup = Paths.get(System.getProperty("user.dir")+"/src/codigo/LexerCup.java");
            if(Files.exists(rutaLexerCup))
                Files.delete(rutaLexerCup);
            jflex.Main.generate(new File(System.getProperty("user.dir")+"/src/codigo/LexerCup.flex"));
            
            String[] rutaS = {"-parser", "BASE", System.getProperty("user.dir")+"/src/codigo/BASE.cup"}; 
            java_cup.Main.main(rutaS);
           
            Path rutaSym = Paths.get(System.getProperty("user.dir")+"/src/codigo/sym.java");
            if(Files.exists(rutaSym))
                Files.delete(rutaSym);
        
            Path rutaSintax = Paths.get(System.getProperty("user.dir")+"/src/codigo/BASE.java");
            if(Files.exists(rutaSintax))
                Files.delete(rutaSintax);
                
            Files.move(Paths.get(System.getProperty("user.dir")+"/sym.java"),
                   Paths.get(System.getProperty("user.dir")+"/src/codigo/sym.java"));
        
            Files.move(Paths.get(System.getProperty("user.dir")+"/BASE.java"),
                  Paths.get(System.getProperty("user.dir")+"/src/codigo/BASE.java"));
             */
        } catch (jflex.GeneratorException | IOException e) {
            System.out.println("Error en la compilación de los archivos JFlex y Jcup, se cargarán los archivos por defecto.");
        } catch (Exception ex) {
            System.out.println("Error en la compilación de los archivos JFlex y Jcup, se cargarán los archivos por defecto.");
        }
    }
}
