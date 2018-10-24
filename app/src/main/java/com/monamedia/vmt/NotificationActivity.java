package com.monamedia.vmt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.monamedia.vmt.common.Utils;
import com.monamedia.vmt.controller.notification.NotificationFragment;

public class NotificationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_layout);
        if (getSupportActionBar() != null) {
            getSupportActionBar().show();

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Utils.getMsg(this,R.string.notification));
        }
        Utils.addFragment(getSupportFragmentManager(),
                "",
                R.id.root,
                new NotificationFragment());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
