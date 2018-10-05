package com.calix.calixgigamanage.ui.network;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.adapter.NetworkPriorityAdapter;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.services.APIRequestHandler;
import com.calix.calixgigamanage.support.dragdrop.OnStartDragListener;
import com.calix.calixgigamanage.support.dragdrop.SimpleItemTouchHelperCallback;
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.NumberUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NetworkPriority extends BaseActivity implements OnStartDragListener {

    @BindView(R.id.network_priority_lay)
    LinearLayout mNetworkPriorityLay;

    @BindView(R.id.nework_priority_recycler_view)
    RecyclerView mNetworkPriorityRecyclerView;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.network_priority_header_bg_lay)
    RelativeLayout mNetworkPriorityHeaderBgLay;

    private ItemTouchHelper mItemTouchHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_network_priority);
        initView();
    }

    private void initView() {

            /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

       /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI( mNetworkPriorityLay);

        setHeaderView();

        setAdapter();

        APIRequestHandler.getInstance().dashboardAPICall(this);

    }


    private void setHeaderView() {

        /*Header*/
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.network_priority));

         /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mNetworkPriorityHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size45);
                    mNetworkPriorityHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(NetworkPriority.this)));
                    mNetworkPriorityHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(NetworkPriority.this), 0, 0);
                }

            });
        }
    }

    /*View onClick*/
    @OnClick({R.id.header_left_img_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
        }
    }


    private void setAdapter() {
        ArrayList<String> dummyList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dummyList.add("Lonas Android " + i);
        }


        NetworkPriorityAdapter parentalControlAdapter = new NetworkPriorityAdapter(this, dummyList, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mNetworkPriorityRecyclerView.setLayoutManager(mLayoutManager);
        mNetworkPriorityRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mNetworkPriorityRecyclerView.setNestedScrollingEnabled(false);
        mNetworkPriorityRecyclerView.setAdapter(parentalControlAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(parentalControlAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mNetworkPriorityRecyclerView);


    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);

    }

    @Override
    public void onBackPressed() {
        backScreen();
    }
}
