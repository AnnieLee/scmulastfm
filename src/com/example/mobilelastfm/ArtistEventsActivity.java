package com.example.mobilelastfm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import webimageview.WebImageView;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
		Intent intent = new Intent(getApplicationContext(), EventActivity.class);
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
			holder.text.setText(item.getTitle() + "\n\t\t\t" + getDuration(item));
			holder.image.setImageWithURL(getContext(), item.getImageURL(ImageSize.MEDIUM));
			return convertView;
		}

		private String getDuration(Event event) {
			String duration = "";
			Date start = event.getStartDate();
			Date end = event.getEndDate();

			if (end == null)
				duration = dateToString(start.toString(), null);
			else
				duration = dateToString(start.toString(), end.toString());
			return duration;
		}

		private String dateToString(String start, String end) {
			String to_return = "";
			String start_month = getMonth(start);
			String start_day = getDay(start);

			if (end != null)
			{
				String end_month = getMonth(end);
				String end_day = getDay(end);

				if (start_month.equals(end_month))
					to_return = start_day + "-" + end_day + " " + start_month;
				else
					to_return = start_day + " " + start_month + "-" + end_day + " " + end_month;
			}
			else
				to_return = start_day + start_month;
			return to_return;
		}

		private String getMonth(String date) {
			String result = "";
			result = date.substring(4, 7);
			return result;
		}

		private String getDay(String date) {
			String result = "";
			result = date.substring(8, 10);
			return result;
		}
	}

}
