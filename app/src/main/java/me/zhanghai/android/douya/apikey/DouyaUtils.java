/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.douya.apikey;

import android.content.Context;

public class DouyaUtils {

    public static final String PACKAGE_NAME = "me.zhanghai.android.douya";

    private DouyaUtils() {}

    public static boolean isInstalled(Context context) {
        return PackageUtils.isPackageInstalled(PACKAGE_NAME, context);
    }

    public static void installApp(Context context) {
        PackageUtils.installPackage(PACKAGE_NAME, context);
    }

    public static void setApiKeyAndSecret(String apiKey, String apiSecret) {
        // TODO
    }

    public static void launch(Context context) {
        context.startActivity(IntentUtils.makeLaunchApp(PACKAGE_NAME, context));
    }
}
