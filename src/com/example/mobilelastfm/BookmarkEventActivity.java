package com.example.mobilelastfm;

import java.util.List;

import ormdroid.Entity;
import webimageview.WebImageView;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import database_entities.EventBookmark;

public class BookmarkEventActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookmark_event);

		List<EventBookmark> events = Entity.query(EventBookmark.class).executeMulti();
		if (events.isEmpty())
		{
			TextView txt = (TextView) findViewById(R.id.bookmarks_empty);
			txt.setVisibility(View.VISIBLE);
		}
		else
		{
			setListAdapter(new EventListAdapter(getApplicationContext(), R.layout.bookmark_row, events));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bookmark_event, menu);
		return true;
	}

	private void onItemClicked(EventBookmark item) {

		if (MainActivity.wifi.isWifiEnabled()) {
			Intent intent = new Intent(getApplicationContext(),
					EventTabActivity.class);
			intent.putExtra(MainActivity.ACTIVE_DATA, false);
			intent.putExtra(MainActivity.EVENT, item.name);
			intent.putExtra(MainActivity.EVENT_ID, item.lid);
			startActivity(intent);
		} else
			Toast.makeText(getApplicationContext(), "Please turn on your WiFi",
					Toast.LENGTH_LONG).show();
	}
	private class EventListAdapter extends ArrayAdapter<EventBookmark> {

		class ViewHolder{
			public WebImageView image;
			public TextView text;
		}

		public EventListAdapter(Context context, int rowResource, List<EventBookmark> list){
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


			final EventBookmark item = getItem(position);
			holder = (ViewHolder) convertView.getTag();
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onItemClicked(item);
				}
			});
			holder.text.setText(item.name);
			holder.image.setImageWithURL(getContext(), item.poster);
			return convertView;
		}
	}

}
