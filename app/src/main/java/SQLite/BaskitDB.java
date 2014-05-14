package SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Remi on 07/05/2014.
 */
public class BaskitDB {

    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "baskit.db";

    // Table Liste
    private static final String TABLE_LISTE = "TABLE_LISTE";

    private static final String L_COL_ID_LISTE = "ID_LISTE";
    private static final int L_NUM_COL_ID_LISTE = 0;

    private static final String L_COL_NOM_LISTE = "NOM_LISTE";
    private static final int L_NUM_COL_NOM_LISTE = 1;

    private static final String L_COL_DATE_CREATION = "DATE_CREATION";
    private static final int L_NUM_COL_DATE_CREATION = 2;

    // Table Produits
    private static final String TABLE_PRODUITS = "TABLE_PRODUITS";

    private static final String P_COL_ID_PRODUIT = "ID_PRODUIT";
    private static final int P_NUM_COL_ID_PRODUIT = 0;

    private static final String P_COL_NOM_PRODUIT = "NOM_PRODUIT";
    private static final int P_NUM_COL_NOM_PRODUIT = 1;

    private static final String P_COL_ID_LISTE = "ID_LISTE";
    private static final int P_NUM_COL_ID_LISTE = 2;

    private SQLiteDatabase bdd;

    private maBaseSQLite maBase;

    public BaskitDB(Context context) { maBase = new maBaseSQLite(context, NOM_BDD, null, VERSION_BDD); }

    public void open() { bdd = maBase.getWritableDatabase(); }

    public void close() { bdd.close(); }

    public SQLiteDatabase getBDD() { return bdd; }

    /*
     *
     *   Opérations sur la table TABLE_LISTE
     *
     */
    public long insertListe(Liste liste)
    {
        ContentValues values = new ContentValues();
        // On n'ajoute pas l'id, SQLite s'en occupe de lui-même
        values.put(L_COL_NOM_LISTE, liste.getNom_liste());
        values.put(L_COL_DATE_CREATION, String.valueOf(liste.getDate_creation()));

        return bdd.insert(TABLE_LISTE, null, values);
    }

    public int deleteListe(int id) { return bdd.delete(TABLE_LISTE, L_COL_ID_LISTE + " = " + id, null); }

    public long updateListe(int id, Liste liste)
    {
        ContentValues values = new ContentValues();
        values.put(L_COL_NOM_LISTE, liste.getNom_liste());
        values.put(L_COL_DATE_CREATION, String.valueOf(liste.getDate_creation()));

        return bdd.update(TABLE_LISTE, values, L_COL_ID_LISTE + " = " + id, null);
    }

    public List<Liste> getListe()
    {
        Cursor c = bdd.query(TABLE_LISTE, new String[] {L_COL_ID_LISTE, L_COL_NOM_LISTE, L_COL_DATE_CREATION}
                , null, null, null, null, null);
        c.moveToFirst();
        List<Liste> listeList = new LinkedList<Liste>();
        while(!c.isAfterLast())
        {
            Liste liste = new Liste();
            liste.setId_liste(c.getInt(L_NUM_COL_ID_LISTE));
            liste.setNom_liste(c.getString(L_NUM_COL_NOM_LISTE));
            liste.setDate_creation(c.getString(L_NUM_COL_DATE_CREATION));
            listeList.add(liste);
            c.moveToNext();
        }
        c.close();
        return listeList;
    }

    private Liste cursorToListe(Cursor c)
    {
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();

        Liste liste = new Liste();

        liste.setId_liste(c.getInt(L_NUM_COL_ID_LISTE));
        liste.setNom_liste(c.getString(L_NUM_COL_NOM_LISTE));
        liste.setDate_creation(c.getString(L_NUM_COL_DATE_CREATION));

        c.close();

        return liste;
    }

    public Liste getListe(int id)
    {
        Cursor c = bdd.query(TABLE_LISTE, new String[] {L_COL_ID_LISTE, L_COL_NOM_LISTE, L_COL_DATE_CREATION}
                , L_COL_ID_LISTE + " = " + id, null, null, null, null);
        return cursorToListe(c);
    }

    /*
     *
     *   Opérations sur la table TABLE_PRODUITS
     *
     */
    public long insertProduit(Produits produit, int id_liste)
    {
        ContentValues values = new ContentValues();
        // On n'ajoute pas l'id, SQLite s'en occupe de lui-même
        values.put(P_COL_NOM_PRODUIT, produit.getNom_produit());
        values.put(P_COL_ID_LISTE, String.valueOf(id_liste));
        // Test
        return bdd.insert(TABLE_PRODUITS, null, values);
    }

    public int deleteProduit(int id) { return bdd.delete(TABLE_PRODUITS, P_COL_ID_PRODUIT + " = " + id, null); }

    public List<Produits> getProduits()
    {
        Cursor c = bdd.query(TABLE_PRODUITS, new String[] {P_COL_ID_PRODUIT, P_COL_NOM_PRODUIT, P_COL_ID_LISTE}
                , null, null, null, null, null);
        c.moveToFirst();
        List<Produits> listeProduits = new LinkedList<Produits>();
        while(!c.isAfterLast())
        {
            Produits produit = new Produits();
            produit.setId_produit(c.getInt(P_NUM_COL_ID_PRODUIT));
            produit.setNom_produit(c.getString(P_NUM_COL_NOM_PRODUIT));
            produit.setId_produit(c.getInt(P_NUM_COL_ID_LISTE));
            listeProduits.add(produit);
            c.moveToNext();
        }
        c.close();
        return listeProduits;
    }

    private Produits cursorToProduits(Cursor c)
    {
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();

        Produits produits = new Produits();

        produits.setId_produit(c.getInt(P_NUM_COL_ID_PRODUIT));
        produits.setNom_produit(c.getString(P_NUM_COL_NOM_PRODUIT));
        produits.setId_liste(c.getInt(P_NUM_COL_ID_LISTE));

        c.close();

        return produits;
    }

}