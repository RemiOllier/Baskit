package com.copiercoller.baskit.app;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import SQLite.BaskitDB;
import SQLite.Liste;
import SQLite.Produits;
import SQLite.ProduitsAdapter;


public class DisplayList extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list);



        Bundle extra = getIntent().getExtras();
        int id_liste = Integer.parseInt(extra.getString("id"));

        //Toast.makeText(this, id_liste + " selected", Toast.LENGTH_LONG).show();

        // On récupère le nom de la liste afin de la mettre en titre de l'activité
        BaskitDB listesDB = new BaskitDB(this);
        listesDB.open();

        Liste liste_recup = listesDB.getListe(id_liste);
        String nom_liste = String.valueOf(liste_recup.getNom_liste());
        this.setTitle(nom_liste);

        if(listesDB.countProduits(id_liste) > 0) {
            List<Produits> listAllProduits = new LinkedList<Produits>();
            listAllProduits = listesDB.getProduitsWithIdListe(id_liste);

            // Création de la ArrayList qui nous permettra de remplir la listView
            ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

            // On déclare la HashMap qui contiendra les informations pour un item
            HashMap<String, String> map;

            for (int i = 0; i < listAllProduits.size(); i++) {
                map = new HashMap<String, String>();
                map.put("nom_produit", listAllProduits.get(i).getNom_produit());
                map.put("id_produit", String.valueOf(listAllProduits.get(i).getId_produit()));
                map.put("id_liste", String.valueOf(id_liste));

                listItem.add(map);
            }


            ProduitsAdapter adapter = new ProduitsAdapter(this, listItem);
            setListAdapter(adapter);
        }
        else
        {
            TextView tv_no_product = (TextView) findViewById(R.id.no_product);
            tv_no_product.setVisibility(View.VISIBLE);
        }
        listesDB.close();

    }

    /******
     *
     *  Suppression d'un produit
     *  Lorsqu'on clique dessus
     *
     */

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        HashMap<String, String> item = (HashMap<String, String>) getListAdapter().getItem(position);

        final int id_prod = Integer.parseInt(item.get("id_produit"));
        int id_liste = Integer.parseInt(item.get("id_liste"));

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Supprimer le produit ?");

        alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                callDeleteProduit(id_prod);

            }
        });

        alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alert.show();


        /*
        Intent i = new Intent(this, DisplayList.class);
        i.putExtra("id", item.get("id_liste"));
        startActivity(i);
        finish();
        */


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_renommer_liste) {
            Bundle extra = getIntent().getExtras();
            callRenommerListe(Integer.parseInt(extra.getString("id")));
        }

        if (id == R.id.action_supprimer) {
            BaskitDB listesDB = new BaskitDB(this);
            listesDB.open();
            Bundle extra = getIntent().getExtras();
            int id_liste = Integer.parseInt(extra.getString("id"));
            listesDB.deleteListe(id_liste);
            listesDB.close();
            Intent i = new Intent(this, ListsActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        if (id == R.id.action_ajout_produit) {
            Bundle extra = getIntent().getExtras();
            final int id_liste = Integer.parseInt(extra.getString("id"));

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Ajouter produit");

            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("Créer", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    String nom_nouveauProduit = String.valueOf(input.getText());

                    if(nom_nouveauProduit.length() > 0) {

                        BaskitDB listesDB = new BaskitDB(getApplicationContext());
                        listesDB.open();
                        Produits nouveauProduit = new Produits();
                        nouveauProduit.setNom_produit(nom_nouveauProduit);

                        listesDB.insertProduit(nouveauProduit, id_liste);
                        listesDB.close();

                        finish();
                        startActivity(getIntent());
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Veuillez saisir un nom de produit", Toast.LENGTH_LONG).show();
                    }
                }
            });

            alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });

            alert.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /******
     *
     *  Fonction callDeleteProduit :
     *  Appelée pour supprimer un produit
     *  Paramètre : id du produit
     */

    private void callDeleteProduit(int id_prod)
    {
        BaskitDB theDB = new BaskitDB(this);
        theDB.open();
        theDB.deleteProduit(id_prod);
        theDB.close();

        Toast.makeText(this, "Produit supprimé", Toast.LENGTH_LONG).show();
        startActivity(getIntent());
        finish();

    }

    /******
     *
     *  Fonction callModifierTitre :
     *  Appelée pour éditer le titre d'une liste
     *  Paramètre : id de la liste
     */

    private void callRenommerListe(int id_list)
    {
        final int id_liste = id_list;

        BaskitDB theDB = new BaskitDB(this);
        theDB.open();

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Renommer la liste");

        final EditText input = new EditText(this);
        alert.setView(input);

        Liste liste_recup = theDB.getListe(id_liste);
        String nom_liste = String.valueOf(liste_recup.getNom_liste());

        input.setText(nom_liste);

        theDB.close();

        alert.setPositiveButton("Renommer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String nouveauNom = String.valueOf(input.getText());

                BaskitDB listesDB = new BaskitDB(getApplicationContext());
                listesDB.open();

                if(nouveauNom.length() > 0)
                {
                    listesDB.renommerListe(id_liste, nouveauNom);
                    Toast.makeText(getApplicationContext(), "Liste renommée avec succès !", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Veuillez saisir un titre pour la liste", Toast.LENGTH_LONG).show();
                }

                listesDB.close();

                finish();
                startActivity(getIntent());
            }
        });

        alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alert.show();

    }



    // Lors de l'appui sur la touche retour
    // On rappelle la main activity
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, ListsActivity.class);
        startActivity(i);
        finish();
    }
}
