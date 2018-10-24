package com.monamedia.vmt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.monamedia.vmt.common.HttpRequest;
import com.monamedia.vmt.common.PrefManager;
import com.monamedia.vmt.common.Utils;
import com.monamedia.vmt.common.ViewPagerAdapter;
import com.monamedia.vmt.common.interfaces.AccountCallBack;
import com.monamedia.vmt.common.interfaces.MenuCallBack;
import com.monamedia.vmt.common.interfaces.NotificationCallBack;
import com.monamedia.vmt.common.interfaces.Statics;
import com.monamedia.vmt.controller.buy.BuyHelpOrderFragment;
import com.monamedia.vmt.controller.home.HomeFragment;
import com.monamedia.vmt.controller.menu.MenuFragment;
import com.monamedia.vmt.controller.menu.MenuProfileFragment;
import com.monamedia.vmt.controller.order.OrderFragment;
import com.monamedia.vmt.controller.pay.PayOrderFragment;
import com.monamedia.vmt.controller.transport.TransportOrderFragment;
import com.monamedia.vmt.controller.profile.ProfileFragment;
import com.monamedia.vmt.model.AccountDto;
import com.monamedia.vmt.model.MenuDto;
import com.monamedia.vmt.model.NotificationDto;
import com.monamedia.vmt.view.confirmDialog.ConfirmDialog;
import com.monamedia.vmt.view.confirmDialog.ConfirmDialogCallBack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {
    private String TAG = "MainActivity";
    private Toolbar toolbar;
    private TextView btnLogout;
    private FrameLayout menuCart, menuNotification;
    private TextView tvTitle;
    private Context context;
    private AccountDto accountDto;
    private TextView tvCurrency;
    private View content;
    private Button btnReloadLogout;
    private TextView tvNodataLogout;
    private ProgressBar progressBarLogout;
    private MenuProfileFragment menuProfileFragment;
    private ImageView ivNewNoti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;
        accountDto = (AccountDto) getIntent().getSerializableExtra(AccountDto.KEY);
        bind();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onClick(View v) {
        if (v == btnLogout) {
            logout();
        } else if (v == menuCart) {
            Utils.showMsg(this, "CART");
        } else if (v == menuNotification) {
            notification();
        }
//        else if (v == btnReload) {
//            GetMenuMain();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetAllNoti();
    }

    private void notification() {
        Intent intent = new Intent(this, NotificationActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
    }

    private void bind() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        homeFragment(false);

        Utils.addFragment(getSupportFragmentManager(),
                "",
                R.id.menuList,
                new MenuFragment(accountDto));

        menuProfileFragment = new MenuProfileFragment();
        Utils.addFragment(getSupportFragmentManager(),
                "",
                R.id.profile,
                menuProfileFragment);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvCurrency = (TextView) findViewById(R.id.tvCurrency);
        btnLogout = (TextView) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);

        menuCart = (FrameLayout) findViewById(R.id.menuCart);
        menuCart.setOnClickListener(this);
        menuNotification = (FrameLayout) findViewById(R.id.menuNotification);
        menuNotification.setOnClickListener(this);

        content = (View) findViewById(R.id.content);
        btnReloadLogout = (Button) content.findViewById(R.id.btnReload);
        btnReloadLogout.setVisibility(View.GONE);
        tvNodataLogout = (TextView) content.findViewById(R.id.tvNodata);
        tvNodataLogout.setVisibility(View.GONE);
        progressBarLogout = (ProgressBar) content.findViewById(R.id.progressBar);
        progressBarLogout.setVisibility(View.GONE);

        ivNewNoti = (ImageView) findViewById(R.id.ivNewNoti);
        ivNewNoti.setVisibility(View.INVISIBLE);
    }

    public void homeFragment(boolean isProfile) {
//        Utils.addFragment(getSupportFragmentManager(),
//                "",
//                R.id.fr,
//                new HomeFragment());
        addFragment(new HomeFragment(isProfile));
    }

    public void addFragment(Fragment fragment) {
        Utils.addFragment(getSupportFragmentManager(),
                "",
                R.id.fr,
                fragment);
    }

    public void setTextCurrency(String msg) {
        if (msg == null) msg = "";
        if (tvCurrency != null) tvCurrency.setText("" + msg);
    }

    public void setTitle(String msg) {
//        if (toolbar != null) toolbar.setTitle(msg);
        if (tvTitle != null) tvTitle.setText(msg);
    }

    private void logout() {
        new ConfirmDialog(this,
                Utils.getMsg(this, R.string.app_name),
                Utils.getMsg(this, R.string.are_you_sure_logout),
                Utils.getMsg(this, R.string.yes),
                Utils.getMsg(this, R.string.no),
                new ConfirmDialogCallBack() {
                    @Override
                    public void onConfirm() {
                        LogOut();
                    }

                    @Override
                    public void onCancel() {

                    }
                }).create().show();
    }

    private void startLoadingLogout() {
        if (context == null) return;
        progressBarLogout.setVisibility(View.VISIBLE);
        btnLogout.setEnabled(false);
    }

    private void stopLoadingLogout(String msg) {
        if (context == null) return;
        progressBarLogout.setVisibility(View.GONE);
        btnLogout.setEnabled(true);
        if (msg != null && msg.length() > 0) Utils.showMsg(context, msg);
    }

    private void LogOut() {
        startLoadingLogout();
        Map<String, String> params = new HashMap<>();
        params.put("UID", "" + new PrefManager(context).userId());
        params.put("DeviceToken", "" + new PrefManager(context).getRegId());
        Log.d(TAG, "LogOut params:" + new Gson().toJson(params));
        new HttpRequest().LogOut(context,
                Statics.POST,
                params,
                Statics.LogOut,
                new AccountCallBack() {
                    @Override
                    public void onSuccess(AccountDto menuDto) {
                        if (context == null) return;
//                        String msg = accountDto == null
//                                || accountDto.Message == null
//                                || accountDto.Message.length() == 0 ? "Success" : accountDto.Message;
                        launchLogin();
                    }

                    @Override
                    public void onFail(AccountDto accountDto) {
                        if (context == null) return;
                        String msg = accountDto == null
                                || accountDto.Message == null
                                || accountDto.Message.length() == 0 ? "fail" : accountDto.Message;
                        stopLoadingLogout(msg);
                    }

                    @Override
                    public void onVolleyError(VolleyError error) {
                        if (context == null) return;
                        stopLoadingLogout(error.toString());
                    }
                });
    }

    private void launchLogin() {
        new PrefManager(this).setRemember(false);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
        finish();
    }

    private Handler myHandler = new Handler();
    private Runnable myRunnable = new Runnable() {
        public void run() {
            mIsExit = false;
        }
    };
    private boolean mIsExit;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
//        else if (adapter.getRegisteredFragment(viewPager.getCurrentItem()) != null
//                && adapter.getRegisteredFragment(viewPager.getCurrentItem()) instanceof BuyHelpOrderFragment
//                && ((BuyHelpOrderFragment) adapter.getRegisteredFragment(viewPager.getCurrentItem())).canGoBack()) {
//            ((BuyHelpOrderFragment) adapter.getRegisteredFragment(viewPager.getCurrentItem())).goBack();
//        }
        else if (canGoBack()) {
            goBack();
        } else {
            if (!isTaskRoot()) {
                super.onBackPressed();
            } else {
                if (mIsExit) {
                    super.onBackPressed();
                } else {
                    // press 2 times to exit app feature
                    this.mIsExit = true;
                    Utils.showMsg(this, R.string.quit);
                    myHandler.postDelayed(myRunnable, 2000);
                }
            }
        }
    }

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fr);
    }

    private void goBack() {
        if (canGoBack()) {
            Fragment fm = getCurrentFragment();
            if (fm != null && fm instanceof HomeFragment) {
                ((HomeFragment) fm).goBack();
            } else if (fm != null && fm instanceof OrderFragment) {
                ((OrderFragment) fm).goBack();
            }
        }
    }

    private boolean canGoBack() {
        Fragment fm = getCurrentFragment();
        if (fm != null && fm instanceof HomeFragment) {
            if (((HomeFragment) fm).canGoBack()) return true;
        } else if (fm != null && fm instanceof OrderFragment) {
            if (((OrderFragment) fm).canGoBack()) return true;
        }
        return false;
    }

    public void refreshProfile() {
        if (menuProfileFragment != null) menuProfileFragment.refreshProfile();
    }

    public void closeDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    public void profileTab() {
//        viewPager.setCurrentItem(adapter.getCount() - 1);
        Fragment fm = getCurrentFragment();
        if (fm != null && fm instanceof HomeFragment) {
            // switch to page profile
            ((HomeFragment) fm).profileTab();
        } else {
            homeFragment(true);
        }
    }

    private void GetAllNoti() {
        Map<String, String> params = new HashMap<>();
        params.put("UID", "" + new PrefManager(context).userId());
        Log.d(TAG, "GetAllNoti params:" + new Gson().toJson(params));
        new HttpRequest().GetAllNoti(context,
                Statics.POST,
                params,
                Statics.GetAllNoti,
                new NotificationCallBack() {
                    @Override
                    public void onSuccess(NotificationDto notificationDto) {
                        if (context == null) return;
                        Log.d(TAG, "GetAllNoti success");
                        if (ivNewNoti != null) {
                            boolean flag = false;
                            for (NotificationDto obj : notificationDto.ListNoti) {
                                if (!obj.Status.equals("2")) {
                                    flag = true;
                                    break;
                                }
                            }
                            if (flag) {
                                ivNewNoti.setVisibility(View.VISIBLE);
                            } else {
                                ivNewNoti.setVisibility(View.INVISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFail(NotificationDto accountDto) {
                        if (context == null) return;
                        if (ivNewNoti != null) ivNewNoti.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onVolleyError(VolleyError error) {
                    }
                });
    }

}
