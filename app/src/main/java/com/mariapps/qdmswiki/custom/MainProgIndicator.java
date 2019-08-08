package com.mariapps.qdmswiki.custom;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.LinearLayout;
import com.mariapps.qdmswiki.utils.ViewUtils;

/**
 * Created by aruna.ramakrishnan on 08-08-2019
 */
public class MainProgIndicator extends View {

    private int color;

    public MainProgIndicator(Context context, int indicatorHeight, int color, int radius) {
        super(context);
        this.color = color;
        initialize(indicatorHeight, radius);
    }

    private void initialize(int indicatorHeight, int radius) {
        this.setBackground(getCube(radius));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewUtils.px2dp(getContext(), indicatorHeight), ViewUtils.px2dp(getContext(), indicatorHeight));
//        layoutParams.rightMargin = Utils.px2dp(getContext(), (int) (1.7f * indicatorHeight));
        this.setLayoutParams(layoutParams);
//        startAnim(0, 0);
//        removeAnim();
    }

    private GradientDrawable getCube(int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(color);
        drawable.setCornerRadius(ViewUtils.px2dp(getContext(), radius));
        return drawable;
    }
}
