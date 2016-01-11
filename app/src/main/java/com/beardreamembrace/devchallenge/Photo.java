package com.beardreamembrace.devchallenge;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin User on 9/28/2015.
 */
public class Photo implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static final String LOG_TAG = Photo.class.getSimpleName();

    private String mTitle;

    public static long getSerialVersionUID ()
    {
        return serialVersionUID;
    }


    private double mVerticalScaling;
    private double mHorizontalScaling;
    private int mVerticalPhotoHeight;
    private int mHorizontalPhotoHeight;
    private int mVerticalPhotoWidth;
    private int mHorizontalPhotoWidth;
    private String mHorizontalFit;
    private String mVerticalFit;

    private String mAuthor;

    public double getVerticalScaling ()
    {
        return mVerticalScaling;
    }

    public double getHorizontalScaling ()
    {
        return mHorizontalScaling;
    }

    public String getHorizontalFit ()
    {
        return mHorizontalFit;
    }

    public String getVerticalFit ()
    {
        return mVerticalFit;
    }

    private String mAuthorId;


    private String mId;
    private String mImage;
    private String mCropSize;
    private String mWidth;
    private String mHeight;
    private HashMap<String, String> mImages;

    public HashMap<String, String> getImages ()
    {
        return mImages;
    }

    public Photo (String title, String author, String authorId, String id, String image, String cropSize, String width, String height, HashMap<String, String> images)
    {
        mTitle = title;
        mAuthor = author;
        mAuthorId = authorId;
        mId = id;


        mWidth = width;
        mHeight = height;
        mImages = images;
        /*Log.v(LOG_TAG, String.valueOf(mImages == null));
        if (mImages != null)
        {
            for (Map.Entry<String, String> entry : mImages.entrySet())
                Log.v(LOG_TAG, "Key " + entry.getKey() + " Value " + entry.getValue());
        }
        else
            Log.v(LOG_TAG, String.valueOf(mImages == null));*/
        if (cropSize != null && cropSize !="")
        {
            mCropSize = cropSize;
            mImage = image;
            getBestFit();
        }

    }

    public void setSize (String cropSize)
    {
        if (!cropSize.equals(mCropSize))
        {
            this.mCropSize = cropSize;
            mImage = mImages.get(cropSize);
            getBestFit();
        }
    }


    public int getFormattedPhotoWidth (String cropSize)
    {
        switch (cropSize)
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
            return Integer.parseInt(cropSize);
        case "1080":
        case "1600":
        case "2048":
            return Integer.parseInt(mWidth) >= Integer.parseInt(mHeight) ? Integer
                    .parseInt(cropSize) : Integer.parseInt(cropSize) * Integer
                    .parseInt(mWidth) / Integer.parseInt(mHeight);

        case "4":
            return Integer.parseInt(mWidth) >= Integer.parseInt(mHeight) ? 900 : 900 * Integer
                    .parseInt(mWidth) / Integer.parseInt(mHeight);
        case "5":
            return Integer.parseInt(mWidth) >= Integer.parseInt(mHeight) ? 1170 : 1170 * Integer
                    .parseInt(mWidth) / Integer.parseInt(mHeight);
        case "20":
            return 300 * Integer.parseInt(mWidth) / Integer.parseInt(mHeight);
        case "21":
            return 600 * Integer.parseInt(mWidth) / Integer.parseInt(mHeight);
        default: // case "30"
            return Integer.parseInt(mWidth) >= Integer.parseInt(mHeight) ? 256 : 256 * Integer
                    .parseInt(mWidth) / Integer.parseInt(mHeight);

        }
    }

    public int getFormattedPhotoHeight (String cropSize)
    {
        switch (cropSize)
        {
        case "1":

        case "2":

        case "3":

        case "100":
        case "200":
        case "440":
        case "600":
            return getFormattedPhotoWidth(cropSize); // photo widhth and photo height are the same for cropped sizes
        case "1080":
        case "1600":
        case "2048":
            return Integer.parseInt(mWidth) > Integer.parseInt(mHeight) ? Integer
                    .parseInt(cropSize) * Integer.parseInt(mHeight) / Integer
                    .parseInt(mWidth) : Integer.parseInt(cropSize);

        case "4":
            return Integer.parseInt(mHeight) >= Integer.parseInt(mWidth) ? 900 : 900 * Integer
                    .parseInt(mHeight) / Integer.parseInt(mWidth);
        case "5":
            return Integer.parseInt(mHeight) >= Integer.parseInt(mWidth) ? 1170 : 1170 * Integer
                    .parseInt(mHeight) / Integer.parseInt(mWidth);
        case "20":
            return 300;
        case "21":
            return 600;
        default: // case "30"
            return Integer.parseInt(mHeight) >= Integer.parseInt(mWidth) ? 256 : 256 * Integer
                    .parseInt(mHeight) / Integer.parseInt(mWidth);

        }
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
                ", mImages ='" + mImages + '\'' +
                '}';
    }

    public static boolean isCroppedSize (String cropSize)
    {
        switch (cropSize)
        {
        case "1":
        case "2":
        case "3":
        case "100":
        case "200":
        case "440":
        case "600":
            return true;

        default:
            return false;
        }
    }

    public static int getPhotoWidth (String cropSize)
    {
        switch (cropSize)
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
            return Integer.parseInt(cropSize);
        case "4":
            return 900;
        case "5":
            return 1170;
        case "20":
            return 300 ;
        case "21":
            return 600 ;
        default: // case "30"
            return 256;


        }
    }

    public int getVerticalPhotoHeight ()
    {
        return mVerticalPhotoHeight;
    }

    public int getHorizontalPhotoWidth ()
    {
        return mHorizontalPhotoWidth;
    }

    public int getVerticalPhotoWidth ()
    {
        return mVerticalPhotoWidth;
    }

    public int getHorizontalPhotoHeight ()
    {
        return mHorizontalPhotoHeight;
    }

    public void getBestFit ()
    {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        int count = 0;
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        mVerticalScaling = screenWidth > screenHeight ? (double)screenWidth : (double)screenHeight;
        mHorizontalScaling = mVerticalScaling;
        Log.v(LOG_TAG, "VerticalScaling " + mVerticalScaling + " screen height " + screenHeight );
        Log.v(LOG_TAG, "HorizontalScaling " + mHorizontalScaling + " screen widht " + screenWidth);


        while (count < 2)
        {
            if (screenWidth > screenHeight)


            {
                for (Map.Entry<String, String> entry : mImages.entrySet())
                {
                    String cropSize = entry.getKey();
                    if (!isCroppedSize(cropSize))
                    {
                        int formattedWidth = getFormattedPhotoWidth(cropSize);
                        int formattedHeight = getFormattedPhotoHeight(cropSize);
                        // find if more scaling needs to be done for horizontal or vertical axis and save the best fit (smallest factor larger than 1)
                        if ((double) formattedWidth / screenWidth > (double) formattedHeight / screenHeight)
                        {
                            if (mHorizontalScaling > (double) formattedWidth / screenWidth && (double) formattedWidth / screenWidth >= 1)
                            {
                                mHorizontalScaling = (double)formattedWidth / screenWidth;
                                mHorizontalPhotoWidth = screenWidth;
                                mHorizontalPhotoHeight = (int) (((double)formattedHeight)/mHorizontalScaling);
                                mHorizontalFit = entry.getValue();

                                Log.v(LOG_TAG, "HorizontalScaling " + mHorizontalScaling );

                            }

                        }
                        else
                        {
                            if (mHorizontalScaling > (double) formattedHeight / screenHeight && (double) formattedHeight / screenHeight >= 1)
                            {
                                mHorizontalScaling = (double)formattedHeight / screenHeight;
                                mHorizontalPhotoHeight = screenHeight;
                                mHorizontalPhotoWidth = (int) (((double)formattedWidth)/mHorizontalScaling);
                                mHorizontalFit = entry.getValue();

                                Log.v(LOG_TAG, "HorizontalScaling " + mHorizontalScaling );
                            }
                        }
                    }
                }
            }
            else
            {
                for (Map.Entry<String, String> entry : mImages.entrySet())
                {
                    String cropSize = entry.getKey();
                    if (!isCroppedSize(cropSize))
                    {
                        int formattedWidth = getFormattedPhotoWidth(cropSize);
                        int formattedHeight = getFormattedPhotoHeight(cropSize);
                        // find if more scaling needs to be done for horizontal or vertical axis and save the best fit (smallest factor larger than 1)
                        if ((double) formattedWidth / screenWidth > (double) formattedHeight / screenHeight)
                        {
                            if (mVerticalScaling > (double) formattedWidth / screenWidth && (double) formattedWidth / screenWidth >= 1)
                            {
                                mVerticalScaling = (double)formattedWidth / screenWidth;
                                mVerticalFit = entry.getValue();
                                mVerticalPhotoWidth = screenWidth;
                                mVerticalPhotoHeight = (int) (((double)formattedHeight)/mVerticalScaling);

                                Log.v(LOG_TAG, "VerticalScaling " + mVerticalScaling );

                            }

                        }
                        else
                        {
                            if (mVerticalScaling > (double) formattedHeight / screenHeight && (double) formattedHeight / screenHeight >= 1)
                            {
                                mVerticalScaling = (double)formattedHeight / screenHeight;
                                mVerticalFit = entry.getValue();
                                mVerticalPhotoHeight = screenHeight;
                                mVerticalPhotoWidth = (int) (((double)formattedWidth)/mVerticalScaling);

                                Log.v(LOG_TAG, "VerticalScaling " + mVerticalScaling );
                            }
                        }
                    }
                }

            }
            count ++;
            int temp = screenWidth;
            screenWidth = screenHeight;
            screenHeight = temp;

        }
       // int mVerticalPhotoHeight = mVerticalScaling
    }

    public int getPhotoWidth ()
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

    public int getPhotoHeight ()
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
