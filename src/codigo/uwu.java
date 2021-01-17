package codigo;

/**
 *
 * @author jesus
 */
public class uwu {
    public static void main(String omega[]){
        Token token = new Token("awa", "ewe", 5, 5);
        ErrorLSSL error = new ErrorLSSL(3, "\\[[]]Error % # \\% \\# # []sintáctico {},\\[][][\\[]] eliminar []] porfavor #[] \\[]%", token);
        //error = new ErrorLSSL(3, "owo\\[]uwu\\[]", token);
        //error = new ErrorLSSL(3, "\\[][[]] \\[]kl[]", token);
        //error = new ErrorLSSL(3, "\\[][][\\[]]", token);
        System.out.println(error);
        /*String cad = "[[]]";
        boolean uwu = cad.matches("[^\\[]*\\[\\][^\\]]*|[^\\[]*\\[\\][\\]]*|[\\[]*\\[\\][^\\]]*"); 
        System.out.println(uwu);
*/
        
    }
}
