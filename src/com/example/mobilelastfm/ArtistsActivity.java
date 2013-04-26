package com.example.mobilelastfm;

import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import de.umass.lastfm.Artist;
import de.umass.lastfm.Caller;
import de.umass.lastfm.ImageSize;


public class ArtistsActivity extends Activity {

	public static String API_KEY = "029fe710ea7af934b46f8da780722083";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artists);

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
	    
	    protected void onProgressUpdate(Integer... progress) {
	        //TODO meter um progress bar
	    }

	    protected void onPostExecute(Collection<Artist> artists) {
	    	TableLayout table = (TableLayout) findViewById(R.id.artists_list);
	    	Iterator<Artist> artist_it = artists.iterator();
	    	Context context = getApplicationContext();
	    	
			while (artist_it.hasNext()) {
				Artist artist = artist_it.next();
				TableRow row = new TableRow(context);
				TextView text = new TextView(context);
				text.setText(artist.getName());
				text.setTextSize(20);
				text.setTextColor(Color.BLACK);
				
				
				String url = artist.getImageURL(ImageSize.MEDIUM);
				
				WebImageView *myImage = (WebImageView)findViewById(R.id.my_img);
				myImage.setImageWithURL(context, "http://raptureinvenice.com/images/samples/pic-2.png");
				
				ImageView image_view = new ImageView(context);
//				image_view.setImageBitmap(image);
				
				row.addView(text);
				row.addView(image_view);
				table.addView(row);
			}
	    }

	 }
}
