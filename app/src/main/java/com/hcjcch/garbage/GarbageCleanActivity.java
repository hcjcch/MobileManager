package com.hcjcch.garbage;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hcjcch.garbage.contract.GarbageCleanContract;
import com.hcjcch.garbage.contract.GarbageCleanPresenterImpl;
import com.hcjcch.garbage.event.CurrentCleanDirectoryEvent;
import com.hcjcch.garbage.event.DeleteFileEvent;
import com.hcjcch.mobilemanager.BaseActivity;
import com.hcjcch.mobilemanager.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/4/30 15:13
 */

public class GarbageCleanActivity extends BaseActivity implements GarbageCleanContract.View {

    @Bind(R.id.layout_garbage_clean)
    RelativeLayout layoutGarbageClean;

    @Bind(R.id.text_start_clean_garbage)
    TextView textStartCleanGarbage;

    @Bind(R.id.text_garbage_number)
    TextView textGarbageNumber;

    @Bind(R.id.layout_start_clean)
    RelativeLayout layoutStartClean;

    GarbageCleanContract.Presenter presenter;
    @Bind(R.id.text_clean_directory)

    TextView textCleanDirectory;

    @Override
    protected void subOnCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activty_garbage_clean);
        ButterKnife.bind(this);
        presenter = new GarbageCleanPresenterImpl(this);
        presenter.init();
        presenter.onCreate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void toolbarNavigationClick(View v) {
        finish();
    }


    @Override
    public void setGarbageCleanBg(int R, int G, int B) {
        layoutGarbageClean.setBackgroundColor(Color.argb(255, R, G, B));
    }

    @Override
    public void setCleanText(String text) {
        textStartCleanGarbage.setText(text);
    }

    @Override
    public void setGarbageNumberText(long number) {
        textGarbageNumber.setText(number / 1024 / 1024 + "M");
    }

    @Override
    public void setCleaningText() {
        textStartCleanGarbage.setText("正在清理");
    }

    @Override
    public void setFinishCleaningText() {
        textStartCleanGarbage.setText("清理完成");
    }

    @Override
    public void setScanText() {
        textStartCleanGarbage.setText("正在扫描");
    }

    @Override
    public void setFinishScanText() {
        textStartCleanGarbage.setText("开始清理");
    }


    @Override
    public void setGarbageNumberVisible(boolean visible) {
        if (visible) {
            textGarbageNumber.setVisibility(View.VISIBLE);
        } else {
            textGarbageNumber.setVisibility(View.GONE);
        }
    }

    @Override
    public void resetText() {
        textStartCleanGarbage.setText("开始扫描");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.layout_start_clean)
    void startClean() {
        presenter.startClean();
    }

    public void onEventMainThread(CurrentCleanDirectoryEvent event) {
        if (!TextUtils.isEmpty(event.getAbsolutePath())) {
            textCleanDirectory.setText(event.getAbsolutePath());
        }
    }

    public void onEventMainThread(DeleteFileEvent event) {
        if (!TextUtils.isEmpty(event.getAbsolutePath())) {
            textCleanDirectory.setText(event.getAbsolutePath());
        }
    }
}