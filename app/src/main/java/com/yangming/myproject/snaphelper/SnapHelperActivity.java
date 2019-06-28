package com.yangming.myproject.snaphelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yangming.myproject.R;

import java.util.ArrayList;
import java.util.List;

import baselibrary.recycleview.CommonAdapter;
import baselibrary.recycleview.ViewHolder;

public class SnapHelperActivity extends AppCompatActivity {

    private RecyclerView recycleView;
    private List<String> mList = new ArrayList<String>();
    private CommonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_helper);
        initData();
        initView();
    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            mList.add("http://img.qqzhi.com/upload/img_5_1730069524D1438852309_23.jpg");
        }
    }

    private void initView() {
        recycleView = (RecyclerView) findViewById(R.id.recycla_view);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recycleView.setLayoutManager(linearLayoutManager);
        //添加分割线
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        recycleView.addItemDecoration(dividerItemDecoration);

//        LinearSnapHelper mLinearSnapHelper = new LinearSnapHelper();
//        mLinearSnapHelper.attachToRecyclerView(recycleView);

        MySnapHelper mySnapHelper = new MySnapHelper(3);
        mySnapHelper.attachToRecyclerView(recycleView);

        adapter = new CommonAdapter<String>(this, R.layout.item_recycler, mList) {

            @Override
            public void convert(ViewHolder holder, String s, int position) {
                holder.setImageUrl(R.id.img, s);
            }
        };
        recycleView.setAdapter(adapter);
    }

    public void OnButtonClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                adapter.addData(1, "http://img.qqzhi.com/upload/img_0_2546391613D4281219345_23.jpg");
                break;
            case R.id.btn_delete:
                adapter.removeData(1);
                break;
            case R.id.btn_change:
                recycleView.setAdapter(adapter);
                break;
            default:
                break;
        }
    }

}
