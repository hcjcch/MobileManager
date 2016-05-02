package com.hcjcch.pm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hcjcch.flowstatistics.model.AppInfo;
import com.hcjcch.mobilemanager.BaseActivity;
import com.hcjcch.mobilemanager.R;
import com.hcjcch.pm.adapter.AllApplicationAdapter;
import com.hcjcch.pm.contarct.AllAppPresenterImpl;
import com.hcjcch.pm.contarct.AllApplicationContract;
import com.hcjcch.pm.event.AppDetailEvent;
import com.hcjcch.pm.event.NoInstallApkLongClickEvent;
import com.hcjcch.pm.event.UnInstallAppEvent;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/5/1 21:06
 */

public class AllApplicationActivity extends BaseActivity implements AllApplicationContract.View {

    @Bind(R.id.recycler_all_apk)
    RecyclerView recyclerAllApk;

    private AllApplicationAdapter allApplicationAdapter;
    private AllApplicationContract.Presenter presenter;

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
    protected void subOnCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_all_application);
        ButterKnife.bind(this);
        presenter = new AllAppPresenterImpl(this);
        presenter.setRecyclerView();
        presenter.init();
    }

    @Override
    protected void toolbarNavigationClick(View v) {
        finish();
    }

    @Override
    public void initRecyclerView() {
        recyclerAllApk.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void setRecyclerViewData(List<AppInfo> appInfoList) {
        allApplicationAdapter = new AllApplicationAdapter(appInfoList);
        recyclerAllApk.setAdapter(allApplicationAdapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public void onEventMainThread(AppDetailEvent event) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppDetailActivity.INTENT_KEY_PACKAGE_NAME, event.getAppInfo().getAppPackageName());
        openActivity(AppDetailActivity.class, bundle);
    }

    public void onEventMainThread(UnInstallAppEvent event) {
        presenter.unInstallApp(this, event.getAppInfo().getAppPackageName());
    }

    @Override
    public void setTitle() {
        setTitle("所有应用");
    }
}
