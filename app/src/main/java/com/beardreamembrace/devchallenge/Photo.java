package com.beardreamembrace.devchallenge;

import java.io.Serializable;

/**
 * Created by Admin User on 9/28/2015.
 */
public class Photo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String mTitle;

    public static long getSerialVersionUID ()
    {
        return serialVersionUID;
    }

    private String mAuthor;
    private String mAuthorId;



    private String mId;
    private String mImage;
    private String mCropSize;
    private String mWidth;
    private String mHeight;

    public Photo (String title, String author, String authorId, String id, String image, String cropSize, String width, String height)
    {
        mTitle = title;
        mAuthor = author;
        mAuthorId = authorId;
        mId = id;
        mImage = image;
        mCropSize = cropSize;
        mWidth = width;
        mHeight = height;
    }

    @Override
    public String toString ()
    {
        return "Photo{" +
                "mTitle='" + mTitle + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mAuthorId='" + mAuthorId + '\'' +
                ", mId='" + mId + '\'' +
                ", mImage='" + mImage + '\'' +
                ", mCropSize='" + mCropSize + '\'' +
                ", mWidth='" + mWidth + '\'' +
                ", mHeight='" + mHeight + '\'' +
                '}';
    }

    public int getPhotoWidth()
    {
        switch (mCropSize)
        {
        case "1":
            return 70;
        case "2":
            return 140;
        case "3":
            return 280;
        case "100":
        case "200":
        case "440":
        case "600":
        case "1080":
        case "1600":
        case "2048":
            return Integer.parseInt(mCropSize);
        case "4":
            return 900;
        case "5":
            return 1170;
        case "20":
            return 300 * Integer.parseInt(mWidth) / Integer.parseInt(mHeight);
        case "21":
            return 600 * Integer.parseInt(mWidth) / Integer.parseInt(mHeight);
        default: // case "30"
            return 256;
        }


    }

    public int getPhotoHeight()
    {
        switch (mCropSize)
        {
        case "20":
            return 300;
        case "21":
            return 500;
        default:
            return getPhotoWidth();


        }
    }

    public String getTitle ()
    {
        return mTitle;
    }

    public String getAuthor ()
    {
        return mAuthor;
    }

    public String getAuthorId ()
    {
        return mAuthorId;
    }

    public String getId ()
    {
        return mId;
    }

    public String getImage ()
    {
        return mImage;
    }

    public String getCropSize ()
    {
        return mCropSize;
    }

    public String getWidth ()
    {
        return mWidth;
    }

    public String getHeight ()
    {
        return mHeight;
    }
}
