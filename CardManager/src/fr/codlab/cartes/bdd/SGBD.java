package fr.codlab.cartes.bdd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Xml;

public class SGBD 
{    
	static final String DATABASE_NAME = "cardmanager";
	static final String TABLE_POSSESSIONS = "possession";
	static final int DATABASE_VERSION = 3;

	/* 
	 * EXTENSION
	 * id INT primary key
	 * nom VARCHAR 30
	 * 
	 * CARTE
	 * 
	 */
	static final String CREATE_POSSESSION = "create table if not exists "+TABLE_POSSESSIONS+" (_id integer primary key autoincrement,extension integer, carte integer, quantite integer, quantite_reverse integer, quantite_holo integer)";
	//private static final String CREATE_POSSESSION_HOLO = "create table if not exists "+TABLE_POSSESSIONS_HOLO+" (_id integer primary key autoincrement,extension integer, carte integer, quantite integer)";
	//private static final String CREATE_POSSESSION_HOLO = "create table if not exists "+TABLE_POSSESSIONS_HOLO+" (_id integer primary key autoincrement,extension integer, carte integer, quantite integer)";


	private final Context context; 

	private static DatabaseHelper DBHelper;
	private static SQLiteDatabase db;

	public SGBD(Context ctx) 
	{
		this.context = ctx;
		if(DBHelper == null)
			DBHelper = new DatabaseHelper(context);
	}

	//---opens the database---
	public SGBD open() throws SQLException{
		if(db == null || !db.isOpen())
			db = DBHelper.getWritableDatabase();
		db.execSQL(CREATE_POSSESSION);

		return this;
	}

	//---closes the database---    
	public void close() 
	{
		DBHelper.close();
		db = null;
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

	private Cursor getPossessions(){
		Cursor cursor = db.query(true, TABLE_POSSESSIONS, new String[]{
				"extension as e",
				"carte as c",
				"quantite as q",
				"quantite_holo as qh",
				"quantite_reverse as qr"
		},null,
		null,null,null,null,null);
		if(cursor != null)
			cursor.moveToFirst();
		return cursor;
	}
	/**
	 * Create Possession file
	 * 
	 * Create xml
	 * <possessions>
	 * 	<carte e='' id='' qn='' qh='' qr='' />
	 * </possessions>
	 * 
	 * Create csv
	 * extension;carte;quantite;quantite_holo;quantite_reverse
	 * 
	 * Create json
	 * {possessions:{[
	 * 	{carte:{
	 * 		e:'',
	 * 		id:'',
	 * 		qn:'',
	 * 		qh:'',
	 * 		qr:''
	 * 		}
	 * 	},
	 * 	{carte:{
	 * 		e:'',
	 * 		id:'',
	 * 		qn:'',
	 * 		qh:'',
	 * 		qr:''
	 * 		}
	 * 	}
	 * 	]}
	 * }
	 * @param output
	 * @param mode
	 * @throws IOException
	 */
	private void writePossessions(OutputStreamWriter output, Output mode) throws IOException{
		if(output != null){
			if(mode == Output.XML){
				output.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
				output.write("<possessions>\n");
			}else if(mode == Output.CSV){
				output.write("extension"+
						";carte"+
						";quantite"+
						";quantite_holo"+
						";quantite_reverse\n");
			}else if(mode == Output.JSON){
				output.write("{possessions:[");
			}
			
			Cursor cursor = getPossessions();
			boolean f=false;
			if(cursor != null){
				switch(mode){
				case XML:
					output.write("	<carte e=\""+cursor.getColumnIndex("e")+
							"\" id=\""+cursor.getColumnIndex("c")+
							"\" qn=\""+cursor.getColumnIndex("q")+
							"\" qh=\""+cursor.getColumnIndex("qh")+
							"\" qr=\""+cursor.getColumnIndex("qr")+
							"\" />\n");
					break;
				case JSON:
					if(f)
						output.write(",\n");
					output.write("{" +
							"carte:{" +
							"e:'"+cursor.getColumnIndex("e")+"',"+
							"id:'"+cursor.getColumnIndex("c")+"',"+
							"qn:'"+cursor.getColumnIndex("q")+"',"+
							"qh:'"+cursor.getColumnIndex("qh")+"',"+
							"qr:'"+cursor.getColumnIndex("qr")+"'"+
							"}");
					f=true;
					break;
				case CSV:
					output.write(cursor.getColumnIndex("e")+
							";"+cursor.getColumnIndex("c")+
							";"+cursor.getColumnIndex("q")+
							";"+cursor.getColumnIndex("qh")+
							";"+cursor.getColumnIndex("qr")+"\n");
					break;
				default:
						
				}
				cursor.moveToNext();
			}
			cursor.close();
			if(mode == Output.XML)
				output.write("</possessions>\n");
			else if(mode == Output.JSON){
				if(f)
					output.write("\n");
				output.write("]}");
			}
		}
	}
	public void writePossessionJSON(OutputStreamWriter output) throws IOException{
		writePossessions(output, Output.JSON);
	}
	public void writePossessionXML(OutputStreamWriter output) throws IOException{
		writePossessions(output, Output.XML);
	}
	public void writePossessionCSV(OutputStreamWriter output) throws IOException{
		writePossessions(output, Output.CSV);
	}
	private String readPossessionToString(InputStreamReader input) throws IOException{
		String string="";
		StringWriter res = new StringWriter();
		BufferedReader buffer=new BufferedReader(input);
		String line="";
		while ( null!=(line=buffer.readLine())){
			res.write(line); 
		}
		return res.toString();
	}
	private void readPossession(InputStreamReader input, Output mode) throws IOException, JSONException, XmlPullParserException{
		if(mode == Output.JSON){
			String res = readPossessionToString(input);			
			JSONObject obj = new JSONObject(res);
		}else if(mode == Output.XML){
			XmlPullParser p = XmlPullParserFactory.newInstance().newPullParser();
			p.setInput(input);
		}
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
