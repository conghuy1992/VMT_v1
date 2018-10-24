package com.monamedia.vmt.controller.notification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.monamedia.vmt.R;
import com.monamedia.vmt.WebViewActivity;
import com.monamedia.vmt.common.HttpRequest;
import com.monamedia.vmt.common.PrefManager;
import com.monamedia.vmt.common.Utils;
import com.monamedia.vmt.common.interfaces.NotificationCallBack;
import com.monamedia.vmt.common.interfaces.Statics;
import com.monamedia.vmt.databinding.NotificationItemBinding;
import com.monamedia.vmt.model.MenuDto;
import com.monamedia.vmt.model.NotificationDto;

import java.util.HashMap;
import java.util.Map;

public class NotificationItemVH extends RecyclerView.ViewHolder {
    private String TAG = "NotificationItemVH";
    private NotificationItemBinding binding;
    private Context context;
    private NotificationAdapter notificationAdapter;

    public NotificationItemVH(NotificationItemBinding itemView, Context context, NotificationAdapter notificationAdapter) {
        super(itemView.getRoot());
        this.context = context;
        this.binding = itemView;
        this.notificationAdapter = notificationAdapter;
    }

    public void bind(final NotificationDto obj, final int position) {
        binding.setViewModel(obj);
        Utils.setHtmlText(binding.tvNoti, obj.Message);
        binding.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!obj.Status.equals("2")) {
                    obj.Status = "2";
                    if (notificationAdapter != null)
                        notificationAdapter.notifyItemChanged(position);
                    UpdateNoti(obj, position);
                }
                openWeb(obj.Link);
            }
        });
    }

    private void openWeb(String link) {
        if (link == null || link.trim().length() == 0) {
            Utils.showMsg(context, "Can not get url");
            return;
        }
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(MenuDto.KEY, link);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
    }

    private void UpdateNoti(final NotificationDto obj, final int position) {
        Map<String, String> params = new HashMap<>();
        params.put("UID", "" + new PrefManager(context).userId());
        params.put("NotificationID", "" + obj.NotificationID);
        Log.d(TAG, "UpdateNoti params:" + new Gson().toJson(params));
        new HttpRequest().UpdateNoti(context,
                Statics.POST,
                params,
                Statics.UpdateNoti,
                new NotificationCallBack() {
                    @Override
                    public void onSuccess(NotificationDto notificationDto) {
//                        if (context == null) return;
//                        obj.Status = "2";
//                        if (notificationAdapter != null)
//                            notificationAdapter.notifyItemChanged(position);
                    }

                    @Override
                    public void onFail(NotificationDto accountDto) {
                    }

                    @Override
                    public void onVolleyError(VolleyError error) {
                    }
                });
    }
}
