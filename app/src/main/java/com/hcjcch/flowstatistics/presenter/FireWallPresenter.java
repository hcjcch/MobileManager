package com.hcjcch.flowstatistics.presenter;

import android.content.Context;

import com.hcjcch.flowstatistics.model.AppInfo;

import java.util.List;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/4/8 11:18
 */

public interface FireWallPresenter {
    void saveRule(Context context, List<AppInfo> appInfoList);
    void applySavedIpTablesRules(Context context);
}
