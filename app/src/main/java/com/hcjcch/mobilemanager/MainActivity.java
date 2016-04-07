package com.hcjcch.mobilemanager;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.hcjcch.flowstatistics.FlowStatisticsActivity;

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
    }

    @OnClick(R.id.network)
    void intentToFlow() {
        openActivity(FlowStatisticsActivity.class, null);
    }
}
