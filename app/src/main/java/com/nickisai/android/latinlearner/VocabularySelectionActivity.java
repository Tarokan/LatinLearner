package com.nickisai.android.latinlearner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;


public class VocabularySelectionActivity extends Activity {

    private static final String TAG = VocabularySelectionActivity.class.getCanonicalName();

    private ArrayList<String> chapterNames;
    private ListView chapterListView;
    private TextView titleTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_list);

        ResourceLoader chapterLoader = new ResourceLoader(R.raw.chapter_names, this);
        try {
            chapterNames = chapterLoader.readAsChapters();
            Log.e(TAG, "Loaded Successfully!");
        } catch (IOException e) {
            chapterNames = new ArrayList<>();
            Log.e(TAG, "Failed to load chapter names");
        }

        chapterListView = findViewById(R.id.listView);

        ChapterAdapter adapter = new ChapterAdapter(this, chapterNames);
        chapterListView.setAdapter(adapter);
        chapterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                int chapter = position + 1;

                // Start a new CrimePagerActivity to show details of crime
                Intent i = new Intent(VocabularySelectionActivity.this, VocabularyQuizActivity.class);
                i.putExtra(VocabularyQuizFragment.EXTRA_CHAPTER_ID, chapter);
                startActivity(i);
            }
        });

        titleTextView = findViewById(R.id.listTitle);
        titleTextView.setText(R.string.chapter_selection);
    }

    private class ChapterAdapter extends ArrayAdapter<String> {
        private ChapterAdapter(Context context, ArrayList<String> chapterNames) {
            super(context, 0, chapterNames);
        }

        @Override
        @NonNull
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_chapter, parent, false);
            }

            String c = getItem(position);

            TextView titleTextView = (TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
            String titleText = getString(R.string.chapter, (position + 1));
            titleTextView.setText(titleText);
            TextView dateTextView = (TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
            dateTextView.setText(c);

            return convertView;
        }
    }
}
