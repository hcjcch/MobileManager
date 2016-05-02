package com.hcjcch.flowstatistics.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.MenuItem;

import com.hcjcch.flowstatistics.FlowStatisticsActivity;
import com.hcjcch.flowstatistics.flowutil.Api;
import com.hcjcch.flowstatistics.flowutil.G;
import com.hcjcch.flowstatistics.model.AppInfo;
import com.hcjcch.flowstatistics.view.FlowView;
import com.hcjcch.mobilemanager.R;
import com.hcjcch.util.AppInfoUtil;
import com.hcjcch.util.Constants;
import com.hcjcch.util.FlowSharePreferenceHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/4/2 21:49
 */

public class FlowPresenterImpl implements FlowPresenter {

    private List<AppInfo> allAppInfoList = new ArrayList<>();
    private boolean isSelectFilter = false;
    FlowView flowView;
    List<AppInfo> appInfoList = new ArrayList<>();
    FireWallPresenter fireWallPresenter;
    Context context;

    public FlowPresenterImpl(FlowView flowView) {
        this.flowView = flowView;
        context = (Activity) flowView;
        fireWallPresenter = new FireWallPresenterImpl();
    }


    @Override
    public void onCreate() {
        flowView.setTitle();
        flowView.initRecyclerView();
        AppInfoUtil
                .getTrafficStatsApplication(((FlowStatisticsActivity) flowView))
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        flowView.showProgressBar();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<AppInfo>>() {
                    @Override
                    public void onCompleted() {
                        flowView.hideProgressBar();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        flowView.hideProgressBar();
                    }

                    @Override
                    public void onNext(List<AppInfo> appInfoList) {
                        FlowPresenterImpl.this.allAppInfoList = appInfoList;
                        for (AppInfo appInfo : allAppInfoList) {
                            FlowPresenterImpl.this.appInfoList.add(appInfo);
                        }
                        flowView.setRecyclerViewData(FlowPresenterImpl.this.appInfoList);
                    }
                });
    }

    @Override
    public boolean menuItemSelected(MenuItem item) {
        flowView.menuFlowSort(item);
        switch (item.getItemId()) {
            case R.id.menu_app_flow_number:
                Collections.sort(appInfoList, new Comparator<AppInfo>() {
                    @Override
                    public int compare(AppInfo lhs, AppInfo rhs) {
                        return (int) (rhs.getAppFlow() / 1024 / 1024 - lhs.getAppFlow() / 1024 / 1024);
                    }
                });
                flowView.filterRecyclerViewData(appInfoList);
                break;
            case R.id.menu_app_uid:
                Collections.sort(appInfoList, new Comparator<AppInfo>() {
                    @Override
                    public int compare(AppInfo lhs, AppInfo rhs) {
                        return lhs.getUid() - rhs.getUid();
                    }
                });
                flowView.filterRecyclerViewData(appInfoList);
                break;
            case R.id.menu_app_name:
                Collections.sort(appInfoList, new Comparator<AppInfo>() {
                    @Override
                    public int compare(AppInfo lhs, AppInfo rhs) {
                        return lhs.getName().compareTo(rhs.getName());
                    }
                });
                flowView.filterRecyclerViewData(appInfoList);
                break;
            case R.id.menu_apply:
                fireWallPresenter.saveRule(context, appInfoList);
                break;
            case R.id.menu_toggle:
                if (G.fireWallEnable()) {
                    //关闭防火墙
                    flowView.setFireWallModeOpenText();
                    boolean purgeSuccess = Api.purgeIpTables(context, true);
                    if (purgeSuccess) {
                        Log.d("huangchen", "success");
                        G.setFireWallEnable(false);
                    }
                } else {
                    //打开防火墙
                    flowView.setFireWallModeCloseText();
                    boolean applySuccess = Api.applySavedIpTablesRules(context);
                    if (applySuccess) {
                        Log.d("huangchen", "success");
                        G.setFireWallEnable(true);
                    }
                }
                break;
            case R.id.select_filter:
                isSelectFilter = !isSelectFilter;
                AppInfoUtil.filterSelectAppInfo(allAppInfoList, appInfoList, isSelectFilter);
                flowView.notifyChanged();
                break;
        }
        return true;
    }

    @Override
    public void selectFireMode(int which, String modeText) {
        final String mode = (which == 0 ? Api.MODE_WHITE_LIST : Api.MODE_BLACK_LIST);
        FlowSharePreferenceHelper.saveString(Constants.SP_KEY_FIRE_WALL_MODE, mode);
        flowView.refreshFireModeHeader(modeText);
    }


}