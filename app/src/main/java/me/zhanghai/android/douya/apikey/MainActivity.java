/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.douya.apikey;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;
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

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String DOUBAN_PACKAGE_NAME = "com.douban.frodo";

    @Bind(R.id.api_key)
    TextView mApiKeyText;
    @Bind(R.id.api_secret)
    TextView mApiSecretText;

    private String mApiKey;
    private String mApiSecret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        setupApiKeyAndSecret();
    }

    private void setupApiKeyAndSecret() {
        try {
            initApiKeyAndSecretOrThrow();
            mApiKeyText.setText(mApiKey);
            mApiSecretText.setText(mApiSecret);
        } catch (BadPaddingException | IllegalBlockSizeException
                | InvalidAlgorithmParameterException | InvalidKeyException
                | PackageManager.NameNotFoundException | NoSuchAlgorithmException
                | NoSuchPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            if (e instanceof PackageManager.NameNotFoundException) {
                mApiKeyText.setText(R.string.error_frodo_not_found);
            } else if (e instanceof BadPaddingException || e instanceof IllegalBlockSizeException
                    || e instanceof InvalidAlgorithmParameterException
                    || e instanceof InvalidKeyException) {
                mApiKeyText.setText(R.string.error_decryption);
            } else {
                mApiKeyText.setText(R.string.error_unexpected);
            }
            mApiSecretText.setText(getStackTrace(e));
        }
    }

    private void initApiKeyAndSecretOrThrow() throws BadPaddingException, IllegalBlockSizeException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            PackageManager.NameNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException,
            UnsupportedEncodingException {

        @SuppressLint("PackageManagerGetSignatures")
        PackageInfo packageInfo = getPackageManager().getPackageInfo(DOUBAN_PACKAGE_NAME,
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

        mApiKey = new String(cipher.doFinal(Base64.decode(
                "74CwfJd4+7LYgFhXi1cx0IQC35UQqYVFycCE+EVyw1E=", Base64.DEFAULT)));
        mApiSecret = new String(cipher.doFinal(Base64.decode("MkFm2XdTnoPKFKXu1gveBQ==",
                Base64.DEFAULT)));
    }

    private String getStackTrace(Throwable throwable) {
        StringWriter writer = new StringWriter();
        throwable.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }
}
