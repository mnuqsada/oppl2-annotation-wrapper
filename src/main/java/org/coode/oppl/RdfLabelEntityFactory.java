package org.coode.oppl;

import org.coode.oppl.entity.OWLEntityCreationException;
import org.coode.oppl.entity.OWLEntityCreationSet;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiomChange;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyChange;

/**
 * @author Manuel Quesada
 */
public class RdfLabelEntityFactory extends EntityFactory {

	private final OPPLAbstractFactory factory;
	
    /**
     * @param f
     *        f
     */
    public RdfLabelEntityFactory(OPPLAbstractFactory f) {
        super(f);
        this.factory = f;
    }
  
    @Override
    public <T extends OWLEntity> OWLEntityCreationSet<T> createOWLEntity(Class<T> type, String shortName, IRI baseIRI) throws OWLEntityCreationException {
    	OWLEntityCreationSet<T> result = super.createOWLEntity(type, shortName, baseIRI);
    	// Add changes to the extra changes variable.
    	if ( factory instanceof RdfLabelOPPLFactory ) {
    		RdfLabelOPPLFactory rdfLabelFactory = (RdfLabelOPPLFactory) factory;
    		for ( OWLOntologyChange change : result.getOntologyChanges() ) {
    			if ( change instanceof OWLAxiomChange ) {
    				rdfLabelFactory.getCreateVariableAdditionalChanges().add( (OWLAxiomChange) change );
    			}
    		}
    	}
        return result;
    }

}
