package com.monamedia.vmt.controller.notification;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.monamedia.vmt.R;
import com.monamedia.vmt.databinding.NotificationItemBinding;
import com.monamedia.vmt.model.NotificationDto;

import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<NotificationDto> NotificationDtoList;
    private String TAG = "NotificationAdapter";

    public List<NotificationDto> getList() {
        return NotificationDtoList;
    }

    public void update(List<NotificationDto> NotificationDtoList) {
        this.NotificationDtoList = NotificationDtoList;
        this.notifyDataSetChanged();
    }

    public NotificationAdapter(Context context, List<NotificationDto> NotificationDtoList) {
        this.context = context;
        this.NotificationDtoList = NotificationDtoList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == 1) {
            vh = null;
        } else {
            NotificationItemBinding binding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.notification_item, parent, false);
            vh = new NotificationItemVH(binding, context,this);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NotificationDto obj = NotificationDtoList.get(position);
        if (holder instanceof NotificationItemVH) {
            ((NotificationItemVH) holder).bind(obj, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return NotificationDtoList.get(position).type;
    }

    @Override
    public int getItemCount() {
        return NotificationDtoList.size();
    }

}
