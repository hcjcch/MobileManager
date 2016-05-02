package com.hcjcch.pm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hcjcch.mobilemanager.BaseActivity;
import com.hcjcch.mobilemanager.R;
import com.hcjcch.pm.adapter.UnInstalledApkAdapter;
import com.hcjcch.pm.contarct.ApplicationManagerContract;
import com.hcjcch.pm.contarct.PmPresenterImpl;
import com.hcjcch.pm.event.NoInstallApkLongClickEvent;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/5/1 12:26
 */

public class ApplicationManagerActivity extends BaseActivity implements ApplicationManagerContract.View {

    @Bind(R.id.text_check_all_application)
    TextView textCheckAllApplication;

    @Bind(R.id.recycler_all_apk)
    RecyclerView recyclerAllApk;

    @Bind(R.id.text_uninstall_apk)
    TextView textUninstallApk;

    @Bind(R.id.progress)
    ProgressBar progress;


    private ApplicationManagerContract.Presenter presenter;
    private UnInstalledApkAdapter apkAdapter;


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
        setContentView(R.layout.activity_application_manager);
        ButterKnife.bind(this);
        presenter = new PmPresenterImpl(this);
        presenter.onCreate();
        presenter.getUnInstalledApk(this);
    }

    @Override
    protected void toolbarNavigationClick(View v) {
        finish();
    }

    @Override
    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerAllApk.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void setRecyclerData(List<ApkFile> apkFiles) {
        apkAdapter = new UnInstalledApkAdapter(apkFiles);
        recyclerAllApk.setAdapter(apkAdapter);
    }

    @Override
    public void setUnInstalledApkNumber(int number) {
        textUninstallApk.setText("未安装(" + String.valueOf(number) + ")");
    }

    @Override
    public void showUnInstallApkClickDialog(final ApkFile apkFile) {
        new MaterialDialog.Builder(this)
                .title("选择您的操作")
                .items(R.array.un_install_dialog)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        presenter.selectInstallMode(which, apkFile);
                    }
                })
                .show();
    }

    @Override
    public void adapterRefresh() {
        apkAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(NoInstallApkLongClickEvent event) {
        showUnInstallApkClickDialog(event.getApkFile());
    }

    @OnClick(R.id.text_check_all_application)
    void intentToAllApplication() {
        openActivity(AllApplicationActivity.class, null);
    }

    @Override
    public void setTitle() {
        setTitle("用用管理");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
