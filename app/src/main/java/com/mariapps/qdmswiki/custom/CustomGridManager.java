package com.mariapps.qdmswiki.custom;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

public class CustomGridManager extends GridLayoutManager {
    private boolean isScrollEnabled = true;
    private int mSpanCount;
    boolean mPendingSpanCountChange = false;
    GridLayoutManager.SpanSizeLookup mSpanSizeLookup = new GridLayoutManager.DefaultSpanSizeLookup();

    public CustomGridManager(Context context, int spanCount) {
        super(context,spanCount);
        this.mSpanCount=spanCount;
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }


    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }


    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}
