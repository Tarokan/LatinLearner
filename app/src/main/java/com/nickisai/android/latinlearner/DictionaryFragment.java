package com.nickisai.android.latinlearner;

import android.app.ListFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nickisai.android.latinlearner.UIElements.LatinTextWatcher;

import java.util.ArrayList;

/**
 * Setups the fragment under DictionaryActivity.
 */
public class DictionaryFragment extends ListFragment {

    private static final String TAG = DictionaryFragment.class.getCanonicalName();

    private EditText mSearchEditText;
    private RadioButton mLatinRadio, mEnglishRadio;
    private ArrayList<String> mAllTermsEnglish, mPossibleTermsEnglish, mAllTermsLatin,
            mPossibleTermsLatin;

    private String searchTerm = "";
    private termsAdapter adapter;

    public DictionaryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ResourceLoader resourceLoader = new ResourceLoader(R.raw.dict, getActivity());

        //load dictionary files
        resourceLoader.populateAllWords();
        mAllTermsEnglish = resourceLoader.getEnglishWords();
        mAllTermsLatin = resourceLoader.getLatinWords();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dictionary, container, false);

        getMatches("");

        mSearchEditText = (EditText) v.findViewById(R.id.searchEditText);
        mSearchEditText.addTextChangedListener(new LatinTextWatcher(mSearchEditText) {
            @Override
            public void onTextChangedAfterConversion(String s) {
                getMatches(s);
            }
        });

        mLatinRadio = (RadioButton) v.findViewById(R.id.toLatinRadio);
        mLatinRadio.setChecked(false);

        mEnglishRadio = (RadioButton) v.findViewById(R.id.toEnglishRadio);
        mEnglishRadio.setChecked(true);

        mLatinRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLatinRadio.setChecked(true);
                mEnglishRadio.setChecked(false);
                if (!searchTerm.equals("")) {
                    getMatches(searchTerm);
                }
            }
        });

        mEnglishRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEnglishRadio.setChecked(true);
                mLatinRadio.setChecked(false);

                if (!searchTerm.equals("")) {
                    getMatches(searchTerm);
                }
            }
        });

        if (savedInstanceState != null) {
            boolean a = savedInstanceState.getBoolean("cp");
            if (!a) {
                mEnglishRadio.setChecked(false);
                mLatinRadio.setChecked(true);
            }
        }

        adapter = new termsAdapter(mPossibleTermsEnglish, mPossibleTermsLatin);
        setListAdapter(adapter);

        Log.e(TAG, mAllTermsLatin.size() + " size");
        return v;
    }

    //save the state when the fragment is interrupted
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("cp", mEnglishRadio.isChecked());
    }

    //retrieves words with similar spelling
    public void getMatches(String givenTerm) {

        if (givenTerm.isEmpty()) {
            mPossibleTermsLatin = (ArrayList<String>) mAllTermsLatin.clone();
            mPossibleTermsEnglish = (ArrayList<String>) mAllTermsEnglish.clone();
        } else {
            mPossibleTermsLatin.clear();
            mPossibleTermsEnglish.clear();
            if (mLatinRadio.isChecked()) {
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
            } else if (mEnglishRadio.isChecked()) {
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
        adapter = new termsAdapter(mPossibleTermsEnglish, mPossibleTermsLatin);
        setListAdapter(adapter);
    }

    //replaces the user-input capital letters with the corresponding macron letters
    private String latinize(String givenGuess) {
        String guess = givenGuess;
        if (guess.contains("A")) {
            guess = guess.replace('A', 'ā');
        }
        if (guess.contains("E")) {
            guess = guess.replace('E', 'ē');
        }
        if (guess.contains("I")) {
            guess = guess.replace('I', 'ī');
        }
        if (guess.contains("O")) {
            guess = guess.replace('O', 'ō');
        }
        if (guess.contains("U")) {
            guess = guess.replace('U', 'ū');
        }
        return guess;
    }

    private class termsAdapter extends ArrayAdapter<String> {

        private ArrayList<String> mTermsEnglish, mTermsLatin;

        public termsAdapter(ArrayList<String> givenTermsEnglish, ArrayList<String>
                givenTermsLatin) {
            super(getActivity(), 0, givenTermsLatin);
            mTermsEnglish = givenTermsEnglish;
            mTermsLatin = givenTermsLatin;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_dict, null);
            }

            TextView latinTextView = (TextView) convertView.findViewById(R.id.latinWord);
            latinTextView.setText(mTermsLatin.get(position));
            TextView engTextView = (TextView) convertView.findViewById(R.id.engWord);
            engTextView.setText(mTermsEnglish.get(position));


            return convertView;
        }

    }

}
