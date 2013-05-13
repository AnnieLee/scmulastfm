package com.example.mobilelastfm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ormdroid.Entity;
import webimageview.WebImageView;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import bloomfilter.BloomFilter;
import database_entities.ArtistBookmark;
import de.umass.lastfm.Artist;
import de.umass.lastfm.Caller;
import de.umass.lastfm.ImageSize;


public class ArtistsActivity extends ListActivity {

	public static String EXTRA_MESSAGE = "Artist name";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_artists);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		String result = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

		if (MainActivity.wifi.isWifiEnabled())
			new SearchTask().execute(result);
		else
			Toast.makeText(getApplicationContext(), "Please turn your WiFi", Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.artists, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch(item.getItemId())
		{
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
			intent = new Intent(this, FriendsTabActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	public void onItemClicked(Artist artist) {
		Intent intent = new Intent(getApplicationContext(), ArtistTabActivity.class);
		intent.putExtra(MainActivity.ACTIVE_DATA, true);
		ActiveData.artist = artist;
		startActivity(intent);
	}

	public class SearchTask extends AsyncTask<String, Void, Collection<Artist>> {

		protected Collection<Artist> doInBackground(String... artist) {
			try {
				Caller.getInstance().setCache(null);
				Caller.getInstance().setUserAgent("tst");
				Collection<Artist> artist_result = Artist.search(artist[0], MainActivity.API_KEY);
				return artist_result;
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setProgressBarIndeterminateVisibility(true);
		}

		protected void onPostExecute(Collection<Artist> artists) {
			List<Artist> list = null;
			if (artists.isEmpty())
			{
				TextView txt = (TextView) findViewById(R.id.artists_not_found);
				txt.setVisibility(View.VISIBLE);
			}
			else
			{
				if(artists instanceof List){
					list = (List<Artist>) artists;
				}else{
					list = new ArrayList<Artist>(artists);
				}
				final ArtistsListAdapter adapter = new ArtistsListAdapter(ArtistsActivity.this, R.layout.row_layout, list);
				setListAdapter(adapter);
			}
			setProgressBarIndeterminateVisibility(false);
		}

	}

	private class ArtistsListAdapter extends ArrayAdapter<Artist> {

		class ViewHolder{
			public WebImageView image;
			public TextView text;
			public CheckBox box;
		}

		public ArtistsListAdapter(Context context, int rowResource, List<Artist> list){
			super(context, rowResource, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout, null);
				holder.text = (TextView) convertView.findViewById(R.id.text);
				holder.image = (WebImageView) convertView.findViewById(R.id.image);
				holder.box = (CheckBox) convertView.findViewById(R.id.favorite);
				convertView.setTag(holder);
			}

			holder = (ViewHolder) convertView.getTag();
			final Artist artist = getItem(position);

			holder.text.setText(artist.getName());
			holder.image.setImageWithURL(getContext(), artist.getImageURL(ImageSize.MEDIUM));
			final ArtistBookmark a = Entity.query(ArtistBookmark.class).where("mbid").eq(artist.getMbid()).execute();
			if (a == null)
				holder.box.setChecked(false);
			else
				holder.box.setChecked(true);

			holder.box.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					bookmark(v, artist, a);
				}
			});

			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onItemClicked(artist);
				}
			});

			return convertView;
		}
	}

	public void bookmark(View view, Artist artist, ArtistBookmark ab) {
		Artist a = artist;
		CheckBox box = (CheckBox) view.findViewById(R.id.favorite);
		boolean checked = box.isChecked();
		if (checked && ab == null)
		{
			updateBloomFilter(true, a);
			
			ab = new ArtistBookmark();
			ab.mbid = a.getMbid();
			ab.name = a.getName();
			ab.photo = a.getImageURL(ImageSize.MEDIUM);
			ab.save();
			
			Toast.makeText(getApplicationContext(), "Artist bookmarked with success!", Toast.LENGTH_LONG).show();
		}
		else if (checked && a != null)
		{
			Toast.makeText(getApplicationContext(), "Artist already bookmarked", Toast.LENGTH_LONG).show();
		}
		else if (!checked && a == null)
		{
			Toast.makeText(getApplicationContext(), "Artist needs to be bookmarked to be removed", Toast.LENGTH_LONG).show();
		}
		else
		{
			updateBloomFilter(false, a);
			
			ab.delete();
			ab.save();

			Toast.makeText(getApplicationContext(), "Artist removed with success", Toast.LENGTH_LONG).show();
		}
	}

	private void updateBloomFilter(boolean add, Artist a) {
		BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		BloomFilter<String> bloom = new BloomFilter<String>(0.1, 1000);

		List<ArtistBookmark> a_list = Entity.query(ArtistBookmark.class).executeMulti();
		Iterator<ArtistBookmark> it = a_list.iterator();
		while (it.hasNext()) {
			ArtistBookmark next = it.next();
			if (!add && next.name != a.getName())
				bloom.add(next.name);
		}
		if (add)
			bloom.add(a.getName());

		String friendly_name = mBtAdapter.getName();
		String bit_set_str = bloom.getBitSet().toString();

		String new_friendly_name = friendly_name + "&&" + bit_set_str;
		mBtAdapter.setName(new_friendly_name);
	}

}
