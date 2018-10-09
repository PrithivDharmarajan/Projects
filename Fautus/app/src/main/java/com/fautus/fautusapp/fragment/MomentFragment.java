package com.fautus.fautusapp.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fautus.fautusapp.R;
import com.fautus.fautusapp.adapter.MomentListAdapter;
import com.fautus.fautusapp.entity.MomentPhotoEntity;
import com.fautus.fautusapp.entity.PhotoEntity;
import com.fautus.fautusapp.main.BaseFragment;
import com.fautus.fautusapp.ui.HomeScreen;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.InterfaceBtnCallback;
import com.fautus.fautusapp.utils.NetworkUtil;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MomentFragment extends BaseFragment implements InterfaceBtnCallback {


    @BindView(R.id.moment_swipe_refresh_view)
    SwipeRefreshLayout mMomentSwipeRefreshView;

    @BindView(R.id.moment_recycler_view)
    RecyclerView mMomentRecyclerView;

    @BindView(R.id.no_moments_txt)
    TextView mNoMomentsTxt;

    private int mPhotoAPICountInt;
    private ArrayList<MomentPhotoEntity> mMomentArrList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_moments_screen, container, false);
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);
        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);
        /*For focus current fragment*/
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return rootView;
    }


    /*Fragment manual onResume*/
    @Override
    public void onFragmentResume() {
        super.onFragmentResume();
        /*set header text and header right img*/
        ((HomeScreen) getActivity()).setDrawerAction(AppConstants.MOMENT_FROM_MENU.equalsIgnoreCase(AppConstants.SUCCESS_CODE));
        ((HomeScreen) getActivity()).setHeaderText(getString(R.string.moments));
        ((HomeScreen) getActivity()).setHeadRightImgVisible(false, R.mipmap.app_icon);

        initView();


    }

    private void initView() {


        mMomentSwipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMomentDetails();
                mMomentSwipeRefreshView.setRefreshing(false);

            }
        });

        getMomentDetails();


    }

    private void getMomentDetails() {
         /*Check internet connection*/
        if (NetworkUtil.isNetworkAvailable(getActivity())) {

            DialogManager.getInstance().showProgress(getActivity());

            ParseQuery<ParseObject> momentQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Moment);
            momentQuery.whereEqualTo(ParseAPIConstants.closed, true);
            momentQuery.whereContainedIn(ParseAPIConstants.authorizedUsers, Collections.singletonList(ParseUser.getCurrentUser()));
            momentQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> momentObjects, ParseException e) {
                    if (e == null && momentObjects != null) {
                        if (momentObjects.size() > 0) {
                            mMomentArrList = new ArrayList<>();
                            mPhotoAPICountInt = 0;
                            getMomentPhotoDetails(momentObjects, 0);
                        } else {
                            DialogManager.getInstance().hideProgress();
                        }
                        mMomentSwipeRefreshView.setVisibility(momentObjects.size() > 0 ? View.VISIBLE : View.GONE);
                        mNoMomentsTxt.setVisibility(momentObjects.size() > 0 ? View.GONE : View.VISIBLE);
                    } else if (e != null) {
                        DialogManager.getInstance().hideProgress();
                        DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), MomentFragment.this);
                        mMomentSwipeRefreshView.setVisibility(View.GONE);
                        mNoMomentsTxt.setVisibility(View.VISIBLE);
                    }

                }
            });
        } else {
            /*Alert message will be appeared if there is no internet connection*/
            DialogManager.getInstance().hideProgress();
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), getString(R.string.no_internet), this);
        }
    }

    public void getMomentPhotoDetails(final List<ParseObject> momentList, final int momentListPos) {

      /*Check internet connection*/
        if (getActivity() != null && NetworkUtil.isNetworkAvailable(getActivity())) {
            if (momentList.size() > momentListPos && momentList.get(momentListPos) != null) {


                ParseQuery<ParseObject> photoQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photo);
                photoQuery.whereEqualTo(ParseAPIConstants.moment, momentList.get(momentListPos));
                photoQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> momentPhotosObjList, ParseException e) {
                        if (e == null && momentPhotosObjList != null && momentPhotosObjList.size() > 0) {
                            MomentPhotoEntity momentPhotoEntity = new MomentPhotoEntity();
                            momentPhotoEntity.setMoment(momentList.get(momentListPos));
                            ArrayList<PhotoEntity> ParseFileObj = new ArrayList<>();
                            int purchasedPhotosCountInt = 0;
                            for (int j = 0; j < momentPhotosObjList.size(); j++) {
                                PhotoEntity photoFile = new PhotoEntity();
                                photoFile.setPhotoObj(momentPhotosObjList.get(j));
                                photoFile.setPhoto(momentPhotosObjList.get(j).getParseFile(ParseAPIConstants.photo));
                                photoFile.setPhoto_purchased(momentPhotosObjList.get(j).getBoolean(ParseAPIConstants.purchased) ? AppConstants.SUCCESS_CODE : AppConstants.FAILURE_CODE);
                                if (momentPhotosObjList.get(j).getBoolean(ParseAPIConstants.purchased)) {
                                    purchasedPhotosCountInt += 1;
                                }
                                ParseFileObj.add(photoFile);
                            }
                            momentPhotoEntity.setPurchasedPhotosCount(String.valueOf(purchasedPhotosCountInt));
                            momentPhotoEntity.setPhoto(ParseFileObj);
                            mMomentArrList.add(momentPhotoEntity);


                        } else if (e != null) {
                            DialogManager.getInstance().hideProgress();
                            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), MomentFragment.this);
                        }


                        mPhotoAPICountInt += 1;

                        if (momentList.size() == mPhotoAPICountInt) {
                            DialogManager.getInstance().hideProgress();

                            Collections.sort(mMomentArrList, new Comparator<MomentPhotoEntity>() {
                                @Override
                                public int compare(MomentPhotoEntity firstMomentDetails, MomentPhotoEntity secondMomentDetails) {
                                    Date firstDate = new Date();
                                    Date secondDate = new Date();
                                    try {
                                        firstDate = firstMomentDetails.getMoment().getDate(ParseAPIConstants.momentDate);
                                        secondDate = secondMomentDetails.getMoment().getDate(ParseAPIConstants.momentDate);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    return firstDate.after(secondDate) ? -1 : 1;
                                }
                            });

                            setAdapter(mMomentArrList);
                        } else {
                            getMomentPhotoDetails(momentList, mPhotoAPICountInt);
                        }

                    }
                });
            } else {
                DialogManager.getInstance().hideProgress();

                Collections.sort(mMomentArrList, new Comparator<MomentPhotoEntity>() {
                    @Override
                    public int compare(MomentPhotoEntity firstMomentDetails, MomentPhotoEntity secondMomentDetails) {
                        Date firstDate = new Date();
                        Date secondDate = new Date();
                        try {
                            firstDate = firstMomentDetails.getMoment().getDate(ParseAPIConstants.momentDate);
                            secondDate = secondMomentDetails.getMoment().getDate(ParseAPIConstants.momentDate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return firstDate.after(secondDate) ? -1 : 1;
                    }
                });
                setAdapter(mMomentArrList);
            }
        } else {
            /*Alert message will be appeared if there is no internet connection*/
            DialogManager.getInstance().hideProgress();
            if (getActivity() != null)
                DialogManager.getInstance().showAlertPopup(getContext(), getString(R.string.app_name), getString(R.string.no_internet), this);
        }

    }

    @Override
    public void onOkClick() {

    }

    private void setAdapter(ArrayList<MomentPhotoEntity> momentArrList) {
        MomentListAdapter  galleryMomentAdapter = new MomentListAdapter(getActivity(), momentArrList);
        mMomentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMomentRecyclerView.setAdapter(galleryMomentAdapter);
        mMomentRecyclerView.setNestedScrollingEnabled(true);
    }
}
