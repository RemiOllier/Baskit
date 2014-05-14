package com.copiercoller.baskit.app;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import SQLite.BaskitDB;
import SQLite.Liste;
import SQLite.Produits;


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
        Liste maListe = listesDB.getListe(id_liste);
        String nom_liste = maListe.getNom_liste();
        this.setTitle(nom_liste);
        listesDB.close();

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
                    Toast.makeText(getApplicationContext(), "Valeur : " + nom_nouveauProduit, Toast.LENGTH_LONG).show();

                    listesDB.insertProduit(nouveauProduit, id_liste);
                    listesDB.close();

                    finish();
                    startActivity(getIntent());
                }
            });

            alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
