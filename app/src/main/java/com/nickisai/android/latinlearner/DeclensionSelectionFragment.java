package com.nickisai.android.latinlearner;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
public class DeclensionSelectionFragment extends ListFragment {
    private ArrayList<String> mDeclensions;
    private TextView mTitle;
    private static final String TAG = "DecSelFrag";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ResourceLoader chapterLoader = new ResourceLoader(R.raw.declension_names_new, getActivity
                ());
        try {
            mDeclensions = chapterLoader.readAsChapters();
            Log.e(TAG, "Loaded Successfully!");
        } catch (IOException e) {
            mDeclensions = new ArrayList<String>();
            Log.e(TAG, "Failed to load declension names");
        }

        DeclensionAdapter adapter = new DeclensionAdapter(mDeclensions);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_item_empty, parent, false);

        ListView listView = (ListView)v.findViewById(android.R.id.list);

        mTitle = (TextView)v.findViewById(R.id.selectionTitle);
        mTitle.setText(R.string.declension_selection);

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
        int declension = position + 1;
        //i.putExtra(DeclensionQuizFragment.EXTRA_DECLENSION, declension);
        Intent i;
        if(mDeclensions.get(position).contains("-v")) {
            Log.e(TAG, "launched Conjugation");
            i = new Intent(getActivity(), ConjugationQuizActivity.class);
            i.putExtra(ConjugationQuizFragment.EXTRA_TITLE_STRING, mDeclensions.get(position));
        } else {
            Log.e(TAG, "launched Declension");
            i = new Intent(getActivity(), DeclensionQuizActivity.class);
            i.putExtra(DeclensionQuizFragment.EXTRA_TITLE_STRING, mDeclensions.get(position));
        }
        startActivity(i);
    }

    private class DeclensionAdapter extends ArrayAdapter<String> {
        public DeclensionAdapter(ArrayList<String> declensionNames) {
            super(getActivity(), 0, declensionNames);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_declension, null);
            }
            // configure the view for this crime
            String c = getItem(position);

            TextView titleTextView = (TextView)convertView.findViewById(R.id.declension);
            titleTextView.setText(c.substring(6).replace("-v",""));

            TextView chapterTextView = (TextView)convertView.findViewById(R.id.chapter_number);
            chapterTextView.setText("Chapter "+c.substring(0, 2));

            return convertView;
        }
    }
}
