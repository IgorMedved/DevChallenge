package com.beardreamembrace.devchallenge;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin User on 9/24/2015.
 */




public class Get500PxData extends GetRawData
{
    private String LOG_TAG = Get500PxData.class.getSimpleName();
    private List<Photo> mPhotos;
    protected Uri mDestinationUri;
    //private int currentPageNumber;

    public Get500PxData (ArrayList<String> paramValues)
    {

        super(null);
        createAndUpdateUri(paramValues);
        super.setmRawUrl(mDestinationUri.toString());
        mPhotos = new ArrayList<Photo>();
 //       currentPageNumber = paramValues.get(6).equals("")? 1:Integer.parseInt(paramValues.get(6));
    }

    public void execute ()
    {

        Download500PxData download500PxData = new Download500PxData();
        Log.v(LOG_TAG, "Built URI = " + mDestinationUri.toString());
        download500PxData.execute(mDestinationUri.toString());
    }

/*    protected void addPhotos (ArrayList<String>paramValues)
    {
        super.reset();
        createAndUpdateUri(paramValues);
        super.setmRawUrl(mDestinationUri.toString());
        mPhotos = new ArrayList<>();
        Download500PxData download500PxData = new Download500PxData();
        download500PxData.execute(mDestinationUri.toString());

    }*/
    public boolean createAndUpdateUri (ArrayList<String> paramValues)
    {


        mDestinationUri = new Uri500BuildHelper(paramValues).getmDestinationUri();

        return mDestinationUri != null;
    }

    public List<Photo> getPhotos ()
    {
        return mPhotos;
    }

    public void processResult()
    {
        if (this.getmDownloadStatus()!= DownloadStatus.OK)
        {
            Log.e(LOG_TAG, "Error downloading raw file");
            return;
        }

        final String PX500_PHOTOS = "photos";
        final String PX500_TITLE = "name";
        final String PX500_USER = "user";
        final String PX500_AUTHOR = "fullname";
        final String PX500_USER_ID = "user_id";
        final String PX500_PHOTO_ID = "id";
        final String PX500_IMAGES = "images";
        final String PX500_CROP_SIZE = "size";
        final String PX500_IMAGE = "image_url";
        final String PX500_PHOTO_WIDTH = "width";
        final String PX500_PHOTO_HEIGHT = "height";



        try
        {
            JSONObject jsonData = new JSONObject(getmData());
            JSONArray itemsArray = jsonData.getJSONArray(PX500_PHOTOS);

            for (int i = 0; i <itemsArray.length(); i++)
            {
                JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                String title = jsonPhoto.getString(PX500_TITLE);
                String userId = jsonPhoto.getString(PX500_USER_ID);
                String id = jsonPhoto.getString(PX500_PHOTO_ID);
                String image = jsonPhoto.getString(PX500_IMAGE);
                String width = jsonPhoto.getString(PX500_PHOTO_WIDTH);
                String height = jsonPhoto.getString(PX500_PHOTO_HEIGHT);

                JSONObject jsonUser = jsonPhoto.getJSONObject(PX500_USER);
                String author = jsonUser.getString(PX500_AUTHOR);

                JSONArray imagesArray = jsonPhoto.getJSONArray (PX500_IMAGES);
                JSONObject jsonImage = imagesArray.getJSONObject(0);
                String cropSize = jsonImage.getString(PX500_CROP_SIZE);



                Photo photoObject  = new Photo (title, author, userId, id, image, cropSize, width, height);

                this.mPhotos.add(photoObject);

                //  Log.v(LOG_TAG, "I am here " + i);
                //  Log.v(LOG_TAG,mPhotos.toString());

            }
            for (Photo singlePhoto: mPhotos)
            {
                Log.v(LOG_TAG, singlePhoto.toString());
            }
        }
        catch (JSONException jsone)
        {
            jsone.printStackTrace();
            Log.e (LOG_TAG, "Error processing Json data");
        }

    }

    public class Download500PxData extends DownloadRawData
    {
        protected void onPostExecute(String webData)
        {
            super.onPostExecute (webData);
            processResult();
        }
        @Override
        protected String doInBackground (String... params)
        {
            String[] par = {mDestinationUri.toString()};
            Log.v(LOG_TAG +"Do in Background", "mDestinationUri " +mDestinationUri.toString());
            return super.doInBackground(par);
        }
    }
}
