package com.hcjcch.flowstatistics.presenter;

import android.content.Context;

import com.hcjcch.flowstatistics.flowutil.Api;
import com.hcjcch.flowstatistics.model.AppInfo;
import com.hcjcch.util.Constants;
import com.hcjcch.util.FlowSharePreferenceHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/4/8 11:22
 */

public class FireWallPresenterImpl implements FireWallPresenter {
    @Override
    public void saveRule(final Context context, List<AppInfo> appInfoList) {
        Observable
                .from(appInfoList)
                .filter(new Func1<AppInfo, Boolean>() {
                    @Override
                    public Boolean call(AppInfo appInfo) {
                        return appInfo != null;
                    }
                })
                .filter(new Func1<AppInfo, Boolean>() {
                    @Override
                    public Boolean call(AppInfo appInfo) {
                        return appInfo.isFlowCheck() || appInfo.isWifiCheck();
                    }
                })
                .collect(new Func0<Map<String, StringBuilder>>() {
                    @Override
                    public Map<String, StringBuilder> call() {
                        Map<String, StringBuilder> map = new HashMap<>();
                        map.put(Constants.SP_KEY_FLOW_SELECT_UID, new StringBuilder());
                        map.put(Constants.SP_KEY_WIFI_SELECT_UID, new StringBuilder());
                        return map;
                    }
                }, new Action2<Map<String, StringBuilder>, AppInfo>() {
                    @Override
                    public void call(Map<String, StringBuilder> map, AppInfo appInfo) {
                        StringBuilder flowStringBuilder = map.get(Constants.SP_KEY_FLOW_SELECT_UID);
                        StringBuilder wifiStringBuilder = map.get(Constants.SP_KEY_WIFI_SELECT_UID);
                        if (appInfo.isWifiCheck()) {
                            if (wifiStringBuilder.length() != 0) {
                                wifiStringBuilder.append("|");
                            }
                            wifiStringBuilder.append(appInfo.getUid());
                        }
                        if (appInfo.isFlowCheck()) {
                            if (flowStringBuilder.length() != 0) {
                                flowStringBuilder.append("|");
                            }
                            flowStringBuilder.append(appInfo.getUid());
                        }
                    }
                })
                .subscribe(new Subscriber<Map<String, StringBuilder>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Map<String, StringBuilder> map) {
                        FlowSharePreferenceHelper.saveString(Constants.SP_KEY_FLOW_SELECT_UID, map.get(Constants.SP_KEY_FLOW_SELECT_UID).toString());
                        FlowSharePreferenceHelper.saveString(Constants.SP_KEY_WIFI_SELECT_UID, map.get(Constants.SP_KEY_WIFI_SELECT_UID).toString());
                    }
                });
    }

    @Override
    public void applySavedIpTablesRules(Context context) {
        Api.applySavedIpTablesRules(context);
    }

}
