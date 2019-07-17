package com.mariapps.qdmswiki.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import com.mariapps.qdmswiki.R;

/**
 * Created by elby.samson on 03,January,2019
 */
public class AnimateLinearLayout extends LinearLayout {

    private boolean startAnimation;

    public AnimateLinearLayout(Context context) {
        super(context);
        init(context, null);
    }

    public AnimateLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AnimateLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LinearLayoutAnimation);
        startAnimation = ta.getBoolean(R.styleable.LinearLayoutAnimation_startAnimation, false);
        ta.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (startAnimation) {
            startAnimation();
        }
    }

    public void startAnimation() {

        int startOffset = 0;

        for (int i = 0; i < getChildCount(); i++) {

            startOffset += 100;
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_view_showing);
            animation.setStartOffset(startOffset);
            getChildAt(i).startAnimation(animation);
        }

        startAnimation = false;
    }
}