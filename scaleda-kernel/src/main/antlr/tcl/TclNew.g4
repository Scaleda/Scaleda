grammar TclNew;

source_text
    : commandLine* EOF
    ;

Identifier
    : '$'? [a-zA-Z_:] [a-zA-Z0-9_():]*
    ;

Text
    : [a-zA-Z_\-./\\]+ SpecialChars*
    ;

SpecialChars
    : [\-|]
    ;

commandArg
    : SpecialChars? Text
    ;

String
   : '"' (~ [\n\r])* '"'
   ;


OneLineComment
   : '#' .*? ('\n'|'\r\n'|EOF) -> channel (HIDDEN)
   ;

EmptyLine
   : [\r?\n]+
   ;

WhiteSpace
   : [ \t] + -> channel (HIDDEN)
   ;

Decimal
   : [0-9]+
   ;

OperatorAB
    : '+'
    | '-'
    | '*'
    | '/'
    | '%'
    | '=='
    | '!='
    | '<'
    | '>'
    | '<='
    | '>='
    | '&&'
    | '||'
    | '!'
    | '<<'
    | '>>'
    | '&'
    | '|'
    | '^'
    | '~'
    ;


expr
    : Identifier expr?
    | Decimal
    | String
    | commandArg
    | Text
    | expr OperatorAB expr
    | '{' expr '}'
    | '[' command ']'
    | '[' expr ']'
    ;

//BadCharacter
//   : . -> channel (HIDDEN)
//   ;

commandName
    : Identifier
    ;

commandEmpty
    : EmptyLine{}
    ;

command
    : commandName expr*
    | commandEmpty
    | if
    ;

commandLine
    : command (EmptyLine | ';')*
    ;

ifExpr
    : expr
    ;

ifBody
    : commandLine EmptyLine?
    ;

elseBody
    : commandLine
    ;

if
    : 'if' ifExpr '{' EmptyLine* ifBody* '}' (elsePart? | elseif)
    ;

elsePart
    : 'else' '{' EmptyLine* elseBody* '}'
    ;

elseif
    : 'elseif' ifExpr '{' EmptyLine* ifBody* '}' (elsePart? | elseif)
    ;