package com.smaat.virtualtrainer.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smaat.virtualtrainer.R;
import com.smaat.virtualtrainer.adapter.AddJoinessAdapter;
import com.smaat.virtualtrainer.apiinterface.APIRequestHandler;
import com.smaat.virtualtrainer.entity.UserDetailsEntityRes;
import com.smaat.virtualtrainer.main.BaseActivity;
import com.smaat.virtualtrainer.model.InviteUserListEntity;
import com.smaat.virtualtrainer.model.UserListEntity;
import com.smaat.virtualtrainer.utils.AppConstants;
import com.smaat.virtualtrainer.utils.DialogManager;
import com.smaat.virtualtrainer.utils.DialogMangerTwoBtnCallback;
import com.smaat.virtualtrainer.utils.GlobalMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddJoinessScreen extends BaseActivity implements DialogMangerTwoBtnCallback {

    @BindView(R.id.parent_lay)
    ViewGroup mAddJoinessViewGroup;

    @BindView(R.id.header_left_btn_lay)
    RelativeLayout mHeaderLeftBtnLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.header_curve_lay)
    RelativeLayout mHeaderCurveLay;

    @BindView(R.id.menu_with_curve_lay)
    RelativeLayout mMenuWithCurveLay;

    @BindView(R.id.email_id_autotxt)
    AutoCompleteTextView mEmailIdAutoTxt;

    @BindView(R.id.invite_list)
    RecyclerView mInviteRecyclerListView;

    @BindView(R.id.menu_out_side_view)
    View mMenuOutSideView;

    private ArrayList<UserDetailsEntityRes> mAddEmailIdUDEArrList;
    private ArrayList<UserDetailsEntityRes> mAllUserUSEArrList;
    public static ArrayList<UserDetailsEntityRes> sIdUDEArrList;
    private AddJoinessAdapter mAddJoinessAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_add_joiness);

        mAddEmailIdUDEArrList = new ArrayList<>();
        mAllUserUSEArrList = new ArrayList<>();
        sIdUDEArrList = new ArrayList<>();

        initComponent();
    }


    private void initComponent() {
        ButterKnife.bind(this);
        setupUI(mAddJoinessViewGroup);

        mHeaderLeftBtnLay.setVisibility(View.VISIBLE);

        mHeaderCurveLay.setVisibility(View.VISIBLE);
        mMenuWithCurveLay.setVisibility(View.GONE);
        mMenuOutSideView.setVisibility(View.GONE);

        mHeaderTxt.setText(getString(R.string.add_joiness));
        animationAction();


//        mEmailIdAutoTxt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                searchUserEmailID(s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        APIRequestHandler.getInstance().userListAPICall(this);

    }

    private void animationAction() {
        mEnterFromTop.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mHeaderCurveLay.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mMenuOutSideView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mExitToTop.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mMenuOutSideView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mHeaderCurveLay.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @OnClick({R.id.header_left_btn_lay, R.id.email_id_add_img, R.id.recent_list_txt, R.id.invite_btn, R.id.profile_lay, R.id.help_lay, R.id.buy_full_version_lay, R.id.terms_condition_lay, R.id.support_lay
            , R.id.logout_lay, R.id.menu_curve_lay, R.id.menu_out_side_view, R.id.header_menu_img})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.email_id_add_img:
//                String emailStr = mEmailIdAutoTxt.getText().toString();
//
//                if (emailStr.isEmpty() || !GlobalMethods.isEmailValid(emailStr)) {
//                    mEmailIdAutoTxt.requestFocus();
//                    DialogManager.showBasicAlertDialog(this, getString(R.string.enter_email), getString(R.string.ok), false, getString(R.string.ok), true, true, AddJoinessScreen.this);
//                } else {
//
////                    searchUserEmailID(emailStr);
//                    sIdUDEArrList.add(emailStr);
//                    mEmailIdAutoTxt.setText("");
//                    setAddJoinessAdapter(sIdUDEArrList);
//
//                }

                break;
            case R.id.recent_list_txt:

                mHeaderCurveLay.setVisibility(View.VISIBLE);
                mMenuWithCurveLay.setVisibility(View.GONE);
                mMenuOutSideView.setVisibility(View.GONE);
                DialogManager.showBasicAlertDialog(this, getString(R.string.future_release), getString(R.string.ok), false, getString(R.string.ok), true, true, AddJoinessScreen.this);
                break;
            case R.id.invite_btn:

                mHeaderCurveLay.setVisibility(View.VISIBLE);
                mMenuWithCurveLay.setVisibility(View.GONE);
                mMenuOutSideView.setVisibility(View.GONE);

                String selectedemailStr = "";

                for (int i = 0; i < mAddEmailIdUDEArrList.size(); i++) {
                    selectedemailStr += "," + mAddEmailIdUDEArrList.get(i).getSvt_user_id();
                }


                APIRequestHandler.getInstance().inviteUserListAPICall(AppConstants.STREAMING_NAME, AppConstants.STREAMING_ID,
                        selectedemailStr, this);

                break;
            case R.id.profile_lay:

                mHeaderCurveLay.setVisibility(View.VISIBLE);
                mMenuWithCurveLay.setVisibility(View.GONE);
                mMenuOutSideView.setVisibility(View.GONE);
                if (!(mActivity instanceof ProfileScreen))
                    nextScreen(ProfileScreen.class, false);


                break;
            case R.id.help_lay:

                mHeaderCurveLay.setVisibility(View.VISIBLE);
                mMenuWithCurveLay.setVisibility(View.GONE);
                mMenuOutSideView.setVisibility(View.GONE);
                if (!(mActivity instanceof HelpandTermsScreen)) {
                    mHeaderType = 1;
                    nextScreen(HelpandTermsScreen.class, false);
                }

                break;
            case R.id.buy_full_version_lay:

                mHeaderCurveLay.setVisibility(View.VISIBLE);
                mMenuWithCurveLay.setVisibility(View.GONE);
                mMenuOutSideView.setVisibility(View.GONE);
                if (!(mActivity instanceof ProVersionScreen)) {
                    nextScreen(ProVersionScreen.class, false);
                }

                break;
            case R.id.terms_condition_lay:

                mHeaderCurveLay.setVisibility(View.VISIBLE);
                mMenuWithCurveLay.setVisibility(View.GONE);
                mMenuOutSideView.setVisibility(View.GONE);
                if (!(mActivity instanceof HelpandTermsScreen)) {
                    mHeaderType = 2;
                    nextScreen(HelpandTermsScreen.class, false);
                }

                break;
            case R.id.support_lay:

                mHeaderCurveLay.setVisibility(View.VISIBLE);
                mMenuWithCurveLay.setVisibility(View.GONE);
                mMenuOutSideView.setVisibility(View.GONE);
                if (!(mActivity instanceof SupportScreen)) {
                    nextScreen(SupportScreen.class, false);
                }

                break;
            case R.id.logout_lay:

                mHeaderCurveLay.setVisibility(View.VISIBLE);
                mMenuWithCurveLay.setVisibility(View.GONE);
                mMenuOutSideView.setVisibility(View.GONE);
                DialogManager.showBasicAlertDialog(mActivity, getString(R.string.logout_msg), getString(R.string.no), true, getString(R.string.yes), true, false, new DialogMangerTwoBtnCallback() {
                    @Override
                    public void onYesClick() {
                        GlobalMethods.userDetails(getString(R.string.three), AppConstants.FAILURE_CODE, "", "", "", AppConstants.FAILURE_CODE, AddJoinessScreen.this);
                        previousScreen(EntryScreen.class, true);
                    }

                    @Override
                    public void onNoClick() {

                    }
                });
                break;

            case R.id.menu_out_side_view:
            case R.id.menu_curve_lay:
                mMenuWithCurveLay.setVisibility(View.GONE);
                mMenuOutSideView.setVisibility(View.GONE);
                mMenuWithCurveLay.startAnimation(mExitToTop);
                break;

            case R.id.header_menu_img:
                mMenuWithCurveLay.setVisibility(View.VISIBLE);
                mMenuWithCurveLay.startAnimation(mEnterFromTop);
                break;

//            case R.id.add_call_img:
//                if (mTempAddCallEdu.getText().toString().length() > 0) {
//                    GlobalMethods.storeStringValue(this, AppConstants.CREATOR_JOINNER_STATUS, AppConstants.FAILURE_CODE);
//                    nextScreen(SampleActivity.class, false);
//
//                } else {
//                    DialogManager.showBasicAlertDialog(mActivity, getString(R.string.tem_msg_id), getString(R.string.no), false, getString(R.string.yes), true, false, this);
//                }
//                break;

        }


    }


//    private void searchUserEmailID(String mSearchVal) {
//
//        if (!mSearchVal.trim().isEmpty()) {
//            mAllUserAdpStrArrList.clear();
//
//            if (mAllUserUSEArrList != null && mAllUserUSEArrList.size() > 0) {
//                for (int i = 0; i < mAllUserUSEArrList.size(); i++) {
//                    String universit = mAllUserUSEArrList.get(i).toLowerCase(Locale.ENGLISH);
//                    if (universit.contains(mSearchVal.toLowerCase(Locale.ENGLISH))) {
//
//                        mAllUserAdpStrArrList.add(mAllUserUSEArrList.get(i));
//
//                    }
//
//                    if (mAllUserAdpStrArrList != null && mAllUserAdpStrArrList.size() > 0) {
//                        mAllUserListAdaptr = new InviteStreamingAdapter(this, mAllUserAdpStrArrList);
//                        mAllUserListAdaptr.notifyDataSetChanged();
//                        mAllUserRecyclerListView.setLayoutManager(new LinearLayoutManager(this));
//                        mAllUserRecyclerListView.setAdapter(mAllUserListAdaptr);
//                        mAllUserRecyclerListView.setVisibility(View.VISIBLE);
//                    } else {
//                        mAllUserRecyclerListView.setVisibility(View.GONE);
//                    }
//                }
//            } else {
//                mAllUserRecyclerListView.setVisibility(View.GONE);
//            }
//
//
//        } else {
//            mAllUserRecyclerListView.setVisibility(View.GONE);
//        }
//
//    }


    private void setAutoCompleteAdapter() {

        final ArrayList<String> idsList = new ArrayList<>();

        for (int i = 0; i < mAllUserUSEArrList.size(); i++) {
            idsList.add(mAllUserUSEArrList.get(i).getEmail_id());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, idsList);
        mEmailIdAutoTxt.setThreshold(1);
        mEmailIdAutoTxt.setAdapter(adapter);

        mEmailIdAutoTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean isMailBool = true;
                for (int i = 0; i < mAddEmailIdUDEArrList.size(); i++) {
                    if (idsList.get(position).equals(mAddEmailIdUDEArrList.get(i).getEmail_id())) {
                        isMailBool = false;
                        break;
                    }
                }

                if (isMailBool) {
                    sIdUDEArrList.add(mAllUserUSEArrList.get(position));
                    hideSoftKeyboard(AddJoinessScreen.this);
                    mEmailIdAutoTxt.setText("");
                    setAddJoinessAdapter(sIdUDEArrList);
                } else {
                    mEmailIdAutoTxt.setText("");
                    hideSoftKeyboard(AddJoinessScreen.this);
                    mEmailIdAutoTxt.requestFocus();
                    DialogManager.showBasicAlertDialog(AddJoinessScreen.this, getString(R.string.enter_email), getString(R.string.ok), false, getString(R.string.ok), true, true, AddJoinessScreen.this);
                }
            }
        });

    }

    private void setAddJoinessAdapter(ArrayList<UserDetailsEntityRes> mList) {
        mInviteRecyclerListView.setVisibility(View.VISIBLE);
        if (mAddJoinessAdapter == null) {
            mAddEmailIdUDEArrList = new ArrayList<>();
            mAddEmailIdUDEArrList.addAll(mList);
            mAddJoinessAdapter = new AddJoinessAdapter(this, mAddEmailIdUDEArrList);
            mInviteRecyclerListView.setLayoutManager(new LinearLayoutManager(this));
            mInviteRecyclerListView.setAdapter(mAddJoinessAdapter);


        } else {
            mAddEmailIdUDEArrList.clear();
            mAddEmailIdUDEArrList.addAll(mList);

            if (mAddEmailIdUDEArrList.size() == 0) {
                mInviteRecyclerListView.setVisibility(View.GONE);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAddJoinessAdapter.notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);
        if (responseObj instanceof UserListEntity) {
            UserListEntity userListRes = (UserListEntity) responseObj;

            if (userListRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                ArrayList<UserDetailsEntityRes> mUserDetailsUDEArrList = new ArrayList<>();

                mUserDetailsUDEArrList.addAll(userListRes.getResult());

                for (int i = 0; i < mUserDetailsUDEArrList.size(); i++) {
                    mAllUserUSEArrList.add(mUserDetailsUDEArrList.get(i));
                }
                setAutoCompleteAdapter();

            } else {
                DialogManager.showBasicAlertDialog(this, userListRes.getMessage(), getString(R.string.no),
                        false, getString(R.string.yes), true, true, this);
            }
        } else if (responseObj instanceof InviteUserListEntity) {
            InviteUserListEntity inviteUserListRes = (InviteUserListEntity) responseObj;
            if (inviteUserListRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                DialogManager.showBasicAlertDialog(this, inviteUserListRes.getMessage(), getString(R.string.no),
                        false, getString(R.string.yes), true, true, new DialogMangerTwoBtnCallback() {
                            @Override
                            public void onYesClick() {
                                onBackPressed();
                            }

                            @Override
                            public void onNoClick() {
                                onBackPressed();

                            }
                        });
            } else {
                DialogManager.showBasicAlertDialog(this, inviteUserListRes.getMessage(), getString(R.string.no),
                        false, getString(R.string.yes), true, true, this);
            }

        }
    }


    @Override
    public void onBackPressed() {
        mHeaderCurveLay.setVisibility(View.VISIBLE);
        mMenuWithCurveLay.setVisibility(View.GONE);
        finishScreen();
    }

    @Override
    public void onYesClick() {

    }

    @Override
    public void onNoClick() {

    }


}
