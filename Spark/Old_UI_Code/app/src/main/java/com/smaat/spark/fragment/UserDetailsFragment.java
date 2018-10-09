package com.smaat.spark.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.smaat.spark.R;
import com.smaat.spark.entity.inputEntity.ChatSendReceiveInputEntity;
import com.smaat.spark.entity.outputEntity.UserDetailsEntity;
import com.smaat.spark.main.BaseFragment;
import com.smaat.spark.model.CommonResponse;
import com.smaat.spark.services.APIRequestHandler;
import com.smaat.spark.ui.HomeScreen;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.DialogManager;
import com.smaat.spark.utils.GlobalMethods;
import com.smaat.spark.utils.InterfaceBtnCallback;
import com.smaat.spark.utils.InterfaceTwoBtnCallback;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserDetailsFragment extends BaseFragment implements InterfaceBtnCallback {

    @BindView(R.id.user_name_txt)
    TextView mUserNameTxt;

    @BindView(R.id.address_txt)
    TextView mAddressTxt;

    @BindView(R.id.user_interest_txt)
    TextView mUserInterestTxt;

    @BindView(R.id.add_msg_req_btn)
    Button mAddMsgReqBtn;

    @BindView(R.id.ignore_del_btn)
    Button mIgnoreDelBtn;

    @BindView(R.id.user_img_pager)
    ViewPager mUserImgPager;

    @BindView(R.id.default_profile_img)
    ImageView mDefaultProfileImg;

    @BindView(R.id.indicator_one_img)
    ImageView mIndicatorOneImg;

    @BindView(R.id.indicator_two_img)
    ImageView mIndicatorTwoImg;

    @BindView(R.id.indicator_three_img)
    ImageView mIndicatorThreeImg;

    @BindView(R.id.indicator_four_img)
    ImageView mIndicatorFourImg;

    @BindView(R.id.indicator_five_img)
    ImageView mIndicatorFiveImg;


    @BindView(R.id.block_btn_lay)
    RelativeLayout mBlockBtnLay;


    private UserDetailsEntity mUserDetailsArrRes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_user_details_screen, container, false);
        ButterKnife.bind(this, rootView);
        setupUI(rootView);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        ((HomeScreen) getActivity()).setDrawerAction(false);
        ((HomeScreen) getActivity()).setHeaderText(getString(R.string.app_name));
        ((HomeScreen) getActivity()).setHeadRigImgVisible(false, R.mipmap.app_icon);


        mUserDetailsArrRes = new Gson().fromJson(AppConstants.OTHER_USER_DETAILS, UserDetailsEntity.class);
        setUserData();
    }


    @OnClick({R.id.block_btn_lay, R.id.add_msg_req_btn, R.id.ignore_del_btn})
    public void onClick(View v) {
        String addDelReqStr = mAddMsgReqBtn.getText().toString().trim();
        String ignoreDelStr = mIgnoreDelBtn.getText().toString().trim();
        switch (v.getId()) {
            case R.id.block_btn_lay:
                addAndDelFriendAPICall(AppConstants.API_BLOCK_FRIEND, getString(R.string.block_friend));

                break;
            case R.id.add_msg_req_btn:
                if (addDelReqStr.equalsIgnoreCase(getString(R.string.add_friend))) {
                    ChatSendReceiveInputEntity friendListInputEntityRes = new ChatSendReceiveInputEntity(AppConstants.API_ADD_FRIEND, AppConstants.PARAMS_ADD_FRIEND, GlobalMethods.getUserID(getActivity()), mUserDetailsArrRes.getUser_id());
//                            AppConstants.OTHER_USER_ID);
                    APIRequestHandler.getInstance().callAddFriendAPI(friendListInputEntityRes, UserDetailsFragment.this);
                } else if (addDelReqStr.equalsIgnoreCase(getString(R.string.message))) {
                    AppConstants.CHAT_SUB = AppConstants.CHAT_SUB_FRIEND;
                    AppConstants.CHAT_FRIEND_ID = mUserDetailsArrRes.getUser_id();
                    AppConstants.CHAT_FRIEND_NAME = mUserDetailsArrRes.getUsername();
//                    nextScreen(ChatNormalScreen.class, false);
                    ((HomeScreen) getActivity()).addFragment(new ChatFriendFragment());
                }

                break;
            case R.id.ignore_del_btn:
                if (ignoreDelStr.equalsIgnoreCase(getString(R.string.ignore))) {
                    addAndDelFriendAPICall(AppConstants.API_IGNORE_FRIEND, getString(R.string.ignore_friend));
                } else if (ignoreDelStr.equalsIgnoreCase(getString(R.string.del_friend))) {
                    addAndDelFriendAPICall(AppConstants.API_REMOVE_FRIEND, getString(R.string.delete_friend));
                }
                break;

        }
    }


    private void addAndDelFriendAPICall(final String callApiNameStr, String alertMsgStr) {
        DialogManager.getInstance().showOptionPopup(getActivity(), alertMsgStr, getString(R.string.yes), getString(R.string.cancel), new InterfaceTwoBtnCallback() {
            @Override
            public void onYesClick() {
                ChatSendReceiveInputEntity friendListInputEntityRes = new ChatSendReceiveInputEntity(callApiNameStr, AppConstants.PARAMS_ADD_FRIEND, GlobalMethods.getUserID(getActivity()), mUserDetailsArrRes.getUser_id());
//                        AppConstants.OTHER_USER_ID);
                APIRequestHandler.getInstance().callAddFriendAPI(friendListInputEntityRes, UserDetailsFragment.this);
            }

            @Override
            public void onNoClick() {

            }
        });


    }


    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);

        if (resObj instanceof CommonResponse) {
            CommonResponse addFriendRes = (CommonResponse) resObj;
            if (addFriendRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                DialogManager.getInstance().showAlertPopup(getActivity(), addFriendRes.getMessage());
                getActivity().onBackPressed();

            } else {
                DialogManager.getInstance().showAlertPopup(getActivity(), addFriendRes.getMessage());
            }

        }
    }


    private void setUserData() {
        ((HomeScreen) getActivity()).setHeaderText(mUserDetailsArrRes.getUsername());
        mUserNameTxt.setText(mUserDetailsArrRes.getUsername());
        mAddressTxt.setText(mUserDetailsArrRes.getAddress());
        mUserInterestTxt.setText(mUserDetailsArrRes.getInterests());


        sysOut("Friend ID" + mUserDetailsArrRes.getUser_id());
        sysOut("Friend Name" + mUserDetailsArrRes.getUsername());

        String addDelReqStr = getString(R.string.message);
        String ignoreDelStr = getString(R.string.del_friend);

        if (mUserDetailsArrRes.getFriend().equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
            addDelReqStr = getString(R.string.add_friend);
            ignoreDelStr = getString(R.string.ignore);
        } else if (mUserDetailsArrRes.getFriend().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
            addDelReqStr = getString(R.string.req_sent);
            ignoreDelStr = getString(R.string.ignore);
        }

        mBlockBtnLay.setVisibility(addDelReqStr.equals(getString(R.string.message)) ? View.VISIBLE : View.INVISIBLE);
        mAddMsgReqBtn.setText(addDelReqStr);
        mIgnoreDelBtn.setText(ignoreDelStr);

        ArrayList<String> imageStrArrList = new ArrayList<>();
        if (!mUserDetailsArrRes.getMain_picture().trim().isEmpty()) {
            imageStrArrList.add(mUserDetailsArrRes.getMain_picture());
        }

        if (!mUserDetailsArrRes.getMore_mages().trim().isEmpty()) {

            String moreImagesList[] = mUserDetailsArrRes.getMore_mages().trim().split(",");
            Collections.addAll(imageStrArrList, moreImagesList);
        }

        mDefaultProfileImg.setVisibility(imageStrArrList.size() > 0 ? View.GONE : View.VISIBLE);
        mUserImgPager.setVisibility(imageStrArrList.size() > 0 ? View.VISIBLE : View.GONE);
        if (imageStrArrList.size() > 0) {
            mUserImgPager.setAdapter(new UserImagesPager(getActivity(), imageStrArrList));
            setViewPageIndicator(imageStrArrList.size());
        } else {
            setViewPageIndicator(1);
        }

        mUserImgPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mIndicatorOneImg.setImageResource(position == 0 ? R.drawable.circle_sky_blue_bg : R.drawable.circle_gray_bg);
                mIndicatorTwoImg.setImageResource(position == 1 ? R.drawable.circle_sky_blue_bg : R.drawable.circle_gray_bg);
                mIndicatorThreeImg.setImageResource(position == 2 ? R.drawable.circle_sky_blue_bg : R.drawable.circle_gray_bg);
                mIndicatorFourImg.setImageResource(position == 3 ? R.drawable.circle_sky_blue_bg : R.drawable.circle_gray_bg);
                mIndicatorFiveImg.setImageResource(position == 4 ? R.drawable.circle_sky_blue_bg : R.drawable.circle_gray_bg);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mUserImgPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                // do transformation here
                page.startAnimation(mShakeAnimation);
            }
        });
    }

    private void setViewPageIndicator(int pageCount) {
        if (pageCount == 1) {

            mIndicatorOneImg.setVisibility(View.VISIBLE);
            mIndicatorTwoImg.setVisibility(View.GONE);
            mIndicatorThreeImg.setVisibility(View.GONE);
            mIndicatorFourImg.setVisibility(View.GONE);
            mIndicatorFiveImg.setVisibility(View.GONE);

        } else if (pageCount == 2) {

            mIndicatorOneImg.setVisibility(View.VISIBLE);
            mIndicatorTwoImg.setVisibility(View.VISIBLE);
            mIndicatorThreeImg.setVisibility(View.GONE);
            mIndicatorFourImg.setVisibility(View.GONE);
            mIndicatorFiveImg.setVisibility(View.GONE);
        } else if (pageCount == 3) {

            mIndicatorOneImg.setVisibility(View.VISIBLE);
            mIndicatorTwoImg.setVisibility(View.VISIBLE);
            mIndicatorThreeImg.setVisibility(View.VISIBLE);
            mIndicatorFourImg.setVisibility(View.GONE);
            mIndicatorFiveImg.setVisibility(View.GONE);
        } else if (pageCount == 4) {

            mIndicatorOneImg.setVisibility(View.VISIBLE);
            mIndicatorTwoImg.setVisibility(View.VISIBLE);
            mIndicatorThreeImg.setVisibility(View.VISIBLE);
            mIndicatorFourImg.setVisibility(View.VISIBLE);
            mIndicatorFiveImg.setVisibility(View.GONE);
        } else {

            mIndicatorOneImg.setVisibility(View.VISIBLE);
            mIndicatorTwoImg.setVisibility(View.VISIBLE);
            mIndicatorThreeImg.setVisibility(View.VISIBLE);
            mIndicatorFourImg.setVisibility(View.VISIBLE);
            mIndicatorFiveImg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onYesClick() {

    }

    private class UserImagesPager extends PagerAdapter {
        private Context mContext;
        private ArrayList<String> mUserStrArrList;

        private UserImagesPager(Context context, ArrayList<String> userList) {
            mContext = context;
            mUserStrArrList = userList;
        }

        @Override
        public int getCount() {
            if (mUserStrArrList.size() > 5) {
                return 5;
            }
            return mUserStrArrList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ViewGroup rootViewGrp = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.adp_pager_img_view,
                    container, false);

            final ImageView profileImg = (ImageView) rootViewGrp.findViewById(R.id.profile_img);
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (mUserStrArrList.get(position).isEmpty()) {

                        profileImg.setImageResource(R.drawable.default_user_img);
                    } else {
                        try {
                            Glide.with(mContext)
                                    .load(mUserStrArrList.get(position)).asBitmap().fitCenter().into(profileImg);

                        } catch (Exception e) {
                            Log.e(AppConstants.TAG, e.toString());
                            profileImg.setImageResource(R.drawable.default_user_img);
                        }
                    }
                }
            });

            profileImg.setTag(position);
            profileImg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int posInt = (int) view.getTag();
                    DialogManager.getInstance().showOriginalImgPopup(getActivity(), mUserStrArrList.get(posInt));
                    return true;
                }
            });
            container.addView(rootViewGrp);
            return rootViewGrp;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }

    }
//    public class ParallaxPageTransformer implements ViewPager.PageTransformer {
//
//        public void transformPage(View view, float position) {
//
//            int pageWidth = view.getWidth();
//
//
//            if (position < -1) { // [-Infinity,-1)
//                // This page is way off-screen to the left.
//                view.setAlpha(1);
//
//            } else if (position <= 1) { // [-1,1]
//
//                dummyImageView.setTranslationX(-position * (pageWidth / 2)); //Half the normal speed
//
//            } else { // (1,+Infinity]
//                // This page is way off-screen to the right.
//                view.setAlpha(1);
//            }
//
//
//        }
//    }
}
