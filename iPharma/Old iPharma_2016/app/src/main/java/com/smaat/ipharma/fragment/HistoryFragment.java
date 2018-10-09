package com.smaat.ipharma.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.smaat.ipharma.R;
import com.smaat.ipharma.apiinterface.APICommonInterface;
import com.smaat.ipharma.entity.HistoryOrderEntity;
import com.smaat.ipharma.entity.OrderHistoryCommonResponseEntity;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.DialogManager;
import com.smaat.ipharma.util.DialogMangerCallback;
import com.smaat.ipharma.util.GlobalMethods;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HistoryFragment extends BaseFragment implements OnClickListener,
        DialogMangerCallback {
    private ImageView mLeft, mRight, mPrescriptionImage;
    private ProgressBar mProductListProgressBar;
    private ArrayList<String> images;
    private float downX, downY, upX, upY;
    public static int i = 0;
    static final int MIN_DISTANCE = 100;
    private LinearLayout mSelect_from_history;
    private String UserID;
    private ArrayList<HistoryOrderEntity> history_list = new ArrayList<HistoryOrderEntity>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_history, container,
                false);
        setupUI(rootview);

        return rootview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup mViewGroup = (ViewGroup) view
                .findViewById(R.id.parent_layout);
        setupUI(mViewGroup);
        hideSoftKeyboard(getActivity());
        UserID = GlobalMethods.getUserID(getActivity());
        initComponents(view);
        callOrderHistoryService();// API Call

        HomeScreen.mHeaderLeftLay.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                HomeScreen.onBackMove(getActivity());
            }
        });

        mSelect_from_history.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                AppConstants.from_my_order = AppConstants.FAILURE_CODE;
                AppConstants.photo = AppConstants.PHOTO;
                HomeScreen.mHeaderRightLay.setVisibility(View.INVISIBLE);
                HomeScreen.mBottombar.setVisibility(View.GONE);
                HomeScreen.mFooterText.setText(R.string.place_order);
                if (history_list != null && history_list.size() != 0) {
                    AppConstants.history_mPharmacyName = history_list.get(i)
                            .getShopName();
                    AppConstants.history_mPharmacyAddress = history_list.get(i)
                            .getAddress();
                    AppConstants.history_mDistance = history_list.get(i)
                            .getDeliveryTime();
                    AppConstants.history_mShopId = history_list.get(i)
                            .getShopID();
                    AppConstants.history_mPrescriptionId = history_list.get(i)
                            .getPrescriptionID();
                    AppConstants.history_mNotehere = history_list.get(i)
                            .getOrderNote();
                    AppConstants.history_mPhonenum = history_list.get(i)
                            .getPhone();


                } else {
                    AppConstants.history_mPharmacyName = "";
                    AppConstants.history_mPharmacyAddress = "";
                    AppConstants.history_mDistance = "";
                    AppConstants.history_mShopId = "";
                    AppConstants.history_mPrescriptionId = "";
                    AppConstants.history_mNotehere = "";
                    AppConstants.history_mPhonenum = "";
                }

                ((HomeScreen) getActivity()).replaceFragment(
                        new OrderDetailsFragment(), true);

            }
        });

    }

    private void callOrderHistoryService() {

        DialogManager.showProgress(getActivity());
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(
                AppConstants.Base_Url).build();
        APICommonInterface interfaces = restAdapter
                .create(APICommonInterface.class);

        // APIRequestHandler.getInstance().getMyOrders(UserID, this);
        interfaces.getMyOrders(UserID,
                new Callback<OrderHistoryCommonResponseEntity>() {

                    public void failure(RetrofitError arg0) {

                        DialogManager.hideProgress(getActivity());
                        mLeft.setVisibility(View.INVISIBLE);
                        mRight.setVisibility(View.INVISIBLE);
                        mSelect_from_history.setClickable(false);
                        DialogManager.showCustomAlertDialog(getActivity(),
                                HistoryFragment.this,
                                "Please Check your Intenet Connection.");
                    }

                    public void success(
                            OrderHistoryCommonResponseEntity response,
                            Response obj) {

                        DialogManager.hideProgress(getActivity());
                        if (response.getStatus().equalsIgnoreCase("1")) {
                            images = new ArrayList<String>();
                            history_list.clear();
                            history_list = response.getResult()
                                    .getOrderResult();
                            if (history_list != null) {
                                for (int i = 0; i < response.getResult()
                                        .getOrderResult().size(); i++) {
                                    images.add(response.getResult()
                                            .getOrderResult().get(i)
                                            .getPrescriptionURL());
                                }
                                if (images != null && images.size() == 0) {
                                    mLeft.setVisibility(View.VISIBLE);
                                    mRight.setVisibility(View.VISIBLE);
                                    mLeft.setClickable(false);
                                    mRight.setClickable(false);
                                }
                                if (images.size() != 0) {
                                    AppConstants.history_prec_img = images
                                            .get(0);
                                    setImage(AppConstants.Base_Url + "/" + images.get(0));
                                    mLeft.setVisibility(View.VISIBLE);
                                    mLeft.setClickable(false);
                                } else {
                                    mProductListProgressBar
                                            .setVisibility(View.GONE);
                                    DialogManager
                                            .showCustomAlertDialog(
                                                    getActivity(),
                                                    HistoryFragment.this,
                                                    getString(R.string.no_history_available));
                                }
                            } else {
                                DialogManager
                                        .showCustomAlertDialog(
                                                getActivity(),
                                                HistoryFragment.this,
                                                getString(R.string.no_history_available));
                            }
                        }
                    }

                });
    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        if (responseObj instanceof OrderHistoryCommonResponseEntity) {
            OrderHistoryCommonResponseEntity mOrderHistoryCommonResponse = (OrderHistoryCommonResponseEntity) responseObj;
            if (mOrderHistoryCommonResponse.getStatus().equalsIgnoreCase(
                    AppConstants.SUCCESS_CODE)) {
                images = new ArrayList<String>();
                history_list.clear();
                history_list = mOrderHistoryCommonResponse.getResult()
                        .getOrderResult();
                for (int i = 0; i < mOrderHistoryCommonResponse.getResult()
                        .getOrderResult().size(); i++) {
                    images.add(mOrderHistoryCommonResponse.getResult()
                            .getOrderResult().get(i).getPrescriptionURL());
                }
                if (images != null && images.size() == 0) {
                    mLeft.setVisibility(View.INVISIBLE);
                    mRight.setVisibility(View.INVISIBLE);
                }
                if (images.size() != 0) {
                    AppConstants.history_prec_img = images.get(0);
                    setImage(AppConstants.Base_Url + "/" + images.get(0));

                    mLeft.setVisibility(View.INVISIBLE);
                } else {
                    DialogManager.showCustomAlertDialog(getActivity(),
                            HistoryFragment.this,
                            getString(R.string.no_history_available));
                }
            }
        }
        super.onRequestSuccess(responseObj);
    }

    @Override
    public void onRequestFailure(RetrofitError errorCode) {

        super.onRequestFailure(errorCode);
        mLeft.setVisibility(View.INVISIBLE);
        mRight.setVisibility(View.INVISIBLE);
        mSelect_from_history.setClickable(false);

    }

    private void initComponents(View view) {
        mProductListProgressBar = (ProgressBar) view
                .findViewById(R.id.img_progress);
        mLeft = (ImageView) view.findViewById(R.id.left_arrow);
        mRight = (ImageView) view.findViewById(R.id.right_arrow);
        mPrescriptionImage = (ImageView) view.findViewById(R.id.history_image);
        mSelect_from_history = (LinearLayout) view
                .findViewById(R.id.select_from_history);
        mLeft.setOnClickListener(this);
        mRight.setOnClickListener(this);
        setImageListener(mPrescriptionImage);
        // mPrescriptionImage.setImageResource(images.get(i));
    }

    public void setImageListener(View v) {

        v.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        downX = event.getX();
                        downY = event.getY();
                        return true;
                    }
                    case MotionEvent.ACTION_UP: {
                        upX = event.getX();
                        upY = event.getY();

                        float deltaX = downX - upX;
                        float deltaY = downY - upY;

                        // swipe horizontal?
                        if (Math.abs(deltaX) > Math.abs(deltaY)) {
                            if (Math.abs(deltaX) > MIN_DISTANCE) {
                                // left or right
                                if (deltaX < 0) {
                                    onRightToLeftSwipe();
                                    return true;
                                }
                                if (deltaX > 0) {
                                    onLeftToRightSwipe();
                                    return true;
                                }
                            } else {
                                Log.i("Subway",
                                        "Horizontal Swipe was only "
                                                + Math.abs(deltaX)
                                                + " long, need at least "
                                                + MIN_DISTANCE);
                                return false; // We don't consume the event
                            }
                        }
                        // swipe vertical?
                        else {
                            if (Math.abs(deltaY) > MIN_DISTANCE) {
                                // top or down
                                if (deltaY < 0) {

                                    return true;
                                }
                                if (deltaY > 0) {
                                    return true;
                                }
                            } else {
                                Log.i("Subway",
                                        "Vertical Swipe was only "
                                                + Math.abs(deltaX)
                                                + " long, need at least "
                                                + MIN_DISTANCE);
                                return false; // We don't consume the event
                            }
                        }

                        return true;
                    }
                }
                return true;
            }
        });

    }

    public void onRightToLeftSwipe() {
        i--;

        if (i < images.size() && !(i < 0)) {

            mRight.setClickable(true);
            AppConstants.history_prec_img = images.get(i);

            setImage(AppConstants.Base_Url + "/" + images.get(i));

        } else {
            i++;

        }

    }

    private void setImage(String url) {
        Glide.with(HistoryFragment.this).load(url)
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(R.drawable.splash)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mPrescriptionImage);
    }

    public void onLeftToRightSwipe() {
        i++;
        if (i < images.size()) {

            AppConstants.history_prec_img = images.get(i);
            setImage(AppConstants.Base_Url + "/" + images.get(i));


            mLeft.setClickable(true);

        } else {
            i--;
        }

    }


    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.left_arrow:
                i--;
                if (i < 1) {
                    mLeft.setClickable(false);
                }
                if (i < images.size()) {

                    // mPrescriptionImage.setImageResource(images[i]);
                    AppConstants.history_prec_img = images.get(i);

                    setImage(AppConstants.Base_Url + "/" + images.get(i));

                    mRight.setClickable(true);
                    mRight.setVisibility(View.VISIBLE);
                    if (i == 0) {
                        mLeft.setVisibility(View.INVISIBLE);
                    }
                } else {
                    i++;

                }
                break;
            case R.id.right_arrow:
                i++;
                if (i < images.size()) {


                    AppConstants.history_prec_img = images.get(i);
                    setImage(AppConstants.Base_Url + "/" + images.get(i));
                    mLeft.setClickable(true);
                    mLeft.setVisibility(View.VISIBLE);
                    if (images.size() - 1 == i) {
                        mRight.setVisibility(View.INVISIBLE);
                    }

                } else {

                    i--;

                }
                break;

            default:
                break;
        }
    }

    public void onItemclick(String SelctedItem, int pos) {
        // TODO Auto-generated method stub

    }

    public void onOkclick() {
        HomeScreen.mHeaderRightLay.setVisibility(View.INVISIBLE);
        HomeScreen.mBottombar.setVisibility(View.GONE);
        HomeScreen.mHeaderLeft.setBackgroundResource(R.drawable.back_butto);
        ((HomeScreen) getActivity()).replaceFragment(HomeScreen.mFragment,
                true);
    }
}
