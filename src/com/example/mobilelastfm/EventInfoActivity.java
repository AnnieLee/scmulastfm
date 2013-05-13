package com.example.mobilelastfm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ormdroid.Entity;
import webimageview.WebImageView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import database_entities.EventBookmark;
import de.umass.lastfm.Caller;
import de.umass.lastfm.Event;
import de.umass.lastfm.ImageSize;
import de.umass.lastfm.Venue;

public class EventInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_info);

		Intent intent = getIntent();
		boolean active_data = intent.getBooleanExtra(MainActivity.ACTIVE_DATA,
				true);
		if (MainActivity.wifi.isWifiEnabled()) {
			if (active_data) {
				Event event = ActiveData.event;
				fill_content(event);
			} else {

				String event = intent.getIntExtra(MainActivity.EVENT_ID, -1)
						+ "";
				new EventTask().execute(event);
			}
		} else
			Toast.makeText(getApplicationContext(), "Please turn on your WiFi",
					Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_info, menu);
		return true;
	}

	public void bookmark(View view) {
		CheckBox box = (CheckBox) findViewById(R.id.favorite);
		boolean checked = box.isChecked();
		Event event = ActiveData.event;
		EventBookmark e = Entity.query(EventBookmark.class).where("name").eq(event.getTitle()).execute();
		if (checked && e == null)
		{
			e = new EventBookmark();
			e.lid = event.getId();
			e.name = event.getTitle();
			e.poster = event.getImageURL(ImageSize.MEDIUM);
			e.venue = event.getVenue().getName() + "\n" + event.getVenue().getCity() + ", " + event.getVenue().getCountry();
			e.date = EventDate.getDuration(event);
			e.save();
			Toast.makeText(getApplicationContext(), "Event bookmarked with success!", Toast.LENGTH_LONG).show();
		}
		else if (checked && e != null)
		{
			Toast.makeText(getApplicationContext(), "Event already bookmarked", Toast.LENGTH_LONG).show();
		}
		else if (!checked && e == null)
		{
			Toast.makeText(getApplicationContext(), "Event needs to be bookmarked to be removed", Toast.LENGTH_LONG).show();
		}
		else
		{
			e.delete();
			Toast.makeText(getApplicationContext(), "Event removed with success!", Toast.LENGTH_LONG).show();
		}
	}
	
	private void onItemClicked(String item) {
		Intent intent = new Intent(getApplicationContext(), ArtistTabActivity.class);
		intent.putExtra(MainActivity.ACTIVE_DATA, false);
		intent.putExtra(MainActivity.ARTIST, item);
		startActivity(intent);
	}
	
	private void fill_content(Event event) {
		WebImageView image = (WebImageView) findViewById(R.id.event_image);
		image.setImageWithURL(getApplicationContext(), event.getImageURL(ImageSize.LARGE));

		TextView title = (TextView) findViewById(R.id.title);
		title.setText(event.getTitle());

		TextView txt = (TextView) findViewById(R.id.info);

		Venue venue = event.getVenue();
		txt.setText(venue.getName() + "\n" + venue.getCity() + ", " + venue.getCountry());
		txt.append(Html.fromHtml("<br/>" + EventDate.getDuration(event)));
		Collection<String> artists = event.getArtists();

		List<String> list = null;
		if(artists instanceof List){
			list = (List<String>) artists;
		}else{
			list = new ArrayList<String>(artists);
		}

		View header = LayoutInflater.from(getApplicationContext()).inflate(R.layout.text_list_header, null);
		TextView header_txt = (TextView) header.findViewById(R.id.header);
		header_txt.setText("Artists");
		ListView lv = (ListView) findViewById(R.id.artists);
		lv.addHeaderView(header);
		lv.setAdapter(new ArtistsListAdapter(getApplicationContext(), R.layout.text_list_item, list));

		CheckBox box = (CheckBox) findViewById(R.id.favorite);
		EventBookmark e = Entity.query(EventBookmark.class).where("name").eq(event.getTitle()).execute();
		if (e == null)
			box.setChecked(false);
		else
			box.setChecked(true);
	}

	private class ArtistsListAdapter extends ArrayAdapter<String> {

		class ViewHolder{
			public TextView text;
		}

		public ArtistsListAdapter(Context context, int rowResource, List<String> list){
			super(context, rowResource, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null)
			{
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.text_list_item, null);
				holder.text = (TextView) convertView.findViewById(R.id.row_title);
				convertView.setTag(holder);
			}

			
			final String item = getItem(position);
			
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onItemClicked(item);
				}
			});
			
			holder = (ViewHolder) convertView.getTag();
			holder.text.setText(item);
			return convertView;
		}
	}
	
	public class EventTask extends AsyncTask<String, Void, Event> {

		protected Event doInBackground(String... args) {
			try {
				Caller.getInstance().setCache(null);
				Caller.getInstance().setUserAgent("tst");
				Event event = Event.getInfo(args[0], MainActivity.API_KEY);
				return event;
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setProgressBarIndeterminateVisibility(true);
		}

		protected void onPostExecute(Event event) {
			ActiveData.event = event;
			fill_content(event);
			setProgressBarIndeterminateVisibility(false);
		}
	}

}
