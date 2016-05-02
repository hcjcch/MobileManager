package com.hcjcch.pm.contarct;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.hcjcch.flowstatistics.model.AppInfo;
import com.hcjcch.util.AppInfoUtil;
import com.hcjcch.util.AppUnInstallHelper;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/5/2 01:19
 */

public class AppDetailPresenterImpl implements AppDetailContract.Presenter {
    private AppDetailContract.View view;
    private Context context;
    private AppInfo appInfo;

    public AppDetailPresenterImpl(AppDetailContract.View view) {
        this.view = view;
        this.context = (Activity) view;
    }

    @Override
    public void init(final String packageName) {
        view.setTitle();
        AppInfoUtil
                .getAppInfoByPackageName(context, packageName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AppInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(AppInfo appInfo) {
                        AppDetailPresenterImpl.this.appInfo = appInfo;
                        view.setAppName(appInfo.getName());
                        view.setUploadTraffic((int) appInfo.getAppFlow());
                        view.setImageView(appInfo.getAppIcon());
                        view.setPackageName(packageName);
                    }
                });
    }

    @Override
    public void intentToAppInformation(Context context) {
        final String SCHEME = "package";
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts(SCHEME, appInfo.getAppPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    @Override
    public void unInstall() {
        AppUnInstallHelper.unInstallApp(context, appInfo.getAppPackageName());
    }
}
