package com.example.mobilelastfm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ormdroid.Entity;
import webimageview.WebImageView;
import android.app.Activity;
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
import database_entities.AlbumBookmark;
import de.umass.lastfm.Album;
import de.umass.lastfm.Caller;
import de.umass.lastfm.ImageSize;
import de.umass.lastfm.Tag;
import de.umass.lastfm.Track;

public class AlbumActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_album);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		Intent intent = getIntent();
		boolean active_data = intent.getBooleanExtra(MainActivity.ACTIVE_DATA, true);
		if (active_data)
		{

			Album album = ActiveData.album;
			getActionBar().setTitle(album.getName());

			WebImageView image = (WebImageView) findViewById(R.id.cover);
			image.setImageWithURL(getApplicationContext(), album.getImageURL(ImageSize.LARGE));

			TextView title = (TextView) findViewById(R.id.title);
			title.setText(album.getName());

			CheckBox box = (CheckBox) findViewById(R.id.favorite);
			AlbumBookmark a = Entity.query(AlbumBookmark.class).where("mbid").eq(album.getMbid()).execute();
			if (a == null)
				box.setChecked(false);
			else
				box.setChecked(true);

			new TagsTask().execute(ActiveData.artist.getName(), album.getName());
			new TracksTask().execute(ActiveData.artist.getName(), album.getName());
		}
		else
		{
			String album = intent.getStringExtra(MainActivity.ALBUM);
			String artist = intent.getStringExtra(MainActivity.ARTIST);
			new AlbumTask().execute(artist, album);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.album, menu);
		return true;
	}

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
		case R.id.action_chat:
			intent = new Intent(this, FriendsToConnectActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void bookmark(View view) {
		CheckBox box = (CheckBox) findViewById(R.id.favorite);
		boolean checked = box.isChecked();
		Album album = ActiveData.album;
		AlbumBookmark a = Entity.query(AlbumBookmark.class).where("mbid").eq(album.getMbid()).execute();
		if (checked && a == null)
		{
			a = new AlbumBookmark();
			a.mbid = album.getMbid();
			a.title = album.getName();
			a.cover = album.getImageURL(ImageSize.MEDIUM);
			a.artist = album.getArtist();
			a.save();
			Toast.makeText(getApplicationContext(), "Album bookmarked with success!", Toast.LENGTH_LONG).show();
		}
		else if (checked && a != null)
		{
			Toast.makeText(getApplicationContext(), "Album already bookmarked", Toast.LENGTH_LONG).show();
		}
		else if (!checked && a == null)
		{
			Toast.makeText(getApplicationContext(), "Album needs to be bookmarked to be removed", Toast.LENGTH_LONG).show();
		}
		else
		{
			a.delete();
			Toast.makeText(getApplicationContext(), "Album removed with success!", Toast.LENGTH_LONG).show();
		}
	}

	public class AlbumTask extends AsyncTask<String, Void, Album> {

		protected Album doInBackground(String... args) {
			try {
				Caller.getInstance().setCache(null);
				Caller.getInstance().setUserAgent("tst");
				Album album = Album.getInfo(args[0], args[1], MainActivity.API_KEY);

				//				Artist artist_result = Artist.getInfo(artist[0], MainActivity.API_KEY);
				return album;
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setProgressBarIndeterminateVisibility(true);
		}

		protected void onPostExecute(Album album) {
			ActiveData.album = album;
			getActionBar().setTitle(album.getName());

			WebImageView image = (WebImageView) findViewById(R.id.cover);
			image.setImageWithURL(getApplicationContext(), album.getImageURL(ImageSize.LARGE));

			TextView title = (TextView) findViewById(R.id.title);
			title.setText(album.getName());

			CheckBox box = (CheckBox) findViewById(R.id.favorite);
			AlbumBookmark a = Entity.query(AlbumBookmark.class).where("mbid").eq(album.getMbid()).execute();
			if (a == null)
				box.setChecked(false);
			else
				box.setChecked(true);

			new TagsTask().execute(album.getArtist(), album.getName());
			new TracksTask().execute(album.getArtist(), album.getName());

			setProgressBarIndeterminateVisibility(false);
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
			List<Track> list = new ArrayList<Track>();
			Iterator<Track> it = tracks.iterator();
			while (it.hasNext())
				list.add(it.next());
			
			View header = LayoutInflater.from(getApplicationContext()).inflate(R.layout.text_list_header, null);
			TextView txt = (TextView) header.findViewById(R.id.header);
			txt.setText("Tracks");
			ListView lv = (ListView) findViewById(R.id.tracks);
			lv.addHeaderView(header);
			lv.setAdapter(new TracksListAdapter(getApplicationContext(), R.layout.text_list_item, list));
			setProgressBarIndeterminateVisibility(false);
		}
	}

	private class TracksListAdapter extends ArrayAdapter<Track> {

		class ViewHolder{
			public TextView text;
			public TextView duration;
		}

		public TracksListAdapter(Context context, int rowResource, List<Track> list){
			super(context, rowResource, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;


			if (convertView == null)
			{
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.text_list_item, null);
				holder.text = (TextView) convertView.findViewById(R.id.row_title);
				holder.duration = (TextView) convertView.findViewById(R.id.duration);
				convertView.setTag(holder);
			}


			final Track item = getItem(position);
			holder = (ViewHolder) convertView.getTag();
			//What Sound - 3:42
			holder.text.setText(item.getName());
			holder.duration.setText(Html.fromHtml("<small>" + EventDate.getTrackDuration(item.getDuration()) + "</small>"));
//					Html.fromHtml(
//					item.getName() + "<br/><small style='float: right;'>"
//						+ EventDate.getTrackDuration(item.getDuration()) + "</small>" ));
			return convertView;
		}
	}

	public class TagsTask extends AsyncTask<String, Void, Collection<Tag>> {

		protected Collection<Tag> doInBackground(String... args) {
			try {
				Caller.getInstance().setCache(null);
				Caller.getInstance().setUserAgent("tst");
				Collection<Tag> tags = Album.getTopTags(args[0], args[1], MainActivity.API_KEY);
				//				Collection<Tag> tags = Artist.getTopTags(artist[0], MainActivity.API_KEY);
				return tags;
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setProgressBarIndeterminateVisibility(true);
		}

		protected void onPostExecute(Collection<Tag> tags) {
			List<Tag> list = new ArrayList<Tag>();
			Iterator<Tag> it = tags.iterator();
			int counter = 0;
			while (it.hasNext() && counter < 5)
			{
				Tag next = it.next();
				if (next != null)
				{
					list.add(it.next());
					counter++;
				}
			}
			ListView lv = (ListView) findViewById(R.id.tags_list);
			TagListAdapter adapter = new TagListAdapter(getApplicationContext(), R.layout.tag_item, list);
			lv.setDivider(null);
			lv.setAdapter(adapter);

			setProgressBarIndeterminateVisibility(false);
		}
	}

	private class TagListAdapter extends ArrayAdapter<Tag> {

		class ViewHolder{
			public TextView text;
		}

		public TagListAdapter(Context context, int rowResource, List<Tag> list){
			super(context, rowResource, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null)
			{
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.tag_item, null);
				holder.text = (TextView) convertView.findViewById(R.id.artist_tag_item);
				convertView.setTag(holder);
			}

			final Tag item = getItem(position);
			holder = (ViewHolder) convertView.getTag();
			holder.text.setText(item.getName());
			return convertView;
		}
	}
}
