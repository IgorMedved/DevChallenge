package com.beardreamembrace.devchallenge;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin User on 9/30/2015.
 */

public class Px500UriSelectorRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<Px500InputInterfaceCell> mCellList;
    private Context mContext;


    public ArrayList<Integer> getSpinnersState ()
    {
        return spinnersState;
    }

    public ArrayList<String> getTextContent ()
    {
        return textContent;
    }

    private ArrayList<Integer> spinnersState;
    private ArrayList<String> textContent;
    //private ArrayList<String> paramList;

    public Px500UriSelectorRecyclerViewAdapter (Context mContext, List<Px500InputInterfaceCell> mCellList)
    {
        this (mContext, mCellList, null);


    }

    public Px500UriSelectorRecyclerViewAdapter (Context mContext, List <Px500InputInterfaceCell> mCellList, Bundle savedData)
    {
        this.mCellList = mCellList;
        this.mContext = mContext;
        if (savedData != null && savedData.getStringArrayList("textEditorContent")!=null && savedData.getIntegerArrayList("spinnerState")!=null)
        {
            textContent = savedData.getStringArrayList("textEditorContent");
            spinnersState = savedData.getIntegerArrayList("spinnerState");
        }
        else
        {


            spinnersState = initializeSpinners();
            textContent = initializeText();
        }


    }

    public ArrayList<String> getParamList ()
    {
        final int PARAM_NUMBER = mCellList.size()/2 - 1;
        ArrayList<String> paramList = new ArrayList<>(PARAM_NUMBER);

        for (int i = 0; i < PARAM_NUMBER; i ++)
            paramList.add(getContent((i+1)*2+1));



        return paramList;
    }


    private ArrayList<Integer> initializeSpinners ()
    {
        final int SPINNER_NUMBER = 7;
        ArrayList<Integer> spinnerStates = new ArrayList<>(SPINNER_NUMBER);


        for (int i = 0; i < SPINNER_NUMBER; i++)
            spinnerStates.add(0);

        return spinnerStates;
    }

    private ArrayList <String> initializeText()
    {
        final int EDIT_TEXT_NUMBER=5;
        ArrayList<String> editTextContent = new ArrayList (EDIT_TEXT_NUMBER);

        for (int i = 0; i <EDIT_TEXT_NUMBER; i++)
        {
            editTextContent.add("");

        }
        return editTextContent;
    }
    private String getContent (int position)
    {
        int viewType = getItemViewType(position);

        if (viewType == 1)
        {
            return mCellList.get(position).getCellValue().get(spinnersState
                    .get(getSpinnerPosition(position))); // return content of spinner based on spinner position
        }
        else
            return textContent.get(getEditTextPosition(position));
    }

    private int getSpinnerPosition (int position)
    {

        switch (position)
        {
        case 3:
            return 0;
        case 9:
            return 1;
        case 11:
            return 2;
        case 13:
            return 3;
        case 19:
            return 4;

        case 21:
            return 5;
        default:
            return 6;
        }


    }

   private int getEditTextPosition (int position)
    {
        switch(position)
        {
        case 5:
            return 0;
        case 7:
            return 1;
        case 15:
            return 2;
        case 17:
            return 3;
        default:
            return 4  ;
        }
    }


    enum VIEW_HOLDER_TYPE
    {
        HEADER (0), SPINNER (1), TEXT(2), EDITTEXT(3);

        private final int value;
        VIEW_HOLDER_TYPE (int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }

    class Px500SpinnerViewHolder extends RecyclerView.ViewHolder
    {
        protected Spinner mSpinner;

        public Px500SpinnerViewHolder (View view)
        {
            super(view);
            this.mSpinner = (Spinner) view.findViewById(R.id.spinCell);

            mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
                {

                    // update spinner state
                    int spinnerPosition = getAdapterPosition();
                    Log.v ("onItemSelected",  "spinnerPosition " + spinnerPosition );
                    int spinnerStatePosition = getSpinnerPosition(spinnerPosition);
                    if(spinnersState.get(spinnerStatePosition)!= position)
                    {
                        spinnersState.set(spinnerStatePosition, position);
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView)
                {
                    return;
                }

            });


        }

    }

    class Px500HeaderViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView mTextView;

        public Px500HeaderViewHolder (View view)
        {
            super(view);
            this.mTextView = (TextView) view.findViewById(R.id.txtHeader);
        }
    }

    class Px500TextViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView mTextView;

        public Px500TextViewHolder (View view)
        {
            super(view);
            this.mTextView = (TextView) view.findViewById(R.id.txtCell);


        }
    }

     class Px500EditTextViewHolder extends RecyclerView.ViewHolder
    {
        protected EditText mEditText;

        public Px500EditTextViewHolder (View view)
        {
            super(view);
            this.mEditText = (EditText) view.findViewById(R.id.editTxtCell);

            mEditText.addTextChangedListener(new TextWatcher()
            {

                public void afterTextChanged(Editable s)
                {
                    int textViewPosition = getAdapterPosition();
                    int textPosition = getEditTextPosition(textViewPosition);
                    if(!mEditText.getText().toString().equals(mCellList.get(textViewPosition).getCellValue()))
                        textContent.set(textPosition, mEditText.getText().toString());

                }

                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {

                }
            });
        }
    }


    public int getParamFromText (int textPos)
    {
        switch (textPos)
        {
        case 0:
            return 1;
        case 1:
            return 2;
        case 2:
            return 5;
        case 3:
            return 6;
        default:
            return 11;
        }
    }

    public int getParamFromSpinner(int spinnerPos)
    {
        switch (spinnerPos)
        {
        case 0:
            return 0;
        case 1:
            return 1 + 2;
        case 2:
            return 2 + 2;
        case 3:
            return 3 + 2;
        case 4:
            return 4 + 4;
        case 5:
            return 5 + 4;

        default:
            return 6 + 4;
        }
    }


    @Override
    public int getItemViewType(int position) {

        switch(position)
        {
        case 0:
        case 1:
            return VIEW_HOLDER_TYPE.HEADER.getValue();
        case 3:
        case 9:
        case 11:
        case 13:
        case 19:
        case 21:
        case 23:
            return VIEW_HOLDER_TYPE.SPINNER.getValue();


        default: // not any position used above
            switch (position % 2) // divide positions in odd and even
            {
            case 0:// even position, but not zero
                return VIEW_HOLDER_TYPE.TEXT.getValue();
            case 1:// odd position, but not any of the above
                return VIEW_HOLDER_TYPE.EDITTEXT.getValue();
            }
        }
        return 0;


    }







    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        switch (viewType)
        {
        case 0:
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_uri_header_cell_layout, null);

            return new Px500HeaderViewHolder(view);
        case 1:
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_uri_spinner_cell_layout, null);
            Px500SpinnerViewHolder holder = new Px500SpinnerViewHolder(view);
            Log.v("test", "added spinner");
            ///spinners.add(holder);
            return holder;
        case 2:
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_uri_text_cell_layout, null);
            return new Px500TextViewHolder(view);
        case 3:
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_uri_edit_txt_cell, null);
            Px500EditTextViewHolder text = new Px500EditTextViewHolder(view);
            Log.v("test", "add text");
            //texts.add(text);
            return  text;

        }
        return null;
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder viewHolder, int position)
    {


        int viewType = getItemViewType(position);

        switch (viewType)
        {
        case 0:
             Px500HeaderViewHolder mHViewHolder = (Px500HeaderViewHolder)viewHolder;
             mHViewHolder.mTextView.setText(mCellList.get(position).getCellValue().get(0));
             break;
        case 1:
            Px500SpinnerViewHolder mSViewHolder = (Px500SpinnerViewHolder)viewHolder;

            ArrayList<String> spinnerData = mCellList.get(position).getCellValue();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,android.R.layout.simple_spinner_item, spinnerData);


            mSViewHolder.mSpinner.setAdapter(adapter);

            int selection = spinnersState.get(getSpinnerPosition(position));
            Log.v("bind spinner", "selection " + selection);

            mSViewHolder.mSpinner.setSelection(selection);

            break;


        case 2:
            Px500TextViewHolder mTViewHolder = (Px500TextViewHolder)viewHolder;
            mTViewHolder.mTextView.setText(mCellList.get(position).getCellValue().get(0));
            break;
        case 3:
            Px500EditTextViewHolder mEViewHolder = (Px500EditTextViewHolder)viewHolder;
            Log.v("bind", "added text");
            mEViewHolder.mEditText.setText(textContent.get(getEditTextPosition(position)));


            break;


        }


    }

    @Override
    public int getItemCount ()
    {
        return (null!= mCellList ? mCellList.size():0);
    }

}