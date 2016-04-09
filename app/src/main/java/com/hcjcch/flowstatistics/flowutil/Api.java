package com.hcjcch.flowstatistics.flowutil;

import android.content.Context;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/4/8 09:55
 */

public class Api {
    private static String AFWALL_CHAIN_NAME = "fire_wall";
    public static final String ITFS_3G[] = {"rmnet+", "pdp+", "uwbr+", "wimax+", "vsnet+",
            "rmnet_sdio+", "ccmni+", "qmi+", "svnet0+", "ccemni+",
            "wwan+", "cdma_rmnet+", "usb+", "rmnet_usb+", "clat4+", "cc2mni+", "bond1+", "rmnet_smux+", "ccinet+", "v4-rmnet+", "seth_w+"};

    public static final String ITFS_VPN[] = {"tun+", "ppp+", "tap+"};

    public void saveRules(Context context) {

    }
}
