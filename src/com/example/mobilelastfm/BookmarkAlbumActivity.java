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
import database_entities.AlbumBookmark;

public class BookmarkAlbumActivity extends ListActivity {

	//	MapView maps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookmark_artist);

		List<AlbumBookmark> albuns = Entity.query(AlbumBookmark.class).executeMulti();
		if (albuns.isEmpty())
		{
			TextView txt = (TextView) findViewById(R.id.bookmarks_empty);
			txt.setVisibility(View.VISIBLE);
		}
		else
		{
			setListAdapter(new AlbumListAdapter(getApplicationContext(), R.layout.bookmark_row, albuns));
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bookmark_album, menu);
		return true;
	}

	private void onItemClicked(AlbumBookmark item) {

		if (MainActivity.wifi.isWifiEnabled()) {
			Intent intent = new Intent(getApplicationContext(),
					AlbumActivity.class);
			intent.putExtra(MainActivity.ACTIVE_DATA, false);
			intent.putExtra(MainActivity.ARTIST, item.artist);
			intent.putExtra(MainActivity.ALBUM, item.title);
			startActivity(intent);
		} else
			Toast.makeText(getApplicationContext(), "Please turn on your WiFi",
					Toast.LENGTH_LONG).show();
	}

	private class AlbumListAdapter extends ArrayAdapter<AlbumBookmark> {

		class ViewHolder{
			public WebImageView image;
			public TextView text;
		}

		public AlbumListAdapter(Context context, int rowResource, List<AlbumBookmark> list){
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


			final AlbumBookmark item = getItem(position);
			holder = (ViewHolder) convertView.getTag();
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onItemClicked(item);
				}
			});
			holder.text.setText(item.title);
			holder.image.setImageWithURL(getContext(), item.cover);
			return convertView;
		}
	}
}
