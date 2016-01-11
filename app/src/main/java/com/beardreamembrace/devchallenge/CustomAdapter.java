package com.beardreamembrace.devchallenge;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Admin User on 10/21/2015.
 */
public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Picasso.Listener
{
    public final static String LOG_TAG = CustomAdapter.class.getSimpleName();


    protected ArrayList<Photo> mPhotoList;
    private CustomAdapter.OnImageLoadFailListener myListener;

    public void setMyListener (OnImageLoadFailListener myListener)
    {
        this.myListener = myListener;
    }

    public CustomAdapter()
    {
        mPhotoList = null;
    }




    public CustomAdapter (Context mContext, ArrayList<Photo> mPhotoList)
    {
        this.mPhotoList = mPhotoList;

    }
    public void loadNewData (ArrayList<Photo> newPhotos)
    {
        notifyDataSetChanged();
        mPhotoList = newPhotos;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType)
    {
        return null;
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position)
    {

    }

    @Override
    public int getItemCount ()
    {
        return 0;
    }

    @Override
    public void onImageLoadFailed (Picasso picasso, Uri uri, Exception exception)
    {


    }



    protected int findViewAdapterPositionByUri (Uri uri)
    {
        int position;

        for (position = 0; position < mPhotoList.size(); position++)
        {
            if (mPhotoList.get(position).getImage().equals(uri.toString()))
            {
                return position;
            }

        }
        return -1;


    }

    public interface OnImageLoadFailListener
    {
        public void imageFailed (PicassoImageLoadFailEvent e);

    }
}
