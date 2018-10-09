package com.smaat.ipharma.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import com.smaat.ipharma.R;
import com.smaat.ipharma.main.BaseActivity;

public class GCMTempScreen extends BaseActivity {
    String typeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        ViewGroup mViewGroup = (ViewGroup) findViewById(R.id.parent_layout);
        setupUI(mViewGroup);

        initview();
    }

    private void initview() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            typeId = bundle.getString("type");
        }
        Intent in = new Intent(this, HomeScreen.class);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        in.putExtra("mTypeId", typeId);
        startActivity(in);
        finish();
    }
}
