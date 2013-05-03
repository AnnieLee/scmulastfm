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
public class BookmarkTabActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_bookmark_tab);

		getActionBar().setTitle("Bookmarks");

		getActionBar().setDisplayHomeAsUpEnabled(true);

		TabHost tabHost = getTabHost();

		// Tab for Photos
		TabSpec artistspec = tabHost.newTabSpec("Artists");
		artistspec.setIndicator("Artists");
		// setting Title and Icon for the Tab
		Intent artistIntent = new Intent(this, BookmarkArtistActivity.class);
		artistspec.setContent(artistIntent);

		// Tab for Songs
		TabSpec albumspec = tabHost.newTabSpec("Albuns");      
		albumspec.setIndicator("Albuns");
		Intent albunsIntent = new Intent(this, BookmarkAlbumActivity.class);
		albumspec.setContent(albunsIntent);

		// Tab for Videos
		TabSpec eventspec = tabHost.newTabSpec("Events");
		eventspec.setIndicator("Events");
		Intent eventsIntent = new Intent(this, BookmarkEventActivity.class);
		eventspec.setContent(eventsIntent);

		// Adding all TabSpec to TabHost
		tabHost.addTab(artistspec);
		tabHost.addTab(albumspec);
		tabHost.addTab(eventspec);

		setProgressBarIndeterminateVisibility(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bookmark_tab, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
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
			intent = new Intent(this, EventsActivity.class);
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
