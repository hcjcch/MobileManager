package com.hcjcch.flowstatistics.flowutil;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.hcjcch.mobilemanager.BuildConfig;
import com.hcjcch.mobilemanager.R;
import com.hcjcch.util.AppInfoUtil;
import com.hcjcch.util.Constants;
import com.hcjcch.util.FlowSharePreferenceHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/4/8 09:55
 */

public class Api {
    private static final String TAG = "API";
    private static final String SCRIPT_FILE = "fireWall.sh";
    public static final String MODE_WHITE_LIST = "white_list";
    public static final String MODE_BLACK_LIST = "black_list";
    private static String CHAIN_NAME = "fire_wall";
    /**
     * special application UID used to indicate "any application"
     */
    public static final int SPECIAL_UID_ANY = -10;
    /**
     * special application UID used to indicate the Linux Kernel
     */
    public static final int SPECIAL_UID_KERNEL = -11;

    public static final String ITFS_WIFI[] = {"eth+", "wlan+", "tiwlan+", "ra+", "bnep+"};

    public static final String ITFS_3G[] = {"rmnet+", "pdp+", "uwbr+", "wimax+", "vsnet+",
            "rmnet_sdio+", "ccmni+", "qmi+", "svnet0+", "ccemni+",
            "wwan+", "cdma_rmnet+", "usb+", "rmnet_usb+", "clat4+", "cc2mni+", "bond1+", "rmnet_smux+", "ccinet+", "v4-rmnet+", "seth_w+"};

    public static final String ITFS_VPN[] = {"tun+", "ppp+", "tap+"};


    public void saveRules(Context context) {

    }

    public static void alert(Context ctx, CharSequence msg) {
        if (ctx != null) {
            new AlertDialog.Builder(ctx)
                    .setNeutralButton(android.R.string.ok, null)
                    .setMessage(msg)
                    .show();
        }
    }

    public static boolean applySavedIpTablesRules(Context context) {
        assertBinaries(context);
        int code;
        final StringBuilder script = new StringBuilder();
        script.append(scriptHeader(context));
        script.append("" +
                "$IPTABLES --version || exit 1\n" +
                "# Create the " + CHAIN_NAME + " chains if necessary\n" +
                "$IPTABLES -L " + CHAIN_NAME + " >/dev/null 2>/dev/null || $IPTABLES --new " + CHAIN_NAME + " || exit 2\n" +
                "$IPTABLES -L " + CHAIN_NAME + "-3g >/dev/null 2>/dev/null || $IPTABLES --new " + CHAIN_NAME + "-3g || exit 3\n" +
                "$IPTABLES -L " + CHAIN_NAME + "-wifi >/dev/null 2>/dev/null || $IPTABLES --new " + CHAIN_NAME + "-wifi || exit 4\n" +
                "$IPTABLES -L " + CHAIN_NAME + "-reject >/dev/null 2>/dev/null || $IPTABLES --new " + CHAIN_NAME + "-reject || exit 5\n" +
                "# Add FireWall chain to OUTPUT chain if necessary\n" +
                "$IPTABLES -L OUTPUT | $GREP -q " + CHAIN_NAME + " || $IPTABLES -A OUTPUT -j  " + CHAIN_NAME + " || exit 6\n" +
                "# Flush existing rules\n" +
                "$IPTABLES -F " + CHAIN_NAME + " || exit 7\n" +

                "$IPTABLES -F " + CHAIN_NAME + "-3g || exit 8\n" +
                "$IPTABLES -F " + CHAIN_NAME + "-wifi || exit 9\n" +
                "$IPTABLES -F " + CHAIN_NAME + "-reject || exit 10\n" +
                "");
        script.append("" +
                "# Create the reject rule (log disabled)\n" +
                "$IPTABLES -A " + CHAIN_NAME + "-reject -j  REJECT || exit 11\n" +
                "");
        // NOTE: we still need to open a hole to let WAN-only UIDs talk to a DNS server
        // on the LAN
        script.append("$IPTABLES -A " + CHAIN_NAME + " -p udp --dport 53 -j RETURN\n");
        script.append("# Main rules (per interface)\n");
        for (final String itf : ITFS_3G) {
            script.append("$IPTABLES -A " + CHAIN_NAME + " -o ").append(itf).append(" -j  " + CHAIN_NAME + "-3g || exit\n");
        }
        for (final String itf : ITFS_WIFI) {
            script.append("$IPTABLES -A " + CHAIN_NAME + " -o ").append(itf).append(" -j  " + CHAIN_NAME + "-wifi || exit\n");
        }
        script.append("# Filtering rules\n");
        //TODO 暂时只做白名单
        boolean whiteList = true;
        boolean blackList = !whiteList;
        final String targetRule = "RETURN";
        final List<Integer> flowUidList = AppInfoUtil.getUidListFromPref(FlowSharePreferenceHelper.getString(Constants.SP_KEY_FLOW_SELECT_UID, ""));
        final List<Integer> wifiUidList = AppInfoUtil.getUidListFromPref(FlowSharePreferenceHelper.getString(Constants.SP_KEY_WIFI_SELECT_UID, ""));
        final boolean allFlow = flowUidList.indexOf(SPECIAL_UID_ANY) >= 0;
        final boolean allWifi = wifiUidList.indexOf(SPECIAL_UID_ANY) >= 0;
        if (whiteList && !allWifi) {
            int uid = android.os.Process.getUidForName("dhcp");
            if (uid != -1) {
                script.append("# dhcp user\n");
                script.append("$IPTABLES -A " + CHAIN_NAME + "-wifi -m owner --uid-owner ").append(uid).append(" -j  RETURN || exit\n");
            }
            uid = android.os.Process.getUidForName("wifi");
            if (uid != -1) {
                script.append("# wifi user\n");
                script.append("$IPTABLES -A " + CHAIN_NAME + "-wifi -m owner --uid-owner ").append(uid).append(" -j  RETURN || exit\n");
            }
        }
        if (allFlow) {
            if (blackList) {
                script.append("$IPTABLES -A " + CHAIN_NAME + "-3g -j ").append(targetRule).append(" || exit\n");
            }
        } else {
            for (Integer uid : flowUidList) {
                if (uid >= 0) {
                    script.append("$IPTABLES -A " + CHAIN_NAME + "-3g -m owner --uid-owner ").append(uid).append(" -j ").append(targetRule).append(" || exit\n");
                }
            }
        }
        if (allWifi) {
            if (blackList) {
                script.append("$IPTABLES -A " + CHAIN_NAME + "-wifi -j ").append(targetRule).append(" || exit\n");
            }
        } else {
            for (Integer uid : wifiUidList) {
                if (uid >= 0) {
                    script.append("$IPTABLES -A " + CHAIN_NAME + "-wifi -m owner --uid-owner ").append(uid).append(" -j ").append(targetRule).append(" || exit\n");
                }
            }
        }
        if (whiteList) {
            if (!allFlow) {
                if (flowUidList.indexOf(SPECIAL_UID_KERNEL) >= 0) {
                    script.append("# hack to allow kernel packets on white-list\n");
                    script.append("$IPTABLES -A " + CHAIN_NAME + "-3g -m owner --uid-owner 0:999999999 -j  " + CHAIN_NAME + "-reject || exit\n");
                } else {
                    script.append("$IPTABLES -A " + CHAIN_NAME + "-3g -j  " + CHAIN_NAME + "-reject || exit\n");
                }
            }
            if (!allWifi) {
                if (wifiUidList.indexOf(SPECIAL_UID_KERNEL) >= 0) {
                    script.append("# hack to allow kernel packets on white-list\n");
                    script.append("$IPTABLES -A " + CHAIN_NAME + "-wifi -m owner --uid-owner 0:999999999 -j  " + CHAIN_NAME + "-reject || exit\n");
                } else {
                    script.append("$IPTABLES -A " + CHAIN_NAME + "-wifi -j  " + CHAIN_NAME + "-reject || exit\n");
                }
            }
        } else {
            if (flowUidList.indexOf(SPECIAL_UID_KERNEL) >= 0) {
                script.append("# hack to BLOCK kernel packets on black-list\n");
                script.append("$IPTABLES -A " + CHAIN_NAME + "-3g -m owner --uid-owner 0:999999999 -j  RETURN || exit\n");
                script.append("$IPTABLES -A " + CHAIN_NAME + "-3g -j  " + CHAIN_NAME + "-reject || exit\n");
            }
            if (wifiUidList.indexOf(SPECIAL_UID_KERNEL) >= 0) {
                script.append("# hack to BLOCK kernel packets on black-list\n");
                script.append("$IPTABLES -A " + CHAIN_NAME + "-wifi -m owner --uid-owner 0:999999999 -j  RETURN || exit\n");
                script.append("$IPTABLES -A " + CHAIN_NAME + "-wifi -j  " + CHAIN_NAME + "-reject || exit\n");
            }
        }
        final StringBuilder res = new StringBuilder();
        try {
            code = runScriptAsRoot(context, script.toString(), res);
            if (code != 0) {
                String msg = res.toString();
                Log.e("fireWall", msg);
                // Remove unnecessary help message from output
                alert(context, "Error applying iptables rules. Exit code: " + code + "\n\n" + msg.trim());
            } else {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean purgeIpTables(Context context, boolean showErrors) {
        assertBinaries(context);
        StringBuilder res = new StringBuilder();
        int code;
        try {
            code = runScriptAsRoot(context, scriptHeader(context)
                    + "$IPTABLES -F " + CHAIN_NAME + "\n"

                    + "$IPTABLES -F " + CHAIN_NAME + "-reject\n"
                    + "$IPTABLES -F " + CHAIN_NAME + "-3g\n"
                    + "$IPTABLES -F " + CHAIN_NAME + "-wifi\n"
                    + "$IPTABLES -P OUTPUT ACCEPT\n"
                    + "$IPTABLES -D OUTPUT -j " + CHAIN_NAME, res);
            if (code == -1) {
                if (showErrors)
                    alert(context, "error purging iptables. exit code: " + code + "\n" + res);
                return false;
            }
        } catch (IOException e) {
            if (showErrors) alert(context, "error purging iptables: " + e);
            return false;
        }
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
        return ""
                + "IPTABLES=iptables\n"
                + "IP6TABLES=ip6tables\n"
                + "BUSYBOX=busybox\n"
                + "GREP=grep\n"
                + "ECHO=echo\n"
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

    public static int runScriptAsRoot(Context ctx, String script, StringBuilder res) throws IOException {
        return runScriptAsRoot(ctx, script, res, 40000);
    }

    public static int runScriptAsRoot(Context ctx, String script, StringBuilder res, long timeout) {
        return runScript(ctx, script, res, timeout, true);
    }

    public static int runScript(Context ctx, String script, StringBuilder res, long timeout, boolean asRoot) {
        final File file = new File(ctx.getCacheDir(), SCRIPT_FILE);
        final ScriptRunner runner = new ScriptRunner(file, script, res, asRoot);
        runner.start();
        try {
            if (timeout > 0) {
                runner.join(timeout);
            } else {
                runner.join();
            }
            if (runner.isAlive()) {
                // Timed-out
                runner.interrupt();
                runner.join(150);
                runner.destroy();
                runner.join(50);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return runner.exitCode;
    }

    /**
     * Display iptables rules output
     *
     * @param ctx application context
     */
    public static void showIpTableRules(Context ctx) {
        try {
            final StringBuilder res = new StringBuilder();
            runScriptAsRoot(ctx, scriptHeader(ctx) +
                    "$ECHO $IPTABLES\n" +
                    "$IPTABLES -L -v\n", res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
