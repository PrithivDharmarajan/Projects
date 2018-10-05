package com.calix.calixgigamanage.output.model;

import java.io.Serializable;

/**
 * Created by user on 3/5/2018.
 */

public class IOTRemoveDeviceResponse implements Serializable {

    private IOTRemoveDeviceEntity Data;

    public IOTRemoveDeviceEntity getData() {
        return Data == null ? new IOTRemoveDeviceEntity() : Data;
    }

    public void setData(IOTRemoveDeviceEntity data) {
        Data = data;
    }

}
