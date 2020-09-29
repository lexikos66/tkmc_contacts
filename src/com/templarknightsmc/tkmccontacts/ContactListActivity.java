package com.templarknightsmc.tkmccontacts;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ListActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.ArrayAdapter;

public class ContactListActivity extends ListActivity {
	private String lodgeName;
	public static final int MENU_CALL = 1;
	public static final int MENU_TEXT = 2;
	public static final int MENU_EMAIL = 3;
	public static int MENU_ACTION = 0;
	private SharedPreferences sharedPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerForContextMenu(getListView());
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ContactListActivity.this);

		// Get the lodge from the intent
		Intent intent = getIntent();
		this.lodgeName = intent.getStringExtra(MainActivity.LODGE_NAME);
		ContactList contactList = this.getContacts();
		ArrayAdapter<Contact> adapter = new ContactAdapter(this, contactList.getContacts());
		setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView list, View view, int position, long id) {
		super.onListItemClick(list, view, position, id);
		Object o = this.getListAdapter().getItem(position);
		Contact contact = (Contact)o;
		Toast.makeText(this, "'Long Click' to Text, Call or Email:\n" + contact.getNickname(), Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		ContactList contactList = this.getContacts();
		ArrayAdapter<Contact> adapter = new ContactAdapter(this, contactList.getContacts());
		setListAdapter(adapter);
		Configuration.LODGE_PREF = sharedPrefs.getString("lodgePref", "0");
	}
	
	/**
	 * Get contacts to populate Contact ListView
	 * 
	 * @return ContactList
	 */
	private ContactList getContacts() {
		TkmcDbHelper dbHelper = new TkmcDbHelper("r");
		Configuration.SORT_ORDER = sharedPrefs.getString("sortPref", "1");
		return dbHelper.getContacts(this.lodgeName);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_contact_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.menu_settings:
			Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(ContactListActivity.this, TkmcPreferenceActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		menu.add(Menu.NONE, MENU_CALL, Menu.NONE, "Call");
		menu.add(Menu.NONE, MENU_TEXT, Menu.NONE, "Text");
		menu.add(Menu.NONE, MENU_EMAIL, Menu.NONE, "Email");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case MENU_CALL:
			Object o = this.getListAdapter().getItem(info.position);
			Contact contact = (Contact)o;
			Toast.makeText(this, contact.getDisplayName(), Toast.LENGTH_SHORT).show();
			String phoneCallUri = "tel:" + contact.getPhone();
			Intent intent = new Intent(Intent.ACTION_CALL);
			intent.setData(Uri.parse(phoneCallUri));		
			startActivity(intent);
			return true;
		case MENU_TEXT:
			o = this.getListAdapter().getItem(info.position);
			contact = (Contact)o;
			Intent sendIntent = new Intent (Intent.ACTION_VIEW);
			sendIntent.setType("vnd.android-dir/mms-sms");
			sendIntent.putExtra("address",  contact.getPhone());
			startActivity(sendIntent);
			return true;
		case MENU_EMAIL:
			o = this.getListAdapter().getItem(info.position);
			contact = (Contact)o;
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {contact.getEmail()});
			emailIntent.setType("plain/text");
			startActivity(Intent.createChooser(emailIntent,  "Send email..."));
			return true;
		default:
			return super.onContextItemSelected(item);	
		}
	}

}
