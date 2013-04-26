package com.example.mobilelastfm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import webimageview.WebImageView;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.umass.lastfm.Artist;
import de.umass.lastfm.Caller;
import de.umass.lastfm.ImageSize;


public class ArtistsActivity extends ListActivity {

	public static String API_KEY = "029fe710ea7af934b46f8da780722083";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		String result = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		
		new SearchTask().execute(result);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.artists, menu);
		return true;
	}
	
	public class SearchTask extends AsyncTask<String, Void, Collection<Artist>> {
		
	    protected Collection<Artist> doInBackground(String... artist) {
	        try {
	        	Caller.getInstance().setCache(null);
	    		Caller.getInstance().setUserAgent("tst");
	        	Collection<Artist> artist_result = Artist.search(artist[0], ArtistsActivity.API_KEY);
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
	    	if(artists instanceof List){
	    		list = (List<Artist>) artists;
	    	}else{
	    		list = new ArrayList<Artist>(artists);
	    	}
	    	setListAdapter(new ArtistsListAdapter(ArtistsActivity.this, R.layout.artist_row, list));
	    }

	 }
	
	private class ArtistsListAdapter extends ArrayAdapter<Artist> {
		
		class ViewHolder{
			public WebImageView image;
			public TextView text;
		}
		
		public ArtistsListAdapter(Context context, int rowResource, List<Artist> list){
			super(context, rowResource, list);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.artist_row, null);
				holder.text = (TextView) convertView.findViewById(R.id.text);
				holder.image = (WebImageView) convertView.findViewById(R.id.image);
				convertView.setTag(holder);
			}
			
			holder = (ViewHolder) convertView.getTag();
			Artist artist = getItem(position);
			
			holder.text.setText(artist.getName());
			holder.image.setImageWithURL(getContext(), artist.getImageURL(ImageSize.MEDIUM));
			
			return convertView;
		}
	}
}
