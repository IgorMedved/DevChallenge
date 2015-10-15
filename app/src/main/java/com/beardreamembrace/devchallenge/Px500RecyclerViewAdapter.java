package com.beardreamembrace.devchallenge;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin User on 9/29/2015.
 */
public class Px500RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<Photo> mPhotoList;
    private Context mContext;

    public Px500RecyclerViewAdapter (Context mContext, List<Photo> mPhotoList)
    {
        this.mPhotoList = mPhotoList;
        this.mContext = mContext;
    }

    class Px500GridCellImageViewHolder extends RecyclerView.ViewHolder
    {
        protected ImageView thumbnail;


        public Px500GridCellImageViewHolder (View view)
        {
            super(view);
            this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);




        }


    }

    @Override
    public int getItemCount ()
    {
        return (null!= mPhotoList ? mPhotoList.size():0);
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder viewHolder, int i)
    {
        Px500GridCellImageViewHolder px500GridCellImageViewHolder = (Px500GridCellImageViewHolder)viewHolder;
        Photo photoItem = mPhotoList.get(i);
        Picasso.with(mContext).load(photoItem.getImage())
               .error(R.drawable.placeholder)
               .placeholder(R.drawable.placeholder)
               .into(px500GridCellImageViewHolder.thumbnail);




    }

    @Override
    public Px500GridCellImageViewHolder onCreateViewHolder (ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.thumbnail_grid_cell_layout, null);
        Px500GridCellImageViewHolder px500GridCellImageViewHolder = new Px500GridCellImageViewHolder(view);

        return px500GridCellImageViewHolder;

    }

    public void loadNewData (List<Photo> newPhotos)
    {
        mPhotoList = newPhotos;
        notifyDataSetChanged();
    }

    // Add at the top of the list.

    public void addPhotosFirst(ArrayList<Photo> list) {
        mPhotoList.addAll(0, list);
        notifyDataSetChanged();
    }

    // Add at the end of the list.

    public void addPhotosLast(ArrayList<Photo> list) {
        mPhotoList.addAll(mPhotoList.size(), list);
        notifyDataSetChanged();
    }

    public Photo getPhoto (int position)
    {
        return (null!=mPhotoList? mPhotoList.get(position): null);
    }

    public List<Photo> getPhotos()
    {
        return mPhotoList;
    }
}

