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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import SQLite.BaskitDB;
import SQLite.Liste;
import SQLite.ListeAdapter;


public class ListsActivity extends ListActivity {

    public static final int MENU_SUPPRIMER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        Bundle extra = getIntent().getExtras();

        //ListView lv = (ListView) findViewById(R.id.list_item);
        registerForContextMenu(getListView());

        BaskitDB listesBD = new BaskitDB(this);
        listesBD.open();

        List<Liste> listAllLists = new LinkedList<Liste>();
        listAllLists = listesBD.getListe();
        /*for (Liste vListes : listAllLists)
        {
            Log.d("Test noms listes", vListes.getNom_liste());

        }*/

        // Création de la ArrayList qui nous permettra de remplir la listView
        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

        // On déclare la HashMap qui contiendra les informations pour un item
        HashMap<String, String> map;

        for(int i = 0 ; i < listAllLists.size() ; i++) {
            map = new HashMap<String, String>();
            map.put("nom", listAllLists.get(i).getNom_liste());
            map.put("date", String.valueOf(listAllLists.get(i).getDate_creation()));
            map.put("id", String.valueOf(listAllLists.get(i).getId_liste()));
            listItem.add(map);
        }
        ListeAdapter adapter = new ListeAdapter(this, listItem);
        setListAdapter(adapter);

        listesBD.close();

    }

     /*
        TODO : Créer le layout de chaque liste : menu_each_list.xml
     */

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        HashMap<String, String> item = (HashMap<String, String>) getListAdapter().getItem(position);
        //Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, DisplayList.class);
        i.putExtra("id", item.get("id"));
        startActivity(i);
        //finish();
    }
/*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Menu");
        menu.add(Menu.NONE, MENU_SUPPRIMER, Menu.NONE, "Supprimer la liste");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case MENU_SUPPRIMER:

                Bundle extra = getIntent().getExtras();
                //int id_liste = Integer.parseInt(extra.getString("id"));

                Toast.makeText(this, "TODO: Supprimer la liste : " + info.position, Toast.LENGTH_LONG).show();


                BaskitDB listesBD = new BaskitDB(this);
                listesBD.open();
               // listesBD.deleteListe(info.position);
                listesBD.close();

                this.onResume();

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }*/

    @Override
    protected void onResume() {

        super.onResume();
        this.onCreate(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lists, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        if (id == R.id.action_ajout) {

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Nouvelle liste");

            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("Créer", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String nom_nouvelleListe = String.valueOf(input.getText());

                    //Toast.makeText(getApplicationContext(), "Valeur : " + nom_nouvelleListe, Toast.LENGTH_LONG).show();
                    BaskitDB listesDB = new BaskitDB(getApplicationContext());
                    listesDB.open();
                    Liste nouvelleListe = new Liste();
                    nouvelleListe.setNom_liste(nom_nouvelleListe);
                    nouvelleListe.setDate_creation(getCurrentTimeStamp());
                    listesDB.insertListe(nouvelleListe);
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

    public static String getCurrentTimeStamp() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy à HH:mm:ss");
            String currentTimeStamp = dateFormat.format(new Date());

            return currentTimeStamp;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}


/*
        Toast.makeText(this, listesBD.getListe(1).getNom_liste(), Toast.LENGTH_LONG).show();

 */