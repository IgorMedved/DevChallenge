package com.beardreamembrace.devchallenge;

import java.util.ArrayList;

/**
 * Created by Admin User on 9/30/2015.
 */
public class Px500InputInterfaceCell
{
    public enum Cell_Data_Type {
        NULL(0) , STRING(1), STRINGLIST(2);


        private final int value;
        Cell_Data_Type (int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }



    private Cell_Data_Type mType;
    private ArrayList <String> cellValue;

    public Px500InputInterfaceCell(ArrayList<String> value)
    {
        if (value == null)
        {
            mType = Cell_Data_Type.NULL;
            cellValue = null;
        }
        else
        {
            cellValue = new ArrayList<>();
            cellValue.add(value.get(0));
            if (value.size() == 1)
            {
                mType = Cell_Data_Type.STRING;

            }
            else
            {
                mType = Cell_Data_Type.STRINGLIST;
                for (int i = 1; i < value.size(); i++)
                {
                    cellValue.add(value.get(i));
                }
            }
        }
    }

    public Cell_Data_Type getType ()
    {
        return mType;
    }

    public ArrayList<String> getCellValue ()
    {
        return cellValue;
    }
}
