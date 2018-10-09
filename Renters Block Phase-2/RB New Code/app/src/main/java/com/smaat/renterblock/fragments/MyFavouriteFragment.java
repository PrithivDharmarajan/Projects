package com.smaat.renterblock.fragments;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smaat.renterblock.R;
import com.smaat.renterblock.adapters.BoardAdapter;
import com.smaat.renterblock.adapters.FavouriteAdapter;
import com.smaat.renterblock.entity.BoardsEntity;
import com.smaat.renterblock.entity.FavouriteEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.BoardsResponse;
import com.smaat.renterblock.model.FavouriteResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.PreferenceUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MyFavouriteFragment extends BaseFragment {


    @BindView(R.id.favourites_list)
    RecyclerView mFavouritesRecyclerView;


    @BindView(R.id.board_list)
    RecyclerView mBoardRecyclerView;

    @BindView(R.id.favourite_txt)
    TextView mFavouriteTxt;

    @BindView(R.id.favourites_view)
    View mFavouritesView;

    @BindView(R.id.boards_txt)
    TextView mBoardsTxt;

    @BindView(R.id.boards_view)
    View mBoardsView;

    private boolean mIsEditBool = true;
    private ArrayList<FavouriteEntity> mFavouriteListRes = new ArrayList<>();
    private ArrayList<BoardsEntity> mBoardListRes = new ArrayList<>();

    private FavouriteAdapter mFavouriteAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_my_favourite, container, false);
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

        initView();
        return rootView;
    }

    @Override
    public void onFragmentResume() {
         /* If the value of visibleInt is zero,  the view will set gone. Or if the value of visibleInt is one,  the view will set visible. Or else, the view will set gone*/
        if (getActivity() != null) {
             /*Slide menu action*/
            ((HomeScreen) getActivity()).setDrawerAction(true);

            /*set header first  img*/
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(1);
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(mFavouritesRecyclerView.getVisibility() == View.VISIBLE ?
                    (mIsEditBool ? R.drawable.edit_icon : R.drawable.close_icon) : R.drawable.add_icon, 1);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 0);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.mipmap.app_icon, 0,"");

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.app_name), 0);

            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.my_favourites), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.app_name), 0);



            ((HomeScreen) getActivity()).mHeaderRightFirstImgLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getActivity() != null) {

                        if (getActivity() != null) {
                            if (mFavouritesRecyclerView.getVisibility() == View.VISIBLE) {
                                if (mFavouriteListRes.size() > 0) {
                                    mIsEditBool = !mIsEditBool;
                                    setFavoriteAdapter();
                                } else {
                                    DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.you_not_having), MyFavouriteFragment.this);
                                }
                            } else {
                                AppConstants.MAP_CURRENT_BACK_FRAGMENT = new MyFavouriteFragment();
                                AppConstants.TYPE_OF_PROPERTY = AppConstants.RENT;
                                ((HomeScreen) getActivity()).addFragment(new MapFragment());

                            }
                        }
                    }
                }
            });

        }
    }


    /*init view*/
    private void initView() {
        sysOut("User ID---" + PreferenceUtil.getUserID(getActivity()));
        /*Favourite API Call*/
        APIRequestHandler.getInstance().getFavouriteListAPICall(this);

    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof FavouriteResponse) {
            FavouriteResponse favouriteResponse = (FavouriteResponse) resObj;
            if (favouriteResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                mFavouriteListRes = new ArrayList<>();
                mFavouriteListRes = favouriteResponse.getResult();
                setFavoriteAdapter();

            } else if (getActivity() != null) {
                DialogManager.getInstance().showAlertPopup(getActivity(), favouriteResponse.getMsg(), this);
            }
        } else if (resObj instanceof BoardsResponse) {
            BoardsResponse boardResponse = (BoardsResponse) resObj;
            if (boardResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                mBoardListRes = new ArrayList<>();
                mBoardListRes = boardResponse.getResult();
                setBoardAdapter();

            } else if (getActivity() != null) {
                DialogManager.getInstance().showAlertPopup(getActivity(), boardResponse.getMsg(), this);
            }
        }
    }

    /*set OnClick*/
    @OnClick({R.id.favourite_lay, R.id.boards_lay})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.favourite_lay:
            case R.id.boards_lay:
                if (view.getId() == R.id.favourite_lay) {
                    APIRequestHandler.getInstance().getFavouriteListAPICall(this);
                } else {
                    APIRequestHandler.getInstance().getBoardListAPICall(this);
                }
                setData(view.getId() == R.id.favourite_lay);
                break;

        }
    }

    /*Set Data*/
    private void setData(boolean isFavouriteBool) {
        if (getActivity() != null) {
            mFavouriteTxt.setTextColor(ContextCompat.getColor(getActivity(), isFavouriteBool ? R.color.app_blue : R.color.black));
            mFavouritesView.setVisibility(isFavouriteBool ? View.VISIBLE : View.GONE);

            mBoardsTxt.setTextColor(ContextCompat.getColor(getActivity(), !isFavouriteBool ? R.color.app_blue : R.color.black));
            mBoardsView.setVisibility(!isFavouriteBool ? View.VISIBLE : View.INVISIBLE);

            mFavouritesRecyclerView.setVisibility(isFavouriteBool ? View.VISIBLE : View.GONE);
            mBoardRecyclerView.setVisibility(!isFavouriteBool ? View.VISIBLE : View.GONE);

            if (isFavouriteBool) setFavoriteAdapter();
            else setBoardAdapter();
        }
    }

    private void setFavoriteAdapter() {
        if (getActivity() != null) {
            AppConstants.IS_FAVOURITE_DELETE = !mIsEditBool;
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(mIsEditBool ? R.drawable.edit_icon : R.drawable.close_icon, 1);
            mFavouriteAdapter = new FavouriteAdapter(getActivity(), mFavouriteListRes);
            mFavouritesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mFavouritesRecyclerView.setAdapter(mFavouriteAdapter);
        }
    }

    private void setBoardAdapter() {
        if (getActivity() != null) {
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.add_icon, 1);
            BoardAdapter boardAdapter = new BoardAdapter(getActivity(), mBoardListRes);
            mBoardRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            mBoardRecyclerView.setAdapter(boardAdapter);

        }
    }
}