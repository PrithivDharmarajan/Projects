package com.smaat.renterblock.util;

import com.smaat.renterblock.model.PropertyDetailResponse;

public interface WebserviceCallbackInterface {

	void onResponseError(String errorCode);

	void onRequestSuccess(PlaceRespone obj);
	
	void onRequestSuccess(PropertyDetailResponse obj);

}
