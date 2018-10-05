package com.bridgellc.bridge.ui.upload;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.main.BaseFragmentActivity;
import com.bridgellc.bridge.utils.TypefaceSingleton;

import java.util.Locale;

/**
 * Created by sys on 3/14/2016.
 */
public class UploadGoodsServiceConditionFragment extends Fragment {

    private View view;
    private Button mNewNoviceBtn, mUsedIntermediateBtn, mExpertBtn;
    private TextView mTitleTxt;
    private String mAlertMsg = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        if (UploadScreen.mEntity.isGood) {
            view = inflater.inflate(R.layout.upload_fragment_goods_condition, container, false);
        } else {
            view = inflater.inflate(R.layout.upload_fragment_service_condition, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents();
    }


    private void initComponents() {

        Typeface mLightFont = TypefaceSingleton.getTypeface().getLightFont(getActivity());

        mTitleTxt=(TextView)view.findViewById(R.id.top_txt);
        mNewNoviceBtn = (Button) view.findViewById(R.id.selling);
        mUsedIntermediateBtn = (Button) view.findViewById(R.id.requesting);

        mNewNoviceBtn.setTypeface(mLightFont);
        mUsedIntermediateBtn.setTypeface(mLightFont);
        mTitleTxt.setTypeface(mLightFont);

        mNewNoviceBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                UploadScreen.mEntity.condition = ((Button) v).getText().toString().trim()
                        .toLowerCase(Locale.ENGLISH);

                if (UploadScreen.mEntity.isGood) {
                    ((BaseFragmentActivity) getActivity()).addFragment(new
                            UploadItemServiceBriefDescFragment(), 7);
                } else {
                    if (UploadScreen.mEntity.isSelling) {

                        ((BaseFragmentActivity) getActivity()).addFragment(new
                                UploadItemServiceBriefDescFragment(), 6);

                    } else {
                        ((BaseFragmentActivity) getActivity()).addFragment(new
                                UploadItemServiceBriefDescFragment(), 7);

                    }
                }
            }
        });
        mUsedIntermediateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UploadScreen.mEntity.condition = ((Button) v).getText().toString().trim()
                        .toLowerCase(Locale.ENGLISH);
                if (UploadScreen.mEntity.isSelling && UploadScreen.mEntity.isGood) {

                    ((BaseFragmentActivity) getActivity()).addFragment(new
                            UploadItemServiceBriefDescFragment(), 7);
                } else {
                    if (!UploadScreen.mEntity.isSelling) {
                        UploadScreen.mEntity.desc = "abcd";

                        ((BaseFragmentActivity) getActivity()).addFragment(new
                                UploadPhotosFragment(), 6);
                    } else {

                        ((BaseFragmentActivity) getActivity()).addFragment(new
                                UploadItemServiceBriefDescFragment(), 6);
                    }
                }
            }
        });

        if (!UploadScreen.mEntity.isGood) {
            mExpertBtn = (Button) view.findViewById(R.id.expert);
            mExpertBtn.setTypeface(mLightFont);
            mExpertBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UploadScreen.mEntity.condition = ((Button) view).getText().toString().trim()
                            .toLowerCase(Locale.ENGLISH);
                    if (UploadScreen.mEntity.isSelling && UploadScreen.mEntity.isGood) {

                        ((BaseFragmentActivity) getActivity()).addFragment(new
                                UploadItemServiceBriefDescFragment(), 7);
                    } else {
                        if (!UploadScreen.mEntity.isSelling) {
                            UploadScreen.mEntity.desc = "abcd";

                            ((BaseFragmentActivity) getActivity()).addFragment(new
                                    UploadPhotosFragment(), 6);
                        } else {

                            ((BaseFragmentActivity) getActivity()).addFragment(new
                                    UploadItemServiceBriefDescFragment(), 6);
                        }
                    }
                }
            });
        }
    }


}
