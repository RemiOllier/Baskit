package com.copiercoller.baskit.app;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
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
        List<Produits> listAllProduits = new LinkedList<Produits>();
        listAllProduits = listesDB.getProduitsWithIdListe(id_liste);

        // Création de la ArrayList qui nous permettra de remplir la listView
        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

        // On déclare la HashMap qui contiendra les informations pour un item
        HashMap<String, String> map;

        for(int i = 0 ; i < listAllProduits.size() ; i++) {
            map = new HashMap<String, String>();
            map.put("nom_produit", listAllProduits.get(i).getNom_produit());
            map.put("id_produit", String.valueOf(listAllProduits.get(i).getId_produit()));
            listItem.add(map);
        }
        ProduitsAdapter adapter = new ProduitsAdapter(this, listItem);
        setListAdapter(adapter);

        Liste liste_recup = listesDB.getListe(id_liste);
        String nom_liste = String.valueOf(liste_recup.getNom_liste());
        this.setTitle(nom_liste);
        listesDB.close();

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        HashMap<String, String> item = (HashMap<String, String>) getListAdapter().getItem(position);

        int id_prod = Integer.parseInt(item.get("id_produit"));
        Toast.makeText(this, id_prod + " à barrer", Toast.LENGTH_LONG).show();

        TextView tv = (TextView) v.findViewById(R.id.tv_label_produit);
        // TODO :



        if(id_prod == 1) {
            // On barre si isChecked = 1
            tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {

            tv.setPaintFlags(tv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        //Intent i = new Intent(this, DisplayList.class);
        //i.putExtra("id", item.get("id"));
        //startActivity(i);
        //finish();
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
            //Toast.makeText(this, "TODO : Dialog new product with id_liste : " + id_liste, Toast.LENGTH_LONG).show();
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Ajouter produit");

            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("Créer", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String nom_nouveauProduit = String.valueOf(input.getText());

                    //Toast.makeText(getApplicationContext(), "Valeur : " + nom_nouvelleListe, Toast.LENGTH_LONG).show();

                    BaskitDB listesDB = new BaskitDB(getApplicationContext());
                    listesDB.open();
                    Produits nouveauProduit = new Produits();
                    nouveauProduit.setNom_produit(nom_nouveauProduit);
                    //Toast.makeText(getApplicationContext(), "Valeur : " + nom_nouveauProduit, Toast.LENGTH_LONG).show();

                    listesDB.insertProduit(nouveauProduit, id_liste);
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

            return true;
        }
        return super.onOptionsItemSelected(item);
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
