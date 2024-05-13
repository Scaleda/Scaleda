grammar TclNew;

source_text
    : commandLine* EOF
    ;

Identifier
    : '$'? ([a-zA-Z_:] [a-zA-Z0-9_():]* | '{' [a-zA-Z_:] [a-zA-Z0-9_():]* '}')
    ;

Text
    : [a-z0-9A-Z_\-./\\,()@]+ SpecialChars*
    ;

SpecialChars
    : [\-]
    ;

commandArg
    : SpecialChars? Text
    ;

fragment ESC
    : '\\"' | '\\\\'
    ;

String
    :'"' (ESC|.)*? '"'
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
    | '<<'
    | '>>'
    | '&'
    | '|'
    | '^'
    ;

OperatorA
    : '!'
    | '~'
    ;


expr
    : Identifier expr?
//    | Decimal
    | String
    | commandArg
    | Text
    | expr OperatorAB expr
    | OperatorA expr
    | expr '?' expr ':' expr
    | '(' expr ')'
    | '{' expr '}'
    | '[' command ';'? ']'
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
    | expr+
    | commandEmpty
    | if
    | switch
    | while
    | for
    | foreach
    | proc
    | package
    | namespace
    | regexp
    ;

commandLine
    : command EmptyLine*
    | ';'
    ;

ifCond
    : expr
    ;

ifBody
    : commandLine EmptyLine?
    ;

elseBody
    : commandLine
    ;

if
    : 'if' ifCond EmptyLine* '{' EmptyLine* ifBody* '}' EmptyLine* (elsePart? | elseif)
    ;

elsePart
    : 'else' EmptyLine* '{' EmptyLine* elseBody* '}'
    ;

elseif
    : 'elseif' ifCond EmptyLine* '{' EmptyLine* ifBody* '}' EmptyLine* (elsePart? | elseif)
    ;

switchExpr
    : expr
    ;

switchMatch
    : expr
    ;

switchBody
    : commandLine
    ;

switchDefaultBody
    : commandLine
    ;

switch
    : 'switch' switchExpr '{' EmptyLine*
        ( switchMatch '{' EmptyLine* switchBody* '}' EmptyLine* )+
        ( 'default' '{' EmptyLine* switchDefaultBody* '}' )? EmptyLine* '}'
    ;

whileBody
    : commandLine
    ;

whileCond
    : expr
    ;

while
    : 'while' EmptyLine* whileCond EmptyLine* '{' EmptyLine*
        whileBody* EmptyLine* '}'
    ;

forInit
    : expr
    ;

forCond
    : expr
    ;

forIncr
    : expr
    ;

forBody
    : commandLine
    ;

for
    : 'for' EmptyLine* forInit EmptyLine* forCond EmptyLine* forIncr EmptyLine* '{'
         forBody* EmptyLine* '}'
    ;

foreachVar
    : Identifier
    ;

foreachList
    : expr
    ;

foreachBody
    : commandLine
    ;

foreach
    : 'foreach' EmptyLine* foreachVar EmptyLine* foreachList '{'
        foreachBody* EmptyLine* '}'
    ;

procIdentifier
    : Identifier
    ;

procArg
    : Identifier
    ;

procArgDef
    : '{' procArg Text '}'
    ;

procBody
    : commandLine
    ;

proc
    : 'proc' EmptyLine* procIdentifier EmptyLine* '{' (procArg | procArgDef)* '}' EmptyLine*
        '{' procBody* EmptyLine* '}'
    ;

packageIdentifier
    : Identifier
    ;

packageVersion
    : Text
    ;

packageRequire
    : 'package' 'require' packageIdentifier packageVersion?
    ;

packageProvide
    : 'package' 'provide' packageIdentifier packageVersion?
    ;

package
    : packageRequire
    | packageProvide
    ;

namespaceIdentifier
    : Identifier
    ;

namespace
    : 'namespace' EmptyLine* 'eval' EmptyLine* namespaceIdentifier EmptyLine* '{' EmptyLine* commandLine* '}'
    ;

regexp
    : 'regexp' .*? (EmptyLine | ';')
    ;