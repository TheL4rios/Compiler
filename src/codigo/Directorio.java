package codigo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
/**
 *
 * @author Marissa, Yisus
 */
public class Directorio {
    JFileChooser selecFile = new JFileChooser();
    File file;
    String[] options = new String[]{"Guardar y continuar", "Descartar"};
    String titulo = "RoboTec", extension = ".rbt";
    public String getTextFile(File file){
        String text = "";
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
            while(true){
            int b = entrada.read();
                if(b!=-1)
                    text += (char)b;
                else
                    break;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("El archivo no pudo ser encontrado... "+ex.getMessage());
            return null;
        } catch (IOException ex) {
            System.out.println("Error al leer el archivo... "+ex.getMessage());
            return null;
        }
        return text;   
    }
    
    public boolean saveFile(File archivo, String text){
        try {
            FileOutputStream output = new FileOutputStream(archivo);
            byte[] bytesText = text.getBytes();
            output.write(bytesText);
        } catch (FileNotFoundException ex) {
            System.out.println("El archivo no pudo ser encontrado... "+ex.getMessage());
            return false;
        } catch (IOException ex) {
            System.out.println("Error al escribir en el archivo... "+ex.getMessage());
            return false;
        } 
        return true;
    }
    
    
    public boolean guardarEditNuevo(File fileG, JFileChooser selecFileG, Compilador compF){
        int x;
        if(compF.getTitle().equals(titulo+"*"))
            x = 0;
        else
            x = JOptionPane.showOptionDialog(compF, "El archivo actual está siendo editado, ¿desea guardar los cambios?",
                                            "¿Descartar edición?", -1, 3, null, options, options[0]);
        if(x==0){
            if(selecFileG.getSelectedFile() != null){
                boolean save = saveFile(fileG, compF.jtpCode.getText());
                if(save)
                    compF.setTitle(fileG.getName());
                    
            }
            else if(compF.getTitle().equals(titulo+"*")){
                int y = JOptionPane.showOptionDialog(compF, "¿Desea guardar el archivo actual?",
                                                    "¿Descartar edición de archivo nuevo?", -1, 3, null, options, options[0]);
                if(y==0){
                    if(selecFileG.showDialog(compF, "Guardar") == JFileChooser.APPROVE_OPTION){
                        fileG = selecFileG.getSelectedFile();
                        String fileGname = fileG.getName();
                
                        if(fileGname.endsWith(extension)){
                            if(!fileGname.split("[.]")[0].replace(" ","").equals("") && !fileGname.contains(" ")){
                                if(!fileG.exists())
                                    guardarArch(fileG, compF);  
                                else{
                                    int z = JOptionPane.showConfirmDialog(compF, "Ya hay un archivo con este nombre, ¿desea "
                                                                         +"sobreescribirlo?", "Sobreescribir archivo", 2);
                                    if(z == 0)
                                        guardarArch(fileG, compF);                                        
                                }
                            }
                            else{
                                JOptionPane.showMessageDialog(compF, "Escriba un nombre válido para el archivo",
                                                              "Nombre inválido", 2);
                                return false;
                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(compF, "El archivo debe de tener la extensión '"+extension+"'",
                                                          "Extensión inválida", 2); 
                            return false;
                        }
                    }
                    
                }
                else
                     return true;
            }
            else{
            int z = JOptionPane.showConfirmDialog(compF, "Ya hay un archivo con este nombre, ¿desea "
                                                                         +"sobreescribirlo?", "Sobreescribir archivo", 2);
                if(z == 0)
                guardarArch(fileG, compF); 
            }
               
        }
        return true;
    }
    
    
    public boolean guardarEditAbrir(File fileG, JFileChooser selecFileG, Compilador compF){
        int x;
        if(compF.getTitle().equals(titulo+"*"))
            x = 0;
        else
            x = JOptionPane.showOptionDialog(compF, "El archivo actual está siendo editado, ¿desea guardar los cambios?",
                                            "¿Descartar edición?", -1, 3, null, options, options[0]);
        if(x==0){
            if(selecFileG.getSelectedFile() != null){
                boolean save = saveFile(fileG, compF.jtpCode.getText());
                if(save)
                    compF.setTitle(fileG.getName());
                    
            }
            else if(compF.getTitle().equals(titulo+"*")){
                int y = JOptionPane.showOptionDialog(compF, "¿Desea guardar el archivo actual?",
                                                    "¿Descartar edición de archivo nuevo?", -1, 3, null, options, options[0]);
               
                if(y==0){
                    if(selecFileG.showDialog(compF, "Guardar") == JFileChooser.APPROVE_OPTION){
                        fileG = selecFileG.getSelectedFile();
                        String fileGname = fileG.getName();
                
                        if(fileGname.endsWith(extension)){
                            if(!fileGname.split("[.]")[0].replace(" ","").equals("") && !fileGname.contains(" ")){
                                if(!fileG.exists())
                                    guardarArch(fileG, compF);  
                                else{
                                    int z = JOptionPane.showConfirmDialog(compF, "Ya hay un archivo con este nombre, ¿desea "
                                                                         +"sobreescribirlo?", "Sobreescribir archivo", 2);
                                    if(z == 0)
                                        guardarArch(fileG, compF);  
                                    else{
                                    
                                    }
                                }
                            }
                            else{
                                JOptionPane.showMessageDialog(compF, "Escriba un nombre válido para el archivo",
                                                              "Nombre inválido", 2);
                                return false;
                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(compF, "El archivo debe de tener la extensión '"+extension+"'",
                                                          "Extensión inválida", 2); 
                            return false;
                        }
                    }
                    
                }
                else{
                     compF.jtpCode.setText("");
                     compF.setTitle(titulo);
                     compF.resetearComp();
                }
            }             
        }
        else{
            compF.jtpCode.setText("");
            compF.setTitle(titulo);
            compF.resetearComp();
        }
        return true;
    }
    
    
    public void guardarArch(File file, Compilador compF){
        boolean save = saveFile(file, compF.jtpCode.getText());   
        if(save)
            compF.setTitle(file.getName());
        else
            JOptionPane.showMessageDialog(compF, "No se pudo guardar el archivo",
                                         "Error desconocido", 2); 
    }
        
    public void Nuevo(Compilador compF){ 
        file = selecFile.getSelectedFile();
        
        if(compF.getTitle().contains("*")){
            if(guardarEditNuevo(file, selecFile, compF)){
                compF.setTitle(titulo);
                compF.jtpCode.setText("");
                compF.resetearComp();
                selecFile = new JFileChooser();
                file = null;
            }
        }
        else{
            compF.setTitle(titulo);
            compF.jtpCode.setText("");
            compF.resetearComp();
            selecFile = new JFileChooser();
            file = null;
        }
    }
    
    public void Salir(Compilador compF){
        if(compF.getTitle().contains("*")){
            if(guardarEditAbrir(file, selecFile, compF)){
                selecFile = new JFileChooser();
                file = null;
            }
        }
    }
    
    public boolean Abrir(Compilador compF){ 
        if(compF.getTitle().contains("*"))
            guardarEditAbrir(file, selecFile, compF);

        JFileChooser tSelecFile = new JFileChooser();
        File tFile;
        if(tSelecFile.showDialog(compF, "Abrir") == JFileChooser.APPROVE_OPTION){
            tFile = tSelecFile.getSelectedFile();
            String filename = tFile.getName();
              
            if(filename.endsWith(extension)){
                if(!filename.split("[.]")[0].replace(" ","").equals("") && !filename.contains(" ")){
                    if(!tFile.exists())
                        JOptionPane.showMessageDialog(compF, "El archivo que sea desea abrir no existe en el directorio especificado",
                                                     "Archivo no encontrado", 2);      
                    else{
                        String t = getTextFile(tFile);
                           
                        if(t != null){
                            compF.jtpCode.setText(t);
                            compF.setTitle(tFile.getName());
                            compF.resetearComp();
                            selecFile = tSelecFile;
                            file = tFile;
                        }
                        else{
                            JOptionPane.showMessageDialog(compF, "Error al leer el archivo",
                                                         "Error desconocido", 2);
                            return false;
                        }
                    }
                }
                else{
                    JOptionPane.showMessageDialog(compF, "Escriba un nombre válido para el archivo",
                                                  "Nombre inválido", 2); 
                    return false;
                }
            }
            else{
                JOptionPane.showMessageDialog(compF, "El archivo debe de tener la extensión '"+extension+"'",
                                             "Extensión inválida", 2);  
                return false;
            }
        }
        else
            return false;
        return true;
    }
    
    public boolean Guardar(Compilador compF){
        if(file != null)
            guardarArch(file, compF);
        else{
            JFileChooser tSelecFile = new JFileChooser();
            File tFile;
            
            if(tSelecFile.showDialog(compF, "Guardar") == JFileChooser.APPROVE_OPTION){
                tFile = tSelecFile.getSelectedFile();
                String filename = tFile.getName();
                
                if(filename.endsWith(extension)){
                    if(!filename.split("[.]")[0].replace(" ","").equals("") && !filename.contains(" ")){
                        if(!tFile.exists()){
                            guardarArch(tFile, compF); 
                            file = tFile;
                            selecFile = tSelecFile;
                        }
                        else{
                            int x = JOptionPane.showConfirmDialog(compF, "Ya hay un archivo con este nombre, ¿desea "
                                                                +"sobreescribirlo?", "Sobreescribir archivo", 2);
                            if(x == 0){
                                guardarArch(tFile, compF); 
                                file = tFile;
                                selecFile = tSelecFile;
                            }
                            else{
                                selecFile = new JFileChooser();
                                file = null;
                                return false;
                            }
                            
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(compF, "Escriba un nombre válido para el archivo",
                                                  "Nombre inválido", 2);
                        selecFile = new JFileChooser();
                        file = null;
                        return false;
                    }
                }
                else{
                    JOptionPane.showMessageDialog(compF, "El archivo debe de tener la extensión '"+extension+"'",
                                                  "Extensión inválida", 2); 
                    selecFile = new JFileChooser();
                    file = null;
                    return false;
                }
            }
            else 
                return false;
        }
        return true;
    }
    
    
    public void GuardarC(Compilador compF){
        JFileChooser tSelecFile = new JFileChooser();
        
        if(tSelecFile.showDialog(compF, "Guardar como") == JFileChooser.APPROVE_OPTION){
            File tFile;
            tFile = tSelecFile.getSelectedFile();
            String filename = tFile.getName();
                
            if(filename.endsWith(extension)){
                if(!filename.split("[.]")[0].replace(" ","").equals("") && !filename.contains(" ")){
                    guardarArch(tFile, compF);  
                    file = tFile;
                    selecFile = tSelecFile;
                }
                else
                    JOptionPane.showMessageDialog(compF, "Escriba un nombre válido para el archivo",
                                                 "Nombre inválido", 2); 
            }
            else
                JOptionPane.showMessageDialog(compF, "El archivo debe de tener la extensión '"+extension+"'",
                                             "Extensión inválida", 2);  
        }
    }

}//fin de la clase

/*
Esta clase de Java fue creada por Yisus y Marissa para la materia de Lenguajes y Autómatas I, 
impartida por la docente Sonia Alvarado Mares. Hacerlo costó mucho trabajo, pero la funcionalidad
de cada uno de los botones (Abrir, Guardar, Guardar Como, etc) funciona como si realmente fuera
de un compilador profesional. Pueden hacer uso de el, siempre y cuando no borren los créditos
de los creadores que está en la parte de abajo. Esperamos les sea de utilidad.
*/

/*Created By Yisus FBI and MarissaM*/