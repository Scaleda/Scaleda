grammar BluespecParser;

sourceText: intLiteral;

//options
//   { tokenVocab = BluespecLexer; }

K_allBitsZeros: '\'0' ;
K_allBitsOnes: '\'1' ;

K_module : 'module' ;
K_endmodule : 'endmodule' ;

K_begin : 'begin' ;
K_end : 'end' ;

K_if : 'if' ;
K_elif : 'elif' ;
K_else : 'else' ;

K_for : 'for' ;

fragment Decimal_base: '\'' [dD] ;
fragment Binary_base: '\'' [bB] ;
fragment Octal_base: '\'' [oO] ;
fragment Hex_base: '\'' [hH] ;

fragment DecDigits: [0-9]+ ;
fragment DecDigitsUnderscore: [0-9]+ ;
fragment HexDigitsUnderscore: [0-9a-fA-F]+ ;
fragment OctDigitsUnderscore: [0-7]+ ;
fragment BinDigitsUnderscore: [01]+ ;

BaseLiteral
    : Decimal_base DecDigitsUnderscore
    | Hex_base HexDigitsUnderscore
    | Octal_base OctDigitsUnderscore
    | Binary_base BinDigitsUnderscore;

DecNum: DecDigits DecDigitsUnderscore?;

Sign: '+' | '-';

// ===========================================

intLiteral
    : K_allBitsZeros | K_allBitsOnes
    | sizedIntLiteral
    | unsizedIntLiteral;

sizedIntLiteral: bitWidth BaseLiteral;

unsizedIntLiteral
    : Sign+ BaseLiteral
    | Sign+ DecNum;

bitWidth: DecDigits;

