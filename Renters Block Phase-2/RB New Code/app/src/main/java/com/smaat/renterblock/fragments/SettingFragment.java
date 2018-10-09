package com.smaat.renterblock.fragments;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.smaat.renterblock.R;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.SettingResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.ContentURLScreen;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.InterfaceBtnCallback;
import com.smaat.renterblock.utils.PreferenceUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingFragment extends BaseFragment {

    @BindView(R.id.sync_contact)
    ToggleButton mSyncContactToglBtn;

    @BindView(R.id.tog_new_saved_search)
    ToggleButton mNewSavedSearchToglBtn;

    @BindView(R.id.tog_update_results)
    ToggleButton mUpdateResultMapMovesToglBtn;

    @BindView(R.id.search_count_edit)
    EditText mSearchResultCountEdtTxt;

    String mSyncContactStr = "", mNewSavedSearchStr = "", mUpdateResultStr = "", mSearchResultCountStr;
    private boolean mSyncContactBool = false, mNewSavedSearchBool = false, mUpdateResultBool = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstances) {
        View rootView = inflater.inflate(R.layout.setting_fragment, container, false);
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);
        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);

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

    @Override
    public void onFragmentResume() {
        super.onFragmentResume();
         /* If the value of visibleInt is zero,  the view will set gone. Or if the value of visibleInt is one,  the view will set visible. Or else, the view will set gone*/
        if (getActivity() != null) {
             /*Slide menu action*/
            ((HomeScreen) getActivity()).setDrawerAction(true);

            /*set header first  img*/
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(1);
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.mipmap.app_icon, 2);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 0);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.mipmap.app_icon, 0,"");

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.app_name), 0);

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.settings), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.app_name), 0);

            initView();
        }
    }

    private void initView() {


        mSyncContactStr = PreferenceUtil.getStringValue(getContext(), AppConstants.SYNC_CONTACTS);
        mNewSavedSearchStr = PreferenceUtil.getStringValue(getContext(), AppConstants.NEW_SAVED_SEARCH_MATCHES);
        mUpdateResultStr = PreferenceUtil.getStringValue(getContext(), AppConstants.UPDATE_RESULT_AS_MAP_MOVES);
        mSearchResultCountStr = PreferenceUtil.getStringValue(getContext(), AppConstants.SEARCH_RESULT_COUNT).isEmpty() ? getString(R.string.fifty) :
                PreferenceUtil.getStringValue(getContext(), AppConstants.SEARCH_RESULT_COUNT);
        mSearchResultCountEdtTxt.setText(mSearchResultCountStr);
        mSearchResultCountEdtTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView searchTxtView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String searchResultStr = searchTxtView.getText().toString().trim();
                    if (!searchResultStr.isEmpty() && Integer.valueOf(searchResultStr) > 9) {
                        PreferenceUtil.storeStringValue(getContext(), AppConstants.SEARCH_RESULT_COUNT, searchTxtView.getText().toString());
                    }
                    hideSoftKeyboard();
                    return true;
                } else
                    return false;
            }
        });
        mSearchResultCountEdtTxt.setFilters(new InputFilter[]{new InputFilterMinMax(1, 50)});

        if (mSyncContactStr.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
            mSyncContactToglBtn.setBackgroundResource(R.drawable.toggle_on);
        } else {
            mSyncContactToglBtn.setBackgroundResource(R.drawable.toggle_off);
        }

        if (mNewSavedSearchStr.equalsIgnoreCase(AppConstants.ON)) {
            mNewSavedSearchToglBtn.setBackgroundResource(R.drawable.toggle_on);
        } else {
            mNewSavedSearchToglBtn.setBackgroundResource(R.drawable.toggle_off);
        }

        if (mUpdateResultStr.equalsIgnoreCase(AppConstants.ON)) {
            mUpdateResultMapMovesToglBtn.setBackgroundResource(R.drawable.toggle_on);
        } else {
            mUpdateResultMapMovesToglBtn.setBackgroundResource(R.drawable.toggle_off);
        }
    }

    /*set OnClick*/
    @OnClick({R.id.sync_contact, R.id.tog_new_saved_search, R.id.tog_update_results, R.id.about_lay, R.id.terms_lay, R.id.privacy_lay})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.sync_contact:
                mSyncContactToglBtn.setBackgroundResource(mSyncContactBool ? R.drawable.toggle_on : R.drawable.toggle_off);
                if (mSyncContactBool) {
                    PreferenceUtil.storeStringValue(getContext(), AppConstants.SYNC_CONTACTS, "1");
                    mSyncContactBool = false;
                    stroedToCheckUpdateTime();
                } else {
                    PreferenceUtil.storeStringValue(getContext(), AppConstants.SYNC_CONTACTS, "0");
                    mSyncContactBool = true;
                }
                break;
            case R.id.tog_new_saved_search:
                String on_off_Str;
                mNewSavedSearchToglBtn.setBackgroundResource(mNewSavedSearchBool ? R.drawable.toggle_on : R.drawable.toggle_off);
                if (mNewSavedSearchBool) {
                    PreferenceUtil.storeStringValue(getContext(), AppConstants.NEW_SAVED_SEARCH_MATCHES, "ON");
                    mNewSavedSearchBool = false;
                    on_off_Str = "1";
                    APIRequestHandler.getInstance().settingAPICall(on_off_Str, SettingFragment.this);
                } else {
                    PreferenceUtil.storeStringValue(getContext(), AppConstants.NEW_SAVED_SEARCH_MATCHES, "OFF");
                    mNewSavedSearchBool = true;
                    on_off_Str = "0";
                    APIRequestHandler.getInstance().settingAPICall(on_off_Str, SettingFragment.this);
                }
                break;
            case R.id.tog_update_results:
                mUpdateResultMapMovesToglBtn.setBackgroundResource(mUpdateResultBool ? R.drawable.toggle_off : R.drawable.toggle_on);
                if (mUpdateResultBool) {
                    PreferenceUtil.storeStringValue(getContext(), AppConstants.UPDATE_RESULT_AS_MAP_MOVES, "OFF");
                    mUpdateResultBool = false;
                } else {
                    PreferenceUtil.storeStringValue(getContext(), AppConstants.UPDATE_RESULT_AS_MAP_MOVES, "ON");
                    mUpdateResultBool = true;
                }
                break;
            case R.id.about_lay:
                AppConstants.CONTENT_HEADER = getString(R.string.about_renters_blocks);
                AppConstants.CONTENT_URL = AppConstants.AB;
                nextScreen(ContentURLScreen.class, false);

                break;
            case R.id.terms_lay:
                AppConstants.CONTENT_HEADER = getString(R.string.terms_of_service);
                AppConstants.CONTENT_URL = AppConstants.TC;
                nextScreen(ContentURLScreen.class, false);
                break;
            case R.id.privacy_lay:
                AppConstants.CONTENT_HEADER = getString(R.string.privacy_policy);
                AppConstants.CONTENT_URL = AppConstants.PP;
                nextScreen(ContentURLScreen.class, false);

                break;

        }
    }


    private void stroedToCheckUpdateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String dateTimeFormatStr = dateFormat.format(new Date());
        PreferenceUtil.storeStringValue(getContext(), AppConstants.Settings_current_time, dateTimeFormatStr);
    }

//    private class InputFilterMinMax implements InputFilter {
//        private int min, max;
//
//        private InputFilterMinMax(int min, int max) {
//            this.min = min;
//            this.max = max;
//        }
//
//
//        @Override
//        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
//
//            try {
//                int input = Integer.parseInt(charSequence.toString() + spanned.toString());
//                if (IsRange(min, max, input))
//                    return null;
//            } catch (NumberFormatException numberFormatException) {
//                Log.e(AppConstants.TAG, numberFormatException.toString());
//
//            }
//            return "";
//        }
//
//        private boolean IsRange(int a, int b, int c) {
//            return b > a ? c >= a && c <= b : c >= b && c <= a;
//
//        }
//    }

    public void onRequestSuccess(Object mResponseObj) {
        super.onRequestSuccess(mResponseObj);
        if (mResponseObj instanceof SettingResponse) {
            SettingResponse mSettingEntity = (SettingResponse) mResponseObj;
            DialogManager.getInstance().showAlertPopup(getContext(), mSettingEntity.getMsg(), new InterfaceBtnCallback() {
                @Override
                public void onPositiveClick() {
                    baseFragmentAlertDismiss(null);
                }
            });
        }
    }

    public class InputFilterMinMax implements InputFilter {

        private int min, max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public InputFilterMinMax(String min, String max) {
            this.min = Integer.parseInt(min);
            this.max = Integer.parseInt(max);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException nfe) {
            }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
}
