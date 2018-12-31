package com.nickisai.android.latinlearner;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Nicholas on 7/24/2015.
 */
public class QuizManager {
    public boolean isWasCorrect() {
        return wasCorrect;
    }

    boolean wasCorrect;
    ArrayList<String> mLatinData;
    ArrayList<String> mEnglishData;
    int mCurrentQuestionID;
    int mCorrectlyAnswered;
    private enum questionStates {
        UNUSED, CORRECT, PASSED
    }
    int mQuestionsAnswered;
    questionStates[] used;
    Random mRandom;
    ArrayList<String> missedLatinWords = new ArrayList<String>();
    ArrayList<String> missedDefinitions = new ArrayList<String>();
    ArrayList<String> keywords = new ArrayList<String>();
    private static final String TAG = QuizManager.class.getCanonicalName();

    public int getQuestionsAnswered() {
        return mQuestionsAnswered;
    }

    public QuizManager(ArrayList<String> latinData, ArrayList<String>
            englishData) {
        Log.d(TAG, "" + latinData.size());
        mLatinData = latinData;
        mEnglishData = englishData;
        mRandom = new Random(System.currentTimeMillis());
        used = new questionStates[latinData.size()];
        for(int i = 0; i < used.length; i++) {
            used[i] = questionStates.UNUSED;
        }
    }

    public String getLatinWord() {
        return mLatinData.get(mCurrentQuestionID);
    }

    public String getEnglishTranslation() {
        return mEnglishData.get(mCurrentQuestionID);
    }

    public int getLength() {
        return mLatinData.size();
    }

    public int getNumberOfKeywords() {
        return keywords.size();
    }

    public boolean isCorrect(String guess) {
        Scanner scanner = new Scanner(mEnglishData.get(mCurrentQuestionID));
        keywords = new ArrayList<String>();
        String word = null;
        if ((mEnglishData.get(mCurrentQuestionID).equals("to be"))) {
            keywords.add("be");
        } else{
            while (scanner.hasNext() == true) {
                if ((word = scanner.next()).compareTo("to") != 0 && word.compareTo("of") != 0 &&
                        word.compareTo("be") != 0) {
                    word = word.replace(',', ' ');
                    word = word.replace(';', ' ');
                    word = word.replace('?', ' ');
                    word = word.trim();
                    keywords.add(word);
                }
            }
        }

        boolean mContainsDefinition = false;
        for(int i = 0; i < keywords.size(); i++) {
            if(guess.contains(keywords.get(i)) || guess.toLowerCase().contains(keywords.get(i).toLowerCase()
            )) {
                mContainsDefinition = true;
            }
        }
        if(mContainsDefinition == true) {
            mCorrectlyAnswered++;
            used[mCurrentQuestionID] = questionStates.CORRECT;
            wasCorrect = true;
        }

        return mContainsDefinition;
    }

    public void setCurrentQuestionPassed() {
        used[mCurrentQuestionID] = questionStates.PASSED;
        Log.e(TAG, ""+mCurrentQuestionID);
        missedLatinWords.add(getLatinWord());
        missedDefinitions.add(getEnglishTranslation());
    }

    public void setNewQuestion() {
        while(used[mCurrentQuestionID] == questionStates.CORRECT || used[mCurrentQuestionID] ==
                questionStates.PASSED) {
            mCurrentQuestionID = mRandom.nextInt(mLatinData.size());
        }
        wasCorrect = false;
        mQuestionsAnswered++;
    }

    public boolean isCompleted() {
        boolean isCompleted = true;
        for(int i = 0; i < used.length; i++) {
            if(used[i] == questionStates.UNUSED) {
                isCompleted = false;
            }
        }
        if(isCompleted) {
            Log.e(TAG, "they were all used???");
        }
        return isCompleted;
    }

    public ArrayList<String> getMissedLatinWords() {
        return missedLatinWords;
    }

    public ArrayList<String> getMissedDefinitions() {
        return missedDefinitions;
    }

    public int getCorrectlyAnswered() {
        return mCorrectlyAnswered;
    }
}
