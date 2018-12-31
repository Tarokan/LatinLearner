package com.nickisai.android.latinlearner.ConjugationAndDeclension;

import android.app.AlertDialog;
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

import com.nickisai.android.latinlearner.R;
import com.nickisai.android.latinlearner.ResourceLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Fragment for Conjugation Quiz
 * Created by Nicholas on 8/10/2015.
 */
public class ConjugationQuizFragment extends Fragment {
    private ConjugationQuizManager mCQM;
    private LinearLayout mConjQuizBackground;
    private ArrayList<EditText> mTextFields = new ArrayList<EditText>();
    private TextView mConjTitle, mConjQuizTitle, mBaseTextView;
    private String title;
    private Button mGiveUpButton, mNextButton, mPreviousButton;
    private int quizNumber = 1;

    public static final String EXTRA_TITLE_STRING = "latinlearner.EXTRA_TITLE_STRING";
    private static final String TAG = "ConjQuizFrag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        title = (String)getActivity().getIntent().getStringExtra(EXTRA_TITLE_STRING);
        Scanner scanner = new Scanner(title);
        Log.e(TAG, title);
        scanner.nextInt();
        int selection = scanner.nextInt();
        ResourceLoader resourceLoader = new ResourceLoader(R.raw.conjugation_data, getActivity());

        try {
            resourceLoader.processConjugationData(selection);
            mCQM = new ConjugationQuizManager(resourceLoader.getDeclensionData(), resourceLoader
                    .getBases(), resourceLoader.getDeclensionTitle());
        } catch (IOException e) {
            Log.e(TAG, "did not load data");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Macrons are important! To insert a macron, use a capital letter!")
                .setPositiveButton("OK", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        Log.e(TAG, "onCreateView called:" + quizNumber);
        View v = inflater.inflate(R.layout.fragment_conjugation_quiz, container, false);

        mConjTitle = (TextView)v.findViewById(R.id.conjTitle);
        mConjTitle.setText(mCQM.getTitle(quizNumber));

        mConjQuizTitle = (TextView)v.findViewById(R.id.conjQuizTitle);
        mConjQuizTitle.setText(title.substring(6).replace("-v", ""));

        mBaseTextView = (TextView)v.findViewById(R.id.baseTextView);
        mBaseTextView.setText(mCQM.getBase(quizNumber));
        mConjQuizBackground = (LinearLayout)v.findViewById(R.id.conjQuizBackGround);
        if(mTextFields.size() == 0) {
            mTextFields.add((EditText) v.findViewById(R.id.Sg1st));
            mTextFields.add((EditText) v.findViewById(R.id.Sg2nd));
            mTextFields.add((EditText) v.findViewById(R.id.Sg3rd));
            mTextFields.add((EditText) v.findViewById(R.id.Pl1st));
            mTextFields.add((EditText) v.findViewById(R.id.Pl2nd));
            mTextFields.add((EditText) v.findViewById(R.id.Pl3rd));

            for(int i = 0; i < mTextFields.size(); i++) {
                final int finalI = i;
                setTextListener(finalI);
            }

        }
        mGiveUpButton = (Button)v.findViewById(R.id.giveUpButton);
        mGiveUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < mTextFields.size(); i++) {
                    if (mTextFields.get(i).isEnabled()) {
                        mTextFields.get(i).setEnabled(false);
                        mTextFields.get(i).setTextColor(0xffff4444);
                        mTextFields.get(i).setText(mCQM.getDeclension(i, quizNumber) + " ");


                    }
                }
                mGiveUpButton.setEnabled(false);
            }
        });

        mNextButton = (Button)v.findViewById(R.id.nextButton);
        if(mCQM.getNumberOfQuizzes() == 1) {
            mNextButton.setEnabled(false);

            mNextButton.setTextColor(0xFFCDC57D);
        }
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quizNumber < mCQM.getNumberOfQuizzes()) {
                    quizNumber++;
                    resetQuizStates();
                    if(quizNumber == mCQM.getNumberOfQuizzes()) {
                        mNextButton.setEnabled(false);

                        mNextButton.setTextColor(0xFFCDC57D);
                    }
                    mPreviousButton.setEnabled(true);

                    mPreviousButton.setTextColor(0xFF119B88);
                }
            }
        });

        mPreviousButton = (Button)v.findViewById(R.id.previousButton);
        if(quizNumber == 1) {
            mPreviousButton.setEnabled(false);

            mPreviousButton.setTextColor(0xFFCDC57D);
        }
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quizNumber > 1) {
                    quizNumber--;
                    resetQuizStates();
                    if (quizNumber == 1) {
                        mPreviousButton.setEnabled(false);

                        mPreviousButton.setTextColor(0xFFCDC57D);
                    }
                    mNextButton.setEnabled(true);

                    mNextButton.setTextColor(0xFF119B88);
                } else {
                    mPreviousButton.setEnabled(false);

                    mPreviousButton.setTextColor(0xFFCDC57D);
                }
            }
        });
        return v;
    }


    private void completed() {
        Log.e(TAG, "completed");
        mConjQuizBackground.setBackgroundColor(0xFF2FFF3F);
        mGiveUpButton.setEnabled(false);
    }

    private void setTextListener(final int finalI) {
        Log.e(TAG, "setTextListener called:"+quizNumber +" " +finalI);
        String declension = mCQM.getDeclension(finalI, quizNumber);
        if(declension.contains("-")) {
            if(declension.contains("-v")) {
                mTextFields.get(finalI).setText("varies for each word");
            } else if (declension.contains("-2")) {
                mTextFields.get(finalI).setText(declension.replace("-2", " "));
            } else {
                mTextFields.get(finalI).setText("n/a");
            }
            mTextFields.get(finalI).setEnabled(false);
            mCQM.setDeclensionSolved(finalI, quizNumber);
        } else {
            mTextFields.get(finalI).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().equals(mCQM.latinize(s.toString()))) {
                        mTextFields.get(finalI).setText(mCQM.latinize(s.toString()));

                        mTextFields.get(finalI).setSelection(mTextFields.get(finalI).getText()
                                .length());
                    }
                    if (mCQM.checkDeclension(finalI, s.toString(), quizNumber)) {
                        if (!s.toString().equals(mCQM.getDeclension(finalI, quizNumber))) {
                            mTextFields.get(finalI).setText(mCQM.getDeclension(finalI, quizNumber));
                        }
                        if (mCQM.checkAllSolved(quizNumber)) {
                            completed();
                        } else {
                            if (finalI < 5) {
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

    private void resetQuizStates() {
        mConjTitle.setText(mCQM.getTitle(quizNumber));
        mConjQuizBackground.setBackgroundColor(0xfffff69b);
        for(int i = 0; i < mTextFields.size(); i++) {
            String declension = mCQM.getDeclension(i, quizNumber);
            mTextFields.get(i).setText("");
            mTextFields.get(i).setTextColor(0xFF000000);
            mTextFields.get(i).setEnabled(true);
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
                mCQM.setDeclensionSolved(i, quizNumber);
            }
        }
        mBaseTextView.setText(mCQM.getBase(quizNumber));
        mGiveUpButton.setEnabled(true);
    }
}
