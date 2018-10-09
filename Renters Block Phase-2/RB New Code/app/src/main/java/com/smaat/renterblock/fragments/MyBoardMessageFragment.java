package com.smaat.renterblock.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.smaat.renterblock.R;
import com.smaat.renterblock.adapters.BoardMessageAdapter;
import com.smaat.renterblock.entity.BoardMessageEntity;
import com.smaat.renterblock.entity.ChatInputEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.BoardMessageResponse;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.model.CreateGroupChatResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.PreferenceUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyBoardMessageFragment extends BaseFragment {
    @BindView(R.id.board_msg_recycler_view)
    RecyclerView mBoardMessageRecyclerView;
    @BindView(R.id.edit_lay)
    RelativeLayout mEditLay;
    @BindView(R.id.enter_chat_edt)
    EditText mChatEdt;
    @BindView(R.id.post_btn)
    Button mPostBtn;
    private BoardMessageAdapter mBoardMsgAdapter;
    private ArrayList<BoardMessageEntity> mMessageList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstances) {
        View rootView = inflater.inflate(R.layout.frag_board_chat, container, false);
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
             /*Slide menu action*/
            ((HomeScreen) getActivity()).setDrawerAction(false);

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
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.my_boards), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.app_name), 0);

            initView();

        }
    }

    private void initView() {
        APIRequestHandler.getInstance().getBoardMessages(AppConstants.BOARD_MESSAGE_PROPERTY_ID, MyBoardMessageFragment.this);

        mPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = mChatEdt.getText().toString();
                if (msg.isEmpty()) {
                    DialogManager.getInstance().showToast(getActivity(), "Please enter message ");
                } else {
                    APIRequestHandler.getInstance().sendBoardMessage(AppConstants.BOARD_MESSAGE_PROPERTY_ID, msg, MyBoardMessageFragment.this);
                }
            }
        });


    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof BoardMessageResponse) {
            BoardMessageResponse mRes = (BoardMessageResponse) resObj;
            mMessageList = mRes.getResult();
            setAdapter();

        } else if (resObj instanceof CommonResponse) {
            CommonResponse mRes = (CommonResponse) resObj;
            mChatEdt.setText("");
            APIRequestHandler.getInstance().getBoardMessages(AppConstants.BOARD_MESSAGE_PROPERTY_ID, MyBoardMessageFragment.this);
        }
       else if (resObj instanceof CreateGroupChatResponse) {
            CreateGroupChatResponse chatIdRes = (CreateGroupChatResponse) resObj;
            if (chatIdRes.getError_code().equals(AppConstants.SUCCESS_CODE)) {
                AppConstants.CHAT_INPUT_ENTITY = new ChatInputEntity();
                AppConstants.CHAT_INPUT_ENTITY.setUser_id(PreferenceUtil.getUserID(getActivity()));
                AppConstants.CHAT_INPUT_ENTITY.setFriend_id(chatIdRes.getFriend_id());
                AppConstants.CHAT_INPUT_ENTITY.setSchedule_id(chatIdRes.getResult());

                ((HomeScreen) getActivity()).addFragment(new ChatFragment());
            }
        }
    }

    private void setAdapter() {
        if (getActivity() != null) {

            mBoardMsgAdapter = new BoardMessageAdapter(MyBoardMessageFragment.this, mMessageList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            mBoardMessageRecyclerView.setLayoutManager(linearLayoutManager);
            mBoardMessageRecyclerView.setAdapter(mBoardMsgAdapter);
            mBoardMessageRecyclerView.setNestedScrollingEnabled(false);
            linearLayoutManager.setStackFromEnd(true);

        }

    }

    @Override
    public void onRequestFailure(Throwable t) {
        super.onRequestFailure(t);

    }
}
