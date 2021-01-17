package codigo;
import java_cup.runtime.Symbol; 
%%
%class LexerCup
%type java_cup.runtime.Symbol
%cup
%full
%line
%column

L = [A-Za-zÑñ_\ÁÉÍÓÚáéíóú]
espacio = [ \t]
saltL = [\n\r]
D = [0-9]
rang = [A-Za-z0-9]
exp = ([^\n\$]|\\+\$)*
expC = \${exp}\$
expI = \${exp}
pal = ([^\n\"]|\\+\")*
palC = \"{pal}\"
cad = ([^\n\!]|\\+\!)*
cadC = ¡{cad}\!
cadI = ¡{cad}
coment = ([^*]|\*+[^/])*\**
docComent = \/\*{coment}\*\/
docComentI = \/\*{coment}
%{
    private Symbol symbol(int type, Object value){
        return new Symbol(type, yyline, yycolumn, value);
    }
    private Symbol symbol(int type){
        return new Symbol(type, yyline, yycolumn);
    }
    
%}
%%
{docComent} {/*ignore*/}
{docComentI} {/*ignore*/}
--.* {/*ignore*/}
{cadC} {return new Symbol(sym.cadena, yychar, yyline, yytext());}
{cadI} {return new Symbol(sym.cadena, yychar, yyline, yytext());}
{palC} {return new Symbol(sym.palRes_user, yychar, yyline, yytext());}
{expC} {return new Symbol(sym.expReg, yychar, yyline, yytext());}
{expI} {return new Symbol(sym.expReg, yychar, yyline, yytext());}
Lexico {return new Symbol(sym.palRes_lex, yychar, yyline, yytext());}
Declaracion {return new Symbol(sym.palRes_dec, yychar, yyline, yytext());}
Terminales {return new Symbol(sym.palRes_ter, yychar, yyline, yytext());}
Ignorar {return new Symbol(sym.palRes_ig, yychar, yyline, yytext());}
Sintactico {return new Symbol(sym.palRes_sint, yychar, yyline, yytext());}
NoTerminales {return new Symbol(sym.palRes_nter, yychar, yyline, yytext());}
IniciaCon {return new Symbol(sym.palRes_inic, yychar, yyline, yytext());}
Producciones {return new Symbol(sym.palRes_prod, yychar, yyline, yytext());}
Semantico {return new Symbol(sym.palRes_semant, yychar, yyline, yytext());}
importar {return new Symbol(sym.palRes_imp, yychar, yyline, yytext());}
expreg {return new Symbol(sym.tipoDato_ex, yychar, yyline, yytext());}
alfa {return new Symbol(sym.tipoDato_al, yychar, yyline, yytext());}
palres {return new Symbol(sym.tipoDato_pr, yychar, yyline, yytext());}
cadena {return new Symbol(sym.tipoDato_cd, yychar, yyline, yytext());}
lexerSint {return new Symbol(sym.macro_ls, yychar, yyline, yytext());}
codigo {return new Symbol(sym.macro_c, yychar, yyline, yytext());}
si {return new Symbol(sym.sentCont_si, yychar, yyline, yytext());}
sino {return new Symbol(sym.sentCont_sn, yychar, yyline, yytext());}
nombreC {return new Symbol(sym.funcion_nc, yychar, yyline, yytext());}
tablaTokens {return new Symbol(sym.funcion_tk, yychar, yyline, yytext());}

{rang}\-{rang} {return new Symbol(sym.rango, yychar, yyline, yytext());}
::= {return new Symbol(sym.opAsig_tepr, yychar, yyline, yytext());}
\-> {return new Symbol(sym.opAsig_cad, yychar, yyline, yytext());}
'.'|'\\.' {return new Symbol(sym.caracter, yychar, yyline, yytext());}
:{L}({L}|{D})*: {return new Symbol(sym.Terminal, yychar, yyline, yytext());}
\<{L}({L}|{D})*\> {return new Symbol(sym.produc, yychar, yyline, yytext());}
{D}+{L}+({D}|{L})* {return new Symbol(sym.ident, yychar, yyline, yytext());}
{L}({L}|{D})* {return new Symbol(sym.ident, yychar, yyline, yytext());}
{D}+ {return new Symbol(sym.numero, yychar, yyline, yytext());}
\[ {return new Symbol(sym.corchete_a, yychar, yyline, yytext());}
\] {return new Symbol(sym.corchete_c, yychar, yyline, yytext());}
\{ {return new Symbol(sym.llave_a, yychar, yyline, yytext());}
\} {return new Symbol(sym.llave_c, yychar, yyline, yytext());}
: {return new Symbol(sym.dos_puntos, yychar, yyline, yytext());}
; {return new Symbol(sym.punto_coma, yychar, yyline, yytext());}
\< {return new Symbol(sym.opRel_may, yychar, yyline, yytext());}
\> {return new Symbol(sym.opRel_men, yychar, yyline, yytext());}
== {return new Symbol(sym.opRel_ig, yychar, yyline, yytext());}
\!= {return new Symbol(sym.opRel_dif, yychar, yyline, yytext());}
\! {return new Symbol(sym.sigExcl_c, yychar, yyline, yytext());}
= {return new Symbol(sym.opAsig, yychar, yyline, yytext());}
' {return new Symbol(sym.comi_sim, yychar, yyline, yytext());}
\" {return new Symbol(sym.comi_dob, yychar, yyline, yytext());}
- {return new Symbol(sym.opArt_resta, yychar, yyline, yytext());}
\+ {return new Symbol(sym.opArt_suma, yychar, yyline, yytext());}
\^ {return new Symbol(sym.opArt_pot, yychar, yyline, yytext());}
\* {return new Symbol(sym.opArt_mult, yychar, yyline, yytext());}
\| {return new Symbol(sym.opLog_o, yychar, yyline, yytext());}
& {return new Symbol(sym.opLog_y, yychar, yyline, yytext());}
\( {return new Symbol(sym.opAgr_a, yychar, yyline, yytext());}
\) {return new Symbol(sym.opAgr_c, yychar, yyline, yytext());}
\\ {return new Symbol(sym.barra_inv, yychar, yyline, yytext());}
\/ {return new Symbol(sym.barra, yychar, yyline, yytext());}
, {return new Symbol(sym.coma, yychar, yyline, yytext());}
\. {return new Symbol(sym.punto, yychar, yyline, yytext());}
{espacio} {/*Ignore*/}
{saltL} {/*Ignore*/}
. {return new Symbol(sym.SYMBOL, yychar, yyline, yytext());}