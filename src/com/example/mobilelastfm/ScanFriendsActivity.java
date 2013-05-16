package com.example.mobilelastfm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ormdroid.Entity;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import database_entities.ArtistBookmark;
import database_entities.Friend;
import database_entities.SharedBookmark;

public class ScanFriendsActivity extends Activity {

	public static String EXTRA_DEVICE_ADDRESS = "device_address";

	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	public static final int MESSAGE_TIME = 6;
	public static final int MESSAGE_READ_TIME = 7;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	public static final int STATE_NONE = 0;       // we're doing nothing
	public static final int STATE_LISTEN = 1;     // now listening for incoming connections
	public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
	public static final int STATE_CONNECTED = 3;  // now connected to a remote device

	private BluetoothAdapter mBtAdapter;
	private DevicesListAdapter mNewDevicesArrayAdapter;
	public List<BluetoothDevice> scan_results;
	public int book_counter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_friends);

		//		getActionBar().setTitle("Scanning...");

		scan_results = new ArrayList<BluetoothDevice>();
		mNewDevicesArrayAdapter = new DevicesListAdapter(this, R.layout.friend_name);
		//				ArrayAdapter<String>(this, R.layout.device_name, R.id.device_name);		

		ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
		newDevicesListView.setAdapter(mNewDevicesArrayAdapter);

		// Register for broadcasts when a device is discovered
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);

		// Register for broadcasts when discovery has finished
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mReceiver, filter);

		// Get the local Bluetooth adapter
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();

		List<ArtistBookmark> a_list = Entity.query(ArtistBookmark.class).executeMulti();
		book_counter = a_list.size();

		mBtAdapter.startDiscovery();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friends, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		if (MainActivity.wifi.isWifiEnabled()) {
			switch (item.getItemId()) {
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
				intent = new Intent(this, EventsTabActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				return true;
			case R.id.action_friends:
				intent = new Intent(this, ScanFriendsActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
		} else {
			Toast.makeText(getApplicationContext(), R.string.wifi_off,
					Toast.LENGTH_LONG).show();
			return false;
		}
	}

	public void add_friend(View v) throws NoSuchMethodException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		mBtAdapter.cancelDiscovery();


		CheckBox box = (CheckBox) v.findViewById(R.id.add_friend);
		String to_split = box.getContentDescription().toString();
		String[] splitted = to_split.split("--");
		String address = splitted[0];
		String name[] = splitted[1].split("&&");

		BluetoothDevice device = mBtAdapter.getRemoteDevice(address);
		@SuppressWarnings("rawtypes")
		Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
		@SuppressWarnings("unchecked")
		Method createBondMethod = class1.getMethod("createBond");  
		Boolean returnValue = (Boolean) createBondMethod.invoke(device);

		if (returnValue)
		{
			Friend f = Entity.query(Friend.class).where("mac_address").eq(address).execute();
			if (f == null)
			{			
				f = new Friend();
				f.device_name = name[0];
				f.mac_address = address;
				if (name.length != 1)
					f.bookmarks = name[1]; 
				f.save();

				BluetoothDevice item = mBtAdapter.getRemoteDevice(address); 
				addSharedBookmarks(item, f.id);

				Toast.makeText(getApplicationContext(), "Friend added with success!", Toast.LENGTH_LONG).show();
			}
			else
				Toast.makeText(getApplicationContext(), "Friend already added", Toast.LENGTH_LONG).show();
		}
		else
			Toast.makeText(getApplicationContext(), "Oops, something went wrong", Toast.LENGTH_LONG).show();

	}

	private void addSharedBookmarks(BluetoothDevice item, int friend_id) {
		String device_name = item.getName();

		String[] splitted_name = device_name.split("&&");
		if (splitted_name.length != 1)
		{

			String[] artists = splitted_name[1].split("-");
			for (int i = 0; i < artists.length; i++) {
				ArtistBookmark a = Entity.query(ArtistBookmark.class).where("a_hashcode").eq(artists[i]).execute();
				if (a != null)
				{
					SharedBookmark sb = new SharedBookmark();
					sb.artist_id = a.mbid;
					sb.friend_id = friend_id;
					sb.save();
				}
			}
		}
	}

	// The BroadcastReceiver that listens for discovered devices and
	// changes the title when discovery is finished
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				setProgressBarIndeterminateVisibility(false);
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				// If it's already paired, skip it, because it's been listed already
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					scan_results.add(device);
					mNewDevicesArrayAdapter.add(device);
					//					}
				}
			}
			else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
			{
				setProgressBarIndeterminateVisibility(false);
				TextView txt = (TextView) findViewById(R.id.label);
				txt.setText(R.string.friends_label);
			}
		}
	};

	private class DevicesListAdapter extends ArrayAdapter<BluetoothDevice> {

		class ViewHolder{
			public TextView text;
			public CheckBox box;
			public TextView counter;
		}

		public DevicesListAdapter(Context context, int rowResource) {
			super(context, rowResource);
		}

		@Override
		public void add(BluetoothDevice object) {
			super.add(object);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null)
			{
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.friend_name, null);
				holder.text = (TextView) convertView.findViewById(R.id.device_name);
				holder.box = (CheckBox) convertView.findViewById(R.id.add_friend);
				holder.counter = (TextView) convertView.findViewById(R.id.counter);
				convertView.setTag(holder);
			}


			final BluetoothDevice item = getItem(position);		
			holder = (ViewHolder) convertView.getTag();
			String device_name = item.getName();
			int counter = 0;
			if (device_name != null)
			{
				String[] splitted_name = device_name.split("&&");
				device_name = splitted_name[0];
				if (splitted_name.length != 1)
				{
					String[] artists = splitted_name[1].split("-");
					for (int i = 0; i < artists.length; i++) {
						ArtistBookmark a = Entity.query(ArtistBookmark.class).where("a_hashcode").eq(artists[i]).execute();
						if (a != null)
							counter++;
					}
				}
			}
			float perc;
			if (book_counter != 0)
				perc = (counter*1f / book_counter) * 100; 
			else
				perc = 0;

			holder.counter.setText(Html.fromHtml("<small>" + perc + "%</small>"));	
			if (device_name == null)
				holder.text.setText("Name not found");
			else
				holder.text.setText(device_name);

			holder.box.setContentDescription(item.getAddress() + "--" + item.getName());

			return convertView;
		}
	}
}
