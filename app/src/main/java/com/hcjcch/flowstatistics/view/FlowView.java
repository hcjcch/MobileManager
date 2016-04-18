package com.hcjcch.flowstatistics.view;

import android.view.MenuItem;

import com.hcjcch.flowstatistics.model.AppInfo;

import java.util.List;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/4/2 21:47
 */

public interface FlowView extends TitleView {

    void setRecyclerViewData(List<AppInfo> appInfoList);

    void initRecyclerView();

    void showProgressBar();

    void hideProgressBar();

    void menuFlowSort(MenuItem menuItem);

    void filterRecyclerViewData(List<AppInfo> appInfoList);

    void showFireWallModeSelect();

    void refreshFireModeHeader(String headerString);

    void setFireWallModeOpenText();

    void setFireWallModeCloseText();

}
