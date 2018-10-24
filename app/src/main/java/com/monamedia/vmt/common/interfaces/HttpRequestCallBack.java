package com.monamedia.vmt.common.interfaces;

import com.android.volley.VolleyError;
import com.monamedia.vmt.model.HttpRequestDto;


public interface HttpRequestCallBack {
    void onSuccess(HttpRequestDto obj);

    void onFail(HttpRequestDto obj);

    void onVolleyError(VolleyError error);
}
