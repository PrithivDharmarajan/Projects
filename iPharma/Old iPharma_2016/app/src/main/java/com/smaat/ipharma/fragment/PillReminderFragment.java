package com.smaat.ipharma.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smaat.ipharma.R;
import com.smaat.ipharma.adapter.PillReminderAdapter;
import com.smaat.ipharma.database.DatabaseUtil;
import com.smaat.ipharma.db.model.PillTimerResponse;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.util.AppConstants;

import java.util.ArrayList;


public class PillReminderFragment extends BaseFragment {

    private RecyclerView mPillReminderList;
    private FloatingActionButton mAddReminder;
    private DatabaseUtil db;
    private PillReminderAdapter mAdapter;
    public  ArrayList<PillTimerResponse> mPillRes;
    public static ArrayList<PillTimerResponse> mPillResDBRes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.pill_reminder_screen, container, false);

        db = new DatabaseUtil();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewGroup viewGroup = (ViewGroup) view.findViewById(R.id.parent_lay);
        setupUI(viewGroup);
        setFont(viewGroup, mHelvetica);
        initiComponents(view);

        AppConstants.FRAG = AppConstants.MAP_SCREEN;
        HomeScreen.mHeaderLeftLay.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                HomeScreen.onBackMove(getActivity());
            }
        });


    }

    private void initiComponents(View view) {
        mPillReminderList = (RecyclerView) view.findViewById(R.id.pill_reminder_list);
        mAddReminder = (FloatingActionButton) view.findViewById(R.id.add_new_float_btn);

        mAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddNewReminderFragment.mReminderID = 0;
                HomeScreen.mHeaderRightLay.setVisibility(View.INVISIBLE);
                HomeScreen.mHeaderLeft
                        .setBackgroundResource(R.drawable.back_butto);
                ((HomeScreen) getActivity())
                        .replaceFragment(new AddNewReminderFragment(), true);
            }
        });


        HomeScreen.mBottombar.setVisibility(View.GONE);
        mPillResDBRes=db.getPillReminderRes();
        setAdapter(mPillResDBRes);
    }

    private void setAdapter(ArrayList<PillTimerResponse> mPillRemData) {

        if (mAdapter == null) {
            mPillRes = new ArrayList<>();
            mPillRes.addAll(mPillRemData);
            mAdapter = new PillReminderAdapter(getActivity(), mPillRes);
            mPillReminderList.setLayoutManager(new LinearLayoutManager(getActivity()));
            mPillReminderList.setAdapter(mAdapter);
        } else {
            mPillRes.clear();
            mPillRes.addAll(mPillRemData);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });

        }
    }


}
