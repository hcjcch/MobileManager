package com.hcjcch.mobilemanager;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hcjcch.flowstatistics.FlowStatisticsActivity;
import com.hcjcch.garbage.GarbageCleanActivity;
import com.hcjcch.pm.ApplicationManagerActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected void subOnCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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
