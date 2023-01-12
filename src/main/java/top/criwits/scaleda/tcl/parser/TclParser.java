// Generated from java-escape by ANTLR 4.11.1
package top.criwits.scaleda.tcl.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class TclParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.11.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, IDENTIFICADOR=34, CONST_INTEGER=35, CONST_DOUBLE=36, 
		CONST_STRING=37, WS=38, COMMENT=39, BAD_CHARACTER=40;
	public static final int
		RULE_inicio = 0, RULE_identificator = 1, RULE_puts = 2, RULE_gets = 3, 
		RULE_declaracion = 4, RULE_agrup = 5, RULE_aux_agrup = 6, RULE_aux_array = 7, 
		RULE_param_func = 8, RULE_aux_param = 9, RULE_asignacion = 10, RULE_indice = 11, 
		RULE_val_indice = 12, RULE_const = 13, RULE_line = 14, RULE_expr = 15, 
		RULE_expresion = 16, RULE_exp_or = 17, RULE_exp_and = 18, RULE_exp_ig = 19, 
		RULE_exp_rel = 20, RULE_exp_add = 21, RULE_exp_mul = 22, RULE_exp_pot = 23, 
		RULE_exp_una = 24, RULE_term = 25, RULE_op_ig = 26, RULE_op_rel = 27, 
		RULE_op_add = 28, RULE_op_mul = 29, RULE_op_una = 30;
	private static String[] makeRuleNames() {
		return new String[] {
			"inicio", "identificator", "puts", "gets", "declaracion", "agrup", "aux_agrup", 
			"aux_array", "param_func", "aux_param", "asignacion", "indice", "val_indice", 
			"const", "line", "expr", "expresion", "exp_or", "exp_and", "exp_ig", 
			"exp_rel", "exp_add", "exp_mul", "exp_pot", "exp_una", "term", "op_ig", 
			"op_rel", "op_add", "op_mul", "op_una"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'$'", "'${'", "'}'", "'puts'", "'gets'", "'stdin'", "'set'", "'['", 
			"']'", "'array'", "'size'", "'exists'", "'{'", "'('", "')'", "'expr'", 
			"'||'", "'&&'", "'**'", "'eq'", "'=='", "'ne'", "'!='", "'>'", "'<'", 
			"'>='", "'<='", "'+'", "'-'", "'*'", "'/'", "'%'", "'!'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, "IDENTIFICADOR", 
			"CONST_INTEGER", "CONST_DOUBLE", "CONST_STRING", "WS", "COMMENT", "BAD_CHARACTER"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "java-escape"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public TclParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InicioContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(TclParser.EOF, 0); }
		public List<LineContext> line() {
			return getRuleContexts(LineContext.class);
		}
		public LineContext line(int i) {
			return getRuleContext(LineContext.class,i);
		}
		public InicioContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inicio; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterInicio(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitInicio(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitInicio(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InicioContext inicio() throws RecognitionException {
		InicioContext _localctx = new InicioContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_inicio);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((_la) & ~0x3f) == 0 && ((1L << _la) & 176L) != 0) {
				{
				{
				setState(62);
				line();
				}
				}
				setState(67);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(68);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IdentificatorContext extends ParserRuleContext {
		public TerminalNode IDENTIFICADOR() { return getToken(TclParser.IDENTIFICADOR, 0); }
		public IndiceContext indice() {
			return getRuleContext(IndiceContext.class,0);
		}
		public IdentificatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identificator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterIdentificator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitIdentificator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitIdentificator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentificatorContext identificator() throws RecognitionException {
		IdentificatorContext _localctx = new IdentificatorContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_identificator);
		try {
			setState(77);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
				enterOuterAlt(_localctx, 1);
				{
				setState(70);
				match(T__0);
				setState(71);
				match(IDENTIFICADOR);
				setState(72);
				indice();
				}
				break;
			case T__1:
				enterOuterAlt(_localctx, 2);
				{
				setState(73);
				match(T__1);
				setState(74);
				match(IDENTIFICADOR);
				setState(75);
				match(T__2);
				setState(76);
				indice();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PutsContext extends ParserRuleContext {
		public AsignacionContext asignacion() {
			return getRuleContext(AsignacionContext.class,0);
		}
		public PutsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_puts; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterPuts(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitPuts(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitPuts(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PutsContext puts() throws RecognitionException {
		PutsContext _localctx = new PutsContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_puts);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(79);
			match(T__3);
			setState(80);
			asignacion();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class GetsContext extends ParserRuleContext {
		public GetsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_gets; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterGets(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitGets(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitGets(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GetsContext gets() throws RecognitionException {
		GetsContext _localctx = new GetsContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_gets);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
			match(T__4);
			setState(83);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DeclaracionContext extends ParserRuleContext {
		public TerminalNode IDENTIFICADOR() { return getToken(TclParser.IDENTIFICADOR, 0); }
		public AsignacionContext asignacion() {
			return getRuleContext(AsignacionContext.class,0);
		}
		public DeclaracionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaracion; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterDeclaracion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitDeclaracion(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitDeclaracion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclaracionContext declaracion() throws RecognitionException {
		DeclaracionContext _localctx = new DeclaracionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_declaracion);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
			match(T__6);
			setState(86);
			match(IDENTIFICADOR);
			setState(87);
			asignacion();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AgrupContext extends ParserRuleContext {
		public Aux_agrupContext aux_agrup() {
			return getRuleContext(Aux_agrupContext.class,0);
		}
		public AgrupContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_agrup; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterAgrup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitAgrup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitAgrup(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AgrupContext agrup() throws RecognitionException {
		AgrupContext _localctx = new AgrupContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_agrup);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(89);
			match(T__7);
			setState(90);
			aux_agrup();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Aux_agrupContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode IDENTIFICADOR() { return getToken(TclParser.IDENTIFICADOR, 0); }
		public Param_funcContext param_func() {
			return getRuleContext(Param_funcContext.class,0);
		}
		public GetsContext gets() {
			return getRuleContext(GetsContext.class,0);
		}
		public Aux_arrayContext aux_array() {
			return getRuleContext(Aux_arrayContext.class,0);
		}
		public Aux_agrupContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_aux_agrup; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterAux_agrup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitAux_agrup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitAux_agrup(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Aux_agrupContext aux_agrup() throws RecognitionException {
		Aux_agrupContext _localctx = new Aux_agrupContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_aux_agrup);
		try {
			setState(104);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__15:
				enterOuterAlt(_localctx, 1);
				{
				setState(92);
				expr();
				setState(93);
				match(T__8);
				}
				break;
			case IDENTIFICADOR:
				enterOuterAlt(_localctx, 2);
				{
				setState(95);
				match(IDENTIFICADOR);
				setState(96);
				param_func();
				setState(97);
				match(T__8);
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 3);
				{
				setState(99);
				gets();
				setState(100);
				match(T__8);
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 4);
				{
				setState(102);
				match(T__9);
				setState(103);
				aux_array();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Aux_arrayContext extends ParserRuleContext {
		public TerminalNode IDENTIFICADOR() { return getToken(TclParser.IDENTIFICADOR, 0); }
		public Aux_arrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_aux_array; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterAux_array(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitAux_array(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitAux_array(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Aux_arrayContext aux_array() throws RecognitionException {
		Aux_arrayContext _localctx = new Aux_arrayContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_aux_array);
		try {
			setState(112);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__10:
				enterOuterAlt(_localctx, 1);
				{
				setState(106);
				match(T__10);
				setState(107);
				match(IDENTIFICADOR);
				setState(108);
				match(T__8);
				}
				break;
			case T__11:
				enterOuterAlt(_localctx, 2);
				{
				setState(109);
				match(T__11);
				setState(110);
				match(IDENTIFICADOR);
				setState(111);
				match(T__8);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Param_funcContext extends ParserRuleContext {
		public Aux_paramContext aux_param() {
			return getRuleContext(Aux_paramContext.class,0);
		}
		public Param_funcContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_param_func; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterParam_func(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitParam_func(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitParam_func(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Param_funcContext param_func() throws RecognitionException {
		Param_funcContext _localctx = new Param_funcContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_param_func);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(114);
			match(T__12);
			setState(115);
			aux_param();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Aux_paramContext extends ParserRuleContext {
		public AsignacionContext asignacion() {
			return getRuleContext(AsignacionContext.class,0);
		}
		public Param_funcContext param_func() {
			return getRuleContext(Param_funcContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public Aux_paramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_aux_param; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterAux_param(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitAux_param(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitAux_param(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Aux_paramContext aux_param() throws RecognitionException {
		Aux_paramContext _localctx = new Aux_paramContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_aux_param);
		try {
			setState(125);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
			case T__7:
			case CONST_INTEGER:
			case CONST_DOUBLE:
			case CONST_STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(117);
				asignacion();
				setState(118);
				match(T__2);
				setState(119);
				param_func();
				}
				break;
			case T__15:
				enterOuterAlt(_localctx, 2);
				{
				setState(121);
				expr();
				setState(122);
				match(T__2);
				setState(123);
				param_func();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AsignacionContext extends ParserRuleContext {
		public ConstContext const_() {
			return getRuleContext(ConstContext.class,0);
		}
		public IdentificatorContext identificator() {
			return getRuleContext(IdentificatorContext.class,0);
		}
		public AgrupContext agrup() {
			return getRuleContext(AgrupContext.class,0);
		}
		public AsignacionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_asignacion; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterAsignacion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitAsignacion(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitAsignacion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AsignacionContext asignacion() throws RecognitionException {
		AsignacionContext _localctx = new AsignacionContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_asignacion);
		try {
			setState(131);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CONST_INTEGER:
			case CONST_DOUBLE:
			case CONST_STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(127);
				const_();
				}
				break;
			case T__0:
				enterOuterAlt(_localctx, 2);
				{
				setState(128);
				match(T__0);
				setState(129);
				identificator();
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 3);
				{
				setState(130);
				agrup();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IndiceContext extends ParserRuleContext {
		public Val_indiceContext val_indice() {
			return getRuleContext(Val_indiceContext.class,0);
		}
		public IndiceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_indice; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterIndice(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitIndice(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitIndice(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IndiceContext indice() throws RecognitionException {
		IndiceContext _localctx = new IndiceContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_indice);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(133);
			match(T__13);
			setState(134);
			val_indice();
			setState(135);
			match(T__14);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Val_indiceContext extends ParserRuleContext {
		public ConstContext const_() {
			return getRuleContext(ConstContext.class,0);
		}
		public AgrupContext agrup() {
			return getRuleContext(AgrupContext.class,0);
		}
		public IdentificatorContext identificator() {
			return getRuleContext(IdentificatorContext.class,0);
		}
		public Val_indiceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_val_indice; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterVal_indice(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitVal_indice(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitVal_indice(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Val_indiceContext val_indice() throws RecognitionException {
		Val_indiceContext _localctx = new Val_indiceContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_val_indice);
		try {
			setState(141);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CONST_INTEGER:
			case CONST_DOUBLE:
			case CONST_STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(137);
				const_();
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 2);
				{
				setState(138);
				agrup();
				}
				break;
			case T__0:
				enterOuterAlt(_localctx, 3);
				{
				setState(139);
				match(T__0);
				setState(140);
				identificator();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConstContext extends ParserRuleContext {
		public TerminalNode CONST_STRING() { return getToken(TclParser.CONST_STRING, 0); }
		public TerminalNode CONST_INTEGER() { return getToken(TclParser.CONST_INTEGER, 0); }
		public TerminalNode CONST_DOUBLE() { return getToken(TclParser.CONST_DOUBLE, 0); }
		public ConstContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_const; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterConst(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitConst(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitConst(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstContext const_() throws RecognitionException {
		ConstContext _localctx = new ConstContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_const);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(143);
			_la = _input.LA(1);
			if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 240518168576L) != 0) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LineContext extends ParserRuleContext {
		public PutsContext puts() {
			return getRuleContext(PutsContext.class,0);
		}
		public GetsContext gets() {
			return getRuleContext(GetsContext.class,0);
		}
		public DeclaracionContext declaracion() {
			return getRuleContext(DeclaracionContext.class,0);
		}
		public LineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_line; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterLine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitLine(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitLine(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LineContext line() throws RecognitionException {
		LineContext _localctx = new LineContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_line);
		try {
			setState(148);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__3:
				enterOuterAlt(_localctx, 1);
				{
				setState(145);
				puts();
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 2);
				{
				setState(146);
				gets();
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 3);
				{
				setState(147);
				declaracion();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprContext extends ParserRuleContext {
		public ExpresionContext expresion() {
			return getRuleContext(ExpresionContext.class,0);
		}
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		ExprContext _localctx = new ExprContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_expr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(150);
			match(T__15);
			setState(151);
			match(T__12);
			setState(152);
			expresion();
			setState(153);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpresionContext extends ParserRuleContext {
		public Exp_orContext exp_or() {
			return getRuleContext(Exp_orContext.class,0);
		}
		public ExpresionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expresion; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterExpresion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitExpresion(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitExpresion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpresionContext expresion() throws RecognitionException {
		ExpresionContext _localctx = new ExpresionContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_expresion);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(155);
			exp_or(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Exp_orContext extends ParserRuleContext {
		public Exp_andContext exp_and() {
			return getRuleContext(Exp_andContext.class,0);
		}
		public Exp_orContext exp_or() {
			return getRuleContext(Exp_orContext.class,0);
		}
		public Exp_orContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exp_or; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterExp_or(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitExp_or(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitExp_or(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Exp_orContext exp_or() throws RecognitionException {
		return exp_or(0);
	}

	private Exp_orContext exp_or(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Exp_orContext _localctx = new Exp_orContext(_ctx, _parentState);
		Exp_orContext _prevctx = _localctx;
		int _startState = 34;
		enterRecursionRule(_localctx, 34, RULE_exp_or, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(158);
			exp_and(0);
			}
			_ctx.stop = _input.LT(-1);
			setState(165);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new Exp_orContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_exp_or);
					setState(160);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(161);
					match(T__16);
					setState(162);
					exp_and(0);
					}
					} 
				}
				setState(167);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Exp_andContext extends ParserRuleContext {
		public Exp_igContext exp_ig() {
			return getRuleContext(Exp_igContext.class,0);
		}
		public Exp_andContext exp_and() {
			return getRuleContext(Exp_andContext.class,0);
		}
		public Exp_andContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exp_and; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterExp_and(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitExp_and(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitExp_and(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Exp_andContext exp_and() throws RecognitionException {
		return exp_and(0);
	}

	private Exp_andContext exp_and(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Exp_andContext _localctx = new Exp_andContext(_ctx, _parentState);
		Exp_andContext _prevctx = _localctx;
		int _startState = 36;
		enterRecursionRule(_localctx, 36, RULE_exp_and, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(169);
			exp_ig(0);
			}
			_ctx.stop = _input.LT(-1);
			setState(176);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new Exp_andContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_exp_and);
					setState(171);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(172);
					match(T__17);
					setState(173);
					exp_ig(0);
					}
					} 
				}
				setState(178);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Exp_igContext extends ParserRuleContext {
		public Exp_relContext exp_rel() {
			return getRuleContext(Exp_relContext.class,0);
		}
		public Exp_igContext exp_ig() {
			return getRuleContext(Exp_igContext.class,0);
		}
		public Op_igContext op_ig() {
			return getRuleContext(Op_igContext.class,0);
		}
		public Exp_igContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exp_ig; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterExp_ig(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitExp_ig(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitExp_ig(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Exp_igContext exp_ig() throws RecognitionException {
		return exp_ig(0);
	}

	private Exp_igContext exp_ig(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Exp_igContext _localctx = new Exp_igContext(_ctx, _parentState);
		Exp_igContext _prevctx = _localctx;
		int _startState = 38;
		enterRecursionRule(_localctx, 38, RULE_exp_ig, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(180);
			exp_rel(0);
			}
			_ctx.stop = _input.LT(-1);
			setState(188);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new Exp_igContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_exp_ig);
					setState(182);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(183);
					op_ig();
					setState(184);
					exp_rel(0);
					}
					} 
				}
				setState(190);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Exp_relContext extends ParserRuleContext {
		public Exp_addContext exp_add() {
			return getRuleContext(Exp_addContext.class,0);
		}
		public Exp_relContext exp_rel() {
			return getRuleContext(Exp_relContext.class,0);
		}
		public Op_relContext op_rel() {
			return getRuleContext(Op_relContext.class,0);
		}
		public Exp_relContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exp_rel; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterExp_rel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitExp_rel(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitExp_rel(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Exp_relContext exp_rel() throws RecognitionException {
		return exp_rel(0);
	}

	private Exp_relContext exp_rel(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Exp_relContext _localctx = new Exp_relContext(_ctx, _parentState);
		Exp_relContext _prevctx = _localctx;
		int _startState = 40;
		enterRecursionRule(_localctx, 40, RULE_exp_rel, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(192);
			exp_add(0);
			}
			_ctx.stop = _input.LT(-1);
			setState(200);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new Exp_relContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_exp_rel);
					setState(194);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(195);
					op_rel();
					setState(196);
					exp_add(0);
					}
					} 
				}
				setState(202);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Exp_addContext extends ParserRuleContext {
		public Exp_mulContext exp_mul() {
			return getRuleContext(Exp_mulContext.class,0);
		}
		public Exp_addContext exp_add() {
			return getRuleContext(Exp_addContext.class,0);
		}
		public Op_addContext op_add() {
			return getRuleContext(Op_addContext.class,0);
		}
		public Exp_addContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exp_add; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterExp_add(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitExp_add(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitExp_add(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Exp_addContext exp_add() throws RecognitionException {
		return exp_add(0);
	}

	private Exp_addContext exp_add(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Exp_addContext _localctx = new Exp_addContext(_ctx, _parentState);
		Exp_addContext _prevctx = _localctx;
		int _startState = 42;
		enterRecursionRule(_localctx, 42, RULE_exp_add, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(204);
			exp_mul(0);
			}
			_ctx.stop = _input.LT(-1);
			setState(212);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new Exp_addContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_exp_add);
					setState(206);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(207);
					op_add();
					setState(208);
					exp_mul(0);
					}
					} 
				}
				setState(214);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Exp_mulContext extends ParserRuleContext {
		public Exp_potContext exp_pot() {
			return getRuleContext(Exp_potContext.class,0);
		}
		public Exp_mulContext exp_mul() {
			return getRuleContext(Exp_mulContext.class,0);
		}
		public Op_mulContext op_mul() {
			return getRuleContext(Op_mulContext.class,0);
		}
		public Exp_mulContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exp_mul; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterExp_mul(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitExp_mul(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitExp_mul(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Exp_mulContext exp_mul() throws RecognitionException {
		return exp_mul(0);
	}

	private Exp_mulContext exp_mul(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Exp_mulContext _localctx = new Exp_mulContext(_ctx, _parentState);
		Exp_mulContext _prevctx = _localctx;
		int _startState = 44;
		enterRecursionRule(_localctx, 44, RULE_exp_mul, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(216);
			exp_pot(0);
			}
			_ctx.stop = _input.LT(-1);
			setState(224);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new Exp_mulContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_exp_mul);
					setState(218);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(219);
					op_mul();
					setState(220);
					exp_pot(0);
					}
					} 
				}
				setState(226);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Exp_potContext extends ParserRuleContext {
		public Exp_unaContext exp_una() {
			return getRuleContext(Exp_unaContext.class,0);
		}
		public Exp_potContext exp_pot() {
			return getRuleContext(Exp_potContext.class,0);
		}
		public Exp_potContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exp_pot; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterExp_pot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitExp_pot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitExp_pot(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Exp_potContext exp_pot() throws RecognitionException {
		return exp_pot(0);
	}

	private Exp_potContext exp_pot(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Exp_potContext _localctx = new Exp_potContext(_ctx, _parentState);
		Exp_potContext _prevctx = _localctx;
		int _startState = 46;
		enterRecursionRule(_localctx, 46, RULE_exp_pot, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(228);
			exp_una();
			}
			_ctx.stop = _input.LT(-1);
			setState(235);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new Exp_potContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_exp_pot);
					setState(230);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(231);
					match(T__18);
					setState(232);
					exp_una();
					}
					} 
				}
				setState(237);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Exp_unaContext extends ParserRuleContext {
		public Op_unaContext op_una() {
			return getRuleContext(Op_unaContext.class,0);
		}
		public Exp_unaContext exp_una() {
			return getRuleContext(Exp_unaContext.class,0);
		}
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public Exp_unaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exp_una; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterExp_una(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitExp_una(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitExp_una(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Exp_unaContext exp_una() throws RecognitionException {
		Exp_unaContext _localctx = new Exp_unaContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_exp_una);
		try {
			setState(242);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__28:
			case T__32:
				enterOuterAlt(_localctx, 1);
				{
				setState(238);
				op_una();
				setState(239);
				exp_una();
				}
				break;
			case T__0:
			case T__7:
			case T__13:
			case CONST_INTEGER:
			case CONST_DOUBLE:
			case CONST_STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(241);
				term();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TermContext extends ParserRuleContext {
		public IdentificatorContext identificator() {
			return getRuleContext(IdentificatorContext.class,0);
		}
		public AgrupContext agrup() {
			return getRuleContext(AgrupContext.class,0);
		}
		public ConstContext const_() {
			return getRuleContext(ConstContext.class,0);
		}
		public Exp_orContext exp_or() {
			return getRuleContext(Exp_orContext.class,0);
		}
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_term);
		try {
			setState(252);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
				enterOuterAlt(_localctx, 1);
				{
				setState(244);
				match(T__0);
				setState(245);
				identificator();
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 2);
				{
				setState(246);
				agrup();
				}
				break;
			case CONST_INTEGER:
			case CONST_DOUBLE:
			case CONST_STRING:
				enterOuterAlt(_localctx, 3);
				{
				setState(247);
				const_();
				}
				break;
			case T__13:
				enterOuterAlt(_localctx, 4);
				{
				setState(248);
				match(T__13);
				setState(249);
				exp_or(0);
				setState(250);
				match(T__14);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Op_igContext extends ParserRuleContext {
		public Op_igContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_op_ig; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterOp_ig(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitOp_ig(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitOp_ig(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Op_igContext op_ig() throws RecognitionException {
		Op_igContext _localctx = new Op_igContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_op_ig);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(254);
			_la = _input.LA(1);
			if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 15728640L) != 0) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Op_relContext extends ParserRuleContext {
		public Op_relContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_op_rel; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterOp_rel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitOp_rel(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitOp_rel(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Op_relContext op_rel() throws RecognitionException {
		Op_relContext _localctx = new Op_relContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_op_rel);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(256);
			_la = _input.LA(1);
			if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 251658240L) != 0) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Op_addContext extends ParserRuleContext {
		public Op_addContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_op_add; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterOp_add(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitOp_add(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitOp_add(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Op_addContext op_add() throws RecognitionException {
		Op_addContext _localctx = new Op_addContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_op_add);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(258);
			_la = _input.LA(1);
			if ( !(_la==T__27 || _la==T__28) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Op_mulContext extends ParserRuleContext {
		public Op_mulContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_op_mul; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterOp_mul(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitOp_mul(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitOp_mul(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Op_mulContext op_mul() throws RecognitionException {
		Op_mulContext _localctx = new Op_mulContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_op_mul);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(260);
			_la = _input.LA(1);
			if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 7516192768L) != 0) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Op_unaContext extends ParserRuleContext {
		public Op_unaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_op_una; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).enterOp_una(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TclListener ) ((TclListener)listener).exitOp_una(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TclVisitor ) return ((TclVisitor<? extends T>)visitor).visitOp_una(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Op_unaContext op_una() throws RecognitionException {
		Op_unaContext _localctx = new Op_unaContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_op_una);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(262);
			_la = _input.LA(1);
			if ( !(_la==T__28 || _la==T__32) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 17:
			return exp_or_sempred((Exp_orContext)_localctx, predIndex);
		case 18:
			return exp_and_sempred((Exp_andContext)_localctx, predIndex);
		case 19:
			return exp_ig_sempred((Exp_igContext)_localctx, predIndex);
		case 20:
			return exp_rel_sempred((Exp_relContext)_localctx, predIndex);
		case 21:
			return exp_add_sempred((Exp_addContext)_localctx, predIndex);
		case 22:
			return exp_mul_sempred((Exp_mulContext)_localctx, predIndex);
		case 23:
			return exp_pot_sempred((Exp_potContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean exp_or_sempred(Exp_orContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean exp_and_sempred(Exp_andContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean exp_ig_sempred(Exp_igContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean exp_rel_sempred(Exp_relContext _localctx, int predIndex) {
		switch (predIndex) {
		case 3:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean exp_add_sempred(Exp_addContext _localctx, int predIndex) {
		switch (predIndex) {
		case 4:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean exp_mul_sempred(Exp_mulContext _localctx, int predIndex) {
		switch (predIndex) {
		case 5:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean exp_pot_sempred(Exp_potContext _localctx, int predIndex) {
		switch (predIndex) {
		case 6:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001(\u0109\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"+
		"\u0001\u0000\u0005\u0000@\b\u0000\n\u0000\f\u0000C\t\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0003\u0001N\b\u0001\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0003\u0006"+
		"i\b\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0003\u0007q\b\u0007\u0001\b\u0001\b\u0001\b\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0003\t~\b\t\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0003\n\u0084\b\n\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0003\f\u008e\b\f\u0001"+
		"\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e\u0003\u000e\u0095\b\u000e"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u0010"+
		"\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0005\u0011\u00a4\b\u0011\n\u0011\f\u0011\u00a7\t\u0011\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0005"+
		"\u0012\u00af\b\u0012\n\u0012\f\u0012\u00b2\t\u0012\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0005\u0013"+
		"\u00bb\b\u0013\n\u0013\f\u0013\u00be\t\u0013\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0005\u0014\u00c7"+
		"\b\u0014\n\u0014\f\u0014\u00ca\t\u0014\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0005\u0015\u00d3\b\u0015"+
		"\n\u0015\f\u0015\u00d6\t\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0005\u0016\u00df\b\u0016\n"+
		"\u0016\f\u0016\u00e2\t\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0005\u0017\u00ea\b\u0017\n\u0017\f\u0017"+
		"\u00ed\t\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0003\u0018"+
		"\u00f3\b\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019"+
		"\u0001\u0019\u0001\u0019\u0001\u0019\u0003\u0019\u00fd\b\u0019\u0001\u001a"+
		"\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001c\u0001\u001c\u0001\u001d"+
		"\u0001\u001d\u0001\u001e\u0001\u001e\u0001\u001e\u0000\u0007\"$&(*,.\u001f"+
		"\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a"+
		"\u001c\u001e \"$&(*,.02468:<\u0000\u0006\u0001\u0000#%\u0001\u0000\u0014"+
		"\u0017\u0001\u0000\u0018\u001b\u0001\u0000\u001c\u001d\u0001\u0000\u001e"+
		" \u0002\u0000\u001d\u001d!!\u0101\u0000A\u0001\u0000\u0000\u0000\u0002"+
		"M\u0001\u0000\u0000\u0000\u0004O\u0001\u0000\u0000\u0000\u0006R\u0001"+
		"\u0000\u0000\u0000\bU\u0001\u0000\u0000\u0000\nY\u0001\u0000\u0000\u0000"+
		"\fh\u0001\u0000\u0000\u0000\u000ep\u0001\u0000\u0000\u0000\u0010r\u0001"+
		"\u0000\u0000\u0000\u0012}\u0001\u0000\u0000\u0000\u0014\u0083\u0001\u0000"+
		"\u0000\u0000\u0016\u0085\u0001\u0000\u0000\u0000\u0018\u008d\u0001\u0000"+
		"\u0000\u0000\u001a\u008f\u0001\u0000\u0000\u0000\u001c\u0094\u0001\u0000"+
		"\u0000\u0000\u001e\u0096\u0001\u0000\u0000\u0000 \u009b\u0001\u0000\u0000"+
		"\u0000\"\u009d\u0001\u0000\u0000\u0000$\u00a8\u0001\u0000\u0000\u0000"+
		"&\u00b3\u0001\u0000\u0000\u0000(\u00bf\u0001\u0000\u0000\u0000*\u00cb"+
		"\u0001\u0000\u0000\u0000,\u00d7\u0001\u0000\u0000\u0000.\u00e3\u0001\u0000"+
		"\u0000\u00000\u00f2\u0001\u0000\u0000\u00002\u00fc\u0001\u0000\u0000\u0000"+
		"4\u00fe\u0001\u0000\u0000\u00006\u0100\u0001\u0000\u0000\u00008\u0102"+
		"\u0001\u0000\u0000\u0000:\u0104\u0001\u0000\u0000\u0000<\u0106\u0001\u0000"+
		"\u0000\u0000>@\u0003\u001c\u000e\u0000?>\u0001\u0000\u0000\u0000@C\u0001"+
		"\u0000\u0000\u0000A?\u0001\u0000\u0000\u0000AB\u0001\u0000\u0000\u0000"+
		"BD\u0001\u0000\u0000\u0000CA\u0001\u0000\u0000\u0000DE\u0005\u0000\u0000"+
		"\u0001E\u0001\u0001\u0000\u0000\u0000FG\u0005\u0001\u0000\u0000GH\u0005"+
		"\"\u0000\u0000HN\u0003\u0016\u000b\u0000IJ\u0005\u0002\u0000\u0000JK\u0005"+
		"\"\u0000\u0000KL\u0005\u0003\u0000\u0000LN\u0003\u0016\u000b\u0000MF\u0001"+
		"\u0000\u0000\u0000MI\u0001\u0000\u0000\u0000N\u0003\u0001\u0000\u0000"+
		"\u0000OP\u0005\u0004\u0000\u0000PQ\u0003\u0014\n\u0000Q\u0005\u0001\u0000"+
		"\u0000\u0000RS\u0005\u0005\u0000\u0000ST\u0005\u0006\u0000\u0000T\u0007"+
		"\u0001\u0000\u0000\u0000UV\u0005\u0007\u0000\u0000VW\u0005\"\u0000\u0000"+
		"WX\u0003\u0014\n\u0000X\t\u0001\u0000\u0000\u0000YZ\u0005\b\u0000\u0000"+
		"Z[\u0003\f\u0006\u0000[\u000b\u0001\u0000\u0000\u0000\\]\u0003\u001e\u000f"+
		"\u0000]^\u0005\t\u0000\u0000^i\u0001\u0000\u0000\u0000_`\u0005\"\u0000"+
		"\u0000`a\u0003\u0010\b\u0000ab\u0005\t\u0000\u0000bi\u0001\u0000\u0000"+
		"\u0000cd\u0003\u0006\u0003\u0000de\u0005\t\u0000\u0000ei\u0001\u0000\u0000"+
		"\u0000fg\u0005\n\u0000\u0000gi\u0003\u000e\u0007\u0000h\\\u0001\u0000"+
		"\u0000\u0000h_\u0001\u0000\u0000\u0000hc\u0001\u0000\u0000\u0000hf\u0001"+
		"\u0000\u0000\u0000i\r\u0001\u0000\u0000\u0000jk\u0005\u000b\u0000\u0000"+
		"kl\u0005\"\u0000\u0000lq\u0005\t\u0000\u0000mn\u0005\f\u0000\u0000no\u0005"+
		"\"\u0000\u0000oq\u0005\t\u0000\u0000pj\u0001\u0000\u0000\u0000pm\u0001"+
		"\u0000\u0000\u0000q\u000f\u0001\u0000\u0000\u0000rs\u0005\r\u0000\u0000"+
		"st\u0003\u0012\t\u0000t\u0011\u0001\u0000\u0000\u0000uv\u0003\u0014\n"+
		"\u0000vw\u0005\u0003\u0000\u0000wx\u0003\u0010\b\u0000x~\u0001\u0000\u0000"+
		"\u0000yz\u0003\u001e\u000f\u0000z{\u0005\u0003\u0000\u0000{|\u0003\u0010"+
		"\b\u0000|~\u0001\u0000\u0000\u0000}u\u0001\u0000\u0000\u0000}y\u0001\u0000"+
		"\u0000\u0000~\u0013\u0001\u0000\u0000\u0000\u007f\u0084\u0003\u001a\r"+
		"\u0000\u0080\u0081\u0005\u0001\u0000\u0000\u0081\u0084\u0003\u0002\u0001"+
		"\u0000\u0082\u0084\u0003\n\u0005\u0000\u0083\u007f\u0001\u0000\u0000\u0000"+
		"\u0083\u0080\u0001\u0000\u0000\u0000\u0083\u0082\u0001\u0000\u0000\u0000"+
		"\u0084\u0015\u0001\u0000\u0000\u0000\u0085\u0086\u0005\u000e\u0000\u0000"+
		"\u0086\u0087\u0003\u0018\f\u0000\u0087\u0088\u0005\u000f\u0000\u0000\u0088"+
		"\u0017\u0001\u0000\u0000\u0000\u0089\u008e\u0003\u001a\r\u0000\u008a\u008e"+
		"\u0003\n\u0005\u0000\u008b\u008c\u0005\u0001\u0000\u0000\u008c\u008e\u0003"+
		"\u0002\u0001\u0000\u008d\u0089\u0001\u0000\u0000\u0000\u008d\u008a\u0001"+
		"\u0000\u0000\u0000\u008d\u008b\u0001\u0000\u0000\u0000\u008e\u0019\u0001"+
		"\u0000\u0000\u0000\u008f\u0090\u0007\u0000\u0000\u0000\u0090\u001b\u0001"+
		"\u0000\u0000\u0000\u0091\u0095\u0003\u0004\u0002\u0000\u0092\u0095\u0003"+
		"\u0006\u0003\u0000\u0093\u0095\u0003\b\u0004\u0000\u0094\u0091\u0001\u0000"+
		"\u0000\u0000\u0094\u0092\u0001\u0000\u0000\u0000\u0094\u0093\u0001\u0000"+
		"\u0000\u0000\u0095\u001d\u0001\u0000\u0000\u0000\u0096\u0097\u0005\u0010"+
		"\u0000\u0000\u0097\u0098\u0005\r\u0000\u0000\u0098\u0099\u0003 \u0010"+
		"\u0000\u0099\u009a\u0005\u0003\u0000\u0000\u009a\u001f\u0001\u0000\u0000"+
		"\u0000\u009b\u009c\u0003\"\u0011\u0000\u009c!\u0001\u0000\u0000\u0000"+
		"\u009d\u009e\u0006\u0011\uffff\uffff\u0000\u009e\u009f\u0003$\u0012\u0000"+
		"\u009f\u00a5\u0001\u0000\u0000\u0000\u00a0\u00a1\n\u0002\u0000\u0000\u00a1"+
		"\u00a2\u0005\u0011\u0000\u0000\u00a2\u00a4\u0003$\u0012\u0000\u00a3\u00a0"+
		"\u0001\u0000\u0000\u0000\u00a4\u00a7\u0001\u0000\u0000\u0000\u00a5\u00a3"+
		"\u0001\u0000\u0000\u0000\u00a5\u00a6\u0001\u0000\u0000\u0000\u00a6#\u0001"+
		"\u0000\u0000\u0000\u00a7\u00a5\u0001\u0000\u0000\u0000\u00a8\u00a9\u0006"+
		"\u0012\uffff\uffff\u0000\u00a9\u00aa\u0003&\u0013\u0000\u00aa\u00b0\u0001"+
		"\u0000\u0000\u0000\u00ab\u00ac\n\u0002\u0000\u0000\u00ac\u00ad\u0005\u0012"+
		"\u0000\u0000\u00ad\u00af\u0003&\u0013\u0000\u00ae\u00ab\u0001\u0000\u0000"+
		"\u0000\u00af\u00b2\u0001\u0000\u0000\u0000\u00b0\u00ae\u0001\u0000\u0000"+
		"\u0000\u00b0\u00b1\u0001\u0000\u0000\u0000\u00b1%\u0001\u0000\u0000\u0000"+
		"\u00b2\u00b0\u0001\u0000\u0000\u0000\u00b3\u00b4\u0006\u0013\uffff\uffff"+
		"\u0000\u00b4\u00b5\u0003(\u0014\u0000\u00b5\u00bc\u0001\u0000\u0000\u0000"+
		"\u00b6\u00b7\n\u0002\u0000\u0000\u00b7\u00b8\u00034\u001a\u0000\u00b8"+
		"\u00b9\u0003(\u0014\u0000\u00b9\u00bb\u0001\u0000\u0000\u0000\u00ba\u00b6"+
		"\u0001\u0000\u0000\u0000\u00bb\u00be\u0001\u0000\u0000\u0000\u00bc\u00ba"+
		"\u0001\u0000\u0000\u0000\u00bc\u00bd\u0001\u0000\u0000\u0000\u00bd\'\u0001"+
		"\u0000\u0000\u0000\u00be\u00bc\u0001\u0000\u0000\u0000\u00bf\u00c0\u0006"+
		"\u0014\uffff\uffff\u0000\u00c0\u00c1\u0003*\u0015\u0000\u00c1\u00c8\u0001"+
		"\u0000\u0000\u0000\u00c2\u00c3\n\u0002\u0000\u0000\u00c3\u00c4\u00036"+
		"\u001b\u0000\u00c4\u00c5\u0003*\u0015\u0000\u00c5\u00c7\u0001\u0000\u0000"+
		"\u0000\u00c6\u00c2\u0001\u0000\u0000\u0000\u00c7\u00ca\u0001\u0000\u0000"+
		"\u0000\u00c8\u00c6\u0001\u0000\u0000\u0000\u00c8\u00c9\u0001\u0000\u0000"+
		"\u0000\u00c9)\u0001\u0000\u0000\u0000\u00ca\u00c8\u0001\u0000\u0000\u0000"+
		"\u00cb\u00cc\u0006\u0015\uffff\uffff\u0000\u00cc\u00cd\u0003,\u0016\u0000"+
		"\u00cd\u00d4\u0001\u0000\u0000\u0000\u00ce\u00cf\n\u0002\u0000\u0000\u00cf"+
		"\u00d0\u00038\u001c\u0000\u00d0\u00d1\u0003,\u0016\u0000\u00d1\u00d3\u0001"+
		"\u0000\u0000\u0000\u00d2\u00ce\u0001\u0000\u0000\u0000\u00d3\u00d6\u0001"+
		"\u0000\u0000\u0000\u00d4\u00d2\u0001\u0000\u0000\u0000\u00d4\u00d5\u0001"+
		"\u0000\u0000\u0000\u00d5+\u0001\u0000\u0000\u0000\u00d6\u00d4\u0001\u0000"+
		"\u0000\u0000\u00d7\u00d8\u0006\u0016\uffff\uffff\u0000\u00d8\u00d9\u0003"+
		".\u0017\u0000\u00d9\u00e0\u0001\u0000\u0000\u0000\u00da\u00db\n\u0002"+
		"\u0000\u0000\u00db\u00dc\u0003:\u001d\u0000\u00dc\u00dd\u0003.\u0017\u0000"+
		"\u00dd\u00df\u0001\u0000\u0000\u0000\u00de\u00da\u0001\u0000\u0000\u0000"+
		"\u00df\u00e2\u0001\u0000\u0000\u0000\u00e0\u00de\u0001\u0000\u0000\u0000"+
		"\u00e0\u00e1\u0001\u0000\u0000\u0000\u00e1-\u0001\u0000\u0000\u0000\u00e2"+
		"\u00e0\u0001\u0000\u0000\u0000\u00e3\u00e4\u0006\u0017\uffff\uffff\u0000"+
		"\u00e4\u00e5\u00030\u0018\u0000\u00e5\u00eb\u0001\u0000\u0000\u0000\u00e6"+
		"\u00e7\n\u0002\u0000\u0000\u00e7\u00e8\u0005\u0013\u0000\u0000\u00e8\u00ea"+
		"\u00030\u0018\u0000\u00e9\u00e6\u0001\u0000\u0000\u0000\u00ea\u00ed\u0001"+
		"\u0000\u0000\u0000\u00eb\u00e9\u0001\u0000\u0000\u0000\u00eb\u00ec\u0001"+
		"\u0000\u0000\u0000\u00ec/\u0001\u0000\u0000\u0000\u00ed\u00eb\u0001\u0000"+
		"\u0000\u0000\u00ee\u00ef\u0003<\u001e\u0000\u00ef\u00f0\u00030\u0018\u0000"+
		"\u00f0\u00f3\u0001\u0000\u0000\u0000\u00f1\u00f3\u00032\u0019\u0000\u00f2"+
		"\u00ee\u0001\u0000\u0000\u0000\u00f2\u00f1\u0001\u0000\u0000\u0000\u00f3"+
		"1\u0001\u0000\u0000\u0000\u00f4\u00f5\u0005\u0001\u0000\u0000\u00f5\u00fd"+
		"\u0003\u0002\u0001\u0000\u00f6\u00fd\u0003\n\u0005\u0000\u00f7\u00fd\u0003"+
		"\u001a\r\u0000\u00f8\u00f9\u0005\u000e\u0000\u0000\u00f9\u00fa\u0003\""+
		"\u0011\u0000\u00fa\u00fb\u0005\u000f\u0000\u0000\u00fb\u00fd\u0001\u0000"+
		"\u0000\u0000\u00fc\u00f4\u0001\u0000\u0000\u0000\u00fc\u00f6\u0001\u0000"+
		"\u0000\u0000\u00fc\u00f7\u0001\u0000\u0000\u0000\u00fc\u00f8\u0001\u0000"+
		"\u0000\u0000\u00fd3\u0001\u0000\u0000\u0000\u00fe\u00ff\u0007\u0001\u0000"+
		"\u0000\u00ff5\u0001\u0000\u0000\u0000\u0100\u0101\u0007\u0002\u0000\u0000"+
		"\u01017\u0001\u0000\u0000\u0000\u0102\u0103\u0007\u0003\u0000\u0000\u0103"+
		"9\u0001\u0000\u0000\u0000\u0104\u0105\u0007\u0004\u0000\u0000\u0105;\u0001"+
		"\u0000\u0000\u0000\u0106\u0107\u0007\u0005\u0000\u0000\u0107=\u0001\u0000"+
		"\u0000\u0000\u0011AMhp}\u0083\u008d\u0094\u00a5\u00b0\u00bc\u00c8\u00d4"+
		"\u00e0\u00eb\u00f2\u00fc";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}