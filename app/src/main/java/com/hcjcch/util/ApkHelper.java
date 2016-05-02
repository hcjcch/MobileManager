package com.hcjcch.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.hcjcch.flowstatistics.model.AppInfo;
import com.hcjcch.pm.ApkFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/5/1 12:43
 */

public class ApkHelper {

    public static Observable<List<ApkFile>> getAllApk() {
        return Observable.create(new Observable.OnSubscribe<List<ApkFile>>() {
            @Override
            public void call(Subscriber<? super List<ApkFile>> subscriber) {
                File file = new File("/sdcard");
                List<ApkFile> apkFiles = new ArrayList<>();
                int i = 0;
                findFile(file, apkFiles, i);
                subscriber.onNext(apkFiles);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    private static Observable<List<ApkFile>> setApkInstallCheck(final Context context) {
        return Observable
                .zip(AppInfoUtil.getAllNoSystemInstalledApp(context), getAllApk(), new Func2<List<AppInfo>, List<ApkFile>, List<ApkFile>>() {
                    @Override
                    public List<ApkFile> call(List<AppInfo> appInfoList, List<ApkFile> apkFiles) {
                        return filterInstalledApk(context, apkFiles, appInfoList);
                    }
                });
    }

    public static Observable<List<ApkFile>> getAllInstalledOrNot(final Context context, final boolean installed) {
        return setApkInstallCheck(context)
                .flatMap(new Func1<List<ApkFile>, Observable<ApkFile>>() {
                    @Override
                    public Observable<ApkFile> call(List<ApkFile> apkFiles) {
                        return Observable.from(apkFiles);
                    }
                })
                .filter(new Func1<ApkFile, Boolean>() {
                    @Override
                    public Boolean call(ApkFile apkFile) {
                        return installed == apkFile.isInstalled();
                    }
                })
                .collect(new Func0<List<ApkFile>>() {
                    @Override
                    public List<ApkFile> call() {
                        return new ArrayList<>();
                    }
                }, new Action2<List<ApkFile>, ApkFile>() {
                    @Override
                    public void call(List<ApkFile> apkFiles, ApkFile apkFile) {
                        apkFiles.add(apkFile);
                    }
                });
    }


    //遍历3层
    private static void findFile(File file, List<ApkFile> apkFiles, int i) {
        i++;
        if (i > 3) {
            return;
        }
        File[] file1 = file.listFiles();
        for (File file2 : file1) {
            if (file2 != null) {
                if (file2.isDirectory()) {
                    findFile(file2, apkFiles, i);
                } else {
                    if (checkIsApk(file2)) {
                        apkFiles.add(new ApkFile(file2.getPath()));
                    }
                }
            }
        }
    }

    private static boolean checkIsApk(File file) {
        String path = file.getAbsolutePath();
        return path.endsWith(".apk");
    }

    private static List<ApkFile> filterInstalledApk(Context context, List<ApkFile> apkFiles, List<AppInfo> installedApp) {
        List<ApkFile> resultApkFile = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        for (ApkFile apkFile : apkFiles) {
            PackageInfo packageInfo = pm.getPackageArchiveInfo(apkFile.getPath(), PackageManager.GET_ACTIVITIES);
            ApplicationInfo info = packageInfo.applicationInfo;
            PackageInfo newPackageInfo;
            try {
                newPackageInfo = context.getPackageManager().getPackageInfo(
                        info.packageName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                newPackageInfo = null;
                e.printStackTrace();
            }
            apkFile.setInstalled(newPackageInfo != null);
            apkFile.setIcon(info.loadIcon(context.getPackageManager()));
            apkFile.setName(String.valueOf(info.loadLabel(context.getPackageManager())));
            apkFile.setVersionCode(packageInfo.versionCode);
            resultApkFile.add(apkFile);
        }
        return resultApkFile;
    }

}
