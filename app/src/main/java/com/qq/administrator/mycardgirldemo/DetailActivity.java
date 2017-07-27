package com.qq.administrator.mycardgirldemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 崔琦 on 2017/7/26 0026.
 * Describe : .....
 */
public class DetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private String mUrl;
    private CircleImageView mCircleImageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
    }

    private void initView(){
        mUrl = getIntent().getStringExtra("url");
        mCircleImageView = (CircleImageView)findViewById(R.id.img_me);
        Picasso.with(DetailActivity.this).load(mUrl).into(mCircleImageView);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
