package com.nickisai.android.latinlearner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * A placeholder fragment containing a simple view.
 */
public class DeclensionQuizFragment extends DialogFragment {
    private EditText mSgNom, mPlNom, mSgGen, mPlGen, mSgDat, mPlDat, mSgAcc, mPlAcc, mSgAbl, mPlAbl;
    private ArrayList<EditText> mTextFields = new ArrayList<EditText>();
    private TextView mBaseTextView, mDeclQuizTitle, mDeclTitle;
    private DeclensionQuizManager mDQM;
    private LinearLayout mDeclQuizBackground;
    private Button mGiveUpButton, mNextButton, mPreviousButton;
    private String title;
    private int quizNumber = 1;

    public static final String EXTRA_DECLENSION = "latinlearner.DECLENSION";
    public static final String EXTRA_TITLE_STRING = "latinlearner.DECLENSION_TITLE";
    private static final String TAG = "DeclQuizFrag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        title = (String)getActivity().getIntent().getStringExtra(EXTRA_TITLE_STRING);
        //int selection = (int)getActivity().getIntent().getSerializableExtra(EXTRA_DECLENSION);
        Scanner scanner = new Scanner(title);
        scanner.nextInt();
        int selection = scanner.nextInt();
        ResourceLoader resourceLoader = new ResourceLoader(R.raw.declension_data, getActivity());

        try {
            resourceLoader.processDeclensionData(selection);
            mDQM = new DeclensionQuizManager(resourceLoader.getDeclensionData(), resourceLoader
                    .getDeclensionTitle());
        } catch (IOException e) {
            Log.e(TAG, "did not load data");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_declension_quiz, container, false);

        mDeclQuizBackground = (LinearLayout)v.findViewById(R.id.declQuizBackGround);

        mDeclTitle = (TextView)v.findViewById(R.id.declTitle);
        mDeclTitle.setText(mDQM.getTitle(quizNumber));

        mDeclQuizTitle = (TextView)v.findViewById(R.id.declQuizTitle);
        mDeclQuizTitle.setText(title.substring(6));

        mBaseTextView = (TextView)v.findViewById(R.id.baseTextView);
        setBaseTextViewText();

        mSgNom = (EditText)v.findViewById(R.id.sgNom);
        mSgGen = (EditText)v.findViewById(R.id.sgGen);
        mSgDat = (EditText)v.findViewById(R.id.sgDat);
        mSgAcc = (EditText)v.findViewById(R.id.sgAcc);
        mSgAbl = (EditText)v.findViewById(R.id.sgAbl);
        mPlNom = (EditText)v.findViewById(R.id.plNom);
        mPlGen = (EditText)v.findViewById(R.id.plGen);
        mPlDat = (EditText)v.findViewById(R.id.plDat);
        mPlAcc = (EditText)v.findViewById(R.id.plAcc);
        mPlAbl = (EditText)v.findViewById(R.id.plAbl);
        mTextFields.add(mSgNom);
        mTextFields.add(mSgGen);
        mTextFields.add(mSgDat);
        mTextFields.add(mSgAcc);
        mTextFields.add(mSgAbl);
        mTextFields.add(mPlNom);
        mTextFields.add(mPlGen);
        mTextFields.add(mPlDat);
        mTextFields.add(mPlAcc);
        mTextFields.add(mPlAbl);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Macrons are important! To insert a macron, use a capital letter!")
                .setPositiveButton("OK", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        for(int i = 0; i < mTextFields.size(); i++) {
            final int finalI = i;
            setTextListener(finalI);
        }

        mGiveUpButton = (Button)v.findViewById(R.id.giveUpButton);
        mGiveUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < mTextFields.size(); i++) {
                    if(mTextFields.get(i).isEnabled()) {
                        mTextFields.get(i).setEnabled(false);
                        mTextFields.get(i).setTextColor(0xffff4444);
                        mTextFields.get(i).setText(mDQM.getDeclension(i, quizNumber) +" ");


                    }
                }
                mGiveUpButton.setEnabled(false);

            }
        });

        mNextButton = (Button)v.findViewById(R.id.nextButton);
        if(mDQM.getNumberOfQuizzes() == 1) {
            mNextButton.setEnabled(false);
        }
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quizNumber < mDQM.getNumberOfQuizzes()) {
                    quizNumber++;
                    resetQuizStates();
                    if(quizNumber == mDQM.getNumberOfQuizzes()) {
                        mNextButton.setEnabled(false);
                    }
                    mPreviousButton.setEnabled(true);
                }
            }
        });

        mPreviousButton = (Button)v.findViewById(R.id.previousButton);
        if(quizNumber == 1) {
            mPreviousButton.setEnabled(false);
        }
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quizNumber > 1) {
                    quizNumber--;
                    resetQuizStates();
                    if(quizNumber == 1) {
                        mPreviousButton.setEnabled(false);
                    }
                    mNextButton.setEnabled(true);
                } else {
                    mPreviousButton.setEnabled(false);
                }
            }
        });

        return v;
    }

    private void completed() {
        Log.e(TAG, "completed");
        mDeclQuizBackground.setBackgroundColor(0xFF2FFF3F);
        mGiveUpButton.setEnabled(false);
    }

    private void resetQuizStates() {
        mDeclTitle.setText(mDQM.getTitle(quizNumber));
        mDeclQuizBackground.setBackgroundColor(0xfffff69b);
        for(int i = 0; i < mTextFields.size(); i++) {
            mTextFields.get(i).setText("");
            String declension = mDQM.getDeclension(i, quizNumber);
            mTextFields.get(i).setEnabled(true);
            mTextFields.get(i).setTextColor(0xFF000000);
            if(declension.contains("-")) {
                if(declension.contains("-v")) {
                    mTextFields.get(i).setText("varies for each word");
                } else if (declension.contains("-2")) {
                    mTextFields.get(i).setText(declension.replace("-2", " "));
                } else {
                    mTextFields.get(i).setText("n/a");
                }
                mTextFields.get(i).setEnabled(false);
                mTextFields.get(i).setTextColor(0xFFCDC57D);
                mDQM.setDeclensionSolved(i, quizNumber);
            }
            //setTextListener(i);
        }
        setBaseTextViewText();
        mGiveUpButton.setEnabled(true);
    }

    private void setTextListener(final int finalI) {
        String declension = mDQM.getDeclension(finalI, quizNumber);
        if(declension.contains("-")) {
            if(declension.contains("-v")) {
                mTextFields.get(finalI).setText("varies for each word");
            } else if (declension.contains("-2")) {
                mTextFields.get(finalI).setText(declension.replace("-2", " "));
            } else {
                mTextFields.get(finalI).setText("n/a");
            }
            mTextFields.get(finalI).setEnabled(false);
            mDQM.setDeclensionSolved(finalI, quizNumber);
        } else {
            mTextFields.get(finalI).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().equals(mDQM.latinize(s.toString()))) {
                        mTextFields.get(finalI).setText(mDQM.latinize(s.toString()));

                        mTextFields.get(finalI).setSelection(mTextFields.get(finalI).getText()
                                .length());
                    }
                    if (mDQM.checkDeclension(finalI, s.toString(), quizNumber)) {
                        if (!s.toString().equals(mDQM.getDeclension(finalI, quizNumber))) {
                            mTextFields.get(finalI).setText(mDQM.getDeclension(finalI, quizNumber));
                        }
                        if (mDQM.checkAllSolved(quizNumber)) {
                            completed();
                        } else {
                            if (finalI < 9) {
                                mTextFields.get(finalI + 1).requestFocus();
                            } else {
                                mTextFields.get(0).requestFocus();
                            }

                        }
                        mTextFields.get(finalI).setEnabled(false);
                        mTextFields.get(finalI).setTextColor(0xFFCDC57D);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private String parseDeclension(String declension) {



        return null;
    }

    private void setBaseTextViewText() {

        mBaseTextView.setText(mDQM.getDeclension(0, quizNumber).replace("-v", " ").replace("-n",
                "n/a").trim() + ", " + mDQM.getDeclension(1, quizNumber));
        Log.e(TAG, title.substring(0,2));
        if(title.substring(0,2).equals("39")) {
            mBaseTextView.setText("laudō, laudāre, laudāvī, laudātum");
        }
    }

    }


