package com.nickisai.android.latinlearner;

import android.app.ListFragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.io.IOException;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class VocabularyQuizFragment extends ListFragment {

    private EditText mGuessField;
    private String userGuess;
    private TextView mQuizWord;
    private TextView mFullSolution;
    private TextView mProgressText;
    private TextView mResultsComment;
    private FrameLayout mFrameLayout;
    private LinearLayout mLinearLayoutStep1;
    private LinearLayout mLinearLayoutStep2;
    private Button mButton;
    private Button mReturnButton;
    private Button mStudyAgainButton;
    private TextView mScoreText;

    private QuizManager mQM;

    private static final String TAG = "VocabQuizFrag";
    public static final String EXTRA_CHAPTER_ID = "latinlearner.CRIME_ID";

    public static final String EXTRA_CRIME_CONEXT = "latinlearner.CRIME_CONTEXT";
    public VocabularyQuizFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        int chapter = (int)getActivity().getIntent().getSerializableExtra(EXTRA_CHAPTER_ID);
        ResourceLoader resourceLoader = new ResourceLoader(R.raw.vocab_comprehensive, getActivity());

        mQM = null;
        try {
            resourceLoader.processData(chapter);
            Log.e(TAG, "Loaded latin terms..?");

            mQM = new QuizManager(resourceLoader.getLatinWords(), resourceLoader
                    .getEnglishWords());
        } catch (IOException e) {
            Log.d(TAG, "Failed to load latin terms");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_vocabulary_quiz, container, false);
        v.setSoundEffectsEnabled(true);

        mQuizWord = (TextView)v.findViewById(R.id.QuizWord);
        mQuizWord.setText(mQM.getLatinWord());

        mScoreText = (TextView)v.findViewById(R.id.scoreText);

        mFrameLayout = (FrameLayout)v.findViewById(R.id.quizBackground);
        mLinearLayoutStep1 = (LinearLayout)v.findViewById(R.id.vocab_quiz_step1);
        mLinearLayoutStep2 = (LinearLayout)v.findViewById(R.id.vocab_quiz_step2);

        mFullSolution = (TextView)v.findViewById(R.id.fullSolution);
        mFullSolution.setText(mQM.getEnglishTranslation());

        mReturnButton = (Button)v.findViewById(R.id.returnButton);
        mReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
                v.playSoundEffect(android.view.SoundEffectConstants.CLICK);
            }
        });

        mProgressText = (TextView)v.findViewById(R.id.ProgressText);
        mProgressText.setText((mQM.getQuestionsAnswered() + 1) + " of " + mQM.getLength());

        mResultsComment = (TextView)v.findViewById(R.id.resultTextView);

        mStudyAgainButton = (Button)v.findViewById(R.id.studyAgainButton);
        mStudyAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFragment();
                v.playSoundEffect(android.view.SoundEffectConstants.CLICK);
            }
        });

        mButton = (Button)v.findViewById(R.id.passButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQM.setCurrentQuestionPassed();
                switchQuestion();
                v.playSoundEffect(android.view.SoundEffectConstants.CLICK);
            }

        });

        mGuessField = (EditText)v.findViewById(R.id.userGuess);
        mGuessField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userGuess = s.toString();
                Log.e(TAG, "USer typed");
                if(mQM.isWasCorrect() == false) {
                    Log.e(TAG, "Checking...");
                    if(mQM.isCorrect(userGuess)) {
                        correctQuestion();
                        Log.e(TAG, "Guess was Correct");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if(mQM.isCompleted() == true) {
            switchQuestion();
        }

        return v;
    }



    private void correctQuestion() {
        mFrameLayout.setBackgroundColor(0xFF2FFF3F);
        mFullSolution.setVisibility(View.VISIBLE);
        mButton.setEnabled(false);
        int i = 250 + 500 * mQM.getNumberOfKeywords();
        new CountDownTimer(i, 250) {
            public void onFinish() {
                mFrameLayout.setBackgroundColor(0xFFFFFC98);
                mFullSolution.setVisibility(View.INVISIBLE);
                switchQuestion();
                mButton.setEnabled(true);
                if(!mQM.isCompleted()) {
                    //Log.e(TAG, "mQM.isWasCorrect() is set to false");
                    //mQM.isWasCorrect() = false;
                }
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();
    }

    private void switchQuestion() {
        Log.e(TAG, "Quiz Manager completed?" + mQM.isCompleted());
        Log.e(TAG, "Last Question correct?" + mQM.isWasCorrect());
        if(mQM.isCompleted() == false) {
            mQM.setNewQuestion();
            mGuessField.clearComposingText();

            mQuizWord.setText(mQM.getLatinWord());
            mGuessField.setText("");
            mProgressText.setText("" + (mQM.getQuestionsAnswered() + 1) + " of " + mQM.getLength());
            mFullSolution.setText(mQM.getEnglishTranslation());
        } else {
            mGuessField.clearComposingText();
            mLinearLayoutStep1.setVisibility(View.INVISIBLE);
            mLinearLayoutStep2.setVisibility(View.VISIBLE);

            mistakesAdapter adapter = new mistakesAdapter(mQM.getMissedLatinWords(), mQM.getMissedDefinitions());
            setListAdapter(adapter);
            mScoreText.setText("Score: " + mQM.getCorrectlyAnswered() + "/" + mQM.getLength());

            float correctlyAnswered = (float)mQM.getCorrectlyAnswered() / mQM.getLength();
            if (correctlyAnswered == 1) {
                mStudyAgainButton.setVisibility(View.INVISIBLE);
                mResultsComment.setText("Wow! You got all of the questions right!");
            } else if(correctlyAnswered > 0.7) {
                mResultsComment.setText("Nice Job!  Here's a few words you missed:");
            } else if(correctlyAnswered > 0) {
                mResultsComment.setText("Ouch.  Let's see what you missed:");
            } else {
                mResultsComment.setText("Were you even trying?");
            }
        }
    }

    private void resetFragment() {
        mQM = new QuizManager(mQM.getMissedLatinWords(), mQM.getMissedDefinitions());
        mQuizWord.setText(mQM.getLatinWord());
        mGuessField.setText("");
        mProgressText.setText("" + (mQM.getQuestionsAnswered() + 1) + " of " + mQM.getLength());
        mFullSolution.setText(mQM.getEnglishTranslation());

        mLinearLayoutStep1.setVisibility(View.VISIBLE);
        mLinearLayoutStep2.setVisibility(View.INVISIBLE);
    }

    private class mistakesAdapter extends ArrayAdapter<String> {
        private ArrayList<String> mWrongWords;
        private ArrayList<String> mDefinitions;
        public mistakesAdapter(ArrayList<String> wrongWords, ArrayList<String> defitions) {
            super(getActivity(), 0, wrongWords);
            mWrongWords = wrongWords;
            mDefinitions = defitions;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_vocab, null);
            }

            TextView titleTextView = (TextView)convertView.findViewById(R.id.vocab_word);
            String string = "<b>"+mWrongWords.get(position).trim() + "</b> - "+mDefinitions
                    .get(position).trim();
            titleTextView.setText(Html.fromHtml(string));

            return convertView;
        }
    }
}
