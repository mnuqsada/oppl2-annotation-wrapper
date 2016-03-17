package org.coode.oppl.error.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.RewriteEmptyStreamException;
import org.coode.parsers.ErrorListener;
import org.coode.parsers.Type;

public class MyOPPLParserErrorListener implements ErrorListener {

	private final List<UnrecognisedSymbol> unrecognisedSymbols = new ArrayList<UnrecognisedSymbol>();
	private final List<IncompatibleSymbolType> incompatibleSymbolTypes = new ArrayList<IncompatibleSymbolType>();
	private final List<IncompatibleSymbols> incompatibleSymbols = new ArrayList<IncompatibleSymbols>();
	private final List<IllegalToken> illegalTokens = new ArrayList<IllegalToken>();
	private final List<RecognitionExceptionAndTokens> recognitionExceptions = new ArrayList<RecognitionExceptionAndTokens>();
	private final List<RewriteEmptyStreamException> rewriteEmptyStreamExceptions = new ArrayList<RewriteEmptyStreamException>();
	private final List<ReportThrowable> reportThrowables = new ArrayList<ReportThrowable>();
	
	@Override
	public void unrecognisedSymbol(CommonTree t) {
		Logger.getAnonymousLogger().severe(t.toString());
		unrecognisedSymbols.add(new UnrecognisedSymbol(t));
	}

	@Override
	public void incompatibleSymbolType(CommonTree t, Type type, CommonTree expression) {
		Logger.getAnonymousLogger().severe(t.toString()+type.toString()+expression.toString());
		incompatibleSymbolTypes.add(new IncompatibleSymbolType(t, type, expression));
	}

	@Override
	public void incompatibleSymbols(CommonTree parentExpression, CommonTree... trees) {
		Logger.getAnonymousLogger().severe(parentExpression.toString()+trees.toString());
		incompatibleSymbols.add(new IncompatibleSymbols(parentExpression, trees));
	}

	@Override
	public void illegalToken(CommonTree t, String message) {
		Logger.getAnonymousLogger().severe(t.toString()+message);
		illegalTokens.add(new IllegalToken(t, message));
	}

	@Override
	public void recognitionException(RecognitionException e) {
		Logger.getAnonymousLogger().severe(e.toString());
		recognitionExceptions.add(new RecognitionExceptionAndTokens(e));
	}

	@Override
	public void recognitionException(RecognitionException e, String... tokenNames) {
		Logger.getAnonymousLogger().severe(e.toString()+tokenNames.toString());
		recognitionExceptions.add(new RecognitionExceptionAndTokens(e, tokenNames));
	}

	@Override
	public void rewriteEmptyStreamException(RewriteEmptyStreamException e) {
		Logger.getAnonymousLogger().severe(e.toString());
		rewriteEmptyStreamExceptions.add(e);
	}

	@Override
	public void reportThrowable(Throwable t, int line, int charPosInLine, int length) {
		Logger.getAnonymousLogger().severe(t.toString()+line+charPosInLine+length);
		reportThrowables.add(new ReportThrowable(t, line, charPosInLine, length));
	}
	
	// GETTERS AND SETTERS

	public Set<String> getUnrecognisedSymbols () {
		HashSet<String> symbols = new HashSet<String>();
		for ( UnrecognisedSymbol symbol : unrecognisedSymbols ) {
			symbols.add(symbol.getT().toString());
		}
		return symbols;
	}
	
	public int getNumberOfErrors () {
		return 
		unrecognisedSymbols.size() +
		incompatibleSymbolTypes.size() +
		incompatibleSymbols.size() +
		illegalTokens.size() +
		recognitionExceptions.size() +
		rewriteEmptyStreamExceptions.size() +
		reportThrowables.size();
	}
	
}
