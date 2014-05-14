package SQLite;

/**
 * Created by Remi on 05/05/2014.
 */
public class Produits {

    private int id_produit;
    private int id_liste;
    private String nom_produit;


    /*
     *  Constructeur
     */

    public Produits() {}

    public Produits(int id_produit, int id_liste, String nom_produit)
    {
        this.id_liste = id_liste;
        this.nom_produit = nom_produit;
        this.id_produit = id_produit;

    }

    /*
     *  Get / Set id_produits
     */

    public int getId_produit() { return id_produit; }

    public void setId_produit(int id) { this.id_produit = id; }

    /*
     *  Get / Set id_liste
     */

    public int getId_liste() { return id_liste; }

    public void setId_liste(int id) { this.id_liste = id; }

    /*
     *  Get / Set nom_produit
     */

    public String getNom_produit() { return nom_produit; }

    public void setNom_produit(String nom) { this.nom_produit = nom; }


}
