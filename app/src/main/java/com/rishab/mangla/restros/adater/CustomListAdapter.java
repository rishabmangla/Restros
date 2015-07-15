package com.rishab.mangla.restros.adater;

import com.rishab.mangla.restros.R;
import com.rishab.mangla.restros.app.AppController;
import com.rishab.mangla.restros.model.Restro;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by rishabmangla on 15/7/15.
 */
public class CustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Restro> restroItems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomListAdapter(Activity activity, List<Restro> restroItems) {
		this.activity = activity;
		this.restroItems = restroItems;
	}

	@Override
	public int getCount() {
		return restroItems.size();
	}

	@Override
	public Object getItem(int location) {
		return restroItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_row, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		NetworkImageView thumbNail = (NetworkImageView) convertView
				.findViewById(R.id.thumbnail);
		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView distance = (TextView) convertView.findViewById(R.id.distance);
		TextView cuisine = (TextView) convertView.findViewById(R.id.cuisine);
		TextView location = (TextView) convertView.findViewById(R.id.location);

		// getting restro data for the row
		Restro m = restroItems.get(position);

		// thumbnail image
		thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
		
		// name
		name.setText(m.getName());
		
		// distance
		distance.setText("Dist: " + String.valueOf((int) m.getDistance()) + "m");
		
		// cuisine
		String genreStr = "";
		for (String str : m.getCuisine()) {
			genreStr += str + ", ";
		}
		genreStr = genreStr.length() > 0 ? genreStr.substring(0,
				genreStr.length() - 2) : genreStr;
		cuisine.setText(genreStr);

        // location
        location.setText(m.getLocation());

		return convertView;
	}

}