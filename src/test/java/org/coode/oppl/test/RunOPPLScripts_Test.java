package org.coode.oppl.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;

import org.coode.oppl.AnnotationBasedSymbolTableFactory;
import org.coode.oppl.ChangeExtractor;
import org.coode.oppl.OPPLParser;
import org.coode.oppl.OPPLScript;
import org.coode.oppl.ParserFactory;
import org.coode.oppl.RdfLabelChangeExtractor;
import org.coode.oppl.RdfLabelParserFactory;
import org.coode.oppl.exceptions.RuntimeExceptionHandler;
import org.coode.oppl.rendering.ManchesterSyntaxRenderer;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxOntologyFormat;
import org.coode.parsers.common.SystemErrorEcho;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.AddOntologyAnnotation;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomChange;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChangeVisitor;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLRuntimeException;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.model.RemoveImport;
import org.semanticweb.owlapi.model.RemoveOntologyAnnotation;
import org.semanticweb.owlapi.model.SetOntologyID;
import org.semanticweb.owlapi.reasoner.BufferingMode;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

import uk.ac.manchester.cs.jfact.JFactReasoner;

public class RunOPPLScripts_Test {

	// EXPECTED VALUES
	private static final int EXPECTED_NUMBER_OF_CLASSES = 8548;
	private static final int EXPECTED_CHANGES_SCRIPT_1 = 1;
	private static final int EXPECTED_CHANGES_SCRIPT_2 = 1;
	private static final int EXPECTED_CHANGES_SCRIPT_3 = 7764;
	
	// CONFIGURATION PARAMETERS
	private static final Boolean PRINT_CHANGES = false;
	
	// LOCAL VARIABLES
	private OWLOntologyManager manager = null;
	private OWLOntology ontology = null;
	private OWLReasoner reasoner = null;
	
	@Before
	public void loadOntology () throws OWLOntologyCreationException {
		
		// Create the manager and load the ontology
		manager = OWLManager.createOWLOntologyManager();
		ontology = manager.loadOntology(IRI.create(URI.create("http://miuras.inf.um.es/~mfoppl/mfEKAW.owl")));
		org.junit.Assert.assertTrue(EXPECTED_NUMBER_OF_CLASSES == ontology.getClassesInSignature().size());
		
		// Create JFact reasoner
		OWLReasonerConfiguration config = new SimpleConfiguration(50000);
		reasoner = new JFactReasoner(ontology, config, BufferingMode.BUFFERING);
		
		// Classify
		Logger.getAnonymousLogger().info("Classifying...");
		long initTime, finalTime;
		initTime = System.currentTimeMillis();
		reasoner.precomputeInferences(InferenceType.values());
		finalTime = System.currentTimeMillis();
		Logger.getAnonymousLogger().info("\t"+(finalTime-initTime)+" msecs");
		
		// Check inconsistency
		Node<OWLClass> inconsistentClasses = reasoner.getUnsatisfiableClasses();
		if ( inconsistentClasses.getSize() > 1 ) {
			Logger.getAnonymousLogger().severe("The ontology has unsatisfiable classes so we stop the Oquare metrics calculation");
			for ( OWLClass unsatisfiableClasses : inconsistentClasses.getEntities()) {
				Logger.getAnonymousLogger().severe("\t"+unsatisfiableClasses);
			}
			org.junit.Assert.assertTrue(false);
		}
	}
	
	@After
	public void cleanOntology () {
		manager = null;
		ontology = null;
		reasoner.dispose();
		reasoner = null;
		System.gc();
	}
	
	@Test
	public void script_1() throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {
		
		// Define the script
        String opplScriptString = "";
        opplScriptString = "?x:CLASS"+"\n";
		opplScriptString += "SELECT "+"\n";
		opplScriptString += "?x SubClassOf molecular_function "+"\n";
		opplScriptString += "WHERE "+"\n";
		opplScriptString += "?x Match(\"hormone receptor binding\")"+"\n";
		opplScriptString += "BEGIN "+"\n";
		opplScriptString += "ADD ?x SubClassOf binding "+"\n";
		opplScriptString += "END;";
		
		// Execute the script
		Integer numChanges = executeOPPLScript(opplScriptString);
		org.junit.Assert.assertTrue(EXPECTED_CHANGES_SCRIPT_1 == numChanges);
		        
    }
	
	@Test
	public void script_2() throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {
		
		// Define the script
        String opplScriptString = "";
        opplScriptString = "?x:CLASS=Match(\"hormone receptor binding\")"+"\n";
		opplScriptString += "SELECT "+"\n";
		opplScriptString += "?x SubClassOf molecular_function "+"\n";
		opplScriptString += "WHERE "+"\n";
		opplScriptString += "?x Match(\"hormone receptor binding\")"+"\n";
		opplScriptString += "BEGIN "+"\n";
		opplScriptString += "ADD ?x SubClassOf binding"+"\n";
		opplScriptString += "END;";
		
		// Execute the script
		Integer numChanges = executeOPPLScript(opplScriptString);
		org.junit.Assert.assertTrue(EXPECTED_CHANGES_SCRIPT_2 == numChanges);
		        
    }
	
	@Test
	public void script_3() throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {
		
		// Define the script
        String opplScriptString = "";
        opplScriptString =  "?x:CLASS=Match(\"(?i)([\\(\\)\\, a-zA-Z0-9_\\-]+) binding\"),"+"\n";
        opplScriptString += "?y:CLASS=create(?x.GROUPS(1))"+"\n";
        opplScriptString += "SELECT"+"\n";
        opplScriptString += "?x subClassOf binding"+"\n";
        opplScriptString += "BEGIN"+"\n";
        opplScriptString += "ADD ?x subClassOf molecular_function,"+"\n";
        opplScriptString += "ADD ?y subClassOf Thing,"+"\n";
        opplScriptString += "ADD ?y subClassOf enables some (!binds some ?x)"+"\n";
        opplScriptString += "END;"+"\n";
		
		// Execute the script
		Integer numChanges = executeOPPLScript(opplScriptString);
		org.junit.Assert.assertTrue(EXPECTED_CHANGES_SCRIPT_3 == numChanges);
		        
    }
	
	/**
	 * This script references to URI fragments so the OPPL-Annotation-Wrapper does not process it.
	 * 
	 * @throws OWLOntologyCreationException
	 * @throws IOException
	 * @throws OWLOntologyStorageException
	 */
	@Test
	public void script_4() throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {
		
		// Define the script
        String opplScriptString = "";
        opplScriptString += "?x:CLASS=MATCH(\".*GO_0005131\"),"+"\n";
        opplScriptString += "?y:CLASS=MATCH(\".*GO_0005131\")"+"\n";
        opplScriptString += "SELECT"+"\n";
        opplScriptString += "?x subClassOf molecular_function,"+"\n";
        opplScriptString += "?y subClassOf molecular_function"+"\n";
        opplScriptString += "WHERE ?x MATCH(\".*GO_0005131\") , ?y MATCH(\".*GO_0005131\")"+"\n";
        opplScriptString += "BEGIN"+"\n";
        opplScriptString += "ADD ?x subClassOf ?y"+"\n";
        opplScriptString += "END;"+"\n";

		// Execute the script
        try {
        	executeOPPLScript(opplScriptString);
        }
        catch ( IllegalStateException ex ) {
        	org.junit.Assert.assertTrue("The OPPL script could not be parsed.".compareTo(ex.getMessage()) == 0 );
        }
		        
    }
	
	// PRIVATE METHODS
	
	private Integer executeOPPLScript (String opplScriptString) throws IOException, OWLOntologyStorageException {
		
		// Print the script
		Logger.getAnonymousLogger().info(opplScriptString);
		
		// Create an annotation based symbol table factory for making reference in the OPPL script to 
		// object rdf:labels
		OWLAnnotationProperty rdfLabel = ontology.getOWLOntologyManager().getOWLDataFactory().getRDFSLabel();
		AnnotationBasedSymbolTableFactory abstf = new AnnotationBasedSymbolTableFactory(ontology.getOWLOntologyManager(), Arrays.asList(rdfLabel.getIRI()));
		        
		// Create a parser factory that use rdf:labels
		ParserFactory parserFactory = new RdfLabelParserFactory(ontology.getOWLOntologyManager(), ontology, reasoner);
		OPPLParser parser = parserFactory.build(new SystemErrorEcho(), abstf);

		// Parse the script
		OPPLScript parsed = parser.parse(opplScriptString);
		if ( parsed == null ) { 
			throw new IllegalStateException("The OPPL script could not be parsed.");
		}

		        
		// Define a change extractor and use the visitor pattern for obtaining 
		ChangeExtractor extractor = new RdfLabelChangeExtractor(new MyRuntimeExceptionHandler(), true);
		List<OWLAxiomChange> changes = extractor.visit(parsed);
		
		// Print the changes is enable
		if ( PRINT_CHANGES ) {
			for (OWLAxiomChange owlAxiomChange : changes) {
				/* Be careful, this name can mislead as this a rdf:label render */
				final ManchesterSyntaxRenderer renderer = parserFactory.getOPPLFactory().getManchesterSyntaxRenderer(parsed.getConstraintSystem());
				owlAxiomChange.accept(new OWLOntologyChangeVisitor() {
					@Override
					public void visit(RemoveOntologyAnnotation change) { System.out.println(change); }
					@Override
					public void visit(AddOntologyAnnotation change) { System.out.println(change); }
					@Override
					public void visit(RemoveImport change) { System.out.println(change); }
					@Override
					public void visit(AddImport change) { System.out.println(change); }
					@Override
					public void visit(SetOntologyID change) { System.out.println(change); }
					@Override
					public void visit(RemoveAxiom change) {
						OWLAxiom axiom = change.getAxiom();
						axiom.accept(renderer);
						System.out.println(String.format("REMOVE %s", renderer));
					}
					@Override
					public void visit(AddAxiom change) {
						OWLAxiom axiom = change.getAxiom();
						axiom.accept(renderer);
						System.out.println(String.format("ADD %s", renderer));
					}
				});
			}
		}
		
		// Apply the changes in the ontology
		ontology.getOWLOntologyManager().applyChanges(changes);
		
		// Save the modified ontology
		File tmpFile = File.createTempFile("ontology", ".owl");
		ontology.getOWLOntologyManager().saveOntology(ontology, new ManchesterOWLSyntaxOntologyFormat(), new FileOutputStream(tmpFile));
		
		// Print information about the changes
		Logger.getAnonymousLogger().info("Number of changes: "+changes.size());
		Logger.getAnonymousLogger().info("Modified ontology: "+tmpFile.getPath());
		
		return changes.size();
		
	}
	
}

class MyRuntimeExceptionHandler implements RuntimeExceptionHandler {
	@Override
	public void handlePatternSyntaxExcpetion(PatternSyntaxException e) {
		e.printStackTrace();
	}

	@Override
	public void handleOWLRuntimeException(OWLRuntimeException e) {
		e.printStackTrace();
	}

	@Override
	public void handleException(RuntimeException e) {
		e.printStackTrace();
	}
}
