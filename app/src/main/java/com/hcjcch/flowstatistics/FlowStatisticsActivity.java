package com.hcjcch.flowstatistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hcjcch.flowstatistics.adapter.FlowAdapter;
import com.hcjcch.flowstatistics.flowutil.G;
import com.hcjcch.flowstatistics.model.AppInfo;
import com.hcjcch.flowstatistics.presenter.FlowPresenter;
import com.hcjcch.flowstatistics.presenter.FlowPresenterImpl;
import com.hcjcch.flowstatistics.view.FlowView;
import com.hcjcch.mobilemanager.BaseActivity;
import com.hcjcch.mobilemanager.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/4/2 16:07
 */

public class FlowStatisticsActivity extends BaseActivity implements FlowView {
    FlowPresenter flowPresenter;

    @Bind(R.id.flow_recycler)
    RecyclerView flowRecyclerView;

    @Bind(R.id.progress)
    ProgressBar progressBar;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.wall_type)
    TextView callType;

    MenuItem flowMenuItem;
    MenuItem uidMenuItem;
    MenuItem toggleMenuItem;

    FlowAdapter flowAdapter;

    @Override
    protected void subOnCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_flow_statistics);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        flowPresenter = new FlowPresenterImpl(this);
        flowPresenter.onCreate();

    }

    @Override
    public void setRecyclerViewData(List<AppInfo> appInfoList) {
        flowAdapter = new FlowAdapter(appInfoList);
        flowRecyclerView.setAdapter(flowAdapter);
    }

    @Override
    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        flowRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void menuFlowSort(MenuItem menuItem) {
        menuItem.setChecked(true);
    }

    @Override
    public void filterRecyclerViewData(List<AppInfo> appInfoList) {
        flowAdapter.notifyDataSetChanged();
    }

    @Override
    public void showFireWallModeSelect() {
        new MaterialDialog.Builder(this)
                .title("选择防火墙模式")
                .items(R.array.fire_wall_mode)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        flowPresenter.selectFireMode(which, text.toString());
                    }
                })
                .show();
    }

    @Override
    public void refreshFireModeHeader(String headerString) {
        callType.setText(headerString);
    }

    @Override
    public void setFireWallModeOpenText() {
        toggleMenuItem.setTitle("打开防火墙");
    }

    @Override
    public void setFireWallModeCloseText() {
        toggleMenuItem.setTitle("关闭防火墙");
    }

    @Override
    public void setTitle() {
        setTitle("流量统计");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.flow_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return flowPresenter.menuItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        flowMenuItem = menu.findItem(R.id.menu_app_flow_number);
        uidMenuItem = menu.findItem(R.id.menu_app_uid);
        toggleMenuItem = menu.findItem(R.id.menu_toggle);
        if (G.fireWallEnable()) {
            setFireWallModeCloseText();
        } else {
            setFireWallModeOpenText();
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void toolbarNavigationClick(View v) {
        finish();
    }

    @OnClick(R.id.wall_type)
    void selectFireWallMode() {
        showFireWallModeSelect();
    }


}