package com.thehackerati;


import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
public class DatabaseConnector {
	private static final String DATABASE_NAME="TheHackerati";
	private SQLiteDatabase db;
	private DatabaseOpenHelper dbHelper;
	public DatabaseConnector(Context  context) {
		dbHelper=new DatabaseOpenHelper(context,DATABASE_NAME,null,1);
	}

	public void open()  { 
		db=  dbHelper.getWritableDatabase();
	}

	public  void close() {
		if(db != null)
			db.close();
	}
	public HashMap<Integer, Integer> getMyFavorite() {
		HashMap<Integer, Integer> hashMapofFav = new HashMap<Integer, Integer>();
		Cursor cursor =  db.rawQuery("select entry_ID from Hackerati", null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				int entry_ID = cursor.getInt(0);
				hashMapofFav.put(entry_ID, entry_ID);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return hashMapofFav ;
	}

	public boolean isFavorite(int entry_Id) {
		boolean isfav = false ;
		Cursor cursor = db.rawQuery("Select * from Hackerati where entry_Id="+entry_Id, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				int entry_ID = cursor.getInt(0);
				isfav = true ;
			} while (cursor.moveToNext());
		}
		cursor.close();
		return isfav ;
	}



	public void insertentry_Id(int entry_Id) {
		ContentValues newAssignment= new ContentValues();
		newAssignment.put("entry_Id",entry_Id);
		open();
		db.insert("Hackerati", null, newAssignment);
		close();
	}

	public void deleteentry_Id(int entry_Id) {
		open();
		db.delete("Hackerati","entry_Id="+entry_Id,null);
		close();			
	}


	private class DatabaseOpenHelper extends SQLiteOpenHelper{
		public DatabaseOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			String createTable=" CREATE TABLE Hackerati " +
					" ( _id integer primary key autoincrement, entry_Id integer) ;   ";
			db.execSQL(createTable);
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// no implementation
		}
	}
}


