package com.example.vincent.pulllayoutdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private NewPullLayout mNewPullLayout;
    private Button mButton;
    private boolean isShow;
    private TextView mTitle;
    private Button mJump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNewPullLayout = (NewPullLayout) findViewById(R.id.newpulllayout);
        mButton = (Button) findViewById(R.id.button);
        mJump = (Button) findViewById(R.id.jump);
        mTitle = (TextView) findViewById(R.id.title);
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
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow){
                    mTitle.setVisibility(View.VISIBLE);
                    isShow = !isShow;
                }else {
                    mTitle.setVisibility(View.GONE);
                    isShow = !isShow;
                }
            }
        });
        mJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WebViewActivity.class));
            }
        });
    }
}
