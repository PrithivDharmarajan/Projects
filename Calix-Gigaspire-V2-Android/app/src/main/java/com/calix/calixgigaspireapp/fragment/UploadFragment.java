package com.calix.calixgigaspireapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.adapter.parentcontrol.NetworkUsageAdapter;
import com.calix.calixgigaspireapp.main.BaseFragment;
import com.calix.calixgigaspireapp.output.model.DeviceEntity;
import com.calix.calixgigaspireapp.output.model.DeviceListResponse;
import com.calix.calixgigaspireapp.services.APIRequestHandler;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.InterfaceBtnCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UploadFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mDownloadRecyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_download_upload, container, false);

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);

        /*To focus on current fragment*/
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        /*Init view*/
        initView();

        return rootView;
    }


    private void initView() {
        /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

        /*Upload List API*/
        uploadListAPI();
    }


    /*API calls*/
    private void uploadListAPI() {
        APIRequestHandler.getInstance().deviceListAPICall("", this);
    }

    /*Set device list adapter*/
    private void setUploadListAdapter(ArrayList<DeviceListResponse> deviceListRes) {
        if (getActivity() != null) {
            mDownloadRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mDownloadRecyclerView.setNestedScrollingEnabled(false);
            mDownloadRecyclerView.setAdapter(new NetworkUsageAdapter(deviceListRes, false, getContext()));

        }
    }

    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof DeviceListResponse) {
            DeviceListResponse deviceListResponse = (DeviceListResponse) resObj;
            arrangeAdapterList(deviceListResponse.getDevices());

        }
    }

    /*Arrange adapter based on the device list */
    private void arrangeAdapterList(ArrayList<DeviceEntity> deviceListRes) {

        ArrayList<DeviceListResponse> arrangeDevicesArrLisRes = new ArrayList<>();
        LinkedHashMap<Integer, ArrayList<DeviceEntity>> phoneListArrLisRes = new LinkedHashMap<>();
        LinkedHashMap<Integer, ArrayList<DeviceEntity>> computerListArrLisRes = new LinkedHashMap<>();
        LinkedHashMap<Integer, ArrayList<DeviceEntity>> consoleListArrLisRes = new LinkedHashMap<>();
        LinkedHashMap<Integer, ArrayList<DeviceEntity>> storageListArrLisRes = new LinkedHashMap<>();
        LinkedHashMap<Integer, ArrayList<DeviceEntity>> printerListArrLisRes = new LinkedHashMap<>();
        LinkedHashMap<Integer, ArrayList<DeviceEntity>> televisionListArrLisRes = new LinkedHashMap<>();
        LinkedHashMap<Integer, ArrayList<DeviceEntity>> iotListArrLisRes = new LinkedHashMap<>();
        LinkedHashMap<Integer, ArrayList<DeviceEntity>> cameraListArrLisRes = new LinkedHashMap<>();
        LinkedHashMap<Integer, ArrayList<DeviceEntity>> unknownListArrLisRes = new LinkedHashMap<>();


        for (int posInt = 0; posInt < deviceListRes.size(); posInt++) {
            DeviceEntity deviceRes = deviceListRes.get(posInt);

            /*find the IOT device List*/

            switch (deviceRes.getType()) {
                case 1:
                    ArrayList<DeviceEntity> phoneLisRes = phoneListArrLisRes.containsKey(deviceRes.getType()) ? phoneListArrLisRes.get(deviceRes.getType()) : new ArrayList<DeviceEntity>();
                    phoneLisRes.add(deviceRes);
                    phoneListArrLisRes.put(1, phoneLisRes);
                    break;
                case 2:
                    ArrayList<DeviceEntity> computerListRes = computerListArrLisRes.containsKey(deviceRes.getType()) ? computerListArrLisRes.get(deviceRes.getType()) : new ArrayList<DeviceEntity>();
                    computerListRes.add(deviceRes);
                    computerListArrLisRes.put(2, computerListRes);
                    break;
                case 3:
                    ArrayList<DeviceEntity> consoleListRes = consoleListArrLisRes.containsKey(deviceRes.getType()) ? consoleListArrLisRes.get(deviceRes.getType()) : new ArrayList<DeviceEntity>();
                    consoleListRes.add(deviceRes);
                    consoleListArrLisRes.put(3, consoleListRes);
                    break;
                case 4:
                    ArrayList<DeviceEntity> storageListRes = storageListArrLisRes.containsKey(deviceRes.getType()) ? storageListArrLisRes.get(deviceRes.getType()) : new ArrayList<DeviceEntity>();
                    storageListRes.add(deviceRes);
                    storageListArrLisRes.put(4, storageListRes);
                    break;
                case 5:
                    ArrayList<DeviceEntity> printerListRes = printerListArrLisRes.containsKey(deviceRes.getType()) ? printerListArrLisRes.get(deviceRes.getType()) : new ArrayList<DeviceEntity>();
                    printerListRes.add(deviceRes);
                    printerListArrLisRes.put(5, printerListRes);
                    break;
                case 6:
                    ArrayList<DeviceEntity> televisionListRes = televisionListArrLisRes.containsKey(deviceRes.getType()) ? televisionListArrLisRes.get(deviceRes.getType()) : new ArrayList<DeviceEntity>();
                    televisionListRes.add(deviceRes);
                    televisionListArrLisRes.put(6, televisionListRes);
                    break;
                case 7:
                    ArrayList<DeviceEntity> iotListRes = iotListArrLisRes.containsKey(deviceRes.getType()) ? iotListArrLisRes.get(deviceRes.getType()) : new ArrayList<DeviceEntity>();
                    iotListRes.add(deviceRes);
                    iotListArrLisRes.put(7, iotListRes);
                    break;
                case 8:
                    ArrayList<DeviceEntity> cameraListRes = cameraListArrLisRes.containsKey(deviceRes.getType()) ? cameraListArrLisRes.get(deviceRes.getType()) : new ArrayList<DeviceEntity>();
                    cameraListRes.add(deviceRes);
                    cameraListArrLisRes.put(8, cameraListRes);
                    break;
                default:
                    ArrayList<DeviceEntity> unknownListRes = unknownListArrLisRes.containsKey(deviceRes.getType()) ? unknownListArrLisRes.get(deviceRes.getType()) : new ArrayList<DeviceEntity>();
                    unknownListRes.add(deviceRes);
                    unknownListArrLisRes.put(0, unknownListRes);
                    break;

            }


        }

        if (phoneListArrLisRes.get(1) != null) {
            DeviceListResponse phoneArrList = new DeviceListResponse();
            phoneArrList.setType(1);
            phoneArrList.setDevices(phoneListArrLisRes.get(1));
            arrangeDevicesArrLisRes.add(phoneArrList);
        }
        if (computerListArrLisRes.get(2) != null) {
            DeviceListResponse computerArrList = new DeviceListResponse();
            computerArrList.setType(2);
            computerArrList.setDevices(computerListArrLisRes.get(2));
            arrangeDevicesArrLisRes.add(computerArrList);
        }
        if (consoleListArrLisRes.get(3) != null) {
            DeviceListResponse consoleArrList = new DeviceListResponse();
            consoleArrList.setType(3);
            consoleArrList.setDevices(consoleListArrLisRes.get(3));
            arrangeDevicesArrLisRes.add(consoleArrList);
        }
        if (storageListArrLisRes.get(4) != null) {
            DeviceListResponse storageArrList = new DeviceListResponse();
            storageArrList.setType(4);
            storageArrList.setDevices(storageListArrLisRes.get(4));
            arrangeDevicesArrLisRes.add(storageArrList);
        }
        if (printerListArrLisRes.get(5) != null) {
            DeviceListResponse printerArrList = new DeviceListResponse();
            printerArrList.setType(5);
            printerArrList.setDevices(printerListArrLisRes.get(5));
            arrangeDevicesArrLisRes.add(printerArrList);
        }
        if (televisionListArrLisRes.get(6) != null) {
            DeviceListResponse televisionArrList = new DeviceListResponse();
            televisionArrList.setType(6);
            televisionArrList.setDevices(televisionListArrLisRes.get(6));
            arrangeDevicesArrLisRes.add(televisionArrList);
        }
        if (iotListArrLisRes.get(7) != null) {
            DeviceListResponse iotArrList = new DeviceListResponse();
            iotArrList.setType(7);
            iotArrList.setDevices(iotListArrLisRes.get(7));
            arrangeDevicesArrLisRes.add(iotArrList);
        }
        if (cameraListArrLisRes.get(8) != null) {
            DeviceListResponse cameraArrList = new DeviceListResponse();
            cameraArrList.setType(8);
            cameraArrList.setDevices(cameraListArrLisRes.get(8));
            arrangeDevicesArrLisRes.add(cameraArrList);
        }
        if (unknownListArrLisRes.get(0) != null) {
            DeviceListResponse unknownArrList = new DeviceListResponse();
            unknownArrList.setType(0);
            unknownArrList.setDevices(unknownListArrLisRes.get(0));
            arrangeDevicesArrLisRes.add(unknownArrList);
        }
        /*Set Adapter*/
        setUploadListAdapter(arrangeDevicesArrLisRes);
    }


    @Override
    public void onRequestFailure(Object resObj, Throwable t) {
        super.onRequestFailure(resObj, t);
        if (t instanceof IOException) {
            DialogManager.getInstance().showNetworkErrorPopup(getActivity(),
                    (t instanceof java.net.ConnectException ? getString(R.string.no_internet) : getString(R.string
                            .connect_time_out)), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {
                            uploadListAPI();
                        }
                    });
        }
    }
}
