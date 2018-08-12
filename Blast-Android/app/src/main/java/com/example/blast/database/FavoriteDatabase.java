package com.example.blast.database;


import java.util.ArrayList;

import com.example.blast.model.FavoriteModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FavoriteDatabase {

	private static final String TAG = "DBAdapter";
	private static final String DATABASE_NAME = "favorite_database";
	private static final String DATABASE_TABLE = "favorite_table";
	private static final int DATABASE_VERSION = 1;

	public static final String KEY_title = "title";
	public static final String KEY_description = "description";
	public static final String KEY_category = "category";
	public static final String KEY_thumbnail = "thumbnail";
	public static final String KEY_url = "url";
	public static final String KEY_vote_up = "vote_up";
	public static final String KEY_vote_down = "vote_down";

	private static final String DATABASE_CREATE =
			"create table " + DATABASE_TABLE + " (_id integer primary key autoincrement, "
					+ KEY_title + " text not null,"
					+ KEY_description + " text not null,"
					+ KEY_category + " text not null,"
					+ KEY_thumbnail + " text not null,"
					+ KEY_url + " text not null,"
					+ KEY_vote_up + " text not null,"
					+ KEY_vote_down + " text not null);";

	private final Context context; 
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public FavoriteDatabase(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion 
					+ " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS titles");
			onCreate(db);
		}
	}    

	//---opens the database---
	public FavoriteDatabase open() throws SQLException  {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	//---closes the database---    
	public void close() {
		DBHelper.close();
	}

	//---insert a title into the database---
	public long insertTitle(int image) {
		ContentValues initialValues = new ContentValues();
		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	public long InsertOne(String title, String description, String category, String thumbnail, String url, String vote_up, String vote_down) {
		ContentValues initialValues = new ContentValues();

		initialValues.put(KEY_title, title);
		initialValues.put(KEY_description, description);
		initialValues.put(KEY_category, category);
		initialValues.put(KEY_thumbnail, thumbnail);
		initialValues.put(KEY_url, url);
		initialValues.put(KEY_vote_up, vote_up);
		initialValues.put(KEY_vote_down, vote_down);

		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean deleteOne(String url)  {
		return db.delete(DATABASE_TABLE, KEY_url + "='" + url + "'", null) > 0;
	}

	public ArrayList<FavoriteModel> getAll(){

		ArrayList<FavoriteModel> list = new ArrayList<FavoriteModel>();
		Cursor c = null;
		try {
			c = this.db.query(FavoriteDatabase.DATABASE_TABLE,
					new String []{KEY_title, KEY_description, KEY_category, KEY_thumbnail, KEY_url, KEY_vote_up, KEY_vote_down},
					null, null, null, null, null);
			int numRows = c.getCount();
			c.moveToFirst();
			for (int i = 0; i < numRows; ++i) {
				FavoriteModel item = new FavoriteModel();

				item.title = c.getString(0);
				item.description = c.getString(1);
				item.category = c.getString(2);
				item.thumbnail = c.getString(3);
				item.url = c.getString(4);
				item.vote_up = c.getString(5);
				item.vote_down = c.getString(5);

				list.add(item);

				c.moveToNext();
			}

		} 
		catch (SQLException e) { 
			e.printStackTrace();
			
		} finally{
			if (c != null && c.isClosed()){
				c.close();
			}
		}
		return list;
	}
	
	/*
	 * Get Cursor from url
	 */
	public Cursor getSameURL(String url) throws SQLException {
		Cursor mCursor =
				db.query(true, DATABASE_TABLE, null,
						KEY_url + "='" + url + "'", 
						null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
}
