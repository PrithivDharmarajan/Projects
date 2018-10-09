package com.smaat.spark.entity.outputEntity;

import java.io.Serializable;

/**
 * Created by Freeware Sys on 12/19/2016.
 */

public class AddressEntity implements Serializable {

    private String formatted_address;

    public String getFormatted_address() {
        if (formatted_address == null) {
            formatted_address = "";
        }
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

}
