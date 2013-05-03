package com.example.mobilelastfm;

import android.app.Activity;
import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class MapActivity extends Activity {
//	static final LatLng HAMBURG = new LatLng(53.558, 9.927);
//	static final LatLng KIEL = new LatLng(53.551, 9.993);
//	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
//		SupportMapFragment fragment = new SupportMapFragment();
//        getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.activity_map, menu);
		return true;
	}

}
