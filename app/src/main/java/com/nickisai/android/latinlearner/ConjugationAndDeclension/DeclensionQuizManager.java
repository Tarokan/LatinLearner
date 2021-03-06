package com.nickisai.android.latinlearner.ConjugationAndDeclension;

import android.util.Log;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Nicholas on 8/2/2015.
 */
public class DeclensionQuizManager {
    private ArrayList<String> declensions = new ArrayList<String>();
    private int[] corrects;
    private int quizNumbers;
    private static final String TAG = "DeclQuizMan";
    private ArrayList<String> titles = new ArrayList<String>();

    public DeclensionQuizManager (String rawDeclensionData, String rawDeclensionTitle) {
        Scanner scanner = new Scanner(rawDeclensionData);
        int i = 0;
        Log.e(TAG, rawDeclensionData);
        if(scanner.hasNextInt()) {
            quizNumbers = scanner.nextInt();
            Log.e(TAG, ""+quizNumbers);
        }
        while(scanner.hasNext()) {
            declensions.add(scanner.next());
            i++;
        }
        corrects = new int[i];

        Scanner scanner2 = new Scanner(rawDeclensionTitle).useDelimiter(";");
        while(scanner2.hasNext()) {
            titles.add(scanner2.next());
        }

    }

    public String getTitle(int currentQuiz) {
        if(titles.size() >= currentQuiz) {
            return titles.get(currentQuiz - 1);
        } else {
            return "";
        }
    }

    public String getDeclension(int caseNumber, int currentQuiz) {
        return declensions.get(caseNumber + (currentQuiz-1) * 10);
    }

    public String latinize(String givenGuess) {
        String guess = givenGuess;
        if(guess.contains("A")) {
            guess = guess.replace('A', 'ā');
        }
        if(guess.contains("E")) {
            guess = guess.replace('E', 'ē');
        }
        if(guess.contains("I")) {
            guess = guess.replace('I', 'ī');
        }
        if(guess.contains("O")) {
            guess = guess.replace('O', 'ō');
        }
        if(guess.contains("U")) {
            guess = guess.replace('U', 'ū');
        }
        return guess;
    }

    public boolean checkDeclension(int caseNumber, String guess, int currentQuiz) {
        String answer;
        //       if (base != null) {
        //            answer = base + declensions.get(caseNumber - 1);
        //       } else {
        answer = declensions.get(caseNumber + ((currentQuiz-1)*10));
        //       }
        if(guess.contains("A")) {
            guess = guess.replace('A', 'ā');
        }
        if(guess.contains("E")) {
            guess = guess.replace('E', 'ē');
        }
        if(guess.contains("I")) {
            guess = guess.replace('I', 'ī');
        }
        if(guess.contains("O")) {
            guess = guess.replace('O', 'ō');
        }
        if(guess.contains("U")) {
            guess = guess.replace('U', 'ū');
        }
        Log.e(TAG, "["+answer+"]");
        Log.e(TAG, "["+guess+"]");
        for(int i = 0; i<guess.length(); i++) {
            Log.e(TAG, "Character of guess at" + i + ":" + (int)guess.charAt(i));
            if(i < answer.length()) {
                Log.e(TAG, "Character of answer at" + i + ":" + (int) answer.charAt(i));
            }
        }

        boolean isCorrect = answer.contentEquals(guess);

        if(isCorrect == true) {
            corrects[caseNumber + (currentQuiz-1)*10] = 1;
            Log.e(TAG, "they were equal");
        } else {
            Log.e(TAG, "they are not equal");
        }
        return isCorrect;
    }

    public void setDeclensionSolved(int caseNumber, int currentQuiz) {

        corrects[caseNumber + (currentQuiz-1)*10] = 1;
    }

    public boolean checkAllSolved(int currentQuiz) {
        boolean isAllSolved = true;
        for(int i = (currentQuiz-1)*10; i < currentQuiz * 10; i++){
            if(corrects[i] == 0) {
                isAllSolved = false;
            }
            Log.e(TAG, "" + corrects[i]);
        }
        return isAllSolved;
    }

    public int getNumberOfQuizzes() {
        return quizNumbers;
    }

}
