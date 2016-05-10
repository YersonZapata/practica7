package com.zapata.yerson.practica7;

/**
 * Created by Le Yerson on 04/05/2016.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDeDatos extends SQLiteOpenHelper {

    public BaseDeDatos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table peluches(ident integer primary key, nombre text, cantidad integer, valor integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exist peluches");
        db.execSQL("create table peluches(ident integer primary key, nombre text, cantidad integer, valor integer)");
    }
}


