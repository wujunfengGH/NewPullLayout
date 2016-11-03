package com.example.vincent.pulllayoutdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private NewPullLayout mNewPullLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNewPullLayout = (NewPullLayout) findViewById(R.id.newpulllayout);
        mNewPullLayout.setPageChangeListener(new NewPullLayout.PageChangeListener() {
            @Override
            public void toPreviousPage() {
                Toast.makeText(MainActivity.this,"toPreviousPage 上一页",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void toNextPage() {
                Toast.makeText(MainActivity.this,"toNextPage 下一页",Toast.LENGTH_SHORT).show();
            }
        });
        View headerView = View.inflate(MainActivity.this, R.layout.header_view, null);
        mNewPullLayout.setHeaderView(headerView);
    }
}
