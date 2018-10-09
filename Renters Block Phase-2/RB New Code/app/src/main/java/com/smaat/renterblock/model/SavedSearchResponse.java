package com.smaat.renterblock.model;


import com.smaat.renterblock.entity.LocalSavedSearchEntity;
import com.smaat.renterblock.entity.SavedSearchEntity;

import java.util.ArrayList;

public class SavedSearchResponse extends CommonResponse {
    private ArrayList<LocalSavedSearchEntity>result;

    public ArrayList<LocalSavedSearchEntity> getResult() {
        return result;
    }

    public void setResult(ArrayList<LocalSavedSearchEntity> result) {
        this.result = result;
    }
}
