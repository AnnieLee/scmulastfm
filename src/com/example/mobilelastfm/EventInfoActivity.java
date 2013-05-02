package com.example.mobilelastfm;

import webimageview.WebImageView;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.widget.TextView;
import de.umass.lastfm.Event;
import de.umass.lastfm.ImageSize;
import de.umass.lastfm.Venue;

public class EventInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_info);
		
		Event event = C.event;

		WebImageView image = (WebImageView) findViewById(R.id.event_image);
		image.setImageWithURL(getApplicationContext(), event.getImageURL(ImageSize.LARGE));

		TextView title = (TextView) findViewById(R.id.title);
		title.setText(event.getTitle());

		TextView txt = (TextView) findViewById(R.id.info);

		Venue venue = event.getVenue();
		txt.setText(venue.getCity() + ", " + venue.getCountry());
		txt.append(Html.fromHtml("<br/>" + EventDate.getDuration(event)));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_info, menu);
		return true;
	}

}
