package com.hcjcch.pm.contarct;

import android.app.Activity;
import android.content.Context;

import com.hcjcch.pm.ApkFile;
import com.hcjcch.util.ApkHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/5/1 14:35
 */

public class PmPresenterImpl implements ApplicationManagerContract.Presenter {
    private ApplicationManagerContract.View view;
    private Context context;
    private List<ApkFile> apkFiles = new ArrayList<>();
    private ApplicationManagerContract.InstallPresenter installPresenter;

    public PmPresenterImpl(ApplicationManagerContract.View view) {
        this.view = view;
        context = (Activity) view;
        installPresenter = new InstallPresenterImpl(view, this);
    }

    @Override
    public void onCreate() {
        view.initRecyclerView();
        view.setTitle();
    }

    @Override
    public void getUnInstalledApk(Context context) {
        view.showProgress();
        ApkHelper
                .getAllInstalledOrNot(context, false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ApkFile>>() {
                    @Override
                    public void onCompleted() {
                        view.hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.hideProgress();
                    }

                    @Override
                    public void onNext(List<ApkFile> apkFiles) {
                        PmPresenterImpl.this.apkFiles = apkFiles;
                        view.setUnInstalledApkNumber(apkFiles.size());
                        view.setRecyclerData(PmPresenterImpl.this.apkFiles);
                    }
                });
    }

    @Override
    public void selectInstallMode(int which, ApkFile apkFile) {
        installPresenter.selectInstallMode(which, apkFile);
    }

    public void deleteApkFile(ApkFile apkFile) {
        apkFiles.remove(apkFile);
    }

}
