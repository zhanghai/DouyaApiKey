/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.douya.apikey.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class DouyaUtils {

    private static final String PACKAGE_NAME = "me.zhanghai.android.douya";
    private static final int MINIMUM_VERSION_CODE = 2;

    private static final String RECEIVER_CLASS_NAME = PACKAGE_NAME
            + ".network.api.credential.SetApiCredentialReceiver";
    private static final String EXTRA_API_V2_API_KEY =
            "me.zhanghai.android.douya.intent.extra.API_V2_API_KEY";
    private static final String EXTRA_API_V2_API_SECRET =
            "me.zhanghai.android.douya.intent.extra.API_V2_API_SECRET";
    private static final String EXTRA_FRODO_API_KEY =
            "me.zhanghai.android.douya.intent.extra.FRODO_API_KEY";
    private static final String EXTRA_FRODO_API_SECRET =
            "me.zhanghai.android.douya.intent.extra.FRODO_API_SECRET";

    private DouyaUtils() {}

    public static boolean isInstalled(Context context) {
        return PackageUtils.isInstalled(PACKAGE_NAME, context);
    }

    public static boolean hasMinimumVersion(Context context) {
        return PackageUtils.hasMinimumVersion(PACKAGE_NAME, MINIMUM_VERSION_CODE, context);
    }

    public static void installApp(Context context) {
        PackageUtils.install(PACKAGE_NAME, context);
    }

    public static void setApiKeyAndSecret(String apiV2apiKey, String apiV2apiSecret,
                                          String frodoApiKey, String frodoApiSecret,
                                          Context context) {
        context.sendBroadcast(new Intent()
                .setComponent(new ComponentName(PACKAGE_NAME, RECEIVER_CLASS_NAME))
                .putExtra(EXTRA_API_V2_API_KEY, apiV2apiKey)
                .putExtra(EXTRA_API_V2_API_SECRET, apiV2apiSecret)
                .putExtra(EXTRA_FRODO_API_KEY, frodoApiKey)
                .putExtra(EXTRA_FRODO_API_SECRET, frodoApiSecret)
                .addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES));
    }

    public static void launch(Context context) {
        context.startActivity(IntentUtils.makeLaunchApp(PACKAGE_NAME, context));
    }
}
