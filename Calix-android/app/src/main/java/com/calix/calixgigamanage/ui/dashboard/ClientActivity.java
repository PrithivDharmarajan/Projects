package com.calix.calixgigamanage.ui.dashboard;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;

import com.calix.calixgigamanage.main.BaseActivity;

import java.net.InetAddress;
import java.util.Arrays;

public class ClientActivity extends BaseActivity {

    private String SERVICE_NAME = "iothub";
    //    private String SERVICE_TYPE = "_http._tcp.local";
////    private String SERVICE_TYPE = "_iothub._tcp.local";
    private String SERVICE_TYPE = "_iothub._tcp.";

    private InetAddress hostAddress;
    private int hostPort;
    private NsdManager mNsdManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set content view and other stuff

        // NSD Stuff
        mNsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);
//        mNsdManager.discoverServices(SERVICE_TYPE,
//                NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
    }


    @Override
    protected void onPause() {
        if (mNsdManager != null) {
            mNsdManager.stopServiceDiscovery(mDiscoveryListener);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNsdManager != null) {
            mNsdManager.discoverServices(
                    SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
        }

    }

    @Override
    protected void onDestroy() {
        if (mNsdManager != null) {
            mNsdManager.stopServiceDiscovery(mDiscoveryListener);
        }
        super.onDestroy();
    }


    NsdManager.DiscoveryListener mDiscoveryListener = new NsdManager.DiscoveryListener() {

        // Called as soon as service discovery begins.
        @Override
        public void onDiscoveryStarted(String regType) {
            sysOut("Service discovery started");
        }

        @Override
        public void onServiceFound(NsdServiceInfo service) {
            // A service was found! Do something with it.
            sysOut("Service discovery success : " + service);
            sysOut("Host = " + service.getServiceName());
            sysOut("port = " + String.valueOf(service.getPort()));

            // connect to the service and obtain serviceInfo
            mNsdManager.resolveService(service, mResolveListener);

        }

        @Override
        public void onServiceLost(NsdServiceInfo service) {
            // When the network service is no longer available.
            // Internal bookkeeping code goes here.
            sysOut("service lost" + service);
        }

        @Override
        public void onDiscoveryStopped(String serviceType) {
            sysOut("Discovery stopped: " + serviceType);
        }

        @Override
        public void onStartDiscoveryFailed(String serviceType, int errorCode) {
            sysOut("Discovery failed: Error code:" + errorCode);
            mNsdManager.stopServiceDiscovery(this);
        }

        @Override
        public void onStopDiscoveryFailed(String serviceType, int errorCode) {
            sysOut("Discovery failed: Error code:" + errorCode);
            mNsdManager.stopServiceDiscovery(this);
        }
    };

    NsdManager.ResolveListener mResolveListener = new NsdManager.ResolveListener() {

        @Override
        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
            // Called when the resolve fails. Use the error code to debug.
            sysOut("Resolve failed " + errorCode);
        }

        @Override
        public void onServiceResolved(NsdServiceInfo serviceInfo) {
            sysOut("Resolve Succeeded. " + serviceInfo);


            // Obtain port and IP
            sysOut("serviceInfo---"+serviceInfo.toString());
            sysOut("getServiceName---"+serviceInfo.getServiceName());
            sysOut("getServiceType---"+serviceInfo.getServiceType());
            sysOut("getHost---"+serviceInfo.getHost());
            sysOut("getHostName---"+serviceInfo.getHost().getHostName());
            sysOut("getHostAddress---"+serviceInfo.getHost().getHostAddress());
            sysOut("getAddress---"+ Arrays.toString(serviceInfo.getHost().getAddress()));
            sysOut("getCanonicalHostName---"+serviceInfo.getHost().getCanonicalHostName());
            sysOut("getPort---"+serviceInfo.getPort());

        }
    };

}
