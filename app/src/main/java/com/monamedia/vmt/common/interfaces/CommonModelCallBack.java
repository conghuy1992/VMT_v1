package com.monamedia.vmt.common.interfaces;

import com.android.volley.VolleyError;
import com.monamedia.vmt.model.CommonModel;

public interface CommonModelCallBack {
    void onSuccess(CommonModel accountDto);

    void onFail(CommonModel obj);

    void onVolleyError(VolleyError error);
}
