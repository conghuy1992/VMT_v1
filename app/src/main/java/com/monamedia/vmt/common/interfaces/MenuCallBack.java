package com.monamedia.vmt.common.interfaces;

import com.android.volley.VolleyError;
import com.monamedia.vmt.model.MenuDto;

public interface MenuCallBack {
    void onSuccess(MenuDto menuDto);

    void onFail(MenuDto obj);

    void onVolleyError(VolleyError error);
}
