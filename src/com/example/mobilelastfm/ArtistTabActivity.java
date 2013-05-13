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
public class ArtistTabActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_artist_tab);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		boolean active_data = intent.getBooleanExtra(MainActivity.ACTIVE_DATA, true);

		if (MainActivity.wifi.isWifiEnabled()) {
			String artist;
			if (active_data)
				artist = ActiveData.artist.getName();
			else
				artist = intent.getStringExtra(MainActivity.ARTIST);

			getActionBar().setTitle(artist);

			TabHost tabHost = getTabHost();

			TabSpec artistspec = tabHost.newTabSpec("Artist");
			artistspec.setIndicator("Artist");
			Intent artistIntent = new Intent(this, ArtistInfoActivity.class);
			if (!active_data) {
				artistIntent.putExtra(MainActivity.ACTIVE_DATA, active_data);
				artistIntent.putExtra(MainActivity.ARTIST, artist);
			}
			artistspec.setContent(artistIntent);

			TabSpec albumspec = tabHost.newTabSpec("Albuns");
			albumspec.setIndicator("Albuns");
			Intent albunsIntent = new Intent(this, ArtistAlbunsActivity.class);
			albumspec.setContent(albunsIntent);

			TabSpec eventspec = tabHost.newTabSpec("Events");
			eventspec.setIndicator("Events");
			Intent eventsIntent = new Intent(this, ArtistEventsActivity.class);
			eventspec.setContent(eventsIntent);

			// Adding all TabSpec to TabHost
			tabHost.addTab(artistspec);
			tabHost.addTab(albumspec);
			tabHost.addTab(eventspec);

			setProgressBarIndeterminateVisibility(false);
		} else
			Toast.makeText(getApplicationContext(), "Please turn on your WiFi",
					Toast.LENGTH_LONG).show();
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
