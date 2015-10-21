package com.example.administrator.myring;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.RelativeLayout;
import android.widget.Scroller;

public class MyFlag extends RelativeLayout {
    private  Scroller mScroller;
    private  ScrollerCompat scrollCompat;

    public MyFlag(Context context) {
        super(context);
    }

    public MyFlag(Context context, AttributeSet attrs) {
        super(context, attrs);
        scrollCompat = ScrollerCompat.create(context);
        mScroller = new Scroller(context);
    }

    public MyFlag(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int getPx(int idp) {
        DisplayMetrics ccc = new DisplayMetrics();
        ((Activity) getContext())
                .getWindowManager().getDefaultDisplay().getMetrics(ccc);
        return (int) (ccc.density * (float)idp + 0.5f);
    }

    private ViewGroup child1;
    private View tv2;

    int leftLimitScrollY = 0;
    int rightLimitscrollY = 0;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        child1 = (ViewGroup) findViewById(R.id.child1);
        tv2 = findViewById(R.id.tv2);



//
//        int w = MeasureSpec.getSize(widthMeasureSpec);
//        int child1W = w - getPx(80);
//
//
//        child2.measure(widthMeasureSpec, heightMeasureSpec);
//        child1.measure(MeasureSpec.makeMeasureSpec(child1W, MeasureSpec.EYACTLY), heightMeasureSpec);
//
//        leftLimitScrollY = -child1.getMeasuredWidth();
//        rightLimitscrollY = 0;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed,l,t,r,b);

        child1.layout(l, t - getPx(100), r, b - getPx(100));
    }

    private int getDisdance(){
        return  getPx(100);
    }
    int iRecordY = 0;




    private boolean isTouchPointInView(View view, float x, float y) {
        int[] locations = new int[2];

        view.getLocationInWindow(locations);

        int left = locations[0];
        int top = locations[1];
        int right = locations[0] + view.getMeasuredWidth();
        int bottom = locations[1] + view.getMeasuredHeight();
        if (y >= top && y <= bottom && x >= left
                && x <= right) {
            return true;
        }
        return false;
    }
    private boolean bOnView =false;

    private boolean bOpen = false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                bOnView = isTouchPointInView(tv2, event.getRawX(),event.getRawY());

                iRecordY = (int) event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                int moveY = (int) event.getY();
                int deltaY = iRecordY - moveY;

                if(bOnView){
                    child1.scrollBy(0, deltaY);
                }
                iRecordY = moveY;

                if(bOnView){
                    int currentScrollY = child1.getScrollY();
                    if(currentScrollY <= -getPx(100)){
                        child1.scrollTo(0, -getPx(100));
                    }else if(currentScrollY >= 0){
                        child1.scrollTo(0,0);
                    }
                }

                break;

            case MotionEvent.ACTION_UP:
                int currentScrollY = child1.getScrollY();
                if(!bOpen && currentScrollY <= -getPx(100)/10){
                    createValueAnimator(currentScrollY,-getPx(100));
                    bOpen = true;
                }else if(!bOpen && currentScrollY > -getPx(100)/10){
                    createValueAnimator(currentScrollY, 0);
                    bOpen = false;
                }else if(bOpen && currentScrollY > -getPx(100)*9/10){
                    createValueAnimator(currentScrollY, 0);
                    bOpen = false;
                }else if(bOpen && currentScrollY <= -getPx(100)/10){
                    createValueAnimator(currentScrollY,-getPx(100));
                    bOpen = true;
                }
                break;
        }
        child1.invalidate();
        invalidate();
        return true;
    }

    private void createValueAnimator(int from, int to){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(from,to);
        valueAnimator.setInterpolator(new BounceInterpolator());
        valueAnimator.setDuration(200);
        valueAnimator.setRepeatCount(0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                child1.scrollTo(0, (Integer) animation.getAnimatedValue());
                child1.invalidate();
                invalidate();
            }
        });
        valueAnimator.start();
    }

//    @Override
//    public void computeScroll() {
//        super.computeScroll();
//        if(mScroller.computeScrollOffset()){
//            child1.scrollTo(0,mScroller.getCurrY());
//            child1.invalidate();
//        }
//    }
}
