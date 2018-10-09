package com.calix.calixgigaspireapp.output.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sibaprasad on 2/7/2018.
 */

public class EncryptionTypeResponse implements Serializable {

    private ArrayList<EncryptionTypeEntity> types = new ArrayList<>();

    public ArrayList<EncryptionTypeEntity> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<EncryptionTypeEntity> types) {
        this.types = types;
    }


}
