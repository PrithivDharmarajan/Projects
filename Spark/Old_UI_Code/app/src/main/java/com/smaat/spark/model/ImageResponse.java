package com.smaat.spark.model;



import com.smaat.spark.entity.outputEntity.ImageEntity;

import java.io.Serializable;

/**
 * Created by admin on 11/28/2016.
 */

public class ImageResponse implements Serializable
{
        private String mode;
        private String api_name;
        private String response_code;
        private String message;
        private ImageEntity result;
        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getApi_name() {
            return api_name;
        }

        public void setApi_name(String api_name) {
            this.api_name = api_name;
        }

        public String getResponse_code() {
            return response_code;
        }

        public void setResponse_code(String response_code) {
            this.response_code = response_code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    public ImageEntity getResult() {
        return result;
    }

    public void setResult(ImageEntity result) {
        this.result = result;
    }
}
