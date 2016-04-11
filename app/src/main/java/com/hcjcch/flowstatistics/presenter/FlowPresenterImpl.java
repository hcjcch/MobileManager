package com.hcjcch.flowstatistics.presenter;

import android.app.Activity;
import android.content.Context;
import android.view.MenuItem;

import com.hcjcch.flowstatistics.FlowStatisticsActivity;
import com.hcjcch.flowstatistics.flowutil.Api;
import com.hcjcch.flowstatistics.flowutil.G;
import com.hcjcch.flowstatistics.model.AppInfo;
import com.hcjcch.flowstatistics.view.FlowView;
import com.hcjcch.mobilemanager.MApplication;
import com.hcjcch.mobilemanager.R;
import com.hcjcch.util.AppInfoUtil;

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
                        FlowPresenterImpl.this.appInfoList = appInfoList;
                        flowView.setRecyclerViewData(appInfoList);
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
                } else {
                    //打开防火墙
                    Api.applySavedIpTablesRules(MApplication.getContext());
                }
                break;
        }
        return true;
    }


}
