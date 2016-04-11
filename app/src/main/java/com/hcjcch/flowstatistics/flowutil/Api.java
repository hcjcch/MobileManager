package com.hcjcch.flowstatistics.flowutil;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.hcjcch.mobilemanager.BuildConfig;
import com.hcjcch.mobilemanager.R;
import com.hcjcch.util.Constants;
import com.hcjcch.util.FlowSharePreferenceHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/4/8 09:55
 */

public class Api {
    private static final String TAG = "API";
    private static String FIRE_WALL_WALL_CHAIN_NAME = "fire_wall";

    public static final String ITFS_WIFI[] = {"eth+", "wlan+", "tiwlan+", "ra+", "bnep+"};

    public static final String ITFS_3G[] = {"rmnet+", "pdp+", "uwbr+", "wimax+", "vsnet+",
            "rmnet_sdio+", "ccmni+", "qmi+", "svnet0+", "ccemni+",
            "wwan+", "cdma_rmnet+", "usb+", "rmnet_usb+", "clat4+", "cc2mni+", "bond1+", "rmnet_smux+", "ccinet+", "v4-rmnet+", "seth_w+"};

    public static final String ITFS_VPN[] = {"tun+", "ppp+", "tap+"};

    public void saveRules(Context context) {

    }

    public static void applySavedIpTablesRules(Context context) {
        assertBinaries(context);
    }

    public static boolean purgeIptables(Context context) {
        assertBinaries(context);
        return true;
    }

    public static boolean assertBinaries(Context ctx) {
        int currentVer, lastVer;
        currentVer = BuildConfig.VERSION_CODE;
        lastVer = FlowSharePreferenceHelper.getInt(Constants.SP_KEY_APP_VERSION);
        if (lastVer == currentVer) {
            return true;
        }
        String abi = Build.CPU_ABI;
        boolean ret;
        if (abi.startsWith("x86")) {
            ret = installBinary(ctx, R.raw.busybox_x86, "busybox") &&
                    installBinary(ctx, R.raw.iptables_x86, "iptables") &&
                    installBinary(ctx, R.raw.ip6tables_x86, "ip6tables");
        } else if (abi.startsWith("mips")) {
            ret = installBinary(ctx, R.raw.busybox_mips, "busybox") &&
                    installBinary(ctx, R.raw.iptables_mips, "iptables") &&
                    installBinary(ctx, R.raw.ip6tables_mips, "ip6tables");
        } else {
            // default to ARM
            ret = installBinary(ctx, R.raw.busybox_arm, "busybox") &&
                    installBinary(ctx, R.raw.iptables_arm, "iptables") &&
                    installBinary(ctx, R.raw.ip6tables_arm, "ip6tables");
        }
        FlowSharePreferenceHelper.saveInt(Constants.SP_KEY_APP_VERSION, currentVer);
        return ret;
    }

    private static boolean installBinary(Context ctx, int resId, String filename) {
        try {
            File f = new File(ctx.getDir("bin", 0), filename);
            if (f.exists()) {
                f.delete();
            }
            copyRawFile(ctx, resId, f, "0755");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "installBinary failed: " + e.getLocalizedMessage());
            return false;
        }
    }

    private static void copyRawFile(Context ctx, int resid, File file, String mode) throws IOException, InterruptedException {
        final String abspath = file.getAbsolutePath();
        // Write the iptables binary
        final FileOutputStream out = new FileOutputStream(file);
        final InputStream is = ctx.getResources().openRawResource(resid);
        byte buf[] = new byte[1024];
        int len;
        while ((len = is.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();
        is.close();
        // Change the permissions
        Runtime.getRuntime().exec("chmod " + mode + " " + abspath).waitFor();
    }

    public static String scriptHeader(Context context) {
        final String dir = context.getDir("bin", 0).getAbsolutePath();
        final String myIptables = dir + "/iptables";
        return "" + "IPTABLES=iptables\n" + "IP6TABLES=ip6tables\n"
                + "BUSYBOX=busybox\n" + "GREP=grep\n" + "ECHO=echo\n"
                + "# Try to find busybox\n" + "if "
                + dir
                + "/busybox --help >/dev/null 2>/dev/null ; then\n"
                + "	BUSYBOX="
                + dir
                + "/busybox\n"
                + "	GREP=\"$BUSYBOX grep\"\n"
                + "elif busybox --help >/dev/null 2>/dev/null ; then\n"
                + "	BUSYBOX=busybox\n"
                + "fi\n"
                + "# Try to find iptables\n"
                + "if "
                + myIptables
                + " --version >/dev/null 2>/dev/null ; then\n"
                + "	IPTABLES="
                + myIptables + "\n" + "fi\n" + "";
    }
}
