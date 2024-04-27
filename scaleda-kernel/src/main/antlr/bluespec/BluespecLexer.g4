lexer grammar BluespecLexer;

// In general, keywords do not use uppercase letters (the only exception is the keyword valueOf).
// The following are the keywords in BSV (and so they cannot be used as identifiers).

K_Action: 'Action';
K_ActionValue: 'ActionValue';
K_BVI: '"BVI"';
K_C: 'C';
K_CF: 'CF';
K_E: 'E';
K_SB: 'SB';
K_SBR: 'SBR';
K_action: 'action';
K_endaction: 'endaction';
K_actionvalue: 'actionvalue';
K_endactionvalue: 'endactionvalue';
K_ancestor: 'ancestor';
K_begin: 'begin';
K_bit: 'bit';
K_case: 'case';
K_endcase: 'endcase';
K_clocked_by: 'clocked_by';
K_default: 'default';
K_default_clock: 'default_clock';
K_default_reset: 'default_reset';
K_dependencies: 'dependencies';
K_deriving: 'deriving';
K_determines: 'determines';
K_e: 'e';
K_else: 'else';
K_enable: 'enable';
K_end: 'end';
K_enum: 'enum';
K_export: 'export';
K_for: 'for';
K_function: 'function';
K_endfunction: 'endfunction';
K_if: 'if';
K_ifc_inout: 'ifc_inout';
K_import: 'import';
K_inout: 'inout';
K_input_clock: 'input_clock';
K_input_reset: 'input_reset';
K_instance: 'instance';
K_endinstance: 'endinstance';
K_interface: 'interface';
K_endinterface: 'endinterface';
K_let: 'let';
K_match: 'match';
K_matches: 'matches';
K_method: 'method';
K_endmethod: 'endmethod';
K_module: 'module';
K_endmodule: 'endmodule';
K_numeric: 'numeric';
K_output_clock: 'output_clock';
K_output_reset: 'output_reset';
K_package: 'package';
K_endpackage: 'endpackage';
K_parameter: 'parameter';
K_par: 'par';
K_endpar: 'endpar';
K_path: 'path';
K_port: 'port';
K_provisos: 'provisos';
K_reset_by: 'reset_by';
K_return: 'return';
K_rule: 'rule';
K_endrule: 'endrule';
K_rules: 'rules';
K_endrules: 'endrules';
K_same_family: 'same_family';
K_schedule: 'schedule';
K_struct: 'struct';
K_tagged: 'tagged';
K_type: 'type';
K_typeclass: 'typeclass';
K_endtypeclass: 'endtypeclass';
K_typedef: 'typedef';
K_union: 'union';
K_valueOf: 'valueOf';
K_valueof: 'valueof';
K_void: 'void';
K_while: 'while';

// The following are keywords in SystemVerilog (which includes all the keywords in Verilog).
// Although most of them are not used in BSV,
// for compatibility reasons they are not allowed as identifiers in BSV either.

K_alias: 'alias';
K_always: 'always';
K_always_comb: 'always_comb';
K_always_ff: 'always_ff';
K_always_latch: 'always_latch';
K_and: 'and';
K_assert: 'assert';
K_assert_strobe: 'assert_strobe';
K_assign: 'assign';
K_assume: 'assume';
K_automatic: 'automatic';
K_before: 'before';
K_bind: 'bind';
K_bins: 'bins';
K_binsof: 'binsof';
K_break: 'break';
K_buf: 'buf';
K_bufif0: 'bufif0';
K_bufif1: 'bufif1';
K_byte: 'byte';
K_casex: 'casex';
K_casez: 'casez';
K_cell: 'cell';
K_chandle: 'chandle';
K_class: 'class';
K_endclass: 'endclass';
K_clocking: 'clocking';
K_endclocking: 'endclocking';
K_cmos: 'cmos';
K_config: 'config';
K_endconfig: 'endconfig';
K_const: 'const';
K_constraint: 'constraint';
K_context: 'context';
K_continue: 'continue';
K_cover: 'cover';
K_covergroup: 'covergroup';
K_endgroup: 'endgroup';
K_coverpoint: 'coverpoint';
K_cross: 'cross';
K_deassign: 'deassign';
K_defparam: 'defparam';
K_design: 'design';
K_disable: 'disable';
K_dist: 'dist';
K_do: 'do';
K_edge: 'edge';
K_event: 'event';
K_expect: 'expect';
K_extends: 'extends';
K_extern: 'extern';
K_final: 'final';
K_first_match: 'first_match';
K_force: 'force';
K_foreach: 'foreach';
K_forever: 'forever';
K_fork: 'fork';
K_forkjoin: 'forkjoin';
K_generate: 'generate';
K_endgenerate: 'endgenerate';
K_genvar: 'genvar';
K_highz0: 'highz0';
K_highz1: 'highz1';
K_iff: 'iff';
K_ifnone: 'ifnone';
K_ignore_bins: 'ignore_bins';
K_illegal_bins: 'illegal_bins';
K_incdir: 'incdir';
K_include: 'include';
K_initial: 'initial';
K_input: 'input';
K_inside: 'inside';
K_int: 'int';
K_integer: 'integer';
K_intersect: 'intersect';
K_join: 'join';
K_join_any: 'join_any';
K_join_none: 'join_none';
K_large: 'large';
K_liblist: 'liblist';
K_library: 'library';
K_local: 'local';
K_localparam: 'localparam';
K_logic: 'logic';
K_longint: 'longint';
K_macromodule: 'macromodule';
K_medium: 'medium';
K_modport: 'modport';
K_nand: 'nand';
K_negedge: 'negedge';
K_new: 'new';
K_nmos: 'nmos';
K_nor: 'nor';
K_noshowcancelled: 'noshowcancelled';
K_not: 'not';
K_notif0: 'notif0';
K_notif1: 'notif1';
K_null: 'null';
K_or: 'or';
K_output: 'output';
K_packed: 'packed';
K_pmos: 'pmos';
K_posedge: 'posedge';
K_primitive: 'primitive';
K_endprimitive: 'endprimitive';
K_priority: 'priority';
K_program: 'program';
K_endprogram: 'endprogram';
K_property: 'property';
K_endproperty: 'endproperty';
K_protected: 'protected';
K_pull0: 'pull0';
K_pull1: 'pull1';
K_pulldown: 'pulldown';
K_pullup: 'pullup';
K_pulsestyle_onevent: 'pulsestyle_onevent';
K_pulsestyle_ondetect: 'pulsestyle_ondetect';
K_pure: 'pure';
K_rand: 'rand';
K_randc: 'randc';
K_randcase: 'randcase';
K_randsequence: 'randsequence';
K_rcmos: 'rcmos';
K_real: 'real';
K_realtime: 'realtime';
K_ref: 'ref';
K_reg: 'reg';
K_release: 'release';
K_repeat: 'repeat';
K_rnmos: 'rnmos';
K_rpmos: 'rpmos';
K_rtran: 'rtran';
K_rtranif0: 'rtranif0';
K_rtranif1: 'rtranif1';
K_scalared: 'scalared';
K_sequence: 'sequence';
K_seq: 'seq';
K_endsequence: 'endsequence';
K_endseq: 'endseq';
K_shortint: 'shortint';
K_shortreal: 'shortreal';
K_showcancelled: 'showcancelled';
K_signed: 'signed';
K_small: 'small';
K_solve: 'solve';
K_specify: 'specify';
K_endspecify: 'endspecify';
K_specparam: 'specparam';
K_static: 'static';
K_string: 'string';
K_strong0: 'strong0';
K_strong1: 'strong1';
K_super: 'super';
K_supply0: 'supply0';
K_supply1: 'supply1';
K_table: 'table';
K_endtable: 'endtable';
K_task: 'task';
K_endtask: 'endtask';
K_this: 'this';
K_throughout: 'throughout';
K_time: 'time';
K_timeprecision: 'timeprecision';
K_timeunit: 'timeunit';
K_tran: 'tran';
K_tranif0: 'tranif0';
K_tranif1: 'tranif1';
K_tri: 'tri';
K_tri0: 'tri0';
K_tri1: 'tri1';
K_triand: 'triand';
K_trior: 'trior';
K_trireg: 'trireg';
K_unique: 'unique';
K_unsigned: 'unsigned';
K_use: 'use';
K_var: 'var';
K_vectored: 'vectored';
K_virtual: 'virtual';
K_wait: 'wait';
K_wait_order: 'wait_order';
K_wand: 'wand';
K_weak0: 'weak0';
K_weak1: 'weak1';
K_wildcard: 'wildcard';
K_wire: 'wire';
K_with: 'with';
K_within: 'within';
K_wor: 'wor';
K_xnor: 'xnor';
K_xor: 'xor';

// maybe not keyworks...?
K_no_reset: 'no_reset';
K_Rules: 'Rules';
K_ready: 'ready';
K_BDPI: '"BDPI"';

AM       : '&';
AMAM     : '&&';
AMAMAM   : '&&&';
AMEQ     : '&=';
AP       : '\'';
AS       : '*';
ASAS     : '**';
ASEQ     : '*=';
ASGT     : '*>';
AT       : '@';
ATAT     : '@@';
CA       : '^';
CAEQ     : '^=';
CATI     : '^~';
CL       : ':';
CLCL     : '::';
CLEQ     : ':=';
CLSL     : ':/';
CO       : ',';
DL       : '$';
DQ       : '"';
DT       : '.';
DTAS     : '.*';
EM       : '!';
EMEQ     : '!=';
EMEQEQ   : '!==';
EMEQQM   : '!=?';
EQ       : '=';
EQEQ     : '==';
EQEQEQ   : '===';
EQEQQM   : '==?';
EQGT     : '=>';
// TODO: channel(DIRECTIVES), pushMode(DIRECTIVE_MODE);
GA       : '`';
GT       : '>';
GTEQ     : '>=';
GTGT     : '>>';
GTGTEQ   : '>>=';
GTGTGT   : '>>>';
GTGTGTEQ : '>>>=';
HA       : '#';
HAEQHA   : '#=#';
HAHA     : '##';
HAMIHA   : '#-#';
LB       : '[';
LC       : '{';
LP       : '(';
LT       : '<';
LTEQ     : '<=';
LTLT     : '<<';
LTLTEQ   : '<<=';
LTLTLT   : '<<<';
LTLTLTEQ : '<<<=';
LTMIGT   : '<->';
MI       : '-';
MICL     : '-:';
MIEQ     : '-=';
MIGT     : '->';
LTMI     : '<-';
MIGTGT   : '->>';
MIMI     : '--';
MO       : '%';
MOEQ     : '%=';
PL       : '+';
PLCL     : '+:';
PLEQ     : '+=';
PLPL     : '++';
QM       : '?';
RB       : ']';
LBRB     : '[]';
RC       : '}';
RP       : ')';
SC       : ';';
SL       : '/';
SLEQ     : '/=';
TI       : '~';
TIAM     : '~&';
TICA     : '~^';
TIVL     : '~|';
VL       : '|';
VLEQ     : '|=';
VLEQGT   : '|=>';
VLMIGT   : '|->';
VLVL     : '||';
LPDTDTRP : '(..)';
LPAS     : '(*';
ASRP     : '*)';


// tasks
/// systemTaskStmt
T_ungetc    : '$ungetc';
T_fflush    : '$fflush';
T_finish    : '$finish';
T_stop      : '$stop';
T_dumpvars  : '$dumpvars';
T_dumpon    : '$dumpon';
T_dumpoff   : '$dumpoff';
/// displayTaskName
T_display   : '$display';
T_displayb  : '$displayb';
T_displayo  : '$displayo';
T_displayh  : '$displayh';
T_write     : '$write';
T_writeb    : '$writeb';
T_writeo    : '$writeo';
T_writeh    : '$writeh';
/// stringTaskName
T_swrite    : '$swrite';
T_swriteb   : '$swriteb';
T_swriteo   : '$swriteo';
T_swriteh   : '$swriteh';
T_sformat   : '$sformat';
/// systemFunctionCall
T_time      : '$time';
T_stime     : '$stime';
/// systemTaskCall
T_realtobits: '$realtobits';
T_bitstoreal: '$bitstoreal';
T_testplusargs: '$test$plusargs';
T_format    : '$format';
T_fopen     : '$fopen';
T_fgetc     : 'fgetc';
/// stringAVTaskName
T_swriteAV  : '$swriteAV';
T_swritebAV : '$swritebAV';
T_swriteoAV : '$swriteoAV';
T_swritehAV : '$swritehAV';
T_sformatAV : '$sformatAV';




BlockComment
    :   '/*' .*? '*/'
        -> skip
    ;

LineComment
    :   '//' ~[\r\n]*
        -> skip
    ;


Whitespace
    :   [ \t]+
        -> skip
    ;

Newline
    :   (   '\r' '\n'?
        |   '\n'
        )
        -> skip
    ;


// meta: [] - optional {} - zero or more

// string literals
fragment
CharacterConstant
    :   '\'' CCharSequence '\'';

fragment
CCharSequence
    :   CChar+
    ;

fragment
CChar
    :   ~['\\\r\n]
    |   EscapeSequence
    ;

fragment
NoneWhitespace:
    ~[ \t];

fragment
EscapeSequence
    :   SimpleEscapeSequence
    |   OctalEscapeSequence
    |   HexadecimalEscapeSequence
    ;

fragment
SimpleEscapeSequence
    :   '\\' ['"abfnrtv\\]
    ;
fragment
OctalEscapeSequence
    :   '\\' OctalDigit OctalDigit? OctalDigit?
    ;
fragment
HexadecimalEscapeSequence
    :   '\\x' HexadecimalDigit+
    ;

StringLiteral
    :   '"' SCharSequence? '"'
    ;

fragment
SChar
    :   ~["\\\r\n]
    |   EscapeSequence
    |   '\\\n'   // Added line
    |   '\\\r\n' // Added line
    ;

fragment
HexadecimalDigit
    :   [0-9a-fA-F]
    ;


fragment
OctalDigit
    :   [0-7]
    ;

fragment
SCharSequence
    :   SChar+
    ;


// id

fragment
Digit
    :   [0-9]
    ;

Identifier
    :   (IdentifierNondigit)
        (   IdentifierNondigit
        |   Digit
        )*
    | '\\' NoneWhitespace+; // for special symbal

fragment
FileName
    :
        (   IdentifierNondigit
        |   Digit
        )+
    ;

fragment
IdentifierNondigit
    :   Nondigit
    ;


fragment
Nondigit
    :   [a-zA-Z_$]
    ;


// Integer literals
IntLiteral  :   '\'0' | '\'1'
            |   SizedIntLiteral
            |   UnsizedIntLiteral;

fragment
SizedIntLiteral :  BitWidth BaseLiteral;

fragment
UnsizedIntLiteral   :  /*Sign?*/ BaseLiteral  // ignore sign this stage
                    |   /*Sign?*/ DecNum;

fragment
BaseLiteral :   ('\'d' | '\'D') DecDigitsUnderscore
            |   ('\'h' | '\'H') HexDigitsUnderscore
            |   ('\'o' | '\'O') OctDigitsUnderscore
            |   ('\'b' | '\'B') BinDigitsUnderscore;

fragment
DecNum      :  DecDigits DecDigitsUnderscore?;
fragment
BitWidth    :  DecDigits;
fragment
Sign        :  '+' | '-';

fragment
DecDigits   :  Digit+;
fragment
DecDigitsUnderscore     :  [0-9_]+;
fragment
HexDigitsUnderscore     :  [0-9a-fA-F_]+;
fragment
OctDigitsUnderscore     :  [0-7_]+;
fragment
BinDigitsUnderscore     :  [01_]+;

//  Real literals

RealLiteral : DecNum('.'DecDigitsUnderscore)? Exp Sign? DecDigitsUnderscore
            | DecNum'.'DecDigitsUnderscore;

fragment
Exp         : 'e' | 'E';

//  Compiler directives

CompilerDirective   : ('`include' Whitespace* StringLiteral
                    | '`include' Whitespace* '<' Whitespace* FileName Whitespace* '>'
                    | '`include' MacroInvocation
                    | '`line' Whitespace* LineNumber Whitespace* '"' Whitespace* FileName Whitespace* '"' Whitespace* Level
                    | '`undef' Whitespace* MacroName
                    | '`resetall'
                    | '`ifdef' Whitespace* MacroName
                    | '`ifndef' Whitespace* MacroName
                    | '`elsif' Whitespace* MacroName
                    | '`else'
                    | '`endif'
                    ) -> skip;

fragment
LineNumber  : DecDigits;
fragment
Level       : '0' | '1' | '2';
fragment
MacroName   : Identifier;
fragment
MacroFormals: Identifier;

fragment
MacroInvocation_ : '\''MacroName('('MacroActuals')')?;

MacroInvocation : MacroInvocation_;
fragment
MacroActuals    : SubstText (',' SubstText)*;
fragment
SubstText       : .*?;
