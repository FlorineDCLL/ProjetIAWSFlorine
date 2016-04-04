package requests;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



/**
 * Created by Camille on 02/04/2016.
 */
public class OpenWeatherMapRequest {
    /**
     * Interroge le web sevice OpenMapWeather pour le bulletin météo de la ville de Toulouse
     * Génère un fichier bulletinMeteo.xml dans le répertoire courrant
     */
    public static void getBulletinMeteo() {
        // Envoi de la requête au service web OpenMapWeather
        Client c = ClientBuilder.newClient();
        WebTarget wt = c.target("http://api.openweathermap.org/data/2.5/forecast/city?id=2972315&mode=xml&appid=4cd343650c5f762146e50e3c53525e8b");
        String[] tab = new String[1];
        // On récupère du XML
        tab[0] = MediaType.APPLICATION_XML;
        File pluviometrie3h = wt.request(tab).get(File.class);
        SAXBuilder saxBuilder = new SAXBuilder();
        try {
            Document document = saxBuilder.build(pluviometrie3h);
            // On crée un fichier bulletinMeteo.xml et on y met le contenu de la réponse
            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(document, new FileWriter("bulletinMeteo.xml"));
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Evalue la pluviometrie grace à une requete XPath sur le document bulletinMeteo.xml
     * @return pluviometrie : float
     */
    public static float chausseeMouillee () {
        // Récupération du document bulletinMeteo.xml du répertoire courrant
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        org.w3c.dom.Document doc = null;
        Node pluviometrie = null;
        try {
            doc = domFactory.newDocumentBuilder().parse("./bulletinMeteo.xml");
            // Création et évaluation de la requête XPath sur la pluviométrie
            XPath xpath = XPathFactory.newInstance().newXPath();
            String xPathStr = "/weatherdata/forecast/time/precipitation/@value";
            // On s'arrête au premier résultat trouvé dans le fichier xml
            pluviometrie = ((NodeList) xpath.compile(xPathStr).evaluate(doc, XPathConstants.NODESET)).item(0);
        }
        catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        catch (SAXException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return Float.parseFloat(pluviometrie.getNodeValue());
    }
    public static void main (String args []){
        getBulletinMeteo();
        System.out.println(chausseeMouillee());

    }
}
