package com.example.mobilelastfm;

import java.util.Iterator;

import webimageview.WebImageView;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import de.umass.lastfm.Event;
import de.umass.lastfm.ImageSize;

public class EventActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_event);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		Event event = C.event;
		getActionBar().setTitle(event.getTitle());

		if (MainActivity.wifi.isWifiEnabled()) {
			WebImageView image = (WebImageView) findViewById(R.id.event_image);
			image.setImageWithURL(getApplicationContext(),
					event.getImageURL(ImageSize.LARGE));

			TextView title = (TextView) findViewById(R.id.title);
			title.setText(event.getTitle());

			TextView date = (TextView) findViewById(R.id.date);
			date.setText(Html.fromHtml("<strong>When: </strong>"
					+ EventDate.getDuration(event)));

			TextView artists = (TextView) findViewById(R.id.artists);
			artists.setText(Html.fromHtml("<strong>Artists:<br/></strong>"));
			Iterator<String> it = event.getArtists().iterator();
			while (it.hasNext()) {
				artists.append(it.next() + "\n");
			}
		} else
			Toast.makeText(getApplicationContext(), "Please turn on your WiFi",
					Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event, menu);
		return true;
	}

}
