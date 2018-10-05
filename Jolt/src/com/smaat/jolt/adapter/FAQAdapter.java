package com.smaat.jolt.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smaat.jolt.R;
import com.smaat.jolt.entity.FAQEntity;
import com.smaat.jolt.ui.HomeScreen;

public class FAQAdapter extends BaseAdapter {
	private final Activity context;
	private ArrayList<FAQEntity> faq_list;

	public FAQAdapter(Activity context, ArrayList<FAQEntity> faq) {
		this.context = context;
		this.faq_list = faq;
	}

	public static class ViewHolder {
		TextView mQuesTxt, mAnsTxt;

	}

	public View getView(int position, View view, ViewGroup parent) {

		if (view == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			view = inflater.inflate(R.layout.faq_adapter, parent, false);
			ViewHolder mholder = new ViewHolder();
			mholder.mQuesTxt = (TextView) view.findViewById(R.id.ques_txt);
			mholder.mAnsTxt = (TextView) view.findViewById(R.id.ans_txt);

			mholder.mQuesTxt.setTypeface(HomeScreen.helveticaNeueBold);
			mholder.mAnsTxt.setTypeface(HomeScreen.helveticaNeueMedium);
			mholder.mQuesTxt.setText(faq_list.get(position).getQuestion());
			mholder.mAnsTxt.setText(faq_list.get(position).getAnswer());
		}
		return view;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return faq_list.size();
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
	};
}