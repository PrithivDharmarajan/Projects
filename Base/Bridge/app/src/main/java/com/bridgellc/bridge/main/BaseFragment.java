package com.bridgellc.bridge.main;

import android.support.v4.app.Fragment;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;

import java.net.ConnectException;

import retrofit.RetrofitError;

public class BaseFragment extends Fragment {

    public void onRequestSuccess(Object responseObj) {

    }


    public void onRequestFailure(RetrofitError errorCode) {

        if (errorCode.getCause() instanceof ConnectException ||errorCode.getCause() instanceof java.net.UnknownHostException) {
            DialogManager.showBasicAlertDialog(getActivity(), getString(R.string.no_internet), new DialogMangerOkCallback() {
                @Override
                public void onOkClick() {

                }
            });
        } else if (errorCode.getCause() instanceof java.net.SocketTimeoutException) {
            DialogManager.showBasicAlertDialog(getActivity(),
                    getString(R.string.connect_time_out), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {

                        }
                    });

        } else if (errorCode.getCause() instanceof retrofit.converter.ConversionException) {
            DialogManager.showBasicAlertDialog(getActivity(),
                    getString(R.string.serv_con_exce), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {

                        }
                    });
        } else {
            DialogManager.showBasicAlertDialog(getActivity(),
                    getString(R.string.serv_not_res), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {

                        }
                    });

        }

    }

}
