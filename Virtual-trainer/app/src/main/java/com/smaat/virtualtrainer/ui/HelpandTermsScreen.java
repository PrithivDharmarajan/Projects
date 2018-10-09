package com.smaat.virtualtrainer.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smaat.virtualtrainer.R;
import com.smaat.virtualtrainer.main.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HelpandTermsScreen extends BaseActivity {

    @BindView(R.id.parent_lay)
    ViewGroup mWelComeViewGroup;

    @BindView(R.id.wel_txt)
    TextView mWelTxt;

    @BindView(R.id.cont_btn)
    Button mContBtn;

    @BindView(R.id.header_lay)
    RelativeLayout mHeaderLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_welcome_screen);


    }

    @Override
    protected void onResume() {
        super.onResume();
        initComponent();
    }

    private void initComponent() {
        ButterKnife.bind(this);
        setupUI(mWelComeViewGroup);

        mWelTxt.setVisibility(View.GONE);
        mContBtn.setVisibility(View.GONE);
        mHeaderLeftBtnLay.setVisibility(View.VISIBLE);
        mHeaderRightBtnLay.setVisibility(View.INVISIBLE);
        mHeaderLay.setVisibility(View.VISIBLE);


        mHeaderTxt.setText(mHeaderType == 1 ? getString(R.string.help) : getString(R.string.terms_cond));
    }


    @OnClick({R.id.header_left_btn_lay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;

        }

    }

    @Override
    public void onBackPressed() {
        finishScreen();
    }
}
