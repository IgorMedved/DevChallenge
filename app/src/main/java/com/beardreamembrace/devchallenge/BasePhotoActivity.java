package com.beardreamembrace.devchallenge;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Admin User on 10/14/2015.
 */
public class  BasePhotoActivity extends AppCompatActivity
{
    protected ArrayList<Photo> mPhotoList;
    protected CustomAdapter mAdapter;
    protected ArrayList<String> uriParameters;
    private ArrayList<PhotoSize> spanLookupList;

    private final static String LOG_TAG = BasePhotoActivity.class.getSimpleName();



    public void initializeBaseAdapter (CustomAdapter myAdapter, ArrayList<Photo> photoList)
    {
        mAdapter = myAdapter;
        mPhotoList = photoList;
    }

    protected ArrayList<PhotoSize> getSpanLookupList()

    {
        return spanLookupList;
    }

    public class ProcessPhotos extends Get500PxData
    {



        public ProcessPhotos (ArrayList<String> paramValues)
        {
            super(paramValues); // Created new URI and initialized rawUrl
        }

        public void execute()
        {
            //super.execute();

            ProcessData processData = new ProcessData();
            processData.execute(); // run in Backround of raw data downloaded data
        }

        /*       public void addPhotos(ArrayList<String> paramValues)
               {
                   super.addPhotos(paramValues);
               }
       */
        public class ProcessData extends Download500PxData
        {
            protected void onPostExecute(String webData)
            {
                super.onPostExecute(webData);

                Log.v(LOG_TAG, Integer.toString(getPhotos().size()));
                if (mPhotoList == null)
                {
                    mPhotoList = getPhotos();
                }
                else
                    mPhotoList.addAll(mPhotoList.size(), getPhotos());

                if(!Photo.isCroppedSize(uriParameters.get(8)))
                {
                    photoSizeUpdate(uriParameters.get(8));
                    createSpanSizeLookup();
                }

               // for (int i = 0; i < mPhotoList.size(); i++)
                //    Log.v(LOG_TAG, "photo " + i+ " cropsize " +mPhotoList.get(i).getCropSize()+ " link "+ mPhotoList.get(i).getImage());


                mAdapter.loadNewData(mPhotoList);


            }
        }

        private void photoSizeUpdate (String cropSize)
        {
            for (int i = 0; i < mPhotoList.size(); i ++)
            {
                mPhotoList.get(i).setSize(cropSize);
            }
        }

        public void createSpanSizeLookup()
        {
            if (mPhotoList != null)
            {
                spanLookupList = new ArrayList<>(mPhotoList.size());


                int position = 0;
                while (position <mPhotoList.size())
                {
                    optimizeLayout (position); // second parameter - number of photos per widths with which the layout optimization starts  do not change this parameter
                    position= spanLookupList.size();
                }
            }
        }


        public ArrayList<PhotoSize> getSpanLookupList ()
        {
            return spanLookupList;
        }

        private void optimizeLayout (int position)
        {
            final int photosPerWidth = 2;

            ArrayList<Integer> photoHeights = new ArrayList<>();
            ArrayList<Integer> photoWidths = new ArrayList<>();

            if(mPhotoList.size() == position +1)
            {
                int height = mPhotoList.get(position).getFormattedPhotoHeight(mPhotoList.get(position).getCropSize());
                int width = mPhotoList.get(position).getFormattedPhotoWidth(mPhotoList.get(position)
                                                                                      .getCropSize());
                int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
                spanLookupList.add(new PhotoSize(PhotoSize.MINIMUN_HEIGHT*screenWidth/width, screenWidth));
                return;
            }

            for (int i = 0; i  < photosPerWidth; i++)
            {
                int height = mPhotoList.get(position+i).getFormattedPhotoHeight(mPhotoList.get(position+i).getCropSize());
                int width = mPhotoList.get(position+i).getFormattedPhotoWidth(mPhotoList.get(position+i).getCropSize());
                photoHeights.add(height);
                photoWidths.add(width);
            }

            calculateSize(photoHeights, photoWidths);
        }

        private void calculateSize (ArrayList<Integer> heights, ArrayList<Integer>widths)
        {
            int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
            final int minimumHeight = PhotoSize.MINIMUN_HEIGHT;
            int sum = 0;
            for (int i = 0; i <widths.size(); i++)
            {
                sum+= widths.get(i)*minimumHeight/heights.get(i);

            }

            if (sum > screenWidth)
            {
                if (widths.size()== 2)

                    spanLookupList.add (new PhotoSize(heights.get(0)*screenWidth/widths.get(0) ,screenWidth));

                return;
            }
            else
            {
                if (widths.size() == 2)
                {
                    for (int i = 0; i < widths.size(); i++)
                        spanLookupList.add(new PhotoSize(minimumHeight * screenWidth / sum , widths
                                .get(i) * minimumHeight * screenWidth / heights.get(i) / sum -1));
                }
                else
                {
                    for (int i = 0; i < widths.size() -1; i++)
                    {
                        int pos = spanLookupList.size() - widths.size() +1  + i;
                        spanLookupList.set(pos, new PhotoSize(minimumHeight * screenWidth / sum, widths
                                .get(i) * minimumHeight * screenWidth / heights.get(i) / sum -1));
                    }
                    spanLookupList.add(new PhotoSize(minimumHeight * screenWidth / sum, widths
                            .get(widths.size()-1) * minimumHeight * screenWidth/ heights.get(heights.size()-1) / sum-1));

                }
                int photoPosition = spanLookupList.size();
                //Log.v (LOG_TAG, "photoList size" +mPhotoList.size() + " sum " +sum);
                if (photoPosition < mPhotoList.size())
                {
                    int height = mPhotoList.get(photoPosition).getFormattedPhotoHeight(mPhotoList.get(photoPosition).getCropSize());
                    int width = mPhotoList.get(photoPosition).getFormattedPhotoWidth(mPhotoList.get(photoPosition).getCropSize());
                    heights.add(height);
                    widths.add(width);
                    calculateSize(heights, widths);
                }
            }

        }

    }

}


