package SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Remi on 05/05/2014.
 */
public class maBaseSQLite extends SQLiteOpenHelper {

    private static final String TABLE_LISTE = "table_liste";
    private static final String COL_ID_LISTE = "ID_LISTE";
    private static final String COL_NOM_LISTE = "NOM_LISTE";
    private static final String COL_DATE_CREATION = "DATE_CREATION";

    private static final String CREATE_TABLE_LISTE = "CREATE TABLE " + TABLE_LISTE + " ("
            + COL_ID_LISTE + " INTEGER PRIMARY KEY ASC, " + COL_NOM_LISTE + " TEXT NOT NULL, "
            + COL_DATE_CREATION + " TEXT NOT NULL);";

    private static final String TABLE_PRODUITS = "table_produits";
    private static final String COL_ID_PRODUIT = "ID_PRODUIT";
    private static final String COL_NOM_PRODUIT = "NOM_PRODUIT";
    //private static final String COL_IS_CHECKED = "IS_CHECKED";

    private static final String CREATE_TABLE_PRODUITS = "CREATE TABLE " + TABLE_PRODUITS + " ("
            + COL_ID_PRODUIT + " INTEGER PRIMARY KEY ASC, " + COL_NOM_PRODUIT + " TEXT NOT NULL, "
            + COL_ID_LISTE + " TEXT NOT NULL)";

    public maBaseSQLite(Context context, String name, CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUITS);
        db.execSQL(CREATE_TABLE_LISTE);
        db.execSQL(CREATE_TABLE_PRODUITS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_LISTE + ";");
        db.execSQL("DROP TABLE " + TABLE_PRODUITS + ";");
        onCreate(db);
    }

}

