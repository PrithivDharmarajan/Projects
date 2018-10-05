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
public class UploadItemServiceCondFragment extends Fragment {

    private View view;
    private Button mNextBtn;
    private EditText mInputBox;

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
        if(UploadScreen.mEntity.condition != null && UploadScreen.mEntity.condition.length() > 0){
            mInputBox.setText(UploadScreen.mEntity.condition);
        }
    }

    private void initComponents()
    {
        mNextBtn = (Button)view.findViewById(R.id.footer_btn);
        mInputBox = (EditText) view.findViewById(R.id.input_box);

        Typeface mLightFont = TypefaceSingleton.getTypeface().getLightFont(getActivity());
        mNextBtn.setTypeface(mLightFont);
        mInputBox.setTypeface(mLightFont);

        mInputBox.setText("");
        TextView titleTv = (TextView)view.findViewById(R.id.title_tv);

        mNextBtn.setText(getString(R.string.next));

        if(UploadScreen.mEntity.isGood)
        {
            titleTv.setText(getString(R.string.brief_desc_item_cond));
        }


        mNextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mInputBox.getText().toString().trim().length() > 0) {
             UploadScreen.mEntity.condition = mInputBox.getText().toString().trim();
//                ((BaseFragmentActivity) getActivity()).addFragment(new UploadItemServiceBriefDescFragment(), 7);
                    ((BaseFragmentActivity) getActivity()).addFragment(new UploadItemServiceBriefDescFragment(), 6);
            }else{
                DialogManager.showBasicAlertDialog(getActivity(), getString(R.string.enter_text), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                    }
                });
            }
            }
        });

    }
}
