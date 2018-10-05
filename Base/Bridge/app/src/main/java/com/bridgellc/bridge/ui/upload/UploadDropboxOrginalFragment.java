package com.bridgellc.bridge.ui.upload;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.main.BaseFragmentActivity;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.TypefaceSingleton;
//import com.dropbox.chooser.android.DbxChooser;

/**
 * Created by admin on 4/28/2016.
 */
public class UploadDropboxOrginalFragment extends Fragment implements DialogMangerOkCallback {

    private View view;
    private Button mNextBtn;
    private EditText mInputBox;
    private ImageView mDropBoxImage;
    public static final int DX_CHOOSER_REQUEST = 0;  // You can change this if needed
    private TextView mOrTxt;
    private Button mChooserButton;
//    private DbxChooser mChooser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.upload_drop_box, container, false);
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
        if (UploadScreen.mEntity.dropBoxUrlOrginal != null && UploadScreen.mEntity.dropBoxUrlOrginal.length() > 0) {
            mInputBox.setText(UploadScreen.mEntity.dropBoxUrlOrginal);
        }
    }

    private void initComponents() {


        mNextBtn = (Button) view.findViewById(R.id.footer_btn);
        mInputBox = (EditText) view.findViewById(R.id.input_box);
        mOrTxt = (TextView) view.findViewById(R.id.text);

        mInputBox.setHint(R.string.enter_url_hint);

        Typeface mLightFont = TypefaceSingleton.getTypeface().getLightFont(getActivity());
        mNextBtn.setTypeface(mLightFont);
        mInputBox.setTypeface(mLightFont);
        mOrTxt.setTypeface(mLightFont);


//        mChooser = new DbxChooser(AppConstants.DROPBOX_APP_KEY);

        mDropBoxImage = (ImageView) view.findViewById(R.id.drop_box_icon);
        mDropBoxImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mChooser.forResultType(DbxChooser.ResultType.PREVIEW_LINK)
//                        .launch(getActivity(), DX_CHOOSER_REQUEST);
            }
        });

        mNextBtn.setText(getString(R.string.next));
        mInputBox.setText("");
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mInputBox.getText().toString().trim().length() > 0) {
                    UploadScreen.mEntity.dropBoxUrlOrginal = mInputBox.getText().toString().trim();
                    UploadScreen.mEntity.isDropBoxUrl = false;
//                    ((BaseFragmentActivity) getActivity()).addFragment(new UploadPublishFragment(), 12);
                    ((BaseFragmentActivity) getActivity()).addFragment(new UploadPublishFragment(), 11);
                } else {
                    DialogManager.showBasicAlertDialog(getActivity(), getString(R.string.enter_url), UploadDropboxOrginalFragment.this);

                }

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DX_CHOOSER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
//                DbxChooser.Result result = new DbxChooser.Result(data);
//                Log.e("main", "Link to selected file: " + result.getLink());

//                mInputBox.setText("" + result.getLink());

                // Handle the result
            } else {
                // Failed or was cancelled by the user.
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onOkClick() {

    }
}
