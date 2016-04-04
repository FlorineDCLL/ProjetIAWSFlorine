package requests;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

/**
 * Created by Florine on 29/03/2016.
 */
public class JCDecauxRequest {

    private static final String API_URI = "https://api.jcdecaux.com/vls/v1/";
    private static final String API_KEY = "a541682318252681be6f92750b808cef9e2340d8";

    private ArrayList listeId = new ArrayList();

    /**
     *  Renvoie le nombre de vélos disponibles à la station idStation
     * @param idStation la station à considérer
     * @return (int) le nombre de vélos disponibles à la station idStation
     */
    public static int getNbVelosDispos(int idStation) {
        final Client c = ClientBuilder.newClient();
        final WebTarget wt = c.target(API_URI);
        try {
            final JsonObject result;
                result = wt.path("stations").path(idStation+"").queryParam("contract","Toulouse").queryParam("apiKey",API_KEY)
                    .request(MediaType.APPLICATION_JSON).get(JsonObject.class);
            return Integer.parseInt(result.getJsonNumber("available_bikes").toString());
        } catch (InternalServerErrorException e) {
            System.err.println("Réponse HTTP " + e.getResponse().getStatus());
            return 0;
        }
    }

    /**
     * Renvoie le nombre de places disponibles à la station idStation
     * @param idStation la station à considérer
     * @return (int) le nombre de places (vides) disponibles à la station idStation
     */
    public int getNbPlacesDispos(int idStation) {
        final Client c = ClientBuilder.newClient();
        final WebTarget wt = c.target(API_URI);
        try {
            final JsonObject result;
            result = wt.path("stations").path(idStation+"").queryParam("contract","Toulouse").queryParam("apiKey",API_KEY)
                    .request(MediaType.APPLICATION_JSON).get(JsonObject.class);
            return Integer.parseInt(result.getJsonNumber("available_bike_stands").toString());
        } catch (InternalServerErrorException e) {
            System.err.println("Réponse HTTP " + e.getResponse().getStatus());
            return 0;
        }
    }

    /**
     * Retourne les coordonnées de toutes les stations de Toulouse
     * @return (ArrayList<CoordonneesStation>) : une collection contenant, pour chaque station, son id, sa latitude
     * , longitude, et la distance par rapport à l'addresse (null pour le moment)
     */
    public static ArrayList<CoordonneesStation> getStations(){
        ArrayList<CoordonneesStation> coordonnees = new ArrayList<CoordonneesStation>();
        final Client c = ClientBuilder.newClient();
        final WebTarget wt = c.target(API_URI);
        try {
            final JsonArray result;
            result =  wt.path("stations").queryParam("contract","Toulouse").queryParam("apiKey",API_KEY)
                    .request(MediaType.APPLICATION_JSON).get(JsonArray.class);
            for(int i =0; i<result.size();i++){
                JsonObject station = result.getJsonObject(i);
                int id = Integer.parseInt(station.getJsonNumber("number").toString());
                JsonObject position = station.getJsonObject("position");
                float lat = Float.parseFloat(position.getJsonNumber("lat").toString());
                float lng = Float.parseFloat(position.getJsonNumber("lng").toString());
                coordonnees.add(new CoordonneesStation(id,lat,lng));
            }
            return coordonnees;
        } catch (InternalServerErrorException e) {
            System.err.println("Réponse HTTP " + e.getResponse().getStatus());
            return null;
        }
    }


    /**
     * Il faudra d'abord transformer l'adresse (String) en coordonnées GPS
     * @return
     */
    public String get3StationsNonVidesLesPlusProches(){
        return "";

    }

    public static  void main(String [] args) {
    }
}
