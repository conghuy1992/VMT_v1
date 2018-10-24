package com.monamedia.vmt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.monamedia.vmt.common.CommonWebViewFragment;
import com.monamedia.vmt.common.Utils;
import com.monamedia.vmt.model.MenuDto;

public class WebViewActivity extends AppCompatActivity {
    private String TAG = "WebViewActivity";
    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.webview_layout);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            getSupportActionBar().setTitle(msg);
        }
        url = getIntent().getStringExtra(MenuDto.KEY);
        addFragment();
    }

    private void addFragment() {
        Log.d(TAG, "url:" + url);
        Utils.addFragment(getSupportFragmentManager(),
                "",
                R.id.root,
                new CommonWebViewFragment(url));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        url = intent.getStringExtra(MenuDto.KEY);
        Log.d(TAG, "onNewIntent");
        addFragment();
    }

    //    @Override
//    public void onBackPressed() {
//        onBackPressed();
//    }
//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
