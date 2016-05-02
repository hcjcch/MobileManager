package com.hcjcch.mobilemanager;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;

import com.hcjcch.flowstatistics.FlowStatisticsActivity;
import com.hcjcch.flowstatistics.flowutil.Api;
import com.hcjcch.garbage.GarbageCleanActivity;
import com.hcjcch.pm.ApplicationManagerActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Bind(R.id.network)
    Button network;

    @Bind(R.id.application_manager)
    Button applicationManager;

    @Bind(R.id.garbage)
    Button garbage;

    @Override
    protected void subOnCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/din_condensed_bold.ttf");
        network.setTypeface(typeface);
        applicationManager.setTypeface(typeface);
        garbage.setTypeface(typeface);
        Log.d("huangchen", this.getDir("bin", 0).getAbsolutePath());
        Log.d("huangchen", Api.scriptHeader(this));
        Api.runScript(this, Api.scriptHeader(this), new StringBuilder(), 3000, true);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Api.showIpTableRules(this);
    }

    @OnClick(R.id.network)
    void intentToFlow() {
        openActivity(FlowStatisticsActivity.class, null);
    }

    @OnClick(R.id.garbage)
    void intentGarbage() {
        openActivity(GarbageCleanActivity.class, null);
    }

    @OnClick(R.id.application_manager)
    void intentToApplicationManager() {
        openActivity(ApplicationManagerActivity.class, null);
    }
}
