package com.bridgellc.bridge.ui.upload;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.main.BaseFragmentActivity;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.TypefaceSingleton;

/**
 * Created by sys on 3/14/2016.
 */
public class UploadItemServiceQuantityFragment extends Fragment implements DialogMangerOkCallback {

    private View view;
    private Button mNextBtn;
    private EditText mInputBox;
    private TextView mTitleTxt;
    private String mAlertMsg = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.upload_text_type_price, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UploadScreen.mEntity.quantity != null && UploadScreen.mEntity.quantity.length() > 0) {
            mInputBox.setText(UploadScreen.mEntity.quantity);
        }
    }

    private void initComponents() {
        mNextBtn = (Button) view.findViewById(R.id.footer_btn);
        mInputBox = (EditText) view.findViewById(R.id.input_box);


        mNextBtn.setText(getString(R.string.next));


        mTitleTxt = (TextView) view.findViewById(R.id.title_tv);

        Typeface mLightFont = TypefaceSingleton.getTypeface().getLightFont(getActivity());
        mNextBtn.setTypeface(mLightFont);
        mInputBox.setTypeface(mLightFont);
        mTitleTxt.setTypeface(mLightFont);

        setData();
//        if (UploadScreen.mEntity.isGood) {
//            if (UploadScreen.mEntity.isGood) {
//                if (UploadScreen.mEntity.isSelling && UploadScreen.mEntity.isGood && !UploadScreen.mEntity.isDeliveryInPerson && UploadScreen.mEntity.category.equalsIgnoreCase(getString(R.string.ticket)))
//                    mTitleTxt.setText(getString(R.string.no_ticket));
//                else
//                    mTitleTxt.setText(getString(R.string.quantity_how_many));
//
//
//            } else {
//                mTitleTxt.setText(getString(R.string.how_much_for_service));
//            }
//        } else {
//            if (UploadScreen.mEntity.isGood) {
//                mTitleTxt.setText(getString(R.string.how_much_pay_item));
//            } else {
//                mTitleTxt.setText(getString(R.string.how_much_pay_service));
//            }
//        }
        mInputBox.setText("");
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mInputBox.getText().toString().trim().length() > 0 && Integer.valueOf(mInputBox.getText().toString().trim()) > 0) {
                    UploadScreen.mEntity.quantity = mInputBox.getText().toString().trim();

                    if (UploadScreen.mEntity.isSelling && UploadScreen.mEntity.isGood && !UploadScreen.mEntity.isDeliveryInPerson && UploadScreen.mEntity.category.equalsIgnoreCase(getString(R.string.ticket))) {
//                        ((BaseFragmentActivity) getActivity()).addFragment(new UploadItemServicePriceFragment(), 8);
                        ((BaseFragmentActivity) getActivity()).addFragment(new UploadItemServicePriceFragment(), 7);
                    } else if (UploadScreen.mEntity.isSelling == false) {
//                        ((BaseFragmentActivity) getActivity()).addFragment(new
//                                UploadItemServiceBriefDescFragment(), 7);
                        ((BaseFragmentActivity) getActivity()).addFragment(new
                                UploadItemServiceBriefDescFragment(), 6);
                    } else {
//                        ((BaseFragmentActivity) getActivity()).addFragment(new UploadGoodsServiceConditionFragment(), 7);
                        ((BaseFragmentActivity) getActivity()).addFragment(new UploadGoodsServiceConditionFragment(), 6);
                    }
                } else {
                    DialogManager.showBasicAlertDialog(getActivity(), mAlertMsg, UploadItemServiceQuantityFragment.this);
                }
            }
        });

    }

    private void setData() {

        if (UploadScreen.mEntity.isSelling) {
            //Selling
            //Goods
            mTitleTxt.setText(getString(R.string.quantity_how_many));
            mInputBox.setHint(getString(R.string.pg_quantity));
            mAlertMsg = getString(R.string.enter_pg_quantity);

            if (UploadScreen.mEntity.category.equalsIgnoreCase(getString(R.string.ticket))) {
                //Tickets
                mTitleTxt.setText(getString(R.string.tick_quantity));
                mInputBox.setHint(getString(R.string.type_here));
                mAlertMsg = getString(R.string.enter_tick_quantity);
            }

        } else {
            //Requesting
            //Goods
            mTitleTxt.setText(getString(R.string.reqpg_quantity));
            mInputBox.setHint(getString(R.string.type_here));
            mAlertMsg = getString(R.string.enter_tick_quantity);

        }
    }

    @Override
    public void onOkClick() {

    }
}
