package com.smaat.renterblock.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.smaat.renterblock.R;
import com.smaat.renterblock.adapters.SchedulingAdapter;
import com.smaat.renterblock.entity.ScheduleListArray;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.model.ScheduleListResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.InterfaceBtnCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SchedulingFragment extends BaseFragment {

    @BindView(R.id.scheduling_recycler_view)
    RecyclerView mSchedulingRecyclerView;

    @BindView(R.id.delete_btn)
   Button mDeleteBtn;



    SchedulingAdapter mSchedulingAdapter;

    ArrayList<ScheduleListArray> mFinalList = new ArrayList<>();

    boolean mEditBool = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_scheduling, container, false);
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
            ((HomeScreen) getActivity()).setDrawerAction(AppConstants.SCHEDULE_BACK_FRAGMENT instanceof SchedulingFragment);

            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.scheduling), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.app_name), 0);

             /*set header first  img*/
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(1);
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.edit_button, 1);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 2);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.drawable.add_button, 1,"");

               /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.send), 0);

            ((HomeScreen) getActivity()).mHeaderRightSecondImgLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppConstants.EDIT = "ADD";
                    ((HomeScreen) getActivity()).addFragment(new CreateScheduleFragment());
                }
            });

            mEditBool = false;
            if(getActivity()!=null) {
                ((HomeScreen) getActivity()).mHeaderRightFirstImgLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!mEditBool) {
                            mEditBool = true;
                            setAdapter(mFinalList, mEditBool);
                        } else {
                            mEditBool = false;
                            setAdapter(mFinalList, mEditBool);
                        }
                    }
                });
            }

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        getScheduleList();
        AppConstants.INVITE_FRIENDS_HASH_MAP.clear();
        AppConstants.SCHEDULE_TIME = getString(R.string.time);
        AppConstants.SCHEDULE_DATE = getString(R.string.date);
    }

    /*View OnClick*/
    @OnClick({R.id.delete_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_btn:
                if (AppConstants.DELETE_SCHEDULE_ID.size() > 0)
                    deleteScheduleAPI();
                break;

        }
    }

    private void getScheduleList() {

        APIRequestHandler.getInstance().getScheduleList(SchedulingFragment.this);

    }

    private void deleteScheduleAPI() {

        String deleteIdStr = "";
       deleteIdStr= TextUtils.join(",", AppConstants.DELETE_SCHEDULE_ID);
        APIRequestHandler.getInstance().deleteSchedule(SchedulingFragment.this, deleteIdStr);
    }

    private void setAdapter(ArrayList<ScheduleListArray> mEntity, boolean mEditVariable) {
        if (getActivity() != null) {

            if(mEntity.size()==0){
                ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.edit_button, 2);
            }

           else if (!mEditVariable) {
//                mEditVariable = true;
                mDeleteBtn.setVisibility(View.GONE);
                ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.edit_button, 1);
            } else {
//                mEditVariable = false;
                ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.close_icon, 1);
                mDeleteBtn.setVisibility(View.VISIBLE);
            }

            mSchedulingAdapter = new SchedulingAdapter(SchedulingFragment.this, mEntity, mEditVariable);
            GridLayoutManager manager = new GridLayoutManager(getActivity(), 1);
            mSchedulingRecyclerView.setLayoutManager(manager);
            mSchedulingRecyclerView.setNestedScrollingEnabled(false);
            mSchedulingRecyclerView.setFocusable(false);
            mSchedulingRecyclerView.setAdapter(mSchedulingAdapter);

        }
    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);

        if (responseObj instanceof ScheduleListResponse) {
            final ScheduleListResponse mResponse = (ScheduleListResponse) responseObj;
            if (mResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                mFinalList.clear();
               if(mResponse.getResult().getAccepted_schedule()!=null) {
                  for (int i = 0; i < mResponse.getResult().getAccepted_schedule().size(); i++) {
                       ScheduleListArray entity = mResponse.getResult().getAccepted_schedule().get(i);
                       entity.setIs_Accepted_schedule(AppConstants.SUCCESS_CODE);
                       entity.setIs_friends_schedule(AppConstants.FAILURE_CODE);
                       entity.setIs_my_schedule(AppConstants.FAILURE_CODE);
                       mFinalList.add(entity);
                   }
               }
                if(mResponse.getResult().getFriend_schedule()!=null) {
                    for (int i = 0; i < mResponse.getResult().getFriend_schedule().size(); i++) {
                        ScheduleListArray entity = mResponse.getResult().getFriend_schedule().get(i);
                        entity.setIs_Accepted_schedule(AppConstants.FAILURE_CODE);
                        entity.setIs_friends_schedule(AppConstants.SUCCESS_CODE);
                        entity.setIs_my_schedule(AppConstants.FAILURE_CODE);
                        mFinalList.add(entity);
                    }
                }
                if(mResponse.getResult().getMyschedule()!=null) {
                    for (int i = 0; i < mResponse.getResult().getMyschedule().size(); i++) {
                        ScheduleListArray entity = mResponse.getResult().getMyschedule().get(i);
                        entity.setIs_Accepted_schedule(AppConstants.FAILURE_CODE);
                        entity.setIs_friends_schedule(AppConstants.FAILURE_CODE);
                        entity.setIs_my_schedule(AppConstants.SUCCESS_CODE);
                        mFinalList.add(entity);
                    }
                }

                setAdapter(mFinalList, mEditBool);
            }
        } else if (responseObj instanceof CommonResponse) {
            final CommonResponse mResponse = (CommonResponse) responseObj;


            if (mResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                getScheduleList();

                DialogManager.getInstance().showAlertPopup(getContext(), mResponse.getMsg(), new InterfaceBtnCallback() {
                    @Override
                    public void onPositiveClick() {

                    }
                });
            }
        }
    }

}
