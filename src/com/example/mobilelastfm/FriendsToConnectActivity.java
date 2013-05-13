package com.example.mobilelastfm;

import java.util.Iterator;
import java.util.List;

import ormdroid.Entity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import database_entities.Friend;

public class FriendsToConnectActivity extends Activity {

	protected static final String EXTRA_DEVICE_ADDRESS = "DEVICE_EXTRA";
	// Member fields
	private BluetoothAdapter mBtAdapter;
	private ArrayAdapter<String> arrayAdapter;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Setup the window
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_friends_to_connect);

		// Set result CANCELED incase the user backs out
		setResult(Activity.RESULT_CANCELED);

		arrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
		
		// Find and set up the ListView for paired devices
		ListView pairedListView = (ListView) findViewById(R.id.friends);
		pairedListView.setAdapter(arrayAdapter);
		pairedListView.setOnItemClickListener(mDeviceClickListener);

		List<Friend> f = Entity.query(Friend.class).executeMulti();
		Iterator<Friend> it = f.iterator();
		while (it.hasNext())
		{
			Friend next = it.next();
			arrayAdapter.add(next.device_name + "\n" + next.mac_address);
		}

		// Get the local Bluetooth adapter
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	// The on-click listener for all devices in the ListViews
	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
			// Cancel discovery because it's costly and we're about to connect
			mBtAdapter.cancelDiscovery();

			// Get the device MAC address, which is the last 17 chars in the View
			String info = ((TextView) v).getText().toString();
			String address = info.substring(info.length() - 17);

			// Create the result Intent and include the MAC address
			Intent intent = new Intent();
			intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

			// Set result and finish this Activity
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	};
}
