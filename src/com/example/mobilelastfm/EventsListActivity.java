package com.example.mobilelastfm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import webimageview.WebImageView;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import de.umass.lastfm.Caller;
import de.umass.lastfm.Event;
import de.umass.lastfm.Geo;
import de.umass.lastfm.ImageSize;
import de.umass.lastfm.PaginatedResult;

public class EventsListActivity extends ListActivity {

	public double latitude;
	public double longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_events_list);

		if (MainActivity.wifi.isWifiEnabled()) {
			@SuppressWarnings("static-access")
			LocationManager locationManager = (LocationManager) this
					.getSystemService(getApplicationContext().LOCATION_SERVICE);

			Location location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			latitude = location.getLatitude();
			longitude = location.getLongitude();

			LocationListener locationListener = new LocationListener() {
				public void onLocationChanged(Location location) {}
				public void onStatusChanged(String provider, int status,
						Bundle extras) {}
				public void onProviderEnabled(String provider) {}
				public void onProviderDisabled(String provider) {}
			};

			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
			new EventsTask().execute("");
		} else
			Toast.makeText(getApplicationContext(), R.string.wifi_off,
					Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.events_list, menu);
		return true;
	}
	
	private void onItemClicked(Event item) {
		if (MainActivity.wifi.isWifiEnabled()) {
			Intent intent = new Intent(getApplicationContext(),
					EventTabActivity.class);
			ActiveData.event = item;
			startActivity(intent);
		} else
			Toast.makeText(getApplicationContext(), R.string.wifi_off,
					Toast.LENGTH_LONG).show();
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
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.bookmark_row, null);
				holder.text = (TextView) convertView.findViewById(R.id.row_title);
				holder.image = (WebImageView) convertView.findViewById(R.id.row_image);
				convertView.setTag(holder);
			}


			final Event item = getItem(position);
			holder = (ViewHolder) convertView.getTag();
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					v.setBackgroundColor(Color.RED);
					onItemClicked(item);
				}
			});
			Spanned html_text = Html.fromHtml(item.getTitle() +
					"<br/><small>" + item.getVenue().getName() 
					+ "<br/>" + EventDate.getDuration(item) + "</small>");
			holder.text.setText(html_text);
			holder.image.setImageWithURL(getContext(), item.getImageURL(ImageSize.MEDIUM));
			
			return convertView;
		}
	}
	
	public class EventsTask extends AsyncTask<String, Void, Collection<Event>> {

		protected Collection<Event> doInBackground(String... artist) {
			try {
				Caller.getInstance().setCache(null);
				Caller.getInstance().setUserAgent("tst");
				PaginatedResult<Event> paginated_events = Geo.getEvents(latitude, longitude, 0, MainActivity.API_KEY);
				Collection<Event> events = paginated_events.getPageResults();
				return events;
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
			ActiveData.nearby_events = events;
			List<Event> list = new ArrayList<Event>();
			Iterator<Event> it = events.iterator();
			while (it.hasNext())
				list.add(it.next());
			
			if (list.isEmpty())
			{
				TextView txt = (TextView) findViewById(R.id.events_not_found);
				txt.setVisibility(View.VISIBLE);
			}
			else
				getListView().setAdapter(new EventListAdapter(getApplicationContext(), R.layout.bookmark_row, list));
			
			setProgressBarVisibility(false);
//			setProgressBarIndeterminateVisibility(false);
		}

	}

}