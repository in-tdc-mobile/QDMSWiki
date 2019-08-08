package com.mariapps.qdmswiki.custom;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.Gravity;
import android.widget.RelativeLayout;
import com.mariapps.qdmswiki.utils.ViewUtils;

/**
 * Created by elby.samson on 03,January,2019
 */
public class CustomProgressIndicator extends RelativeLayout {

    private MainProgIndicator mainProgIndicator;
    private ObjectAnimator objectAnimator;
    private int number;

    public CustomProgressIndicator(Context context, int indicatorHeight, int color, int radius, int number) {
        super(context);
        initialize(indicatorHeight, color, radius, number);
    }

    private void initialize(int indicatorHeight, int color, int radius, int number) {
        this.number = number;
        this.mainProgIndicator = new MainProgIndicator(getContext(), indicatorHeight, color, radius);
        LayoutParams layoutParams = new LayoutParams(ViewUtils.px2dp(getContext(), indicatorHeight * 8),
                ViewUtils.px2dp(getContext(), indicatorHeight * 8));
        this.setLayoutParams(layoutParams);
        this.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        this.addView(mainProgIndicator);
        startAnim(0, 0);
        removeAnim();
        this.setAlpha(0f);
    }

    public void startAnim(final long animationDuration, final long delay) {
        objectAnimator = ObjectAnimator.ofFloat(this, "rotation", (number * 15), -360 + (number * 15));
        objectAnimator.setInterpolator(new CustomProgressInterpolator());
        objectAnimator.setDuration(animationDuration);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setRepeatCount(2);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                CustomProgressIndicator.this.setAlpha(1f);
                startAlphaAnimation(animationDuration);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                removeAnim();
                startAnim(animationDuration, 0);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        objectAnimator.setStartDelay(delay);
        objectAnimator.start();
    }

    public void startAlphaAnimation(long animationDuration) {
        this.animate().alpha(0).setDuration(animationDuration / 12).setStartDelay(2 * animationDuration);
    }

    public void removeAnim() {
        this.animate().alpha(0f).setDuration(0).setStartDelay(0);
        objectAnimator.removeAllListeners();
        objectAnimator.cancel();
        objectAnimator.end();
    }
}
