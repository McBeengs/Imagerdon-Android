package com.mcbeengs.imagerdon.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.mcbeengs.imagerdon.ImagerdonApplication;
import com.mcbeengs.imagerdon.R;

/**
 * Created by McBeengs on 01/10/2016.
 */

public class BaseActivity extends AppCompatActivity {

    private static Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImagerdonApplication app = ImagerdonApplication.getInstance();
    }

    protected void setUpToolbar() {
        toolbar =  (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
     }

    protected Toolbar getToolbar(){
        if (toolbar != null) {
            return toolbar;
        }
        Log.e("Imagerdon", "Method \"setUpToolbar()\" wasn't called before \"getToolbar()\"");
        return null;
    }

    protected Context getContext() {
        return this;
    }
}
