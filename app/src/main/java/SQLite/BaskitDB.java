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

    private static final String P_COL_IS_CHECKED = "IS_CHECKED";
    private static final int P_NUM_COL_IS_CHECKED = 3;

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

    // Lors de la suppression d'une liste
    // Supprimer également les produits contenus dans cette liste
    public void deleteListe(int id) {
        deleteProduitsWithList(id);

        Cursor c2 = bdd.rawQuery("DELETE FROM " + TABLE_LISTE + " WHERE " + L_COL_ID_LISTE + " = " + id, null);
        c2.moveToFirst();
        while(!c2.isAfterLast()) { c2.moveToNext(); }
        c2.close();
        //return bdd.delete(TABLE_LISTE, L_COL_ID_LISTE + " = " + id, null);
    }

    public void deleteProduitsWithList(int id_liste) {
        Cursor c = bdd.rawQuery("DELETE FROM " + TABLE_PRODUITS + " WHERE " + P_COL_ID_LISTE + " = " + id_liste, null);
        c.moveToFirst();
        while(!c.isAfterLast()) { c.moveToNext(); }
        c.close();
    }

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
        values.put(P_COL_IS_CHECKED, false);
        return bdd.insert(TABLE_PRODUITS, null, values);
    }

    public int deleteProduit(int id) { return bdd.delete(TABLE_PRODUITS, P_COL_ID_PRODUIT + " = " + id, null); }

    public void checkProduit(int id_liste, int id_prod) {

        Cursor c = bdd.rawQuery("UPDATE " + TABLE_PRODUITS + " SET " + P_COL_IS_CHECKED + " = 1 " +
                                " WHERE " + P_COL_ID_LISTE + " = " + id_liste +
                                " AND " + P_COL_ID_PRODUIT + " = " + id_prod, null);
        c.moveToFirst();
        while(!c.isAfterLast()) { c.moveToNext(); }
        c.close();

    }

    public void uncheckProduit(int id_liste, int id_prod) {

        Cursor c = bdd.rawQuery("UPDATE " + TABLE_PRODUITS + " SET " + P_COL_IS_CHECKED + " = 0 " +
                " WHERE " + P_COL_ID_LISTE + " = " + id_liste +
                " AND " + P_COL_ID_PRODUIT + " = " + id_prod, null);
        c.moveToFirst();
        while(!c.isAfterLast()) { c.moveToNext(); }
        c.close();

    }

    public List<Produits> getProduitsWithIdListe(int id_liste)
    {

        Cursor c = this.bdd.rawQuery("SELECT * FROM " + TABLE_PRODUITS + " WHERE " + P_COL_ID_LISTE + " = " + id_liste, null);
        c.moveToFirst();
        List<Produits> listeProduits = new LinkedList<Produits>();
        while(!c.isAfterLast())
        {
            Produits produit = new Produits();
            produit.setId_produit(c.getInt(P_NUM_COL_ID_PRODUIT));
            produit.setNom_produit(c.getString(P_NUM_COL_NOM_PRODUIT));
            produit.setId_liste(c.getInt(P_NUM_COL_ID_LISTE));
            produit.setisChecked(c.getShort(P_NUM_COL_IS_CHECKED));
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