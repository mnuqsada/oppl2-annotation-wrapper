package org.coode.oppl.error.parser;

import org.antlr.runtime.tree.CommonTree;

public class IllegalToken {

	private final CommonTree t;
	private final String message;
	
	public IllegalToken (CommonTree t, String message) {
		this.t = t;
		this.message = message;
	}
	
	// GETTERS
	
	public CommonTree getT() {
		return t;
	}
	
	public String getMessage() {
		return message;
	}
	
}
