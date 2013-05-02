package com.example.mobilelastfm;

import java.util.List;

import ormdroid.Entity;
import webimageview.WebImageView;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import database_entities.EventBookmark;

public class BookmarkEventActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookmark_event);

		try
		{
			List<EventBookmark> events = Entity.query(EventBookmark.class).executeMulti();
			setListAdapter(new EventListAdapter(getApplicationContext(), R.layout.artist_row, events));
		}
		catch (Exception e)
		{
			TextView txt = (TextView) findViewById(R.id.bookmarks_empty);
			txt.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bookmark_event, menu);
		return true;
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
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout, null);
				holder.text = (TextView) convertView.findViewById(R.id.row_title);
				holder.image = (WebImageView) convertView.findViewById(R.id.row_image);
				convertView.setTag(holder);
			}


			final EventBookmark item = getItem(position);
			holder = (ViewHolder) convertView.getTag();
			//			convertView.setOnClickListener(new OnClickListener() {
			//				@Override
			//				public void onClick(View v) {
			//					onItemClicked(item);
			//				}
			//			});
			holder.text.setText(item.name);
			holder.image.setImageWithURL(getContext(), item.poster);
			return convertView;
		}
	}

}
