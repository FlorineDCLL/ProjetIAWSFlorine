package utilitaires;

/**
 * Created by Yvan on 02/04/2016.
 */
public class Coordonnees {
    private double lat;
    private double lng;

    public Coordonnees(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public String toString(){
        return
                " lat : "+lat
                +"\n lng : "+lng+"\n";
    }
}
