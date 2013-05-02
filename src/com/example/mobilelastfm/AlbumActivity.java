package com.example.mobilelastfm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import webimageview.WebImageView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import de.umass.lastfm.Album;
import de.umass.lastfm.Caller;
import de.umass.lastfm.ImageSize;
import de.umass.lastfm.Track;

public class AlbumActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_album);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		Album album = C.album;
		getActionBar().setTitle(album.getName());
		
		WebImageView image = (WebImageView) findViewById(R.id.cover);
		image.setImageWithURL(getApplicationContext(), album.getImageURL(ImageSize.LARGE));

		TextView title = (TextView) findViewById(R.id.title);
		title.setText(album.getName());

		new TracksTask().execute(C.artist.getName(), album.getName());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.album, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
		case android.R.id.home:
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;	
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public class TracksTask extends AsyncTask<String, Void, Collection<Track>> {

		protected Collection<Track> doInBackground(String... args) {
			try {
				Caller.getInstance().setCache(null);
				Caller.getInstance().setUserAgent("tst");
				Album album = Album.getInfo(args[0], args[1], MainActivity.API_KEY);
				Collection<Track> tracks = album.getTracks();
//				Artist artist_result = Artist.getInfo(artist[0], MainActivity.API_KEY);
				return tracks;
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setProgressBarIndeterminateVisibility(true);
		}

		protected void onPostExecute(Collection<Track> tracks) {
			List<String> list = new ArrayList<String>();
			Iterator<Track> it = tracks.iterator();
			while (it.hasNext())
				list.add(it.next().getName());
			
			ListView lv = (ListView) findViewById(R.id.tracks);
			lv.setAdapter(new TracksListAdapter(getApplicationContext(), R.layout.artist_row, list));
			setProgressBarIndeterminateVisibility(false);
		}
	}

	private class TracksListAdapter extends ArrayAdapter<String> {

		class ViewHolder{
			public TextView text;
		}

		public TracksListAdapter(Context context, int rowResource, List<String> list){
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
				convertView.setTag(holder);
			}


			final String item = getItem(position);
			holder = (ViewHolder) convertView.getTag();
//			convertView.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					onItemClicked(item);
//				}
//			});
			holder.text.setText(item);
			return convertView;
		}
	}
}
