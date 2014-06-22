package SQLite;

/**
 * Created by Remi on 05/05/2014.
 */
public class Produits {

    private int id_produit;
    private int id_liste;
    private String nom_produit;
    //private short isChecked = 0;


    /*
     *  Constructeur
     */

    public Produits() {}

    public Produits(int id_prod, int id_list, String nom_prod)
    {
        this.id_liste = id_list;
        this.nom_produit = nom_prod;
        this.id_produit = id_prod;

    }

    /*
     *  Get / Set id_produits
     */

    public int getId_produit() { return this.id_produit; }

    public void setId_produit(int id) { this.id_produit = id; }

    /*
     *  Get / Set id_liste
     */

    public int getId_liste() { return this.id_liste; }

    public void setId_liste(int id) { this.id_liste = id; }

    /*
     *  Get / Set nom_produit
     */

    public String getNom_produit() { return this.nom_produit; }

    public void setNom_produit(String nom) { this.nom_produit = nom; }

    /*
     *  Get / Set isChecked


    public short getisChecked() { return this.isChecked; }

    public void setisChecked(short isChecked) { this.isChecked = isChecked; }

    public void unsetIsChecked(short isChecked) { this.isChecked = 0; }
    */
}
