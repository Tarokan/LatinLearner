package com.nickisai.android.latinlearner;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

//        outState.putBoolean("cp", toEnglishRadio.isChecked());
import com.nickisai.android.latinlearner.UIElements.LatinTextWatcher;

import java.util.ArrayList;

public class DictionaryActivity extends Activity {

    private static final String TAG = DictionaryActivity.class.getCanonicalName();

    private EditText mSearchEditText;
    private RadioButton toLatinRadio, toEnglishRadio;
    private ArrayList<String> mAllTermsEnglish, mPossibleTermsEnglish, mAllTermsLatin, mPossibleTermsLatin;
    private ListView dictionaryListView;

    private String searchTerm = "";
    private TermsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        ResourceLoader resourceLoader = new ResourceLoader(R.raw.dict, this);

        //load dictionary files
        resourceLoader.populateAllWords();
        mAllTermsEnglish = resourceLoader.getEnglishWords();
        mAllTermsLatin = resourceLoader.getLatinWords();

        dictionaryListView = findViewById(R.id.dictionaryListView);

        mSearchEditText = findViewById(R.id.searchEditText);
        mSearchEditText.addTextChangedListener(new LatinTextWatcher(mSearchEditText) {
            @Override
            public void onTextChangedAfterConversion(String s) {
                searchTerm = s;
                getMatches(s);
            }
        });

        toLatinRadio = findViewById(R.id.toLatinRadio);
        toLatinRadio.setChecked(false);

        toEnglishRadio = findViewById(R.id.toEnglishRadio);
        toEnglishRadio.setChecked(true);

        toLatinRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLatinRadio.setChecked(true);
                toEnglishRadio.setChecked(false);
                getMatches(searchTerm);
            }
        });

        toEnglishRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toEnglishRadio.setChecked(true);
                toLatinRadio.setChecked(false);
                getMatches(searchTerm);
            }
        });

        if (savedInstanceState != null) {
            boolean a = savedInstanceState.getBoolean("cp");
            if (!a) {
                toEnglishRadio.setChecked(false);
                toLatinRadio.setChecked(true);
            }
        }

        getMatches("");

        Log.e(TAG, mAllTermsLatin.size() + " size");
    }


    //retrieves words with similar spelling
    public void getMatches(String givenTerm) {

        if (givenTerm.isEmpty()) {
            mPossibleTermsLatin = (ArrayList<String>) mAllTermsLatin.clone();
            mPossibleTermsEnglish = (ArrayList<String>) mAllTermsEnglish.clone();
        } else {
            mPossibleTermsLatin.clear();
            mPossibleTermsEnglish.clear();
            if (toLatinRadio.isChecked()) {
                //converting to english
                for (int i = 0; i < mAllTermsLatin.size(); i++) {
                    final String term = mAllTermsLatin.get(i);
                    if (term.contains(givenTerm)) {
                        if (term.charAt(0) == givenTerm.charAt(0)) {
                            mPossibleTermsLatin.add(0, term);
                            mPossibleTermsEnglish.add(0, mAllTermsEnglish.get(i));

                        } else {
                            mPossibleTermsLatin.add(term);
                            mPossibleTermsEnglish.add(mAllTermsEnglish.get(i));
                        }
                    }
                }
                Log.i(TAG, "" + mPossibleTermsEnglish.size());
                Log.i(TAG, "" + mPossibleTermsLatin.size());
            } else if (toEnglishRadio.isChecked()) {
                for (int i = 0; i < mAllTermsEnglish.size(); i++) {
                    String term = mAllTermsEnglish.get(i);
                    if (term.contains(givenTerm)) {
                        if (term.charAt(0) == givenTerm.charAt(0)) {
                            mPossibleTermsLatin.add(0, mAllTermsLatin.get(i));
                            mPossibleTermsEnglish.add(0, term);

                        } else {
                            mPossibleTermsLatin.add(mAllTermsLatin.get(i));
                            mPossibleTermsEnglish.add(term);
                        }
                    }
                }
            }
        }
        adapter = new TermsAdapter(this, mPossibleTermsEnglish, mPossibleTermsLatin);
        dictionaryListView.setAdapter(adapter);
    }

    private class TermsAdapter extends ArrayAdapter<String> {

        private ArrayList<String> mTermsEnglish, mTermsLatin;

        public TermsAdapter(Context context, ArrayList<String> givenTermsEnglish, ArrayList<String> givenTermsLatin) {
            super(context, 0, givenTermsLatin);
            mTermsEnglish = givenTermsEnglish;
            mTermsLatin = givenTermsLatin;
        }

        @Override
        @NonNull
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_dict, parent, false);
            }
            TextView mainTextView = convertView.findViewById(R.id.mainText);
            mainTextView.setText(toLatinRadio.isChecked() ? mTermsLatin.get(position) : mTermsEnglish.get(position));
            TextView subTextView = convertView.findViewById(R.id.subText);
            subTextView.setText(toLatinRadio.isChecked() ? mTermsEnglish.get(position) : mTermsLatin.get(position));

            return convertView;
        }

    }
}
