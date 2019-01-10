package com.nickisai.android.latinlearner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nickisai.android.latinlearner.ConjugationAndDeclension.ConjugationAndDeclensionSelectionActivity;

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
                launchIntoMenuOption(MainMenu.this, ConjugationAndDeclensionSelectionActivity.class);
            }
        });

        mDictionaryButton = (Button)findViewById(R.id.button_conjugation);
        mDictionaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchIntoMenuOption(MainMenu.this, DictionaryActivity.class);
            }
        });

        mVocabularyButton = (Button)findViewById(R.id.button_vocabulary);
        mVocabularyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                launchIntoMenuOption(MainMenu.this, VocabularySelectionActivity.class);
            }
        });

        mAboutButton = (Button)findViewById(R.id.button_about);
        mAboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchIntoMenuOption(MainMenu.this, AboutActivity.class);
            }
        });

    }

    private void launchIntoMenuOption(Context context, Class targetClass) {
        Intent intent = new Intent(context, targetClass);
        startActivity(intent);
    }
}
