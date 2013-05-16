package com.example.mobilelastfm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ormdroid.Entity;

import database_entities.ArtistBookmark;
import database_entities.Friend;
import database_entities.SharedBookmark;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FriendsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_friends);
		
		BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();

		Set<BluetoothDevice> bonded = mBtAdapter.getBondedDevices();
		
		if (bonded.isEmpty())
		{
			TextView txt = (TextView) findViewById(R.id.empty_friends);
			txt.setVisibility(View.VISIBLE);
		}
		else
		{
			List<BluetoothDevice> friends = new ArrayList<BluetoothDevice>();
			Iterator<BluetoothDevice> it = bonded.iterator();
			List<Friend> f_list = Entity.query(Friend.class).executeMulti();
			boolean empty = f_list.isEmpty();
			while (it.hasNext())
			{
				BluetoothDevice next = it.next();
				friends.add(next);
				if (empty)
				{
					Friend f = new Friend();
					String[] name = next.getName().split("&&");
					f.device_name = name[0];
					f.mac_address = next.getAddress();
					if (name.length != 1)
						f.bookmarks = name[1];
					f.save();
					addSharedBookmarks(next, f.id);
				}
			}
			ListView lv = (ListView) findViewById(R.id.friends_list);
			FriendsListAdapter adapter = new FriendsListAdapter(getApplicationContext(), R.layout.friend_item, friends);
			lv.setAdapter(adapter);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friends, menu);
		return true;
	}

	private class FriendsListAdapter extends ArrayAdapter<BluetoothDevice> {

		class ViewHolder{
			public TextView text;
		}

		public FriendsListAdapter(Context context, int rowResource, List<BluetoothDevice> list){
			super(context, rowResource, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null)
			{
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.friend_item, null);
				holder.text = (TextView) convertView.findViewById(R.id.name);
				convertView.setTag(holder);
			}

			
			final BluetoothDevice item = getItem(position);		
			holder = (ViewHolder) convertView.getTag();
			String device_name = item.getName().split("&&")[0];
			holder.text.setText(device_name);
			return convertView;
		}
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
}
