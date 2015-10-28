package org.coode.oppl;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

/**
 * @author Manuel Quesada
 */
public class RdfLabelParserFactory extends ParserFactory {

    public RdfLabelParserFactory(OWLOntologyManager ontologyManager, OWLOntology ontology, OWLReasoner reasoner) {
        super(ontologyManager, ontology, reasoner);
    }

    @Override
    public OPPLFactory getOPPLFactory() {
        return new RdfLabelOPPLFactory(getOntologyManager(), getOntology(), getReasoner());
    }
    
}

