package com.example.mobilelastfm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import webimageview.WebImageView;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.umass.lastfm.Artist;
import de.umass.lastfm.Caller;
import de.umass.lastfm.Event;
import de.umass.lastfm.ImageSize;

public class ArtistEventsActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_artist_events);

		Artist artist = C.artist;
		new GetEventsTask().execute(artist.getName());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.artist_events, menu);
		return true;
	}

	private void onItemClicked(Event item) {
		Intent intent = new Intent(getApplicationContext(), EventTabActivity.class);
		C.event = item;
		startActivity(intent);
	}

	public class GetEventsTask extends AsyncTask<String, Void, Collection<Event>> {

		protected Collection<Event> doInBackground(String... artist) {
			try {
				Caller.getInstance().setCache(null);
				Caller.getInstance().setUserAgent("tst");
				Collection<Event> events_result = Artist.getEvents(artist[0], MainActivity.API_KEY).getPageResults();
				return events_result;
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setProgressBarIndeterminateVisibility(true);			
		}

		protected void onPostExecute(Collection<Event> events) {
			List<Event> list = null;
			if(events instanceof List){
				list = (List<Event>) events;
			}else{
				list = new ArrayList<Event>(events);
			}


			if (!list.isEmpty())
			{
				setListAdapter(new EventListAdapter(ArtistEventsActivity.this, R.layout.artist_row, list));
			}
			else
			{
				TextView txt = (TextView) findViewById(R.id.events_not_found);
				txt.setVisibility(View.VISIBLE);
			}
			setProgressBarIndeterminateVisibility(false);
		}

	}

	private class EventListAdapter extends ArrayAdapter<Event> {

		class ViewHolder{
			public WebImageView image;
			public TextView text;
		}

		public EventListAdapter(Context context, int rowResource, List<Event> list){
			super(context, rowResource, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;


			if (convertView == null)
			{
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout, null);
				holder.text = (TextView) convertView.findViewById(R.id.row_title);
				holder.image = (WebImageView) convertView.findViewById(R.id.row_image);
				convertView.setTag(holder);
			}


			final Event item = getItem(position);
			holder = (ViewHolder) convertView.getTag();
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onItemClicked(item);
				}
			});
			Spanned html_text = Html.fromHtml(item.getTitle() +
					"<br/><small>" + item.getVenue().getCity() + ", " + item.getVenue().getCountry() 
					+ "<br/>" + EventDate.getDuration(item) + "</small>");
			holder.text.setText(html_text);
			holder.image.setImageWithURL(getContext(), item.getImageURL(ImageSize.MEDIUM));
			return convertView;
		}
	}

}
