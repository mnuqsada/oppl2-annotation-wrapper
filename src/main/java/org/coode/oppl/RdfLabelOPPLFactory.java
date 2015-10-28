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

import java.util.ArrayList;
import java.util.List;

import org.coode.oppl.rendering.ManchesterSyntaxRenderer;
import org.coode.oppl.rendering.RdfLabelRendererVisitor;
import org.coode.oppl.rendering.RdfLabelShortFormProvider;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.model.OWLAxiomChange;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;

/**
 * @author Manuel Quesada
 */
public class RdfLabelOPPLFactory extends OPPLFactory {
	
	private final List<OWLAxiomChange> createVariableAdditionalChanges;
	
    public RdfLabelOPPLFactory(OWLOntologyManager ontologyManager, OWLOntology ontology, OWLReasoner reasoner) {
        super(ontologyManager, ontology, reasoner);
        this.createVariableAdditionalChanges = new ArrayList<OWLAxiomChange>();
    }

    @SuppressWarnings("unused")
	private OWLEntityChecker defaultEntityChecker() {
        BidirectionalShortFormProviderAdapter bshp = 
        		new BidirectionalShortFormProviderAdapter(
        				super.getOntologyManager().getOntologies(), 
        				new OPPLShortFormProvider(new RdfLabelShortFormProvider(super.getOntologyManager())));
        // XXX fix for missing Thing
        bshp.add(super.getOntologyManager().getOWLDataFactory().getOWLThing());
        bshp.add(super.getOntologyManager().getOWLDataFactory().getOWLNothing());
        return new ShortFormEntityChecker(bshp);
    }

    @Override
    public ManchesterSyntaxRenderer getManchesterSyntaxRenderer(ConstraintSystem cs) {
        return new RdfLabelRendererVisitor(new RdfLabelShortFormProvider(getOntologyManager()));
    }
    
    @Override
    public org.coode.oppl.entity.OWLEntityFactory getOWLEntityFactory() {
        return new RdfLabelEntityFactory(this);
    }
    
    // GETTERS AND SETTERS
    
    public List<OWLAxiomChange> getCreateVariableAdditionalChanges() {
		return createVariableAdditionalChanges;
	}
    
}



