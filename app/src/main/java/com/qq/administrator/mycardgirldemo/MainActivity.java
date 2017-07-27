package com.qq.administrator.mycardgirldemo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fashare.stack_layout.StackLayout;
import com.fashare.stack_layout.transformer.AngleTransformer;
import com.fashare.stack_layout.transformer.StackPageTransformer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    static List<Integer> sRandomColors = new ArrayList<>();
    static{
        for(int i=0; i<100; i++)
            sRandomColors.add(new Random().nextInt() | 0xff000000);
    }

    StackLayout mStackLayout;
    Adapter mAdapter;
    private Toolbar toolbar;
    List<String> mData;
    List<BeautifulGirl> mBeautifulGirlData;
    private int page = 1;
    int curPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        loadData(0);
    }

    private void initView() {
        new GetData().execute("http://gank.io/api/data/福利/3/1");
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mStackLayout = (StackLayout) findViewById(R.id.stack_layout);
        mStackLayout.setAdapter(mAdapter = new Adapter(mData = new ArrayList<>()));
        mStackLayout.addPageTransformer(
                new StackPageTransformer(),     // 堆叠
                new MyAlphaTransformer(),       // 渐变
                new AngleTransformer()          // 角度
        );

        mStackLayout.setOnSwipeListener(new StackLayout.OnSwipeListener() {
            @Override
            public void onSwiped(View swipedView, int swipedItemPos, boolean isSwipeLeft, int itemLeft) {
                //toast((isSwipeLeft? "往左": "往右") + "移除" + mData.get(swipedItemPos) + "." + "剩余" + itemLeft + "项");
                if (isSwipeLeft){
                    ToastUtil.showToastNoLove(MainActivity.this,"不喜欢");
                }else {
                    ToastUtil.showToastLove(MainActivity.this,"我喜欢");
                }
                // 少于5条, 加载更多
                if(itemLeft < 5){
                    loadData(++ curPage);
                }
            }
        });
    }
    /**
     * 异步加载图片
     * */
    private class  GetData extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... strings) {
            return MyOkHttp.get(strings[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!TextUtils.isEmpty(result)){
                JSONObject jsonObject;
                Gson gson = new Gson();
                String jsonData = null;
                try {
                    jsonObject = new JSONObject(result);
                    jsonData = jsonObject.getString("results");
                }catch (JSONException e){
                    e.printStackTrace();
                }
                if (mBeautifulGirlData == null || mBeautifulGirlData.size() == 0){
                    mBeautifulGirlData = gson.fromJson(jsonData,new TypeToken<List<BeautifulGirl>>() {}.getType());
                    BeautifulGirl pages = new BeautifulGirl();
                    pages.setPage(page);
                    mBeautifulGirlData.add(pages);
                }else {
                    List<BeautifulGirl> more = gson.fromJson(jsonData,new TypeToken<List<BeautifulGirl>>() {}.getType());
                    mBeautifulGirlData.clear();
                    mBeautifulGirlData.addAll(more);
                    BeautifulGirl pages = new BeautifulGirl();
                    pages.setPage(page);
                    mBeautifulGirlData.add(pages);
                }
            }
        }
    }

    private void loadData(final int page) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.getData().addAll(Arrays.asList(String.valueOf(page*3), String.valueOf(page*3+1), String.valueOf(page*3+2)));
                new GetData().execute("http://gank.io/api/data/福利/" + page*3+4 + "/1");
                mAdapter.notifyDataSetChanged();
            }
        }, 1000);
    }

    class Adapter extends StackLayout.Adapter<Adapter.ViewHolder>{
        List<String> mData;

        public void setData(List<String> data) {
            mData = data;
        }

        public List<String> getData() {
            return mData;
        }

        public Adapter(List<String> data) {
            mData = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.mTextView.setText(mData.get(position));
            holder.itemView.findViewById(R.id.layout_content).setBackgroundColor(sRandomColors.get(position%sRandomColors.size()));
            if (mBeautifulGirlData != null && mBeautifulGirlData.size() > 0){
                if (mBeautifulGirlData.get(position).getUrl() == null){
                    holder.cardImg.setImageResource(R.mipmap.streetball);
                }else {
                    Picasso.with(getApplicationContext()).load(mBeautifulGirlData.get(position).getUrl()).into(holder.cardImg);
                }
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                    intent.putExtra("url",mBeautifulGirlData.get(position).getUrl());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class ViewHolder extends StackLayout.ViewHolder{
            TextView mTextView;
            ImageView cardImg;
            public ViewHolder(View itemView) {
                super(itemView);
                mTextView = (TextView) itemView.findViewById(R.id.tv);
                cardImg = (ImageView)itemView.findViewById(R.id.card_img);
            }
        }

    }
}
