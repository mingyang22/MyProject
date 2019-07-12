package com.yangming.myproject.imageselector;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yangming.myproject.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import baselibrary.recycleview.CommonAdapter;
import baselibrary.recycleview.ViewHolder;

/**
 * 图片选择界面
 */
public class PictureGridviewActivity extends AppCompatActivity implements OnItemClickListener {

    private EditText edtDesc;
    private TextView txtHead, txtSubmit;
    private ImageView imgBig;
    RecyclerView recycleView;

    /**
     * 存储文件夹中的图片数量
     */
    private int mPicsSize;
    /**
     * 用户选择的图片文件夹
     */
    private File mChooseImgDir;
    /**
     * 当前文件夹中所有的图片
     */
    private List<String> mImgs;

    /**
     * 所有的图片
     */
    private List<String> allImgs = new ArrayList<String>();

    private MyAdapter mAdapter;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<String>();

    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();

    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public List<String> mSelectedImage = new LinkedList<String>();

    int totalCount = 0;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                // 为View绑定数据
                data2View();
                // 初始化展示文件夹的popupWindw
                // initListDirPopupWindw();
            } else if (msg.what == 1) {

            }
        }
    };


    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_gridview);
        verifyStoragePermissions(this);
        initView();
        getImages();
    }

    private void initView() {
        recycleView = (RecyclerView) findViewById(R.id.recycla_view);
        recycleView.setLayoutManager(new GridLayoutManager(this, 4));
//        recycleView.setAdapter(mAdapter);
    }

    // private void initEvent() {
    // /**
    // * 为底部的布局设置点击事件，弹出popupWindow
    // */
    // mChooseDir.setOnClickListener(new OnClickListener() {
    // @Override
    // public void onClick(View v) {
    // mListImageDirPopupWindow.setAnimationStyle(R.style.anim_popup_dir);
    // mListImageDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);
    //
    // // 设置背景颜色变暗
    // WindowManager.LayoutParams lp = getWindow().getAttributes();
    // lp.alpha = .3f;
    // getWindow().setAttributes(lp);
    // }
    // });
    // }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                String firstImage = null;

                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = PictureGridviewActivity.this.getContentResolver();

                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png", "image/jpg"}, MediaStore.Images.Media.DATE_MODIFIED);

                Log.e("TAG", mCursor.getCount() + "");
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    // 获得所有图片
                    allImgs.add(path);
                    Log.e("TAG", path);
                    // 拿到第一张图片的路径
                    if (firstImage == null)
                        firstImage = path;
                    // 获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();
                    ImageFloder imageFloder = null;
                    // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        // 初始化imageFloder
                        imageFloder = new ImageFloder();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                        imageFloder.setName(parentFile.getName());
                    }
                    if (parentFile.list() == null)
                        continue;
                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg"))
                                return true;
                            return false;
                        }
                    }).length;
                    totalCount += picSize;

                    imageFloder.setCount(picSize);
                    mImageFloders.add(imageFloder);

                    if (picSize > mPicsSize) {
                        mPicsSize = picSize;
                        // mImgDir = parentFile;
                    }
                }
                mCursor.close();

                ImageFloder imageFloder = new ImageFloder();
                imageFloder.setFirstImagePath(allImgs.size() == 0 ? "" : allImgs.get(0));
                imageFloder.setName("全部图片");
                imageFloder.setCount(allImgs.size());
                mImageFloders.add(0, imageFloder);

                // 扫描完成，辅助的HashSet也就可以释放内存了
                mDirPaths = null;

                // 通知Handler扫描图片完成
                mHandler.sendEmptyMessage(0);

            }
        }).start();

    }

    /**
     * 为View绑定数据
     */
    private void data2View() {
        if (allImgs == null || allImgs.size() == 0) {
            Toast.makeText(getApplicationContext(), "擦，一张图片没扫描到", Toast.LENGTH_SHORT).show();
            return;
        }
        mChooseImgDir = null;
        // mImgs = Arrays.asList(mImgDir.list());
        // mImgs = refreshFileList(mImgs);

        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
//        mAdapter = new MyAdapter(allImgs, null);

        CommonAdapter adapter = new CommonAdapter<String>(this, R.layout.gridview_item_picture, allImgs) {
            @Override
            protected void convert(ViewHolder holder, String data, int position) {
                final String realPath = data;
                holder.setImageUrl(R.id.id_item_image, data);
                holder.setOnClickListener(R.id.id_item_select, v -> {
                    // 已经选择过该图片
                    if (mSelectedImage.contains(realPath)) {
                        mSelectedImage.remove(realPath);
                        holder.setImageResource(R.id.id_item_select, R.mipmap.pictures_unselected);
                    } else {
                        // 未选择该图片
                        mSelectedImage.add(realPath);
                        holder.setImageResource(R.id.id_item_select, R.mipmap.pictures_selected);
                    }
                });
                if (mSelectedImage.contains(data)) {
                    holder.setImageResource(R.id.id_item_select, R.mipmap.pictures_selected);
                }
            }

        };

        recycleView.setAdapter(adapter);
        // mChooseDir.setText(mImgDir.getName() + "/");
        // mImageCount.setText(totalCount + "张");
    }

    ;

    /**
     * 获得指定格式文件
     *
     * @param list
     * @return
     */
    private List<String> refreshFileList(List<String> list) {
        List<String> mImgs = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            String path = list.get(i);
            if (path.contains(".png") || path.contains(".jpg") || path.contains(".jpeg")) {
                mImgs.add(path);
            }
        }
        return mImgs;

    }

    // /**
    // * 初始化展示文件夹的popupWindw
    // */
    // private void initListDirPopupWindw() {
    // mListImageDirPopupWindow = new
    // ListImageDirPopupWindow(LayoutParams.MATCH_PARENT, (int) (mScreenHeight *
    // 0.7),
    // mImageFloders,
    // LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_dir,
    // null));
    //
    // mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener() {
    //
    // @Override
    // public void onDismiss() {
    // // 设置背景颜色变暗
    // WindowManager.LayoutParams lp = getWindow().getAttributes();
    // lp.alpha = 1.0f;
    // getWindow().setAttributes(lp);
    // }
    // });
    // // 设置选择文件夹的回调
    // mListImageDirPopupWindow.setOnImageDirSelected(this);
    // }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 110:
                // 无选择
                finish();
                break;
            case 120:
                // 有选择
                int position = data.getIntExtra("mSelectedPos", 0);
                if (position == 0) {
                    data2View();

                } else {
                    selected(mImageFloders.get(position));
                }
                break;
            default:
                break;
        }
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        /**
         * 文件夹路径
         */
        private String mDirPath;
        private List<String> mDatas;

        public MyAdapter(List<String> mDatas, String dirPath) {
            this.mDirPath = dirPath;
            this.mDatas = mDatas;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    PictureGridviewActivity.this).inflate(R.layout.gridview_item_picture, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final String realPath;
            if (mDirPath == null) {
                realPath = mDatas.get(position);
            } else {
                realPath = mDirPath + "/" + mDatas.get(position);
            }
            Glide.with(PictureGridviewActivity.this).load(realPath).fitCenter().into(holder.id_item_image);
            holder.id_item_select.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 已经选择过该图片
                    if (mSelectedImage.contains(realPath)) {
                        mSelectedImage.remove(realPath);
                        holder.id_item_select.setImageResource(R.mipmap.pictures_unselected);
                    } else {
                        // 未选择该图片
                        mSelectedImage.add(realPath);
                        holder.id_item_select.setImageResource(R.mipmap.pictures_selected);
                    }
                }
            });

            /**
             * 已经选择过的图片，显示出选择过的效果
             */
            if (mSelectedImage.contains(mDirPath + "/" + mDatas.get(position))) {
                holder.id_item_select.setImageResource(R.mipmap.pictures_selected);
            }
        }

        @Override
        public int getItemCount() {
            return 0;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView id_item_image, id_item_select;

            public MyViewHolder(View view) {
                super(view);
                id_item_select = (ImageView) view.findViewById(R.id.id_item_select);
                id_item_image = (ImageView) view.findViewById(R.id.id_item_image);
            }
        }

    }

//	protected void refreshView() {
//		if (mSelectedImage.size() > 0) {
//			mConfirm.setTextColor(Color.parseColor("#1b9ee2"));
//			mConfirm.setText("确认(" + mSelectedImage.size() + ")");
//			mConfirm.setClickable(true);
//		} else {
//			mConfirm.setTextColor(Color.parseColor("#89888a"));
//			mConfirm.setText("确认");
//			mConfirm.setClickable(false);
//		}
//
//	}

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        String path = "";
//		Animation mAnimation = AnimationUtils.loadAnimation(this, R.anim.glide_zoom_in);
//		imgBig.setAnimation(mAnimation);
//		imgBig.setVisibility(View.VISIBLE);
        if (mChooseImgDir == null) {
            path = allImgs.get(arg2);
        } else {
            path = mChooseImgDir.getAbsolutePath() + "/" + mImgs.get(arg2);
        }
//		Glide.with(PictureGridviewActivity.this).load(path).animate(R.anim.glide_zoom_in).fitCenter().into(imgBig);
        // 预览大图
//		Intent intent = new Intent(PictureGridviewActivity.this, PicturePreviewActivity.class);
//		intent.putExtra("urlPath", path);
//		startActivity(intent);
    }

    public void selected(ImageFloder floder) {
        mChooseImgDir = new File(floder.getDir());
        mImgs = Arrays.asList(mChooseImgDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg"))
                    return true;
                return false;
            }
        }));
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        mAdapter = new MyAdapter(mImgs,
                mChooseImgDir.getAbsolutePath());
//		mGirdView.setAdapter(mAdapter);
//		mGirdView.setSelection(mImgs.size() - 1);
        txtHead.setText(floder.getName());

    }

}
