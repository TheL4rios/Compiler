package codigo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import static codigo.Metodos.multStr;
import static codigo.Metodos.centrarPal;

/**
 *
 * @author jesus
 */
public class Gramatica {

    private ArrayList<Produccion> producciones;
    private final ArrayList<ErrorLSSL> errores;
    private boolean lineaColumnaIni, compIntManual, indiceCompInt, mostrarM, validacion;
    private int contProd;
    public static boolean MENSAJE_POR_DEFECTO = true,
            SIN_MENSAJE = false;

    public Gramatica(ArrayList<Token> tokens, ArrayList<ErrorLSSL> errores) {
        producciones = new ArrayList();
        contProd = 0;
        lineaColumnaIni = validacion = true;
        compIntManual = indiceCompInt = false;
        mostrarM = true;
        tokens.forEach((tk) -> {
            producciones.add(new Produccion(tk));
        });
        this.errores = errores;
        String version = " Gramatica v0.15 (By Yisus FBI and M45t3r L3g10n) ",
                desc = "Gramática generada con éxito, se crearon " + producciones.size() + " producciones",
                desc2 = "Todos los componentes están listos para su ejecución";
        int tamaño = desc.length() + 6;

        System.out.println("\n" + Metodos.ANSI_GREEN_BLACK + multStr("-", tamaño));
        System.out.println(Metodos.ANSI_GREEN_BLACK + "| " + centrarPal(desc, " ", " ", tamaño - 4) + " |");
        System.out.println(Metodos.ANSI_GREEN_BLACK + "| " + centrarPal(desc2, " ", " ", tamaño - 4) + " |");
        System.out.println(Metodos.ANSI_GREEN_BLACK + centrarPal(version, "-", "-", tamaño) + Metodos.ANSI_RESET + "\n");
    }

    //Método para  mostrar la linea y columna inicial de la producción indicada    
    public void lineaColumaIni() {
        lineaColumnaIni = true;
    }

    //Método para  mostrar la linea y columna final de la producción indicada 
    public void lineaColumnaFin() {
        lineaColumnaIni = false;
    }

    private String[] eliminarCompRepNum(String[] componentes) {
        ArrayList<String> componentesArray = new ArrayList();
        componentesArray.addAll(Arrays.asList(componentes));
        for (int i = 0; i < componentesArray.size(); i++) {
            if (componentesArray.get(i).matches("[0-9]+")) {
                componentesArray.remove(i);
                i = i - 1;
            }
        }
        for (int i = 0; i < componentesArray.size(); i++) {
            for (int j = 0; j < componentesArray.size(); j++) {
                if (i != j) {
                    if (componentesArray.get(i).equals(componentesArray.get(j))) {
                        componentesArray.remove(j);
                        j = j - 1;
                    }
                }
            }
        }
        if (componentesArray.size() != componentes.length) {
            String[] componentesT = new String[componentesArray.size()];
            for (int i = 0; i < componentesT.length; i++) {
                componentesT[i] = componentesArray.get(i);
            }

            return componentesT;
        } else {
            return componentes;
        }
    }

    private String[] separarComp(String expReg) {
        if (expReg == null) {
            return null;
        }
        return eliminarCompRepNum(expReg.replaceAll("[^0-9A-Za-zÑñÁÉÍÓÚáéíóúÜü_ ]", " ").trim().replaceAll(" +", " ").split(" "));
    }

    /*La creación de este método requirió un amplio conocimiento de manejo de índices, así como una investigación acerca 
      de los parámetros como referencia y valor. Agrupa las producciones, creando una nueva producción. Recibe como 
      referencia el nombre de la nueva producción, la expresión regular, los componentes de la expresión regular, el
      número de error, el tipo de error, el mensaje de error y el ArrayList de producciones al que desea agregar
        .
     */
    public void agrupar(String nombreP, String expReg, String[] componentes, int numeroError, String mensajeError, int indiceC, ArrayList<Produccion> produccion) {
        if (mostrarM) {
            System.out.println(".................................................................................................................."
                    + "..................................................................................................................");//Impresión al iniciar de una agrupación
        }
        contProd += 1;
        if (validarAgr(nombreP, expReg, componentes, indiceC)) {
            expReg = expReg.replace(" ", "");
            ArrayList<Produccion> produccionesT = new ArrayList();
            int tamaño = producciones.size(), agrs = 0;
            if (indiceCompInt && indiceC < 0) {
                indiceC = indiceC + componentes.length;
            }

            for (int i = 0; i < tamaño; i++) {
                Produccion prodAct = producciones.get(i);
                if (!prodAct.nombreIgualA(componentes)) {
                    produccionesT.add(prodAct);
                    continue;
                }/*Si no forma parte de los componentes, se agrega la producción y 
                   se dirige hacia el final del for*/
                int indiceAct = -1;
                String strConcat = "";
                for (int j = i; j < tamaño; j++) {
                    prodAct = producciones.get(j);
                    if (!prodAct.nombreIgualA(componentes)) {
                        if (indiceAct != -1) {
                            Produccion pt = new Produccion(), ptIndiceC = new Produccion();
                            for (int k = i; k <= indiceAct; k++) {
                                Produccion ptAct = producciones.get(k);
                                pt.agregarTokens(ptAct);
                                if (indiceCompInt) {
                                    if (k - i == indiceC) {
                                        ptIndiceC = ptAct;
                                    }
                                }
                            }//Crear una sola producción para agregarla
                            pt.setNombre(nombreP);
                            produccionesT.add(pt);
                            if (produccion != null) {
                                produccion.add(pt);
                            }
                            if (mensajeError != null) {
                                if (indiceCompInt) {
                                    errores.add(new ErrorLSSL(numeroError, mensajeError, ptIndiceC, lineaColumnaIni));
                                } else {
                                    errores.add(new ErrorLSSL(numeroError, mensajeError, pt, lineaColumnaIni));
                                }
                            }
                            i = indiceAct;
                            agrs += 1;
                            break;
                        }//Si el índices es diferente de -1
                        else {
                            Produccion ptAct = producciones.get(i);
                            produccionesT.add(ptAct);
                            break;
                        }//Si el índice es igual a -1
                    }//Si la producción no pertenece a los componentes
                    else {
                        strConcat += prodAct.getNombre();
                        if (strConcat.matches(expReg)) {
                            indiceAct = j;
                        }
                        if (j == tamaño - 1) {
                            if (indiceAct != -1) {
                                Produccion pt = new Produccion(), ptIndiceC = new Produccion();
                                for (int k = i; k <= indiceAct; k++) {
                                    Produccion ptAct = producciones.get(k);
                                    pt.agregarTokens(ptAct);
                                    if (indiceCompInt) {
                                        if (k - i == indiceC) {
                                            ptIndiceC = ptAct;
                                        }
                                    }
                                }//Crear una sola producción para agregarla
                                pt.setNombre(nombreP);
                                produccionesT.add(pt);
                                if (produccion != null) {
                                    produccion.add(pt);
                                }
                                if (mensajeError != null) {
                                    if (indiceCompInt) {
                                        errores.add(new ErrorLSSL(numeroError, mensajeError, ptIndiceC, lineaColumnaIni));
                                    } else {
                                        errores.add(new ErrorLSSL(numeroError, mensajeError, pt, lineaColumnaIni));
                                    }
                                }
                                i = indiceAct;
                                agrs += 1;
                            }//Si el índices es diferente de -1
                            else {
                                Produccion ptAct = producciones.get(i);
                                produccionesT.add(ptAct);
                            }//Si el índice es igual a -1 
                        }//Si el índice j es igual a el tamaño del arreglo menos 1
                    }//Si la producción pertence a los componentes
                }// for j
            }//for i

            //Área de impresión de información
            if (mostrarM) {
                if (agrs > 0) {
                    int pTam = producciones.size(), ptTam = produccionesT.size();
                    System.out.println("**** Agrupación " + contProd + " \"" + nombreP + "\" realizada con éxito ****\n"
                            + "Cantidad de componentes: " + componentes.length);
                    if (pTam == ptTam) {
                        System.out.println("No hubo reducción en la cantidad de producciones (" + pTam + ")\n");
                    } else {
                        System.out.println("La cantidad de producciones se redujo de " + pTam + " a " + ptTam + "\n");
                    }
                } else {
                    System.out.println(Metodos.ANSI_BLUE_BLACK + "**** Agrupación " + contProd + " \"" + nombreP + "\" realizada, pero sin cambios ****\n"
                            + Metodos.ANSI_BLUE_BLACK + "Cantidad de componentes: " + componentes.length + "\n");
                }
            }

            producciones = produccionesT;
        }
    }

    private boolean validarAgr(String nombreP, String expReg, String[] componentes, int indiceC) {
        if (!validacion) {
            return true;
        }
        int ers = 0, ersExpComp = 0;
        String cadErrores = "";
        if (nombreP == null) {
            ers += 1;
            cadErrores += "\n" + Metodos.ANSI_RED_BLACK + ers + ". El nombre de la producción es un valor nulo, proceda a corregirla o eliminarla";
        } else if (nombreP.contains(" ")) {
            ers += 1;
            cadErrores += "\n" + Metodos.ANSI_RED_BLACK + ers + ". El nombre de la producción contiene uno o más espacios, proceda a eliminarlos:\n"
                    + "\"" + nombreP + "\"";
        } else if (nombreP.equals("")) {
            ers += 1;
            cadErrores += "\n" + Metodos.ANSI_RED_BLACK + ers + ". El nombre de la producción es una cadena vacía, proceda a corregirla o eliminarla";
        } else if (nombreP.matches("[0-9]+")) {
            ers += 1;
            cadErrores += "\n" + Metodos.ANSI_RED_BLACK + ers + ". El nombre de la producción no puede ser un número, proceda a corregirla o eliminarla:\n"
                    + "\"" + nombreP + "\"";
        } else if (!nombreP.matches("[A-Za-zÑñÁÉÍÓÚáéíóúÜü_][0-9A-Za-zÑñÁÉÍÓÚáéíóúÜü_]*")) {
            ers += 1;
            cadErrores += "\n" + Metodos.ANSI_RED_BLACK + ers + ". El nombre de la producción es inválido, debe de cumplir con la siguiente ER: [A-Za-z_][A-Za-z0-9_]*, proceda a corregirla o eliminarla:\n"
                    + "\"" + nombreP + "\"";
        }
        if (expReg == null) {
            ers += 1;
            ersExpComp = -1;
            cadErrores += "\n" + Metodos.ANSI_RED_BLACK + ers + ". La expresión regular de la producción es un valor nulo, proceda a corregirla o eliminarla";
        } else if (expReg.matches(" *")) {
            ers += 1;
            ersExpComp = -1;
            cadErrores += "\n" + Metodos.ANSI_RED_BLACK + ers + ". La expresión regular de la producción no contiene ningún componente ó expresión, proceda a corregirla o eliminarla";
        } else
            try {
            Pattern.compile(expReg);
        } catch (Exception e) {
            ers += 1;
            ersExpComp = -1;
            cadErrores += "\n" + Metodos.ANSI_RED_BLACK + ers + ". Formato de expresión regular inválido: " + e.getLocalizedMessage();
        }
        if (compIntManual) {
            for (String componente : componentes) {
                if (componente.contains(" ")) {
                    ers += 1;
                    ersExpComp = -1;
                    cadErrores += "\n" + Metodos.ANSI_RED_BLACK + ers + ". Uno de los componentes introducidos tiene uno o más espacios, favor de corregirlo o eliminarlo:\n"
                            + "\"" + componente + "\"";
                    break;
                }
            }
        }
        if (ersExpComp != -1) {
            for (String componente : componentes) {
                if (!componente.matches("[A-Za-zÑñÁÉÍÓÚáéíóúÜü_][0-9A-Za-zÑñÁÉÍÓÚáéíóúÜü_]*")) {
                    ers += 1;
                    ersExpComp = -1;
                    cadErrores += "\n" + Metodos.ANSI_RED_BLACK + ers + ". Uno de los componentes no cumple con la siguiente ER: [A-Za-z_][A-Za-z0-9_]*, favor de corregirlo o eliminarlo:\n"
                            + "\"" + componente + "\"";
                    break;
                }
            }
        }
        if (indiceCompInt && ersExpComp != -1) {
            if (expReg != null && !expReg.matches("[0-9A-Za-zÑñÁÉÍÓÚáéíóúÜü_ ]+")) {
                ers += 1;
                ersExpComp = -1;
                cadErrores += "\n" + Metodos.ANSI_RED_BLACK + ers + ". En caso de querer la línea y columna de un componente en específico, no debe de mandar una expresión\n"
                        + Metodos.ANSI_RED_BLACK + multStr(" ", String.valueOf(ers).length()) + "  regular como parámetro, si no solo los componentes en un orden determinado, proceda a corregirla:\n"
                        + "\"" + expReg + "\"";
            }
            if (ersExpComp != -1) {
                int tam = componentes.length;
                if (indiceC >= 0) {
                    if (indiceC > tam - 1) {
                        ers += 1;
                        cadErrores += "\n" + Metodos.ANSI_RED_BLACK + ers + ". El índice excede al tamaño de la cantidad de componentes: " + indiceC + ">" + (tam - 1);
                    }
                } else if (indiceC < -tam) {
                    ers += 1;
                    cadErrores += "\n" + Metodos.ANSI_RED_BLACK + ers + ". El índice negativo es inferior al tamaño de la cantidad de componentes: " + indiceC + "<-" + tam;
                }
            }
        }
        if (ers > 0) {
            System.out.println(Metodos.ANSI_RED_BLACK + "**** Agrupación " + contProd + " \"" + nombreP + "\" no realizada****\n"
                    + Metodos.ANSI_RED_BLACK + "Δ Resuelva los errores a continuación descritos Δ:"
                    + cadErrores + "\n");
            return false;
        }

        return true;
    }

    public void agrupar(String nombreP, String expReg, int numeroError, String mensajeError) {
        agrupar(nombreP, expReg, separarComp(expReg), numeroError, mensajeError, -1, null);
    }

    public void agrupar(String nombreP, String expReg, int numeroError, String mensajeError, int indiceC) {
        indiceCompInt = true;
        agrupar(nombreP, expReg, separarComp(expReg), numeroError, mensajeError, indiceC, null);
        indiceCompInt = false;
    }

    public void agrupar(String nombreP, String expReg) {
        agrupar(nombreP, expReg, separarComp(expReg), -1, null, -1, null);
    }

    public void agrupar(String nombreP, String expReg, String[] componentes) {
        compIntManual = true;
        agrupar(nombreP, expReg, componentes, -1, null, -1, null);
        compIntManual = false;
    }

    public void agrupar(String nombreP, String expReg, int numeroError, String mensajeError, ArrayList<Produccion> produccion) {
        agrupar(nombreP, expReg, separarComp(expReg), numeroError, mensajeError, -1, produccion);
    }

    public void agrupar(String nombreP, String expReg, int numeroError, String mensajeError, int indiceC, ArrayList<Produccion> produccion) {
        indiceCompInt = true;
        agrupar(nombreP, expReg, separarComp(expReg), numeroError, mensajeError, indiceC, produccion);
        indiceCompInt = false;
    }

    public void agrupar(String nombreP, String expReg, ArrayList<Produccion> produccion) {
        agrupar(nombreP, expReg, separarComp(expReg), -1, null, -1, produccion);
    }

    public void agrupar(String nombreP, String expReg, String[] componentes, ArrayList<Produccion> produccion) {
        compIntManual = true;
        agrupar(nombreP, expReg, componentes, -1, null, -1, produccion);
        compIntManual = false;
    }

    /*Método modificado para poder hacer referencia a al índice de un determinado componente en la expresión regular (la cual,
      no deberá ser una expresión regular como tal, sino el conjunto de componentes en un determinado orden
     */
    //********************
    public void eliminar(String nombreP, int numeroError, String mensajeError) {
        if (mostrarM) {
            System.out.println(".................................................................................................................."
                    + "..................................................................................................................");//Impresión al eliminar una producción
        }
        if (validarEliminacion(nombreP)) {
            int eliminados = 0, cantProdOr = producciones.size();

            for (int i = 0; i < producciones.size(); i++) {
                Produccion prodAct = producciones.get(i);
                if (prodAct.nombreIgualA(nombreP)) {
                    if (mensajeError != null) {
                        errores.add(new ErrorLSSL(numeroError, mensajeError, prodAct, lineaColumnaIni));
                    }
                    producciones.remove(i);
                    i = i - 1;
                    eliminados += 1;
                }
            }
            if (mostrarM) {
                if (eliminados > 0) {
                    System.out.println("**** Se realizaron " + eliminados + " eliminaciones de producciones llamadas \"" + nombreP + "\" ****\n"
                            + "La cantidad de producciones se redujo de " + cantProdOr + " a " + producciones.size() + "\n");
                } else {
                    System.out.println(Metodos.ANSI_BLUE_BLACK + "No se encontró alguna producción llamada \"" + nombreP + "\" para su eliminación\n");
                }
            }
        }
    }

    public void eliminar(String nombreP, int numeroError) {
        if (mostrarM) {
            System.out.println(".................................................................................................................."
                    + "..................................................................................................................");//Impresión al eliminar una producción
        }
        if (validarEliminacion(nombreP)) {
            int eliminados = 0, cantProdOr = producciones.size();

            for (int i = 0; i < producciones.size(); i++) {
                Produccion prodAct = producciones.get(i);
                if (prodAct.nombreIgualA(nombreP)) {
                    if (prodAct.getTamTokens() > 1) {
                        errores.add(new ErrorLSSL(numeroError, " × Error sintáctico {}: error de sintaxis sobre los tokens \"[]\", eliminar estos tokens [#, %]",
                                prodAct, lineaColumnaIni
                        ));
                    } else {
                        errores.add(new ErrorLSSL(numeroError, " × Error sintáctico {}: error de sintaxis sobre el token \"[]\", eliminar este token [#, %]",
                                prodAct, lineaColumnaIni
                        ));
                    }
                    producciones.remove(i);
                    i = i - 1;
                    eliminados += 1;
                }
            }
            if (mostrarM) {
                if (eliminados > 0) {
                    System.out.println("**** Se realizaron " + eliminados + " eliminaciones de producciones llamadas \"" + nombreP + "\" ****\n"
                            + "La cantidad de producciones se redujo de " + cantProdOr + " a " + producciones.size() + "\n");
                } else {
                    System.out.println(Metodos.ANSI_BLUE_BLACK + "No se encontró alguna producción llamada \"" + nombreP + "\" para su eliminación\n");
                }
            }
        }
    }

    public void eliminar(String nombreP) {
        eliminar(nombreP, -1, null);
    }

    private boolean validarEliminacion(String nombreP) {
        if (!validacion) {
            return true;
        }
        int ers = 0;
        String cadErrores = "";
        if (nombreP == null) {
            ers += 1;
            cadErrores += "\n" + Metodos.ANSI_RED_BLACK + "1. El nombre de la producción a eliminar es un valor nulo, proceda a corregirla";
        } else if (nombreP.contains(" ")) {
            ers += 1;
            cadErrores += "\n" + Metodos.ANSI_RED_BLACK + "1. El nombre de la producción a eliminar contiene uno o más espacios, proceda a corregirla:\n"
                    + "\"" + nombreP + "\"";
        } else if (nombreP.equals("")) {
            ers += 1;
            cadErrores += "\n" + Metodos.ANSI_RED_BLACK + "1. El nombre de la producción a eliminar es una cadena vacía, proceda a corregirla";
        } else if (nombreP.matches("[0-9]+")) {
            ers += 1;
            cadErrores += "\n" + Metodos.ANSI_RED_BLACK + ers + ". El nombre de la producción a eliminar no puede ser un número, proceda a corregirla:\n"
                    + "\"" + nombreP + "\"";
        } else if (!nombreP.matches("[A-Za-zÑñÁÉÍÓÚáéíóúÜü_][0-9A-Za-zÑñÁÉÍÓÚáéíóúÜü_]*")) {
            ers += 1;
            cadErrores += "\n" + Metodos.ANSI_RED_BLACK + ers + ". El nombre de la producción a eliminar es inválido, debe de cumplir con la siguiente ER: [A-Za-z_][A-Za-z0-9_]*, proceda a corregirla:\n"
                    + "\"" + nombreP + "\"";
        }
        if (ers > 0) {
            System.out.println(Metodos.ANSI_RED_BLACK + "**** Eliminación de la producción \"" + nombreP + "\" no realizada****\n"
                    + Metodos.ANSI_RED_BLACK + "Δ Resuelva los errores a continuación descritos Δ:"
                    + cadErrores + "\n");
            return false;
        }

        return true;
    }

    public void mostrar() {
        System.out.println(this);
    }

    public void activarMen() {
        mostrarM = true;
    }

    public void desactivarMen() {
        mostrarM = false;
    }

    public void activarVal() {
        validacion = true;
    }

    public void desactivarVal() {
        validacion = false;
    }

    @Override
    public String toString() {
        System.out.println("\n" + Metodos.ANSI_PURPLE_BLACK + "**** Mostrando gramaticas ****\n");
        String gramaticas = "";
        gramaticas = producciones.stream().map((prod) -> ".................................................................................................................."
                + "..................................................................................................................\n"
                + prod + "\n\n").reduce(gramaticas, String::concat);

        return gramaticas;
    }
}
