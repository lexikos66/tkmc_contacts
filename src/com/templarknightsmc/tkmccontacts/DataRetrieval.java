package com.templarknightsmc.tkmccontacts;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class DataRetrieval extends AsyncTask<String, Integer, String> {
	private TkmcDbHelper dbHelper;
	protected MainActivity context;
	private String urlPath = "";
	private ProgressDialog progressDialog;
	
	public DataRetrieval(MainActivity _context) {
		this.context = _context;
		File downloadsDirectory = new File(Configuration.DOWNLOAD_FILE_PATH);
		downloadsDirectory.mkdirs();
		dbHelper = new TkmcDbHelper("w");
	}
	
	@Override
	protected void onPreExecute() {
		// Decide which dialogue to show
		if (Configuration.SHOW_UPDATE_DIALOG) {
			progressDialog = ProgressDialog.show(context, "Updating Records", "So let it be written, so let it be done! Poof...");
		} else if (Configuration.SHOW_VERSION_CHECK_DIALOG) {
			progressDialog = ProgressDialog.show(context,  "Checking Version",  "If this is staying up long enough for you to read it, then, Dagone it and woot, woot! . . . Downloading new data for ya.  You can Thank me later!");
		} else {
			progressDialog = ProgressDialog.show(context,"Dagone It", "You don't have any records in the database! Let me take care of that for you...");
		}
	}

	protected String doInBackground(String... urls) {
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			try {
				// params comes from the execute() call: params[0] is the url.
				urlPath = urls[0];
				// Check version numbers
				downloadFile(urlPath, Configuration.VERSIONS_FILE_NAME);
				checkVersions();
				if (Configuration.DOWNLOAD_CONTENT) {
					// Download the contacts and lodges files from the TKMC website
					downloadFile(urlPath, Configuration.CONTACT_FILE_NAME);
					downloadFile(urlPath, Configuration.LODGES_FILE_NAME);
					// Write information from the contacts and lodges files to the database
					writeLodges();
					writeContacts();
					writeDataVersion();
				}
			} catch (IOException e) {
				return "File IO Exception.";
			}
		} else {
			Log.i("DataRetrieval", "No network connection available.");
		}
		return "Success";
	}
	
	/**
	 * Check Versions Strings
	 */
	public void checkVersions() {
		File file = new File(Configuration.DOWNLOAD_FILE_PATH, Configuration.VERSIONS_FILE_NAME);
		if (file.exists()) {
			try {
				BufferedReader inFile;
				inFile = new BufferedReader(new FileReader(file));
				String fileDataVersion = inFile.readLine();
				String dbDataVersion = this.dbHelper.getDataVersion();
				if (Integer.parseInt(fileDataVersion) > Integer.parseInt(dbDataVersion)) {
					this.dbHelper.refreshData();
				}
				inFile.close();
			} catch (IOException e) {}
		}
	}
	
	/**
	 * Write contacts from file to the database.
	 */
	public void writeContacts() {	
		ContentValues values = new ContentValues();
		long newRowId;
		File file = new File(Configuration.DOWNLOAD_FILE_PATH, Configuration.CONTACT_FILE_NAME);
		BufferedReader inFile;
		try {
			inFile = new BufferedReader(new FileReader(file));
			String contact = "";
			while ((contact = inFile.readLine()) != null) {
				String[] items = contact.split(",");
				values.put(TkmcDbContract.TkmcDbContacts.COLUMN_NAME_FIRST_NAME, items[0]);
				values.put(TkmcDbContract.TkmcDbContacts.COLUMN_NAME_LAST_NAME, items[1]);
				values.put(TkmcDbContract.TkmcDbContacts.COLUMN_NAME_NICKNAME, items[2]);
				values.put(TkmcDbContract.TkmcDbContacts.COLUMN_NAME_RANK, items[3]);
				values.put(TkmcDbContract.TkmcDbContacts.COLUMN_NAME_PHONE, items[4]);
				values.put(TkmcDbContract.TkmcDbContacts.COLUMN_NAME_EMAIL, items[5]);
				values.put(TkmcDbContract.TkmcDbContacts.COLUMN_NAME_LODGE, items[6]);
				values.put(TkmcDbContract.TkmcDbContacts.COLUMN_NAME_RECORD_NUM, items[7]);
				newRowId = this.dbHelper.db.insert(TkmcDbContract.TkmcDbContacts.TABLE_NAME, null, values);
				values.clear();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Write lodge information to the database
	 */
	public void writeLodges() {
		ContentValues values = new ContentValues();
		long newRowId;
		File file = new File(Configuration.DOWNLOAD_FILE_PATH, Configuration.LODGES_FILE_NAME);
		BufferedReader inFile;
		try {
			inFile = new BufferedReader(new FileReader(file));
			String lodgeName = "";
			while ((lodgeName = inFile.readLine()) != null) {
				String[] items = lodgeName.split(",");
				values.put(TkmcDbContract.TkmcDbLodges.COLUMN_NAME_LODGE_NUM, items[0]);
				values.put(TkmcDbContract.TkmcDbLodges.COLUMN_NAME_LODGE_NAME, items[1]);
				newRowId = this.dbHelper.db.insert(TkmcDbContract.TkmcDbLodges.TABLE_NAME, null, values);
				values.clear();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Write data version information to the database
	 */
	public void writeDataVersion() {
		File file = new File(Configuration.DOWNLOAD_FILE_PATH, Configuration.VERSIONS_FILE_NAME);
		BufferedReader inFile;
		try {
			inFile = new BufferedReader(new FileReader(file));
			String versionNum = "";
			versionNum = inFile.readLine();
			this.dbHelper.db.execSQL("UPDATE " + TkmcDbContract.TkmcDbVersions.TABLE_NAME + " SET " +
				TkmcDbContract.TkmcDbVersions.COLUMN_NAME_VERSION_NUM + "='" + versionNum + "' WHERE " + 
				TkmcDbContract.TkmcDbVersions.COLUMN_NAME_VERSION_TYPE + "='Data' AND " +
				TkmcDbContract.TkmcDbVersions.COLUMN_NAME_APP_NAME + "='TKMC Contacts'");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/**
	 * Downloads the contacts and lodges files from the TKMC website
	 * 
	 * @param urlPath
	 * @param fileName
	 * @throws IOException
	 */
	private void downloadFile(String urlPath, String fileName ) throws IOException {
		InputStream is = null;
		try {
			URL url = new URL(urlPath + fileName);
			Log.d("DEBUG - Find Crash", "url is: " + url);
			Log.e("DEBUG - Find Crash", "url is: " + url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.connect();
			int response = conn.getResponseCode();
			Log.d("DEBUG - DataRetrieval", "The response is: " + response);
			is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
		    // Read bytes to the Buffer until there is nothing more to read(-1).
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
		    int current = 0;
		    while ((current = bis.read()) != -1) {
		    	baf.append((byte) current);
		    } 

		    /* Convert the Bytes read to a String. */
		    File file = new File(Configuration.DOWNLOAD_FILE_PATH, fileName);
		    FileOutputStream fos = new FileOutputStream(file);
		    fos.write(baf.toByteArray());
		    fos.close();
		} catch (IOException e) {
			Log.e("TKMCContacts:DataRetrieval", "Error: " + e);
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}
	
	@Override
	protected void onPostExecute(String result) {
		// Reset all flags
		Configuration.DOWNLOAD_CONTENT = false;
		Configuration.SHOW_UPDATE_DIALOG = false;
		Configuration.SHOW_VERSION_CHECK_DIALOG = false;
		Configuration.REFRESH_DATA = false;
		
		List<String> list = new ArrayList<String>();
		list = this.dbHelper.getLodges();
		Spinner spinner = (Spinner) context.findViewById(R.id.spinner);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
		spinner.setSelection(Integer.parseInt(Configuration.LODGE_PREF));
		dataAdapter.notifyDataSetChanged();

		progressDialog.dismiss();
		this.dbHelper.db.close();
	}
}	
