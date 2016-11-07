/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.douya.apikey.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.douya.apikey.R;
import me.zhanghai.android.douya.apikey.util.AnimationUtils;
import me.zhanghai.android.douya.apikey.util.DoubanUtils;
import me.zhanghai.android.douya.apikey.util.DouyaUtils;
import me.zhanghai.android.douya.apikey.util.ToastUtils;

public class ApiKeyFragment extends Fragment implements WizardContentFragment {

    private static final String KEY_SHOWING_CUSTOM = ApiKeyFragment.class.getName()
            + ".showingCustom";

    private static final int REFRESH_INTERVAL_MILLI = 1000;

    @BindView(R.id.douban_layout)
    ViewGroup mDoubanLayout;
    @BindView(R.id.douban_refresh)
    Button mDoubanRefreshButton;
    @BindView(R.id.douban_custom)
    Button mDoubanCustomButton;
    @BindView(R.id.custom_layout)
    ViewGroup mCustomLayout;
    @BindView(R.id.custom_douban)
    Button mCustomDoubanButton;
    @BindView(R.id.custom_api_v2_api_key)
    EditText mCustomApiV2ApiKeyEdit;
    @BindView(R.id.custom_api_v2_api_secret)
    EditText mCustomApiV2ApiSecretEdit;
    @BindView(R.id.custom_frodo_api_key)
    EditText mCustomFrodoApiKeyEdit;
    @BindView(R.id.custom_frodo_api_secret)
    EditText mCustomFrodoApiSecretEdit;

    private boolean mShowingCustom;

    private final Handler mHandler = new Handler();
    private final Runnable mRefreshDoubanRunnable = new Runnable() {
        @Override
        public void run() {
            refreshDouban(false);
            mHandler.postDelayed(this, REFRESH_INTERVAL_MILLI);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.api_key_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mShowingCustom = savedInstanceState.getBoolean(KEY_SHOWING_CUSTOM);
            if (mShowingCustom) {
                mDoubanLayout.setVisibility(View.GONE);
                mCustomLayout.setVisibility(View.VISIBLE);
            }
        }

        MainActivity activity = (MainActivity) getActivity();
        activity.setTitleText(R.string.api_key_title);

        mDoubanRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshDouban(true);
            }
        });
        mDoubanCustomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setShowingCustom(true);
            }
        });
        mCustomDoubanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setShowingCustom(false);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(KEY_SHOWING_CUSTOM, mShowingCustom);
    }

    @Override
    public void onResume() {
        super.onResume();

        mRefreshDoubanRunnable.run();
    }

    @Override
    public void onPause() {
        super.onPause();

        mHandler.removeCallbacks(mRefreshDoubanRunnable);
    }

    @Override
    public void onForward() {

        MainActivity activity = (MainActivity) getActivity();
        if (mShowingCustom) {

            String apiV2ApiKey = mCustomApiV2ApiKeyEdit.getText().toString();
            String apiV2ApiSecret = mCustomApiV2ApiSecretEdit.getText().toString();
            String frodoApiKey = mCustomFrodoApiKeyEdit.getText().toString();
            String frodoApiSecret = mCustomFrodoApiSecretEdit.getText().toString();
            if (apiV2ApiKey.isEmpty() || apiV2ApiSecret.isEmpty() || frodoApiKey.isEmpty()
                    || frodoApiSecret.isEmpty()) {
                ToastUtils.show(R.string.api_key_error_empty, activity);
                return;
            }
            DouyaUtils.setApiKeyAndSecret(apiV2ApiKey, apiV2ApiSecret, frodoApiKey, frodoApiSecret,
                    activity);
            activity.replaceFragment(new FinishFragment());

        } else {

            refreshDouban(false);
            if (isDoubanInstalled()) {
                DoubanUtils.GetApiKeyAndSecretReturnValue returnValue =
                        DoubanUtils.getApiKeyAndSecret(activity);
                if (!returnValue.isSuccessful) {
                    ToastUtils.show(returnValue.error, activity);
                    return;
                }
                DouyaUtils.setApiKeyAndSecret(returnValue.apiV2ApiKey, returnValue.apiV2ApiSecret,
                        returnValue.frodoApiKey, returnValue.frodoApiSecret, activity);
                activity.replaceFragment(new FinishFragment());
            } else {
                DoubanUtils.installApp(activity);
            }
        }
    }

    private void setShowingCustom(boolean showingCustom) {

        if (mShowingCustom == showingCustom) {
            return;
        }

        if (showingCustom) {
            AnimationUtils.fadeOutThenFadeIn(mDoubanLayout, mCustomLayout);
            MainActivity activity = (MainActivity) getActivity();
            activity.setForwardText(R.string.api_key_forward_set);
        } else {
            AnimationUtils.fadeOutThenFadeIn(mCustomLayout, mDoubanLayout);
            refreshDouban(false);
        }

        mShowingCustom = showingCustom;
    }

    private void refreshDouban(boolean showToast) {
        if (mShowingCustom) {
            return;
        }
        MainActivity activity = (MainActivity) getActivity();
        activity.setForwardEnabled(true);
        if (isDoubanInstalled()) {
            activity.setForwardText(R.string.api_key_forward_set);
            mDoubanRefreshButton.setText(R.string.api_key_douban_action_refresh_forward);
        } else {
            activity.setForwardText(R.string.api_key_forward_install);
            mDoubanRefreshButton.setText(R.string.api_key_douban_action_refresh_refresh);
            if (showToast) {
                ToastUtils.show(R.string.api_key_douban_toast_not_installed, activity);
            }
        }
    }

    private boolean isDoubanInstalled() {
        return DoubanUtils.isInstalled(getActivity());
    }
}
