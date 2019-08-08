package com.mariapps.qdmswiki.custom;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

import com.mariapps.qdmswiki.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

public class CustomProgressBar extends RelativeLayout {

    private static final int INTERVAL_DEF = 150;
    private static final int INDICATOR_COUNT_DEF = 5;
    private static final int ANIMATION_DURATION_DEF = 1800;
    private static final int INDICATOR_HEIGHT_DEF = 7;
    private static final int INDICATOR_RADIUS_DEF = 0;
    public static final int RIGHT_DIRECTION = 1;
    public static final int LEFT_DIRECTION = -1;

    /**
     * Dots amount
     */
    private int dotAmount;

    /**
     * Drawing tools
     */
    private Paint primaryPaint;
    private Paint startPaint;
    private Paint endPaint;

    /**
     * Animation tools
     */
    private int interval;
    private int animationDuration;
    private int indicatorHeight;
    private int indicatorColor;
    private int indicatorRadius;
    private long animationTime;
    private float animatedRadius;
    private boolean isFirstLaunch = true;
    private ValueAnimator startValueAnimator;
    private ValueAnimator endValueAnimator;

    /**
     * Circle size
     */
    private float dotRadius;
    private float bounceDotRadius;
    /**
     * Circle coordinates
     */
    private float xCoordinate;
    private int dotPosition;

    /**
     * Colors
     */
    private int startColor;
    private int endColor;

    /**
     * This value detect direction of circle animation direction
     * {@link CustomProgressBar#RIGHT_DIRECTION} and {@link CustomProgressBar#LEFT_DIRECTION}
     * */
    private int animationDirection;
    private Handler handler;
    private int progressBarCount = 0;
    private ArrayList<CustomProgressIndicator> customProgressIndicators;
    private boolean isShowing = false;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(attrs);
        init();
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
        init();
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
        init();
    }

    public CustomProgressBar(Context context) {
        super(context);
        initialize(null);
        init();
    }
    private void initialize(@Nullable AttributeSet attrs) {
        this.handler = new Handler();
        this.setRotation(-25);
        setAttributes(attrs);
        initializeIndicators();
    }

    private void initializeIndicators() {
        this.removeAllViews();
        ArrayList<CustomProgressIndicator> CustomProgressIndicators = new ArrayList<>();
        for (int i = 0; i < INDICATOR_COUNT_DEF; i++) {
            CustomProgressIndicator customProgressIndicator = new CustomProgressIndicator(getContext(), indicatorHeight, indicatorColor, indicatorRadius, i);
//                wp10Indicator.setRotation(i * 14);
            CustomProgressIndicators.add(customProgressIndicator);
            this.addView(customProgressIndicator);
        }
        this.customProgressIndicators = CustomProgressIndicators;
    }


    private void setAttributes(AttributeSet attributeSet) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.CustomProgressBar);
        interval = typedArray.getInt(R.styleable.CustomProgressBar_interval, INTERVAL_DEF);
        animationDuration = typedArray.getInt(R.styleable.CustomProgressBar_animationDuration, ANIMATION_DURATION_DEF);
        indicatorHeight = typedArray.getInt(R.styleable.CustomProgressBar_indicatorHeight, INDICATOR_HEIGHT_DEF);
        indicatorRadius = typedArray.getInt(R.styleable.CustomProgressBar_indicatorRadius, INDICATOR_RADIUS_DEF);
        indicatorColor = typedArray.getColor(R.styleable.CustomProgressBar_indicatorColor,
                ContextCompat.getColor(getContext(), R.color.colorAccent));
        typedArray.recycle();
    }

    public static int darker(int color, float factor) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        return Color.argb(a,
                Math.max((int) (r * factor), 0),
                Math.max((int) (g * factor), 0),
                Math.max((int) (b * factor), 0));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());

        if (getMeasuredHeight() > getMeasuredWidth()) {
            dotRadius = getMeasuredWidth() / dotAmount / 4;
        } else {
            dotRadius = getMeasuredHeight() / 4;
        }

        bounceDotRadius = dotRadius + (dotRadius / 3);
        float circlesWidth = (dotAmount * (dotRadius * 2)) + dotRadius * (dotAmount - 1);
        xCoordinate = (getMeasuredWidth() - circlesWidth) / 2 + dotRadius;
    }

    private void initializeAttributes(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.CustomProgressBar,
                    0, 0);

            try {
                setDotAmount(a.getInteger(R.styleable.ProgressBarCustom_amount, 10));
                setAnimationTime(animationTime = a.getInteger(
                        R.styleable.ProgressBarCustom_duration,20)
                );
                setStartColor(
                        a.getInteger(
                                R.styleable.ProgressBarCustom_startColor,
                                ContextCompat.getColor(getContext(), R.color.grey_500)
                        )
                );
                setEndColor(
                        a.getInteger(
                                R.styleable.ProgressBarCustom_endColor,
                                ContextCompat.getColor(getContext(), R.color.blue_A200)
                        )
                );
                setAnimationDirection(a.getInt(R.styleable.ProgressBarCustom_animationDirection, 1));
            } finally {
                a.recycle();
            }

        } else {
            setDotAmount(10);
            setAnimationTime(20);
            setStartColor(ContextCompat.getColor(getContext(), R.color.grey_500));
            setEndColor(ContextCompat.getColor(getContext(), R.color.blue_A200));
            setAnimationDirection(1);
        }
    }

    private void init() {
        primaryPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        primaryPaint.setColor(startColor);
        primaryPaint.setStrokeJoin(Paint.Join.ROUND);
        primaryPaint.setStrokeCap(Paint.Cap.ROUND);
        primaryPaint.setStrokeWidth(20);

        startPaint = new Paint(primaryPaint);
        endPaint = new Paint(primaryPaint);

        startValueAnimator = ValueAnimator.ofInt(startColor, endColor);
        startValueAnimator.setDuration(animationTime);
        startValueAnimator.setEvaluator(new ArgbEvaluator());
        startValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                startPaint.setColor(((Integer) animation.getAnimatedValue()));
            }
        });

        endValueAnimator = ValueAnimator.ofInt(endColor, startColor);
        endValueAnimator.setDuration(animationTime);
        endValueAnimator.setEvaluator(new ArgbEvaluator());
        endValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                endPaint.setColor(((Integer) animation.getAnimatedValue()));
            }
        });
    }

    /**
     * setters
     * */
    void setDotAmount(int amount) {
        this.dotAmount = amount;
    }

    void setStartColor(@ColorInt int color) {
        this.startColor = color;
    }

    void setEndColor(@ColorInt int color) {
        this.endColor = color;
    }

    void setAnimationTime(long animationTime) {
        this.animationTime = animationTime;
    }

    private void setDotPosition(int dotPosition) {
        this.dotPosition = dotPosition;
    }

    /**
     * Set amount of dots, it will be restarted your view
     * @param amount number of dots, dot size automatically adjust
     * */
    public void changeDotAmount(int amount) {
        stopAnimation();
        setDotAmount(amount);
        setDotPosition(0);
        reinitialize();
    }

    /**
     * It will be restarted your view
     * */
    public void changeStartColor(@ColorInt int color) {
        stopAnimation();
        setStartColor(color);
        reinitialize();
    }

    /**
     * It will be restarted your view
     * */
    public void changeEndColor(@ColorInt int color) {
        stopAnimation();
        setEndColor(color);
        reinitialize();
    }

    /**
     * It will be restarted your view
     * */
    public void changeAnimationTime(long animationTime) {
        stopAnimation();
        setAnimationTime(animationTime);
        reinitialize();
    }

    /**
     * Change animation direction, doesn't restart view.
     * @param animationDirection left or right animation direction
     * */
    public void changeAnimationDirection(@AnimationDirection int animationDirection) {
        setAnimationDirection(animationDirection);
    }

    public int getAnimationDirection() {
        return animationDirection;
    }

    void setAnimationDirection(int direction) {
        this.animationDirection = direction;
    }

    /**
     * Reinitialize animators instances
     * */
    void reinitialize() {
        init();
        requestLayout();
        startAnimation();
    }

    private void drawCirclesLeftToRight(Canvas canvas, float radius) {
        float step = 0;
        for (int i = 0; i < dotAmount; i++) {
            drawCircles(canvas, i, step, radius);
            step += dotRadius * 3;
        }
    }

    private void drawCirclesRightToLeft(Canvas canvas, float radius) {
        float step = 0;
        for (int i = dotAmount - 1; i >= 0; i--) {
            drawCircles(canvas, i, step, radius);
            step += dotRadius * 3;
        }
    }

    private void drawCircles(@NonNull Canvas canvas, int i, float step, float radius) {
        if (dotPosition == i) {
            drawCircleUp(canvas, step, radius);
        } else {
            if ((i == (dotAmount - 1) && dotPosition == 0 && !isFirstLaunch) || ((dotPosition - 1) == i)) {
                drawCircleDown(canvas, step, radius);
            } else {
                drawCircle(canvas, step);
            }
        }
    }

    /**
     * Circle radius is grow
     * */
    private void drawCircleUp(@NonNull Canvas canvas, float step, float radius) {
        canvas.drawCircle(
                xCoordinate + step,
                getMeasuredHeight() / 2,
                dotRadius + radius,
                startPaint
        );
    }

    private void drawCircle(@NonNull Canvas canvas, float step) {
        canvas.drawCircle(
                xCoordinate + step,
                getMeasuredHeight() / 2,
                dotRadius,
                primaryPaint
        );
    }

    /**
     * Circle radius is decrease
     * */
    private void drawCircleDown(@NonNull Canvas canvas, float step, float radius) {
        canvas.drawCircle(
                xCoordinate + step,
                getMeasuredHeight() / 2,
                bounceDotRadius - radius,
                endPaint
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (animationDirection < 0) {
            drawCirclesRightToLeft(canvas, animatedRadius);
        } else {
            drawCirclesLeftToRight(canvas, animatedRadius);
        }
    }

    private void stopAnimation() {
        this.clearAnimation();
        postInvalidate();
    }

    private void startAnimation() {
        final BounceAnimation bounceAnimation = new BounceAnimation();
        bounceAnimation.setDuration(animationTime);
        bounceAnimation.setRepeatCount(Animation.INFINITE);
        bounceAnimation.setInterpolator(new LinearInterpolator());
        bounceAnimation.setAnimationListener(new Animation.AnimationListener() {


            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                dotPosition++;
                if (dotPosition == dotAmount) {
                    dotPosition = 0;
                }

                startValueAnimator.start();

                if (!isFirstLaunch) {
                    endValueAnimator.start();
                }

                isFirstLaunch = false;
            }
        });
        startAnimation(bounceAnimation);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);

        if (visibility == GONE || visibility == INVISIBLE) {
            stopAnimation();
        } else {
            startAnimation();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimation();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    public void showProgressBar() {
        progressBarCount++;
        if (progressBarCount == 1) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    CustomProgressBar.this.show();
                }
            });
        }
    }

    private void show() {
        if (isShowing)
            return;
        isShowing = true;
        showAnimation();
    }

    private void hide() {
        clearIndicatorsAnimations();
        isShowing = false;
    }

    private void showAnimation() {
        for (int i = 0; i < customProgressIndicators.size(); i++) {
            customProgressIndicators.get(i).startAnim(animationDuration, (5 - i) * interval);
        }
    }

    private void clearIndicatorsAnimations() {
        for (CustomProgressIndicator customProgressIndicator : customProgressIndicators) {
            customProgressIndicator.removeAnim();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({RIGHT_DIRECTION, LEFT_DIRECTION})
    public @interface AnimationDirection {}

    private class BounceAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            animatedRadius = (bounceDotRadius - dotRadius) * interpolatedTime;
            invalidate();
        }
    }
}
