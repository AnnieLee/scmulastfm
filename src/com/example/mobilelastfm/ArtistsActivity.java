package com.example.mobilelastfm;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import de.umass.lastfm.Artist;
import de.umass.lastfm.CallException;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class ArtistsActivity extends Activity {

	public static String API_KEY = "&api_key=029fe710ea7af934b46f8da780722083";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artists);

		Intent intent = getIntent();
		String result = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		TextView text = (TextView) findViewById(R.id.artist_view);

		
		


		/*Collection<Artist> artists = Artist.search(result, API_KEY);
		Iterator<Artist> artist_it = artists.iterator();

		while (artist_it.hasNext()) {
			Artist artist = artist_it.next();
			String current_text = text.getText().toString() + "\n";
			text.setText(current_text + artist.getName());
		}*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.artists, menu);
		return true;
	}

}
