package com.beardreamembrace.devchallenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;
import java.util.ArrayList;

import static android.support.v7.widget.LinearLayoutManager.HORIZONTAL;

public class PhotoDetailActivity extends BasePhotoActivity
{
    private static String LOG_TAG = PhotoDetailActivity.class.getSimpleName();
    public static String MY_INTENT = PhotoDetailActivity.class.getSimpleName();

    //private List<Photo> mPhotoList; //= new ArrayList<Photo>();
    private SnappingRecyclerView mRecyclerView;
   // private ArrayList<String> uriParameters;
    private int currentPage;
    private boolean loading = true;
    private int previousTotal = 0;
    LinearLayoutManager layoutManager;
    private int thumbnailPhotosPerScreen;
    PhotosPerScreen mPhotosPerScreen;

    @Override
    protected void onResume ()
    {
        super.onResume();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {


            @Override
            public void onScrollStateChanged (RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);


            }

            @Override
            public void onScrolled (RecyclerView recyclerView, int dx, int dy)
            {




                int visibleItemCount = mRecyclerView.getChildCount();
                int totalItemCount = mRecyclerView.getLayoutManager().getItemCount();
                int lastVisibleItem = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                        .findLastVisibleItemPosition();

                if (loading)
                {
                    if (totalItemCount > previousTotal)
                    {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                Log.v (LOG_TAG, "photos per screen "+mPhotosPerScreen.getPhotosOnScreen()*3 + " total item count " + totalItemCount + " visible item count " + visibleItemCount + " last visible item count " + lastVisibleItem + " previous total " + previousTotal + " loading " + loading);
                if (!loading && (totalItemCount - visibleItemCount) <= (lastVisibleItem + mPhotosPerScreen.getPhotosOnScreen()*3))
                {
                    // End has been reached
                    loading = true;
                    currentPage++;
                    uriParameters.set(6, "" + currentPage);

                    //Log.i("Yaeye!", "end called");

                    // Do something

                    ProcessPhotos processPhotos = new ProcessPhotos(uriParameters);

                    processPhotos.execute();


                }

                super.onScrolled(recyclerView, dx, dy);
            }

        });


    }


    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo_detail);

        previousTotal = 0;


        mRecyclerView = (SnappingRecyclerView)(findViewById(R.id.recyclerDetailed));
        mRecyclerView.setSnapEnabled(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager (PhotoDetailActivity.this, HORIZONTAL, false);




        //int adapterPosition;

        Bundle extras = getIntent().getExtras();
        if (extras!= null)
        {
            mPhotoList = (ArrayList<Photo>) extras.getSerializable("savedPhotoList");
          //  currentPage = extras.getInt("savedCurrentPage");
            mPhotosPerScreen = (PhotosPerScreen) extras.getSerializable("savedPhotosPerScreen");
            int adapterPosition = extras.getInt("savedAdapterPosition");
            uriParameters = extras.getStringArrayList("savedUriParameters");
          //  previousTotal = extras.getInt("savedPreviousTotal");
            layoutManager.scrollToPosition(adapterPosition);
        }

        if (savedInstanceState != null) // if the screen rotated
        {
            mPhotoList = (ArrayList<Photo>) savedInstanceState.get("savedPhotoList");
            mPhotosPerScreen = (PhotosPerScreen) savedInstanceState
                    .getSerializable("savedPhotosPerScreen");
        }

        currentPage = uriParameters.get(6).equals("") ? 1 : Integer
                .parseInt(uriParameters.get(6));

        Px500PhotoDetailAdapter mAdapter = new Px500PhotoDetailAdapter(PhotoDetailActivity.this, mPhotoList);
        initializeBaseAdapter(mAdapter,mPhotoList);


        Log.v(LOG_TAG, "" + mPhotoList.size());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            launchIntent();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void launchIntent ()
    {
        Intent intent = new Intent(PhotoDetailActivity.this, ViewPhotoThumbnailActivity.class);
        intent.putExtra("savedPhotoList", (Serializable) (mPhotoList));
        //intent.putExtra("savedCurrentPage", currentPage);
        intent.putExtra("savedPhotosPerScreen", mPhotosPerScreen);
        intent.putExtra("savedAdapterPosition", ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition());
        intent.putExtra("savedUriParameters", uriParameters);
        //intent.putExtra("savedPreviousTotal", previousTotal);
        intent.putExtra ("UniqueId", MY_INTENT);

        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo_detail, menu);
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




/**
 * Created by Admin User on 10/14/2015.
 */






