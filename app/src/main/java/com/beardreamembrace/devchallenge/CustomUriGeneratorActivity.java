package com.beardreamembrace.devchallenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class CustomUriGeneratorActivity extends AppCompatActivity
{
    private static String LOG_TAG = CustomUriGeneratorActivity.class.getSimpleName();
    private List<Px500InputInterfaceCell> mCellList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private Px500UriSelectorRecyclerViewAdapter mPx500UriSelectorRecyclerViewAdapter;
    private Button submitButton;
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "onCreate");
        setContentView(R.layout.activity_custom_uri_generator);
        Px500ParseParameters parse = new Px500ParseParameters ();
        mCellList = parse.getCellList();
        Log.v(LOG_TAG, "" + mCellList.size());
        mRecyclerView = (RecyclerView) findViewById (R.id.recyclerG);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mPx500UriSelectorRecyclerViewAdapter = new Px500UriSelectorRecyclerViewAdapter(CustomUriGeneratorActivity.this, mCellList, savedInstanceState);
        mRecyclerView.setAdapter(mPx500UriSelectorRecyclerViewAdapter);
        submitButton = (Button) findViewById (R.id.btnSubmitCustomUri);




    }
    @Override
    protected void onStart ()
    {
        super.onStart();
        Log.v(LOG_TAG, "onStart");
    }

    @Override
    public void onResume ()
    {
        Log.v(LOG_TAG, "onResume");

        super.onResume();
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                ArrayList<String> uriParameters = mPx500UriSelectorRecyclerViewAdapter
                        .getParamList();


                Intent intent = new Intent(CustomUriGeneratorActivity.this, ViewPhotoThumbnailActivity.class);
                intent.putStringArrayListExtra("uriParameters", uriParameters);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.v(LOG_TAG, "onPause");
        if (submitButton.hasOnClickListeners())
            submitButton.setOnClickListener(null);
    }



    @Override
    protected void onSaveInstanceState (Bundle outState)
    {
        Log.v(LOG_TAG, "onSave");
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList("spinnerState", mPx500UriSelectorRecyclerViewAdapter.getSpinnersState());
        outState.putStringArrayList("textEditorContent", mPx500UriSelectorRecyclerViewAdapter.getTextContent());
    }

    @Override
    protected void onDestroy ()
    {
        super.onDestroy();
        Log.v(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onStop ()
    {
        super.onStop();
        Log.v(LOG_TAG, "onStop");
    }



    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_custom_uri_generator, menu);
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

