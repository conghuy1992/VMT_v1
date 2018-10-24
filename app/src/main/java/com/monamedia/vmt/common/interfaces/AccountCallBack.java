package com.monamedia.vmt.common.interfaces;

import com.android.volley.VolleyError;
import com.monamedia.vmt.model.AccountDto;

public interface AccountCallBack {
    void onSuccess(AccountDto accountDto);

    void onFail(AccountDto obj);

    void onVolleyError(VolleyError error);
}
