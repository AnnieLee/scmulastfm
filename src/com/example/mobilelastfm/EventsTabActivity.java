package com.example.mobilelastfm;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TabHost;
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

		TabHost tabHost = getTabHost();

		// Tab for Photos
		TabSpec listspec = tabHost.newTabSpec("List");
		listspec.setIndicator("List");
		// setting Title and Icon for the Tab
		Intent listIntent = new Intent(this,EventsListActivity.class);
		listspec.setContent(listIntent);

		// Tab for Songs
		TabSpec mapspec = tabHost.newTabSpec("Map");      
		mapspec.setIndicator("Map");
		Intent mapIntent = new Intent(this, EventsActivity.class);
		mapspec.setContent(mapIntent);

		// Adding all TabSpec to TabHost
		tabHost.addTab(listspec);
		tabHost.addTab(mapspec);
	
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
			intent = new Intent(this, FriendsActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	

}
