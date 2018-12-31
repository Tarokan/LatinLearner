package com.nickisai.android.latinlearner.UIElements;

public class UIUtils {

    public static String convertToMacrons(String string) {
        if (string.contains("A")) {
            string = string.replace('A', 'ā');
        }
        if (string.contains("E")) {
            string = string.replace('E', 'ē');
        }
        if (string.contains("I")) {
            string = string.replace('I', 'ī');
        }
        if (string.contains("O")) {
            string = string.replace('O', 'ō');
        }
        if (string.contains("U")) {
            string = string.replace('U', 'ū');
        }
        return string;
    }

}
