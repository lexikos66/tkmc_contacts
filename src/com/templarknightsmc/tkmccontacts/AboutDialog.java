package com.templarknightsmc.tkmccontacts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.widget.TextView;

public class AboutDialog extends Dialog {
	private static Context context = null;
	
	public AboutDialog(Context _context) {
		super(_context);
		context = _context;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.dialog_about);
		
		TextView tv = (TextView)findViewById(R.id.legal_text);
		tv.setText(readRawTextFile(R.raw.legal));
		tv = (TextView)findViewById(R.id.info_text);
		tv.setText(Html.fromHtml(readRawTextFile(R.raw.info)));
		String versionName = "1.0";
		try {
			versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) { }
		tv.append("\nVersion " + versionName + "\nCopyright 2013");
		tv.setLinkTextColor(Color.LTGRAY);
		Linkify.addLinks(tv, Linkify.ALL);
		tv = (TextView)findViewById(R.id.web_text);
		tv.setText(Html.fromHtml(readRawTextFile(R.raw.web)));
		tv.setLinkTextColor(Color.LTGRAY);
		Linkify.addLinks(tv,  Linkify.ALL);
	}
	
	/**
	 * Read text file
	 */
	public static String readRawTextFile(int id) {
		InputStream inputStream = context.getResources().openRawResource(id);
		InputStreamReader in = new InputStreamReader(inputStream);
		BufferedReader buf = new BufferedReader(in);
		String line;
		StringBuilder text = new StringBuilder();
		try {
			while ((line = buf.readLine()) != null) text.append(line);
		} catch (IOException e) {
			return null;
		}
		return text.toString();
	}

}
