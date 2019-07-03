package com.yangming.myproject.imageloader;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.bean.Permissions;
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener;
import com.yangming.myproject.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yang on 2019/6/5
 */
public class PhotoWallActivity extends AppCompatActivity {

    @BindView(R.id.gridView)
    GridView gridView;

    private List<String> urlList = new ArrayList<>();
    private boolean mIsGridViewIdle = true;
    private ImageAdapter adapter;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_wall);
        ButterKnife.bind(this);

        SoulPermission.getInstance().checkAndRequestPermissions(
                Permissions.build(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                new CheckRequestPermissionsListener() {
                    @Override
                    public void onAllPermissionOk(Permission[] allPermissions) {
                    }

                    @Override
                    public void onPermissionDenied(Permission[] refusedPermissions) {
                    }
                });

        initData();
        initView();

    }

    private void initData() {
        for (int i = 0; i < 5; i++) {

            urlList.add("https://ss0.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1730069524,1438852309&fm=23&gp=0.jpg");
        }
        for (int i = 0; i < 5; i++) {
            urlList.add("http://img1.2345.com/duoteimg/qqTxImg/11/2014052014012633444.jpg");
        }

    }

    private void initView() {
        imageLoader = ImageLoader.build(this);
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    mIsGridViewIdle = true;
                    adapter.notifyDataSetChanged();
                } else {
                    mIsGridViewIdle = false;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        adapter = new ImageAdapter();
        gridView.setAdapter(adapter);
    }

    private class ImageAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return urlList.size();
        }

        @Override
        public Object getItem(int position) {
            return urlList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(PhotoWallActivity.this).inflate(R.layout.item_photo_wall, parent, false);
                holder = new ViewHolder();
                holder.imageView = convertView.findViewById(R.id.image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ImageView imageView = holder.imageView;
            final String tag = (String) imageView.getTag();
            final String url = urlList.get(position);
            if (!url.equals(tag)) {
                imageView.setImageResource(R.mipmap.ic_launcher);
            }
            if (mIsGridViewIdle) {
                imageView.setTag(url);
                imageLoader.bindBitmap(url, imageView, 100, 100);
            }

            return convertView;
        }

        public class ViewHolder {
            ImageView imageView;
        }

    }

}
