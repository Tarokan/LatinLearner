package com.nickisai.android.latinlearner.ConjugationAndDeclension;

import android.util.Log;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Nicholas on 8/2/2015.
 */
public class ConjugationQuizManager {
    private ArrayList<String> declensions = new ArrayList<>();
    private int[] corrects;
    private int quizNumbers;
    private static final String TAG = "ConjQuizMan";
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> bases = new ArrayList<>();

    public ConjugationQuizManager(String rawConjugationData, String rawBases, String
            rawConjugationTitle) {
        Scanner scanner = new Scanner(rawConjugationData);
        int i = 0;
        Log.e(TAG, rawConjugationData);
        if(scanner.hasNextInt()) {
            quizNumbers = scanner.nextInt();
            Log.e(TAG, ""+quizNumbers);
        }
        while(scanner.hasNext()) {
            declensions.add(scanner.next());
            i++;
        }
        corrects = new int[i];
        Log.e(TAG,rawConjugationTitle);

        Scanner scanner2 = new Scanner(rawConjugationTitle).useDelimiter(";");
        while(scanner2.hasNext()) {
            titles.add(scanner2.next());
        }

        Scanner scanner3 = new Scanner(rawBases).useDelimiter(";");
        while(scanner3.hasNext()) {
            bases.add(scanner3.next());
        }

    }

    public String getTitle(int currentQuiz) {
        if(titles.size() >= currentQuiz) {
            return titles.get(currentQuiz - 1);
        } else {
            return "";
        }
    }

    public String getBase(int currentQuiz) {
       return bases.get(currentQuiz-1);
    }

    public String getDeclension(int caseNumber, int currentQuiz) {
        String answer = declensions.get(caseNumber + (currentQuiz-1) * 6);
        if (answer.contains("!")) {
            answer = answer.replace('!', ' ');
        }
        return answer;
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
        answer = declensions.get(caseNumber + ((currentQuiz-1)*6));
        guess = latinize(guess);
        if(guess.contains(" ")) {
            guess = guess.replace(' ','!');
        }
        Log.e(TAG, "["+answer+"]");
        Log.e(TAG, "["+guess+"]");
        for(int i = 0; i<guess.length(); i++) {
            //Log.e(TAG, "Character of guess at" + i + ":" + (int)guess.charAt(i));
            if(i < answer.length()) {
                //Log.e(TAG, "Character of answer at" + i + ":" + (int) answer.charAt(i));
            }
        }

        boolean isCorrect = answer.contentEquals(guess);

        if(isCorrect == true) {
            corrects[caseNumber + (currentQuiz-1)*6] = 1;
            Log.e(TAG, "they were equal");
        } else {
            Log.e(TAG, "they are not equal");
        }
        return isCorrect;
    }

    public void setDeclensionSolved(int caseNumber, int currentQuiz) {

        corrects[caseNumber + (currentQuiz-1)*6] = 1;
    }

    public boolean checkAllSolved(int currentQuiz) {
        boolean isAllSolved = true;
        for(int i = (currentQuiz-1)*6; i < currentQuiz * 6; i++){
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
