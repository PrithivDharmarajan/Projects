package com.smaat.renterblock.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.smaat.renterblock.R;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.InterfaceTwoBtnCallback;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareThisAppFragment extends BaseFragment {

    private TwitterAuthClient mAuthClientTwitter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstances) {
        View rootView = inflater.inflate(R.layout.frag_share_this_app, container, false);
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
    public void onResume() {
        super.onResume();
        hideSoftKeyboard();
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
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.mipmap.app_icon, 2);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 0);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.mipmap.app_icon, 0,"");

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.app_name), 0);

            /*set header txt*/
            if (AppConstants.SHARE_THIS_PROFILE){
                ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.share_profile), 1);

            }else {
                ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.share_this_app), 1);

            }
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.app_name), 0);


        }
    }

    @OnClick({R.id.message_lay, R.id.mail_lay, R.id.twitter_lay, R.id.fb_lay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.message_lay:
                if (permissionsForMessageSending()) {
                    onMessageSend();
                }
                break;
            case R.id.mail_lay:
                onEmailSend();
                break;
            case R.id.twitter_lay:
                onTwitterSend();
                break;
            case R.id.fb_lay:
                onFacebookSend();
                break;
        }
    }


    private void onMessageSend() {
        if (getActivity() != null) {

            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.setData(Uri.parse("sms:" + ""));
            smsIntent.putExtra("sms_body",
                    AppConstants.SHARE_DETAILS.getSms_share());
            startActivity(smsIntent);
        }
    }

    private void onEmailSend() {
        if (getActivity() != null) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, AppConstants.SHARE_DETAILS.getEmail_share_text());
            emailIntent.putExtra(Intent.EXTRA_TEXT,"http://rentersblock.com/web_dev/index.php/property/share/"+AppConstants.SHARE_DETAILS.getProperty_id() );
            startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email)));
        }
    }

    private void onTwitterSend() {

        if (getActivity() != null) {

            mAuthClientTwitter = new TwitterAuthClient();

            //Check Twitter Auth User
            mAuthClientTwitter.authorize(getActivity(), new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> twitterSessionResult) {
                    //Twitter Authorized User

                    final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                            .getActiveSession();
                    final Intent intent = new ComposerActivity.Builder(getActivity())
                            .session(session)
                            .text(AppConstants.SHARE_DETAILS.getSms_share())
                            .createIntent();
                    startActivity(intent);

                }

                @Override
                public void failure(TwitterException e) {
                    //Twitter Authorization  Failed
                    TwitterCore.getInstance().getSessionManager().clearActiveSession();
                }
            });

        }
    }

    private void onFacebookSend() {
        if (getActivity() != null) {

            FacebookSdk.sdkInitialize(getActivity());
            ShareDialog shareDialog = new ShareDialog(this);
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentTitle(AppConstants.SHARE_DETAILS.getFacebook_share_title())
                    .setContentDescription(AppConstants.SHARE_DETAILS.getFacebook_share_description())
                    .setContentUrl(Uri.parse("http://rentersblock.com/web_dev/index.php/property/share/"+AppConstants.SHARE_DETAILS.getProperty_id()))
                    .build();
            shareDialog.show(content);
        }
    }

    /* Ask for permission on sending message access*/
    private boolean permissionsForMessageSending() {
        boolean addPermission = true;
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (getActivity() != null) {
                int permissionSendMessage = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS);

                if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
                }
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            addPermission = askAccessPermission(listPermissionsNeeded, 1, new InterfaceTwoBtnCallback() {
                @Override
                public void onPositiveClick() {
                    onMessageSend();
                }

                @Override
                public void onNegativeClick() {

                }
            });
        }

        return addPermission;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mAuthClientTwitter != null)
            mAuthClientTwitter.onActivityResult(requestCode, resultCode, data);
    }
}
