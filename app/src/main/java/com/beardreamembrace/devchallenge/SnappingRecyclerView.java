package com.beardreamembrace.devchallenge;

/**
 * Created by Admin User on 10/21/2015.
 */

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_FLING;
import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;

public class SnappingRecyclerView extends RecyclerView {

    private final static String LOG_TAG = SnappingRecyclerView.class.getSimpleName();
    private boolean mSnapEnabled = false;
    private boolean mUserScrolling = false;
    private boolean mScrolling = false;


    private int mScrollState;
    private long lastScrollTime = 0;
    private Handler mHandler = new Handler();

    private boolean mScaleUnfocusedViews = false;

    private final static int MINIMUM_SCROLL_EVENT_OFFSET_MS = 20;

    public SnappingRecyclerView(Context context) {
        super(context);
    }

    public SnappingRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Enable snapping behaviour for this recyclerView
     * @param enabled enable or disable the snapping behaviour
     */
    public void setSnapEnabled(boolean enabled) {
        mSnapEnabled = enabled;

        if (enabled) {
            addOnLayoutChangeListener(new OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (left == oldLeft && right == oldRight && top == oldTop && bottom == oldBottom) {
                        removeOnLayoutChangeListener(this);
                        updateViews();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                scrollToView(getChildAt(0));
                            }
                        }, 20);
                    }
                }
            });

            addOnScrollListener(new OnScrollListener()
            {
                @Override
                public void onScrolled (RecyclerView recyclerView, int dx, int dy)
                {

                    updateViews();
                    super.onScrolled(recyclerView, dx, dy);
                }

                @Override
                public void onScrollStateChanged (RecyclerView recyclerView, int newState)
                {
                    super.onScrollStateChanged(recyclerView, newState);

                    /** if scroll is caused by a touch (scroll touch, not any touch) **/
                    if (newState == SCROLL_STATE_TOUCH_SCROLL)
                    {
                        /** if scroll was initiated already, this is not a user scrolling, but probably a tap, else set userScrolling **/

                        if (!mScrolling)
                        {
                            mUserScrolling = true;


                            Log.v(LOG_TAG, "recycler state" + newState);
                        }
                    }
                    else if (newState == SCROLL_STATE_IDLE)
                    {
                        if (mUserScrolling)
                        {
                            scrollToView(getCenterView());
                        }

                        mUserScrolling = false;
                        mScrolling = false;

                    }
                    else if (newState == SCROLL_STATE_FLING)
                    {
                        mScrolling = true;

                    }

                    mScrollState = newState;
                }
            });
        } else {
            addOnScrollListener(null);
        }
    }

    /**
     * Enable snapping behaviour for this recyclerView
     * @param enabled enable or disable the snapping behaviour
     * @param scaleUnfocusedViews downScale the views which are not focused based on how far away they are from the center
     */
    public void setSnapEnabled(boolean enabled, boolean scaleUnfocusedViews) {
        this.mScaleUnfocusedViews = scaleUnfocusedViews;
        setSnapEnabled(enabled);
    }

    private void updateViews() {
        for (int i = 0; i < getChildCount(); i++) {
            Log.v(LOG_TAG, "i " + i);
            View child = getChildAt(i);
            setMarginsForChild(child);

            if (mScaleUnfocusedViews) {
                float percentage = getPercentageFromCenter(child);
                float scale = 1f - (0.7f * percentage);
                Log.v(LOG_TAG, "I should not be here");

                child.setScaleX(scale);
                child.setScaleY(scale);
            }
        }
    }

    /**
     *  Adds the margins to a childView so a view will still center even if it's only a single child
     * @param child childView to set margins for
     */
    private void setMarginsForChild(View child) {
        //int lastItemIndex = getLayoutManager().getItemCount() - 1;
        //int childIndex = getChildAdapterPosition(child);

        //int startMargin = childIndex == 0 ? getMeasuredWidth() / 2 : 0;
        //int endMargin = childIndex == lastItemIndex ? getMeasuredWidth() / 2 : 0;
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;


        int margin = (screenWidth - child.getMeasuredWidth())/2;

        Log.v (LOG_TAG, "screenWidth " +screenWidth + " viewWidth " + child.getMeasuredWidth());

        /** if sdk minimum level is 17, set RTL margins **/
        if (Build.VERSION.SDK_INT >= 17) {
//            ((ViewGroup.MarginLayoutParams) child.getLayoutParams()).setMarginStart(startMargin);
//            ((ViewGroup.MarginLayoutParams) child.getLayoutParams()).setMarginEnd(endMargin);
            ((ViewGroup.MarginLayoutParams) child.getLayoutParams()).setMarginStart(margin);
                    ((ViewGroup.MarginLayoutParams) child.getLayoutParams()).setMarginEnd(margin);
        }

//        ((ViewGroup.MarginLayoutParams) child.getLayoutParams()).setMargins(startMargin, 0, endMargin, 0);
        ((ViewGroup.MarginLayoutParams) child.getLayoutParams()).setMargins(margin, 0, margin, 0);
        child.requestLayout();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!mSnapEnabled)
            return super.dispatchTouchEvent(event);

        long currentTime = System.currentTimeMillis();

        /** if touch events are being spammed, this is due to user scrolling right after a tap,
         * so set userScrolling to true **/
        if (mScrolling && mScrollState == SCROLL_STATE_TOUCH_SCROLL) {
            if ((currentTime - lastScrollTime) < MINIMUM_SCROLL_EVENT_OFFSET_MS) {
                mUserScrolling = true;
            }
        }

        lastScrollTime = currentTime;

        View targetView = getChildClosestToPosition((int)event.getX());

        if (!mUserScrolling) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (targetView != getCenterView()) {

                    scrollToView(targetView);
                    return true;
                }
            }
        }

        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (!mSnapEnabled)
            return super.onInterceptTouchEvent(e);

        View targetView = getChildClosestToPosition((int) e.getX());

        if (targetView != getCenterView()) {
            return true;
        }

        return super.onInterceptTouchEvent(e);
    }

    private View getChildClosestToPosition(int x) {
        if (getChildCount() <= 0)
            return null;



        int closestX = 9999;
        View closestChild = null;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int itemWidth = child.getMeasuredWidth();

            int childCenterX = ((int)child.getX() + (itemWidth / 2));
            int xDistance = childCenterX - x;

            /** if child center is closer than previous closest, set it as closest  **/
            if (Math.abs(xDistance) < Math.abs(closestX)) {
                closestX = xDistance;
                closestChild = child;
            }
        }

        return closestChild;
    }

    private View getCenterView() {
        return getChildClosestToPosition(Resources.getSystem().getDisplayMetrics().widthPixels / 2);
    }

    private void scrollToView(View child) {
        if (child == null)
            return;

        stopScroll();


        int scrollDistance = getScrollDistance(child);

        Log.v (LOG_TAG, "scrollDistance " +scrollDistance);

        if (scrollDistance != 0)
            smoothScrollBy(scrollDistance, 0);
    }

    private int getScrollDistance(View child) {
        int itemWidth = getChildAt(0).getMeasuredWidth();
        int centerX = getMeasuredWidth() / 2;

        int childCenterX = ((int)child.getX() + (itemWidth / 2));

        Log.v (LOG_TAG, "ItemWidth " + itemWidth + " centerX " + centerX + " childCenterX " + childCenterX);

        return childCenterX - centerX;

    }

    private float getPercentageFromCenter(View child) {
        float centerX = (getMeasuredWidth() / 2);
        float childCenterX = child.getX() + (child.getWidth() / 2);

        float offSet = Math.max(centerX, childCenterX) - Math.min(centerX, childCenterX);

        int maxOffset = (getMeasuredWidth() / 2) + child.getWidth();

        return (offSet / maxOffset);
    }

    public boolean isChildCenterView(View child) {
        return child == getCenterView();
    }

    public int getHorizontalScrollOffset() {
        return computeHorizontalScrollOffset();
    }

    public int getVerticalScrollOffset() {
        return computeVerticalScrollOffset();
    }

    public void smoothUserScrollBy(int x, int y) {
        mUserScrolling = true;

        smoothScrollBy(x, y);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
    }
}