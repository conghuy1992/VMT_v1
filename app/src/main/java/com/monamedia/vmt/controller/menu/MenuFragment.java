package com.monamedia.vmt.controller.menu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.monamedia.vmt.ChangePasswordActivity;
import com.monamedia.vmt.MainActivity;
import com.monamedia.vmt.R;
import com.monamedia.vmt.TestActivity;
import com.monamedia.vmt.common.CommonWebViewFragment;
import com.monamedia.vmt.common.HttpRequest;
import com.monamedia.vmt.common.PrefManager;
import com.monamedia.vmt.common.Utils;
import com.monamedia.vmt.common.interfaces.MenuCallBack;
import com.monamedia.vmt.common.interfaces.MenuSelect;
import com.monamedia.vmt.common.interfaces.Statics;
import com.monamedia.vmt.controller.order.OrderFragment;
import com.monamedia.vmt.controller.order.SiteOrderFragment;
import com.monamedia.vmt.databinding.MenuFragmentBinding;
import com.monamedia.vmt.model.AccountDto;
import com.monamedia.vmt.model.MenuDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Android on 4/11/2018.
 */

public class MenuFragment extends Fragment implements View.OnClickListener {
    private MenuFragmentBinding binding;
    private String TAG = "MenuFragment";
    private MenuAdapter menuAdapter;
    private AccountDto accountDto;

    public MenuFragment() {

    }

    @SuppressLint("ValidFragment")
    public MenuFragment(AccountDto accountDto) {
        this.accountDto = accountDto;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.menu_fragment, container, false);
        View view = binding.getRoot();
        bind();
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == binding.content.btnReload) {
            GetAllMenu();
        }
    }

    public void bind() {
        menuAdapter = new MenuAdapter(getActivity(), new ArrayList<MenuDto>(), new MenuSelect() {
            @Override
            public void onSelect(MenuDto obj) {
                handlerSelect(obj);
            }
        });
        binding.content.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.content.recyclerView.setAdapter(menuAdapter);
        binding.content.btnReload.setOnClickListener(this);
        GetAllMenu();
    }

    private void startLoading() {
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

    private void GetAllMenu() {
        startLoading();
        Map<String, String> params = new HashMap<>();
//        params.put("UID", "" + new PrefManager(getActivity()).userId());
//        Log.d(TAG, "GetAllMenu params:" + new Gson().toJson(params));
        new HttpRequest().GetAllMenu(getActivity(),
                Statics.POST,
                params,
                Statics.GetAllMenu,
                new MenuCallBack() {
                    @Override
                    public void onSuccess(MenuDto menuDto) {
                        if (getActivity() == null) return;
//                        String msg = accountDto == null
//                                || accountDto.Message == null
//                                || accountDto.Message.length() == 0 ? "Success" : accountDto.Message;
                        stopLoading("");
                        displayInfo(menuDto);
                    }

                    @Override
                    public void onFail(MenuDto accountDto) {
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

    private void displayInfo(MenuDto menuDto) {
        List<MenuDto> list = new ArrayList<>();
        list.add(Utils.HOME());

        for (MenuDto obj : menuDto.ListMenu) {
            if (obj.Parent == 0) {
                if (obj.GroupID != 0) obj.type = MenuDto.PARENT;
                else obj.type = MenuDto.MENU;
                obj.ListMenu = new ArrayList<>();
                list.add(obj);
            } else {
                if (list.size() > 0) {
                    int GroupID = list.get(list.size() - 1).GroupID;
                    if (GroupID == obj.GroupID)
                        list.get(list.size() - 1).ListMenu.add(obj);
                }
            }
        }
        // ACCOUNT_MANAGER
        list.add(Utils.ACCOUNT_MANAGER(getActivity(), accountDto));

        // expand all item
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).ListMenu != null && list.get(i).ListMenu.size() > 0) {
                list.get(i).isExistChild = true;
                for (MenuDto m : list.get(i).ListMenu) {
                    i++;
                    list.add(i, m);
                }
            }
        }

        menuAdapter.update(list);
    }

    private void handlerSelect(MenuDto menuDto) {
        if (menuDto.ShowType == 2) { // for parent is menu : ex : ORDER
//            Intent intent = new Intent(getActivity(), TestActivity.class);
//            startActivity(intent);
//            getActivity().overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
            addFragment(new OrderFragment());
            return;
        }
        if (menuDto.GroupID == MenuDto.ACCOUNT_INFO) {
            ((MainActivity) getActivity()).closeDrawer();
            ((MainActivity) getActivity()).profileTab();
        } else if (menuDto.GroupID == MenuDto.CHANGE_PASS) {
            ((MainActivity) getActivity()).closeDrawer();
            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
        } else if (menuDto.GroupID == MenuDto.HOME) {
            ((MainActivity) getActivity()).closeDrawer();
            ((MainActivity) getActivity()).homeFragment(false);
        } else {
//            if (menuDto.Link != null && menuDto.Link.length() > 0) {
//                ((MainActivity) getActivity()).closeDrawer();
//                Intent intent = new Intent(getActivity(), WebViewActivity.class);
//                intent.putExtra(MenuDto.KEY, menuDto.Link + new PrefManager(getActivity()).userId());
//                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
//            }
            openWeb(menuDto.Link);
        }
    }

    private void openWeb(String link) {
        if (link == null || link.trim().length() == 0) {
            Utils.showMsg(getActivity(), "Can not get url");
            return;
        }
//        ((MainActivity) getActivity()).closeDrawer();
//        Intent intent = new Intent(getActivity(), WebViewActivity.class);
//        intent.putExtra(MenuDto.KEY, link + new PrefManager(getActivity()).userId());
//        startActivity(intent);
//        getActivity().overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
        addFragment(new CommonWebViewFragment(link + new PrefManager(getActivity()).userId()));
    }

    private void addFragment(Fragment fragment) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).closeDrawer();
            ((MainActivity) getActivity()).addFragment(fragment);
        }
    }
}

