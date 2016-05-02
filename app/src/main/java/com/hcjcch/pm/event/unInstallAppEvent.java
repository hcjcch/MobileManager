package com.hcjcch.pm.event;

import com.hcjcch.flowstatistics.model.AppInfo;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/5/2 09:42
 */

public class UnInstallAppEvent {
    private AppInfo appInfo;

    public UnInstallAppEvent(AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    public AppInfo getAppInfo() {
        return appInfo;
    }
}
