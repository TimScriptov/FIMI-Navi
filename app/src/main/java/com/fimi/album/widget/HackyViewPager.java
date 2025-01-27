package com.fimi.album.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;


public class HackyViewPager extends ViewPager {
    private static final String TAG = "HackyViewPager";
    private boolean scrollble;

    public HackyViewPager(Context context) {
        super(context);
        this.scrollble = true;
    }

    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.scrollble = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            if (this.scrollble) {
                return super.onInterceptTouchEvent(ev);
            }
            return false;
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.e(TAG, "hacky viewpager error2");
            return false;
        } catch (IllegalArgumentException e2) {
            Log.e(TAG, "hacky viewpager error1");
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.scrollble) {
            return super.onTouchEvent(ev);
        }
        return false;
    }

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }
}
