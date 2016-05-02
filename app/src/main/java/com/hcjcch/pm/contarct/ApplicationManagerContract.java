package com.hcjcch.pm.contarct;

import android.content.Context;

import com.hcjcch.flowstatistics.view.TitleView;
import com.hcjcch.pm.ApkFile;

import java.util.List;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/5/1 12:27
 */

public interface ApplicationManagerContract {
    interface View extends TitleView{
        void initRecyclerView();

        void setRecyclerData(List<ApkFile> apkFiles);

        void setUnInstalledApkNumber(int number);

        void showUnInstallApkClickDialog(ApkFile apkFile);

        void adapterRefresh();

        void showProgress();

        void hideProgress();
    }

    interface Presenter {
        void onCreate();

        void getUnInstalledApk(Context context);

        void selectInstallMode(int which, ApkFile apkFile);
    }

    interface InstallPresenter {
        void selectInstallMode(int which, ApkFile apkFile);
    }
}
