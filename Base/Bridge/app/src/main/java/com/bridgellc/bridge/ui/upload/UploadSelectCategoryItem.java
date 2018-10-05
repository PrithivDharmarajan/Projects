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
public class UploadSelectCategoryItem extends Fragment {

    private View view;
    private TextView mTopTxt;
    private Button mAcademicBtn, mApparelBtn, mSocialBtn, mElectronicsBtn, mMiscBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.upload_fragment_select_category_item, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            UploadScreen.mEntity.category = ((Button) v).getText().toString().trim().toLowerCase(Locale.ENGLISH);

            if (UploadScreen.mEntity.category.equalsIgnoreCase(getString(R.string.ticket))) {
                if (UploadScreen.mEntity.isSelling) {
                    UploadScreen.mEntity.isDeliveryInPerson = false;
                } else {
                    UploadScreen.mEntity.isDeliveryInPerson = true;
                }
            } else {
                UploadScreen.mEntity.isDeliveryInPerson = true;
            }
//            ((BaseFragmentActivity) getActivity()).addFragment(new UploadItemServiceNameFragment(), 4);
            ((BaseFragmentActivity) getActivity()).addFragment(new UploadItemServiceNameFragment(), 3);
        }
    };

    private void initComponents() {

        mTopTxt = (TextView) view.findViewById(R.id.top_txt);
        mAcademicBtn = (Button) view.findViewById(R.id.academics);
        mApparelBtn = (Button) view.findViewById(R.id.apparel);
        mSocialBtn = (Button) view.findViewById(R.id.social);
        mElectronicsBtn = (Button) view.findViewById(R.id.electronics);
        mMiscBtn = (Button) view.findViewById(R.id.misc);

        Typeface mLightFont = TypefaceSingleton.getTypeface().getLightFont(getActivity());
        mTopTxt.setTypeface(mLightFont);
        mAcademicBtn.setTypeface(mLightFont);
        mApparelBtn.setTypeface(mLightFont);
        mSocialBtn.setTypeface(mLightFont);
        mElectronicsBtn.setTypeface(mLightFont);
        mMiscBtn.setTypeface(mLightFont);


        mAcademicBtn.setOnClickListener(onClickListener);
        mApparelBtn.setOnClickListener(onClickListener);
        mSocialBtn.setOnClickListener(onClickListener);
        mElectronicsBtn.setOnClickListener(onClickListener);
        mMiscBtn.setOnClickListener(onClickListener);


     //   if (UploadScreen.mEntity.isSelling && UploadScreen.mEntity.isGood && !UploadScreen.mEntity.isDeliveryInPerson) {
    //        mSocialBtn.setText(getString(R.string.ticket));
    //    } else {
     //       mSocialBtn.setText(getString(R.string.social));
     //   }
    }
}
