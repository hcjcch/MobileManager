package com.hcjcch.pm.event;

import com.hcjcch.pm.ApkFile;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/5/1 19:38
 */

public class NoInstallApkLongClickEvent {
    private ApkFile apkFile;

    public NoInstallApkLongClickEvent(ApkFile apkFile) {
        this.apkFile = apkFile;
    }

    public ApkFile getApkFile() {
        return apkFile;
    }
}
