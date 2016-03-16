package may;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Simple command-line application for checking the well-formedness of a XML
 * file.
 *
 * @author Daniel May
 * @version 20160306.1
 */
public class CheckXML {
	private static Options opt;
	private static CommandLine cl;
	private static HelpFormatter formatter;

	/**
	 * Checks if the given XML file is well-formed.
	 *
	 * @param file
	 *            the XML file to check
	 */
	private static void check(String file) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(true);
		XMLReader reader;
		try {
			SAXParser parser = factory.newSAXParser();
			reader = parser.getXMLReader();
			reader.setErrorHandler(null);
			reader.parse(new InputSource(file));
		} catch (SAXException e) {
			System.err.println("Error in XML File: " + e.getMessage());
			System.exit(0);
		} catch (ParserConfigurationException e) {
			System.err.println("Parser cannot be created: " + e.getMessage());
			System.exit(0);
		} catch (IOException e) {
			System.err.println("IOException occurred: " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Your document is well-formed!");
	}

	/**
	 * setting up the command line options
	 */
	private static void setUpOptions() {
		opt = new Options();
		OptionGroup optG = new OptionGroup();
		optG.addOption(Option.builder("h").desc("displays this message").longOpt("help").build());
		optG.addOption(Option.builder("f").argName("path-to-file").desc("the filepath of XML file").hasArg()
				.longOpt("file").numberOfArgs(1).build());
		optG.setRequired(true);
		opt.addOptionGroup(optG);
	}

	/**
	 * Parse command line arguments. If a ParseException occurs, the error
	 * message and the usage will be printed. The application will be terminated
	 * in that case.
	 *
	 * @param args
	 *            command line arguments
	 */
	private static void parseOptions(String[] args) {
		DefaultParser parser = new DefaultParser();
		try {
			cl = parser.parse(opt, args);
		} catch (ParseException exp) {
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
			printUsage();
			System.exit(-1);
		}
	}

	/**
	 * Simple wrapper method in order to print the help message including the
	 * usage.
	 */
	private static void printUsage() {
		formatter.printHelp("<filename>.jar", opt, true);
	}

	/**
	 * Main function that connects the functions of this application.
	 *
	 * @param args
	 *            the arguments provided by the command line
	 */
	public static void main(String[] args) {
		CheckXML.setUpOptions();
		formatter = new HelpFormatter();
		parseOptions(args);
		if (cl.hasOption('f'))
			CheckXML.check(cl.getOptionValue('f'));
		else if (cl.hasOption('h'))
			printUsage();
	}
}