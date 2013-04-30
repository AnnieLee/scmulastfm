package com.example.mobilelastfm;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import de.umass.lastfm.Event;

public class EventActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_event);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		Event event = C.event;
		getActionBar().setTitle(event.getTitle());
		
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event, menu);
		return true;
	}

}
