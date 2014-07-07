package com.security.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BlackNumberDataBaseAdapter {
	private static final String TAG = "BlackNumberDataBaseAdapter";
	
	//table colum blacknumber
	private static final String KEY_NUM = "number";
	private static final String KEY_ID = "id";
	//database name
	private static final String DB_NAME = "security.db";
	//table name
	private static final String DB_TABLE = "blackNumTab";
	//db version
	private static final int DB_VERSION = 1;
	
	private Context mContext ;
	
	private static final String DB_CREATE = "CREATE TABLE" 
											+ DB_TABLE +"("
											+ KEY_ID
											+ " INTEGER PRIMARY KEY,"
											+ KEY_NUM +" VARCHAR(20))";
	
	//when open() return to database object
	private SQLiteDatabase mSQLiteDatabase = null;
	//extends SQLiteOpenHelper
	private DatabaseHelper mDatabaseHelper = null ;
	
	private static class DatabaseHelper extends SQLiteOpenHelper{
		
		DatabaseHelper(Context context){
			//when getWritableDatabase()/getReadableDatabase()
			super(context,DB_NAME,null,DB_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(DB_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
		
	}
	
	public BlackNumberDataBaseAdapter(Context context){
		mContext = context;
	}
	
	//open databse return databse object
	public void open() throws SQLException
	{
		mDatabaseHelper = new DatabaseHelper(mContext);
		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
	}
	
	public void close(){
		mDatabaseHelper.close();
	}
	
	public long insertData(String number){
		ContentValues values = new ContentValues();
		values.put(KEY_NUM, number);
		return mSQLiteDatabase.insert(DB_TABLE, KEY_ID, values);
	}
	
	public boolean deleteData(String number){
		return mSQLiteDatabase.delete(DB_TABLE, KEY_NUM + "=" +number, null) > 0 ;
	}
	
	public boolean updateData(String oldNumber, String newNumber){
		ContentValues values= new ContentValues();
		values.put(KEY_NUM, newNumber);
		return mSQLiteDatabase.update(DB_TABLE, values,
				KEY_NUM +"="+ oldNumber, null) > 0 ;
	}
	public List<String> fetchAllData(){
		List<String> numbers = new ArrayList<String>();  
		Cursor cursor = mSQLiteDatabase.query(DB_TABLE, new String[]{KEY_NUM},
				null, null, null, null, null);
		while(cursor.moveToNext()){
			numbers.add(cursor.getString(0));
		}
		cursor.close(); 
		return numbers ;
	}
}
