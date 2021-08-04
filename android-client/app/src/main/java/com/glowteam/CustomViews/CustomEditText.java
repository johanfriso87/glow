package com.glowteam.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.glowteam.R;


public class CustomEditText extends androidx.appcompat.widget.AppCompatEditText {

    String fontStyle;

    public CustomEditText(Context context) {
        super(context);
        applyCustomFont(context, "");
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CustomEditText_customFont:
                    fontStyle = a.getString(attr);
                    applyCustomFont(context, fontStyle);
                    break;

            }
        }
        a.recycle();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context, "");
    }


    private void applyCustomFont(Context context, String fontStyle) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/" +  fontStyle);
        setTypeface(customFont);
    }

}
