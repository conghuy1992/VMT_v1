package com.monamedia.vmt.controller.home;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.monamedia.vmt.MainActivity;
import com.monamedia.vmt.R;
import com.monamedia.vmt.common.HttpRequest;
import com.monamedia.vmt.common.Utils;
import com.monamedia.vmt.common.ViewPagerAdapter;
import com.monamedia.vmt.common.interfaces.MenuCallBack;
import com.monamedia.vmt.common.interfaces.Statics;
import com.monamedia.vmt.controller.buy.BuyHelpOrderFragment;
import com.monamedia.vmt.controller.profile.ProfileFragment;
import com.monamedia.vmt.databinding.HomeFragmentBinding;
import com.monamedia.vmt.model.MenuDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Android on 4/11/2018.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {
    private String TAG = "HomeFragment";
    private HomeFragmentBinding binding;
    private ViewPagerAdapter adapter;
    private int indexTab;
    private boolean isProfile;
    public HomeFragment() {

    }
    @SuppressLint("ValidFragment")
    public HomeFragment(boolean isProfile) {
        this.isProfile=isProfile;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.home_fragment, container, false);
        View view = binding.getRoot();
        bind();
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == binding.content.btnReload) {
            GetMenuMain();
        }
    }

    public void bind() {
        binding.content.btnReload.setOnClickListener(this);
        GetMenuMain();
    }

    private void setupViewPager(ViewPager viewPager, List<MenuDto> list) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
//        adapter.addFragment(new BuyHelpOrderFragment(), Utils.getMsg(getApplicationContext(), R.string.buy_help_order));
//        adapter.addFragment(new TransportOrderFragment(), Utils.getMsg(getApplicationContext(), R.string.transport_order));
//        adapter.addFragment(new PayOrderFragment(), Utils.getMsg(getApplicationContext(), R.string.pay_order));
//        adapter.addFragment(new ProfileFragment(), Utils.getMsg(getApplicationContext(), R.string.profile));
        for (MenuDto item : list) {
            if (item.GroupID == MenuDto.INFO)
                adapter.addFragment(new ProfileFragment(), Utils.getMsg(getActivity(), R.string.profile));
            else
                adapter.addFragment(new BuyHelpOrderFragment(item), item.ItemName);
        }
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(list.size() - 1);
        if(isProfile)
        {
            viewPager.setCurrentItem(list.size()-1);
        }
    }

    private void customTabLayout(TabLayout tabLayout, List<MenuDto> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).GroupID == MenuDto.INFO)
                tabLayout.getTabAt(i).setCustomView(Utils.createItem(getActivity(), Utils.getMsg(getActivity(), R.string.profile_enter)));
            else
                tabLayout.getTabAt(i).setCustomView(Utils.createItem(getActivity(), list.get(i).ItemName));
        }
//        tabLayout.getTabAt(0).setCustomView(Utils.createItem(getApplicationContext(), R.string.buy_help_order_enter));
//        tabLayout.getTabAt(1).setCustomView(Utils.createItem(getApplicationContext(), R.string.transport_order_enter));
//        tabLayout.getTabAt(2).setCustomView(Utils.createItem(getApplicationContext(), R.string.pay_order_enter));
//        tabLayout.getTabAt(3).setCustomView(Utils.createItem(getApplicationContext(), R.string.profile_enter));

        final ViewGroup test = (ViewGroup) (tabLayout.getChildAt(0));
        int tabLen = test.getChildCount();
        for (int i = 0; i < tabLen; i++) {
            View v = test.getChildAt(i);
            v.setPadding(0, 0, 0, 0);
        }
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                indexTab = tab.getPosition();
                Log.d(TAG, "position:" + indexTab);
                setTitle(getTitle(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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

    private void GetMenuMain() {
        startLoading();
        Map<String, String> params = new HashMap<>();
//        params.put("UID", "" + new PrefManager(context).userId());
//        Log.d(TAG, "GetMenuMain params:" + new Gson().toJson(params));
        new HttpRequest().GetMenuMain(getActivity(),
                Statics.POST,
                params,
                Statics.GetMenuMain,
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

    private String getTitle(int index) {
//        if (index == 1) return Utils.getMsg(getApplicationContext(), R.string.transport_order);
//        if (index == 2) return Utils.getMsg(getApplicationContext(), R.string.pay_order);
//        if (index == 3) return Utils.getMsg(getApplicationContext(), R.string.profile);
//        return Utils.getMsg(getApplicationContext(), R.string.buy_help_order);

        try {
            return adapter.getPageTitle(index).toString();
        } catch (Exception e) {
            return "";
        }
    }

    private void setTitle(String msg) {
        if (getActivity() != null && getActivity() instanceof MainActivity)
            ((MainActivity) getActivity()).setTitle(msg);
    }

    private void displayInfo(MenuDto menuDto) {
        MenuDto obj = new MenuDto();
        obj.GroupID = MenuDto.INFO;
        obj.ItemName = Utils.getMsg(getActivity(), R.string.profile_enter);
        menuDto.ListMenu.add(obj);
        setTitle(menuDto.ListMenu.get(0).ItemName);

        setupViewPager(binding.viewPager, menuDto.ListMenu);
        binding.tabs.setupWithViewPager(binding.viewPager);
        customTabLayout(binding.tabs, menuDto.ListMenu);
    }

    public boolean canGoBack() {
        return adapter.getRegisteredFragment(binding.viewPager.getCurrentItem()) != null
                && adapter.getRegisteredFragment(binding.viewPager.getCurrentItem()) instanceof BuyHelpOrderFragment
                && ((BuyHelpOrderFragment) adapter.getRegisteredFragment(binding.viewPager.getCurrentItem())).canGoBack();
    }

    public void goBack() {
        if (canGoBack())
            ((BuyHelpOrderFragment) adapter.getRegisteredFragment(binding.viewPager.getCurrentItem())).goBack();
    }

    public void profileTab() {
        binding.viewPager.setCurrentItem(adapter.getCount() - 1);
    }

}

