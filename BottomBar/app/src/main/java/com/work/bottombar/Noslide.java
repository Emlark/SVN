package com.work.bottombar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2018/4/17.
 */

public class Noslide extends ViewPager {
    private boolean isCanScroll = true;

    public Noslide(Context context){
        super(context);
    }

    public Noslide(Context context, AttributeSet attr){
        super(context,attr);
    }


    public void setCanScroll(boolean isCanScroll){
        this.isCanScroll = isCanScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
