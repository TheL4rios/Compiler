package codigo.cuadruplos;

import java.util.ArrayList;

/**
 *
 * @author Larios
 */
public class Pila {
    private ArrayList<Nodo> pila;

    public Pila() {
        this.pila = new ArrayList<>();
    }
    
    public void add(Nodo n) {
        pila.add(n);
    }
    
    public Nodo pop() {
        if (pila.isEmpty()) return null;
        return pila.remove(pila.size() - 1);
    }
    
    public Nodo get() {
        if (pila.isEmpty()) return null;
        return pila.get(pila.size() - 1);
    }
    
    public boolean isEmpty() {
        return pila.isEmpty();
    }
    
    public void clear() {
        pila.clear();
    }
}
