package com.hcjcch.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.hcjcch.libsuperuser.Shell;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/5/1 19:16
 */

public class ApkInstallHelper {
    public static Observable<String> silentInstall(final String path) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String command = "pm install -r " + path + "\n";
                List<String> result = Shell.SU.run(command);
                String resultString = "";
                if (result != null && result.size() != 0) {
                    for (String string : result) {
                        resultString += string;
                    }
                }
                subscriber.onNext(resultString);
            }
        }).subscribeOn(Schedulers.io());
    }

    public static void SystemInstall(final String path, Context context) {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}