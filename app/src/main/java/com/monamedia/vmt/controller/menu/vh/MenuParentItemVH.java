package com.monamedia.vmt.controller.menu.vh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.monamedia.vmt.controller.menu.MenuAdapter;
import com.monamedia.vmt.databinding.MenuParentItemBinding;
import com.monamedia.vmt.model.MenuDto;


public class MenuParentItemVH extends RecyclerView.ViewHolder {
    private String TAG = "MenuParentItemVH";
    private MenuParentItemBinding binding;
    private Context context;
    private MenuAdapter adapter;

    public MenuParentItemVH(MenuParentItemBinding itemView, Context context, MenuAdapter adapter) {
        super(itemView.getRoot());
        this.context = context;
        this.binding = itemView;
        this.adapter = adapter;
    }

    public void bind(final MenuDto obj, final int position) {
        binding.setViewModel(obj);
        binding.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null) adapter.expand(obj, position);
            }
        });
    }

}
