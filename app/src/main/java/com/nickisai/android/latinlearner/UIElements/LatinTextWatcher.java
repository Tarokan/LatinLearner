package com.nickisai.android.latinlearner.UIElements;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public abstract class LatinTextWatcher implements TextWatcher {

    private EditText editText;

    public LatinTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String s = charSequence.toString();
        if (!s.equals(UIUtils.convertToMacrons(s))) {
            s = UIUtils.convertToMacrons(s);
            int curPos = editText.getSelectionStart();
            editText.setText(s);
            editText.setSelection(curPos);
        } else {
            onTextChangedAfterConversion(s);
        }
    }

    public void onTextChangedAfterConversion(String s) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
