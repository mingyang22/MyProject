package com.yangming.myproject.imageloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileDescriptor;

/**
 * @author yangming on 2019/6/3
 * 图片压缩功能
 */
public class ImageResizer {
    public static final String TAG = "ImageResizer";

    public ImageResizer() {
    }

    /**
     * 加载本地Resource图片
     *
     * @param res       Resources
     * @param resId     图片 ID
     * @param reqWidth  加载宽度
     * @param reqHeight 加载高度
     * @return 压缩后图片
     */
    public Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // 首先设置 inJustDecodeBounds = true 来解析图片原始宽高
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);

    }

    /**
     * 通过文件描述符加载图片
     *
     * @param fd        文件描述符
     * @param reqWidth  加载宽度
     * @param reqHeight 加载宽度
     * @return 压缩后图片
     */
    public Bitmap decodeSampledBitmapFromFileDescriptor(FileDescriptor fd, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }


    /**
     * 计算采样率
     */
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 图片原始宽高
        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleSize = 1;

        if (reqWidth == 0 || reqHeight == 0) {
            return 1;
        }

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.d(TAG, "sampleSize: " + inSampleSize);
        return inSampleSize;
    }


}
