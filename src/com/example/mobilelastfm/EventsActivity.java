package com.example.mobilelastfm;

import java.util.Collection;
import java.util.Iterator;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import de.umass.lastfm.Caller;
import de.umass.lastfm.Event;
import de.umass.lastfm.Geo;
import de.umass.lastfm.PaginatedResult;
import de.umass.lastfm.Venue;

public class EventsActivity extends FragmentActivity {

	public double latitude;
	public double longitude;
	GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_events);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		@SuppressWarnings("static-access")
		LocationManager locationManager = (LocationManager) this.getSystemService(getApplicationContext().LOCATION_SERVICE);

		Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) { }
			public void onStatusChanged(String provider, int status, Bundle extras) {}
			public void onProviderEnabled(String provider) {}
			public void onProviderDisabled(String provider) {}
		};

		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		new EventsTask().execute("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.events, menu);
		return true;
	}
	
	@Override
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
			intent = new Intent(this, EventsActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.action_friends:
			intent = new Intent(this, FriendsActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
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
			Iterator<Event> it = events.iterator();
			map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			LatLng location = new LatLng(latitude, longitude);
			map.addMarker(new MarkerOptions().position(location).title("Me"));
			while (it.hasNext())
			{
				Event next = it.next();
				Venue venue = next.getVenue();
				LatLng coordinates = new LatLng(venue.getLatitude(), venue.getLongitude());
				Marker venue_marker = map.addMarker(new MarkerOptions().position(coordinates).title(next.getTitle()));

				venue_marker.setSnippet(venue.getName());
				venue_marker.showInfoWindow();
			}
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 11));
			setProgressBarIndeterminateVisibility(false);
		}

	}

}
