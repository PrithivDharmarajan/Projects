package com.smaat.renterblock.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import com.google.android.gms.maps.model.LatLng;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class AddressUtil {

    public static LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address = new ArrayList<>();
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            if (address.size() > 0) {
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();
                p1 = new LatLng(location.getLatitude(), location.getLongitude());
            }


        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public static String getAddressFromLatLng(Double latitude, Double longitude, Context context) {

        String addressStr = "", cityStr = "", stateStr = "", countryStr = "";

        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && addresses.size() > 0 && addresses.get(0).getAddressLine(0) != null) {
                addressStr = addresses.get(0).getAddressLine(0);

                if (!addressStr.isEmpty() && addresses.get(0).getAdminArea() != null
                        && addresses.get(0).getCountryName() != null && addresses.get(0).getLocality() != null
                        && !addresses.get(0).getLocality().isEmpty() && !addresses.get(0).getAdminArea().isEmpty() &&
                        !addresses.get(0).getCountryName().isEmpty()) {
                    cityStr = addresses.get(0).getLocality();
                    stateStr = addresses.get(0).getAdminArea();
                    countryStr = addresses.get(0).getCountryName();
                    addressStr = cityStr;
                }

                return addressStr;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return addressStr;
        } finally {
            return addressStr;
        }

    }

}
