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
import database_entities.ArtistBookmark;

public class BookmarkArtistActivity extends ListActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookmark_artist);

		List<ArtistBookmark> artists = Entity.query(ArtistBookmark.class).executeMulti();
		if (artists.isEmpty())
		{
			TextView txt = (TextView) findViewById(R.id.bookmarks_empty);
			txt.setVisibility(View.VISIBLE);
		}
		else
		{
			setListAdapter(new ArtistListAdapter(getApplicationContext(), R.layout.bookmark_row, artists));
		}
		
//		Iterator<ArtistBookmark> it = artists.iterator();
//		while (it.hasNext())
//		{
//			boolean s = MainActivity.bloomFilter.contains(it.next().name);
//			Toast.makeText(getApplicationContext(), s + "", Toast.LENGTH_SHORT).show();
//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bookmark_artist, menu);
		return true;
	}

	private void onItemClicked(ArtistBookmark item) {

		if (MainActivity.wifi.isWifiEnabled()) {
			Intent intent = new Intent(getApplicationContext(),
					ArtistTabActivity.class);
			intent.putExtra(MainActivity.ACTIVE_DATA, false);
			intent.putExtra(MainActivity.ARTIST, item.name);
			startActivity(intent);
		} else
			Toast.makeText(getApplicationContext(), R.string.wifi_off,
					Toast.LENGTH_LONG).show();
	}

	private class ArtistListAdapter extends ArrayAdapter<ArtistBookmark> {

		class ViewHolder{
			public WebImageView image;
			public TextView text;
		}

		public ArtistListAdapter(Context context, int rowResource, List<ArtistBookmark> list){
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


			final ArtistBookmark item = getItem(position);
			holder = (ViewHolder) convertView.getTag();
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onItemClicked(item);
				}
			});
			holder.text.setText(item.name);
			holder.image.setImageWithURL(getContext(), item.photo);
			return convertView;
		}
	}

}
