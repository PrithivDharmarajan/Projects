package com.bridgellc.bridge.ui.upload;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.main.BaseFragmentActivity;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;
import com.bridgellc.bridge.utils.TypefaceSingleton;

/**
 * Created by sys on 3/14/2016.
 */
public class UploadItemServicePriceFragment extends Fragment implements DialogMangerOkCallback {

    private View view;
    private Button mNextBtn;
    private EditText mInputBox;
    private TextView mTitleTxt, mDollTxt;
    private String mAlertMsg = "";
    private String current = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.upload_price_screen, container, false);
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
        if (UploadScreen.mEntity.price != null && UploadScreen.mEntity.price.length() > 0) {
            mInputBox.setText(UploadScreen.mEntity.price);
        }
    }

    private void initComponents() {
        mNextBtn = (Button) view.findViewById(R.id.footer_btn);
        mInputBox = (EditText) view.findViewById(R.id.input_box);
        mDollTxt = (TextView) view.findViewById(R.id.doll_sym_txt);

        InputFilter filter = new InputFilter() {
            final int maxDigitsBeforeDecimalPoint = 10;
            final int maxDigitsAfterDecimalPoint = 2;

            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                StringBuilder builder = new StringBuilder(dest);
                builder.replace(dstart, dend, source
                        .subSequence(start, end).toString());
                String pricePattern;
                if (UploadScreen.mEntity.isSelling && UploadScreen.mEntity.category
                        .equalsIgnoreCase(getString(R.string.ticket))) {
                    pricePattern = "(([0-9]{1})([0-9]{0," + (maxDigitsBeforeDecimalPoint - 1) + "})?)?(\\.[0-9]{0," + maxDigitsAfterDecimalPoint + "})?";
                } else {

                    pricePattern = "(([1-9]{1})([0-9]{0," + (maxDigitsBeforeDecimalPoint - 1) + "})?)?(\\.[0-9]{0," + maxDigitsAfterDecimalPoint + "})?";

                }
                if (!builder.toString().matches(
                        pricePattern)) {
                    if (source.length() == 0)
                        return dest.subSequence(dstart, dend);
                    return "";
                }

                return null;

            }
        };

            mInputBox.setFilters(new InputFilter[]{filter});

        mNextBtn.setText(getString(R.string.next));

        mInputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String mt = mInputBox.getText().toString().trim();
                if (mt.length() > 0) {
                    mDollTxt.setVisibility(View.VISIBLE);
                } else {
                    mDollTxt.setVisibility(View.GONE);
                }

            }
        });
        mTitleTxt = (TextView) view.findViewById(R.id.title_tv);


        Typeface mLightFont = TypefaceSingleton.getTypeface().getLightFont(getActivity());
        mNextBtn.setTypeface(mLightFont);
        mInputBox.setTypeface(mLightFont);
        mTitleTxt.setTypeface(mLightFont);

        setData();
        mInputBox.setText("");
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mInputBox.getText().toString().trim().length() > 0 && editTxtValidation()) {
                    UploadScreen.mEntity.price = mInputBox.getText().toString().trim();
                    if (UploadScreen.mEntity.isGood && UploadScreen.mEntity.isSelling) {
                        if (!UploadScreen.mEntity.isDeliveryInPerson && UploadScreen.mEntity.category.equalsIgnoreCase(getString(R.string.ticket))) {
//                            ((BaseFragmentActivity) getActivity()).addFragment(new
//                                    UploadItemServiceBriefDescFragment(), 9);
                            ((BaseFragmentActivity) getActivity()).addFragment(new
                                    UploadItemServiceBriefDescFragment(), 8);
                        } else {
//                            ((BaseFragmentActivity) getActivity()).addFragment(new UploadItemServiceQuantityFragment(), 6);
                            ((BaseFragmentActivity) getActivity()).addFragment(new UploadItemServiceQuantityFragment(), 5);
                        }
                    } else if (UploadScreen.mEntity.isSelling == false) {
                        if (!UploadScreen.mEntity.isGood) {
//                            ((BaseFragmentActivity) getActivity()).addFragment(new
//                                    UploadItemServiceBriefDescFragment(), 6);
                            ((BaseFragmentActivity) getActivity()).addFragment(new
                                    UploadItemServiceBriefDescFragment(), 5);
                        } else {
//                            ((BaseFragmentActivity) getActivity()).addFragment(new UploadItemServiceQuantityFragment(), 6);
                            ((BaseFragmentActivity) getActivity()).addFragment(new UploadItemServiceQuantityFragment(), 5);

                        }
                    } else {

//                        ((BaseFragmentActivity) getActivity()).addFragment(new UploadGoodsServiceConditionFragment(), 6);
                        ((BaseFragmentActivity) getActivity()).addFragment(new UploadGoodsServiceConditionFragment(), 5);
                    }
                } else {
                    DialogManager.showBasicAlertDialog(getActivity(), mAlertMsg, UploadItemServicePriceFragment.this);
                }
            }
        });

    }

    private boolean editTxtValidation() {

        boolean tickVal;

        if (UploadScreen.mEntity.isSelling && UploadScreen.mEntity.category
                .equalsIgnoreCase(getString(R.string.ticket))) {
            tickVal = true;
        } else {
            String mval = mInputBox.getText().toString().trim();
            if (mval.contains(".")) {
                String mV[] = mval.split("\\.");
                if (mV.length == 2) {
                    mval = GlobalMethods.afterTwoPointVal(mval);
                } else {
                    mval = mval + ".00";
                }
            } else {
                mval = mval + ".00";
            }
            tickVal = (!mval.equalsIgnoreCase(AppConstants.FAILURE_CODE) && !mval.equalsIgnoreCase(GlobalMethods.afterTwoPointVal(AppConstants.FAILURE_CODE)));
            mInputBox.setText(mval);
            mInputBox.setSelection(mval.length());

        }

        return tickVal;
    }

    private void setData() {

        if (UploadScreen.mEntity.isSelling) {
            //Selling
            if (UploadScreen.mEntity.isGood) {
                //Goods
                mTitleTxt.setText(getString(R.string.how_much_for_item));
                mInputBox.setHint(getString(R.string.pg_price));
                mAlertMsg = getString(R.string.enter_pg_price);

                if (UploadScreen.mEntity.category.equalsIgnoreCase(getString(R.string.ticket))) {
                    //Tickets
                    mTitleTxt.setText(getString(R.string.tick_price));
                    mInputBox.setHint(getString(R.string.type_here));
                }


            } else {
                //Services
                mTitleTxt.setText(getString(R.string.how_much_for_service));
                mInputBox.setHint(getString(R.string.pg_price));
                mAlertMsg = getString(R.string.enter_pg_price);

            }
        } else {
            //Requesting
            //Goods
            mTitleTxt.setText(getString(R.string.how_much_for_willing));
            mInputBox.setHint(getString(R.string.pg_price));
            mAlertMsg = getString(R.string.enter_pg_price);


        }

    }

    @Override
    public void onOkClick() {

    }
}
