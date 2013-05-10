package com.example.mobilelastfm;

import java.util.List;

import ormdroid.Entity;
import android.app.Activity;
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
import database_entities.Friend;

public class FriendsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_friends);
		
		List<Friend> friends = Entity.query(Friend.class).executeMulti();
		
		if (friends.isEmpty())
		{
			TextView txt = (TextView) findViewById(R.id.empty_friends);
			txt.setVisibility(View.VISIBLE);
		}
		else
		{
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

	private class FriendsListAdapter extends ArrayAdapter<Friend> {

		class ViewHolder{
			public TextView text;
		}

		public FriendsListAdapter(Context context, int rowResource, List<Friend> list){
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

			
			final Friend item = getItem(position);		
			holder = (ViewHolder) convertView.getTag();
			holder.text.setText(item.device_name);
			return convertView;
		}
	}
}
