package com.beardreamembrace.devchallenge;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Admin User on 10/1/2015.
 */
public class Px500ParseParameters
{
    private ArrayList<Px500InputInterfaceCell> mCellList;

    public Px500ParseParameters()
    {
        mCellList = new ArrayList<>();

        Px500InputInterfaceCell currentCell;
        ArrayList<String> currentArray;

        // first cell
        currentArray = new ArrayList<>();
        currentArray.add("Parameter");
        currentCell = new Px500InputInterfaceCell(currentArray);
        mCellList.add(currentCell);

        // second cell
        currentArray = new ArrayList<>();
        currentArray.add("Value");
        currentCell = new Px500InputInterfaceCell(currentArray);
        mCellList.add(currentCell);
        // Parameter cells
        for (int i = 0; i<Uri500BuildHelper.PARAM_NAMES.length; i++)
        {
            currentArray = new ArrayList<>();
            currentArray.add(Uri500BuildHelper.PARAM_NAMES[i]);
            currentCell = new Px500InputInterfaceCell(currentArray);
            mCellList.add(currentCell);
        }

        // value cells
        for (int position = 3; position < Uri500BuildHelper.PARAM_NAMES.length*2+3; position+=2)
        {
            switch ((position-3)/2)
            {
            case 0:case 3:case 4:case 5:case 8:case 9: case 10:
                currentArray = new ArrayList<>();

                currentArray.addAll ( new ArrayList<> (Arrays.asList(Arrays.copyOfRange(Uri500BuildHelper.ALL_PARAM_VALUES[(position - 3) / 2],2,Uri500BuildHelper.ALL_PARAM_VALUES[(position - 3) / 2].length))));
                if ((position -3) /2 != 0 &&(position-3)/2 !=8)
                currentArray.add(0, "not used");

                currentCell = new Px500InputInterfaceCell(currentArray);
                mCellList.add(position, currentCell);
                break;
            default:
                currentArray = null;
                currentCell = new Px500InputInterfaceCell(currentArray);
                mCellList.add(position, currentCell);

            }
        }

    }

    public ArrayList<Px500InputInterfaceCell> getCellList ()
    {
        return mCellList;
    }
}
