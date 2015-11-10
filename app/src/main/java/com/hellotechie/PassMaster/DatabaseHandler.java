package com.hellotechie.PassMaster;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "pwmanager";

    // Contacts table name
    private static final String TABLE_SITES = "sites";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
	private static final String KEY_URL = "url";
    private static final String KEY_PW = "pw";
    private static final String KEY_DESC = "email";
	private static final String KEY_TYPE = "type";
    private final ArrayList<Site> site_list = new ArrayList<Site>();

    public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
	String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_SITES + "("
		+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT NOT NULL,"
		+ KEY_URL + " TEXT," + KEY_PW + " TEXT," + KEY_DESC + " TEXT," + KEY_TYPE + " TEXT)";
	db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SITES);
	// Create tables again
		onCreate(db);
    }

    public void Add_Site(Site site) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, site.getName());
		values.put(KEY_URL, site.getUrl());
		values.put(KEY_PW, site.getPw());
		values.put(KEY_DESC, site.getDesc());
		values.put(KEY_TYPE, site.getType());
		long res = db.insert(TABLE_SITES, null, values);
		db.close();
    }

    // Getting single contact
    Site Get_Site(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_SITES, new String[] { KEY_ID,
			KEY_NAME, KEY_URL, KEY_PW, KEY_DESC, KEY_TYPE }, KEY_ID + "=?",
			new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
		    cursor.moveToFirst();

		Site site = new Site(Integer.parseInt(cursor.getString(0)),
			cursor.getString(1), cursor.getString(2), cursor.getString(3),
			cursor.getString(4), cursor.getString(5));
		// return contact
		cursor.close();
		db.close();

		return site;
    }

    Site Get_Site_By_Name(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SITES, new String[] { KEY_ID,
                        KEY_NAME, KEY_URL, KEY_PW, KEY_DESC, KEY_TYPE }, KEY_NAME + "=?",
                new String[] { name }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Site site = new Site(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5));
        // return contact
        cursor.close();
        db.close();

        return site;
    }

    public ArrayList<Site> Get_Sites() {
		try {
		    site_list.clear();
	    	String selectQuery = "SELECT  * FROM " + TABLE_SITES;

	    	SQLiteDatabase db = this.getWritableDatabase();
	    	Cursor cursor = db.rawQuery(selectQuery, null);
	    	if (cursor.moveToFirst()) {
				do {
				    Site site = new Site();
			    	site.setID(Integer.parseInt(cursor.getString(0)));
				    site.setName(cursor.getString(1));
				    site.setUrl(cursor.getString(2));
				    site.setPw(cursor.getString(3));
					site.setDesc(cursor.getString(4));
					site.setType(cursor.getString(5));
			    	site_list.add(site);
				} while (cursor.moveToNext());
	    	}
	    	// return contact list
	    	cursor.close();
	    	db.close();
		}
		catch (Exception e) {
	    	// TODO: handle exception
	    	Log.e("all_contact", "" + e);
		}
		return site_list;
    }

    public int Update_Site(Site site) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, site.getName());
		values.put(KEY_URL, site.getUrl());
		values.put(KEY_PW, site.getPw());
		values.put(KEY_DESC, site.getDesc());
		values.put(KEY_TYPE, site.getType());

		return db.update(TABLE_SITES, values, KEY_ID + " = ?",
			new String[] { String.valueOf(site.getID()) });
    }

    public void Delete_Site(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SITES, KEY_ID + " = ?",
				new String[]{String.valueOf(id)});

		db.close();
    }

    public int Get_Total_Sites() {
		String countQuery = "SELECT  * FROM " + TABLE_SITES;
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		return cursor.getCount();
    }
}
