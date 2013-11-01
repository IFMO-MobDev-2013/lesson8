package com.example.Indexer;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyActivity extends Activity {
    public static final String STRUCTURE_NOTES = "notes";
    public static final String AUTHORITY = "org.nottingham.provider";

    private static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri URI_NOTES = Uri.withAppendedPath(CONTENT_URI, STRUCTURE_NOTES);

    public static final String T_NOTES_TEXT = "text";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Cursor cursor = getContentResolver().query(URI_NOTES, new String[]{T_NOTES_TEXT}, null, null,null, null);
        Map<String, Integer> words = new HashMap<String, Integer>();
        while(cursor.moveToNext()){
            String text = cursor.getString(cursor.getColumnIndex(T_NOTES_TEXT));
            Pattern pattern = Pattern.compile("([a-zA-Z]+)");
            Matcher matcher = pattern.matcher(text);
            while(matcher.find()){
                String word = matcher.group(1);
                Integer cnt = words.get(word);
                if(cnt == null){
                    cnt = 0;
                }
                ++cnt;
                words.put(word, cnt);
            }
        }
        Set<String> keySet = words.keySet();
        Iterator<String> it = keySet.iterator();
        while(it.hasNext()){
            String key = it.next();
            Log.w("logg", key + ": " + words.get(key));
        }
        Toast.makeText(getApplicationContext(), "All indexed", Toast.LENGTH_LONG).show();
    }
}
