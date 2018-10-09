package com.smaat.renterblock.findagent;

import java.util.ArrayList;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.smaat.renterblock.R;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.util.TypefaceSingleton;

public class AgentMoreReviewsActivity extends BaseActivity implements
		OnClickListener {

	private ListView mReviewList;
	private AgentMoreReviewsAdapter mReviewsAdapter;
	private Bundle mBundle;
	private ArrayList<AgentReviewsEntity> mReviewsEntityList;
	private TextView how_edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agent_more_reviews);
		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypefaceBold = TypefaceSingleton.getInstance()
				.getHelveticaBold(this);
		setFont(root, mTypefaceBold);
		setupUI(root);

		initComponents();

	}

	@SuppressWarnings("unchecked")
	private void initComponents() {

		mReviewList = (ListView) findViewById(R.id.my_reviews_list);
		how_edit = (TextView) findViewById(R.id.how_edit);
		how_edit.setTypeface(helvetica_bold);

		mBundle = getIntent().getExtras();

		if (mBundle != null) {

			mReviewsEntityList = (ArrayList<AgentReviewsEntity>) mBundle
					.getSerializable("ReviewsEntityList");
			if (mReviewsEntityList.size() != 0) {
				setReviewAdapter();
			}
		}
	}

	private void setReviewAdapter() {

		mReviewsAdapter = new AgentMoreReviewsAdapter(this, mReviewsEntityList);
		mReviewList.setAdapter(mReviewsAdapter);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_icon:
			finish();
			overridePendingTransition(R.anim.slide_out_right,
					R.anim.slide_in_left);
			break;

		}

	}
}
