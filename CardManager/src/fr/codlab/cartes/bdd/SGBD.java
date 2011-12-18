package fr.codlab.cartes.bdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SGBD 
{    
	private static final String DATABASE_NAME = "cardmanager";
	private static final String TABLE_POSSESSIONS = "possession";
	private static final int DATABASE_VERSION = 3;

	/* 
	 * EXTENSION
	 * id INT primary key
	 * nom VARCHAR 30
	 * 
	 * CARTE
	 * 
	 */
	private static final String CREATE_POSSESSION = "create table if not exists "+TABLE_POSSESSIONS+" (_id integer primary key autoincrement,extension integer, carte integer, quantite integer, quantite_reverse integer, quantite_holo integer)";
	//private static final String CREATE_POSSESSION_HOLO = "create table if not exists "+TABLE_POSSESSIONS_HOLO+" (_id integer primary key autoincrement,extension integer, carte integer, quantite integer)";
	//private static final String CREATE_POSSESSION_HOLO = "create table if not exists "+TABLE_POSSESSIONS_HOLO+" (_id integer primary key autoincrement,extension integer, carte integer, quantite integer)";


	private final Context context; 

	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public SGBD(Context ctx) 
	{
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper 
	{
		DatabaseHelper(Context context) 
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) 
		{
			db.execSQL(CREATE_POSSESSION);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, 
				int newVersion) 
		{


			/*Log.w(TAG, "Upgrading database from version " + oldVersion 
                    + " to "
                    + newVersion + ", which will destroy all old data");*/
			//db.execSQL("DROP TABLE IF EXISTS "+TABLE_POSSESSIONS);

			db.execSQL("ALTER TABLE "+TABLE_POSSESSIONS+" ADD quantite_holo integer");
			db.execSQL("ALTER TABLE "+TABLE_POSSESSIONS+" ADD quantite_reverse integer");

			onCreate(db);
		}
	}    

	//---opens the database---
	public SGBD open() throws SQLException 
	{
		db = DBHelper.getWritableDatabase();

		db.execSQL(CREATE_POSSESSION);

		return this;
	}

	//---closes the database---    
	public void close() 
	{
		DBHelper.close();
	}

	public long addCarteExtension(long extension, long carte){
		ContentValues initialValues = new ContentValues();
		initialValues.put("extension", extension);
		initialValues.put("carte", carte);
		initialValues.put("quantite", 0);
		initialValues.put("quantite_holo", 0);
		initialValues.put("quantite_reverse", 0);
		return db.insert(TABLE_POSSESSIONS, null, initialValues);
	}

	private int updateCarteExtensionNormal(long extension, long carte, long valeur){
		ContentValues initialValues = new ContentValues();
		if(valeur<0)
			valeur=0;
		initialValues.put("quantite", valeur);
		return db.update(TABLE_POSSESSIONS, initialValues, "carte="+carte+" AND extension="+extension, null);
	}

	private int updateCarteExtensionHolo(long extension, long carte, long valeur){
		ContentValues initialValues = new ContentValues();
		if(valeur<0)
			valeur=0;
		initialValues.put("quantite_holo", valeur);
		return db.update(TABLE_POSSESSIONS, initialValues, "carte="+carte+" AND extension="+extension, null);
	}

	private int updateCarteExtensionReverse(long extension, long carte, long valeur){
		ContentValues initialValues = new ContentValues();
		if(valeur<0)
			valeur=0;
		initialValues.put("quantite_reverse", valeur);
		return db.update(TABLE_POSSESSIONS, initialValues, "carte="+carte+" AND extension="+extension, null);
	}

	public int getPossessionExtension(long extension) throws SQLException{ 
		Cursor mCursor = db.query(true, TABLE_POSSESSIONS, new String[] {
				"SUM(quantite) as q",
				"SUM(quantite_holo) as qh",
				"SUM(quantite_reverse) as qr"
		}, 
		"extension=" + extension, 
		null,null,null,null,null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		int val = 0;
		if(mCursor.getCount()>0){
			val = mCursor.getInt(mCursor.getColumnIndex("q"))+
					mCursor.getInt(mCursor.getColumnIndex("qh"))+
					mCursor.getInt(mCursor.getColumnIndex("qr"));
		}
		mCursor.close();
		return val;
	}
	public int getExtensionProgression(long extension) throws SQLException{ 
		Cursor mCursor = db.query(true, TABLE_POSSESSIONS, new String[] {
				"count(*) as total"
		}, 
		"extension=" + extension+" AND (quantite>0 OR quantite_holo>0 OR quantite_reverse>0)", 
		null,null,null,null,null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		int val = 0;
		if(mCursor.getCount()>0)
			val = mCursor.getInt(mCursor.getColumnIndex("total"));
		mCursor.close();
		return val;
	}

	private int getPossessionCarteExtension(long extension, long carte, int type) throws SQLException{ 
		Cursor mCursor = db.query(true, TABLE_POSSESSIONS, new String[] {
				"quantite",
				"quantite_reverse",
				"quantite_holo"
		}, 
		"carte="+carte+" AND extension=" + extension, 
		null,null,null,null,null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		int val = 0;
		if(mCursor.getCount()>0){
			if(type == NORMAL)
				val = mCursor.getInt(mCursor.getColumnIndex("quantite"));
			else if(type == REVERSE)
				val = mCursor.getInt(mCursor.getColumnIndex("quantite_reverse"));
			else if(type == HOLO)
				val = mCursor.getInt(mCursor.getColumnIndex("quantite_holo"));
		}
		mCursor.close();
		return val;
	}


	private final static int NORMAL=0;
	private final static int REVERSE=1;
	private final static int HOLO=2;

	private int updatePossessionCarteExtensionGenerique(long extension, long carte, int quantite, int type) throws SQLException{ 
		Cursor mCursor = db.query(true, TABLE_POSSESSIONS, new String[] {
				"quantite"
		}, 
		"carte="+carte+" AND extension=" + extension, 
		null,null,null,null,null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		if(mCursor.getCount()<=0)
			this.addCarteExtension(extension, carte);
		mCursor.close();

		switch(type){
		case NORMAL:
			return this.updateCarteExtensionNormal(extension, carte, quantite);
		case REVERSE:
			return this.updateCarteExtensionReverse(extension, carte, quantite);
		case HOLO:
			return this.updateCarteExtensionHolo(extension, carte, quantite);
		default:
			return 0;

		}		
	}
	
	public int updatePossessionCarteExtensionNormal(long extension, long carte, int quantite){
		return updatePossessionCarteExtensionGenerique(extension, carte, quantite, NORMAL);
	}
	public int updatePossessionCarteExtensionReverse(long extension, long carte, int quantite){
		return updatePossessionCarteExtensionGenerique(extension, carte, quantite, REVERSE);
	}
	public int updatePossessionCarteExtensionHolo(long extension, long carte, int quantite){
		return updatePossessionCarteExtensionGenerique(extension, carte, quantite, HOLO);
	}
	
	public int getPossessionCarteExtensionNormal(long extension, long carte){
		return getPossessionCarteExtension(extension, carte, NORMAL);
	}
	public int getPossessionCarteExtensionReverse(long extension, long carte){
		return getPossessionCarteExtension(extension, carte, REVERSE);
	}
	public int getPossessionCarteExtensionHolo(long extension, long carte){
		return getPossessionCarteExtension(extension, carte, HOLO);
	}
}
