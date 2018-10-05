package com.bridgellc.bridge.ui.upload;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
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
public class UploadItemServiceNameFragment extends Fragment implements DialogMangerOkCallback {

    private View view;
    private Button mNextBtn;
    private EditText mInputBox;
    private TextView mTitleTxt;
    private String mAlertMsg = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.upload_text_type, container, false);
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
        if (UploadScreen.mEntity.name != null && UploadScreen.mEntity.name.length() > 0) {
            mInputBox.setText(UploadScreen.mEntity.name);
        }
    }

    private void initComponents() {
        mNextBtn = (Button) view.findViewById(R.id.footer_btn);
        mInputBox = (EditText) view.findViewById(R.id.input_box);


        mInputBox.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        mInputBox.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
        mNextBtn.setText(getString(R.string.next));

        mTitleTxt = (TextView) view.findViewById(R.id.title_tv);
        mInputBox.setText("");

        Typeface mLightFont = TypefaceSingleton.getTypeface().getLightFont(getActivity());
        mNextBtn.setTypeface(mLightFont);
        mInputBox.setTypeface(mLightFont);
        mTitleTxt.setTypeface(mLightFont);

        setData();

//        if (UploadScreen.mEntity.isSelling) {
//            if (UploadScreen.mEntity.isGood) {
//                if (UploadScreen.mEntity.isDeliveryInPerson) {
//                    titleTv.setText(getString(R.string.what_is_your_item));
//                } else {
//                    if (UploadScreen.mEntity.category.equalsIgnoreCase(getString(R.string.ticket))) {
//                        titleTv.setText(getString(R.string.name_of_event));
//                    } else {
//                        //titleTv.setText(getString(R.string.what_is_your_service));
//                        titleTv.setText(getString(R.string.what_is_your_item));
//                    }
//                }
//            } else {
//
//                titleTv.setText(getString(R.string.what_is_your_service));
//            }
//        } else {
//            if (UploadScreen.mEntity.isGood) {
//                titleTv.setText(getString(R.string.item_looking_for));
//            } else {
//                titleTv.setText(getString(R.string.service_looking_for));
//            }
//        }

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mInputBox.getText().toString().trim().length() > 0) {
                    UploadScreen.mEntity.name = mInputBox.getText().toString().trim();
                    if (UploadScreen.mEntity.isSelling && UploadScreen.mEntity.isGood && !UploadScreen.mEntity.isDeliveryInPerson && UploadScreen.mEntity.category.equalsIgnoreCase(getString(R.string.ticket))) {
//                        ((BaseFragmentActivity) getActivity()).addFragment(new UploadItemTicketDateFragment(), 5);
                        ((BaseFragmentActivity) getActivity()).addFragment(new UploadItemTicketDateFragment(), 4);
                    } else {
//                        ((BaseFragmentActivity) getActivity()).addFragment(new UploadItemServicePriceFragment(), 5);
                        ((BaseFragmentActivity) getActivity()).addFragment(new UploadItemServicePriceFragment(), 4);

                    }
                } else {
                    DialogManager.showBasicAlertDialog(getActivity(), mAlertMsg, UploadItemServiceNameFragment.this);
                }
            }
        });

    }


    private void setData() {

        if (UploadScreen.mEntity.isSelling) {
            //Selling
            if (UploadScreen.mEntity.isGood) {
                //Goods
                mTitleTxt.setText(getString(R.string.what_is_your_item));
                mInputBox.setHint(getString(R.string.pg_title));
                mAlertMsg = getString(R.string.enter_pg_title);

                if (UploadScreen.mEntity.category.equalsIgnoreCase(getString(R.string.ticket))) {
                    //Tickets
                    mTitleTxt.setText(getString(R.string.name_of_event));
                    mInputBox.setHint(getString(R.string.type_here));
                    mAlertMsg = getString(R.string.enter_tick_title);
                }


            } else {
                //Services
                mTitleTxt.setText(getString(R.string.what_is_your_service));
                mInputBox.setHint(getString(R.string.type_wht_do));
                mAlertMsg = getString(R.string.enter_ps_title);
            }
        } else {
            //Requesting
            if (UploadScreen.mEntity.isGood) {
                //Goods
                mTitleTxt.setText(getString(R.string.item_looking_for));
                mInputBox.setHint(getString(R.string.i_need));
                mAlertMsg = getString(R.string.enter_reqpg_title);

            } else {
                //Services
                mTitleTxt.setText(getString(R.string.service_looking_for));
                mInputBox.setHint(getString(R.string.i_need));
                mAlertMsg = getString(R.string.enter_reqpg_title);

            }

        }
    }

    @Override
    public void onOkClick() {

    }
}
