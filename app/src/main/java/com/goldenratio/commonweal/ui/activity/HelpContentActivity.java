package com.goldenratio.commonweal.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.goldenratio.commonweal.R;

/**
 * Created by Administrator on 2016/6/22.
 */

public class HelpContentActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_content_fragment);
        TextView tv_ceshi = (TextView) findViewById(R.id.textView2);
        Intent intent = getIntent();
        String s = intent.getStringExtra("title");
        Log.d("CN", "Activity+++++++++++++onCreate: ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"+s);
        tv_ceshi.setText(s);


    }
}
