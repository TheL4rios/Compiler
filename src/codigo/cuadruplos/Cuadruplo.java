package codigo.cuadruplos;

/**
 *
 * @author Larios
 */

public class Cuadruplo {
    private String op;
    private String arg1;
    private String arg2;
    private String r;
    
    public Cuadruplo(String op, String arg1, String arg2, String r) {
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.r = r;
    }

    public String getOp() {
        return op;
    }

    public String getArg1() {
        return arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public String getR() {
        return r;
    }
    
    public int getNParametros() {
        int c = 0;
        if (!this.op.isEmpty()) {
            c++;
        }
        
        if (!this.arg1.isEmpty()) {
            c++;
        }
        
        if (!this.arg2.isEmpty()) {
            c++;
        }
        
        if (!this.r.isEmpty()) {
            c++;
        }
        return c;
    }
    
    @Override
    public String toString() {
        return "Cuadruplo(" + this.op + ", " + this.arg1 + ", " + this.arg2 + ", " + this.r + ")";
    }
}
