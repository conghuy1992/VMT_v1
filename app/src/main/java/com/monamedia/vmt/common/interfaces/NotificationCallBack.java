package com.monamedia.vmt.common.interfaces;

import com.android.volley.VolleyError;
import com.monamedia.vmt.model.NotificationDto;

public interface NotificationCallBack {
    void onSuccess(NotificationDto NotificationDto);

    void onFail(NotificationDto obj);

    void onVolleyError(VolleyError error);
}
