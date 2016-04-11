package com.hcjcch.flowstatistics.flowutil;

import com.hcjcch.util.Constants;
import com.hcjcch.util.FlowSharePreferenceHelper;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/4/11 16:13
 */

public class G {
    public static boolean fireWallEnable() {
        return FlowSharePreferenceHelper.getBoolean(Constants.SP_KEY_FIRE_WALL_ENABLE);
    }

    public static void setFireWallEnable(boolean enable) {
        FlowSharePreferenceHelper.saveBoolean(Constants.SP_KEY_FIRE_WALL_ENABLE, enable);
    }
}
