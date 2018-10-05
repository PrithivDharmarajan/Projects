package com.bridgellc.bridge.ui.upload;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class UploadItemServicePhnumFragment extends Fragment {
    private View view;
    private Button mNextBtn;
    private EditText mInputBox;
    private TextView mTitleTxt;
    private String mAlertMsg = "";

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
        if (UploadScreen.mEntity.phone_number != null && UploadScreen.mEntity.phone_number.length() > 0) {
            mInputBox.setText(UploadScreen.mEntity.phone_number);
        }
    }

    private void initComponents() {
        mNextBtn = (Button) view.findViewById(R.id.footer_btn);
        mInputBox = (EditText) view.findViewById(R.id.input_box);
        mTitleTxt = (TextView) view.findViewById(R.id.title_tv);
        mTitleTxt.setText(getString(R.string.wht_ph));

        Typeface mLightFont = TypefaceSingleton.getTypeface().getLightFont(getActivity());
        mNextBtn.setTypeface(mLightFont);
        mInputBox.setTypeface(mLightFont);
        mTitleTxt.setTypeface(mLightFont);

        mInputBox.setInputType(InputType.TYPE_CLASS_PHONE);
        mNextBtn.setText(getString(R.string.next));



        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInputBox.getText().toString().trim().length() > 0) {
                    UploadScreen.mEntity.phone_number = mInputBox.getText().toString().trim();
//                    ((BaseFragmentActivity) getActivity()).addFragment(new UploadItemServiceWebsite(), 9);
                    ((BaseFragmentActivity) getActivity()).addFragment(new UploadItemServiceWebsite(), 8);
                } else {
                    DialogManager.showBasicAlertDialog(getActivity(), getString(R.string.enter_phone), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {

                        }
                    });
                }
            }
        });
    }
}
