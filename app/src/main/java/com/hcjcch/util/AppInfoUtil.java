package com.hcjcch.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.util.SparseArray;

import com.hcjcch.flowstatistics.flowutil.Api;
import com.hcjcch.flowstatistics.model.AppInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/2/21 10:28
 */

public class AppInfoUtil {
    public static final String PERMISSION_INTERNET = "android.permission.INTERNET";

    public static Observable<List<AppInfo>> getAllApplication(final Context context) {
        return Observable.create(new Observable.OnSubscribe<List<AppInfo>>() {
            @Override
            public void call(Subscriber<? super List<AppInfo>> subscriber) {
                String flowUidString = FlowSharePreferenceHelper.getString(Constants.SP_KEY_FLOW_SELECT_UID, "");
                String wifiUidString = FlowSharePreferenceHelper.getString(Constants.SP_KEY_WIFI_SELECT_UID, "");
                final List<Integer> flowUidList = getUidListFromPref(flowUidString);
                final List<Integer> wifiUidList = getUidListFromPref(wifiUidString);
                List<AppInfo> appInfoList;
                PackageManager pm = context.getPackageManager();
                List<ApplicationInfo> installed = pm.getInstalledApplications(PackageManager.GET_META_DATA);
                SparseArray<AppInfo> syncMap = new SparseArray<>();
                AppInfo appInfo;
                for (ApplicationInfo info : installed) {
                    appInfo = syncMap.get(info.uid);
                    Drawable appIcon = info.loadIcon(context.getPackageManager());
                    String appName = String.valueOf(info.loadLabel(context.getPackageManager()));
                    String packageName = info.packageName;
                    double flow = 0;
                    boolean hasInternetPermission = false;
                    int uId = info.uid;
                    if (PackageManager.PERMISSION_GRANTED == pm.checkPermission(Manifest.permission.INTERNET, info.packageName)) {
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
                    if (appInfo != null) {
                        appInfo.getAppName().add(appName);
                    } else {
                        appInfo = new AppInfo(uId, appName, appIcon, packageName, flow, hasInternetPermission);
                    }
                    syncMap.put(info.uid, appInfo);
                }
                AppInfo app;
                AppInfo special[] = {
                        new AppInfo(Api.SPECIAL_UID_ANY, "(Any application) - Same as selecting all applications", false, false),
                        new AppInfo(Api.SPECIAL_UID_KERNEL, "(Kernel) - Linux kernel", false, false),
                        new AppInfo(android.os.Process.getUidForName("root"), "(root) - Applications running as root", false, false),
                        new AppInfo(android.os.Process.getUidForName("media"), "Media server", false, false),
                        new AppInfo(android.os.Process.getUidForName("vpn"), "VPN networking", false, false),
                        new AppInfo(android.os.Process.getUidForName("shell"), "Linux shell", false, false),
                };
                for (AppInfo aSpecial : special) {
                    app = aSpecial;
                    if (app.getUid() != -1 && syncMap.get(app.getUid()) == null) {
                        // check if this application is allowed
                        if (Collections.binarySearch(wifiUidList, app.getUid()) >= 0) {
                            app.setWifiCheck(true);
                        }
                        if (Collections.binarySearch(flowUidList, app.getUid()) >= 0) {
                            app.setFlowCheck(true);
                        }
                        syncMap.put(app.getUid(), app);
                    }
                }
                appInfoList = Collections.synchronizedList(new ArrayList<AppInfo>());
                for (int i = 0; i < syncMap.size(); i++) {
                    appInfoList.add(syncMap.valueAt(i));
                }
                subscriber.onNext(appInfoList);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    public static Observable<List<AppInfo>> getTrafficStatsApplication(Context context) {
        String flowUidString = FlowSharePreferenceHelper.getString(Constants.SP_KEY_FLOW_SELECT_UID, "");
        String wifiUidString = FlowSharePreferenceHelper.getString(Constants.SP_KEY_WIFI_SELECT_UID, "");
        final List<Integer> flowUidList = getUidListFromPref(flowUidString);
        final List<Integer> wifiUidList = getUidListFromPref(wifiUidString);
        return getAllApplication(context)
                .flatMap(new Func1<List<AppInfo>, Observable<AppInfo>>() {
                    @Override
                    public Observable<AppInfo> call(List<AppInfo> appInfoList) {
                        return Observable.from(appInfoList);
                    }
                })
                .doOnNext(new Action1<AppInfo>() {
                    @Override
                    public void call(AppInfo appInfo) {
                        if (Collections.binarySearch(flowUidList, appInfo.getUid()) >= 0) {
                            appInfo.setFlowCheck(true);
                        } else {
                            appInfo.setFlowCheck(false);
                        }
                        if (Collections.binarySearch(wifiUidList, appInfo.getUid()) >= 0) {
                            appInfo.setWifiCheck(true);
                        } else {
                            appInfo.setWifiCheck(false);
                        }
                    }
                })
                .collect(new Func0<List<AppInfo>>() {
                    @Override
                    public List<AppInfo> call() {
                        return new ArrayList<>();
                    }
                }, new Action2<List<AppInfo>, AppInfo>() {
                    @Override
                    public void call(List<AppInfo> appInfoList, AppInfo appInfo) {
                        if (appInfo.isHasInternetPermission()) {
                            appInfoList.add(appInfo);
                        }
                    }
                });
    }

    public static List<Integer> getUidListFromPref(String savedPkg_uid) {
        StringTokenizer tok = new StringTokenizer(savedPkg_uid, "|");
        List<Integer> uidList = new ArrayList<>();
        while (tok.hasMoreTokens()) {
            String uid = tok.nextToken();
            if (!uid.equals("")) {
                try {
                    uidList.add(Integer.parseInt(uid));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        // Sort the array to allow using "Arrays.binarySearch" later
        Collections.sort(uidList);
        return uidList;
    }

}