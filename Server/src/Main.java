import java.io.File;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import server.Server;

/**
 * Represents the start of the server.
 * 
 * @author Rune Krauss
 * @author Steffen Gerken
 * @author Niels Arbatschat
 */
public class Main {
	/** Input of the user */
	private static Scanner input;

	/** Path to the XML file */
	private static final String XML_FILE = "level.xml";

	/**
	 * Starts and stops the server.
	 * 
	 * @param args
	 *            Command line parameters
	 */
	public static void main(String args[]) {
		Server server = new Server(55555);
		File xmlFile = new File(XML_FILE);
		if (!xmlFile.exists()) {
			createXMLFile();
		}
		new Thread(server).start();
		String stop;
		input = new Scanner(System.in);
		while (!(stop = input.nextLine()).equals("stop")) {
			System.out.println("You entered '" + stop + "'.");
			System.out.println("Input 'stop' to close the server.");
		}
		server.stop();
	}

	/**
	 * Creates the XML file.
	 */
	private static void createXMLFile() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("levels");
			doc.appendChild(rootElement);
			Element level = doc.createElement("level");
			rootElement.appendChild(level);
			Attr attr = doc.createAttribute("id");
			attr.setValue("level1");
			level.setAttributeNode(attr);
			Element level2 = doc.createElement("level");
			rootElement.appendChild(level2);
			Attr attr2 = doc.createAttribute("id");
			attr2.setValue("level2");
			level2.setAttributeNode(attr2);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(XML_FILE));
			transformer.transform(source, result);
			System.out.println("XML file was created...");
		} catch (ParserConfigurationException | TransformerException e) {
			System.out.print(e.getMessage());
		}
	}
}
