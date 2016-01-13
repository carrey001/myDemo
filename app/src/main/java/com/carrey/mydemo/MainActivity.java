package com.carrey.mydemo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.carrey.common.util.SystemUtil;
import com.carrey.common.util.UIUtil;
import com.carrey.mydemo.guide.GuideListAct;
import com.carrey.mydemo.httptest.HttpTestActivity;
import com.carrey.mydemo.photoview.PhotoAct;
import com.carrey.mydemo.ruler.RulerAct;
import com.carrey.mydemo.zxing.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseAct {

    private ListView listView;
    private List<HomeItem> data = new ArrayList<>();

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    /**
     * 初始化View
     */
    @Override
    protected void initViews() {
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list_view);
        initListData();
        listView.setAdapter(new MyAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(MainActivity.this, data.get(position).clazz));
            }
        });
    }

    private void initListData() {
        data.add(new HomeItem("引导页", GuideListAct.class));
        data.add(new HomeItem("二维码", CaptureActivity.class));
        data.add(new HomeItem("尺子", RulerAct.class));
        data.add(new HomeItem("图片", PhotoAct.class));
        data.add(new HomeItem("http", HttpTestActivity.class));
        data.add(new HomeItem("图片选择", PicPick.class));


    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }


        @Override
        public Object getItem(int position) {
            return data.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new TextView(MainActivity.this);
                AbsListView.LayoutParams params = new AbsListView.LayoutParams(SystemUtil.getScreenWidth(),
                        UIUtil.dip2px(30));
                convertView.setLayoutParams(params);
                convertView.setPadding(10,10,10,10);
                ((TextView)convertView).setTextColor(Color.parseColor("#000000"));
            }
            ((TextView) convertView).setText(data.get(position).name);
            return convertView;
        }
    }

}
