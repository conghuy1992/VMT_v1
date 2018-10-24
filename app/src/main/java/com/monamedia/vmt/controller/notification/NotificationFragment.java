package com.monamedia.vmt.controller.notification;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.monamedia.vmt.R;
import com.monamedia.vmt.common.HttpRequest;
import com.monamedia.vmt.common.PrefManager;
import com.monamedia.vmt.common.Utils;
import com.monamedia.vmt.common.interfaces.NotificationCallBack;
import com.monamedia.vmt.common.interfaces.Statics;
import com.monamedia.vmt.databinding.NotificationFragmentBinding;
import com.monamedia.vmt.model.NotificationDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Android on 4/11/2018.
 */

public class NotificationFragment extends Fragment implements View.OnClickListener {
    private String TAG = "NotificationFragment";
    private NotificationFragmentBinding binding;
    private NotificationAdapter notificationAdapter;

    public NotificationFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.notification_fragment, container, false);
        View view = binding.getRoot();
        bind();
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == binding.content.btnReload) {
            GetAllNoti();
        }
    }

    public void bind() {
        notificationAdapter = new NotificationAdapter(getActivity(), new ArrayList<NotificationDto>());
        binding.content.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.content.recyclerView.setAdapter(notificationAdapter);
        binding.content.btnReload.setOnClickListener(this);
        GetAllNoti();
    }

    private void startLoading() {
        if (getActivity() == null) return;
        binding.content.progressBar.setVisibility(View.VISIBLE);
        binding.content.tvNodata.setVisibility(View.GONE);
        binding.content.btnReload.setVisibility(View.GONE);
    }

    private void stopLoading(String msg) {
        if (getActivity() == null) return;
        binding.content.progressBar.setVisibility(View.GONE);
        binding.content.tvNodata.setVisibility(View.GONE);
        binding.content.btnReload.setVisibility(View.GONE);
        if (msg != null && msg.length() > 0) Utils.showMsg(getActivity(), msg);
    }

    private void GetAllNoti() {
        startLoading();
        Map<String, String> params = new HashMap<>();
        params.put("UID", "" + new PrefManager(getActivity()).userId());
        Log.d(TAG, "GetAllNoti params:" + new Gson().toJson(params));
        new HttpRequest().GetAllNoti(getActivity(),
                Statics.POST,
                params,
                Statics.GetAllNoti,
                new NotificationCallBack() {
                    @Override
                    public void onSuccess(NotificationDto notificationDto) {
                        if (getActivity() == null) return;
//                        String msg = accountDto == null
//                                || accountDto.Message == null
//                                || accountDto.Message.length() == 0 ? "Success" : accountDto.Message;
                        stopLoading("");
                        notificationAdapter.update(notificationDto.ListNoti);
                    }

                    @Override
                    public void onFail(NotificationDto accountDto) {
                        if (getActivity() == null) return;
//                        String msg = accountDto == null
//                                || accountDto.Message == null
//                                || accountDto.Message.length() == 0 ? "fail" : accountDto.Message;
                        stopLoading("");
                        binding.content.tvNodata.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onVolleyError(VolleyError error) {
                        if (getActivity() == null) return;
                        stopLoading(error.toString());
                        binding.content.btnReload.setVisibility(View.VISIBLE);
                    }
                });
    }
}

