package com.smaat.renterblock.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.smaat.renterblock.R;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.InterfaceBtnCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlertAddLocationFragment extends BaseFragment {

    @BindView(R.id.enter_search_name)
    EditText mEnterSearchNameEdtTxt;

    @BindView(R.id.save)
    Button mSaveBtn;

    @BindView(R.id.cancel)
    Button mCancelBtn;


    @BindView(R.id.alert_location_recycler_view)
    RecyclerView mAlertLocationRecyclerView;

    @BindView(R.id.alert_par_lay)
    RelativeLayout mParentLay;

    private String searchArea = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_location_alert, container, false);
          /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);
         /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);
        /*For focus current fragment*/

        /*For focus current fragment*/
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    default:
                        v.performClick();
                }
                return true;
            }
        });
        return rootView;
    }


    /*Fragment manual onResume*/
    @Override
    public void onFragmentResume() {
        super.onFragmentResume();
         /* If the value of visibleInt is zero,  the view will set gone. Or if the value of visibleInt is one,  the view will set visible. Or else, the view will set gone*/
        if (getActivity() != null) {
             /*Slide menu action*/
            ((HomeScreen) getActivity()).setDrawerAction(true);

            /*set header first  img*/
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(1);
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.edit_icon, 1);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 2);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.drawable.add_icon, 0,"");

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.app_name), 0);

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.alerts), 0);
            ((HomeScreen) getActivity()).setHeaderEdt(getActivity().getString(R.string.alerts), 0);

            initView();
        }

    }

    private void initView() {
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEnterSearchNameEdtTxt.getText().toString().trim().isEmpty() || mEnterSearchNameEdtTxt.getText().toString().trim().equalsIgnoreCase(getString(R.string.location))) {
                    DialogManager.getInstance().showAlertPopup(getContext(), getString(R.string.please_enter_location), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {

                        }
                    });
                } else {
//                    AppConstants.ALERTS_RESULT_ENTITY.clear();
                    AppConstants.ALERT_LOCATION = mEnterSearchNameEdtTxt.getText().toString().trim();
                    AppConstants.ALERT = AppConstants.ALERT_CREATE;
                }
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AppConstants.ALERTS_RESULT_ENTITY.clear();
                ((HomeScreen) getActivity()).addFragment(new AlertsFragment());
            }
        });

        mEnterSearchNameEdtTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mAlertLocationFrameLay.setVisibility(View.VISIBLE);
            }
        });
        mParentLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mAlertLocationFrameLay.setVisibility(View.GONE);
                hideSoftKeyboard();
            }
        });

        mEnterSearchNameEdtTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
                searchArea = s.toString().trim();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
