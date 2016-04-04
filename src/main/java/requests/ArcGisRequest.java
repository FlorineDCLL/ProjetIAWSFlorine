package requests;

import javax.json.*;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;

import static javax.xml.bind.DatatypeConverter.parseString;

/**
 * Created by Yvan on 02/04/2016.
 */
public class ArcGisRequest {
    private static final String API_URI = "http://sampleserver6.arcgisonline.com/arcgis/rest/services/Utilities/Geometry/GeometryServer";

    /**
     * Calcul la distance à vol d'oiseau entre 2 points
     * @return distance
     */
    public static String getDistance() {
        Client c = ClientBuilder.newClient();
        final WebTarget wt = c.register(JsonContentTypeResponseFilter.class).target(API_URI);
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
        formData.add("f","json");
        formData.add("sr","4326");
        formData.add("distanceUnit","9036");
        formData.add("geometry1", "{geometryType:esriGeometryPoint,geometry:{x:43.604,y:1.444}}");
        formData.add("geometry2", "{geometryType:esriGeometryPoint,geometry:{x:48.856,y:2.352}}");
        formData.add("geodesic","true");
        System.out.println(formData);
        try {
            JsonObject result;
             result = wt.path("distance").request(MediaType.APPLICATION_JSON).post(Entity.form(formData),JsonObject.class);
            return parseString(result.getJsonNumber("distance").toString());
        } catch (InternalServerErrorException e) {
            System.err.println("Réponse HTTP " + e.getResponse().getStatus());
            return "";
        }
    }
    /*
    Calcul la distance à vol d'oiseau entre 1 point et une liste de stations
     */
    public static JsonArray getLength(ArrayList<CoordonneesStation> coordonnees, Coordonnees depart) {
        Client c = ClientBuilder.newClient();
        final WebTarget wt = c.register(JsonContentTypeResponseFilter.class).target(API_URI);
        /*
        paramètres de la requete
         */
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
        formData.add("f","json");
        formData.add("sr","4269");
        formData.add("lengthUnit","9036");
        formData.add("geodesic","true");
        formData.add("calculationType","preserveShape");
        String paths = "[";
        for(int i=0;i<coordonnees.size();++i) {
            paths+="{paths:[[["+depart.getLat()+","+depart.getLng()+"],["+(coordonnees.get(i)).getLat()+","+coordonnees.get(i).getLat()+"]]]}";
            if(i!=coordonnees.size()-1)
                paths+=",";
        }
        paths+="]";
        formData.add("polylines",paths);
        /*
        Requete
         */
        try {
            JsonObject result;
            result = wt.path("lengths").request(MediaType.APPLICATION_JSON).post(Entity.form(formData),JsonObject.class);
            return result.getJsonArray("lengths");
        } catch (InternalServerErrorException e) {
            System.err.println("Réponse HTTP " + e.getResponse().getStatus());
            return null;
        }
    }


    public static  void main(String [] args){

        System.out.println("Distance = " +getDistance()+"   KM");
    }
}


