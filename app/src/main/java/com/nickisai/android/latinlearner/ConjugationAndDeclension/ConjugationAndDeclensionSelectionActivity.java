package com.nickisai.android.latinlearner.ConjugationAndDeclension;

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

import com.nickisai.android.latinlearner.R;
import com.nickisai.android.latinlearner.ResourceLoader;

import java.io.IOException;
import java.util.ArrayList;


public class ConjugationAndDeclensionSelectionActivity extends Activity {

    private static final String TAG = ConjugationAndDeclensionSelectionActivity.class.getCanonicalName();

    private ListView chapterListView;
    private TextView titleTextView;
    private ArrayList<String> mDeclensions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_list);

        ResourceLoader chapterLoader = new ResourceLoader(R.raw.declension_names_new, this);
        try {
            mDeclensions = chapterLoader.readAsChapters();
            Log.e(TAG, "Loaded Successfully!");
        } catch (IOException e) {
            mDeclensions = new ArrayList<>();
            Log.e(TAG, "Failed to load declension names");
        }

        DeclensionAdapter adapter = new DeclensionAdapter(this, mDeclensions);

        chapterListView = (ListView) findViewById(R.id.listView);

        chapterListView.setAdapter(adapter);
        chapterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent i;
                if(mDeclensions.get(position).contains("-v")) {
                    Log.e(TAG, "launched Conjugation");
                    i = new Intent(ConjugationAndDeclensionSelectionActivity.this, ConjugationQuizActivity.class);
                    i.putExtra(ConjugationQuizFragment.EXTRA_TITLE_STRING, mDeclensions.get(position));
                } else {
                    Log.e(TAG, "launched Declension");
                    i = new Intent(ConjugationAndDeclensionSelectionActivity.this, DeclensionQuizActivity.class);
                    i.putExtra(DeclensionQuizFragment.EXTRA_TITLE_STRING, mDeclensions.get(position));
                }
                startActivity(i);
            }
        });

        titleTextView = (TextView)findViewById(R.id.listTitle);
        titleTextView.setText(R.string.declension_selection);
    }

    private class DeclensionAdapter extends ArrayAdapter<String> {
        private DeclensionAdapter(Context context, ArrayList<String> declensionNames) {
            super(context, 0, declensionNames);
        }

        @Override
        @NonNull
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_declension, parent, false);
            }
            String c = getItem(position);

            TextView titleTextView = (TextView)convertView.findViewById(R.id.declension);
            titleTextView.setText(c.substring(6).replace("-v",""));

            TextView chapterTextView = (TextView)convertView.findViewById(R.id.chapter_number);
            chapterTextView.setText(getString(R.string.chapter, Integer.parseInt(c.substring(0, 2).trim())));

            return convertView;
        }
    }
}
