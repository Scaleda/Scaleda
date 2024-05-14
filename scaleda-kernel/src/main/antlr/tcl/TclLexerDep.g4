// not in use

lexer grammar TclLexer;

Identifier
    : '$'? ([a-zA-Z_:] [a-zA-Z0-9_():]* | '{' [a-zA-Z_:] [a-zA-Z0-9_():]* '}')
    ;

Text
    : [a-z0-9A-Z_\-./\\,()@]+ SpecialChars*
    ;

SpecialChars
    : [\-]
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

//AM       : '&';
//AMAM     : '&&';
AMAMAM   : '&&&';
AMEQ     : '&=';
AP       : '\'';
//AS       : '*';
ASAS     : '**';
ASEQ     : '*=';
ASGT     : '*>';
AT       : '@';
ATAT     : '@@';
//CA       : '^';
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
//EMEQ     : '!=';
EMEQEQ   : '!==';
EMEQQM   : '!=?';
EQ       : '=';
//EQEQ     : '==';
EQEQEQ   : '===';
EQEQQM   : '==?';
EQGT     : '=>';
//GT       : '>';
//GTEQ     : '>=';
//GTGT     : '>>';
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
//LT       : '<';
//LTEQ     : '<=';
//LTLT     : '<<';
LTLTEQ   : '<<=';
LTLTLT   : '<<<';
LTLTLTEQ : '<<<=';
LTMIGT   : '<->';
//MI       : '-';
MICL     : '-:';
MIEQ     : '-=';
MIGT     : '->';
MIGTGT   : '->>';
MIMI     : '--';
//MO       : '%';
MOEQ     : '%=';
//PL       : '+';
PLCL     : '+:';
PLEQ     : '+=';
PLPL     : '++';
QM       : '?';
RB       : ']';
RC       : '}';
RP       : ')';
SC       : ';';
//SL       : '/';
SLEQ     : '/=';
TI       : '~';
TIAM     : '~&';
TICA     : '~^';
TIVL     : '~|';
//VL       : '|';
VLEQ     : '|=';
VLEQGT   : '|=>';
VLMIGT   : '|->';
//VLVL     : '||';

// Keywords

//K_if: 'if';
K_else: 'else';
K_elseif: 'elseif';
K_default: 'default';
K_require: 'require';
K_provide: 'provide';

K_after: 'after';
K_errorInfo: 'errorInfo';
K_load: 'load';
K_re_syntax: 're_syntax';
K_tcl_startOfNextWord: 'tcl_startOfNextWord';
K_append: 'append';
K_eval: 'eval';
K_lrange: 'lrange';
K_read: 'read';
K_tcl_startOfPreviousWord: 'tcl_startOfPreviousWord';
K_apply: 'apply';
K_exec: 'exec';
K_lrepeat: 'lrepeat';
K_refchan: 'refchan';
K_tcl_traceCompile: 'tcl_traceCompile';
K_argc: 'argc';
K_exit: 'exit';
K_lreplace: 'lreplace';
K_regexp: 'regexp';
K_tcl_traceExec: 'tcl_traceExec';
K_argv: 'argv';
K_expr: 'expr';
K_lreverse: 'lreverse';
K_registry: 'registry';
K_tcl_version: 'tcl_version';
K_argv0: 'argv0';
K_fblocked: 'fblocked';
K_lsearch: 'lsearch';
K_regsub: 'regsub';
K_tcl_wordBreakAfter: 'tcl_wordBreakAfter';
K_array: 'array';
K_fconfigure: 'fconfigure';
K_lset: 'lset';
K_rename: 'rename';
K_tcl_wordBreakBefore: 'tcl_wordBreakBefore';
K_auto_execok: 'auto_execok';
K_fcopy: 'fcopy';
K_lsort: 'lsort';
K_return: 'return';
K_tcl_wordchars: 'tcl_wordchars';
K_auto_import: 'auto_import';
K_file: 'file';
K_mathfunc: 'mathfunc';
K_safe: 'safe';
K_tcltest: 'tcltest';
K_auto_load: 'auto_load';
K_fileevent: 'fileevent';
K_mathop: 'mathop';
K_scan: 'scan';
K_tell: 'tell';
K_auto_mkindex: 'auto_mkindex';
K_filename: 'filename';
K_memory: 'memory';
K_seek: 'seek';
K_throw: 'throw';
K_auto_path: 'auto_path';
K_flush: 'flush';
K_msgcat: 'msgcat';
K_self: 'self';
K_time: 'time';
K_auto_qualify: 'auto_qualify';
K_for: 'for';
K_my: 'my';
K_set: 'set';
K_timerate: 'timerate';
K_auto_reset: 'auto_reset';
K_foreach: 'foreach';
K_namespace: 'namespace';
K_socket: 'socket';
K_tm: 'tm';
K_bgerror: 'bgerror';
K_format: 'format';
K_next: 'next';
K_source: 'source';
K_trace: 'trace';
K_binary: 'binary';
K_gets: 'gets';
K_nextto: 'nextto';
K_split: 'split';
K_transchan: 'transchan';
K_break: 'break';
K_glob: 'glob';
//K_oo::class: 'oo::class';
K_string: 'string';
K_try: 'try';
K_catch: 'catch';
K_global: 'global';
//K_oo::copy: 'oo::copy';
K_subst: 'subst';
K_unknown: 'unknown';
K_cd: 'cd';
K_history: 'history';
//K_oo::define: 'oo::define';
K_switch: 'switch';
K_unload: 'unload';
K_chan: 'chan';
K_http: 'http';
//K_oo::objdefine: 'oo::objdefine';
K_tailcall: 'tailcall';
K_unset: 'unset';
K_clock: 'clock';
K_if: 'if';
//K_oo::object: 'oo::object';
K_Tcl: 'Tcl';
K_update: 'update';
K_close: 'close';
K_incr: 'incr';
K_open: 'open';
//K_tcl::prefix: 'tcl::prefix';
K_uplevel: 'uplevel';
K_concat: 'concat';
K_info: 'info';
K_package: 'package';
K_tcl_endOfWord: 'tcl_endOfWord';
K_upvar: 'upvar';
K_continue: 'continue';
K_interp: 'interp';
K_parray: 'parray';
K_tcl_findLibrary: 'tcl_findLibrary';
K_variable: 'variable';
K_coroutine: 'coroutine';
K_join: 'join';
K_pid: 'pid';
K_tcl_interactive: 'tcl_interactive';
K_vwait: 'vwait';
K_dde: 'dde';
K_lappend: 'lappend';
//K_pkg::create: 'pkg::create';
K_tcl_library: 'tcl_library';
K_while: 'while';
K_dict: 'dict';
K_lassign: 'lassign';
K_pkg_mkIndex: 'pkg_mkIndex';
K_tcl_nonwordchars: 'tcl_nonwordchars';
K_yield: 'yield';
K_encoding: 'encoding';
K_lindex: 'lindex';
K_platform: 'platform';
K_tcl_patchLevel: 'tcl_patchLevel';
K_yieldto: 'yieldto';
K_env: 'env';
K_linsert: 'linsert';
//K_platform::shell: 'platform::shell';
K_tcl_pkgPath: 'tcl_pkgPath';
K_zlib: 'zlib';
K_eof: 'eof';
K_list: 'list';
K_proc: 'proc';
K_tcl_platform: 'tcl_platform';
K_error: 'error';
K_llength: 'llength';
K_puts: 'puts';
K_tcl_precision: 'tcl_precision';
K_errorCode: 'errorCode';
K_lmap: 'lmap';
K_pwd: 'pwd';
K_tcl_rcFileName: 'tcl_rcFileName';
// vivado
K_create_project: 'create_project';
K_open_project: 'open_project';
K_close_project: 'close_project';
K_import_files: 'import_files';
K_add_files: 'add_files';
K_set_property: 'set_property';
K_launch_runs: 'launch_runs';
K_wait_on_run: 'wait_on_run';
K_open_run: 'open_run';
K_report_timing_summary: 'report_timing_summary';
K_close_design: 'close_design';
K_open_hw_manager: 'open_hw_manager';
K_connect_hw_server: 'connect_hw_server';
K_open_hw_target: 'open_hw_target';
K_current_hw_device: 'current_hw_device';
K_refresh_hw_device: 'refresh_hw_device';
K_program_hw_devices: 'program_hw_devices';
K_launch_simulation: 'launch_simulation';
K_open_vcd: 'open_vcd';
K_log_vcd: 'log_vcd';
K_run: 'run';
K_close_vcd: 'close_vcd';
K_save_wave_config: 'save_wave_config';
K_close_sim: 'close_sim';
// quartus
K_set_global_assignment: 'set_global_assignment';
K_set_instance_assignment: 'set_instance_assignment';
K_set_location_assignment: 'set_location_assignment';
K_export_assignments: 'export_assignments';
K_project_new: 'project_new';
K_project_open: 'project_open';
K_project_close: 'project_close';