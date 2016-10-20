package com.namhyun.pocketfimanager.data;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

public interface PocketFiService {
    @GET("/usr/kr/basic/state.asp")
    Call<String> requestState(@Header("Authorization") String authorization);

    @GET("/usr/kr/basic/RS_getWiMAXInfo.asp")
    Call<String> getWiMaxInfo(@Header("Authorization") String authorization);

    @GET("/usr/kr/basic/RS_getWiMAXInfo.asp")
    Observable<String> getState(@Header("Authorization") String authorization);

    @POST("/goform/mobile_submit")
    Call<String> sendMobileSubmit(@Header("Authorization") String authorization, @Body String body);
}
