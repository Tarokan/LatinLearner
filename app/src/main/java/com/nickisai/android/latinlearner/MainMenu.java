package com.nickisai.android.latinlearner;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.nickisai.android.latinlearner.ConjugationAndDeclension.DeclensionSelectionActivity;

public class MainMenu extends Activity {

    private Button mDeclConjButton;
    private Button mDictionaryButton;
    private Button mVocabularyButton;
    private Button mAboutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main_menu);


        mDeclConjButton = (Button)findViewById(R.id.button_declension);
        mDeclConjButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenu.this, DeclensionSelectionActivity.class);
                startActivity(i);
            }
        });

        mDictionaryButton = (Button)findViewById(R.id.button_conjugation);
        mDictionaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenu.this, DictionaryActivity.class);
                startActivity(i);
            }
        });

        mVocabularyButton = (Button)findViewById(R.id.button_vocabulary);
        mVocabularyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenu.this, VocabularySelectionActivity.class);
                startActivity(i);
            }
        });

        mAboutButton = (Button)findViewById(R.id.button_about);
        mAboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenu.this, AboutActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
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
}
