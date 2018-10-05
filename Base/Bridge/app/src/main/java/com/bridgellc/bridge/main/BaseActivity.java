package com.bridgellc.bridge.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.apiinterface.APICommonInterface;
import com.bridgellc.bridge.entity.HomeSingleItemEntity;
import com.bridgellc.bridge.model.CommonResponse;
import com.bridgellc.bridge.model.ItemDetailResponse;
import com.bridgellc.bridge.model.NotificationCountResponse;
import com.bridgellc.bridge.ui.ChatScreen;
import com.bridgellc.bridge.ui.DashboardScreen;
import com.bridgellc.bridge.ui.ForgotPwdScreen;
import com.bridgellc.bridge.ui.NegotiationChatRoom;
import com.bridgellc.bridge.ui.NotificationScreen;
import com.bridgellc.bridge.ui.PaymentHomeScreen;
import com.bridgellc.bridge.ui.PhOTPEmailScreen;
import com.bridgellc.bridge.ui.ProductDetailsScreen;
import com.bridgellc.bridge.ui.ReferalScreen;
import com.bridgellc.bridge.ui.RequestBiddingScreen;
import com.bridgellc.bridge.ui.ReviewScreen;
import com.bridgellc.bridge.ui.SettingsScreen;
import com.bridgellc.bridge.ui.SignInScreen;
import com.bridgellc.bridge.ui.SignUpScreen;
import com.bridgellc.bridge.ui.SplashScreen;
import com.bridgellc.bridge.ui.home.HomeScreenActivity;
import com.bridgellc.bridge.ui.home.SearchScreenActivity;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;
import com.bridgellc.bridge.utils.TypefaceSingleton;
import com.paypal.android.sdk.payments.PayPalService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    public static Activity mActivity;
    public ViewGroup mViewGroup;
    public RelativeLayout mHeadeLeftBtnLay, mHeadeRightBtnLay, mFooterOneLay, mFooterTwoLay, mFooterThreeLay;
    public LinearLayout mFooterLay;
    protected Button mFooterOneBtn, mFooterTwoBtn, mFooterThreeBtn;
    public TextView mHeaderTxt;
    public SlidingDrawer slidingDrawer;
    public Animation mAnimFade;
    public static Typeface mLightFont, mRegulartFont;
    private static Context mContext;
    private static int handleHgt, handleWit;
    public static Dialog mPushDialog;
    public static HomeSingleItemEntity mItemDetasRes;
    long timeInterval = 10000;
    public static TextView mHomeTxt, mSearchTxt, mProfileTxt, mNotificationTxt, mRefproTxt, mNotifyLableTxt, mSettingsTxt;
    public static String mNotifyID = "";
    public static boolean isMenuOpenBaseAct = false;
    private static ImageView mImgCloseBaseAct;

    public static boolean isIncBaseAct = false;
    public static Handler notifyHandler = new Handler();
    public static Handler mHandlerBaseAct = new Handler();
    public static Runnable mRunBaseAct;

    private static int _indexNotifyBaseAct = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mActivity = this;
        mContext = this;
        mLightFont = TypefaceSingleton.getTypeface().getLightFont(mActivity);
        mRegulartFont = TypefaceSingleton.getTypeface().getRegularFont(mActivity);
        mHandlerBaseAct = new Handler();

        handelerClear();
    }

    private void handelerClear() {
        if (mActivity instanceof SplashScreen || mActivity instanceof SignInScreen || mActivity instanceof ForgotPwdScreen || mActivity instanceof PhOTPEmailScreen || mActivity instanceof SignUpScreen || mActivity
                instanceof NegotiationChatRoom) {
            if (mHandlerBaseAct != null) {
                mHandlerBaseAct.removeCallbacks(mRunBaseAct);
            }
            if (notifyHandler != null) {
                notifyHandler.removeCallbacks(notifyCheckingService);
            }


        }
    }


    public void setupUI(View view) {

        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(mActivity);
                    return false;
                }
            });
        }
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View mInnerView = ((ViewGroup) view).getChildAt(i);
                setupUI(mInnerView);
            }
        }

//        setOnClickLisMenu();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mActivity = this;
        setOnClickLisMenu();

        BridgeApplication.activityResumed();

        if (mHandlerBaseAct != null) {
            mHandlerBaseAct.removeCallbacks(mRunBaseAct);
        }

        if (notifyHandler != null) {
            notifyHandler.removeCallbacks(notifyCheckingService);
        }
        notifyHandler.postDelayed(notifyCheckingService, 0);


        mActivity.sendBroadcast(new Intent("com.google.android.intent.action.GTALK_HEARTBEAT"));
        mActivity.sendBroadcast(new Intent("com.google.android.intent.action.MCS_HEARTBEAT"));

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mHandlerBaseAct != null) {
            mHandlerBaseAct.removeCallbacks(mRunBaseAct);
        }
        if (notifyHandler != null) {
            notifyHandler.removeCallbacks(notifyCheckingService);
        }
        BridgeApplication.activityStoped();
    }

    protected Runnable notifyCheckingService = new Runnable() {
        @Override
        public void run() {
            if (mActivity instanceof BaseActivity && mNotifyLableTxt != null) {
                if (GlobalMethods.isLoggedIn(mActivity) && mSearchTxt != null) {
                    getNotificationCountResponse(mActivity);
                    notifyHandler.postDelayed(this, timeInterval);
                }
            }
        }
    };

    public void setOnClickLisMenu() {

        slidingDrawer = (SlidingDrawer) findViewById(R.id.slidingDrawer);

        handleWit = (int) getResources().getDimension(R.dimen.size90);
        BaseFragmentActivity.headerText = getString(R.string.home_c);

        if (slidingDrawer != null) {

            final TextView tv = (TextView) findViewById(R.id.header_txt);
            mImgCloseBaseAct = (ImageView) findViewById(R.id.close_img);

            slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
                @Override
                public void onDrawerOpened() {
                    isMenuOpenBaseAct = true;
                    handleHgt = (int) getResources().getDimension(R.dimen.height110);
                    slidingDrawer.getHandle().setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, handleHgt));
                    slidingDrawer.getHandle().setBackgroundColor(Color.parseColor("#04384C"));
                    slidingDrawer.getHandle().setAlpha((float) 0.9);
                    if (tv != null) {
                        BaseFragmentActivity.headerText = tv.getText().toString().trim();
                        tv.setText(getString(R.string.menu));
                        if (mHandlerBaseAct != null) {
                            mHandlerBaseAct.removeCallbacks(mRunBaseAct);
                        }
                        mImgCloseBaseAct.setImageBitmap(BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.close_img));

//                        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.close_img);
//                        mImgCloseBaseAct.setImageBitmap(largeIcon);

                    }

                }
            });

            slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
                @Override
                public void onDrawerClosed() {
                    isMenuOpenBaseAct = false;
                    handleHgt = (int) getResources().getDimension(R.dimen.height110);
                    slidingDrawer.getHandle().setLayoutParams(new RelativeLayout.LayoutParams(handleWit, handleHgt));
                    slidingDrawer.getHandle().setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    if (tv != null) {
                        tv.setText(BaseFragmentActivity.headerText);
                    }
                    if (mNotifyLableTxt.getVisibility() == View.VISIBLE) {
                        NotifyImgLoad();
                    } else {
                        if (mHandlerBaseAct != null) {
                            mHandlerBaseAct.removeCallbacks(mRunBaseAct);
                        }
                        mImgCloseBaseAct.setImageBitmap(BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.down_arrow_darkb_img));


                    }

                }
            });


            mHomeTxt = (TextView) findViewById(R.id.home);
            mSearchTxt = (TextView) findViewById(R.id.search);
            mProfileTxt = (TextView) findViewById(R.id.profile);
            mNotificationTxt = (TextView) findViewById(R.id.notification);
            mNotifyLableTxt = (TextView) findViewById(R.id.notify_lable);
            mRefproTxt = (TextView) findViewById(R.id.refpro_txt);
            mSettingsTxt = (TextView) findViewById(R.id.settings);

            if (mHomeTxt != null) {
                mHomeTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        slidingDrawer.close();
                        if (!(mActivity instanceof HomeScreenActivity)) {
                            nextScreen(HomeScreenActivity.class, true);
                        }


                    }
                });
            }

            if (mSearchTxt != null) {
                mSearchTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        slidingDrawer.close();
                        if (!(mActivity instanceof SearchScreenActivity)) {
                            nextScreen(SearchScreenActivity.class, true);
                        }

                    }
                });
            }

            if (mImgCloseBaseAct != null) {
                mImgCloseBaseAct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (slidingDrawer.isOpened()) {
                            slidingDrawer.animateClose();
                        } else {
                            slidingDrawer.animateOpen();
                        }

                    }
                });
            }

            if (mProfileTxt != null) {
                mProfileTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        slidingDrawer.close();
                        if (!(mActivity instanceof DashboardScreen)) {
                            nextScreen(DashboardScreen.class, true);
                        }

                    }
                });
            }
            if (mNotificationTxt != null) {
                mNotificationTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        slidingDrawer.close();
                        if (!(mActivity instanceof NotificationScreen)) {

                            AppConstants.PAYPAL_NOTIFY = AppConstants.FAILURE_CODE;
                            nextScreen(NotificationScreen.class, true);
                        }

                    }
                });
            }
            if (mRefproTxt != null) {
                mRefproTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        slidingDrawer.close();
//                        GlobalMethods.shareTxt(mActivity);
                        if (!(mActivity instanceof ReferalScreen)) {
//                            nextScreen(ReferalScreen.class, false);

                            Intent referInt = new Intent(mActivity, ReferalScreen.class);
                            mActivity.startActivity(referInt);
                        }

                    }
                });
            }
            if (mSettingsTxt != null) {
                mSettingsTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        slidingDrawer.close();
                        if (!(mActivity instanceof SettingsScreen)) {
                            nextScreen(SettingsScreen.class, true);
                        }

                    }
                });
            }
        }
    }

    public void hideSoftKeyboard(Activity mActivity) {
        try {
            if (mActivity != null && !mActivity.isFinishing()) {
                InputMethodManager mInputMethodManager = (InputMethodManager) mActivity
                        .getSystemService(INPUT_METHOD_SERVICE);

                if (mActivity.getCurrentFocus() != null
                        && mActivity.getCurrentFocus().getWindowToken() != null) {
                    mInputMethodManager.hideSoftInputFromWindow(mActivity
                            .getCurrentFocus().getWindowToken(), 0);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Set Font
     *
     * @param mViewGroup
     * @param mTypeface
     */
    public void setFont(ViewGroup mViewGroup, Typeface mTypeface) {

        int mCount = mViewGroup.getChildCount();
        View mView;
        for (int i = 0; i < mCount; i++) {
            mView = mViewGroup.getChildAt(i);

            if (mView instanceof TextView || mView instanceof Button
                    || mView instanceof EditText/* etc. */) {
                ((TextView) mView).setTypeface(mTypeface);
            }
            if (mView instanceof ViewGroup) {
                setFont((ViewGroup) mView, mTypeface);
            }
        }

    }

    /**
     * Move to Next Screen
     *
     * @param clazz
     */

    public void nextScreen(Class<?> clazz, boolean finish) {
        Intent mIntent = new Intent(mActivity, clazz);
        if (clazz.getSimpleName().equalsIgnoreCase(AppConstants.HOME_SCREEN) || clazz
                .getSimpleName().equalsIgnoreCase(AppConstants.SEARCH_SCREEN) || clazz.getSimpleName
                ().equalsIgnoreCase(AppConstants.DASHBOARD_SCREEN) || clazz.getSimpleName()
                .equalsIgnoreCase(AppConstants.NOTIFICATION_SCREEN) || clazz.getSimpleName()
                .equalsIgnoreCase(AppConstants.SETTINGS_SCREEN)) {

            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
//            mIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        }

        hideSlidingDrawer();

        mActivity.startActivity(mIntent);
        mActivity.overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
        if (finish)
            mActivity.finish();

    }

    public void previousScreen(Class<?> clazz, boolean finish) {
        Intent mIntent = new Intent(mActivity, clazz);
        if (clazz.getSimpleName().equalsIgnoreCase(AppConstants.HOME_SCREEN) || clazz
                .getSimpleName().equalsIgnoreCase(AppConstants.SEARCH_SCREEN) || clazz.getSimpleName
                ().equalsIgnoreCase(AppConstants.DASHBOARD_SCREEN) || clazz.getSimpleName()
                .equalsIgnoreCase(AppConstants.NOTIFICATION_SCREEN) || clazz.getSimpleName()
                .equalsIgnoreCase(AppConstants.SETTINGS_SCREEN)) {
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
//            mIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        }

        hideSlidingDrawer();

        mActivity.startActivity(mIntent);
        mActivity.overridePendingTransition(R.anim.slide_out_right,
                R.anim.slide_in_left);


        if (finish)
            mActivity.finish();
    }

    public void finishScreen() {
        hideSlidingDrawer();

        mActivity.finish();
        mActivity.overridePendingTransition(R.anim.slide_out_right,
                R.anim.slide_in_left);

    }

    public void hideSlidingDrawer() {
        if (slidingDrawer != null && slidingDrawer.isOpened()) {
            slidingDrawer.animateClose();
        }
    }


    public static void displayNotifyDialog(final String mMessage) {
        JSONObject jObj;
        String ty = "";
        String itemId = "";

        try {
            jObj = new JSONObject(AppConstants.TYPE_OF_NOTIFICATION);
            ty = jObj.getString("type_of_notification");
            itemId = jObj.getString("typeId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mActivity instanceof NegotiationChatRoom && ty.equalsIgnoreCase("Negotiation") || mActivity instanceof NegotiationChatRoom && ty.equalsIgnoreCase("Negotiation Approve")) {

            if (NegotiationChatRoom.mHomeSingleItemEntity.getItem_id() != null && !NegotiationChatRoom.mHomeSingleItemEntity.getItem_id().equalsIgnoreCase("") && itemId.equalsIgnoreCase(NegotiationChatRoom.mHomeSingleItemEntity.getItem_id())) {
                return;
            }
        }
        if (mActivity instanceof ChatScreen || mActivity instanceof NegotiationChatRoom) {
            return;
        }

//        if (mActivity instanceof ChatScreen && ty.equalsIgnoreCase("chat")) {
//
//            if (AppConstants.CHAT_ITEM_ID != null && !AppConstants.CHAT_ITEM_ID.equalsIgnoreCase("") && itemId.equalsIgnoreCase(AppConstants.CHAT_ITEM_ID)) {
//                return;
//            }
//        }
        if (mActivity instanceof ReviewScreen && ty.equalsIgnoreCase("Rating")) {
            return;
        }
        if ((mActivity instanceof ProductDetailsScreen)) {
            if (ProductDetailsScreen.mHomeSingleItemEntity.getItem_id() != null && !ProductDetailsScreen.mHomeSingleItemEntity.getItem_id().equalsIgnoreCase("") && itemId.equalsIgnoreCase(ProductDetailsScreen.mHomeSingleItemEntity.getItem_id())) {
                return;
            }
        }


        if (mPushDialog != null && mPushDialog.isShowing()) {
            mPushDialog.dismiss();

            //            mPushDialog.cancel();
        }
        //mPushDialog = null;
        final String finalTy = ty;
        mActivity.runOnUiThread(new Runnable() {
            public void run() {

//                if (mPushDialog != null && mPushDialog.isShowing()) {
//                    mPushDialog.dismiss();
//
//                    //            mPushDialog.cancel();
//                }

                mPushDialog = new Dialog(mActivity);
                mPushDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mPushDialog.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                mPushDialog.setContentView(R.layout.popup_alert_with_bg);
                mPushDialog.getWindow().setGravity(Gravity.BOTTOM);
                mPushDialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(Color.TRANSPARENT));
                mPushDialog.setCancelable(false);
                mPushDialog.setCanceledOnTouchOutside(false);
//                mPushDialog = DialogManager.getDialog(mActivity, R.layout.popup_alert_with_bg);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = mPushDialog.getWindow();
                lp.copyFrom(window.getAttributes());
                //This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
                mPushDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;


                RelativeLayout mCloseLay = (RelativeLayout) mPushDialog.findViewById(R.id.close_img_lay);
                TextView mTitleTxt = (TextView) mPushDialog.findViewById(R.id.header_txt);
                TextView mMessageTxt = (TextView) mPushDialog
                        .findViewById(R.id.msg_txt);
                final Button mYesBtn = (Button) mPushDialog.findViewById(R.id.footer_one_btn);
                final Button mNoBtn = (Button) mPushDialog.findViewById(R.id.footer_two_btn);


                RelativeLayout mFooterOneLay = (RelativeLayout) mPushDialog.findViewById(R.id.footer_one_lay);
                RelativeLayout mFooterTwoLay = (RelativeLayout) mPushDialog.findViewById(R.id.footer_two_lay);

                mFooterOneLay.setVisibility(View.VISIBLE);
                if (finalTy.equalsIgnoreCase("Bid Reject") || finalTy.equalsIgnoreCase("Item Purchased") || finalTy.equalsIgnoreCase("Reject Offer")) {

                    mYesBtn.setText(mActivity.getString(R.string.ok));


                } else {
                    mFooterTwoLay.setVisibility(View.VISIBLE);
                    mYesBtn.setBackgroundColor(mActivity.getResources().getColor(R.color.blue_btn_bg));
                    mNoBtn.setBackgroundColor(mActivity.getResources().getColor(R.color.white));
                    mNoBtn.setTextColor(mActivity.getResources().getColor(R.color.blue_btn_bg));
                    mYesBtn.setText(mActivity.getString(R.string.view));
                    mNoBtn.setText(mActivity.getString(R.string.cancel));
                }
                mTitleTxt.setText(mActivity.getString(R.string.app_name));
                mMessageTxt.setText(mMessage);

                mTitleTxt.setTypeface(mRegulartFont);
                mMessageTxt.setTypeface(mLightFont);
                mYesBtn.setTypeface(mLightFont);
                mNoBtn.setTypeface(mLightFont);

                mYesBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mPushDialog.dismiss();
                        if (mYesBtn.getText().toString().equalsIgnoreCase(mActivity.getString(R.string.view))) {
                            String item_id = "";
                            String otheruser_id = "";
                            String payment_id = "";
                            String notification_id = "";
                            JSONObject json;
                            try {
                                json = new JSONObject(AppConstants.TYPE_OF_NOTIFICATION);
                                item_id = json.getString("typeId");
                                otheruser_id = json.getString("notification_from");
                                payment_id = json.getString("payment_id");
                                notification_id = json.getString("notification_id");
                                mNotifyID = notification_id;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            getItemDetailsResponse(item_id, otheruser_id, payment_id,notification_id, mActivity);
                            getItemDetailsResponse(item_id, otheruser_id, payment_id, "", mActivity);
//                        ((BaseActivity) mContext).movedPage();
                        }
                    }
                });
                mNoBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mPushDialog.dismiss();
                    }
                });
                mCloseLay.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mPushDialog.dismiss();
                    }
                });

                try {
                    mPushDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (mActivity instanceof BaseActivity) {
                        ((BaseActivity) mActivity).nextScreen(HomeScreenActivity.class, true);
                    } else if (mActivity instanceof BaseFragmentActivity) {
                        ((BaseFragmentActivity) mActivity).nextScreen(HomeScreenActivity.class, true);
                    }
                }
            }

        });
    }


    private static void movedPage() {
        JSONObject jsonObj;
        String type = "", item_id = "";
        try {
            jsonObj = new JSONObject(AppConstants.TYPE_OF_NOTIFICATION);
            type = jsonObj.getString("type_of_notification");
            item_id = jsonObj.getString("typeId");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mActivity, e.toString(), Toast.LENGTH_LONG).show();
        }

        if (mItemDetasRes.getComplete().equalsIgnoreCase(mActivity.getString(R.string.zero)) || type.equalsIgnoreCase("Bank")) {
            switch (type) {
                case "Negotiation":
                    ProductDetailsScreen.mFooterBtnCount = 1;
                    ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.negotiation);

                    String otherUserId = "";

                    if (mItemDetasRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        otherUserId = mItemDetasRes.getBuyer_id();
                    } else {
                        //I am Buyer
                        otherUserId = mItemDetasRes.getUser_id();

                    }

//                ProductDetailsScreen.mHomeSingleItemEntity = mItemDetasRes;

                    NegotiationChatRoom.mHomeSingleItemEntity = mItemDetasRes;
                    AppConstants.CHAT_BACK = AppConstants.FAILURE_CODE;
                    AppConstants.NEGO_FRIEND_ID = otherUserId;
                    AppConstants.NEGO_USER_ID = GlobalMethods.getUserID(mActivity);
                    AppConstants.NEGO_ITEM_ID = mItemDetasRes.getItem_id();
                    AppConstants.NEGO_ITEM_NOTI = mActivity.getString(R.string.one);
                    AppConstants.NEGO_ITEM_QTY = mItemDetasRes.getPurchase_quantity();

                    if (mItemDetasRes.getBid_id() != null && !mItemDetasRes.getBid_id().equalsIgnoreCase("") && !mItemDetasRes.getBid_id().equalsIgnoreCase(mActivity.getString(R.string.zero))) {
                        AppConstants.NEGO_BID_ID = mItemDetasRes.getBid_id();
                        NegotiationChatRoom.mHomeSingleItemEntity.setBuyer_id(mItemDetasRes.getUser_id());

                    } else {
                        AppConstants.NEGO_BID_ID = "";
                    }

                    AppConstants.FROME_NOTIFICATION = mActivity.getString(R.string.one);


                    if (mActivity instanceof BaseActivity) {
                        ((BaseActivity) mActivity).nextScreen(NegotiationChatRoom.class, true);
                    } else if (mActivity instanceof BaseFragmentActivity) {
                        ((BaseFragmentActivity) mActivity).nextScreen(NegotiationChatRoom.class, true);
                    }
                    break;
                case "Negotiation Approve":
                    if (mItemDetasRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyerApprovList(mItemDetasRes);
                    } else {
                        //I am Buyer
                        buyerOrderList(mItemDetasRes);

                    }
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.three);

                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetasRes;
                    if (mActivity instanceof BaseActivity) {
                        ((BaseActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);
                    } else if (mActivity instanceof BaseFragmentActivity) {
                        ((BaseFragmentActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);
                    }
                    break;
                case "Buy":
                    if (mItemDetasRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyerApprovList(mItemDetasRes);
                    } else {
                        //I am Buyer
                        buyerOrderList(mItemDetasRes);

                    }
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.three);
                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetasRes;
                    if (mActivity instanceof BaseActivity) {
                        ((BaseActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);
                    } else if (mActivity instanceof BaseFragmentActivity) {
                        ((BaseFragmentActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);
                    }

                    break;
                case "Receive Amount":
                    ProductDetailsScreen.mFooterBtnCount = 2;
                    ProductDetailsScreen.mFooterOneTxt = mContext.getString(R.string.chat);
                    ProductDetailsScreen.mFooterTwoTxt = mContext.getString(R.string.rating);

                    if (mItemDetasRes.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.one)) && mItemDetasRes.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.two))) {
                        ProductDetailsScreen.mFooterBtnCount = 1;
                        ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.rating);
                    }
                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetasRes;
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.three);
                    if (mActivity instanceof BaseActivity) {
                        ((BaseActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);
                    } else if (mActivity instanceof BaseFragmentActivity) {
                        ((BaseFragmentActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);
                    }
//                }
                    break;


                case "Receive Amount Goods":
                    if (mItemDetasRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyerApprovList(mItemDetasRes);
                    } else {
                        //I am Buyer
                        buyerOrderList(mItemDetasRes);

                    }
                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetasRes;
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.three);
                    if (mActivity instanceof BaseActivity) {
                        ((BaseActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);
                    } else if (mActivity instanceof BaseFragmentActivity) {
                        ((BaseFragmentActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);
                    }
                    break;
                case "Receive Amount Service":
                    if (mItemDetasRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyerApprovList(mItemDetasRes);
                    } else {
                        //I am Buyer
                        buyerOrderList(mItemDetasRes);

                    }
                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetasRes;
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.three);
                    if (mActivity instanceof BaseActivity) {
                        ((BaseActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);
                    } else if (mActivity instanceof BaseFragmentActivity) {
                        ((BaseFragmentActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);
                    }
                    break;
                case "Approve Preview":
                    if (mItemDetasRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyerApprovList(mItemDetasRes);
                    } else {
                        //I am Buyer
                        buyerOrderList(mItemDetasRes);

                    }
                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetasRes;
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.three);
                    if (mActivity instanceof BaseActivity) {
                        ((BaseActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);
                    } else if (mActivity instanceof BaseFragmentActivity) {
                        ((BaseFragmentActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);
                    }
                    break;
                case "Upload Preview":
                    if (mItemDetasRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyerApprovList(mItemDetasRes);
                    } else {
                        //I am Buyer
                        buyerOrderList(mItemDetasRes);

                    }
                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetasRes;
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.three);
                    if (mActivity instanceof BaseActivity) {
                        ((BaseActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);
                    } else if (mActivity instanceof BaseFragmentActivity) {
                        ((BaseFragmentActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);
                    }
                    break;
                case "Upload File":


                    ProductDetailsScreen.mFooterBtnCount = 2;
                    ProductDetailsScreen.mFooterOneTxt = mContext.getString(R.string.chat);
                    ProductDetailsScreen.mFooterTwoTxt = mContext.getString(R.string.rating);

                    if (mItemDetasRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller

                    } else {
                        //I am Buyer

                    }

                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetasRes;
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.three);
                    if (mItemDetasRes.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.one)) && mItemDetasRes.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.two))) {
                        ProductDetailsScreen.mFooterBtnCount = 1;
                        ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.rating);
                    }
                    if (mActivity instanceof BaseActivity) {
                        ((BaseActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);
                    } else if (mActivity instanceof BaseFragmentActivity) {
                        ((BaseFragmentActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);
                    }
                    break;
                case "Service Start":
                    if (mItemDetasRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyerApprovList(mItemDetasRes);
                    } else {
                        //I am Buyer
                        buyerOrderList(mItemDetasRes);

                    }

                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetasRes;
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.three);
                    if (mActivity instanceof BaseActivity) {
                        ((BaseActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);
                    } else if (mActivity instanceof BaseFragmentActivity) {
                        ((BaseFragmentActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);
                    }
                    break;
                case "Chat":
                    ProductDetailsScreen.mFooterBtnCount = 1;
                    ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.chat);
                    if (mItemDetasRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyerApprovList(mItemDetasRes);
                        AppConstants.CHAT_FRIEND_ID = mItemDetasRes.getBuyer_id();
                    } else {
//                    I am Buyer
                        buyerOrderList(mItemDetasRes);
                        AppConstants.CHAT_FRIEND_ID = mItemDetasRes.getUser_id();

                    }
                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetasRes;

                    AppConstants.CHAT_ITEM_ID = mItemDetasRes.getItem_id();
                    ChatScreen.isSend = true;

                    AppConstants.FROME_NOTIFICATION = mActivity.getString(R.string.one);
                    if (mActivity instanceof BaseActivity) {
                        ((BaseActivity) mActivity).nextScreen(ChatScreen.class, true);
                    } else if (mActivity instanceof BaseFragmentActivity) {
                        ((BaseFragmentActivity) mActivity).nextScreen(ChatScreen.class, true);
                    }
                    break;
                case "Rating":
                    if (mItemDetasRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                    } else {
                        //I am Buyer

                    }

                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetasRes;
                    AppConstants.FROME_NOTIFICATION = mActivity.getString(R.string.one);

                    ReviewScreen.mOtherUserId = GlobalMethods.getUserID(mActivity);
                    if (mActivity instanceof BaseActivity) {
                        ((BaseActivity) mActivity).nextScreen(ReviewScreen.class, true);
                    } else if (mActivity instanceof BaseFragmentActivity) {
                        ((BaseFragmentActivity) mActivity).nextScreen(ReviewScreen.class, true);
                    }
                    break;

                case "Bid Approve":
                    if (mItemDetasRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyerApprovList(mItemDetasRes);
                    } else {
                        //I am Buyer
                        buyerOrderList(mItemDetasRes);

                    }

                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetasRes;
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.three);
                    if (mActivity instanceof BaseActivity) {
                        ((BaseActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);
                    } else if (mActivity instanceof BaseFragmentActivity) {
                        ((BaseFragmentActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);
                    }

                    break;
                case "Bid":
                    if (mItemDetasRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyerApprovList(mItemDetasRes);
                    } else {
                        //I am Buyer
                        buyerOrderList(mItemDetasRes);

                    }

                    RequestBiddingScreen.mItemId = item_id;
                    AppConstants.FROME_NOTIFICATION = mActivity.getString(R.string.one);
                    if (mActivity instanceof BaseActivity) {
                        ((BaseActivity) mActivity).nextScreen(RequestBiddingScreen.class, true);
                    } else if (mActivity instanceof BaseFragmentActivity) {
                        ((BaseFragmentActivity) mActivity).nextScreen(RequestBiddingScreen.class, true);
                    }
                    break;
                case "Bank":
//                    if (mItemDetasRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
//                        //I am Seller
//                        sellBuyerApprovList(mItemDetasRes);
//                    } else {
//                        //I am Buyer
//                        buyerOrderList(mItemDetasRes);
//
//                    }

//                    RequestBiddingScreen.mItemId = item_id;
                    AppConstants.BANK_ACC_DET_BACK_SCREEN = mActivity.getString(R.string.eight);
                    if (mActivity instanceof BaseActivity) {
                        ((BaseActivity) mActivity).nextScreen(PaymentHomeScreen.class, true);
                    } else if (mActivity instanceof BaseFragmentActivity) {
                        ((BaseFragmentActivity) mActivity).nextScreen(PaymentHomeScreen.class, true);
                    }
                    break;
                case "Offer Negotiation Approve Paypal":
                case "Negotiation Approve Paypal":
                    AppConstants.PAYPAL_NOTIFY = AppConstants.SUCCESS_CODE;
                    NotificationScreen.mFromNotiItemDetailsRes = mItemDetasRes;
                    if (mActivity instanceof BaseActivity) {
                        ((BaseActivity) mActivity).nextScreen(NotificationScreen.class, true);
                    } else if (mActivity instanceof BaseFragmentActivity) {
                        ((BaseFragmentActivity) mActivity).nextScreen(NotificationScreen.class, true);
                    }
                    break;
                case "Refund":
                    if (mItemDetasRes.getUser_id().equalsIgnoreCase(GlobalMethods.getUserID(mActivity))) {
                        //I am Seller
                        sellBuyerApprovList(mItemDetasRes);
                    } else {
                        //I am Buyer
                        buyerOrderList(mItemDetasRes);

                    }


                    ProductDetailsScreen.mHomeSingleItemEntity = mItemDetasRes;
                    AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.three);


                    if (mActivity instanceof BaseActivity) {
                        ((BaseActivity) mActivity).nextScreen(NotificationScreen.class, true);
                    } else if (mActivity instanceof BaseFragmentActivity) {
                        ((BaseFragmentActivity) mActivity).nextScreen(NotificationScreen.class, true);
                    }
                    break;
            }
        } else {
            ProductDetailsScreen.mHomeSingleItemEntity = mItemDetasRes;

            ProductDetailsScreen.mFooterBtnCount = 2;
            ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.chat);
            ProductDetailsScreen.mFooterTwoTxt = mActivity.getString(R.string.rating);
            AppConstants.PRODUCT_DETAILS_BACK = mActivity.getString(R.string.three);

            if (mItemDetasRes.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.one)) && mItemDetasRes.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.two))) {
                ProductDetailsScreen.mFooterBtnCount = 1;
                ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.rating);
            }
            if (mActivity instanceof BaseActivity) {
                ((BaseActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);
            } else if (mActivity instanceof BaseFragmentActivity) {
                ((BaseFragmentActivity) mActivity).nextScreen(ProductDetailsScreen.class, true);
            }
        }

    }

    private static void sellBuyerApprovList(HomeSingleItemEntity homeSingleItemEntity) {
        ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.chat);
        ProductDetailsScreen.mFooterTwoTxt = mActivity.getString(R.string.finish);
        ProductDetailsScreen.mFooterBtnCount = 2;
        if (homeSingleItemEntity.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.one)) && (homeSingleItemEntity.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.two)))) {
            ProductDetailsScreen.mFooterTwoTxt = mContext.getString(R.string.rating);
        }
        if (homeSingleItemEntity.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.two))) {
            if (homeSingleItemEntity.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.one))) {

            } else {
                ProductDetailsScreen.mFooterTwoTxt = mActivity.getString(R.string.upload_txt);
            }
        }
    }

    private static void buyerOrderList(HomeSingleItemEntity buypost) {
        ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.chat);
        ProductDetailsScreen.mFooterTwoTxt = mActivity.getString(R.string.code);
        ProductDetailsScreen.mFooterThreeTxt = mActivity.getString(R.string.unsatis);
        ProductDetailsScreen.mFooterBtnCount = 3;


        if (buypost.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.one)) && buypost.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.two))) {

            ProductDetailsScreen.mFooterBtnCount = 2;
            ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.chat);
            ProductDetailsScreen.mFooterTwoTxt = mActivity.getString(R.string.rating);
            if (buypost.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.one)) && buypost.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.two))) {
                ProductDetailsScreen.mFooterBtnCount = 1;
                ProductDetailsScreen.mFooterOneTxt = mActivity.getString(R.string.rating);
            }
        }
        if (buypost.getItem_type().equalsIgnoreCase(mActivity.getString(R.string.two))) {
            if (buypost.getDelivery_type().equalsIgnoreCase(mActivity.getString(R.string.one))) {
                ProductDetailsScreen.mFooterBtnCount = 2;
                ProductDetailsScreen.mFooterTwoTxt = mActivity.getString(R.string.start);
            } else {

                ProductDetailsScreen.mFooterTwoTxt = mActivity.getString(R.string.preview);
                ProductDetailsScreen.mFooterThreeTxt = mActivity.getString(R.string.approve);
            }
        }
    }

    public void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = mActivity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        }
    }

    public void onRequestSuccess(Object responseObj) {
    }


    public void onRequestFailure(RetrofitError errorCode) {


        try {
            System.out.println("errorCode.getCause() --------" + errorCode.getCause().toString());
            System.out.println("errorCode.getCause() Msg--------" + errorCode.getCause().getMessage().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (errorCode.getCause() instanceof java.net.ConnectException || errorCode.getCause() instanceof java.net.UnknownHostException) {
            DialogManager.showBasicAlertDialog(mActivity, getString(R.string.no_internet), new DialogMangerOkCallback() {
                @Override
                public void onOkClick() {

                }
            });
        } else if (errorCode.getCause() instanceof java.net.SocketTimeoutException) {
            DialogManager.showBasicAlertDialog(mActivity,
                    getString(R.string.connect_time_out), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {

                        }
                    });

        } else if (errorCode.getCause() instanceof retrofit.converter.ConversionException) {
            DialogManager.showBasicAlertDialog(mActivity, getString(R.string.serv_con_exce), new DialogMangerOkCallback() {
                @Override
                public void onOkClick() {

                }
            });
        } else {

            DialogManager.showBasicAlertDialog(mActivity,
                    getString(R.string.serv_not_res), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {

                        }
                    });

        }


    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static void getItemDetailsResponse(String itemID, String buyerID, String paymentID, String notificationID, final Activity mActivity) {
        DialogManager.showProgress(mActivity);
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(
                AppConstants.BASE_URL).build();
        APICommonInterface mInterfaces = restAdapter
                .create(APICommonInterface.class);
        mInterfaces.getItemDetailsResponse(GlobalMethods.getUserID(mActivity), itemID, buyerID, paymentID, notificationID,
                new Callback<ItemDetailResponse>() {

                    @Override
                    public void success(ItemDetailResponse mHomeResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mHomeResponse != null) {
                            Object obj = mHomeResponse;
                            if (mHomeResponse.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                                mItemDetasRes = mHomeResponse.getResult();

                                getNotificationReadResponse(mNotifyID, mActivity);
                            } else {

                                DialogManager.showBasicAlertDialog(mContext, mHomeResponse.getMessage(), new DialogMangerOkCallback() {
                                    @Override
                                    public void onOkClick() {
                                        if (mActivity instanceof BaseActivity) {
                                            ((BaseActivity) mActivity).nextScreen(HomeScreenActivity.class, true);
                                        } else if (mActivity instanceof BaseFragmentActivity) {
                                            ((BaseFragmentActivity) mActivity).nextScreen(HomeScreenActivity.class, true);
                                        }
                                    }
                                });
                            }

                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        onPushNotificationReqFailure(retrofitError);
                    }
                });
    }


    private static void getNotificationReadResponse(String mNotifyId, final Activity
            mActivity) {
        DialogManager.showProgress(mActivity);
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(
                AppConstants.BASE_URL).build();
        APICommonInterface mInterfaces = restAdapter
                .create(APICommonInterface.class);

        mInterfaces.getNotificationReadResponse(GlobalMethods.getUserID(mActivity), mNotifyId,
                new Callback<CommonResponse>() {

                    @Override
                    public void success(CommonResponse mNotificationResponse,
                                        Response arg1) {
                        DialogManager.hideProgress(mActivity);
                        if (mNotificationResponse != null) {
                            Object obj = mNotificationResponse;
                            if (mNotificationResponse.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                                movedPage();
                            } else {

                                DialogManager.showBasicAlertDialog(mContext, mNotificationResponse.getMessage(), new DialogMangerOkCallback() {
                                    @Override
                                    public void onOkClick() {
                                        if (mActivity instanceof BaseActivity) {
                                            ((BaseActivity) mActivity).nextScreen(HomeScreenActivity.class, true);
                                        } else if (mActivity instanceof BaseFragmentActivity) {
                                            ((BaseFragmentActivity) mActivity).nextScreen(HomeScreenActivity.class, true);
                                        }
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        DialogManager.hideProgress(mActivity);
                        onPushNotificationReqFailure(retrofitError);
                    }
                });
    }

    private static void getNotificationCountResponse(final Activity mActivity) {
//        DialogManager.showProgress(mActivity);
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(
                AppConstants.BASE_URL).build();
        APICommonInterface mInterfaces = restAdapter
                .create(APICommonInterface.class);
        mInterfaces.getNotificationCountResponse(GlobalMethods.getUserID(mActivity),
                new Callback<NotificationCountResponse>() {

                    @Override
                    public void success(NotificationCountResponse mNotifyResponse,
                                        Response arg1) {
//                        DialogManager.hideProgress(mActivity);
                        if (mNotifyResponse != null) {

                            if (mNotifyResponse.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                                GlobalMethods.storeValuetoPreference(mActivity,
                                        GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.ISUSERVERIFIED,
                                        mNotifyResponse.getResult().getUser_verify()
                                                .equalsIgnoreCase(mActivity.getString(R.string
                                                        .two)));

                                if (mNotifyResponse.getResult().getUser_status()
                                        != null && mNotifyResponse.getResult().getUser_status()
                                        .equalsIgnoreCase
                                                (mActivity.getString(R.string.two))) {

                                    GlobalMethods.storeValuetoPreference(mActivity,
                                            GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.TUTORIAL_SEEN,
                                            false);
                                    GlobalMethods.storeValuetoPreference(mActivity,
                                            GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.ISUSERVERIFIED,
                                            false);
                                    PayPalService.clearAllUserData(mActivity);
                                    GlobalMethods.userDetails(false, "", "", "", "", "", "",
                                            "", "", "", "", "", "", "", "", "", "", "", mActivity);
                                    Toast.makeText(mActivity, mActivity.getString(R.string.block_msg), Toast
                                            .LENGTH_LONG).show();
                                    GlobalMethods.setBadge(mActivity, Integer.valueOf
                                            (AppConstants.FAILURE_CODE));
                                    if (mActivity instanceof BaseActivity) {

                                        ((BaseActivity) mActivity).nextScreen(SignInScreen
                                                .class, true);
                                    } else if (mActivity instanceof BaseFragmentActivity) {
                                        ((BaseFragmentActivity) mActivity).nextScreen(SignInScreen.class, true);
                                    }


                                } else {
                                    if (mNotifyResponse.getResult().getCount() != null && !mNotifyResponse.getResult().getCount().equalsIgnoreCase("") && !mNotifyResponse.getResult().getCount().equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
                                        String notifiCount = mNotifyResponse.getResult().getCount();

                                        if (mActivity instanceof BaseActivity && mNotifyLableTxt != null) {
                                            mNotifyLableTxt.setVisibility(View.VISIBLE);
                                            mNotifyLableTxt.setText(notifiCount);

                                            GlobalMethods.setBadge(mActivity, Integer.valueOf
                                                    (notifiCount));

                                            if (!isMenuOpenBaseAct) {
                                                NotifyImgLoad();

                                            } else {

                                                mImgCloseBaseAct.setImageBitmap(BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.close_img));
                                            }
                                        }

                                    } else {
                                        if (mActivity instanceof BaseActivity && mNotifyLableTxt != null) {
                                            mNotifyLableTxt.setVisibility(View.GONE);
                                            GlobalMethods.setBadge(mActivity, Integer.valueOf
                                                    (AppConstants.FAILURE_CODE));
                                            if (!isMenuOpenBaseAct) {
                                                mImgCloseBaseAct.setImageBitmap(BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.down_arrow_darkb_img));
                                            } else {
                                                mImgCloseBaseAct.setImageBitmap(BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.close_img));
                                            }
                                        }

                                    }

                                }
                            }

                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                    }
                }

        );
    }


    public static void onPushNotificationReqFailure(RetrofitError errorCode) {

        if (errorCode.getCause() instanceof ConnectException || errorCode.getCause() instanceof java.net.UnknownHostException) {
            DialogManager.showBasicAlertDialog(mActivity,
                    mActivity.getString(R.string.no_internet), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {
                            if (mActivity instanceof BaseActivity) {
                                ((BaseActivity) mActivity).nextScreen(HomeScreenActivity.class, true);
                            } else if (mActivity instanceof BaseFragmentActivity) {
                                ((BaseFragmentActivity) mActivity).nextScreen(HomeScreenActivity.class, true);
                            }
                        }
                    });
        } else if (errorCode.getCause() instanceof java.net.SocketTimeoutException) {
            DialogManager.showBasicAlertDialog(mActivity,
                    mActivity.getString(R.string.connect_time_out), new DialogMangerOkCallback
                            () {
                        @Override
                        public void onOkClick() {
                            if (mActivity instanceof BaseActivity) {
                                ((BaseActivity) mActivity).nextScreen(HomeScreenActivity.class, true);
                            } else if (mActivity instanceof BaseFragmentActivity) {
                                ((BaseFragmentActivity) mActivity).nextScreen(HomeScreenActivity.class, true);
                            }
                        }
                    });

        } else if (errorCode.getCause() instanceof retrofit.converter.ConversionException) {
            DialogManager.showBasicAlertDialog(mActivity,
                    mActivity.getString(R.string.serv_con_exce), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {

                        }
                    });
        } else {

            DialogManager.showBasicAlertDialog(mActivity,
                    mActivity.getString(R.string.serv_not_res), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {
                            if (mActivity instanceof BaseActivity) {
                                ((BaseActivity) mActivity).nextScreen(HomeScreenActivity.class, true);
                            } else if (mActivity instanceof BaseFragmentActivity) {
                                ((BaseFragmentActivity) mActivity).nextScreen(HomeScreenActivity.class, true);
                            }
                        }
                    });

        }


    }

    public static void NotifyImgLoad() {

        if (mHandlerBaseAct != null) {
            mHandlerBaseAct.removeCallbacks(mRunBaseAct);
        }
        mRunBaseAct = new Runnable() {
            @Override
            public void run() {
                {
                    try {

                        Bitmap bmp = BitmapFactory
                                .decodeStream(mActivity.getAssets()
                                        .open("notificationAnimation/bridge-notification-"
                                                + _indexNotifyBaseAct + ".png"));
                        mImgCloseBaseAct.setImageBitmap(bmp);

                        if (!isIncBaseAct || _indexNotifyBaseAct == 1) {
                            _indexNotifyBaseAct++;

                            isIncBaseAct = false;
                            if (_indexNotifyBaseAct == 7) {
                                _indexNotifyBaseAct = 5;
                                isIncBaseAct = true;
                            }
                        } else {
                            _indexNotifyBaseAct--;
                        }

                        mHandlerBaseAct.postDelayed(mRunBaseAct, 70);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        mHandlerBaseAct.postDelayed(mRunBaseAct, 0);
    }

//    @Override
//    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
//        super.onActivityResult(arg0, arg1, arg2);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                System.out.println("Keypad Hidden---");
//                InputMethodManager inputManager = (InputMethodManager) mActivity
//                        .getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputManager.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//
//                inputManager.toggleSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
//
//            }
//        }, 300);
//    }
}
