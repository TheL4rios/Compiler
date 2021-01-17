package codigo;
import java.util.ArrayList;
import java.awt.Color;
%%
%class LexerC
%type Tokens
%char
L = [A-Za-zÑñ_ÁÉÍÓÚáéíóúÜü]
espacio = [ \t]
saltL = [\n\r]
D = [0-9]
coment = ([^*]|\*+[^/])*\**
docComent = \/\*{coment}\*\/
docComentI = \/\*{coment}
%{
    public ArrayList<TextoColor> textoC = Compilador.textoC;  
%}
%%
/* Comentarios */
{docComent} {textoC.add(new TextoColor(yychar, yylength(), new Color(146, 146, 146)));}
{docComentI} {textoC.add(new TextoColor(yychar, yylength(), new Color(146, 146, 146)));}
--.* {textoC.add(new TextoColor(yychar, yylength(), new Color(146, 146, 146)));}

/*Tipos de datos */
(grado | num | bool | sprox | servo) {textoC.add(new TextoColor(yychar, yylength(), new Color(148, 58, 173)));}

/* Variables estáticas */
(SERVO_1 | SERVO_2 | SERVO_3 |SERVO_4 |SPROX_NORTE | SPROX_SUR | SPROX_OESTE | SPROX_ESTE | CENTIM | METRO | PIE | VERDAD | FALSO) {textoC.add(new TextoColor(yychar, yylength(), new Color(1, 115, 1)));}

/* Funciones */
(girar | dist | agarrar | soltar | reset) {textoC.add(new TextoColor(yychar, yylength(), new Color(17, 94, 153)));}

/* Estructuras de control */
(ciclo | bucle) {textoC.add(new TextoColor(yychar, yylength(), new Color(212, 129, 6)));}

/* Bloques de sentencias */
(ident | cuerpo) {textoC.add(new TextoColor(yychar, yylength(), new Color(102, 41, 120)));}

/* Identificadores */
{D}+{L}+({D}|{L})* {textoC.add(new TextoColor(yychar, yylength(), new Color(40, 40, 40)));}
{L}({L}|{D})* {textoC.add(new TextoColor(yychar, yylength(), new Color(40, 40, 40)));}

/* Números */
({D}+|{D}+\.{D}+) {textoC.add(new TextoColor(yychar, yylength(), new Color(35, 120, 147)));}

/* grados */
{D}+° {textoC.add(new TextoColor(yychar, yylength(), new Color(150, 0, 80)));}

/* Operadores de agrupación */
(\{ | \} | \( | \)) {textoC.add(new TextoColor(yychar, yylength(), new Color(146, 146, 146)));}

/* Operadores relaciones y de asignación */
(\< | \> | \<= | \>= | == | =) {textoC.add(new TextoColor(yychar, yylength(), new Color(42, 247, 10)));}

/* Operadores aritméticos */
(- | \+ | \* | \/) {textoC.add(new TextoColor(yychar, yylength(), new Color(42, 247, 10)));}

/* Signos de puntuación */
, {textoC.add(new TextoColor(yychar, yylength(), new Color(169, 155, 179)));}
; {textoC.add(new TextoColor(yychar, yylength(), new Color(40, 40, 40)));}

/* Símbolos especiales */
\\ {textoC.add(new TextoColor(yychar, yylength(), new Color(42, 247, 10)));}

{espacio} {/*Ignore*/}
{saltL} {/*Ignore*/}
. {textoC.add(new TextoColor(yychar, yylength(), new Color(255, 0, 0)));}

/*
Esta clase de JFlex fue creada por Yisus FBI para la materia de Lenguajes y Autómatas I, impartida por la docente 
Sonia Alvarado Mares. Hacerla requirió una exhausta investigación de la funcionalidad StyleDocument de un JTextPane.
Pueden hacer uso de ella, siempre y cuando no borren los créditos de los creadores que está en la parte de abajo. 
Esperamos les sea de utilidad.
*/
/*Created By Yisus FBI*/