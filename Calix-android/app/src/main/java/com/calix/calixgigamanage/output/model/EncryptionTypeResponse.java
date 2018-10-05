package com.calix.calixgigamanage.output.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sibaprasad on 2/7/2018.
 */

public class EncryptionTypeResponse implements Serializable {

    private ArrayList<EncryptionTypeEntity> types;

    public ArrayList<EncryptionTypeEntity> getTypes() {
        return types == null ? new ArrayList<EncryptionTypeEntity>() : types;
    }

    public void setTypes(ArrayList<EncryptionTypeEntity> types) {
        this.types = types;
    }


}
