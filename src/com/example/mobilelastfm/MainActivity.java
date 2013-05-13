package com.example.mobilelastfm;

import ormdroid.ORMDroidApplication;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity  extends Activity {

	public static final String ACTIVE_DATA = "ACTIVE_DATA";
	public static final String ARTIST = "ARTIST";
	public static final String ALBUM = "ALBUM";
	public static final String EVENT = "EVENT";
	public static final String EVENT_ID = "EVENT_ID";

	public static final int expectedSize = 1000;
	public final static String EXTRA_MESSAGE = "com.example.mobilelastfm.MESSAGE";
	public static String API_KEY = "029fe710ea7af934b46f8da780722083";

	public static WifiManager wifi;
	public static BluetoothAdapter bAdapter;

	ViewPager mViewPager;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		getActionBar().setTitle("LastFM");

		//		SQLiteDatabase database = dbHelper.getWritableDatabase();
		//		Ormdroid ormdroid = new Ormdroid(database);


		ORMDroidApplication.initialize(getApplicationContext());		
		
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch(item.getItemId())
		{
		case android.R.id.home:
			intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.action_book:
			intent = new Intent(this, BookmarkTabActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.action_events:
			intent = new Intent(this, EventsTabActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.action_friends:
			intent = new Intent(this, FriendsTabActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.action_chat:
			intent = new Intent(this, BluetoothChatActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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
