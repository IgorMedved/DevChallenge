package com.beardreamembrace.devchallenge;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by Admin User on 11/5/2015.
 */
public class BaseRecyclerView extends RecyclerView implements Px500RecyclerViewAdapter.OnImageLoadFailListener
{
    private String LOG_TAG = BaseRecyclerView.class.getSimpleName();

    public BaseRecyclerView (Context context)
    {
        super (context);
    }

    public BaseRecyclerView (Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void imageFailed (PicassoImageLoadFailEvent event)
    {

        Log.v(LOG_TAG, event.toString());

    }
}
