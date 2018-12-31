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


import com.nickisai.android.latinlearner.UIElements.LatinTextWatcher;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class VocabularyQuizFragment extends ListFragment {

    private EditText mGuessField;
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
            resourceLoader.populateWordsFromChapter(chapter);
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
        mGuessField.addTextChangedListener(new LatinTextWatcher(mGuessField) {
            @Override
            public void onTextChangedAfterConversion(String s) {
                if(mQM.isCorrect(s)) {
                    correctQuestion();
                    Log.i(TAG, "Guess was Correct");
                }
            }
        });

        if(mQM.isCompleted()) {
            switchQuestion();
        }

        mLinearLayoutStep1.setVisibility(View.VISIBLE);
        mLinearLayoutStep2.setVisibility(View.INVISIBLE);

        return v;
    }

    private void correctQuestion() {
        mFrameLayout.setBackgroundColor(0xFF2FFF3F);
        mFullSolution.setVisibility(View.VISIBLE);
        mButton.setEnabled(false);
        int i = 500 + 500 * mQM.getNumberOfKeywords();
        new CountDownTimer(i, 250) {
            public void onFinish() {
                mFrameLayout.setBackgroundColor(0xFFFFFC98);
                mFullSolution.setVisibility(View.INVISIBLE);
                switchQuestion();
                mButton.setEnabled(true);
            }

            public void onTick(long millisUntilFinished) { }
        }.start();
    }

    private void switchQuestion() {
        Log.i(TAG, "Quiz Manager completed?" + mQM.isCompleted());
        Log.i(TAG, "Last Question correct?" + mQM.isWasCorrect());
        if(!mQM.isCompleted()) {
            mQM.setNewQuestion();

            mQuizWord.setText(mQM.getLatinWord());
            mGuessField.setText("");

            String progress = getString(R.string.vocab_quiz_progress, mQM.getQuestionsAnswered(), mQM.getLength());
            mProgressText.setText(progress);

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
                mResultsComment.setText("Let's study some more.");
            }
        }
    }

    private void resetFragment() {
        mQM = new QuizManager(mQM.getMissedLatinWords(), mQM.getMissedDefinitions());

        mQuizWord.setText(mQM.getLatinWord());
        mGuessField.setText("");

        String progress = getString(R.string.vocab_quiz_progress, mQM.getQuestionsAnswered(), mQM.getLength());
        mProgressText.setText(progress);

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
