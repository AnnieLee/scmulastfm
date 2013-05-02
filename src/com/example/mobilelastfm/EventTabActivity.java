package com.example.mobilelastfm;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import de.umass.lastfm.Event;

@SuppressWarnings("deprecation")
public class EventTabActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_artist_tab);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Event event = C.event;
		getActionBar().setTitle(event.getTitle());

		TabHost tabHost = getTabHost();

		// Tab for Photos
		TabSpec eventspec = tabHost.newTabSpec("Info");
		eventspec.setIndicator("Info");
		// setting Title and Icon for the Tab
		Intent eventIntent = new Intent(this, EventInfoActivity.class);
		eventspec.setContent(eventIntent);

		// Tab for Videos
		TabSpec mapspec = tabHost.newTabSpec("Map");
		mapspec.setIndicator("Map");
		Intent mapIntent = new Intent(this, MapActivity.class);
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
		switch(item.getItemId())
		{
		case android.R.id.home:
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;	
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
