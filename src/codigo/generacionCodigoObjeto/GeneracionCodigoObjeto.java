package codigo.generacionCodigoObjeto;

import codigo.cuadruplos.Cuadruplo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author Larios
 */
public class GeneracionCodigoObjeto {
    
    private String codigo = ".MODEL SMALL\n"
                          + ".STACK\n"
                          + ".DATA\n";
    
    public GeneracionCodigoObjeto(ArrayList<Cuadruplo> cuadruplos) {
        generar(cuadruplos);
    }
    
    private void generar(ArrayList<Cuadruplo> cuadruplos) {
        setDeclaraciones(cuadruplos);
        
        for (Cuadruplo cuad: cuadruplos) {
            switch(cuad.getNParametros()) {
                case 4: {
                        if (!cuad.getOp().equals("dist"))
                            codigo += "\t\tMOV " + cuad.getR() + ", " + cuad.getArg1() + "\n";
                       if (getOperacion(cuad.getOp()).equals("MUL") || getOperacion(cuad.getOp()).equals("DIV")) {
                           codigo += "\t\tMOV AX" + ", " + cuad.getArg1() + "\n";
                           codigo += "\t\tMOV BX" + ", " + cuad.getArg1() + "\n";
                           codigo += "\t\t" + getOperacion(cuad.getOp()) + " BX" + "\n";
                           codigo += "\t\tMOV " + cuad.getR() + ", AX\n";
                       } else {
                           if (cuad.getOp().equals("dist")) {
                               codigo += "\t\tMOV AX" + ", " + cuad.getArg1() + "\n";
                               codigo += "\t\tMOV BX" + ", " + cuad.getArg1() + "\n";
                               codigo += "\t\tMOV AH, 42h\n";
                               codigo += "\t\tint 21h\n";
                               codigo += "\t\tMOV " + cuad.getR() + ", CX\n";
                           } else {
                                codigo += "\t\t" + getOperacion(cuad.getOp()) + " " + cuad.getR() + ", " + cuad.getArg2() + "\n";
                           }
                    }
                } 
                break;
                case 3: {
                    if (cuad.getOp().equals("=")) {
                        codigo += "\t\tMOV " + cuad.getR() + ", " + 
                                ((cuad.getArg1().contains("°"))?cuad.getArg1().split("°")[0]:cuad.getArg1()) + "\n";
                    } else {
                        codigo += "\t\tMOV AX" + ", " + 
                                ((cuad.getArg1().contains("°"))?cuad.getArg1().split("°")[0]:cuad.getArg1()) + "\n";
                        codigo += "\t\tMOV BX" + ", " + 
                                ((cuad.getArg2().contains("°"))?cuad.getArg2().split("°")[0]:cuad.getArg2()) + "\n";
                        codigo += "\t\tMOV AH, 47h\n";
                        codigo += "\t\tint 21h\n";
                    }
                }
                break;
                default: {
                    switch(cuad.getOp()) {
                        case "reset": {
                            codigo += "\t\tMOV AH, 00h\n";
                            codigo += "\t\tint 21h\n";
                        } break;
                        case "girar": {
                            codigo += "\t\tMOV AH, 47h\n";
                            codigo += "\t\tint 21h\n";
                        } break;
                        case "soltar": {
                            codigo += "\t\tMOV AH, 4Ch\n";
                            codigo += "\t\tint 21h\n";
                        } break;
                    }
                }
            }
        }
        
        codigo += "\tMOV AX, 4C00h\n";
        codigo += "\tint 21h\n";
        codigo += "end";
    }
    
    private String getOperacion(String op) {
        switch(op) {
            case "-": return "SUB";
            case "+": return "ADD";
            case "*": return "MUL";
            case "/": return "DIV";
            case ">=": return "CMP";
            default: return "";
        }
    }
    
    private void setDeclaraciones(ArrayList<Cuadruplo> cuadruplos) {
        HashMap<String, String> variables = new HashMap<>();
        for (Cuadruplo cuad: cuadruplos) {
            if (!cuad.getR().isEmpty() && variables.get(cuad.getR()) == null) {
                variables.put(cuad.getR(), cuad.getR());
            }
        }
        
        for (Entry<String, String> entry : variables.entrySet()) {
            codigo += "\t" + entry.getKey() + " db 0\n";
        }
        codigo += "\tSERVO_1 db 500\n";
        codigo += "\tSERVO_2 db 600\n";
        codigo += "\tSERVO_3 db 700\n";
        codigo += "\tSERVO_4 db 800\n";//SPROX_NORTE
        
        codigo += "\tSPROX_NORTE db 900\n";
        codigo += "\tSPROX_SUR db 1000\n";
        codigo += "\tSPROX_ESTE db 1100\n";
        codigo += "\tSPROX_OESTE db 1200\n";
                
        codigo += ".CODE\n"
                + "\tMOV AX, @data\n" +
                  "\tMOV DS, AX\n\n";
    }
    
    public String getCodigoGenerado() {
        return codigo;
    }
}
