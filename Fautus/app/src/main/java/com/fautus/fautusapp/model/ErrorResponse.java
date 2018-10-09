package com.fautus.fautusapp.model;


import com.fautus.fautusapp.entity.ErrorEntity;

import java.io.Serializable;

public class ErrorResponse implements Serializable {

    private ErrorEntity error;

    public ErrorEntity getError() {
        return error;
    }

    public void setError(ErrorEntity error) {
        this.error = error;
    }

}
