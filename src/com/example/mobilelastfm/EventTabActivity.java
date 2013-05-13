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
public class EventTabActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_artist_tab);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		boolean active_data = intent.getBooleanExtra(MainActivity.ACTIVE_DATA, true);
		String event;
		if (active_data)
			event = ActiveData.event.getTitle();
		else
			event = intent.getStringExtra(MainActivity.EVENT);
		
		getActionBar().setTitle(event);
		
		TabHost tabHost = getTabHost();

		// Tab for Photos
		TabSpec eventspec = tabHost.newTabSpec("Info");
		eventspec.setIndicator("Info");
		// setting Title and Icon for the Tab
		Intent eventIntent = new Intent(this, EventInfoActivity.class);
		if (!active_data)
		{
			eventIntent.putExtra(MainActivity.ACTIVE_DATA, active_data);
			String name = intent.getStringExtra(MainActivity.EVENT);
			int id = intent.getIntExtra(MainActivity.EVENT_ID, -1);
			eventIntent.putExtra(MainActivity.EVENT, name);
			eventIntent.putExtra(MainActivity.EVENT_ID, id);
		}
		eventspec.setContent(eventIntent);

		// Tab for Videos
		TabSpec mapspec = tabHost.newTabSpec("Map");
		mapspec.setIndicator("Map");
		Intent mapIntent = new Intent(this, EventMapActivity.class);
		mapspec.setContent(mapIntent);

		// Adding all TabSpec to TabHost
		tabHost.addTab(eventspec);
		tabHost.addTab(mapspec);
		
		setProgressBarIndeterminateVisibility(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.artist_tab, menu);
		return true;
	}
	
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
			intent = new Intent(this, FriendsToConnectActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
