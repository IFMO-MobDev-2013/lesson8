package org.nottingham;

/**
 * Created with IntelliJ IDEA.
 * User: asus
 * Date: 01.11.13
 * Time: 14:16
 * To change this template use File | Settings | File Templates.
 */

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import java.util.ArrayList;

public class IndexerActivity extends Activity {
    private static final String TAG = NoteActivity.class.getSimpleName();

    private static final ArrayList<String> includedWord = new ArrayList<String>();
    private static final ArrayList<Integer> includedCount = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.indexer);

        evaluateIncludes();

    }

    private void evaluateIncludes() {
        Cursor c = getContentResolver().query(NotesProvider.URI_NOTES, null, null, null, null);
        String s, s0;

        while (c.moveToNext()) {
            s = c.getString(c.getColumnIndex(NotesProvider.T_NOTES_TEXT));

            String[] words = s.split("\\s+");
            for (int i = 0; i < s.length(); i++){
                pushWord(words[i]);

                s = "";
            }
        }

        int min = Integer.MAX_VALUE, max = -1;
        int mini = -1, maxi = -1;
        for (int i = 0; i < includedWord.size(); i++){
            if (includedCount.get(i) > max){
                max = includedCount.get(i);
                maxi = i;
            } if (includedCount.get(i) < min){
                min = includedCount.get(i);
                mini = i;
            }
        }
        Toast.makeText(getApplicationContext(), "Statistics\n" + "Most rare word: " + includedWord.get(mini) + " - " + includedCount.get(mini) + " usages" + "\n"
                + "Most often word: " + includedWord.get(maxi) + " - " + includedCount.get(maxi) + " usages" + "\n"
                + "Total words: " + includedWord.size(), Toast.LENGTH_LONG).show();
    }

    void pushWord(String s){
        for (int i = 0; i < includedWord.size(); i++){
            if (includedWord.get(i).equals(s)){
                includedCount.add(i, includedCount.get(i).intValue()+1);
                break;
            }
        }
        includedCount.add(1);
        includedWord.add(s);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.note, menu);
        return true;
    }



}
