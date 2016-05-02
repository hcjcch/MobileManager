package com.hcjcch.pm.contarct;

import android.content.Context;

import com.hcjcch.flowstatistics.model.AppInfo;
import com.hcjcch.flowstatistics.view.TitleView;

import java.util.List;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/5/1 21:07
 */

public interface AllApplicationContract {
    interface View extends TitleView{
        void initRecyclerView();

        void setRecyclerViewData(List<AppInfo> appInfoList);
    }

    interface Presenter {
        void init();

        void setRecyclerView();

        void unInstallApp(Context context, String packageName);
    }
}
