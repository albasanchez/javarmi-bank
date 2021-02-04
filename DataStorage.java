import java.io.File;
// import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
// import javax.xml.xpath.XPath;
// import javax.xml.xpath.XPathConstants;
// import javax.xml.xpath.XPathExpressionException;
// import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
// import org.w3c.dom.Node;
// import org.w3c.dom.NodeList;
// import org.xml.sax.SAXException;

public class DataStorage {
  static DocumentBuilderFactory documentBuilderFactory;
  static DocumentBuilder documentBuilder;
  private String file_name = "data/data.xml";
  
  public DataStorage() {
    try {
      documentBuilderFactory = DocumentBuilderFactory.newInstance();
      documentBuilderFactory.setNamespaceAware(false);
      documentBuilderFactory.setValidating(false);
      documentBuilderFactory.setFeature("http://xml.org/sax/features/namespaces", false);
      documentBuilderFactory.setFeature("http://xml.org/sax/features/validation", false);
      documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
      documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

      documentBuilder = documentBuilderFactory.newDocumentBuilder();

    } catch (ParserConfigurationException ex) {
      Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private Document createBlankDocument(File file) {
    Document xml_document = documentBuilder.newDocument();
    Element rootElement = xml_document.createElement("root");
    xml_document.appendChild(rootElement);
    writeInFile(xml_document, file);
    return xml_document;
  }

  private void writeInFile(Document document, File file) {
    try {
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(document);
      StreamResult result = new StreamResult(file);
      transformer.transform(source, result);
    } catch (TransformerException tfe) {
      tfe.printStackTrace();
    }
  }

  public void registerUser(User user) {
    Document xml_document;
    File file = new File(file_name);

    try {
      xml_document = documentBuilder.parse(file);
    } catch (Exception ex) {
      xml_document = createBlankDocument(file);
    }

    Element root = xml_document.getDocumentElement();
    Element xml_user = xml_document.createElement("user");
    
    Attr attr_id = xml_document.createAttribute("id");
    attr_id.setValue(user.document_id);
    xml_user.setAttributeNode(attr_id);
    Attr attr_name = xml_document.createAttribute("name");
    attr_name.setValue(user.name);
    xml_user.setAttributeNode(attr_name);
    Attr attr_username = xml_document.createAttribute("username");
    attr_username.setValue(user.username);
    xml_user.setAttributeNode(attr_username);
    Attr attr_password = xml_document.createAttribute("password");
    attr_password.setValue(user.password);
    xml_user.setAttributeNode(attr_password);
    root.appendChild(xml_user);
    
    writeInFile(xml_document, file);
  }
}
