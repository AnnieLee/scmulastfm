package com.example.mobilelastfm;

import android.app.TabActivity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class EventsTabActivity extends TabActivity {
	
	public double latitude;
	public double longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_events_tab);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (MainActivity.wifi.isWifiEnabled()) {
			TabHost tabHost = getTabHost();

			// Tab for Photos
			TabSpec listspec = tabHost.newTabSpec("List");
			listspec.setIndicator("List");
			// setting Title and Icon for the Tab
			Intent listIntent = new Intent(this, EventsListActivity.class);
			listspec.setContent(listIntent);

			// Tab for Songs
			TabSpec mapspec = tabHost.newTabSpec("Map");
			mapspec.setIndicator("Map");
			Intent mapIntent = new Intent(this, EventsActivity.class);
			mapspec.setContent(mapIntent);

			// Adding all TabSpec to TabHost
			tabHost.addTab(listspec);
			tabHost.addTab(mapspec);

		} else
			Toast.makeText(getApplicationContext(), R.string.wifi_off,
					Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.events_tab, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		switch(item.getItemId())
		{
		case android.R.id.home:
			if (MainActivity.wifi.isWifiEnabled())
			{
				intent = new Intent(this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			else
				Toast.makeText(getApplicationContext(), R.string.wifi_off, Toast.LENGTH_LONG).show();
			return true;
		case R.id.action_book:
			intent = new Intent(this, BookmarkTabActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.action_events:
			if (MainActivity.wifi.isWifiEnabled())
			{
				intent = new Intent(this, EventsTabActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			else
				Toast.makeText(getApplicationContext(), R.string.wifi_off, Toast.LENGTH_LONG).show();
			return true;
		case R.id.action_friends:
			if (!mBtAdapter.enable())
				Toast.makeText(getApplicationContext(), "Please turn your bluetooth on", Toast.LENGTH_LONG).show();
			else
			{
				intent = new Intent(this, FriendsTabActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			return true;
		case R.id.action_chat:
			if (!MainActivity.wifi.isWifiEnabled())
				Toast.makeText(getApplicationContext(), R.string.wifi_off, Toast.LENGTH_LONG).show();
			else if (!mBtAdapter.enable())
				Toast.makeText(getApplicationContext(), "Please turn your bluetooth on", Toast.LENGTH_LONG).show();
			else
			{
				intent = new Intent(this, FriendsToConnectActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	

}
