/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.douya.apikey.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import me.zhanghai.android.douya.apikey.R;

public class DoubanUtils {

    public static final String PACKAGE_NAME = "com.douban.frodo";

    private DoubanUtils() {}

    public static boolean isInstalled(Context context) {
        return PackageUtils.isPackageInstalled(PACKAGE_NAME, context);
    }

    public static void installApp(Context context) {
        PackageUtils.installPackage(PACKAGE_NAME, context);
    }

    public static GetApiKeyAndSecretReturnValue getApiKeyAndSecret(Context context) {

        GetApiKeyAndSecretReturnValue returnValue = new GetApiKeyAndSecretReturnValue();

        try {
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(PACKAGE_NAME,
                    PackageManager.GET_SIGNATURES);
            String password = Base64.encodeToString(packageInfo.signatures[0].toByteArray(), 0);

            StringBuilder builder = new StringBuilder(password);
            while (builder.length() < 16) {
                builder.append("\u0000");
            }
            if (builder.length() > 16) {
                builder.setLength(16);
            }
            SecretKeySpec key = new SecretKeySpec(builder.toString().getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec("DOUBANFRODOAPPIV".getBytes()));

            returnValue.apiKey = new String(cipher.doFinal(Base64.decode(
                    "74CwfJd4+7LYgFhXi1cx0IQC35UQqYVFycCE+EVyw1E=", Base64.DEFAULT)));
            returnValue.apiSecret = new String(cipher.doFinal(Base64.decode(
                    "MkFm2XdTnoPKFKXu1gveBQ==", Base64.DEFAULT)));
            returnValue.isSuccessful = true;
            return returnValue;
        } catch (BadPaddingException | IllegalBlockSizeException
                | InvalidAlgorithmParameterException | InvalidKeyException
                | PackageManager.NameNotFoundException | NoSuchAlgorithmException
                | NoSuchPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            returnValue.isSuccessful = false;
            if (e instanceof PackageManager.NameNotFoundException) {
                returnValue.error = context.getString(
                        R.string.get_api_key_and_secret_error_frodo_not_found);
            } else if (e instanceof BadPaddingException || e instanceof IllegalBlockSizeException
                    || e instanceof InvalidAlgorithmParameterException
                    || e instanceof InvalidKeyException) {
                returnValue.error = context.getString(
                        R.string.get_api_key_and_secret_error_decryption);
            } else {
                returnValue.error = ThrowableUtils.getStackTrace(e);
            }
            return returnValue;
        }
    }

    public static class GetApiKeyAndSecretReturnValue {
        public boolean isSuccessful;
        public String apiKey;
        public String apiSecret;
        public String error;
    }
}
