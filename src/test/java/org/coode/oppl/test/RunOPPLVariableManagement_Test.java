package org.coode.oppl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.coode.oppl.AnnotationBasedSymbolTableFactory;
import org.coode.oppl.OPPLParser;
import org.coode.oppl.OPPLScript;
import org.coode.oppl.ParserFactory;
import org.coode.oppl.RdfLabelParserFactory;
import org.coode.oppl.Variable;
import org.coode.oppl.error.parser.MyOPPLParserErrorListener;
import org.coode.parsers.ErrorListener;
import org.coode.parsers.common.SystemErrorEcho;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.BufferingMode;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

import uk.ac.manchester.cs.jfact.JFactReasoner;

public class RunOPPLVariableManagement_Test {

	// EXPECTED VALUES
	private static final int EXPECTED_NUMBER_OF_CLASSES = 8548;
	private static final int EXPECTED_NUMBER_OF_VARIABLES_SCRIPT1 = 1;
	private static final int EXPECTED_UNRECOGNISED_SYMBOLS_NUMBER = 1;
	private static final String EXPECTED_UNRECOGNISED_SYMBOLS_VALUE = "[dmolecular_function]";
	private static final int EXPECTED_ERRORS = 5;
	
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
	public void test_VariableExtraction() throws OWLOntologyStorageException, IOException {
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
		OPPLScript parsedScript = parseScript(opplScriptString, null);
		
		// Extract the variables and print them
		List<Variable<?>> variables = parsedScript.getVariables();
		int i = 1;
		for ( Variable<?> v : variables ) {
			System.out.println("Variable "+i+": "+v);
			i++;
		}
		
		// Check the result
		assertEquals(EXPECTED_NUMBER_OF_VARIABLES_SCRIPT1, variables.size());
		
	}

	@Test
	public void test_ValidateNamedEntities () throws OWLOntologyStorageException, IOException {

		// Define the script
		String opplScriptString = "";
        opplScriptString = "?x:CLASS"+"\n";
		opplScriptString += "SELECT "+"\n";
		opplScriptString += "?x SubClassOf dmolecular_function "+"\n";
		opplScriptString += "BEGIN "+"\n";
		opplScriptString += "ADD ?x SubClassOf binding "+"\n";
		opplScriptString += "END;";
		
		// Create a listener that store all the errors durint the processing of the script
		MyOPPLParserErrorListener errorInParserProcess = new MyOPPLParserErrorListener();
		
		// Parse the script
		OPPLScript parsedScript = parseScript(opplScriptString, errorInParserProcess);
		if ( parsedScript == null ) {

			// Check errors: unrecognised Symbols
			Set<String> unrecognisedSymbolsDuringParsing = errorInParserProcess.getUnrecognisedSymbols();
			if ( unrecognisedSymbolsDuringParsing.size() != 0 ) {
				int i = 1;
				for ( String unrecognisedSymbol : unrecognisedSymbolsDuringParsing ){
					System.out.println("Unrecognised Symbol "+i+": "+unrecognisedSymbol);
					i++;
				}
				assertTrue(unrecognisedSymbolsDuringParsing.size() == EXPECTED_UNRECOGNISED_SYMBOLS_NUMBER);
				assertTrue(unrecognisedSymbolsDuringParsing.toString().compareTo(EXPECTED_UNRECOGNISED_SYMBOLS_VALUE) == 0 );
			}
			
			// Check errors: others
			Integer errors = errorInParserProcess.getNumberOfErrors();
			if ( errors != 0 ) {
				assertTrue(errors == EXPECTED_ERRORS);
			}	
		}
		
	}

	private OPPLScript parseScript (String opplScriptString, ErrorListener errorListener) throws IOException, OWLOntologyStorageException {
		
		// Print the script
		Logger.getAnonymousLogger().info(opplScriptString);
		
		// Create an annotation based symbol table factory for making reference in the OPPL script to 
		// object rdf:labels
		OWLAnnotationProperty rdfLabel = ontology.getOWLOntologyManager().getOWLDataFactory().getRDFSLabel();
		AnnotationBasedSymbolTableFactory abstf = new AnnotationBasedSymbolTableFactory(ontology.getOWLOntologyManager(), Arrays.asList(rdfLabel.getIRI()));
		        
		// Create a parser factory that use rdf:labels
		ParserFactory parserFactory = new RdfLabelParserFactory(ontology.getOWLOntologyManager(), ontology, reasoner);
		
		OPPLParser parser;
		if(errorListener != null ) {
			parser = parserFactory.build(errorListener, abstf);
		}
		else {
			parser = parserFactory.build(new SystemErrorEcho(), abstf);
		}
		
		// Parse the script
		OPPLScript parsed = parser.parse(opplScriptString);
		
		return parsed;
		
	}
	
}
