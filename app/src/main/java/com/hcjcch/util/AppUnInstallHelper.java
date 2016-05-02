package com.hcjcch.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/5/2 02:01
 */

public class AppUnInstallHelper {
    public static void unInstallApp(Context context, String packageName) {
        Uri packageUri = Uri.parse("package:" + packageName);
        Intent uninstallIntent =
                new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageUri);
        context.startActivity(uninstallIntent);
    }
}
