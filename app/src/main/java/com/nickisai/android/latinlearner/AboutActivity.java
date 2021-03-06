package com.nickisai.android.latinlearner;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class AboutActivity extends Activity {
    private final String message =
            "<center><b> Latin Learner™</b><br>v1.1.0<br>Assistant to Wheelock's Latin<br>© 2015 " +
                    "Nicholas Leung</center><br>";

    private final String message2 = "Feedback is greatly appreciated! Shoot me an email at " +
            "leung5326@gmail.com<br>" +
                    "<br><b>Acknowledgements</b><br>" +
                    "Wheelock's™ is a trademark of Martha Wheelock and Deborah Wheelock Taylor. " +
                    "Copyright © 2011 by Frederic M. Wheelock, Martha Wheelock, and Deborah " +
                    "Wheelock Taylor. Wheelock's Latin may be purchased from HarperCollins. " +
                    "<br><br>" +
                    "All pictures taken from Wikimedia Commons in accordance with their " +
                    "licenses.<br>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView tv = (TextView)findViewById(R.id.aboutTitleTextView);
        tv.setText(Html.fromHtml(message));

        TextView tv2 = (TextView)findViewById(R.id.otherTextView);
        tv2.setText(Html.fromHtml(message2));

    }




}
