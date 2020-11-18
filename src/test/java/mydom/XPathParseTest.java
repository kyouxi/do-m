package mydom;

import mydom.parse.XPathParse;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XPathParseTest {
    @Test
    public void test(){
        Document document = new XPathParse().createDoc("src\\main\\resources\\book.xml");
        NodeList nodeList = document.getElementsByTagName("book");
        System.out.println(nodeList.toString());
        System.out.println(nodeList.item(0).getFirstChild().getNodeValue().trim());
        System.out.println(nodeList.item(0).getAttributes().getNamedItem("category").getNodeValue());
        System.out.println(nodeList.item(0).getChildNodes().item(1).getFirstChild().getNodeValue().trim());
        System.out.println(nodeList.item(0).getChildNodes().item(2).getNodeValue().trim());
        System.out.println(document.getElementsByTagName("title").item(0).getFirstChild().getNodeValue().trim());

        XPathParse xPathParse = new XPathParse("src\\main\\resources\\book.xml");
        String result = xPathParse.parseString("/book/title").trim();
        System.out.println(result);
        String result2 = xPathParse.parseString("/book/title/@id").trim();
        System.out.println(result2);
        Node node = xPathParse.parseNode("/book/@category");
        System.out.println(node.getTextContent());
    }
}
