package com.example.mobilelastfm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity  extends Activity {

	public final static String EXTRA_MESSAGE = "com.example.mobilelastfm.MESSAGE";
	public static String API_KEY = "029fe710ea7af934b46f8da780722083";

	public WifiManager wifi;

	ViewPager mViewPager;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		getActionBar().setTitle("LastFM");
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void clear_text(View view) {
		EditText text = (EditText) findViewById(R.id.search_bar);
		String txt = text.getText().toString();
		if (txt.equals("Search artist"))
		{
			text.setTextColor(Color.BLACK);
			text.setText("");
		}
	}

	public void search(View view) {
		EditText text = (EditText) findViewById(R.id.search_bar);

		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(text.getWindowToken(), 0);

		if (wifi.isWifiEnabled())
		{

			String artist = text.getText().toString();

			Intent intent = new Intent(this, ArtistsActivity.class);
			intent.putExtra(EXTRA_MESSAGE, artist);
			startActivity(intent);
		}
		else
			Toast.makeText(getApplicationContext(), "Please turn your WiFi", Toast.LENGTH_LONG).show();
	}

}
