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
public class UploadSelectCategoryService extends Fragment {

    private View view;

    private TextView mTopTxt;
    private Button mAcademicBtn, mMaintenanceBtn, mErrandsBtn, mMiscBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.upload_fragment_select_category_service, container, false);
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
//            ((BaseFragmentActivity) getActivity()).addFragment(new UploadItemServiceNameFragment(), 4);
            ((BaseFragmentActivity) getActivity()).addFragment(new UploadItemServiceNameFragment(), 3);
        }
    };

    private void initComponents() {
        mAcademicBtn = (Button) view.findViewById(R.id.academics);
        mMaintenanceBtn = (Button) view.findViewById(R.id.maintenance);


        mErrandsBtn = (Button) view.findViewById(R.id.errands);
        mMiscBtn = (Button) view.findViewById(R.id.misc);
        mTopTxt=(TextView)view.findViewById(R.id.top_txt);

        Typeface mLightFont = TypefaceSingleton.getTypeface().getLightFont(getActivity());
        mTopTxt.setTypeface(mLightFont);
        mAcademicBtn.setTypeface(mLightFont);
        mMaintenanceBtn.setTypeface(mLightFont);
        mErrandsBtn.setTypeface(mLightFont);
        mMiscBtn.setTypeface(mLightFont);


        mAcademicBtn.setOnClickListener(onClickListener);
        mMaintenanceBtn.setOnClickListener(onClickListener);

        mErrandsBtn.setOnClickListener(onClickListener);
        mMiscBtn.setOnClickListener(onClickListener);
    }
}
