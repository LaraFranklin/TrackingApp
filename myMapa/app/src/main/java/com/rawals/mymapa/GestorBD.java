package com.rawals.mymapa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ramon on 31/3/16.
 */
public class GestorBD extends AppCompatActivity {
    private SQLiteDatabase bd = null;
    private BDHelper helper = null;





    public GestorBD(Context context) {
        helper = new BDHelper(context, "carreras.sqlite", null, 1);
    }

    public void abrirBD() {
        if (bd == null)
            bd = helper.getWritableDatabase();
    }

    public void cerrarBD() {
        if (bd != null)
            bd.close();
    }

    public void guardarCarrera(carrera c) {
        if (bd.isOpen() && c != null) {

            ContentValues values = new ContentValues();

            values.put("fecha", (String) c.getFecha());
            values.put("distancia", (String) c.getDistancia());
            values.put("duracion", (String) c.getDuracion());
            values.put("polilinea", (String) c.getPolilinea());
            values.put("tipo", (String) c.getTipo());



            bd.insert("carreras", null, values);
        }
    }

    public void borrarCarrera(carrera c) {
        if (bd.isOpen() && c != null) {

            String[] args = new String[]{(String) c.getFecha()};
            bd.execSQL("DELETE FROM carreras WHERE fecha=?", args);
        }
    }

    public carrera leerCarrera(String nombre) {

        carrera c = null;

        if (bd.isOpen()) {

            String tabla = "carreras";
            String[] columnas = new String[]{"id", "fecha", "distancia", "duracion","polilinea","tipo"};
            String where = "fecha = ?";
            String[] argumentoswhere = new String[]{nombre};
            String groupby = null;
            String having = null;
            String orderby = null;
            String limit = null;

            Cursor c1 = bd.query(tabla, columnas, where, argumentoswhere,
                    groupby, having, orderby, limit);

            if (c1.moveToFirst()) {
                c = new carrera();
                c.setFecha(c1.getString(1));
                c.setDistancia(c1.getString(2));
                c.setDuracion(c1.getString(3));
                c.setPolilinea(c1.getString(4));
                c.setTipo(c1.getString(5));
            }
        }

        return c;
    }

    public List<carrera> leerCarreras() {

        List<carrera> l = new ArrayList<>();

        if (bd.isOpen()) {

            String tabla = "carreras";
            String[] columnas = new String[]{"id", "fecha", "distancia", "duracion","polilinea","tipo"};
            String where = null; // "id = ?"
            String[] argumentoswhere = null; // = new String[] {"35"};
            String groupby = null;
            String having = null;
            String orderby = null;
            String limit = null;

            Cursor c1 = bd.query(tabla, columnas, where, argumentoswhere,
                    groupby, having, orderby, limit);

            if (c1.moveToFirst()) {

                // Recorremos el cursor hasta que no haya más registros
                do {
                    carrera c = new carrera(c1.getString(1), c1.getString(2), c1.getString(3), c1.getString(4),c1.getString(5));   // El campo 0 es el ID
                    l.add(c);
                } while (c1.moveToNext());
            }
        }

        return l;
    }

    private class BDHelper extends SQLiteOpenHelper {

        private String tablaCarreras = "CREATE TABLE carreras ( id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, fecha TEXT, distancia TEXT, duracion TEXT, polilinea TEXT,tipo TEXT);";

        public BDHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            if (!db.isReadOnly()) {
                // Enable foreign key constraints
                db.execSQL("PRAGMA foreign_keys=ON;");
                db.execSQL(tablaCarreras);
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            if (!db.isReadOnly()) {
                db.execSQL("DROP TABLE IF EXISTS carreras");

                // Enable foreign key constraints
                db.execSQL("PRAGMA foreign_keys=ON;");
                db.execSQL(tablaCarreras);
            }

        }

    }

}
