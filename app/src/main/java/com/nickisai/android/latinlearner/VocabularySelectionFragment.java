package com.nickisai.android.latinlearner;

import android.app.ListFragment;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Nicholas on 7/24/2015.
 */
public class VocabularySelectionFragment extends ListFragment {
    private ArrayList<String> mChapterNames;
    private TextView mTitle;
    private static final String TAG = "VocabSelFrag";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ResourceLoader chapterLoader = new ResourceLoader(R.raw.chapter_names, getActivity());
        try {
            mChapterNames = chapterLoader.readAsChapters();
            Log.e(TAG, "Loaded Successfully!");
        } catch (IOException e) {
            mChapterNames = new ArrayList<String>();
            Log.e(TAG, "Failed to load chapter names");
        }

        ChapterAdapter adapter = new ChapterAdapter(mChapterNames);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_item_empty, parent, false);

        ListView listView = (ListView)v.findViewById(android.R.id.list);

        mTitle = (TextView)v.findViewById(R.id.selectionTitle);
        mTitle.setText(R.string.chapter_selection);

        return v;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //int resID = getActivity().getResources().getIdentifier("vocab_"+(position + 1), "raw",
        //        getActivity()
        //        .getPackageName());
        int chapter = position + 1;

        // Start a new CrimePagerActivity to show details of crime
        Intent i = new Intent(getActivity(), VocabularyQuizActivity.class);
        //i.putExtra(VocabularyQuizFragment.EXTRA_CRIME_ID, resID);
        i.putExtra(VocabularyQuizFragment.EXTRA_CHAPTER_ID, chapter);
        startActivity(i);
    }

    private class ChapterAdapter extends ArrayAdapter<String> {
        public ChapterAdapter(ArrayList<String> chapterNames) {
            super(getActivity(), 0, chapterNames);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_chapter, null);
            }
            // configure the view for this crime
            String c = getItem(position);

            TextView titleTextView = (TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
            titleTextView.setText("Chapter "+(position + 1));
            TextView dateTextView = (TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
            dateTextView.setText(c);

            return convertView;
        }
    }
}
