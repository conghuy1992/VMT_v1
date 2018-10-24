package com.monamedia.vmt.common.interfaces;

import com.android.volley.VolleyError;

public interface GetResponseCallBack {
    void onResponse(String response);
    void onVolleyError(VolleyError error);
}
