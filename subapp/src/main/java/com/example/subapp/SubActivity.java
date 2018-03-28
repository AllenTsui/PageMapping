package com.example.subapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.annotation.PageName;
import com.example.pagerecord.PageRecorder;

/**
 * Created by xiaoyu on 2018/3/28 0028.
 */
@PageName("subPage")
public class SubActivity extends Activity{
    private TextView pageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        pageName = (TextView) findViewById(R.id.pageName);
        String pageNameStr = PageRecorder.getPageName(getClass());
        pageName.setText(pageNameStr);
    }
}
