package com.hcjcch.flowstatistics.presenter;

import android.view.MenuItem;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/4/2 21:49
 */

public interface FlowPresenter {

    void onCreate();

    boolean menuItemSelected(MenuItem item);

    void selectFireMode(int which,String modeText);
}
