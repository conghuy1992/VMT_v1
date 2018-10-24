package com.monamedia.vmt.controller.menu;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.monamedia.vmt.R;
import com.monamedia.vmt.common.interfaces.MenuSelect;
import com.monamedia.vmt.controller.menu.vh.MenuFunctionItemVH;
import com.monamedia.vmt.controller.menu.vh.MenuItemVH;
import com.monamedia.vmt.controller.menu.vh.MenuParentItemVH;
import com.monamedia.vmt.databinding.MenuFunctionItemBinding;
import com.monamedia.vmt.databinding.MenuItemBinding;
import com.monamedia.vmt.databinding.MenuParentItemBinding;
import com.monamedia.vmt.model.MenuDto;

import java.util.List;


public class MenuAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<MenuDto> MenuDtoList;
    private String TAG = "MenuAdapter";
    private MenuSelect menuSelect;

    public List<MenuDto> getList() {
        return MenuDtoList;
    }

    public void update(List<MenuDto> MenuDtoList) {
        this.MenuDtoList = MenuDtoList;
        this.notifyDataSetChanged();
    }

    public MenuAdapter(Context context, List<MenuDto> MenuDtoList, MenuSelect menuSelect) {
        this.context = context;
        this.MenuDtoList = MenuDtoList;
        this.menuSelect = menuSelect;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == MenuDto.PARENT) {
            MenuParentItemBinding binding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.menu_parent_item, parent, false);
            vh = new MenuParentItemVH(binding, context, this);
        } else if (viewType == MenuDto.MENU) {
            MenuFunctionItemBinding binding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.menu_function_item, parent, false);
            vh = new MenuFunctionItemVH(binding, context, menuSelect);
        } else {
            MenuItemBinding binding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.menu_item, parent, false);
            vh = new MenuItemVH(binding, context, menuSelect);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MenuDto MenuDto = MenuDtoList.get(position);
        if (holder instanceof MenuParentItemVH) {
            ((MenuParentItemVH) holder).bind(MenuDto, position);
        } else if (holder instanceof MenuItemVH) {
            ((MenuItemVH) holder).bind(MenuDto, position);
        } else if (holder instanceof MenuFunctionItemVH) {
            ((MenuFunctionItemVH) holder).bind(MenuDto, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return MenuDtoList.get(position).type;
    }

    @Override
    public int getItemCount() {
        return MenuDtoList.size();
    }

    public void expand(MenuDto obj, int index) {
        if (obj.isExistChild) {
            obj.isExistChild = false;
            notifyItemChanged(index);
            for (int j = index + 1; j < MenuDtoList.size(); j++) {
                if (MenuDtoList.get(j).type == MenuDto.CHILDREN) {
                    MenuDtoList.remove(j);
                    notifyItemRemoved(j);
                    notifyItemRangeChanged(j, getItemCount());
                    j--;
                } else {
                    break;
                }
            }
            return;
        }

        if (index < MenuDtoList.size() && MenuDtoList.get(index).ListMenu != null && MenuDtoList.get(index).ListMenu.size() > 0) {
            int j = index;
            for (MenuDto dto : MenuDtoList.get(index).ListMenu) {
                j++;
                MenuDtoList.add(j, dto);
                obj.isExistChild = true;
                notifyItemInserted(j);
                notifyItemRangeChanged(j, getItemCount());
            }
            notifyItemChanged(index);
        }
    }
}
