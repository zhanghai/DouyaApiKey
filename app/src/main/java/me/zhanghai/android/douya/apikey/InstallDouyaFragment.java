/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.douya.apikey;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InstallDouyaFragment extends Fragment implements WizardContentFragment {

    private static final int REFRESH_INTERVAL_MILLI = 1000;

    @BindView(R.id.refresh)
    Button mRefreshButton;

    private final Handler mHandler = new Handler();
    private final Runnable mRefreshRunnable = new Runnable() {
        @Override
        public void run() {
            refresh();
            mHandler.postDelayed(this, REFRESH_INTERVAL_MILLI);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.install_douya_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        activity.setTitleText(R.string.install_douya_title);

        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        mRefreshRunnable.run();
    }

    @Override
    public void onPause() {
        super.onPause();

        mHandler.removeCallbacks(mRefreshRunnable);
    }

    @Override
    public void onForward() {
        refresh();
        MainActivity activity = (MainActivity) getActivity();
        if (isDouyaInstalled()) {
            activity.replaceFragment(new ApiKeyFragment());
        } else {
            DouyaUtils.installApp(activity);
        }
    }

    private void refresh() {
        MainActivity activity = (MainActivity) getActivity();
        if (isDouyaInstalled()) {
            mRefreshButton.setText(R.string.install_douya_action_refresh_forward);
            activity.setForwardText(R.string.install_douya_forward_forward);
        } else {
            mRefreshButton.setText(R.string.install_douya_action_refresh_refresh);
            activity.setForwardText(R.string.install_douya_forward_install);
        }
    }

    private boolean isDouyaInstalled() {
        return DouyaUtils.isInstalled(getActivity());
    }
}
