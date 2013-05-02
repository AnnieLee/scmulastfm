package com.example.mobilelastfm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import webimageview.WebImageView;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import de.umass.lastfm.Event;
import de.umass.lastfm.ImageSize;
import de.umass.lastfm.Venue;

public class EventInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_info);
		
		Event event = C.event;

		WebImageView image = (WebImageView) findViewById(R.id.event_image);
		image.setImageWithURL(getApplicationContext(), event.getImageURL(ImageSize.LARGE));

		TextView title = (TextView) findViewById(R.id.title);
		title.setText(event.getTitle());

		TextView txt = (TextView) findViewById(R.id.info);

		Venue venue = event.getVenue();
		txt.setText(venue.getName() + "\n" + venue.getCity() + ", " + venue.getCountry());
		txt.append(Html.fromHtml("<br/>" + EventDate.getDuration(event)));
		Collection<String> artists = event.getArtists();
		
		List<String> list = null;
		if(artists instanceof List){
			list = (List<String>) artists;
		}else{
			list = new ArrayList<String>(artists);
		}
		
		ListView lv = (ListView) findViewById(R.id.artists);
		lv.setAdapter(new ArtistsListAdapter(getApplicationContext(), R.layout.artist_row, list));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_info, menu);
		return true;
	}
	
	private class ArtistsListAdapter extends ArrayAdapter<String> {

		class ViewHolder{
			public TextView text;
		}

		public ArtistsListAdapter(Context context, int rowResource, List<String> list){
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
