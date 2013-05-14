package com.example.mobilelastfm;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class FriendsTabActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_friends_tab);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (MainActivity.wifi.isWifiEnabled()) {
			TabHost tabHost = getTabHost();

			// Tab for Photos
			TabSpec friendspec = tabHost.newTabSpec("My Friends");
			friendspec.setIndicator("My Friends");
			// setting Title and Icon for the Tab
			Intent friendsIntent = new Intent(this, FriendsActivity.class);
			friendspec.setContent(friendsIntent);

			// Tab for Videos
			TabSpec scanspec = tabHost.newTabSpec("Find");
			scanspec.setIndicator("Find");
			Intent scanIntent = new Intent(this, ScanFriendsActivity.class);
			scanspec.setContent(scanIntent);


			// Adding all TabSpec to TabHost
			tabHost.addTab(friendspec);
			tabHost.addTab(scanspec);

			setProgressBarIndeterminateVisibility(false);

		}
		else
			Toast.makeText(getApplicationContext(), R.string.wifi_off, Toast.LENGTH_LONG).show();
		
		setProgressBarIndeterminateVisibility(false);

		// setProgressBarIndeterminateVisibility(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friends_tab, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;

		if (MainActivity.wifi.isWifiEnabled()) {
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
				intent = new Intent(this, FriendsToConnectActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
		}
		else {
			Toast.makeText(getApplicationContext(), R.string.wifi_off,
					Toast.LENGTH_LONG).show();
			return false;
		}
	}

}
