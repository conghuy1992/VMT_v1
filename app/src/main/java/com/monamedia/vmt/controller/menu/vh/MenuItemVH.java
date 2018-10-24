package com.monamedia.vmt.controller.menu.vh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.monamedia.vmt.common.interfaces.MenuSelect;
import com.monamedia.vmt.databinding.MenuItemBinding;
import com.monamedia.vmt.model.MenuDto;


public class MenuItemVH extends RecyclerView.ViewHolder {
    private String TAG = "MenuItemVH";
    private MenuItemBinding binding;
    private Context context;
    private MenuSelect menuSelect;

    public MenuItemVH(MenuItemBinding itemView, Context context, MenuSelect menuSelect) {
        super(itemView.getRoot());
        this.context = context;
        this.binding = itemView;
        this.menuSelect = menuSelect;
    }

    public void bind(final MenuDto obj, final int position) {
        binding.setViewModel(obj);
        binding.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuSelect != null) menuSelect.onSelect(obj);
            }
        });
    }

}
