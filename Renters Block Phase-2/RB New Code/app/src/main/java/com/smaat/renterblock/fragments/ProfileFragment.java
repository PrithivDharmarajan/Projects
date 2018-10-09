package com.smaat.renterblock.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smaat.renterblock.R;
import com.smaat.renterblock.adapters.FeedsAdapter;
import com.smaat.renterblock.entity.ChatInputEntity;
import com.smaat.renterblock.entity.ProfileMyFeedsEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.model.CreateGroupChatResponse;
import com.smaat.renterblock.model.FriendsRecentListResponse;
import com.smaat.renterblock.model.ImageUplaodResponse;
import com.smaat.renterblock.model.UserProfileResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.ImageUtil;
import com.smaat.renterblock.utils.InterfaceBtnCallback;
import com.smaat.renterblock.utils.InterfaceEdtWithBtnCallback;
import com.smaat.renterblock.utils.InterfaceTwoBtnCallback;
import com.smaat.renterblock.utils.InterfaceTwoStrCallback;
import com.smaat.renterblock.utils.PathUtils;
import com.smaat.renterblock.utils.PreferenceUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

/**
 * Created by sys on 05-Sep-17.
 */

public class ProfileFragment extends BaseFragment implements InterfaceEdtWithBtnCallback {


    @BindView(R.id.scroll_view_lay)
    ScrollView mScrollLay;

    @BindView(R.id.profile_img)
    ImageView mProfileImg;

    @BindView(R.id.user_profile_name_txt)
    TextView mProfileNameTxt;

    @BindView(R.id.chat_option)
    LinearLayout mChatOptionLay;

    @BindView(R.id.ads_toggle)
    ImageView mAdOnOffImg;


    @BindView(R.id.address_txt)
    TextView mAddressTxt;

    @BindView(R.id.feeds_recycler_view)
    RecyclerView mFeedsRecyclerView;

    @BindView(R.id.change_pwd_txt)
    TextView mChangePwdTxt;

    @BindView(R.id.enhance_profile_lay)
    LinearLayout mEnhancedLay;

    @BindView(R.id.profile_add_friends_btn)
    ImageView mAddFriendImg;

    @BindView(R.id.more_about_me_txt)
    TextView mMoreAboutMeTxt;

    @BindView(R.id.my_feed_txt)
    TextView myFeedTxt;

    @BindView(R.id.chat_icon)
    ImageView mChatIconImg;

    @BindView(R.id.settings_icon)
    ImageView mSettingIconImg;

    @BindView(R.id.notification_lay)
    RelativeLayout mNotificationLay;

    @BindView(R.id.remove_ads_toggle)
    RelativeLayout mRemoveAdsLay;

    @BindView(R.id.profile_edt_img)
    ImageView mProfileEdtImg;

    @BindView(R.id.more_about_me_lay)
    RelativeLayout mMoreAboutMeLay;


    private ProfileMyFeedsEntity mProfileMyFeedsEntity;

    private ArrayList<ProfileMyFeedsEntity> mMyFeedsList = new ArrayList<>();

    private boolean isAdBool = false;

    private String mUserID = "", mFriendUserID = "", mUserNameStr = "", mBlockTxtStr = "";
    private Uri mFileUri;

    String mBlockTxtStrArrCheck[];

    private UserProfileResponse mUserProfileResponse;

    public static int REQUEST_CAMERA = 999;
    public static int REQUEST_GALLERY = 888;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_profile_screen, container, false);
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


    /*Fragment manual onResume*/
    @Override
    public void onFragmentResume() {
        super.onFragmentResume();
         /* If the value of visibleInt is zero,  the view will set gone. Or if the value of visibleInt is one,  the view will set visible. Or else, the view will set gone*/
        if (getActivity() != null) {

            AppConstants.TAG = this.getClass().getSimpleName();

            /*Slide menu action*/
            ((HomeScreen) getActivity()).setDrawerAction(AppConstants.PROFILE_CURRENT_BACK_FRAGMENT instanceof ProfileFragment);

            /*set header first  img*/
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(1);
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.mipmap.app_icon, 2);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 0);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.mipmap.app_icon, 0, "");

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.send), 0);

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.profile), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.feedback), 0);

            initView();

        }
    }

    private void initView() {
        if (getActivity() != null) {
            APIRequestHandler.getInstance().getProfileDetails(this, AppConstants.PROFILE_ID, "0", "100");
        }


    }


    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof UserProfileResponse) {
            final UserProfileResponse mRes = (UserProfileResponse) resObj;
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setData(mRes);
                        AppConstants.UserProfileResponse = mRes;
                    }
                });
            } else {
                DialogManager.getInstance().hideProgress();
            }
        }
        if (resObj instanceof ImageUplaodResponse) {
            ImageUplaodResponse mRes = (ImageUplaodResponse) resObj;
            DialogManager.getInstance().showAlertPopup(getActivity(), mRes.getMsg(), new InterfaceBtnCallback() {
                @Override
                public void onPositiveClick() {

                }
            });
        }
        if (resObj instanceof CommonResponse) {
            CommonResponse mRes = (CommonResponse) resObj;
            DialogManager.getInstance().showAlertPopup(getActivity(), mRes.getMsg(), new InterfaceBtnCallback() {
                @Override
                public void onPositiveClick() {

                }
            });
        }
        if (resObj instanceof FriendsRecentListResponse) {
            FriendsRecentListResponse mRes = (FriendsRecentListResponse) resObj;
            DialogManager.getInstance().showAlertPopup(getActivity(), mRes.getMsg(), new InterfaceBtnCallback() {
                @Override
                public void onPositiveClick() {

                }
            });
        }

        if (resObj instanceof CommonResponse) {
            CommonResponse mRes = (CommonResponse) resObj;
            DialogManager.getInstance().showAlertPopup(getContext(), mRes.getMsg(), new InterfaceBtnCallback() {
                @Override
                public void onPositiveClick() {

                }
            });
        }

        if (resObj instanceof CreateGroupChatResponse) {
            CreateGroupChatResponse mResponse = (CreateGroupChatResponse) resObj;
            if (mResponse.getError_code().equals(AppConstants.SUCCESS_CODE)) {
                AppConstants.CHAT_INPUT_ENTITY = new ChatInputEntity();
                AppConstants.CHAT_INPUT_ENTITY.setUser_id(PreferenceUtil.getUserID(getActivity()));
                AppConstants.CHAT_INPUT_ENTITY.setFriend_id(mResponse.getFriend_id());
                AppConstants.CHAT_INPUT_ENTITY.setSchedule_id(mResponse.getResult());
                ((HomeScreen) getActivity()).addFragment(new ChatFragment());
            }
        }

    }

    private void setData(UserProfileResponse mRes) {

        if (mRes.getError_code().equals(AppConstants.SUCCESS_CODE)) {
            mUserID = mRes.getResult().getUser().get(0).getUser_id();
            mUserNameStr = mRes.getResult().getUser().get(0).getUser_name();
            mUserProfileResponse = mRes;

            mChangePwdTxt.setVisibility(mRes.getResult().getUser().get(0).getUser_id().equalsIgnoreCase(PreferenceUtil.getUserID(getActivity())) &&
                    mRes.getResult().getUser().get(0).getUser_type().equalsIgnoreCase(AppConstants.EMAIL) ? View.VISIBLE : View.GONE);

            mEnhancedLay.setVisibility(mRes.getResult().getUser().get(0).getUser_id().equalsIgnoreCase(PreferenceUtil.getUserID(getActivity())) &&
                    !mRes.getResult().getUser().get(0).getEnhanced_profile().equalsIgnoreCase(AppConstants.SUCCESS_CODE) ? View.VISIBLE : View.GONE);

            mBlockTxtStrArrCheck = mRes.getResult().getAccept().split(",");
            if (mBlockTxtStrArrCheck.length >= 2) {
                if (mBlockTxtStrArrCheck[1].equals(mRes.getResult().getUser().get(0).getUser_id())) {
                    mBlockTxtStr = "Un Block";
                } else {
                    mBlockTxtStr = "Block";
                }
            } else {
                mBlockTxtStr = "Block";
            }
//            mAddFriendImg.setVisibility(mRes.getResult().getIs_friend().equalsIgnoreCase(AppConstants.SUCCESS_CODE)
//                    || mRes.getResult().getIs_friend().isEmpty() ? View.GONE : View.VISIBLE);
//            mAddFriendImg.setVisibility(mUserID.equalsIgnoreCase(PreferenceUtil.getUserID(getActivity())) ? View.GONE : View.VISIBLE);
//            mChatOptionLay.setVisibility(mRes.getResult().getUser().get(0).getRb_user().equalsIgnoreCase(AppConstants.FAILURE_CODE) || mRes.getResult().getUser().get(0).getRb_user().isEmpty() || mRes.getResult().getUser().get(0).getUser_id().equalsIgnoreCase(PreferenceUtil.getUserID(getActivity())) ? View.GONE : View.VISIBLE);

            mProfileNameTxt.setText(mRes.getResult().getUser().get(0).getUser_name());
            mNotificationLay.setVisibility(AppConstants.PROFILE_ID.equalsIgnoreCase(PreferenceUtil.getUserID(getActivity())) ? View.VISIBLE : View.GONE);
            mRemoveAdsLay.setVisibility(AppConstants.PROFILE_ID.equalsIgnoreCase(PreferenceUtil.getUserID(getActivity())) ? View.VISIBLE : View.GONE);

            mAdOnOffImg.setImageResource(PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.REMOVE_ADS_TOGGLE) ? R.drawable.toggle_on : R.drawable.toggle_off);


            if (mRes.getResult().getUser().get(0).getUser_id().equalsIgnoreCase(PreferenceUtil.getUserID(getActivity()))) {
                mProfileEdtImg.setVisibility(View.VISIBLE);
                mChatIconImg.setVisibility(View.GONE);
                mAddFriendImg.setVisibility(View.GONE);
                mSettingIconImg.setVisibility(View.GONE);
            } else {
                mProfileEdtImg.setVisibility(View.GONE);
                if (mRes.getResult().getUser().get(0).getRb_user().equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
                    mSettingIconImg.setVisibility(View.VISIBLE);
                    mChatIconImg.setVisibility(View.GONE);
                    mAddFriendImg.setVisibility(View.VISIBLE);
                } else if (mRes.getResult().getUser().get(0).getRb_user().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                    mSettingIconImg.setVisibility(View.VISIBLE);
                    mChatIconImg.setVisibility(View.VISIBLE);
                    mAddFriendImg.setVisibility(View.GONE);
                }
            }

            if (mRes.getResult().getUser().get(0).getUser_id().equalsIgnoreCase(PreferenceUtil.getUserID(getContext()))) {
                mMoreAboutMeLay.setVisibility(View.VISIBLE);
            } else {
                mMoreAboutMeLay.setVisibility(View.GONE);
            }

            if (!mRes.getResult().getUser().get(0).getUser_id().equalsIgnoreCase(PreferenceUtil.getUserID(getActivity()))) {
                mMoreAboutMeTxt.setText("More About " + mRes.getResult().getUser().get(0).getUser_name());
                myFeedTxt.setText(mRes.getResult().getUser().get(0).getUser_name() + " Feed");
            }
            if (mRes.getResult().getUser().get(0).getUser_pic().isEmpty()) {
                mProfileImg.setImageResource(R.drawable.default_profile_icon);
            } else {
                try {
                    Glide.with(getActivity())
                            .load(mRes.getResult().getUser().get(0).getUser_pic()).error(R.drawable.default_profile_icon)
                            .into(mProfileImg);
                } catch (Exception ex) {
                    mProfileImg.setImageResource(R.drawable.default_profile_icon);
                    Log.e(AppConstants.TAG, ex.getMessage());
                }
            }

//            mChatIconImg.setVisibility(mRes.getResult().getUser().get(0).getRb_user().isEmpty()
//                    || mRes.getResult().getUser().get(0).getRb_user().equalsIgnoreCase(AppConstants.FAILURE_CODE) ? View.GONE : View.VISIBLE);
//            mChatIconImg.setVisibility(View.GONE);

            mMyFeedsList.clear();
            for (int i = 0; i < mRes.getResult().getMessageboard().size(); i++) {
                mProfileMyFeedsEntity = new ProfileMyFeedsEntity();
                mProfileMyFeedsEntity.setPost_id(mRes.getResult().getMessageboard().get(i).getPost_id());
                mProfileMyFeedsEntity.setUser_id(mRes.getResult().getMessageboard().get(i).getUser_id());
                mProfileMyFeedsEntity.setProperty_id(mRes.getResult().getMessageboard().get(i).getProperty_id());
                mProfileMyFeedsEntity.setMessage(mRes.getResult().getMessageboard().get(i).getMessage());
                mProfileMyFeedsEntity.setDatetime(mRes.getResult().getMessageboard().get(i).getDate_time());
                mProfileMyFeedsEntity.setUser_name(mRes.getResult().getMessageboard().get(i).getUser_name());
                mProfileMyFeedsEntity
                        .setUser_profile_image(mRes.getResult().getMessageboard().get(i).getUser_profileImage());
                mProfileMyFeedsEntity.setUser_rating(mRes.getResult().getMessageboard().get(i).getUser_avg_rating());
                mProfileMyFeedsEntity.setFriends_count(mRes.getResult().getMessageboard().get(i).getFriends_count());
                mProfileMyFeedsEntity.setReviews_count(mRes.getResult().getMessageboard().get(i).getReviews_count());
                mProfileMyFeedsEntity.setPhotos_Count(mRes.getResult().getMessageboard().get(i).getPhotos_count());
                mProfileMyFeedsEntity.setAddress(mRes.getResult().getMessageboard().get(i).getAddress());
                mProfileMyFeedsEntity.setProperty_name(mRes.getResult().getMessageboard().get(i).getProperty_name());
                mProfileMyFeedsEntity.setComments(mRes.getResult().getMessageboard().get(i).getMessage());

                mMyFeedsList.add(mProfileMyFeedsEntity);
            }

            for (int i = 0; i < mRes.getResult().getReviews().size(); i++) {
                mProfileMyFeedsEntity = new ProfileMyFeedsEntity();
                mProfileMyFeedsEntity.setProperty_review_id(mRes.getResult().getReviews().get(i).getProperty_review_id());
                mProfileMyFeedsEntity.setProperty_id(mRes.getResult().getReviews().get(i).getProperty_id());
                mProfileMyFeedsEntity.setReview_user_id(mRes.getResult().getReviews().get(i).getReview_user_id());
                mProfileMyFeedsEntity.setComments(mRes.getResult().getReviews().get(i).getComments());
                mProfileMyFeedsEntity.setRating(mRes.getResult().getReviews().get(i).getRating());
                mProfileMyFeedsEntity.setDatetime(mRes.getResult().getReviews().get(i).getDate_time());
                mProfileMyFeedsEntity.setProperty_name(mRes.getResult().getReviews().get(i).getProperty_name());
                mProfileMyFeedsEntity.setFriends_count(mRes.getResult().getFriendscount());
                mProfileMyFeedsEntity.setReviews_count(mRes.getResult().getReviewscount());
                mProfileMyFeedsEntity.setPhotos_Count(mRes.getResult().getPhotocount());
                mProfileMyFeedsEntity.setRating(mRes.getResult().getRating());
                mProfileMyFeedsEntity.setUser_name(mRes.getResult().getUser().get(0).getUser_name());
                mProfileMyFeedsEntity.setUser_profile_image(mRes.getResult().getUser().get(0).getUser_pic());
                mProfileMyFeedsEntity.setAddress(mRes.getResult().getReviews().get(i).getAddress());
                mProfileMyFeedsEntity.setDescription(mRes.getResult().getReviews().get(i).getDescription());
                mMyFeedsList.add(mProfileMyFeedsEntity);
            }
            for (int i = 0; i < mRes.getResult().getPhotoandvideo().size(); i++) {
                mProfileMyFeedsEntity = new ProfileMyFeedsEntity();
                mProfileMyFeedsEntity.setUser_id(mRes.getResult().getPhotoandvideo().get(i).getUser_id());
                mProfileMyFeedsEntity.setProperty_id(mRes.getResult().getPhotoandvideo().get(i).getProperty_id());
                mProfileMyFeedsEntity.setFile(mRes.getResult().getPhotoandvideo().get(i).getFile());
                mProfileMyFeedsEntity.setFile_type(mRes.getResult().getPhotoandvideo().get(i).getFile_type());
                mProfileMyFeedsEntity.setPicture_id(mRes.getResult().getPhotoandvideo().get(i).getPicture_id());
                mProfileMyFeedsEntity.setDatetime(mRes.getResult().getPhotoandvideo().get(i).getDatetime());
                mProfileMyFeedsEntity.setProperty_name(mRes.getResult().getPhotoandvideo().get(i).getProperty_name());
                mProfileMyFeedsEntity.setFriends_count(mRes.getResult().getFriendscount());
                mProfileMyFeedsEntity.setReviews_count(mRes.getResult().getReviewscount());
                mProfileMyFeedsEntity.setPhotos_Count(mRes.getResult().getPhotocount());
                mProfileMyFeedsEntity.setRating((mRes.getResult().getRating()));
                mProfileMyFeedsEntity.setUser_name(mRes.getResult().getUser().get(0).getUser_name());
                mProfileMyFeedsEntity.setUser_profile_image(mRes.getResult().getUser().get(0).getUser_pic());
                mProfileMyFeedsEntity.setAddress(mRes.getResult().getPhotoandvideo().get(i).getAddress());
                mProfileMyFeedsEntity.setDescription(mRes.getResult().getPhotoandvideo().get(i).getDescription());

                mMyFeedsList.add(mProfileMyFeedsEntity);

            }
            Collections.sort(mMyFeedsList, ProfileMyFeedsEntity.DATE_SORT);
            FeedsAdapter mPropertyAdapter = new FeedsAdapter(getActivity(), mMyFeedsList);
            mFeedsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mFeedsRecyclerView.setNestedScrollingEnabled(false);
            mFeedsRecyclerView.setAdapter(mPropertyAdapter);


        }

        DialogManager.getInstance().hideProgress();

    }

    /* Ask for permission on Location access*/
    private boolean checkPermission() {
        if (getActivity() != null) {
            boolean addPermission = true;
            List<String> listPermissionsNeeded = new ArrayList<>();
            if (android.os.Build.VERSION.SDK_INT >= 23) {
                int cameraPermission = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA);
                int readStoragePermission = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
                int storagePermission = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(Manifest.permission.CAMERA);
                }
                if (readStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                if (storagePermission != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
            if (!listPermissionsNeeded.isEmpty()) {
                addPermission = askAccessPermission(listPermissionsNeeded, 1, new InterfaceTwoBtnCallback() {
                    @Override
                    public void onNegativeClick() {
                        DialogManager.getInstance().showImageUploadPopup(getActivity(), getString(R.string.select_photo), getString(R.string.take_photo), getString(R.string.choose_exisiting_photo), new InterfaceTwoBtnCallback() {
                            @Override
                            public void onNegativeClick() {
                                captureImage();
                            }

                            @Override
                            public void onPositiveClick() {
                                galleryImage();
                            }
                        });
                    }


                    @Override
                    public void onPositiveClick() {

                    }


                });
            }

            return addPermission;
        } else {
            return false;
        }
    }


    @OnClick({R.id.profile_img, R.id.user_name_update_lay,
            R.id.settings_icon, R.id.chat_icon, R.id.reviews_lay,
            R.id.profile_add_friends_btn, R.id.notification_lay, R.id.enhance_profile_lay, R.id.ads_toggle, R.id.change_pwd_txt, R.id.photos_videos_lay, R.id.more_about_me_lay})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.profile_img:
                if (checkPermission()) {
                    DialogManager.getInstance().showImageUploadPopup(getActivity(), getString(R.string.select_photo), getString(R.string.take_photo), getString(R.string.choose_exisiting_photo), new InterfaceTwoBtnCallback() {
                        @Override
                        public void onNegativeClick() {
                            captureImage();
                        }

                        @Override
                        public void onPositiveClick() {
                            galleryImage();
                        }
                    });
                }
                break;
            case R.id.settings_icon:
                DialogManager.getInstance().showPopupBlockUser(getContext(), mBlockTxtStr, new InterfaceTwoBtnCallback() {
                    @Override
                    public void onNegativeClick() {
                        final Intent emailInt = new Intent(Intent.ACTION_SENDTO);
                        emailInt.setData(Uri.parse("mailto:" + "admin@rentersblock.com"));
                        emailInt.putExtra(Intent.EXTRA_SUBJECT, "Report on " + mUserNameStr);
                        emailInt.putExtra(Intent.EXTRA_TEXT, "From rentersblock");
                        startActivity(emailInt);
                    }

                    @Override
                    public void onPositiveClick() {
                        if (mUserProfileResponse.getResult().getIs_friend() != null && mUserProfileResponse.getResult().getIs_friend().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                            String mAccept[] = mUserProfileResponse.getResult().getAccept().split(",");
                            if (mAccept.length >= 2) {
                                if (!mAccept[1].equalsIgnoreCase(mUserProfileResponse.getResult().getUser().get(0).getUser_id())) {
                                    DialogManager.getInstance().showAlertPopup(getContext(), "You are not a friend with" + mUserProfileResponse.getResult().getUser().get(0).getUser_name(), new InterfaceBtnCallback() {
                                        @Override
                                        public void onPositiveClick() {

                                        }
                                    });
                                } else {
                                    friendBlockUnBlockRequest("unblock");
                                }
                            } else {
                                if (mBlockTxtStr.equalsIgnoreCase("Un Block")) {
                                    friendBlockUnBlockRequest("unblock");
                                } else {
                                    friendBlockUnBlockRequest("block");
                                }
                            }
                        } else {
                            if (mBlockTxtStr.equalsIgnoreCase("Un Block")) {
                                friendBlockUnBlockRequest("unblock");
                            } else {
                                DialogManager.getInstance().showAlertPopup(getContext(), "You are not a friend with" + mUserProfileResponse.getResult().getUser().get(0).getUser_name(), new InterfaceBtnCallback() {
                                    @Override
                                    public void onPositiveClick() {

                                    }
                                });
                            }
                        }


                    }
                });
                break;
            case R.id.chat_icon:
                callChatAPI(mUserProfileResponse.getResult().getUser().get(0).getUser_id(),
                        mUserProfileResponse.getResult().getUser().get(0).getUser_name());
                break;
            case R.id.profile_add_friends_btn:
                sendFriendRequest();
                break;
            case R.id.notification_lay:
                AppConstants.NOTIFICATION_CURRENT_BACK_FRAGMENT = new ProfileFragment();
                ((HomeScreen) getActivity()).addFragment(new NotificationFragment());
                break;
            case R.id.enhance_profile_lay:
                break;
            case R.id.ads_toggle:
                PreferenceUtil.storeBoolPreferenceValue(getActivity(), AppConstants.REMOVE_ADS_TOGGLE, !PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.REMOVE_ADS_TOGGLE));
                mAdOnOffImg.setImageResource(PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.REMOVE_ADS_TOGGLE) ? R.drawable.toggle_on : R.drawable.toggle_off);
                break;
            case R.id.change_pwd_txt:
                DialogManager.getInstance().changePasswordDialog(getActivity(), new InterfaceTwoStrCallback() {
                    @Override
                    public void onPositiveClick(String oldPasswordStr, String changedPasswordStr) {
                        //isChangePwdResBool = true;
                        APIRequestHandler.getInstance().changePassword(ProfileFragment.this, changedPasswordStr);
                    }
                });
                break;
            case R.id.photos_videos_lay:
                if (getActivity() != null) {
                    AppConstants.FEEDS_LIST = mMyFeedsList;
                    ((HomeScreen) getActivity()).addFragment(new FeedsPhotosAndVideosFragment());
                }
                break;
            case R.id.user_name_update_lay:
                if (mUserID.equalsIgnoreCase(PreferenceUtil.getUserID(getActivity()))) {
                    //isChangePwdResBool = false;
                    DialogManager.getInstance().updateUsernameDialog(getActivity(), mProfileNameTxt.getText().toString(), this);
                }
                break;
            case R.id.reviews_lay:
                if (getActivity() != null) {
                    AppConstants.REVIEW_LIST_CURRENT_BACK_FRAGMENT = new ProfileFragment();
                    ((HomeScreen) getActivity()).addFragment(new ReviewsListFragment());
                }
                break;
            case R.id.more_about_me_lay:
                if (getActivity() != null) {
                    //AppConstants.PROFILE_MORE_ABOUT_FRAGMENT = new ProfileMoreAboutMeFragment();
                    ((HomeScreen) getActivity()).addFragment(new ProfileMoreAboutMeFragment());
                }
                break;
        }
    }


    public void callChatAPI(String mFriedUserId, String mFriendName) {
        APIRequestHandler.getInstance().getChatID(mFriedUserId, mFriendName, ProfileFragment.this);

    }

    /*open camera Image*/
    private void captureImage() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mFileUri = (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) ? FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", ImageUtil.getOutputMediaFile(MEDIA_TYPE_IMAGE)) :
                Uri.fromFile(ImageUtil.getOutputMediaFile(MEDIA_TYPE_IMAGE));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);


        // start the image capture Intent
        getActivity().startActivityForResult(intent, REQUEST_CAMERA);

    }

    /*open gallery Image*/
    private void galleryImage() {
        Intent j = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        getActivity().startActivityForResult(j, REQUEST_GALLERY);
    }


    @Override
    public void onFirstEdtBtnClick(String firstEdtStr) {
        if (getActivity() != null) {
            if (!firstEdtStr.isEmpty()) {
                mProfileNameTxt.setText(firstEdtStr);
                APIRequestHandler.getInstance().updateUserProfileName(this, firstEdtStr);
            }else{
                DialogManager.getInstance().showToast(getContext(),getString(R.string.please_enter_name));
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("mFileUri", mFileUri);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mFileUri = savedInstanceState.getParcelable("mFileUri");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {

                String selectedImageCapturePath = mFileUri.getPath();
                try {
                    Glide.with(getActivity())
                            .load(mFileUri).error(R.drawable.default_profile_icon)
                            .into(mProfileImg);

                    callUploadImage(selectedImageCapturePath);

                } catch (Exception e) {
                    e.printStackTrace();
                    mProfileImg.setImageResource(R.drawable.default_profile_icon);
                }


            } else {
                if (resultCode == RESULT_CANCELED) {
                /*Cancelling the image capture process by the user*/
                    DialogManager.getInstance().showToast(getActivity(), getString(R.string.user_cancelled));
                } else {
               /*image capture getting failed due to certail technical issues*/
                    DialogManager.getInstance().showToast(getActivity(), getString(R.string.sorry_failed_img));
                }
            }

        } else if (requestCode == REQUEST_GALLERY) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                Uri selectedImagePath = data.getData();
                String selectedImageGalleryPath = PathUtils.getPath(getActivity(), selectedImagePath);

                try {

                    Glide.with(getActivity())
                            .load(selectedImagePath)
                            .into(mProfileImg);
                    callUploadImage(selectedImageGalleryPath);

                } catch (Exception e) {
                    e.printStackTrace();
                    mProfileImg.setImageResource(R.drawable.default_profile_icon);
                }

            } else {
                if (resultCode == RESULT_CANCELED) {
                /*Cancelling the image capture process by the user*/
                    DialogManager.getInstance().showToast(getActivity(), getString(R.string.user_cancelled));

                } else {
               /*image capture getting failed due to certail technical issues*/
                    DialogManager.getInstance().showToast(getActivity(), getString(R.string.sorry_failed_img));
                }
            }

        }


    }

    private void friendBlockUnBlockRequest(String BlockUnBlockStr) {
        if (mUserProfileResponse.getResult().getUser().get(0).getUser_id() != null) {
            if (BlockUnBlockStr.equalsIgnoreCase("block")) {
                APIRequestHandler.getInstance().friendBlockAPI(mUserProfileResponse.getResult().getUser().get(0).getUser_id(), ProfileFragment.this);
            } else {
                APIRequestHandler.getInstance().friendUnBlockAPI(mUserProfileResponse.getResult().getUser().get(0).getUser_id(), ProfileFragment.this);
            }
        }
    }

    private void sendFriendRequest() {
        APIRequestHandler.getInstance().sendFriendRequest(this, AppConstants.PROFILE_ID);
    }

    private void callUploadImage(String filepath) {

        APIRequestHandler.getInstance().uploadProfileImage(this, filepath);
    }
}
