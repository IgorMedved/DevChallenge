package com.beardreamembrace.devchallenge;

import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Admin User on 9/25/2015.
 */
public class Uri500BuildHelper
{

    final static String LOG_TAG = Uri500BuildHelper.class.getSimpleName();
    final static String PX500_API_BASE_URL = "https://api.500px.com/v1";
    final static String[] PX500_API_RESOURCES = {"/photos", "/upload", "/users", "/blogs", "/collections"};
    public enum paramTypes {NOTREQUIRED, REQUIRED, DEPENDENT };
    public enum paramValueType {NUMBER, WORD};
    public static final int MAX_PAGE = 1000;
    public static final String[] CATEGORY_NAMES = {"uncategorized", "celebrities", "film", "journalism", "nude",
            "black_and_white", "still_life", "people","landscapes", "city_and_architecture",  "abstract", "animals",
            "macro", "travel", "fashion", "commercial", "concert", "sport", "nature", "performing_arts", "family",
            "street", "underwater", "food", "fine_art", "wedding", "transportation", "urban_exploration" };
    public static final String[] FEATURE_NAMES = {"popular", "highest_rated", "upcoming", "editors", "fresh_today",
            "fresh_yesterday", "fresh_week", "user", "user_friends", "user_favorites"};
    public static final String[] STORE_NAMES = {"store_download", "store_print"};
    public static final String[] STATES_NAMES = {"voted", "favorited", "purchased"};
    public static final String[]  SORT_NAMES = {"created_at", "rating", "highest_rating", "times_viewed",
            "votes_count", "favorites_count", "comments_count", "taken_at"};
    public static final String[] IMAGE_SIZE_NAMES = { "1", "2", "3", "100", "200", "440", "600", "4", "5", "20",
            "21","30", "1080", "1060", "2048"};
    public static final String[] PARAM_NAMES = {"feature","user_id", "username", "only", "exclude", "sort", "page", "rpp", "image_size", "include_store", "include_states", "tags"};
    public static final String[][] ALL_PARAM_VALUES = {
            concatStringArray(new String[]{"" +paramTypes.REQUIRED, "" + paramValueType.WORD}, FEATURE_NAMES), // features
            {"" +paramTypes.NOTREQUIRED, "" + paramValueType.NUMBER,"-1", "1", "" +Long.MAX_VALUE}, // userId
            {"" +paramTypes.NOTREQUIRED, ""+ paramValueType.WORD, "anonymous"}, //username
            concatStringArray (new String[]{"" +paramTypes.NOTREQUIRED, ""+ paramValueType.WORD}, CATEGORY_NAMES), //possible values for only parameter
            concatStringArray (new String[]{"" +paramTypes.NOTREQUIRED, ""+ paramValueType.WORD}, CATEGORY_NAMES), // possible values for exclude parameter
            concatStringArray (new String[]{"" +paramTypes.NOTREQUIRED, ""+ paramValueType.WORD}, SORT_NAMES), // sort values
            {"" +paramTypes.NOTREQUIRED, "" + paramValueType.NUMBER, "1" ,"1", "1000"}, // page values
            {"" +paramTypes.NOTREQUIRED, "" + paramValueType.NUMBER, "20", "20", "100"}, // results per page values
            concatStringArray(new String[]{"" +paramTypes.REQUIRED, "" + paramValueType.WORD ,"440"}, IMAGE_SIZE_NAMES),// image size values
            concatStringArray (new String[]{"" +paramTypes.NOTREQUIRED, ""+ paramValueType.WORD}, STORE_NAMES),// include store values
            concatStringArray (new String[]{"" +paramTypes.NOTREQUIRED, ""+ paramValueType.WORD}, STATES_NAMES),// include states values
            concatStringArray (new String[]{"" +paramTypes.NOTREQUIRED, ""+ paramValueType.WORD}, CATEGORY_NAMES),
            {"" +paramTypes.NOTREQUIRED, ""+ paramValueType.WORD, ""}
   };


    private static Uri mDestinationUri;
    private static final String CONSUMER_KEY = ConsumerKey.CONSUMERKEY;


    public Uri500BuildHelper (ArrayList<String> paramValues)

    {

        Uri.Builder myBuilder = Uri.parse(PX500_API_BASE_URL + PX500_API_RESOURCES[0]).buildUpon();

        if (paramValues == null)
        {// build default URI
            paramValues = createDefaultParamList ();

        }
        for (int i = 0; i < PARAM_NAMES.length; i++)
        {
            Log.v("test", " " + i + " " + paramValues.get(i) + " " + (paramValues.get(i).equals( "") || paramValues.get(i).equals( "not used")));
            if (i!= 8)
            {
                if (isValidParameter(i, paramValues.get(i)))
                    myBuilder.appendQueryParameter(PARAM_NAMES[i], paramValues.get(i));
            }
            else
            {
                ArrayList <String> imageSizes = getImageSizes(paramValues.get(i));
                for (int j = 0; j < imageSizes.size();j++ )
                {
                    myBuilder.appendQueryParameter(PARAM_NAMES[i]+ "[]", imageSizes.get(j));
                }
            }


        }


        myBuilder.appendQueryParameter("consumer_key", CONSUMER_KEY);


        mDestinationUri =myBuilder.build();
    }

    public ArrayList<String> getImageSizes(String imageSize)
    {
        //DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();


        ArrayList<String> imageSizes = new ArrayList<>();
        imageSizes.add(imageSize);

        for (int i = 0; i < IMAGE_SIZE_NAMES.length; i++)
            if (!Photo.isCroppedSize(IMAGE_SIZE_NAMES[i] ))
                imageSizes.add(IMAGE_SIZE_NAMES[i]);




        return imageSizes;
    }

/*    public void getImageSize(ArrayList<String> imageSizes, int width, int height)
    {
        int dimension = width> height? width: height;

        if (dimension <= 900)
        {
            imageSizes.add("30");
            imageSizes.add("4");
            imageSizes.add("1080");
        }
        else if (dimension <= 1080)
        {
            imageSizes.add("4");
            imageSizes.add("1080");
            imageSizes.add("5");
        }
        else if (dimension <= 1170)
        {

            imageSizes.add("1080");
            imageSizes.add("5");
            imageSizes.add("1600");
        }
        else
        {
            imageSizes.add("5");
            imageSizes.add("1600");
            imageSizes.add("2048");
        }
    }*/

    public static boolean isValidParameter(int position, String value)
    {
        switch (position)
        {
        case 0: //feature
        case 8: // the value for feature is always true (default or from spinner
            return true;
        case 3:
        case 4:
        case 5:
        case 9:
        case 10:
            return !(value.equals("not used")) ;
        case 1:
        case 2:
        case 11:
            return !value.equals("");
        case 6: // page number parameter
            try
            {
                int intValue = Integer.parseInt(value);
                return (intValue > 0 && intValue < MAX_PAGE);
            }
            catch (NumberFormatException e)
            {
                return false;
            }
        case 7: // results per page Parameter
            try
            {
                int intValue = Integer.parseInt(value);
                return (intValue > 0 && intValue <=5000);

            }
            catch (NumberFormatException e)
            {
                return false;
            }


        }


        return false;
    }

    public static ArrayList<String> createDefaultParamList()
    {
        ArrayList<String>paramValues = new ArrayList<>();
        for (int i = 0; i < PARAM_NAMES.length; i ++)
            paramValues.add(getDefaultParameter(i));

        return paramValues;
    }

    public static String getDefaultParameter (int position)
    {
        switch (position)
        {
        case 0:
            return "popular"; //default feature stream
        case 8:
            return "440"; // default image size
        case 3:
        case 4:
        case 5:

        case 9:
        case 10:
            return "not used";
        default:
            return "";
        }
    }

    public static Uri getmDestinationUri ()
    {
        return mDestinationUri;
    }




    //default constructor
    private static String[] concatStringArray (String[] a, String[] b)
    {
        int cLen = a.length+b.length;
        String[]c = new String[cLen];

        for (int i = 0; i < a.length; i ++)
        {
            c[i] = a[i];

        }
        for (int i = a.length; i < cLen; i ++)
        {
            c[i] = b[i-a.length];
        }

        return c;
    }
}

