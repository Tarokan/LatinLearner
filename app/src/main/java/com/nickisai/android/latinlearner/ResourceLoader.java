package com.nickisai.android.latinlearner;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * When given text files, will read stuff
 * Created by Nicholas on 7/24/2015.
 */
public class ResourceLoader {

    private static final String TAG = ResourceLoader.class.getCanonicalName();

    private int resourceID;
    private Context mContext;
    private String declensionData;
    private String declensionTitle;
    private ArrayList<String> mLatinWords;
    private ArrayList<String> mEnglishWords;

    public ArrayList<String> getLatinWords() {
        return mLatinWords;
    }

    public ArrayList<String> getEnglishWords() {
        return mEnglishWords;
    }

    public String getDeclensionData() {
        return declensionData;
    }

    public String getDeclensionTitle() {
        return declensionTitle;
    }

    public ResourceLoader(int resID, Context c) {
        resourceID = resID;
        mContext = c;
    }

    public void populateAllWords() {
        mLatinWords = new ArrayList<>();
        mEnglishWords = new ArrayList<>();

        try {
            InputStream in = mContext.getResources().openRawResource(R.raw.dict);
            Log.e(TAG, "opened resource");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                // format is given as:
                // "vultus, vultūs, m.";"noun";"countenance, face"

                String[] elements = currentLine.split(";\"");
                if(elements.length == 3) {
                    mEnglishWords.add(elements[0].replace("\"", ""));
                    mLatinWords.add(elements[2].replace("\"", ""));
                } else {
                    Log.e(TAG, "failed to parse line in dictionary: " + elements.length + currentLine);
                }
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public ArrayList<String> readAsChapters() throws IOException {
        ArrayList<String> chapterNames = new ArrayList<String>();
        BufferedReader reader = null;

        try {
            InputStream in = mContext.getResources().openRawResource(resourceID);
            reader = new BufferedReader(new InputStreamReader(in));

            String line = null;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                chapterNames.add(line);
            }
        } catch (Exception ignored) {

        } finally {
            if(reader != null) {
                reader.close();
            }
        }
        return chapterNames;
    }

    // should be used with vocab_comprehensive.txt
    public void populateWordsFromChapter(int chapter) throws IOException {
        mLatinWords = new ArrayList<>();
        mEnglishWords = new ArrayList<>();
        BufferedReader reader = null;

        try {
            InputStream in = mContext.getResources().openRawResource(resourceID);
            reader = new BufferedReader(new InputStreamReader(in));
            Log.e(TAG, "Opened dictionary");

            // Skip a bunch of lines based on the chapter number, since the file is sorted by chapter.
            int lineOffset = (chapter - 1) * 19;
            for(int counter = 0; counter < lineOffset; counter++) {
                reader.readLine();
            }

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                // format of line is:
                // 2;"sed";"conjunction";"but"
                // <chapter>;"[word]";"[partOfSpeech]";"[definition]"
                String[] elements = currentLine.split(";\"");
                try {
                    int chapterOfWord = Integer.parseInt(elements[0]);
                    if(chapterOfWord == chapter) {

                        if(elements.length == 4) {
                            Log.i(TAG, "added " + currentLine + " with tagged " + chapterOfWord);
                            mEnglishWords.add(elements[1].replace("\"", ""));
                            mLatinWords.add(elements[3].replace("\"", ""));
                        } else {
                            Log.e(TAG, "failed to parse line in dictionary: " + currentLine);
                            Log.e(TAG, "number of elements: " + elements.length);
                        }
                    } else if (chapterOfWord > chapter) {
                        break;
                    }
                } catch (NumberFormatException e) {
                    Log.e(TAG, e.toString());
                } catch (IndexOutOfBoundsException e) {
                    Log.e(TAG, e.toString());
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if(reader != null) {
                reader.close();
            }
        }
        Log.d(TAG, "" + mLatinWords.size());
        Log.d(TAG, "" + mEnglishWords.size());
    }

    public void processDeclensionData(int selection) throws IOException {
        BufferedReader reader = null;
        Log.e(TAG, ""+selection);
        try {
            InputStream in = mContext.getResources().openRawResource(resourceID);
            reader = new BufferedReader(new InputStreamReader(in));
            //reader.readLine();
            for(int i = 0; i < (selection-1) * 2 + 1; i++) {
                reader.readLine();
            }
            declensionTitle = reader.readLine();
            declensionData = reader.readLine();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if(reader != null) {
                reader.close();
            }
        }
    }

    String mBases;

    public void processConjugationData(int selection) throws IOException {
        BufferedReader reader = null;
        Log.e(TAG, ""+selection);
        try {
            InputStream in = mContext.getResources().openRawResource(resourceID);
            reader = new BufferedReader(new InputStreamReader(in));
            //reader.readLine();
            for(int i = 0; i < (selection-1) * 3 + 1; i++) {
                reader.readLine();
            }
            declensionTitle = reader.readLine();
            mBases = reader.readLine();
            declensionData = reader.readLine();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if(reader != null) {
                reader.close();
            }
        }
    }

    public String getBases() {
        return mBases;
    }


}
