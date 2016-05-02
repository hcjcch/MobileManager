package com.hcjcch.pm.contarct;

import android.app.Activity;
import android.content.Context;

import com.hcjcch.flowstatistics.model.AppInfo;
import com.hcjcch.util.AppInfoUtil;
import com.hcjcch.util.AppUnInstallHelper;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/5/1 21:14
 */

public class AllAppPresenterImpl implements AllApplicationContract.Presenter {
    private AllApplicationContract.View view;
    private Context context;
    private List<AppInfo> appInfoList;

    public AllAppPresenterImpl(AllApplicationContract.View view) {
        this.view = view;
        this.context = (Activity) view;
    }


    @Override
    public void init() {
        view.setTitle();
        AppInfoUtil
                .getAllNoSystemInstalledApp(context)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<AppInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<AppInfo> appInfoList) {
                        AllAppPresenterImpl.this.appInfoList = appInfoList;
                        view.setRecyclerViewData(appInfoList);
                    }
                });
    }

    @Override
    public void setRecyclerView() {
        view.initRecyclerView();
    }

    @Override
    public void unInstallApp(Context context, String packageName) {
        AppUnInstallHelper.unInstallApp(context, packageName);
    }
}
