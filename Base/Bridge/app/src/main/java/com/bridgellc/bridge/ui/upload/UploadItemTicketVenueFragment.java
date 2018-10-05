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
 * Created by admin on 5/11/2016.
 */
public class UploadItemTicketVenueFragment extends Fragment implements  DialogMangerOkCallback{

    private View view;
    private Button mNextBtn;
    TextView mTitleTxt;
    private EditText mInputBox;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.upload_ticket_venue, container, false);
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
        if (UploadScreen.mEntity.venue != null && UploadScreen.mEntity.venue.length() > 0) {
            mInputBox.setText(UploadScreen.mEntity.venue);
        }
    }

    private void initComponents() {
        mNextBtn = (Button) view.findViewById(R.id.footer_btn);
        mInputBox = (EditText) view.findViewById(R.id.input_box);
        mTitleTxt=(TextView)view.findViewById(R.id.title_tv);

        Typeface mLightFont = TypefaceSingleton.getTypeface().getLightFont(getActivity());
        mNextBtn.setTypeface(mLightFont);
        mInputBox.setTypeface(mLightFont);
        mTitleTxt.setTypeface(mLightFont);

        mNextBtn.setText(getString(R.string.next));

        mInputBox.setText("");
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mInputBox.getText().toString().trim().length() > 0) {
                    UploadScreen.mEntity.venue = mInputBox.getText().toString().trim();
//                    ((BaseFragmentActivity) getActivity()).addFragment(new UploadItemServiceQuantityFragment(), 7);
                    ((BaseFragmentActivity) getActivity()).addFragment(new UploadItemServiceQuantityFragment(), 6);

                } else {
                    DialogManager.showBasicAlertDialog(getActivity(), getString(R.string.enter_venue), UploadItemTicketVenueFragment.this);

                }
            }
        });

    }

    @Override
    public void onOkClick() {

    }
}

