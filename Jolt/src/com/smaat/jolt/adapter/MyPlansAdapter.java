package com.smaat.jolt.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smaat.jolt.R;
import com.smaat.jolt.model.PlanEntity;
import com.smaat.jolt.ui.HomeScreen;

public class MyPlansAdapter extends BaseAdapter {
	private Activity context;
	private String plan,drinkscount,expdate;

	private ArrayList<PlanEntity> mPlansList;

	public MyPlansAdapter(Activity context,
			ArrayList<PlanEntity> mPlans) {
		this.context = context;
		this.mPlansList = mPlans;
	}

	public static class holder {
		TextView plan_txt, planDetails, expiration_txt, expirationDate,mDrinksCount,mDrinksCountTxt,include;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {

			LayoutInflater inflater = context.getLayoutInflater();
			convertView = inflater.inflate(R.layout.profile_plans_adapter, null,
					true);

			holder mholder = new holder();

			mholder.plan_txt = (TextView) convertView.findViewById(R.id.plan_txt);
			mholder.planDetails = (TextView) convertView.findViewById(R.id.planDetails);
			mholder.expiration_txt = (TextView) convertView.findViewById(R.id.expiration_txt);
			mholder.expirationDate = (TextView) convertView.findViewById(R.id.expirationDate);
			mholder.mDrinksCountTxt = (TextView) convertView
					.findViewById(R.id.drinks_count_txt);
			mholder.mDrinksCount = (TextView) convertView.findViewById(R.id.drinks_count);
			mholder.include = (TextView) convertView.findViewById(R.id.include);
			
			

			mholder.plan_txt.setTypeface(HomeScreen.helveticaNeueBold);
			mholder.planDetails.setTypeface(HomeScreen.helveticaNeueMedium);
			mholder.expiration_txt.setTypeface(HomeScreen.helveticaNeueBold);
			mholder.expirationDate.setTypeface(HomeScreen.helveticaNeueMedium);
			mholder.mDrinksCountTxt.setTypeface(HomeScreen.helveticaNeueBold);
			mholder.mDrinksCount.setTypeface(HomeScreen.helveticaNeueMedium);
			mholder.include.setTypeface(HomeScreen.helveticaNeueMedium);

			 plan = mPlansList.get(position).getCurrentPlan();
			drinkscount = mPlansList.get(position).getCupscount();
			expdate = mPlansList.get(position).getExpiry_date();

			if (plan.trim().isEmpty()) {
				plan = "No Plan";
			}
			if (drinkscount.trim().isEmpty()) {
				drinkscount = "0";
			}
			if (expdate.trim().isEmpty()) {
				expdate = "No Expdate";
			}
			mholder.planDetails.setText(plan);
			mholder.mDrinksCount.setText(drinkscount);
			mholder.expirationDate.setText(expdate);
			mholder.include.setText(mPlansList.get(position).getIncludesInfo());
			convertView.setTag(mholder);
		}
		@SuppressWarnings("unused")
		holder mholder = (holder) convertView.getTag();
		return convertView;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mPlansList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

}
