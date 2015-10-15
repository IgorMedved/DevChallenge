package com.beardreamembrace.devchallenge;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ViewPhotoThumbnailActivity extends AppCompatActivity
{
    private static String LOG_TAG = ViewPhotoThumbnailActivity.class.getSimpleName();
    private List<Photo> mPhotoList; //= new ArrayList<Photo>();
    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private Px500RecyclerViewAdapter px500RecyclerViewAdapter;
    private ArrayList<String> uriParameters;
    private PhotosPerScreen mPhotosPerScreen;
    private boolean loading = true;
    private int previousTotal = 0;
    private int currentPageNumber;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumbnail);


        Bundle extras = getIntent().getExtras();

        if (extras != null) // if the application launched by intent from custom Uri
        {
            Log.v(LOG_TAG, "initialized uri parameters correctly");
            uriParameters = extras.getStringArrayList("uriParameters");

        }
        else
            uriParameters = Uri500BuildHelper.createDefaultParamList();

        if (savedInstanceState != null) // if the screen rotated
        {
            mPhotoList = (ArrayList<Photo>) savedInstanceState.get("savedPhotoList");
            mPhotosPerScreen = (PhotosPerScreen) savedInstanceState
                    .getSerializable("savedPhotosPerScreen");
        }

        if (mPhotosPerScreen == null)
        {
            mPhotosPerScreen = new PhotosPerScreen(uriParameters.get(8));
        }
        final int VISIBLE_THRESHOLD = mPhotosPerScreen.getPhotosPerScreen() * 3;

        uriParameters.set(7, Integer.toString(VISIBLE_THRESHOLD));
        currentPageNumber = uriParameters.get(6).equals("") ? 1 : Integer
                .parseInt(uriParameters.get(6));
        Log.v(LOG_TAG, "photos per Screen " + mPhotosPerScreen.getPhotosPerScreen());


        if (mPhotoList == null)
        {
            ProcessPhotos processPhotos = new ProcessPhotos(uriParameters);

            processPhotos.execute();
        }
        else
            Log.v(LOG_TAG, "mPhotoList" + mPhotoList.size());


        //


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerThumbnail);
        mLayoutManager = new GridLayoutManager(this, mPhotosPerScreen.getPhotosPerWidth());
        mRecyclerView.setLayoutManager(mLayoutManager);
        px500RecyclerViewAdapter = new Px500RecyclerViewAdapter(ViewPhotoThumbnailActivity.this, mPhotoList);
        mRecyclerView.setAdapter(px500RecyclerViewAdapter);




        //GetRawData theRawData = new GetRawData("https://api.500px.com/v1/photos/?feature=popular&sort=created_at&page=2&rpp=2000&image_size=3&include_store=store_download&include_states=voted&consumer_key=medSwpC55WlYiLlQr3JxRkDkQD7OnUDDk2RLEpZQ");
        //theRawData.execute();


        //Get500PxData pxData = new Get500PxData(new String[]{}, new String[]{});
        //pxData.execute();
        //Uri500BuildHelper myHelper = new Uri500BuildHelper();


    }

    @Override
    public void onResume ()
    {
        super.onResume();
        final int VISIBLE_THRESHOLD = mPhotosPerScreen.getPhotosPerScreen()*3;

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()

        {
            @Override
            public void onScrolled (RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = mRecyclerView.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

                if (loading)
                {
                    if (totalItemCount > previousTotal)
                    {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (lastVisibleItem + VISIBLE_THRESHOLD))
                {
                    // End has been reached
                    loading = true;
                    currentPageNumber++;
                    uriParameters.set(6, "" + currentPageNumber);

                    Log.i("Yaeye!", "end called");

                    // Do something

                    ProcessPhotos processPhotos = new ProcessPhotos(uriParameters);

                    processPhotos.execute();


                }
            }

        });

        mRecyclerView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {

                Intent intent = new Intent (ViewPhotoThumbnailActivity.this,PhotoDetailActivity.class );
                intent.putExtra ("savedPhotoList", (Serializable)(px500RecyclerViewAdapter.getPhotos()));
                intent.putExtra ("savedCurrentPage", currentPageNumber);
                intent.putExtra ("savedPhotosPerScreen", mPhotosPerScreen.getPhotosPerScreen());
                intent.putExtra ("savedAdapterPosition", )
                startActivity(intent);



            }
        });
    }
    class PhotosPerScreen implements Serializable
    {
        private int photosPerWidth;
        private int photosPerHeight;


        private Point size;
        private String cropSize;

        public PhotosPerScreen (String cropSize)
        {
            Display display = getWindowManager().getDefaultDisplay();
            size = new Point();
            display.getSize(size);


            // case 1: screenWidth = 0 not initialized
            // do get screenWidth and cropSize and calculate other parameters based on it

            // case 2 screenWidth != this screen width recalculate everything

            // case 3 cropSize changed

            // case 4 screenWidth and cropSize are same as befor - no need to do anything
            int photoWidth=0;
            int photoHeight = 0;

            if (mPhotoList != null && mPhotoList.size() != 0)
            {
                this.cropSize = mPhotoList.get(0).getCropSize();
                switch (this.cropSize)
                {
                case "20":


                case "21":
                    for (int i = 0; i < mPhotoList.size(); i++)
                        if (photoWidth < mPhotoList.get(i).getPhotoWidth())
                             photoWidth = mPhotoList.get(i).getPhotoWidth();


                    break;
                default:
                    photoWidth = mPhotoList.get(0).getPhotoWidth();
                }
                photoHeight = mPhotoList.get(0).getPhotoHeight();

            }
            else
            {
                this.cropSize = cropSize;
                Photo temp = new Photo(null, null, null, null, null, this.cropSize, "1", "1");
                photoWidth = temp.getPhotoWidth();
                photoHeight = temp.getPhotoHeight();

            }

            photosPerWidth = (size.x - (size.x / photoWidth + 1) * 10) / photoWidth; // size.x- screen width;

            photosPerHeight = (size.y - (size.y / photoHeight + 1) * 10) / photoHeight; //size.y - screen height

            Log.v(LOG_TAG, "photosPerWidt " + photosPerWidth + " photos width " + photoWidth + " photos per Height " + photosPerHeight);

            photosPerWidth = photosPerWidth == 0 ? 1 : photosPerWidth;
            photosPerHeight = photosPerHeight == 0 ? 1 : photosPerHeight;


        }

        public int getPhotosPerWidth()
        {
            return photosPerWidth;
        }

        public int getPhotosPerScreen()
        {
            return photosPerHeight*photosPerWidth;
        }

        public void switchPlaces()
        {
            int temp = photosPerHeight;
            photosPerHeight = photosPerWidth;
            photosPerWidth = temp;
        }

    }



    @Override
    protected void onSaveInstanceState (Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("savedUriParameters", uriParameters);
        outState.putSerializable("savedPhotoList", (Serializable) mPhotoList);
        mPhotosPerScreen.switchPlaces();
        outState.putSerializable("savedPhotosPerScreen", mPhotosPerScreen);
        Log.v(LOG_TAG, "PhotoListSize " +mPhotoList.size());


    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_photo_thumbnail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

                px500RecyclerViewAdapter.loadNewData(mPhotoList);


            }
        }

    }

}
