package com.templarknightsmc.tkmccontacts;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
	
	private Spinner spinner;
	private ArrayAdapter<String> dataAdapter;
	public final static String LODGE_NAME = "com.templarknightsmc.tkmccontacts.LODGE_NAME";
	private SharedPreferences sharedPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Get Shared Preferences
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
		Configuration.LODGE_PREF = sharedPrefs.getString("lodgePref",  "0");
		
		/* Instantiate DataRetrieval object which will create "downloads" and "databases" directories and create
		 * database tables if necessary
		 */ 
		DataRetrieval dr = new DataRetrieval(this);
		Configuration.SHOW_VERSION_CHECK_DIALOG = true;
		dr.execute(Configuration.URL_PATH);
		addItemsOnSpinner();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		dataAdapter.notifyDataSetChanged();
		Configuration.LODGE_PREF = sharedPrefs.getString("lodgePref", "0");
		spinner.setSelection(Integer.parseInt(Configuration.LODGE_PREF));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			Toast.makeText(this, "Refresh Data", Toast.LENGTH_SHORT).show();
			refreshData();
			return true;
		case R.id.menu_about:
			AboutDialog about = new AboutDialog(this);
			about.setTitle("About");
			about.show();
			return true;
		case R.id.menu_settings:
			Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(MainActivity.this, TkmcPreferenceActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * void refreshData()
	 * 
	 * Provides action for the menu_refresh option
	 */
	private void refreshData() {
		// Clear out current tables
		Configuration.REFRESH_DATA = true;
		TkmcDbHelper dbHelper = new TkmcDbHelper("w");
		dbHelper.refreshData();
		dbHelper.db.close();
		
		//File dbFile = new File(Configuration.DATABASE_FILE_PATH, Configuration.DATABASE_NAME);
		//dbFile.delete();
		//dbFile = new File(Configuration.DATABASE_FILE_PATH, Configuration.DATABASE_NAME + "-journal");
		//dbFile.delete();
		
		DataRetrieval dr = new DataRetrieval(this);
		Configuration.SHOW_UPDATE_DIALOG = true;
		dr.execute(Configuration.URL_PATH);
	}
	
	/**
	 * private void showAbout()
	 * 
	 * Provides action for the menu_about option
	 */
	private void showAbout() {
		Toast.makeText(this, "TKMC Contacts\n\nGas Frog\nAll Rights Reserved 2013\n\nCreator:Hollywood", Toast.LENGTH_LONG).show();
	}
	
	/**
	 * public void addItemsOnSpinner()
	 * 
	 * Reads a database and populates the lodge dropdown spinner
	 */
	public void addItemsOnSpinner() {
		TkmcDbHelper dbHelper = new TkmcDbHelper("r");
		List<String> list = new ArrayList<String>();
		list = dbHelper.getLodges();
		spinner = (Spinner) findViewById(R.id.spinner);
		dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
		spinner.setSelection(Integer.parseInt(Configuration.LODGE_PREF));
	}
	
	/**
	 * public void selectLodge(View)
	 * 
	 * Called when the user clicks the submit button
	 */
	public void selectLodge(View view) {
		Intent intent = new Intent(this, ContactListActivity.class);
		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		String lodge = spinner.getSelectedItem().toString();
		intent.putExtra(LODGE_NAME, lodge);
		startActivity(intent);
	}

}
