package org.coode.oppl.error.parser;

import org.antlr.runtime.tree.CommonTree;

public class UnrecognisedSymbol {
	
	private final CommonTree t;
	
	public UnrecognisedSymbol ( CommonTree t ) {
		this.t = t;
	}
	
	// GETTERS
	
	public CommonTree getT() {
		return t;
	}
	
}
