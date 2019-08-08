package com.mariapps.qdmswiki.custom;

/**
 Created by aruna.ramakrishnan on 08-08-2019
 */
public class CustomProgressInterpolator implements android.view.animation.Interpolator {
    @Override
    public float getInterpolation(float v) {
        if (v > 0.3f && v < 0.70f)
            return (float) ((-(v - 0.5) / 6) + 0.5f);
        return (float) ((-4) * Math.pow(v - 0.5, 3) + 0.5);
    }
}
