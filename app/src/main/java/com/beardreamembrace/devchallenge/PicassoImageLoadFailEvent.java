package com.beardreamembrace.devchallenge;

import android.net.Uri;

import com.squareup.picasso.Picasso;

/**
 * Created by Admin User on 11/5/2015.
 */
public class PicassoImageLoadFailEvent
{
    private Picasso mPicasso;
    private Exception mException;
    private Uri mUri;

    public Picasso getPicasso ()
    {
        return mPicasso;
    }

    public Exception getException ()
    {
        return mException;
    }

    public Uri getUri ()
    {
        return mUri;
    }

    public PicassoImageLoadFailEvent (Picasso picasso, Exception exception, Uri uri)
    {

        mPicasso = picasso;
        mException = exception;
        mUri = uri;
    }

    @Override
    public String toString ()
    {
        return "PicassoImageLoadFailEvent{" +
                "mPicasso=" + mPicasso +
                ", mException=" + mException +
                ", mUri=" + mUri +
                '}';
    }
}
