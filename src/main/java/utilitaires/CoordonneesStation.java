package utilitaires;

/**
 * Created by Florine on 04/04/2016.
 */
public class CoordonneesStation extends Coordonnees implements Comparable<CoordonneesStation> {
    private float distance;
    private int id;

    public CoordonneesStation(int id, double lat, double lng)  {
        super(lat,lng);
        this.id = id;
        this.distance = -1;
    }

    public void setDistance(float d){
        this.distance = d;
    }

    public float getDistance(){
        return this.distance;
    }

    public int compareTo(CoordonneesStation cs) {
        return Float.compare(distance, cs.getDistance());
    }

    public String toString(){
        return "station n° "+id
                + super.toString();
    }
}
