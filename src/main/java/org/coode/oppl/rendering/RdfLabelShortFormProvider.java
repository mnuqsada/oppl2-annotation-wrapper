package org.coode.oppl.rendering;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologySetProvider;
import org.semanticweb.owlapi.util.AnnotationValueShortFormProvider;
import org.semanticweb.owlapi.util.IRIShortFormProvider;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

public class RdfLabelShortFormProvider extends AnnotationValueShortFormProvider {

	public static final Map<OWLAnnotationProperty,List<String>> langMap = Collections.emptyMap();
	
	public RdfLabelShortFormProvider(OWLOntologyManager man) {
		super(Arrays.asList(new OWLAnnotationProperty[] {man.getOWLDataFactory().getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI())}), langMap, man);
	}
	
	// HIDDEN METHODS	

	private RdfLabelShortFormProvider(
			OWLOntologySetProvider ontologySetProvider,
			ShortFormProvider alternateShortFormProvider,
			IRIShortFormProvider alternateIRIShortFormProvider,
			List<OWLAnnotationProperty> annotationProperties,
			Map<OWLAnnotationProperty, List<String>> preferredLanguageMap) {
		super(ontologySetProvider, alternateShortFormProvider, alternateIRIShortFormProvider, annotationProperties, preferredLanguageMap);
		throw new IllegalStateException("The method should not be invoked");
	}

	private RdfLabelShortFormProvider(
			List<OWLAnnotationProperty> annotationProperties,
			Map<OWLAnnotationProperty, List<String>> preferredLanguageMap,
			OWLOntologySetProvider ontologySetProvider,
			ShortFormProvider alternateShortFormProvider) {
		super(annotationProperties, preferredLanguageMap, ontologySetProvider, alternateShortFormProvider);
		throw new IllegalStateException("The method should not be invoked");
	}

}
