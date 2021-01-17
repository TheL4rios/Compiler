package codigo;

import java.awt.Color;
/**
 * @author Yisus FBI
 */
public class TextoColor {
    private final int inicio, tamaño;
    private final Color color;

    public TextoColor(int inicio, int tamaño, Color color) {
        this.inicio = inicio;
        this.tamaño = tamaño;
        this.color = color;
    }

    public int getInicio() {
        return inicio;
    }

    public int getTamaño() {
        return tamaño;
    }

    public Color getColor() {
        return color;
    }
    
    @Override
    public String toString(){
        return "("+inicio+","+tamaño+") --> "+color;
    }
}
/*
Clase creada por Yisus FBI para los métodos de color. Favor de poner los créditos de
 la parte de abajo en caso de que le sea de utilidad. Gracias.
*/
/*Created by Yisus FBI*/
