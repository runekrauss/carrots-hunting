package server;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;

/**
 * Represents the client of this game.
 * 
 * @author Rune Krauss
 * @author Steffen Gerken
 * @author Niels Arbatschat
 */
public class Handler implements Runnable {

	/** Client socket regarding the server */
	protected Socket clientSocket;

	/** Path to the XML file */
	private final String XML_FILE = "level.xml";

	/** Regex regarding the commands */
	private final String REGEX = "^(get|add){1}\\s(level\\d)(\\s.*)?$";

	/**
	 * Constructor of the client which initializes the attributes.
	 * 
	 * @param clientSocket
	 *            Client socket regarding the server
	 * @param welcomeMessage
	 *            Welcome message of the server
	 */
	public Handler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	/**
	 * Here, Program code is executed in parallel. Gets input and output stream
	 * and writes a message to the client.
	 */
	public void run() {
		try {
			String timestamp = new SimpleDateFormat("HH:mm:ss, dd.MM.yyyy").format(Calendar.getInstance().getTime());
			InputStream input = clientSocket.getInputStream();
			OutputStream output = clientSocket.getOutputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			String command;
			if ((command = br.readLine().trim()) != null) {
				String msg = parseInput(command);
				output.write((msg).getBytes());
			}
			System.out.println("Request was processed: " + timestamp + "...");
			br.close();
			output.close();
			input.close();
		} catch (IOException | ParserConfigurationException | SAXException | TransformerException
				| XPathExpressionException | InputMismatchException e) {
			System.out.print(e.getMessage());
		}
	}

	/**
	 * Parses the input with a regex and generates a output message.
	 * 
	 * @param in
	 *            Input from the user
	 * @return Output message
	 * @throws ParserConfigurationException
	 *             Indicates a serious configuration error.
	 * @throws XPathExpressionException
	 *             Represents an error in an XPath expression.
	 * @throws SAXException
	 *             Can contain basic error or warning information from either
	 *             the XML parser or the application.
	 * @throws IOException
	 *             Signals that an I/O exception of some sort has occurred.
	 * @throws TransformerException
	 *             Specifies an exceptional condition that occurred during the
	 *             transformation process.
	 */
	private String parseInput(String command) throws ParserConfigurationException, XPathExpressionException,
			SAXException, IOException, TransformerException {
		Pattern pattern = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(command);
		String commandLevel = null;
		String commandName = null;
		if (matcher.find()) {
			commandLevel = matcher.group(2);
			if (matcher.group(3) != null) {
				commandName = matcher.group(3).trim();
			}
			File xmlFile = new File(XML_FILE);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			final XPath xPath = XPathFactory.newInstance().newXPath();
			final XPathExpression expression = xPath.compile("//level[@id='" + commandLevel + "']//user");
			final NodeList nodeList = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
			if (commandName != null && matcher.group(1).trim().equalsIgnoreCase("add")) {
				boolean userExists = false;
				for (int i = 0; i < nodeList.getLength(); ++i) {
					if (commandName.equals(((Element) nodeList.item(i)).getAttribute("name"))) {
						userExists = true;
						return "The user name already exists.\n";
					}
				}
				if (!userExists) {
					Node level = (Node) xPath.evaluate("//*[@id='" + commandLevel + "']", doc, XPathConstants.NODE);
					Element user = doc.createElement("user");
					user.setAttribute("name", commandName);
					try {
						level.appendChild(user);
					} catch (NullPointerException e) {
						return "The level does not exist.\n";
					}
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					DOMSource domSource = new DOMSource(doc);
					StreamResult streamResult = new StreamResult(new File(XML_FILE));
					transformer.transform(domSource, streamResult);
					return "The user name was accepted.\n";
				}
			} else {
				int nodeListLength = nodeList.getLength();
				return (nodeListLength + "\n");
			}
		} else {
			return "The regular expression did not match with the given commands.\n";
		}
		return null;
	}
}