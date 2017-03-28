package com.cybussolutions.hititpro.Sql_LocalDataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase _db) {
        _db.execSQL(Database.DATABASE_PORTFOLIO);
        _db.execSQL(Database.DATABASE_EXTERIROR);
        _db.execSQL(Database.DATABASE_INTERIOR);
        _db.execSQL(Database.DATABASE_APPLIANCE);
        _db.execSQL(Database.DATABASE_COOLING);
        _db.execSQL(Database.DATABASE_ELECTRICAL);
        _db.execSQL(Database.DATABASE_FIREPLACE);
        _db.execSQL(Database.DATABASE_INSULATION);
        _db.execSQL(Database.DATABASE_PLUMBING);
        _db.execSQL(Database.DATABASE_ROOFING);
        _db.execSQL(Database.DATABASE_HEATING);



    }

    @Override
    public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
        Log.w("TaskDBAdapter", "Upgrading from version " + _oldVersion + " to "
                + _newVersion + ", which will destroy all old data");
        _db.execSQL("DROP TABLE IF EXISTS " + "TEMPLATE");

        onCreate(_db);


    }

}
