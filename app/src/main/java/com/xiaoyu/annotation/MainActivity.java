package com.xiaoyu.annotation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.annotation.PageName;
import com.example.pagerecord.PageRecorder;
import com.example.subapp.SubActivity;

@PageName("Home")
public class MainActivity extends Activity {
    private TextView pageName;
    private Button jump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pageName = (TextView) findViewById(R.id.pageName);
        jump = (Button) findViewById(R.id.jump);

        String pageNameStr = PageRecorder.getPageName(getClass());
        pageName.setText(pageNameStr);

        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }
}
