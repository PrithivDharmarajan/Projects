package com.bridgellc.bridge.ui.upload;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.main.BaseFragmentActivity;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;
import com.bridgellc.bridge.utils.TypefaceSingleton;

/**
 * Created by sys on 3/14/2016.
 */
public class UploadItemServiceBriefDescFragment extends Fragment implements DialogMangerOkCallback {

    private View view;
    private Button mNextBtn;
    private EditText mInputBox;
    private TextView mTitleTxt;
    private String mAlertMsg = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.upload_text_type_large, container, false);
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
        if (UploadScreen.mEntity.desc != null && UploadScreen.mEntity.desc.length() > 0) {
            mInputBox.setText(UploadScreen.mEntity.desc);
        }
    }

    private void initComponents() {
        mNextBtn = (Button) view.findViewById(R.id.footer_btn);
        mInputBox = (EditText) view.findViewById(R.id.input_box);
        mInputBox.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});


        mNextBtn.setText(getString(R.string.next));

        mTitleTxt = (TextView) view.findViewById(R.id.title_tv);

        Typeface mLightFont = TypefaceSingleton.getTypeface().getLightFont(getActivity());
        mNextBtn.setTypeface(mLightFont);
        mInputBox.setTypeface(mLightFont);
        mTitleTxt.setTypeface(mLightFont);

        setData();
//        if (UploadScreen.mEntity.isSelling) {
//            if (UploadScreen.mEntity.isGood) {
//                if (!UploadScreen.mEntity.isDeliveryInPerson&&UploadScreen.mEntity.category.equalsIgnoreCase(getString(R.string.ticket))) {
//                    mTitleTxt.setText(getString(R.string.desc_ticket));
//                } else {
//                    mTitleTxt.setText(getString(R.string.brief_desc_item));
//                }
//            } else {
//                mTitleTxt.setText(getString(R.string.brief_desc_offering));
//            }
//        } else {
//            if (UploadScreen.mEntity.isGood) {
//                mTitleTxt.setText(getString(R.string.brief_desc_item));
//            } else {
//                mTitleTxt.setText(getString(R.string.brief_desc_offering));
//            }
//        }
        mInputBox.setText("");
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mInputBox.getText().toString().trim().length() > 0) {
                    UploadScreen.mEntity.desc = mInputBox.getText().toString().trim();

                    if (UploadScreen.mEntity.isSelling && UploadScreen.mEntity.isGood) {

                        if (!UploadScreen.mEntity.isDeliveryInPerson && UploadScreen.mEntity.category.equalsIgnoreCase(getString(R.string.ticket))) {
//                            ((BaseFragmentActivity) getActivity()).addFragment(new UploadPhotosFragment(), 10);
                            ((BaseFragmentActivity) getActivity()).addFragment(new UploadPhotosFragment(), 9);
                        } else {
//                            ((BaseFragmentActivity) getActivity()).addFragment(new UploadPhotosFragment(), 9);
                            ((BaseFragmentActivity) getActivity()).addFragment(new UploadPhotosFragment(), 8);
                        }
//                    } else if (!UploadScreen.mEntity.isSelling && UploadScreen.mEntity.isGood) {
//
//                        ((BaseFragmentActivity) getActivity()).addFragment(new UploadPhotosFragment(), 9);
                    } else if (!UploadScreen.mEntity.isSelling && !UploadScreen.mEntity.isGood) {
//                        ((BaseFragmentActivity) getActivity()).addFragment(new UploadPhotosFragment(), 7);
                        ((BaseFragmentActivity) getActivity()).addFragment(new UploadPhotosFragment(), 6);
//                    }else if (!UploadScreen.mEntity.isSelling && UploadScreen.mEntity.isGood) {
//                        ((BaseFragmentActivity) getActivity()).addFragment(new UploadPhotosFragment(), 8);
                    } else {
                        //Selling Service
                        if (GlobalMethods.getStringValue(getActivity(), AppConstants.PARTNER).equalsIgnoreCase(getString(R.string.two))) {

//                            ((BaseFragmentActivity) getActivity()).addFragment(new UploadItemServicePhnumFragment(), 8);
                            ((BaseFragmentActivity) getActivity()).addFragment(new UploadItemServicePhnumFragment(), 7);
                        } else {
//                            ((BaseFragmentActivity) getActivity()).addFragment(new UploadPhotosFragment(), 8);
                            ((BaseFragmentActivity) getActivity()).addFragment(new UploadPhotosFragment(), 7);
                        }
                    }
                } else {
                    DialogManager.showBasicAlertDialog(getActivity(), mAlertMsg, UploadItemServiceBriefDescFragment.this);


                }
            }

        });

    }


    private void setData() {

        if (UploadScreen.mEntity.isSelling) {
            //Selling
            if (UploadScreen.mEntity.isGood) {
                //Goods
                mTitleTxt.setText(getString(R.string.pg_desc));
                mInputBox.setHint(getString(R.string.pg_desc_plch));
                mAlertMsg = getString(R.string.enter_pg_desc);

                if (UploadScreen.mEntity.category.equalsIgnoreCase(getString(R.string.ticket))) {
                    //Tickets
                    mInputBox.setHint(getString(R.string.tick_desc_plch));
                    mAlertMsg = getString(R.string.enter_tick_desc);
                }

            } else {
                //Services
                mTitleTxt.setText(getString(R.string.ps_desc));
                mInputBox.setHint(getString(R.string.ps_desc_plch));
                mAlertMsg = getString(R.string.enter_tick_desc);

            }
        } else {
            //Requesting
            if (UploadScreen.mEntity.isGood) {
                //Goods

                mTitleTxt.setText(getString(R.string.reqpg_desc));
                mInputBox.setHint(getString(R.string.reqpg_desc_plch));
                mAlertMsg = getString(R.string.enter_tick_desc);
            } else {

                if (UploadScreen.mEntity.isDeliveryInPerson) {
                    //Services
                    mTitleTxt.setText(getString(R.string.reqps_desc));
                    mInputBox.setHint(getString(R.string.reqpg_desc_plch));
                    mAlertMsg = getString(R.string.enter_tick_desc);
                } else {
                    mTitleTxt.setText(getString(R.string.reqes_desc));
                    mInputBox.setHint(getString(R.string.reqpg_desc_plch));
                    mAlertMsg = getString(R.string.enter_tick_desc);
                }
            }

        }

    }

    @Override
    public void onOkClick() {

    }
}
