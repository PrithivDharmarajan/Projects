package com.smaat.ipharma.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.smaat.ipharma.R;

public class PharmacyList extends Activity {

	ListView lv;
	Context context;
	TextView map, about;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		map = (TextView) findViewById(R.id.All);
		about = (TextView) findViewById(R.id.Normal);

//		ScrollView sv = (ScrollView) findViewById(R.id.scrollView1);

		LinearLayout Ll = (LinearLayout) findViewById(R.id.foodItemActvity_linearLayout_fragments);

		context = this;
		
		map.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent in = new Intent(PharmacyList.this, MapActivity.class);
				startActivity(in);				
			}
		});
		about.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// Intent in = new Intent(MainActivity.this,
				// AboutActivity.class);
				// startActivity(in);
			}
		});

		for (int i = 0; i < 30; i++) {
			// R.layout.the_layout_file is your layout from the question
			View inflatedView = getLayoutInflater().inflate(
					R.layout.list_item_pharmacy, Ll, false);
			@SuppressWarnings("unused")
			TextView tv = (TextView) inflatedView.findViewById(R.id.textView1);

			Ll.addView(inflatedView);
		}

	}

}
