/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.douya.apikey.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import me.zhanghai.android.douya.apikey.R;

public class RatioFrameLayout extends FrameLayout {

    private float mRatio;

    public RatioFrameLayout(Context context) {
        super(context);

        init(getContext(), null, 0, 0);
    }

    public RatioFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(getContext(), attrs, 0, 0);
    }

    public RatioFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(getContext(), attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RatioFrameLayout(Context context, AttributeSet attrs, int defStyleAttr,
                            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(getContext(), attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RatioFrameLayout,
                defStyleAttr, defStyleRes);
        String ratioString = a.getString(R.styleable.RatioFrameLayout_ratio);
        a.recycle();

        if (!TextUtils.isEmpty(ratioString)) {
            String[] numbers = ratioString.split(":");
            if (numbers.length != 2 || !TextUtils.isDigitsOnly(numbers[0])
                    || !TextUtils.isDigitsOnly(numbers[1])) {
                throw new IllegalArgumentException(
                        "app:ratio should be a string in the format \"<width>:<height>\": "
                                + ratioString);
            }
            mRatio = (float) Integer.parseInt(numbers[0]) / Integer.parseInt(numbers[1]);
        }
    }

    public float getRatio() {
        return mRatio;
    }

    public void setRatio(float ratio) {
        if (mRatio != ratio) {
            mRatio = ratio;
            requestLayout();
            invalidate();
        }
    }

    public void setRatio(float width, float height) {
        setRatio(width / height);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mRatio > 0) {
            int width;
            int height;
            if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
                height = getMeasuredHeight();
                width = Math.round(mRatio * height);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                    width = Math.max(width, getMinimumWidth());
                }
            } else {
                width = getMeasuredWidth();
                height = Math.round(width / mRatio);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                    height = Math.max(height, getMinimumHeight());
                }
            }

            setMeasuredDimension(width, height);
        }
    }
}
