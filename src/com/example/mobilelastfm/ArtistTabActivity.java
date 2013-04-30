package com.example.mobilelastfm;

import de.umass.lastfm.Artist;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class ArtistTabActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_artist_tab);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Artist artist = C.artist;
		getActionBar().setTitle(artist.getName());

		TabHost tabHost = getTabHost();

		// Tab for Photos
		TabSpec artistspec = tabHost.newTabSpec("Artist");
		artistspec.setIndicator("Artist");
		// setting Title and Icon for the Tab
		Intent artistIntent = new Intent(this, ArtistInfoActivity.class);
		artistspec.setContent(artistIntent);

		// Tab for Songs
		TabSpec albumspec = tabHost.newTabSpec("Albuns");      
		albumspec.setIndicator("Albuns");
		Intent albunsIntent = new Intent(this, ArtistAlbunsActivity.class);
		albumspec.setContent(albunsIntent);

		// Tab for Videos
		TabSpec eventspec = tabHost.newTabSpec("Events");
		eventspec.setIndicator("Events");
		Intent eventsIntent = new Intent(this, ArtistEventsActivity.class);
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
