package com.example.apptp_4.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.apptp_4.classes.Etudiant;
import com.example.apptp_4.util.MySQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class EtudiantService {
    private static final String TABLE_NAME = "etudiant";

    private static final String KEY_ID = "id";
    private static final String KEY_NOM = "nom";
    private static final String KEY_PRENOM = "prenom";

    private static String[] COLUMNS = {KEY_ID, KEY_NOM, KEY_PRENOM};

    private MySQLiteHelper helper;

    public EtudiantService(Context context) {
        this.helper = new MySQLiteHelper(context);
    }

    public void create(Etudiant e) {
        SQLiteDatabase db = this.helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOM, e.getNom());
        values.put(KEY_PRENOM, e.getPrenom());

        long result = db.insert(TABLE_NAME, null, values);
        db.close();

        if (result == -1) {
            Log.e("EtudiantService", "Échec de l'ajout de l'étudiant");
        } else {
            Log.d("EtudiantService", "Étudiant ajouté avec succès, ID : " + result);
        }
    }

    public void update(Etudiant e) {
        SQLiteDatabase db = this.helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOM, e.getNom());
        values.put(KEY_PRENOM, e.getPrenom());

        int rowsAffected = db.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(e.getId())});
        db.close();

        if (rowsAffected > 0) {
            Log.d("EtudiantService", "Étudiant mis à jour avec succès, ID : " + e.getId());
        } else {
            Log.e("EtudiantService", "Échec de la mise à jour de l'étudiant, ID : " + e.getId());
        }
    }

    public Etudiant findById(int id) {
        Etudiant e = null;
        SQLiteDatabase db = this.helper.getReadableDatabase();
        Cursor c = db.query(TABLE_NAME, COLUMNS, "id = ?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (c.moveToFirst()) {
            e = new Etudiant();
            e.setId(c.getInt(0));
            e.setNom(c.getString(1));
            e.setPrenom(c.getString(2));
        }
        c.close();
        db.close();
        return e;
    }

    public void delete(Etudiant e, Context context) {
        SQLiteDatabase db = this.helper.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(e.getId())});
        db.close();

        if (rowsDeleted > 0) {
            Toast.makeText(context, "L'étudiant " + e.getNom() + " " + e.getPrenom() + " a été supprimé.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Erreur : Échec de la suppression", Toast.LENGTH_SHORT).show();
        }
    }

    public List<Etudiant> findAll() {
        List<Etudiant> eds = new ArrayList<>();
        String req = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.helper.getReadableDatabase();
        Cursor c = db.rawQuery(req, null);

        if (c.moveToFirst()) {
            do {
                Etudiant e = new Etudiant();
                e.setId(c.getInt(0));
                e.setNom(c.getString(1));
                e.setPrenom(c.getString(2));
                eds.add(e);
                Log.d("EtudiantService", "Étudiant récupéré: " + e.getNom() + " " + e.getPrenom());
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return eds;
    }
}
