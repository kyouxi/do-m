package mydom.parse;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;

public class XPathParse {
    private Document document;
    private XPath xPath;

    public XPathParse(){
        this.xPath = XPathFactory.newInstance().newXPath();
    }

    public XPathParse(String source){
        this.document = createDoc(source);
        this.xPath = XPathFactory.newInstance().newXPath();
    }

    public String parseString(String expression){
        return parseString(document,expression);
    }
    public  String  parseString(Document document , String expression) {
        String result = (String)parse(document, expression,XPathConstants.STRING);
        return result;
    }

    public Node parseNode(String expression){
        return parseNode(document,expression);
    }
    public Node parseNode(Document document,String expression){
        Node node = (Node)parse(document,expression,XPathConstants.NODE);
        return node;
    }

    public  Object parse(Document document, String expression, QName returnType){
        try{
            return xPath.evaluate(expression,document, returnType);
        }catch (Exception e){
            throw new RuntimeException("Error evaluating XPath.  Cause: ",e);
        }
    }

    public Document createDoc(String sourcePath){
        File file = new File(sourcePath);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            return document;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
