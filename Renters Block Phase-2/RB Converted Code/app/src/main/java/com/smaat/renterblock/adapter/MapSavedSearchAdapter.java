package com.smaat.renterblock.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smaat.renterblock.MapFragmentActivity;
import com.smaat.renterblock.R;
import com.smaat.renterblock.savedsearch.LocalSavedSearchEntity;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.TypefaceSingleton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MapSavedSearchAdapter extends ArrayAdapter<List<String>> {

	Context context;
	int layout;
	Holder holder = null;
	private ArrayList<LocalSavedSearchEntity> act_ent = new ArrayList<LocalSavedSearchEntity>();
	Typeface helvetica_normal, helvetica_bold, helvetica_light;

	public MapSavedSearchAdapter(Context context, int layout,
			ArrayList<LocalSavedSearchEntity> local_save_search) {
		super(context, layout);
		this.context = context;
		this.layout = layout;
		this.act_ent = local_save_search;
		helvetica_normal = TypefaceSingleton.getInstance().getHelvetica(context);
		helvetica_bold = TypefaceSingleton.getInstance().getHelveticaBold(context);
		helvetica_light = TypefaceSingleton.getInstance().getHelveticaLight(
				context);
	}

	@Override
	public int getCount() {
		return act_ent.size();
	}

	static class Holder {

		TextView location, prop_type, location_txt;
		RelativeLayout curr_loc, saved_list_lay;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		// if (view == null) {
		if (act_ent.size() > 0) {
			holder = new Holder();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(layout, parent, false);

			holder.location = (TextView) view.findViewById(R.id.location);
			holder.location.setTypeface(helvetica_bold);
			holder.prop_type = (TextView) view.findViewById(R.id.prop_type);
			holder.prop_type.setTypeface(helvetica_normal);
			holder.location_txt = (TextView) view
					.findViewById(R.id.location_txt);
			holder.location_txt.setTypeface(helvetica_normal);
			holder.curr_loc = (RelativeLayout) view
					.findViewById(R.id.curr_location);
			holder.saved_list_lay = (RelativeLayout) view
					.findViewById(R.id.saved_list_lay);
			holder.saved_list_lay.setTag(position);
			holder.location_txt.setTag(position);

			if (!AppConstants.from_api) {
				holder.location_txt.setVisibility(View.GONE);
				holder.location.setVisibility(View.VISIBLE);
				holder.prop_type.setVisibility(View.VISIBLE);
				if (position == 0) {
					holder.curr_loc.setVisibility(View.VISIBLE);
					holder.saved_list_lay.setVisibility(View.GONE);
				} else {
					holder.curr_loc.setVisibility(View.GONE);
					holder.saved_list_lay.setVisibility(View.VISIBLE);
				}
			} else {
				holder.curr_loc.setVisibility(View.GONE);
				holder.saved_list_lay.setVisibility(View.GONE);
				holder.location.setVisibility(View.GONE);
				holder.prop_type.setVisibility(View.GONE);
				holder.location_txt.setVisibility(View.VISIBLE);
			}
			holder.location_txt.setText(act_ent.get(position).getLocation());
			holder.location.setText(act_ent.get(position).getLocation());
			holder.prop_type.setText(act_ent.get(position).getProperty_type());

			holder.location_txt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int pos = Integer.parseInt(String.valueOf(v.getTag()));
					String location_text = act_ent.get(pos).getLocation();
					MapFragmentActivity.callPlacesApi(location_text);
				}
			});

			holder.saved_list_lay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int pos = Integer.parseInt(String.valueOf(v.getTag()));
					String lat = act_ent.get(pos).getLatitude();
					String longi = act_ent.get(pos).getLongitude();
					MapFragmentActivity.callPlacesApi(act_ent.get(pos)
							.getLocation());
					// if (lat != null && longi != null) {
					// MapFragmentActivity.Propertylatlan(lat, longi);
					// }
				}
			});

			holder.curr_loc.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					MapFragmentActivity.Propertylatlan("", "");
				}
			});
		}
		return view;
	}

	public static void deleteCache(Context context) {
		try {
			File dir = context.getCacheDir();
			if (dir != null && dir.isDirectory()) {
				deleteDir(dir);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
}
