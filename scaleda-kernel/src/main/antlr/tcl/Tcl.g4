grammar Tcl;

inicio : (line (';')*)* EOF ;

identificator :  '$' IDENTIFICADOR indice | '${' IDENTIFICADOR '}' indice ;

puts	:	'puts' asignacion  ;
gets	:	'gets' 'stdin' ;
source	:	'source' COMM_STR ;
declaracion	:	'set' IDENTIFICADOR asignacion  ;
func_internal   : gets | source | puts | declaracion;

func_arg  : comm_str | identificator | const | UNKNOW_STR;
func_args : (func_arg)*;
func_call : func_name func_args ;

func_proc   : func_call | func_internal ;

agrup	:	'[' aux_agrup  ;
aux_agrup	:	expr ']' | IDENTIFICADOR param_func ']' | gets ']' | 'array' aux_array  ;
aux_array	:	'size' IDENTIFICADOR ']' | 'exists' IDENTIFICADOR ']'  ;

param_func	:	'{' aux_param ;
aux_param	:	asignacion '}' param_func | expr '}' param_func  ;

asignacion	:	const | '$' identificator | agrup | comm_str | unknown_str  ;

indice	:	'(' val_indice ')' ;
val_indice	:	const | agrup | '$' identificator ;

const	:	CONST_STRING | CONST_INTEGER | CONST_DOUBLE  ;

//line : puts | gets | declaracion | source | func_call | '\n' | '\r\n';
line_empty  : NEWLINE{} ;
line : line_empty | func_proc ;

expr	:	'expr' '{' expresion '}'  ;

expresion	:	exp_or  ;
exp_or	:	exp_or '||' exp_and | exp_and  ;
exp_and	:	exp_and '&&' exp_ig | exp_ig  ;
exp_ig	:	exp_ig op_ig exp_rel | exp_rel  ;
exp_rel	:	exp_rel op_rel exp_add | exp_add  ;
exp_add	:	exp_add op_add exp_mul | exp_mul  ;
exp_mul	:	exp_mul op_mul exp_pot | exp_pot  ;
exp_pot	:	exp_pot '**' exp_una | exp_una  ;
exp_una	:	op_una exp_una | term  ;
term	:	'$' identificator | agrup | const | '(' exp_or ')'  ;

op_ig	:	'eq' | '==' | 'ne' | '!='  ;
op_rel	:	'>' | '<' | '>=' | '<='  ;
op_add	:	'+' | '-'  ;
op_mul	:	'*' | '/' | '%'  ;
op_una	:	'-' | '!'  ;

CONST_INTEGER	:	'-'?[0-9]+  ;
CONST_DOUBLE	:	[0-9]+ '.' [0-9]+  ;
CONST_STRING	:	'"' ~[\r\n"]* '"'  ;

comm_str    : COMM_STR | IDENTIFICADOR;
unknown_str : COMM_STR | IDENTIFICADOR | UNKNOW_STR;
func_name   : IDENTIFICADOR ;

IDENTIFICADOR	:	[a-zA-Z_][a-zA-Z0-9_]*  ;
COMM_STR        :   [a-zA-Z0-9_.\-]+ ;

UNKNOW_STR      :   ~[ \t\r\n;]+ ;

NEWLINE : [\r?\n]+ ;
WS	:	[ \t\r\n]+	->	channel (HIDDEN)  ;
COMMENT	:	'#' ~[\r\n]*	->	channel (HIDDEN)  ;
COMMENT_INLINE	:	';' ~[\r\n]*	->	channel (HIDDEN)  ;

BAD_CHAR    : . ;