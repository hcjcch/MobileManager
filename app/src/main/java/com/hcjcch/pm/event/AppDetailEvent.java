package com.hcjcch.pm.event;

import com.hcjcch.flowstatistics.model.AppInfo;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/5/2 01:26
 */

public class AppDetailEvent {
    private AppInfo appInfo;

    public AppDetailEvent(AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    public AppInfo getAppInfo() {
        return appInfo;
    }

}
