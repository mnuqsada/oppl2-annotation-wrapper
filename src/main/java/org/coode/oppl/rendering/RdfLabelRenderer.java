package org.coode.oppl.rendering;

import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.ShortFormProvider;

public class RdfLabelRenderer implements OWLObjectRenderer {

	private RdfLabelShortFormProvider ren;
	
	public RdfLabelRenderer (OWLOntologyManager man) {
		ren = new RdfLabelShortFormProvider(man);
	}
	
	@Override
	public String render(OWLObject arg0) {
		if ( arg0 instanceof OWLEntity ) {
			return ren.getShortForm((OWLEntity)arg0);
		}
		else {
			throw new IllegalStateException("We can not render by labels objects that are not OWLEntity.");
		}
	}
	
	@Override
	public void setShortFormProvider(ShortFormProvider arg0) {
		throw new IllegalStateException("The ShortFormProvider is read only in the RdfLabelRenderer.");
	}
	
}
