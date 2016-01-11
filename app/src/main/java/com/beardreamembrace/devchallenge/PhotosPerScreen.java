package com.beardreamembrace.devchallenge;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by Admin User on 10/17/2015.
 */
class PhotosPerScreen implements Serializable
{
    private int mPhotosPerLongerDimension;
    private int mPhotosPerShorterDimension;

//    private boolean mOrientation;

    private final static String LOG_TAG = PhotosPerScreen.class.getSimpleName();


    // private Point size;
    private String cropSize;

    public PhotosPerScreen (String cropSize)
    {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        this.cropSize = cropSize;

 //       mOrientation = screenWidth < screenHeight;


        // case 1: screenWidth = 0 not initialized
        // do get screenWidth and cropSize and calculate other parameters based on it

        // case 2 screenWidth != this screen width recalculate everything

        // case 3 cropSize changed

        // case 4 screenWidth and cropSize are same as befor - no need to do anything
        int photoWidth;
        int photoHeight;

        if (Photo.isCroppedSize(cropSize))
        {
            photoWidth=Photo.getPhotoWidth(cropSize);
            photoHeight = photoWidth;
        }
        else
        {
            photoWidth = PhotoSize.MINIMUN_HEIGHT + 100;
            photoHeight = photoWidth;
        }

        int photosPerWidth = (screenWidth - (screenWidth / photoWidth + 1) * 10) / photoWidth;
        int photosPerHeight = (screenHeight - (screenHeight / photoHeight + 1) * 10) / photoHeight;

        mPhotosPerLongerDimension = screenWidth > screenHeight ? photosPerWidth: photosPerHeight;

        mPhotosPerShorterDimension =  screenWidth > screenHeight ? photosPerHeight:photosPerWidth;

        Log.v(LOG_TAG, "mPhotosPerLongerDimension " + mPhotosPerLongerDimension + " photos width " + photoWidth + " photos per Height " + mPhotosPerShorterDimension);

        mPhotosPerLongerDimension = mPhotosPerLongerDimension == 0 ? 1 : mPhotosPerLongerDimension;
        mPhotosPerShorterDimension = mPhotosPerShorterDimension == 0 ? 1 : mPhotosPerShorterDimension;


    }

    public int getPhotosPerWidth ()
    {
        return Resources.getSystem().getDisplayMetrics().widthPixels >Resources.getSystem().getDisplayMetrics().heightPixels?
                mPhotosPerLongerDimension : mPhotosPerShorterDimension;
    }

    public int getPhotosOnScreen()
    {
        return mPhotosPerShorterDimension * mPhotosPerLongerDimension;
    }



}
