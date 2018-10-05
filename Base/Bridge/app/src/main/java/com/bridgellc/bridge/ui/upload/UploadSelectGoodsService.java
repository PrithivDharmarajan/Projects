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

/**
 * Created by sys on 3/14/2016.
 */
public class UploadSelectGoodsService extends Fragment {

    private View view;
    private TextView mHeaderTxt;
    private Button mGoodsBtn, mServicesBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.upload_fragment_two, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents();
    }

    private void initComponents() {

        mHeaderTxt = (TextView) view.findViewById(R.id.header_txt);
        mGoodsBtn = (Button) view.findViewById(R.id.selling);
        mServicesBtn = (Button) view.findViewById(R.id.requesting);

        Typeface mLightFont = TypefaceSingleton.getTypeface().getLightFont(getActivity());
        mHeaderTxt.setTypeface(mLightFont);
        mGoodsBtn.setTypeface(mLightFont);
        mServicesBtn.setTypeface(mLightFont);

        if( UploadScreen.mEntity.isSelling){
            mHeaderTxt.setText(getActivity().getString(R.string.what_selling));
        }else{
            mHeaderTxt.setText(getActivity().getString(R.string.what_requeting));

        }

//        mGoodsBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                UploadScreen.mEntity.isGood = true;
//
//                ((BaseFragmentActivity) getActivity()).addFragment(new UploadSelectDeliveryType(), 2);
//
//            }
//        });
//        mServicesBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                UploadScreen.mEntity.isGood = false;
//
//                ((BaseFragmentActivity) getActivity()).addFragment(new UploadSelectDeliveryType(), 2);
//
//            }
//        });




        mGoodsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UploadScreen.mEntity.isGood = true;
                UploadScreen.mEntity.isDeliveryInPerson = true;

                UploadScreen.mEntity.category = "";
                UploadScreen.mEntity.name = "";
                UploadScreen.mEntity.price = "";
                UploadScreen.mEntity.quantity = "";
                UploadScreen.mEntity.condition = "";
                UploadScreen.mEntity.desc = "";
                UploadScreen.mEntity.bitmap1 = null;
                UploadScreen.mEntity.bitmap2 = null;
                UploadScreen.mEntity.bitmap3 = null;
                UploadScreen.mEntity.imagePath1 = "";
                UploadScreen.mEntity.imagePath2 = "";
                UploadScreen.mEntity.imagePath3 = "";
                UploadScreen.mEntity.dropBoxUrlPreview = "";
                UploadScreen.mEntity.dropBoxUrlOrginal = "";


//                if (UploadScreen.mEntity.isGood) {
                    ((BaseFragmentActivity) getActivity()).addFragment(new UploadSelectCategoryItem(), 2);
//                } else {
//                    ((BaseFragmentActivity) getActivity()).addFragment(new UploadSelectCategoryService(), 2);
//                }
            }
        });
        mServicesBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                UploadScreen.mEntity.isGood = false;
                UploadScreen.mEntity.isDeliveryInPerson = true;

                UploadScreen.mEntity.category = "";
                UploadScreen.mEntity.name = "";
                UploadScreen.mEntity.price = "";
                UploadScreen.mEntity.quantity = "";
                UploadScreen.mEntity.condition = "";
                UploadScreen.mEntity.desc = "";
                UploadScreen.mEntity.bitmap1 = null;
                UploadScreen.mEntity.bitmap2 = null;
                UploadScreen.mEntity.bitmap3 = null;
                UploadScreen.mEntity.imagePath1 = "";
                UploadScreen.mEntity.imagePath2 = "";
                UploadScreen.mEntity.imagePath3 = "";
                UploadScreen.mEntity.dropBoxUrlPreview = "";
                UploadScreen.mEntity.dropBoxUrlOrginal = "";

//                if (UploadScreen.mEntity.isGood) {
//                    ((UploadScreen) getActivity()).addFragment(new UploadSelectCategoryItem(), 2);
//                } else {
                    ((UploadScreen) getActivity()).addFragment(new UploadSelectCategoryService(), 2);
//                }
            }
        });
    }
}
