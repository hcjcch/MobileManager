package com.hcjcch.pm.contarct;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.hcjcch.pm.ApkFile;
import com.hcjcch.util.ApkInstallHelper;

import java.io.File;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/5/1 19:14
 */

public class InstallPresenterImpl implements ApplicationManagerContract.InstallPresenter {

    public static final int SILENT_INSTALL = 0;
    public static final int SYSTEM_INSTALL = 1;
    public static final int DELETE = 2;
    private ApplicationManagerContract.View view;
    private Context context;
    private PmPresenterImpl pmPresenter;

    public InstallPresenterImpl(ApplicationManagerContract.View view, PmPresenterImpl pmPresenter) {
        this.view = view;
        this.context = (Activity) view;
        this.pmPresenter = pmPresenter;
    }

    @Override
    public void selectInstallMode(int which, final ApkFile apkFile) {
        switch (which) {
            case SILENT_INSTALL:
                view.showProgress();
                ApkInstallHelper
                        .silentInstall(apkFile.getPath())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {
                                view.hideProgress();
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                Toast.makeText(context, "安装失败", Toast.LENGTH_SHORT).show();
                                view.hideProgress();
                            }

                            @Override
                            public void onNext(String s) {
                                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                                pmPresenter.deleteApkFile(apkFile);
                                view.adapterRefresh();
                            }
                        });
                break;
            case SYSTEM_INSTALL:
                ApkInstallHelper.SystemInstall(apkFile.getPath(), context);
                break;
            case DELETE:
                boolean isDelete = deleteApkFile(apkFile);
                if (isDelete) {
                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                    pmPresenter.deleteApkFile(apkFile);
                    view.adapterRefresh();
                } else {
                    Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean deleteApkFile(ApkFile apkFile) {
        File file = new File(apkFile.getPath());
        return file.exists() && file.delete();
    }
}
