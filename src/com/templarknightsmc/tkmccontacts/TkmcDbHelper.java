package com.templarknightsmc.tkmccontacts;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class TkmcDbHelper {
	protected SQLiteDatabase db;
	
	/**
	 * Constructor
	 * 
	 * @param perms
	 */
	public TkmcDbHelper(String perms) {
		File databaseDirectory = new File(Configuration.DATABASE_FILE_PATH);
		databaseDirectory.mkdirs();
		try {
			getReadableDatabase();
		} catch (SQLiteException e) {
			//Log.e(TAG, "error -- " + e.getMessage(), e);
			// Error means database does not exist - so create the tables and set a flag to grab the data
			this.db = SQLiteDatabase.openDatabase(Configuration.DATABASE_FILE_PATH + File.separator + Configuration.DATABASE_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
			createTables();
			this.db.close();
			Configuration.DOWNLOAD_CONTENT = true;
		}
		
		if (perms == "w") {
			getWritableDatabase();
		}
	}

	/**
	 * Prep for Refresh of Data
	 */
	public void refreshData() {
		dropTable(TkmcDbContract.TkmcDbContacts.TABLE_NAME);
		dropTable(TkmcDbContract.TkmcDbLodges.TABLE_NAME);
		createTable(Configuration.CONTACTS_CREATE_TABLE);
		createTable(Configuration.LODGES_CREATE_TABLE);
		Configuration.DOWNLOAD_CONTENT = true;
	}

	/**
	 * Creates the database table structure
	 */
	public void createTables() {
		this.db.execSQL(Configuration.CONTACTS_CREATE_TABLE);
		this.db.execSQL(Configuration.LODGES_CREATE_TABLE);
		this.db.execSQL(Configuration.VERSIONS_CREATE_TABLE);
		this.db.execSQL("INSERT INTO " + TkmcDbContract.TkmcDbVersions.TABLE_NAME + " (" + 
			TkmcDbContract.TkmcDbVersions.COLUMN_NAME_VERSION_NUM + "," +
			TkmcDbContract.TkmcDbVersions.COLUMN_NAME_APP_NAME + "," +
			TkmcDbContract.TkmcDbVersions.COLUMN_NAME_VERSION_TYPE + ") VALUES ('0','TKMC Contacts','Data')");
	}
	
	/**
	 * Create a specific table structure
	 */
	private void createTable(String createTable) {
		this.db.execSQL(createTable);
	}
	
	/**
	 * Drops a table structure
	 */
	private void dropTable(String dropTable) {
		this.db.execSQL("DROP TABLE IF EXISTS " + dropTable);
	}
	
	/**
	 * Get a readable database
	 */
	private void getReadableDatabase() {
		this.db = SQLiteDatabase.openDatabase(Configuration.DATABASE_FILE_PATH + File.separator + Configuration.DATABASE_NAME, null, SQLiteDatabase.OPEN_READONLY);
	}
	
	/**
	 * Get a writable database
	 */
	private void getWritableDatabase() {
		this.db = SQLiteDatabase.openDatabase(Configuration.DATABASE_FILE_PATH + File.separator + Configuration.DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
	}
	
	/**
	 * Get Data Version from database
	 */
	public String getDataVersion() {
		String query = "SELECT " + TkmcDbContract.TkmcDbVersions.COLUMN_NAME_VERSION_NUM + " FROM " + TkmcDbContract.TkmcDbVersions.TABLE_NAME + " WHERE " +
				TkmcDbContract.TkmcDbVersions.COLUMN_NAME_APP_NAME + "='TKMC Contacts' AND " + TkmcDbContract.TkmcDbVersions.COLUMN_NAME_VERSION_TYPE + "='Data'";
		Cursor cursor = this.db.rawQuery(query, null);
		if (cursor.getCount() == 1) {
			cursor.moveToFirst();
			return cursor.getString(cursor.getColumnIndex(TkmcDbContract.TkmcDbVersions.COLUMN_NAME_VERSION_NUM));
		}
		return "0";
	}
	
	/**
	 * Retrieves lodges from the database and returns a list so the Spinner can populate 
	 * 
	 * @return List<String>
	 */
	public List<String> getLodges() {
		String query = "SELECT " + TkmcDbContract.TkmcDbLodges.COLUMN_NAME_LODGE_NUM + ", " + TkmcDbContract.TkmcDbLodges.COLUMN_NAME_LODGE_NAME +
				" FROM " + TkmcDbContract.TkmcDbLodges.TABLE_NAME + " ORDER BY " + TkmcDbContract.TkmcDbLodges.COLUMN_NAME_LODGE_NAME;
		
		Cursor cursor = this.db.rawQuery(query,  null);
		cursor.moveToFirst();
		List<String> list = new ArrayList<String>();
		while (cursor.isAfterLast() == false) {
			list.add(cursor.getString(cursor.getColumnIndex(TkmcDbContract.TkmcDbLodges.COLUMN_NAME_LODGE_NAME)));
			cursor.moveToNext();
		}
		return list;
	}
	
	/**
	 * Inserts lodge names into database
	 * 
	 * @param lodges
	 */
	public void insertRows(List<ContentValues> lodges) {
		while (lodges.iterator().hasNext()) {
			this.db.insert(TkmcDbContract.TkmcDbLodges.TABLE_NAME, "null", lodges.iterator().next());
		}
	}
	
	/**
	 * Gets member info from the contacts database and returns it as a ContactList.
	 * 
	 * @param lodge
	 * @return ContactList
	 */
	public ContactList getContacts(String lodge) {
		ContactList contactList = new ContactList();
		String query = "SELECT " + TkmcDbContract.TkmcDbContacts.COLUMN_NAME_RECORD_NUM + ", " + TkmcDbContract.TkmcDbContacts.COLUMN_NAME_FIRST_NAME +
				", " + TkmcDbContract.TkmcDbContacts.COLUMN_NAME_LAST_NAME + ", " + TkmcDbContract.TkmcDbContacts.COLUMN_NAME_NICKNAME + 
				", " + TkmcDbContract.TkmcDbContacts.COLUMN_NAME_RANK + ", " + TkmcDbContract.TkmcDbContacts.COLUMN_NAME_LODGE + 
				", " + TkmcDbContract.TkmcDbContacts.COLUMN_NAME_PHONE + ", " + TkmcDbContract.TkmcDbContacts.COLUMN_NAME_EMAIL + 
				" FROM " + TkmcDbContract.TkmcDbContacts.TABLE_NAME + " WHERE lodge = '" + lodge + "'";
		if (Configuration.SORT_ORDER.equals("2")) {
			query += " ORDER BY " + TkmcDbContract.TkmcDbContacts.COLUMN_NAME_LAST_NAME;
		} else if (Configuration.SORT_ORDER.equals("3")) {
			query += " ORDER BY " + TkmcDbContract.TkmcDbContacts.COLUMN_NAME_NICKNAME;
		} else {
			query += " ORDER BY " + TkmcDbContract.TkmcDbContacts.COLUMN_NAME_FIRST_NAME;
		}
		Cursor cursor = this.db.rawQuery(query,  null);
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			Contact contact = new Contact();
			contact.setId(cursor.getString(cursor.getColumnIndex(TkmcDbContract.TkmcDbContacts.COLUMN_NAME_RECORD_NUM)));
 			if (Configuration.SORT_ORDER.equals("2")) {
 				contact.setDisplayName(cursor.getString(cursor.getColumnIndex(TkmcDbContract.TkmcDbContacts.COLUMN_NAME_LAST_NAME)) + ", " +
					cursor.getString(cursor.getColumnIndex(TkmcDbContract.TkmcDbContacts.COLUMN_NAME_FIRST_NAME)));
 			} else {
 				contact.setDisplayName(cursor.getString(cursor.getColumnIndex(TkmcDbContract.TkmcDbContacts.COLUMN_NAME_FIRST_NAME)) + " " +
 					cursor.getString(cursor.getColumnIndex(TkmcDbContract.TkmcDbContacts.COLUMN_NAME_LAST_NAME)));
			}

			contact.setNickname(cursor.getString(cursor.getColumnIndex(TkmcDbContract.TkmcDbContacts.COLUMN_NAME_NICKNAME)));
			contact.setRank(cursor.getString(cursor.getColumnIndex(TkmcDbContract.TkmcDbContacts.COLUMN_NAME_RANK)));
			contact.setLodge(cursor.getString(cursor.getColumnIndex(TkmcDbContract.TkmcDbContacts.COLUMN_NAME_LODGE)));
			contact.setPhone(cursor.getString(cursor.getColumnIndex(TkmcDbContract.TkmcDbContacts.COLUMN_NAME_PHONE)));
			contact.setEmail(cursor.getString(cursor.getColumnIndex(TkmcDbContract.TkmcDbContacts.COLUMN_NAME_EMAIL)));
			
			contactList.addContact(contact);
			cursor.moveToNext();
		}
		cursor.close();
		return contactList;
	}
	
}
