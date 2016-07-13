/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.douya.apikey;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.title)
    TextView mTitleText;
    @BindView(R.id.forward)
    Button mForwardButton;

    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        mForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFragment != null) {
                    ((WizardContentFragment) mFragment).onForward();
                }
            }
        });

        if (savedInstanceState == null) {
            mFragment = new InstallDouyaFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, mFragment)
                    .commit();
        } else {
            mFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        }

//        setupApiKeyAndSecret();
    }

//    private void setupApiKeyAndSecret() {
//        try {
//            initApiKeyAndSecretOrThrow();
//            mApiKeyText.setText(mApiKey);
//            mApiSecretText.setText(mApiSecret);
//        } catch (BadPaddingException | IllegalBlockSizeException
//                | InvalidAlgorithmParameterException | InvalidKeyException
//                | PackageManager.NameNotFoundException | NoSuchAlgorithmException
//                | NoSuchPaddingException | UnsupportedEncodingException e) {
//            e.printStackTrace();
//            if (e instanceof PackageManager.NameNotFoundException) {
//                mApiKeyText.setText(R.string.error_frodo_not_found);
//            } else if (e instanceof BadPaddingException || e instanceof IllegalBlockSizeException
//                    || e instanceof InvalidAlgorithmParameterException
//                    || e instanceof InvalidKeyException) {
//                mApiKeyText.setText(R.string.error_decryption);
//            } else {
//                mApiKeyText.setText(R.string.error_unexpected);
//            }
//            mApiSecretText.setText(getStackTrace(e));
//        }
//    }

    public void setTitleText(int textResId) {
        mTitleText.setText(textResId);
    }

    public void setForwardEnabled(boolean enabled) {
        mForwardButton.setEnabled(enabled);
    }

    public void setForwardText(int textResId) {
        mForwardButton.setText(textResId);
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                .replace(R.id.fragment_container, fragment)
                // FIXME: Not working
                .commit();
        mFragment = fragment;
    }
}
