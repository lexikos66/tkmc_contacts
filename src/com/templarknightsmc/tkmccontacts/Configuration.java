package com.templarknightsmc.tkmccontacts;

import android.os.Environment;

public class Configuration {
	
	public static final String URL_PATH = "http://templarknightsmc.com/data/";

	public static final String DATABASE_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.templarknightsmc.tkmccontacts/databases";
	public static final String DATABASE_NAME = "tkmccontacts.db";
	public static final int DATABASE_VERSION = 1;
	
	public static final String CONTACTS_CREATE_TABLE = 
			"CREATE TABLE " + TkmcDbContract.TkmcDbContacts.TABLE_NAME + " (" + TkmcDbContract.TkmcDbContacts.COLUMN_NAME_RECORD_NUM + " INTEGER PRIMARY KEY, " +
			TkmcDbContract.TkmcDbContacts.COLUMN_NAME_FIRST_NAME + " TEXT, " +
			TkmcDbContract.TkmcDbContacts.COLUMN_NAME_LAST_NAME + " TEXT, " +
			TkmcDbContract.TkmcDbContacts.COLUMN_NAME_NICKNAME + " TEXT, " +
			TkmcDbContract.TkmcDbContacts.COLUMN_NAME_RANK + " TEXT, " +
			TkmcDbContract.TkmcDbContacts.COLUMN_NAME_PHONE + " TEXT, " +
			TkmcDbContract.TkmcDbContacts.COLUMN_NAME_EMAIL + " TEXT, " +
			TkmcDbContract.TkmcDbContacts.COLUMN_NAME_LODGE + " TEXT " + ")";
	public static final String LODGES_CREATE_TABLE = 
			"CREATE TABLE " + TkmcDbContract.TkmcDbLodges.TABLE_NAME + " (" + TkmcDbContract.TkmcDbLodges.COLUMN_NAME_LODGE_NUM + " INTEGER PRIMARY KEY, " +
			TkmcDbContract.TkmcDbLodges.COLUMN_NAME_LODGE_NAME + " TEXT" + ")";
	public static final String VERSIONS_CREATE_TABLE = 
			"CREATE TABLE " + TkmcDbContract.TkmcDbVersions.TABLE_NAME + " (" + TkmcDbContract.TkmcDbVersions.COLUMN_NAME_VERSION_NUM + " INTEGER, " +
			TkmcDbContract.TkmcDbVersions.COLUMN_NAME_APP_NAME + " TEXT, " +
			TkmcDbContract.TkmcDbVersions.COLUMN_NAME_VERSION_TYPE + " TEXT" + ")";
	
	public static final String DOWNLOAD_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.templarknightsmc.tkmccontacts/downloads";
	public static final String CONTACT_FILE_NAME = "tkmccontacts.csv";
	public static final String LODGES_FILE_NAME = "tkmclodges.csv";
	public static final String VERSIONS_FILE_NAME = "tkmccdv.csv";
	public static boolean DOWNLOAD_CONTENT = false;
	public static boolean REFRESH_DATA = false;
	public static boolean SHOW_UPDATE_DIALOG = false;
	public static boolean SHOW_VERSION_CHECK_DIALOG = false;
	public static String SORT_ORDER = "1";
	public static String LODGE_PREF = "0";
}
