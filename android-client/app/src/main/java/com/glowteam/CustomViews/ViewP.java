package com.glowteam.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class ViewP extends ViewPager {

    private float downX;
    private float downY;
    private boolean isTouchCaptured;
    private float upX1;
    private float upY1;
    private float upX2;
    private float upY2;

    private float x1, x2;
    static final int min_distance = 20;

    boolean eventSent = false;
    public boolean isSwipeRight = false;

    public ViewP(@NonNull Context context) {
        super(context);
    }

    public ViewP(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                downX = event.getX();
                downY = event.getY();

                if (!isTouchCaptured) {
                    upX1 = event.getX();
                    upY1 = event.getY();
                    isTouchCaptured = true;
                } else {
                    upX2 = event.getX();
                    upY2 = event.getY();
                    float deltaX = upX1 - upX2;
                    float deltaY = upY1 - upY2;
                    //HORIZONTAL SCROLL
                    if (Math.abs(deltaX) > Math.abs(deltaY)) {
                        if (Math.abs(deltaX) > min_distance) {
                            // left or right
                            if (deltaX < 0) {
                                if (!eventSent) {
                                    isSwipeRight = false;
//                                    eventSent = true;
                                }
                            }
                            if (deltaX > 0) {
                                if (!eventSent) {
                                    isSwipeRight = true;
//                                    eventSent = true;
//                                    return false;

                                }
                            }
                        } else {
                            //not long enough swipe...
                        }
                    }
                    //VERTICAL SCROLL
                    else {
                        if (Math.abs(deltaY) > min_distance) {
                            // top or down
                            if (deltaY < 0) {

                            }
                            if (deltaY > 0) {

                            }
                        } else {
                            //not long enough swipe...
                        }
                    }
                }
            }
            break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                isTouchCaptured = false;
                eventSent = false;
            }

        }
        return super.onTouchEvent(event);
    }
}
