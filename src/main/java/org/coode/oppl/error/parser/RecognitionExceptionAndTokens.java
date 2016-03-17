package org.coode.oppl.error.parser;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.RecognitionException;

public class RecognitionExceptionAndTokens {

	private final RecognitionException e;
	private final List<String> tokenNames;
	
	public RecognitionExceptionAndTokens(RecognitionException e, String... tokenNames) {
		this.e = e;
		this.tokenNames = new ArrayList<String>();
		for ( String t : tokenNames ) {
			this.tokenNames.add(t);
		}
	}
	
	// GETTERS 
	
	public RecognitionException getE() {
		return e;
	}
	
	public List<String> getTokenNames() {
		return tokenNames;
	}
	
}
