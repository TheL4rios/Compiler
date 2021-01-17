package codigo;
import java.util.ArrayList;
%%
%class Lexer
%type Tokens
%line
%column

L = [A-Za-zÑñ_ÁÉÍÓÚáéíóúÜü]
espacio = [ \t]
saltL = [\n\r]
D = [0-9]
coment = ([^*]|\*+[^/])*\**
docComent = \/\*{coment}\*\/
docComentI = \/\*{coment}
%{
    public ArrayList<Token> tokens = Compilador.tokens;
    
%}
%%
/* Comentarios */
{docComent} {/*ignore*/}
{docComentI} {tokens.add(new Token(null, "coment_inc", yyline, yycolumn));}
--.* {/*ignore*/}

/* Tipos de datos */
grado {tokens.add(new Token(yytext(), "tipoDato_grado", yyline, yycolumn));}
num {tokens.add(new Token(yytext(), "tipoDato_num", yyline, yycolumn));}
bool {tokens.add(new Token(yytext(), "tipoDato_bool", yyline, yycolumn));}
sprox {tokens.add(new Token(yytext(), "tipoDato_sprox", yyline, yycolumn));}
servo {tokens.add(new Token(yytext(), "tipoDato_servo", yyline, yycolumn));}

/* Variables estáticas */
SERVO_1 {tokens.add(new Token(yytext(), "vest_servo1", yyline, yycolumn));}
SERVO_2 {tokens.add(new Token(yytext(), "vest_servo2", yyline, yycolumn));}
SERVO_3 {tokens.add(new Token(yytext(), "vest_servo3", yyline, yycolumn));}
SERVO_4 {tokens.add(new Token(yytext(), "vest_servo4", yyline, yycolumn));}
SPROX_NORTE {tokens.add(new Token(yytext(), "vest_sproxN", yyline, yycolumn));}
SPROX_SUR {tokens.add(new Token(yytext(), "vest_sproxS", yyline, yycolumn));}
SPROX_OESTE {tokens.add(new Token(yytext(), "vest_sproxO", yyline, yycolumn));}
SPROX_ESTE {tokens.add(new Token(yytext(), "vest_sproxE", yyline, yycolumn));}
CENTIM {tokens.add(new Token(yytext(), "vest_centim", yyline, yycolumn));}
METRO {tokens.add(new Token(yytext(), "vest_metro", yyline, yycolumn));}
PIE {tokens.add(new Token(yytext(), "vest_pie", yyline, yycolumn));}
VERDAD {tokens.add(new Token(yytext(), "vest_verdad", yyline, yycolumn));}
FALSO {tokens.add(new Token(yytext(), "vest_falso", yyline, yycolumn));}

/* Funciones */
girar {tokens.add(new Token(yytext(), "fun_girar", yyline, yycolumn));}
dist {tokens.add(new Token(yytext(), "fun_dist", yyline, yycolumn));}
agarrar {tokens.add(new Token(yytext(), "fun_agarrar", yyline, yycolumn));}
soltar {tokens.add(new Token(yytext(), "fun_soltar", yyline, yycolumn));}
reset {tokens.add(new Token(yytext(), "fun_reset", yyline, yycolumn));}

/* Estructuras de control */
ciclo {tokens.add(new Token(yytext(), "estCont_ciclo", yyline, yycolumn));}
bucle {tokens.add(new Token(yytext(), "estCont_bucle", yyline, yycolumn));}

/* Bloques de sentencias */
ident {tokens.add(new Token(yytext(), "bloque_ident", yyline, yycolumn));}
cuerpo {tokens.add(new Token(yytext(), "bloque_cuerpo", yyline, yycolumn));}

/* Identificadores */
{D}+{L}+({D}|{L})* {tokens.add(new Token(yytext(), "ident", yyline, yycolumn));}
{L}({L}|{D})* {tokens.add(new Token(yytext(), "ident", yyline, yycolumn));}

/* Números */
({D}+|{D}+\.{D}+) {tokens.add(new Token(yytext(), "numero", yyline, yycolumn));}

/* grados */
{D}+° {tokens.add(new Token(yytext(), "grado", yyline, yycolumn));}

/* Operadores de agrupación */
\{ {tokens.add(new Token(yytext(), "llave_a", yyline, yycolumn));}
\} {tokens.add(new Token(yytext(), "llave_c", yyline, yycolumn));}
\( {tokens.add(new Token(yytext(), "parent_a", yyline, yycolumn));}
\) {tokens.add(new Token(yytext(), "parent_c", yyline, yycolumn));}

/* Operadores relaciones y de asignación */
\< {tokens.add(new Token(yytext(), "opRel_may", yyline, yycolumn));}
\> {tokens.add(new Token(yytext(), "opRel_men", yyline, yycolumn));}
\<= {tokens.add(new Token(yytext(), "opRel_mayIg", yyline, yycolumn));}
\>= {tokens.add(new Token(yytext(), "opRel_menIg", yyline, yycolumn));}
== {tokens.add(new Token(yytext(), "opRel_ig", yyline, yycolumn));}
= {tokens.add(new Token(yytext(), "opAsig", yyline, yycolumn));}

/* Operadores aritméticos */
- {tokens.add(new Token(yytext(), "opArt_resta", yyline, yycolumn));}
\+ {tokens.add(new Token(yytext(), "opArt_suma", yyline, yycolumn));}
\* {tokens.add(new Token(yytext(), "opArt_mult", yyline, yycolumn));}
\/ {tokens.add(new Token(yytext(), "opArt_div", yyline, yycolumn));}

/* Signos de puntuación */
; {tokens.add(new Token(yytext(), "punto_coma", yyline, yycolumn));}
, {tokens.add(new Token(yytext(), "coma", yyline, yycolumn));}

{espacio} {/*Ignore*/}
{saltL} {/*Ignore*/}
. {tokens.add(new Token(yytext(), "SYMBOL", yyline, yycolumn));}