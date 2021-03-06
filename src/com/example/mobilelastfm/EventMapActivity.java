package com.example.mobilelastfm;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import de.umass.lastfm.Venue;

public class EventMapActivity extends FragmentActivity {

	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		if (MainActivity.wifi.isWifiEnabled()) {
			Venue venue = ActiveData.event.getVenue();
			LatLng coordinates = new LatLng(venue.getLatitude(),
					venue.getLongitude());
			map = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			Marker venue_marker = map.addMarker(new MarkerOptions().position(
					coordinates).title(venue.getName()));

			venue_marker
					.setSnippet(venue.getCity() + ", " + venue.getCountry());
			venue_marker.showInfoWindow();
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15));

		} else
			Toast.makeText(getApplicationContext(), R.string.wifi_off,
					Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//		getMenuInflater().inflate(R.menu.activity_map, menu);
		return true;
	}

}
