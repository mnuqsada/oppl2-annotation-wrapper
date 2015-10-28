/**
 * Copyright (C) 2008, University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.coode.oppl;

import java.util.List;

import org.coode.oppl.exceptions.RuntimeExceptionHandler;
import org.semanticweb.owlapi.model.OWLAxiomChange;

/**
 * Returns the changes that will occur if the visited OPPL construct is executed
 * 
 * @author Manuel Quesada
 */
public class RdfLabelChangeExtractor extends ChangeExtractor {
	
    public RdfLabelChangeExtractor(RuntimeExceptionHandler runtimeExceptionHandler, boolean considerImportClosure) {
        super(runtimeExceptionHandler, considerImportClosure);
    }

    public RdfLabelChangeExtractor(RuntimeExceptionHandler runtimeExceptionHandler, ExecutionMonitor executionMonitor, boolean considerImportClosure) {
        super(runtimeExceptionHandler, executionMonitor, considerImportClosure);
    }

    @Override
    public List<OWLAxiomChange> visit(OPPLScript script) {
    	List<OWLAxiomChange> toReturn = super.visit(script);
        if ( script.getConstraintSystem().getOPPLFactory() instanceof RdfLabelOPPLFactory ) {
        	toReturn.addAll(((RdfLabelOPPLFactory)script.getConstraintSystem().getOPPLFactory()).getCreateVariableAdditionalChanges());
        }
        return toReturn;
    }

}
