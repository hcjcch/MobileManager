package com.hcjcch.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;

import com.hcjcch.flowstatistics.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/2/21 10:28
 */

public class ApplicationUtil {
    public static final String PERMISSION_INTERNET = "android.permission.INTERNET";

    public static Observable<List<AppInfo>> getAllApplication(final Context context) {
        return Observable.create(new Observable.OnSubscribe<List<AppInfo>>() {
            @Override
            public void call(Subscriber<? super List<AppInfo>> subscriber) {
                List<AppInfo> appInfo = new ArrayList<>();
                //获取所有的安装在手机上的应用软件的信息，并且获取这些软件里面的权限信息
                PackageManager pm = context.getPackageManager();//获取系统应用包管理
                //获取每个包内的manifest.xml信息，它的权限等等
                List<PackageInfo> packageInfoList = pm.getInstalledPackages
                        (PackageManager.GET_PERMISSIONS);
                //遍历每个应用包信息
                for (PackageInfo info : packageInfoList) {
                    Drawable appIcon = info.applicationInfo.loadIcon(context.getPackageManager());
                    String appName = String.valueOf(info.applicationInfo.loadLabel(context.getPackageManager()));
                    String packageName = info.packageName;
                    double flow = 0;
                    boolean hasInternetPermission = false;
                    int uId = info.applicationInfo.uid;
                    String[] permissions = info.requestedPermissions;
                    if (permissions != null && permissions.length > 0) {
                        //找出需要网络服务的应用程序
                        for (String permission : permissions) {
                            if (PERMISSION_INTERNET.equals(permission)) {
                                hasInternetPermission = true;
                                //获取每个应用程序在操作系统内的进程id
                                //如果返回-1，代表不支持使用该方法，注意必须是2.2以上的
                                long rx = TrafficStats.getUidRxBytes(uId);
                                //如果返回-1，代表不支持使用该方法，注意必须是2.2以上的
                                long tx = TrafficStats.getUidTxBytes(uId);
                                if (rx > 0 && tx > 0) {
                                    flow = (rx + tx) / 1024 / 1024;
                                }
                            }
                        }
                    }
                    appInfo.add(new AppInfo(uId, appName, appIcon, packageName, flow, hasInternetPermission));
                }
                subscriber.onNext(appInfo);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    public static Observable<List<AppInfo>> getTrafficStatsApplication(Context context) {
        return getAllApplication(context)
                .map(new Func1<List<AppInfo>, List<AppInfo>>() {
                    @Override
                    public List<AppInfo> call(List<AppInfo> appInfoList) {
                        List<AppInfo> appInformationList = new ArrayList<>();
                        for (AppInfo appInfo : appInfoList) {
                            if (appInfo.isHasInternetPermission()) {
                                appInformationList.add(appInfo);
                            }
                        }
                        return appInformationList;
                    }
                });
    }
}