package com.ocwvar.muzi.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.ocwvar.muzi.R;


//商家回复信息dialog
public class MessageDialog extends Activity {


    TextView text_content ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_message);
        text_content = (TextView) findViewById(R.id.message_content);
        text_content.setText(getIntent().getStringExtra("message"));
    }
}
