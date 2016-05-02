package com.hcjcch.pm.contarct;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.hcjcch.flowstatistics.view.TitleView;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/5/2 01:06
 */

public interface AppDetailContract {
    interface View extends TitleView{
        void setImageView(Drawable drawable);

        void setAppName(String appName);

        void setPackageName(String packageName);

        void setUploadTraffic(int traffic);
    }

    interface Presenter {
        void init(String packageName);

        void intentToAppInformation(Context context);

        void unInstall();
    }
}
