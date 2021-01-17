package codigo.cuadruplos;

/**
 *
 * @author Larios
 */

public class Nodo {
    private Nodo izquierdo;
    private String valor;
    private Nodo derecho;

    public Nodo(Nodo izquierdo, String valor, Nodo derecho) {
        this.izquierdo = izquierdo;
        this.valor = valor;
        this.derecho = derecho;
    }

    public Nodo(String valor) {
        this.valor = valor;
        this.izquierdo = null;
        this.derecho = null;
    }

    public Nodo getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(Nodo izquierdo) {
        this.izquierdo = izquierdo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Nodo getDerecho() {
        return derecho;
    }

    public void setDerecho(Nodo derecho) {
        this.derecho = derecho;
    }
    
    
}
