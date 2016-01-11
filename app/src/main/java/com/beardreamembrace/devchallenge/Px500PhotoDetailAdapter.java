package com.beardreamembrace.devchallenge;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Admin User on 10/17/2015.
 */
public class Px500PhotoDetailAdapter extends CustomAdapter
{
    final static String LOG_TAG = Px500PhotoDetailAdapter.class.getSimpleName();

    private ArrayList <Photo> mPhotoList;
    private Context mContext;

    public Px500PhotoDetailAdapter (Context context, ArrayList<Photo> photoList)
    {
        mPhotoList = photoList;
        mContext = context;


    }

    public Px500PhotoDetailAdapter (Context context, CustomAdapter mAdapter)
    {
        mPhotoList = (ArrayList<Photo>)mAdapter.mPhotoList;
        mContext = context;
    }

    class PhotoDetailViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView photoDetail;


        public PhotoDetailViewHolder (View v)
        {
            super(v);

            photoDetail = (ImageView)v.findViewById(R.id.imgDetail);
            DisplayMetrics metrics = new DisplayMetrics();


        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_detail_layout, null);
        PhotoDetailViewHolder photoDetailViewHolder = new PhotoDetailViewHolder(view);

        return photoDetailViewHolder;
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position)
    {
        Photo photoItem = mPhotoList.get(position);
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        boolean horizontalOrientation = metrics.widthPixels>metrics.heightPixels;

        int width = horizontalOrientation? photoItem.getHorizontalPhotoWidth(): photoItem.getVerticalPhotoWidth();
        int height = horizontalOrientation? photoItem.getHorizontalPhotoHeight(): photoItem.getVerticalPhotoHeight();

        String link = horizontalOrientation? photoItem.getHorizontalFit(): photoItem.getVerticalFit();
        double scaling = horizontalOrientation? photoItem.getHorizontalScaling(): photoItem.getVerticalScaling();

        Log.v(LOG_TAG, link );



        Picasso.with(mContext).load(link)
                .resize(width, height)
               .error(R.drawable.placeholder)
                .resize (width, height)
               .placeholder(R.drawable.placeholder)
                .resize (width, height)
               .into(((PhotoDetailViewHolder)holder).photoDetail);

    }

    @Override
    public int getItemCount ()
    {
        return mPhotoList == null? 0: mPhotoList.size();
    }
}
