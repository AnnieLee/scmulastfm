package com.example.mobilelastfm;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ormdroid.Entity;
import database_entities.ArtistBookmark;
import database_entities.Friend;
import database_entities.SharedBookmark;

import messageserverrequest.MessageServerRequest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FriendsToConnectActivity extends Activity {

	protected static final String EXTRA_DEVICE_ADDRESS = "DEVICE_EXTRA";
	// Member fields
	private BluetoothAdapter mBtAdapter;
	private DevicesListAdapter arrayAdapter;

	public MessageServerRequest server;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		server = new MessageServerRequest();
		// Setup the window
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_friends_to_connect);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		// Set result CANCELED incase the user backs out
		setResult(Activity.RESULT_CANCELED);

		mBtAdapter = BluetoothAdapter.getDefaultAdapter();

		Set<BluetoothDevice> bonded = mBtAdapter.getBondedDevices();

		arrayAdapter = new DevicesListAdapter(this, R.layout.device_name);
		ListView pairedListView = (ListView) findViewById(R.id.friends_list);
		pairedListView.setAdapter(arrayAdapter);

		if (bonded.isEmpty())
		{
			TextView txt = (TextView) findViewById(R.id.empty_friends);
			txt.setVisibility(View.VISIBLE);
		}
		else
		{
			Iterator<BluetoothDevice> it = bonded.iterator();
			List<Friend> f_list = Entity.query(Friend.class).executeMulti();
			boolean empty = f_list.isEmpty();
			while (it.hasNext())
			{
				BluetoothDevice next = it.next();
				arrayAdapter.add(next);
				if (empty)
				{
					Friend f = new Friend();
					f.device_name = next.getName().split("&&")[0];
					f.mac_address = next.getAddress();
					f.save();
					addSharedBookmarks(next, f.id);
				}
			}
		}	

		setProgressBarIndeterminateVisibility(false);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friends_to_connect, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		switch(item.getItemId())
		{
		case android.R.id.home:
			if (MainActivity.wifi.isWifiEnabled())
			{
				intent = new Intent(this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			else
				Toast.makeText(getApplicationContext(), R.string.wifi_off, Toast.LENGTH_LONG).show();
			return true;
		case R.id.action_book:
			intent = new Intent(this, BookmarkTabActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.action_events:
			if (MainActivity.wifi.isWifiEnabled())
			{
				intent = new Intent(this, EventsTabActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			else
				Toast.makeText(getApplicationContext(), R.string.wifi_off, Toast.LENGTH_LONG).show();
			return true;
		case R.id.action_friends:
			if (!mBtAdapter.enable())
				Toast.makeText(getApplicationContext(), "Please turn your bluetooth on", Toast.LENGTH_LONG).show();
			else
			{
				intent = new Intent(this, FriendsTabActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			return true;
		case R.id.action_chat:
			if (!mBtAdapter.enable())
				Toast.makeText(getApplicationContext(), "Please turn your bluetooth on", Toast.LENGTH_LONG).show();
			else
			{
				intent = new Intent(this, FriendsToConnectActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void connect(View view) {
		Intent intent = new Intent(this, BluetoothChatActivity.class);
		CheckBox box = (CheckBox) view.findViewById(R.id.connect);
		String[] splitted = box.getContentDescription().toString().split("--");
		String mac_address = splitted[0];
		String device_name = splitted[1].split("&&")[0];
		
		int position = Integer.parseInt(splitted[2]);
		ListView pairedListView = (ListView) findViewById(R.id.friends_list);
		View item_list = (View) pairedListView.getChildAt(position);
		TextView txt = (TextView) item_list.findViewById(R.id.count);
		String text = txt.getText().toString();
		if (text != "")
			intent.putExtra(MainActivity.PENDENT_MESSAGES, true);
		
		intent.putExtra(MainActivity.DEVICE_ADDRESS, mac_address);
		intent.putExtra(MainActivity.DEVICE_NAME, device_name);
		startActivity(intent);
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

	private class DevicesListAdapter extends ArrayAdapter<BluetoothDevice> {

		class ViewHolder{
			public TextView text;
			public CheckBox box;
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
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_name, null);
				holder.text = (TextView) convertView.findViewById(R.id.device_name);
				holder.box = (CheckBox) convertView.findViewById(R.id.connect);
				convertView.setTag(holder);
			}

			holder = (ViewHolder) convertView.getTag();			
			final BluetoothDevice item = getItem(position);		
			String name = item.getName();
			String device_name = name.split("&&")[0];
			new PendentMessageTask().execute(item.getAddress(), mBtAdapter.getAddress(), position + "");
			holder.text.setText(device_name);
			holder.box.setContentDescription(item.getAddress() + "--" + device_name + "--" + position);
			return convertView;
		}
	}

	public class PendentMessageTask extends AsyncTask<String, Void, Integer> {

		public int position;

		protected Integer doInBackground(String... args) {
			try {
				position = Integer.parseInt(args[2]);
				Integer result = server.getCountMessages(args[0], args[1]);
				return result;
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setProgressBarIndeterminateVisibility(true);
		}

		protected void onPostExecute(Integer count) {
			if (count > 0)
			{
				ListView pairedListView = (ListView) findViewById(R.id.friends_list);
				View item_list = (View) pairedListView.getChildAt(position);
//				 pairedListView.getItemAtPosition(position);
				TextView txt = (TextView) item_list.findViewById(R.id.count);
				txt.setText(Html.fromHtml("<strong>" + count + "</strong>"));
//				CheckBox box = (CheckBox) pairedListView.findViewById(R.id.connect);
//				String content = box.getContentDescription().toString();
//				box.setContentDescription(content + "--true");
			}

			setProgressBarIndeterminateVisibility(false);
		}

	}
}
