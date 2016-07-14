/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.douya.apikey.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class IntentUtils {

    private IntentUtils() {}

    public static Intent makeLaunchApp(String packageName, Context context) {
        return context.getPackageManager().getLaunchIntentForPackage(packageName);
    }

    public static Intent makeView(Uri uri) {
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    public static Intent makeViewAppInMarket(String packageName) {
        return makeView(Uri.parse("market://details?id=" + packageName));
    }
}
