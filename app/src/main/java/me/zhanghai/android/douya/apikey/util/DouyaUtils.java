/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.douya.apikey.util;

import android.content.Context;
import android.content.Intent;

public class DouyaUtils {

    private static final String PACKAGE_NAME = "me.zhanghai.android.douya";

    private static final String ACTION_SET_API_CREDENTIAL =
            "me.zhanghai.android.douya.intent.action.SET_API_CREDENTIAL";
    private static final String EXTRA_API_KEY = "me.zhanghai.android.douya.intent.extra.API_KEY";
    private static final String EXTRA_API_SECRET =
            "me.zhanghai.android.douya.intent.extra.API_SECRET";

    private DouyaUtils() {}

    public static boolean isInstalled(Context context) {
        return PackageUtils.isPackageInstalled(PACKAGE_NAME, context);
    }

    public static void installApp(Context context) {
        PackageUtils.installPackage(PACKAGE_NAME, context);
    }

    public static void setApiKeyAndSecret(String apiKey, String apiSecret, Context context) {
        Intent intent = new Intent(ACTION_SET_API_CREDENTIAL)
                .putExtra(EXTRA_API_KEY, apiKey)
                .putExtra(EXTRA_API_SECRET, apiSecret);
        context.sendBroadcast(intent);
    }

    public static void launch(Context context) {
        context.startActivity(IntentUtils.makeLaunchApp(PACKAGE_NAME, context));
    }
}
