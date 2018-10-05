package com.bridgellc.bridge.ui.upload;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
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
public class UploadSelectSellingRequesting extends Fragment {

    private View view;
    private Button mSellingBtn, mRequestingBtn;
    private TextView mTopTxt;
    private static boolean isSelling = false;
    private static boolean isRequesting = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.upload_fragment_one, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents();
    }


    private void initComponents() {
        mSellingBtn = (Button) view.findViewById(R.id.selling);
        mRequestingBtn = (Button) view.findViewById(R.id.requesting);
        mTopTxt = (TextView) view.findViewById(R.id.top_txt);


        Typeface mLightFont = TypefaceSingleton.getTypeface().getLightFont(getActivity());
        mSellingBtn.setTypeface(mLightFont);
        mRequestingBtn.setTypeface(mLightFont);
        mTopTxt.setTypeface(mLightFont);


        mSellingBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                if (GlobalMethods.getStringValue(getActivity(), AppConstants.BANK_DETAILS).equalsIgnoreCase(getString(R.string.one))) {
                UploadScreen.mEntity.isSelling = true;
                ((BaseFragmentActivity) getActivity()).addFragment(new UploadSelectGoodsService(), 1);
//                } else {
//                    isSelling = true;
//                    AppConstants.BANK_ACC_DET_BACK_SCREEN = AppConstants.SUCCESS_CODE;
//                    ((BaseFragmentActivity) getActivity()).nextScreen(BankAccDetails.class, false);
//                }
            }
        });
        mRequestingBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                if (GlobalMethods.getStringValue(getActivity(), AppConstants.CARD_DETAILS).equalsIgnoreCase(getString(R.string.one))) {

                UploadScreen.mEntity.isSelling = false;
                ((BaseFragmentActivity) getActivity()).addFragment(new UploadSelectGoodsService(), 1);
//                } else {
//
//                    isRequesting = true;
//                    AppConstants.CARD_DET_BACK_SCREEN = AppConstants.SUCCESS_CODE;
//                    ((BaseFragmentActivity) getActivity()).nextScreen(AddPayCard.class, false);
//                }
            }
        });


    }


//    public static void resumCall(Context mContext){
//        if (isSelling == true) {
//            isSelling = false;
//            if (GlobalMethods.getStringValue(mContext, AppConstants.BANK_DETAILS).equalsIgnoreCase(mContext.getString(R.string.one))) {
//                UploadScreen.mEntity.isSelling = true;
//                ((BaseFragmentActivity)mContext).addFragment(new UploadSelectGoodsService(), 1);
//            }
//        }
//        if (isRequesting == true) {
//            isRequesting = false;
//            if (GlobalMethods.getStringValue(mContext, AppConstants.CARD_DETAILS).equalsIgnoreCase(mContext.getString(R.string.one))) {
//                UploadScreen.mEntity.isSelling = false;
//                ((BaseFragmentActivity) mContext).addFragment(new UploadSelectGoodsService(), 1);
//            }
//        }
//    }


    private FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getFragmentManager();

                if (manager != null) {
                    if (manager.getBackStackEntryCount() >= 1) {
                        String topOnStack = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1).getName();
                        Log.i("TOP ON BACK STACK", topOnStack);
                    }
                }
            }
        };

        return result;
    }


}
