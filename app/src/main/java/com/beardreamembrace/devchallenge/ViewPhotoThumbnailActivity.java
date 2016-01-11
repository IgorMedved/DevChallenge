package com.beardreamembrace.devchallenge;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;
import java.util.ArrayList;

public class ViewPhotoThumbnailActivity extends BasePhotoActivity
{
    private static String LOG_TAG = ViewPhotoThumbnailActivity.class.getSimpleName();
    public static String MY_INTENT = ViewPhotoThumbnailActivity.class.getSimpleName();
    //= new ArrayList<Photo>();
    private BaseRecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private Px500RecyclerViewAdapter px500RecyclerViewAdapter;
    //private ArrayList<String> uriParameters;
    private PhotosPerScreen mPhotosPerScreen;
    private boolean loading = true;
    private int previousTotal = 0;
    private int currentPageNumber;


    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumbnail);

        previousTotal = 0;

        int position  = resolveIntent();



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
        final int VISIBLE_THRESHOLD = mPhotosPerScreen.getPhotosOnScreen() * 3;

        uriParameters.set(7, Integer.toString(VISIBLE_THRESHOLD));
        currentPageNumber = uriParameters.get(6).equals("") ? 1 : Integer
                .parseInt(uriParameters.get(6));
        Log.v(LOG_TAG, "photos per Screen " + mPhotosPerScreen.getPhotosOnScreen());


        if (mPhotoList == null)
        {
            ProcessPhotos processPhotos = new ProcessPhotos(uriParameters);

            processPhotos.execute();
        }
        else
            Log.v(LOG_TAG, "mPhotoList" + mPhotoList.size());


        //


        mRecyclerView = (BaseRecyclerView) findViewById(R.id.recyclerThumbnail);
        if (Photo.isCroppedSize(uriParameters.get(8)))
            mLayoutManager = new GridLayoutManager(this, mPhotosPerScreen.getPhotosPerWidth());
        else

            mLayoutManager = new GridLayoutManager(this, Resources.getSystem().getDisplayMetrics().widthPixels);
        if (position!=-1)
            mLayoutManager.scrollToPosition(position);
        mRecyclerView.setLayoutManager(mLayoutManager);
        px500RecyclerViewAdapter = new Px500RecyclerViewAdapter(ViewPhotoThumbnailActivity.this, mPhotoList);
        initializeBaseAdapter(px500RecyclerViewAdapter, mPhotoList);
 //       Log.v (LOG_TAG, mPhotoList.get(0).toString());
        mRecyclerView.setAdapter(px500RecyclerViewAdapter);


        //GetRawData theRawData = new GetRawData("https://api.500px.com/v1/photos/?feature=popular&sort=created_at&page=2&rpp=2000&image_size=3&include_store=store_download&include_states=voted&consumer_key=medSwpC55WlYiLlQr3JxRkDkQD7OnUDDk2RLEpZQ");
        //theRawData.execute();


        //Get500PxData pxData = new Get500PxData(new String[]{}, new String[]{});
        //pxData.execute();
        //Uri500BuildHelper myHelper = new Uri500BuildHelper();


    }

    public int getCurrentPageNumber ()
    {
        return currentPageNumber;
    }

    public PhotosPerScreen getPhotosPerScreen ()
    {

        return mPhotosPerScreen;
    }

    public void launchIntent (int position)
    {
        Intent intent = new Intent(ViewPhotoThumbnailActivity.this, PhotoDetailActivity.class);
        intent.putExtra("savedPhotoList", (Serializable) (mPhotoList));
        intent.putExtra("savedCurrentPage", currentPageNumber);
        intent.putExtra("savedPhotosPerScreen", mPhotosPerScreen);
        intent.putExtra("savedAdapterPosition", position);
        intent.putExtra("savedUriParameters", uriParameters);
        intent.putExtra("savedPreviousTotal", previousTotal);
        intent.putExtra ("UniqueId", MY_INTENT);
        startActivity(intent);
    }

    @Override
    public void onResume ()
    {
        super.onResume();
        final int VISIBLE_THRESHOLD = mPhotosPerScreen.getPhotosOnScreen() * 3;

        mAdapter.setMyListener(mRecyclerView);

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

        if (!Photo.isCroppedSize(uriParameters.get(8)))
        {
            mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver()
            {
                @Override
                public void onChanged ()
                {


                   /*for (int i = 0; i < mPhotoList.size(); i++)
                    {
                        Log.v(LOG_TAG, "Photo " + i + "\twidth: " + getSpanLookupList().get(i).getWidth() + "\theight "+ getSpanLookupList().get(i).getHeight());
                        Log.v(LOG_TAG, "Photo " + i + "\twidht: " + mPhotoList.get(i).getWidth() + "\theight " +mPhotoList.get(i).getHeight());

                    }*/
                    if (mPhotoList.size()== mAdapter.getItemCount())
                    {
                        Log.v (LOG_TAG, "Second call");
                    }
                    else
                    {
                        Log.v(LOG_TAG, "Right call");

                        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                            @Override
                            public int getSpanSize(int position) {
                                return getSpanLookupList().get(position).getWidth();
                            }
                        });
                    }

                }
            });
        }


    }


    @Override
    protected void onSaveInstanceState (Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("savedUriParameters", uriParameters);
        outState.putSerializable("savedPhotoList", (Serializable) mPhotoList);

        outState.putSerializable("savedPhotosPerScreen", (Serializable) mPhotosPerScreen);
        Log.v(LOG_TAG, "PhotoListSize " + mPhotoList.size());


    }

    private int resolveIntent()
    {
        Bundle extras = getIntent().getExtras();
        Log.v(LOG_TAG, "inside resolve");


        if (extras != null) // if the application launched by intent from custom Uri
        {
            String id = extras.getString("UniqueId");
            Log.v(LOG_TAG, "gotExtras " + id);

            if (id.equals(MainActivity.MY_INTENT))
            {
                uriParameters = Uri500BuildHelper.createDefaultParamList();
                return -1;
            }
            else if (id.equals(CustomUriGeneratorActivity.MY_INTENT))
            {
                Log.v(LOG_TAG, CustomUriGeneratorActivity.MY_INTENT);
                uriParameters = extras.getStringArrayList("uriParameters");
                return -1;
            }
            else if (id.equals(PhotoDetailActivity.MY_INTENT))
            {
                uriParameters = extras.getStringArrayList("savedUriParameters");
               // previousTotal = extras.getInt("savedPreviousTotal");
                mPhotoList = (ArrayList<Photo>) extras.getSerializable("savedPhotoList");
                mPhotosPerScreen = (PhotosPerScreen) extras.getSerializable("savedPhotosPerScreen");
                //Log.v(LOG_TAG, "should be here " + (uriParameters == null));
                return extras.getInt("savedAdapterPosition");

            }
        }
        return -1;


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
}

