package com.nickisai.android.latinlearner;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * When given text files, will read stuff
 * Created by Nicholas on 7/24/2015.
 */
public class ResourceLoader {
    int mResID;
    Context mContext;

    public ArrayList<String> getLatinWords() {
        return mLatinWords;
    }

    public ArrayList<String> getEnglishWords() {
        return mEnglishWords;
    }

    ArrayList<String> mLatinWords;
    ArrayList<String> mEnglishWords;

    public String getDeclensionData() {
        return declensionData;
    }

    public String getDeclensionTitle() {
        return declensionTitle;
    }

    String declensionData;
    String declensionTitle;

    private static final String TAG = "resourceloader";

    public ResourceLoader(int resID, Context c) {
        mResID = resID;
        mContext = c;
    }

    public void processDataforDictionary() throws IOException {
        ArrayList<String> latinWords = new ArrayList<String>();
        ArrayList<String> englishWords = new ArrayList<String>();
        Scanner scanner = null;
        BufferedReader reader = null;

        try {
            InputStream in = mContext.getResources().openRawResource(R.raw.dict);
            Log.e(TAG, "opened resource");
            reader = new BufferedReader(new InputStreamReader(in));
            String line, latinword, engword;
            while ((line = reader.readLine()) != null) {
                scanner = new Scanner(line).useDelimiter("\"");
                Log.e(TAG, "line" + line);
                //scanner.next();
                latinword = scanner.next();
                latinWords.add(latinword);
                scanner.next();
                scanner.next();
                scanner.next();
                engword = scanner.next();
                englishWords.add(engword);

            }

        } catch (Exception e) {
            Log.e(TAG, e.toString()+"!");
        } finally {
            if(scanner != null) {
                scanner.close();
            }
        }
        mLatinWords = latinWords;
        mEnglishWords = englishWords;
    }

    public ArrayList<String> readAsChapters() throws IOException {
        ArrayList<String> chapterNames = new ArrayList<String>();
        BufferedReader reader = null;

        try {
            InputStream in = mContext.getResources().openRawResource(mResID);
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


    public void processData() throws IOException {
        ArrayList<String> latinWords = new ArrayList<String>();
        ArrayList<String> englishWords = new ArrayList<String>();
        Scanner scanner = null;

        try {
            InputStream in = mContext.getResources().openRawResource(mResID);

            String line = null;
            scanner = new Scanner(in).useDelimiter("\"");
            while (scanner.hasNextLine() == true) {
                scanner.next();
                line = scanner.next();
                latinWords.add(line);
                scanner.next();
                scanner.next();
                scanner.next();
                line = scanner.next();
                englishWords.add(line);
                scanner.nextLine();
            }
            Log.e(TAG, "Opened Resource");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if(scanner != null) {
                scanner.close();
            }
        }
        mLatinWords = latinWords;
        mEnglishWords = englishWords;
    }
    public void processData(int chapter) throws IOException {
        ArrayList<String> latinWords = new ArrayList<String>();
        ArrayList<String> englishWords = new ArrayList<String>();
        BufferedReader reader = null;
        String chapterName = chapter+"";

        try {
            InputStream in = mContext.getResources().openRawResource(mResID);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            Log.e(TAG, "Opened Resource");
            int basis = (chapter -1) * 19;
            int counter = 0;
            int a = 1;
            while (counter++ < basis) {
                reader.readLine();
            }
            while ((line = reader.readLine()) != null) {
                Log.e(TAG, line);
                Scanner checker = new Scanner(line).useDelimiter(";");
                if(checker.hasNextInt()) {
                    a = (new Scanner(line).useDelimiter(";")).nextInt();
                }
                if(a == chapter) {
                    Log.e(TAG, a+"");
                    Log.e(TAG, "added" + line + "with tagged " + chapterName);
                    Scanner lineParser = new Scanner(line).useDelimiter("\"");
                    lineParser.next();
                    String latinWord = lineParser.next();
                    latinWords.add(latinWord);
                    lineParser.next();
                    lineParser.next();
                    lineParser.next();
                    String englishWord = lineParser.next();
                    englishWords.add(englishWord);

                } else if (a > chapter) {
                    break;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if(reader != null) {
                reader.close();
            }
        }
        mLatinWords = latinWords;
        mEnglishWords = englishWords;
    }

    public void processDeclensionData(int selection) throws IOException {
        BufferedReader reader = null;
        Log.e(TAG, ""+selection);
        try {
            InputStream in = mContext.getResources().openRawResource(mResID);
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
            InputStream in = mContext.getResources().openRawResource(mResID);
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
