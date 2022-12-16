lexer grammar VerilogLexer;

K_module : 'module' ;
K_macromodule : 'macromodule' ;

K_endmodule : 'endmodule' ;
K_B0 : 'B0' ;
K_B1 : 'B1' ;
K_PATHPULSE : 'PATHPULSE' ;
K_always : 'always' ;
K_and : 'and' ;
K_assign : 'assign' ;
K_automatic : 'automatic' ;
K_b0 : 'b0' ;
K_b1 : 'b1' ;
K_begin : 'begin' ;
K_buf : 'buf' ;
K_bufif0 : 'bufif0' ;
K_bufif1 : 'bufif1' ;
K_case : 'case' ;
K_casex : 'casex' ;
K_casez : 'casez' ;
K_config : 'config' ;
K_cell : 'cell' ;
K_cmos : 'cmos' ;
K_deassign : 'deassign' ;
K_default : 'default' ;
K_default_nettype : 'default_nettype' ;
K_defparam : 'defparam' ;
K_design : 'design' ;
K_disable : 'disable' ;
K_edge : 'edge' ;
K_else : 'else' ;
K_end : 'end' ;
K_endcase : 'endcase' ;
K_endconfig : 'endconfig' ;
K_endfunction : 'endfunction' ;
K_endgenerate : 'endgenerate' ;
K_endprimitive : 'endprimitive' ;
K_endspecify : 'endspecify' ;
K_endtable : 'endtable' ;
K_endtask : 'endtask' ;
K_event : 'event' ;
K_for : 'for' ;
K_force : 'force' ;
K_forever : 'forever' ;
K_fork : 'fork' ;
K_highz0 : 'highz0' ;
K_highz1 : 'highz1' ;
K_function : 'function' ;
K_generate : 'generate' ;
K_genvar : 'genvar' ;
K_if : 'if' ;
K_ifnone : 'ifnone' ;
K_include : 'include' ;
K_initial : 'initial' ;
K_inout : 'inout' ;
K_input : 'input' ;
K_instance : 'instance' ;
K_integer : 'integer' ;
K_join : 'join' ;
K_large : 'large' ;
K_liblist : 'liblist' ;
K_library : 'library' ;
K_localparam : 'localparam' ;
K_medium : 'medium' ;
K_nand : 'nand' ;
K_negedge : 'negedge' ;
K_nmos : 'nmos' ;
K_nor : 'nor' ;
K_noshowcancelled : 'noshowcancelled' ;
K_not : 'not' ;
K_notif0 : 'notif0' ;
K_notif1 : 'notif1' ;
K_or : 'or' ;
K_output : 'output' ;
K_parameter : 'parameter' ;
K_pmos : 'pmos' ;
K_posedge : 'posedge' ;
K_primitive : 'primitive' ;
K_pull0 : 'pull0' ;
K_pull1 : 'pull1' ;
K_pulldown : 'pulldown' ;
K_pullup : 'pullup' ;
K_pulsestyle_ondetect : 'pulsestyle_ondetect' ;
K_pulsestyle_onevent : 'pulsestyle_onevent' ;
K_rcmos : 'rcmos' ;
K_real : 'real' ;
K_realtime : 'realtime' ;
K_reg : 'reg' ;
K_release : 'release' ;
K_repeat : 'repeat' ;
K_rnmos : 'rnmos' ;
K_rpmos : 'rpmos' ;
K_rtran : 'rtran' ;
K_rtranif0 : 'rtranif0' ;
K_rtranif1 : 'rtranif1' ;
K_scalared : 'scalared' ;
K_showcancelled : 'showcancelled' ;
K_signed : 'signed' ;
K_small : 'small' ;
K_specify : 'specify' ;
K_specparam : 'specparam' ;
K_strong0 : 'strong0' ;
K_strong1 : 'strong1' ;
K_supply0 : 'supply0' ;
K_supply1 : 'supply1' ;
K_table : 'table' ;
K_task : 'task' ;
K_time : 'time' ;
K_timescale : 'timescale' ;
K_tran : 'tran' ;
K_tranif0 : 'tranif0' ;
K_tranif1 : 'tranif1' ;
K_tri : 'tri' ;
K_tri0 : 'tri0' ;
K_tri1 : 'tri1' ;
K_triand : 'triand' ;
K_trior : 'trior' ;
K_trireg : 'trireg' ;
K_use : 'use' ;
K_vectored : 'vectored' ;
K_wait : 'wait' ;
K_wand : 'wand' ;
K_weak0 : 'weak0' ;
K_weak1 : 'weak1' ;
K_while : 'while' ;
K_wire : 'wire' ;
K_wor : 'wor' ;
K_xnor : 'xnor' ;
K_xor : 'xor' ;


Semicolon : ';' ;
Colon : ':' ;
Dollar : '$' ;
Dot : '.' ;
Plus : '+' ;
Minus : '-' ;
Comma : ',' ;
Question_mark : '?' ;
Exclamation_mark : '!' ;
Left_parenthes : '(' ;
Right_parenthes : ')' ;
Left_bracket : '[' ;
Right_bracket : ']' ;
Left_brace : '{' ;
Right_brace : '}' ;
Left_angle_bracket : '<' ;
Right_angle_bracket : '>' ;
Sharp : '#' ;
Underscore : '_' ;
Slash : '/' ;
Backslash : '\\' ;
Eq : '=' ;
Eq_eq : '==' ;
Eq_eq_eq : '===' ;
Not_eq : '!=' ;
Not_eq_eq : '!==' ;
Left_angle_eq : '<=' ;
Right_angle_eq : '=>' ;
At : '@' ;
Asterisk : '*' ;
Left_arrow : '<-' ;
Right_arrow : '->' ;
Left_asterisk_arrow : '<*' ;
Right_asterisk_arrow : '*>' ;
Percent : '%' ;
Ampersant : '&' ;
Vertical_line : '|' ;
Apostrophe : '`' ;
Tilda : '~' ;
Hat : '^' ;
Quotation_mark : '"' ;


Real_number
   : Unsigned_number Dot Unsigned_number | Unsigned_number (Dot Unsigned_number)? [eE] ([+-])? Unsigned_number
   ;

Decimal_number
   : Unsigned_number | (Size)? Decimal_base Unsigned_number | (Size)? Decimal_base X_digit (Underscore)* | (Size)? Decimal_base Z_digit (Underscore)*
   ;

Binary_number
   : (Size)? Binary_base Binary_value
   ;

Octal_number
   : (Size)? Octal_base Octal_value
   ;

Hex_number
   : (Size)? Hex_base Hex_value
   ;


fragment Sign
   : [+-]
   ;


fragment Size
   : Non_zero_unsigned_number
   ;


fragment Non_zero_unsigned_number
   : Non_zero_decimal_digit (Underscore | Decimal_digit)*
   ;


fragment Unsigned_number
   : Decimal_digit (Underscore | Decimal_digit)*
   ;


fragment Binary_value
   : Binary_digit (Underscore | Binary_digit)*
   ;


fragment Octal_value
   : Octal_digit (Underscore | Octal_digit)*
   ;


fragment Hex_value
   : Hex_digit (Underscore | Hex_digit)*
   ;


fragment Decimal_base
   : '\'' [sS]? [dD]
   ;


fragment Binary_base
   : '\'' [sS]? [bB]
   ;


fragment Octal_base
   : '\'' [sS]? [oO]
   ;


fragment Hex_base
   : '\'' [sS]? [hH]
   ;


fragment Non_zero_decimal_digit
   : [1-9]
   ;


fragment Decimal_digit
   : [0-9]
   ;


fragment Binary_digit
   : X_digit | Z_digit | [01]
   ;


fragment Octal_digit
   : X_digit | Z_digit | [0-7]
   ;


fragment Hex_digit
   : X_digit | Z_digit | [0-9a-fA-F]
   ;


fragment X_digit
   : [xX]
   ;


fragment Z_digit
   : [zZ?]
   ;


Escaped_identifier
   : '\\' ('\u0021'..'\u007E')+ ~ [ \r\t\n]*
   ;

Simple_identifier
   : [a-zA-Z_] [a-zA-Z0-9_$]*
   ;

Dollar_identifier
   : Dollar [a-zA-Z0-9_$] [a-zA-Z0-9_$]*
   ;

Time_identifier
   : [0-9] + [mnpf] 's'
   ;

Filepath
   : '"' (~ [\n\r])+ '"'
   ;


String
   : '"' (~ [\n\r])* '"'
   ;


One_line_comment
   : '//' .*? '\r'? '\n' -> channel (HIDDEN)
   ;

Block_comment
   : '/*' .*? '*/' -> channel (HIDDEN)
   ;


White_space
   : [ \t\n\r] + -> channel (HIDDEN)
   ;

Bad_character
   : .
   ;