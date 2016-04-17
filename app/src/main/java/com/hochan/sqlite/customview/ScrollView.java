package com.hochan.sqlite.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2016/4/14.
 */
public class ScrollView extends RelativeLayout{

    private float mInitX = 0;
    private float mInitY = 0;
    private float mLastX = 0;
    private float mLastY = 0;

    public ScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                System.out.println("MotionEvent.ACTION_DOWN");
                mInitX = ev.getX();
                mInitY = ev.getY();
                mLastX = mInitX;
                mLastY = mInitY;
                break;
            case MotionEvent.ACTION_MOVE:
                //System.out.println("MotionEvent.ACTION_MOVE");
                if(Math.abs(mInitX - ev.getX()) > Math.abs(mInitY - ev.getY())){
                    return true;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mInitX = event.getX();
                mInitY = event.getY();
                mLastY = mInitY;
                mLastX = mInitX;
                break;
            case MotionEvent.ACTION_MOVE:
                if(mInitY == 0 && mInitX == 0){
                    mInitX = event.getX();
                    mInitY = event.getY();
                }
                if(mLastX == 0 && mLastY == 0){
                    mLastX = mInitX;
                    mLastY = mInitY;
                }
                scrollBy((int) (mLastX - event.getX()), 0);
                mLastX = event.getX();
                mLastY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if(getScrollX() < 0){
                    scrollTo(0, 0);
                }
                break;
        }
        return super.onTouchEvent(event);
    }


}
