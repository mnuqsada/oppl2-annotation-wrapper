package org.coode.oppl.error.parser;

import org.antlr.runtime.tree.CommonTree;
import org.coode.parsers.Type;

public class IncompatibleSymbolType {
	
	private final CommonTree t;
	private final Type type;
	private final CommonTree expression;
	
	public IncompatibleSymbolType ( CommonTree t, Type type, CommonTree expression ) {
		this.t = t;
		this.type = type;
		this.expression = expression;
	}
	
	// GETTERS
	
	public CommonTree getT() {
		return t;
	}
	
	public Type getType() {
		return type;
	}
	
	public CommonTree getExpression() {
		return expression;
	}
}
