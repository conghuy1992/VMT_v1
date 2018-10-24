package com.monamedia.vmt;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.monamedia.vmt.common.Utils;
import com.monamedia.vmt.controller.login.LoginFragment;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making notification bar transparent
//        if (Build.VERSION.SDK_INT >= 21) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }
        setContentView(R.layout.login_layout);
        addFragment("", new LoginFragment());
    }

    public void setToolbar(boolean isShow,String msg) {
        if (getSupportActionBar() != null) {
            if (isShow) getSupportActionBar().show();
            else getSupportActionBar().hide();

            getSupportActionBar().setDisplayHomeAsUpEnabled(isShow);
            getSupportActionBar().setDisplayShowHomeEnabled(isShow);
            getSupportActionBar().setTitle(msg);
        }
    }

    public void addFragment(String stack, Fragment fragment) {
        Utils.addFragment(getSupportFragmentManager(),
                stack,
                R.id.root,
                fragment);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
