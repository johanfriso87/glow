package com.glowteam.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.glowteam.R;


public class CustomTextView extends androidx.appcompat.widget.AppCompatTextView {

    String fontStyle;


    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.CustomTextView_customFont) {
                fontStyle = a.getString(attr);
                applyCustomFont(context, fontStyle);
            }
        }
        a.recycle();

    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void applyCustomFont(Context context, String fontStyle) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontStyle);
        setTypeface(customFont);
    }
}
