package SQLite;


/**
 * Created by Remi on 05/05/2014.
 */
public class Liste {

    private int id_liste;
    private String nom_liste;
    private String date_creation;


    /*
     *  Constructeur
     */

    public Liste() {}

    public Liste(int id_liste, String nom_liste, String date_creation)
    {
        this.id_liste = id_liste;
        this.nom_liste = nom_liste;
        this.date_creation = date_creation;

    }

    /*
     *  Get / Set id_liste
     */

    public int getId_liste() { return this.id_liste; }

    public void setId_liste(int id) { this.id_liste = id; }

    /*
     *  Get / Set nom_liste
     */

    public String getNom_liste() { return this.nom_liste; }

    public void setNom_liste(String nom) { this.nom_liste = nom; }

    /*
     *  Get / Set date_creation
     */

    public String getDate_creation() { return this.date_creation; }

    public void setDate_creation(String date_creation) { this.date_creation = date_creation; }

}
