package com.smaat.spark.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.smaat.spark.R;
import com.smaat.spark.adapter.DiscoverAdapter;
import com.smaat.spark.entity.inputEntity.ChatConnDisInputEntity;
import com.smaat.spark.entity.outputEntity.DiscoverEntity;
import com.smaat.spark.main.BaseFragment;
import com.smaat.spark.model.DiscoverResponse;
import com.smaat.spark.services.APIRequestHandler;
import com.smaat.spark.ui.HomeScreen;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.DialogManager;
import com.smaat.spark.utils.GlobalMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DiscoverFragment extends BaseFragment {

    @BindView(R.id.discover_recycler_view)
    RecyclerView mDiscoverRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_discover_screen, container, false);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDiscoverApiCall();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreen) getActivity()).setHeaderLeftClick(true);
        ((HomeScreen) getActivity()).setHeadRigImgVisible(false, R.mipmap.app_icon);
        ((HomeScreen) getActivity()).setHeaderText(getString(R.string.discover));
        ((HomeScreen) getActivity()).setHeaderLay(1);

        if (AppConstants.VIDEO_CHAT_SCREEN.equals(AppConstants.SUCCESS_CODE)) {
            AppConstants.VIDEO_CHAT_SCREEN = AppConstants.FAILURE_CODE;
            AppConstants.CONNECT_USER_ANONYMOUS=AppConstants.FAILURE_CODE;
            AppConstants.CONNECT_FRIEND_ANONYMOUS=AppConstants.FAILURE_CODE;
            AppConstants.CHAT_DISCOVER=AppConstants.SUCCESS_CODE;

            ((HomeScreen) getActivity()).setFooterImg(0);
            ((HomeScreen) getActivity()).resetFragmentStack(AppConstants.BOTTOM_BUTTON_POS);
            GlobalMethods.storeStringValue(getActivity(), AppConstants.CHAT_CONNECTED_STATUS, AppConstants.SUCCESS_CODE);
            ((HomeScreen) getActivity()).replaceFragment(new ChatConnectFragment(), 1);

        }

    }

    private void getDiscoverApiCall() {
        ChatConnDisInputEntity discoverInputEntityRes = new ChatConnDisInputEntity(AppConstants.API_DISCOVER, AppConstants.PARAMS_USER_ID, GlobalMethods.getUserID(getActivity()));
        APIRequestHandler.getInstance().callDiscoverAPI(discoverInputEntityRes, this);
    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof DiscoverResponse) {
            DiscoverResponse discoverRes = (DiscoverResponse) resObj;
            if (discoverRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                setDiscoverAdapter(discoverRes.getResult());
            } else {
                DialogManager.getInstance().showAlertPopup(getActivity(), discoverRes.getMessage());
            }
        }
    }

    private void setDiscoverAdapter(ArrayList<DiscoverEntity> discoverList) {

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int widthInt = displaymetrics.widthPixels;

        final DiscoverAdapter discoverAdapter = new DiscoverAdapter(getActivity(), discoverList, widthInt);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int mod = position % 6;

                if (position == 0)
                    return 3;
                else if (position < 5)
                    return 1;
                else if (position == 5)
                    return 2;
                else if (mod == 0)
                    return 3;
                else if (mod < 5)
                    return 1;
                else
                    return 2;
            }
        });
        mDiscoverRecyclerView.setLayoutManager(layoutManager);
        mDiscoverRecyclerView.setAdapter(discoverAdapter);

    }

}
