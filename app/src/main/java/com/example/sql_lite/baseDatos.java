package com.example.sql_lite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

public class baseDatos extends SQLiteOpenHelper {
    public static abstract class Datostabla implements BaseColumns {
        public static final String NOMBRE_TABLE = "grupo";
        public static final String COLUMNA_ID = "matricula";
        public static final String COLUMNA_NAME = "nombre";
        public static final String COLUMNA_MATERIA = "materia";
        public static final String COLUMNA_CARRERA = "carrera";
        public static final String COLUMNA_PARCIAL1 = "parcial1";
        public static final String COLUMNA_PARCIAL2 = "parcial2";
        public static final String COLUMNA_PARCIAL3 = "parcial3";

        //ba ser private por que solo ba permeneser hay
        private static final String CREAR_TABLA = "CREATE TABLE " + Datostabla.NOMBRE_TABLE + " (" +
                Datostabla.COLUMNA_ID + " TEXT PRIMARY KEY, " +
                Datostabla.COLUMNA_NAME + " TEXT NOT NULL, " +
                Datostabla.COLUMNA_MATERIA + " TEXT NOT NULL, " +
                Datostabla.COLUMNA_CARRERA + " TEXT NOT NULL, " +
                Datostabla.COLUMNA_PARCIAL1 + " INTEGER, " +
                Datostabla.COLUMNA_PARCIAL2 + " INTEGER, " +
                Datostabla.COLUMNA_PARCIAL3 + " INTEGER" + ");";

        private static final String SQL_DELETE="DROP TABLE IF EXISTS "+Datostabla.NOMBRE_TABLE;
    }
    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="Escuela.db";
    //MANDA A TRAER EL NOMBRE DE LA TABLA


    public baseDatos(@Nullable Context context) {
        super(context, baseDatos.DATABASE_NAME, null, baseDatos.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Datostabla.CREAR_TABLA);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //checar si esta si no va crear
        db.execSQL(Datostabla.SQL_DELETE);
        onCreate(db);
    }
}
