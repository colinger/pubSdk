package com.colingo.webview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.app.R;

public class MainActivity extends Activity {

    private Button mStartScanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        mStartScanButton = (Button)findViewById(R.id.btn_start_scan);
        //
        mStartScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }

}
