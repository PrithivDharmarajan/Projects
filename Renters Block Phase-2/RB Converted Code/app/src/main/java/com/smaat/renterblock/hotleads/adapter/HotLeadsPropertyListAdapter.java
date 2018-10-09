package com.smaat.renterblock.hotleads.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smaat.renterblock.R;
import com.smaat.renterblock.hotleads.entity.HotLeadsPropertyEntity;
import com.smaat.renterblock.hotleads.ui.HotLeadsMainScreen;
import com.smaat.renterblock.ui.PropertyDetailsActivity;

public class HotLeadsPropertyListAdapter extends ArrayAdapter<List<String>> {

	Context context;
	int layout;
	Holder holder = null;
	private ArrayList<HotLeadsPropertyEntity> act_ent = new ArrayList<HotLeadsPropertyEntity>();

	public HotLeadsPropertyListAdapter(Context context, int layout,
			ArrayList<HotLeadsPropertyEntity> leads_ent) {
		super(context, layout);
		this.context = context;
		this.layout = layout;
		this.act_ent = leads_ent;
	}

	@Override
	public int getCount() {
		return act_ent.size();
	}

	static class Holder {
		TextView price_range, beds_baths, leads_count, review_Count,
				property_address, view_counts;
		RatingBar rating;
		ImageView profile_pic, show_view;
		RelativeLayout adap_view_layout;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		holder = new Holder();
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(layout, parent, false);
		AQuery aq1 = new AQuery(context).recycle(view);

		holder.price_range = (TextView) view
				.findViewById(R.id.property_cost_hot_leads);
		holder.beds_baths = (TextView) view
				.findViewById(R.id.property_details_hot_leads);
		holder.leads_count = (TextView) view.findViewById(R.id.no_of_leads);
		holder.review_Count = (TextView) view
				.findViewById(R.id.property_reviews_hot_leads);
		holder.view_counts = (TextView) view.findViewById(R.id.no_of_counts);
		holder.property_address = (TextView) view
				.findViewById(R.id.property_location_hot_leads);
		holder.rating = (RatingBar) view
				.findViewById(R.id.review_ratingbar_hot_leads);
		holder.show_view = (ImageView) view
				.findViewById(R.id.show_leads_property_view);
		holder.adap_view_layout = (RelativeLayout) view
				.findViewById(R.id.adap_view_layout);

		holder.adap_view_layout.setTag(position);
		holder.show_view.setTag(position);
		int count = 0;
		for (int i = 0; i < act_ent.get(position).getLeadslist().getPassive()
				.size(); i++) {
			if (act_ent.get(position).getLeadslist().getPassive().get(i)
					.getCount() != null
					&& !act_ent.get(position).getLeadslist().getPassive()
							.get(i).getCount().equals("")) {
				String val_count = act_ent.get(position).getLeadslist()
						.getPassive().get(i).getCount();
				if (!val_count.equalsIgnoreCase("")
						|| !val_count.equalsIgnoreCase("0")) {
					int no_of_count = Integer.parseInt(val_count);
					count = count + no_of_count;
				}
			}
		}
		System.out.println("no_of_counts " + count);
		if (count == 1 || count == 0) {
			holder.view_counts.setText("Views" + " (" + String.valueOf(count)
					+ ")");
		} else {
			holder.view_counts.setText("Views" + " (" + String.valueOf(count)
					+ ")");
		}
		holder.price_range.setText("$" + act_ent.get(position).getPrice_range()
				+ "/");

		holder.beds_baths.setText(" mo " + act_ent.get(position).getBeds()
				+ " bed  " + act_ent.get(position).getBaths() + " bath");
		holder.leads_count.setText("Hot Leads " + " ("
				+ act_ent.get(position).getLeads_count() + ")");
		holder.review_Count.setText(act_ent.get(position).getReview_count()
				+ " Reviews");
		holder.property_address.setText(act_ent.get(position).getAddress());
		holder.rating.setRating(Float.valueOf(act_ent.get(position)
				.getProperty_rating()));

		aq1.id(R.id.image)
				.progress(R.id.loading)
				.image(act_ent.get(position).getPropertyImage(), false, false,
						0, R.drawable.default_prop_icon);

		holder.show_view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int pos = Integer.parseInt(String.valueOf(v.getTag()));
				HotLeadsMainScreen.prop_id = act_ent.get(pos).getProperty_id();
				HotLeadsMainScreen.leadsView(act_ent.get(pos).getLeadslist());
			}
		});

		holder.adap_view_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int pos = Integer.parseInt(String.valueOf(v.getTag()));
				Intent inte = new Intent(context, PropertyDetailsActivity.class);
				inte.putExtra("PropertyId", act_ent.get(pos).getProperty_id());
				inte.putExtra("PropType", "");
				context.startActivity(inte);
			}
		});
		return view;
	}

	public static void deleteCache(Context context) {
		try {
			File dir = context.getCacheDir();
			if (dir != null && dir.isDirectory()) {
				deleteDir(dir);
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		return dir.delete();
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
}
