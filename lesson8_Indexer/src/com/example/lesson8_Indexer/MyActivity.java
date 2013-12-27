package com.example.lesson8_Indexer;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MyActivity extends Activity {


    public static final String AUTHORITY = "org.nottingham.provider";
    public static final String STRUCTURE_NOTES = "notes";
    private static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri URI_NOTES = Uri.withAppendedPath(CONTENT_URI, STRUCTURE_NOTES);
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Cursor cursor = null;
        try
        {
            cursor = getContentResolver().query(URI_NOTES, null, null, null, null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        StringBuilder sb =  new StringBuilder();

        while (cursor.moveToNext()) {
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                sb.append(cursor.getString(i)).append(" ");
            }
        }

        String s = sb.toString();

        //s = s.replaceAll("[^[:word:]]", " ");

        s = s.replaceAll("\\W", " ");

        String[] words = s.split("\\s");

        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();

        for(int i=0; i<words.length; ++i)
        {
            if(hashMap.containsKey(words[i]))
            {
                hashMap.put(words[i], hashMap.get(words[i]) + 1);
            }
            else
            {
                hashMap.put(words[i],1);
            }
        }

        Iterator it = hashMap.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pairs = (HashMap.Entry)it.next();

            Log.w("Index", "[ "+pairs.getKey() + " ]: " + pairs.getValue());

//            it.remove(); // avoids a ConcurrentModificationException
        }
    }
}
