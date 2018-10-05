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
public class UploadSelectDeliveryType extends Fragment {

    private View view;
    private TextView mTitleTxt;
    private Button mInPersonBtn, mElectronicallyBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.upload_fragment_delivery_type, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents();
    }

    private void initComponents() {
        mInPersonBtn = (Button) view.findViewById(R.id.selling);
        mElectronicallyBtn = (Button) view.findViewById(R.id.requesting);
        mTitleTxt=(TextView)view.findViewById(R.id.title_txt);

        Typeface mLightFont = TypefaceSingleton.getTypeface().getLightFont(getActivity());
        mInPersonBtn.setTypeface(mLightFont);
        mElectronicallyBtn.setTypeface(mLightFont);
        mTitleTxt.setTypeface(mLightFont);

        if(UploadScreen.mEntity.isSelling){
            mTitleTxt.setText(R.string.delivery_type_item);
        }else{

            mTitleTxt.setText(R.string.delivery_type_req);
        }
        mInPersonBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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


                if (UploadScreen.mEntity.isGood) {
                    ((BaseFragmentActivity) getActivity()).addFragment(new UploadSelectCategoryItem(), 3);
                } else {
                    ((BaseFragmentActivity) getActivity()).addFragment(new UploadSelectCategoryService(), 3);
                }
            }
        });
        mElectronicallyBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UploadScreen.mEntity.isDeliveryInPerson = false;

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

                if (UploadScreen.mEntity.isGood) {
                    ((UploadScreen) getActivity()).addFragment(new UploadSelectCategoryItem(), 3);
                } else {
                    ((UploadScreen) getActivity()).addFragment(new UploadSelectCategoryService(), 3);
                }
            }
        });
    }
}
