package org.coode.oppl.error.parser;

public class ReportThrowable {
	
	private final Throwable t;
	private final int line;
	private final int charPosInLine;
	private final int length;
	
	public ReportThrowable ( Throwable t, int line, int charPosInLine, int length ) {
		this.t = t;
		this.line = line;
		this.charPosInLine = charPosInLine;
		this.length = length;
	}
	
	// GETTERS
	
	public Throwable getT() {
		return t;
	}
	
	public int getLine() {
		return line;
	}
	
	public int getCharPosInLine() {
		return charPosInLine;
	}
	
	public int getLength() {
		return length;
	}
	
}
