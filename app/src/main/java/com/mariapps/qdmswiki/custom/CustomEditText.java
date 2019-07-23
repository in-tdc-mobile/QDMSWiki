package com.mariapps.qdmswiki.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.mariapps.qdmswiki.R;

public class CustomEditText extends AppCompatEditText {

    String defaultFont = "Lato-Regular.ttf";
    public CustomEditText(Context context) {
        super(context);
        initAttributes(null);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(attrs);
    }

    public void initAttributes(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);
            String fontName = a.getString(R.styleable.CustomTextView_fontface);
            try {
                if (fontName != null) {
                    Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
                    setTypeface(myTypeface);
                } else{
                    Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + defaultFont);
                    setTypeface(myTypeface);
                }

            } catch (Exception e) {
                e.printStackTrace();

            }
            a.recycle();

        }

    }

}



