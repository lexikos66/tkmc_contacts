package com.templarknightsmc.tkmccontacts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

public class TkmcPreferenceActivity extends PreferenceActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);

	    // Create Dynamic Lodge Preference List
	    File file = new File(Configuration.DOWNLOAD_FILE_PATH, Configuration.LODGES_FILE_NAME);
		
	    final List<String> lodgeEntries = new ArrayList<String>();
	    final List<String> lodgeEntryValues = new ArrayList<String>();
		if (file.exists()) {
		    BufferedReader inFile;
			try {
				inFile = new BufferedReader(new FileReader(file));
				String lodgeName = "";
				int i = 0;
				while ((lodgeName = inFile.readLine()) != null) {
					String[] items = lodgeName.split(",");
					lodgeEntries.add(items[1]);
					lodgeEntryValues.add(Integer.toString(i));
					i++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		    final CharSequence[] entries = lodgeEntries.toArray(new CharSequence[lodgeEntries.size()]);
		    final CharSequence[] entryValues = lodgeEntryValues.toArray(new CharSequence[lodgeEntryValues.size()]);

		    ListPreference listPref = (ListPreference) findPreference((CharSequence)"lodgePref");
		    listPref.setEntries(entries);
			listPref.setEntryValues(entryValues);
			
		}

	}

}
