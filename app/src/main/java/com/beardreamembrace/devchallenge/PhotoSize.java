package com.beardreamembrace.devchallenge;

/**
 * Created by Admin User on 11/3/2015.
 */
public class PhotoSize
{
    public static final int MINIMUN_HEIGHT = 150;
    private int height;
    private int width;


    public int getHeight ()
    {
        return height;
    }

    public int getWidth ()
    {
        return width;
    }



    public PhotoSize (int height, int width)
    {

        this.height = height;
        this.width = width;

    }
}
