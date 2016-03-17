package org.coode.oppl.error.parser;

import java.util.ArrayList;

import org.antlr.runtime.tree.CommonTree;

public class IncompatibleSymbols {
	
	private final CommonTree parentExpression;
	private final ArrayList<CommonTree> trees;
	
	public IncompatibleSymbols( CommonTree parentExpression, CommonTree... trees ) {
		this.parentExpression = parentExpression;
		this.trees = new ArrayList<CommonTree>();
		for ( CommonTree ct : trees ) {
			this.trees.add(ct);	
		}
	}
	
	// GETTERS
	
	public CommonTree getParentExpression() {
		return parentExpression;
	}
	
	public ArrayList<CommonTree> getTrees() {
		return trees;
	}
	
}
