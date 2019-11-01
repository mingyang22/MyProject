package baselibrary.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import baselibrary.util.CameraUtil;
import baselibrary.util.DisplayUtils;
import baselibrary.util.SensorController;

/**
 * @author yangming on 2019/10/23
 * 拍照View
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "CameraView";

    private Activity mActivity;
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private CameraUtil cameraInstance;
    private SensorController mSensorController;
    private Handler mBackgroundHandler;
    private long lastTime = 0L;
    /**
     * 拍照id  1： 前摄像头  0：后摄像头
     */
    private int mCameraId = 0;
    /**
     * 屏幕宽高
     */
    private int screenWidth;
    private int screenHeight;

    private Handler mHandler = new Handler();

    private FocusCallback focusCallback;

    private FaceDetectorCallback faceDetectorCallback;

    public CameraView(Context context) {
        this(context, null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mHolder = getHolder();
        mHolder.addCallback(this);
        setFocusable(true);

        mActivity = (Activity) context;

        cameraInstance = CameraUtil.getInstance();
        screenWidth = DisplayUtils.getDisplayWidth(context);
        screenHeight = DisplayUtils.getDisplayHeight(context);
        mSensorController = SensorController.getInstance();

        mSensorController.setCameraFocusListener(() -> {
            Point point = new Point(screenWidth / 2, screenWidth / 2);
            onCameraFocus(point);
        });

    }

    public void onStart() {
        mSensorController.onStart();
    }

    public void onStop() {
        mSensorController.onStop();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mCamera == null) {
            mCamera = getCamera(mCameraId);
            try {
                setupCamera(mCamera);
                mCamera.setPreviewDisplay(mHolder);
                cameraInstance.setCameraDisplayOrientation(mActivity, mCameraId, mCamera);
                if (faceDetectorCallback != null) {
                    mCamera.setPreviewCallbackWithBuffer((data, camera) -> {
                        // 识别人脸
                        onFaceDetector(data, camera);
                        camera.addCallbackBuffer(data);
                    });
                    // print saved parameters
                    int previewWidth = mCamera.getParameters().getPreviewSize().width;
                    int previewHeight = mCamera.getParameters().getPreviewSize().height;
                    mCamera.addCallbackBuffer(new byte[((previewWidth * previewHeight) * ImageFormat.getBitsPerPixel(ImageFormat.NV21)) / 8]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
        holder.getSurface().release();
    }

    /**
     * 设置surfaceView的尺寸 因为camera默认是横屏，所以取得支持尺寸也都是横屏的尺寸
     * 我们在startPreview方法里面把它矫正了过来，但是这里我们设置设置surfaceView的尺寸的时候要注意
     * previewSize.height<previewSize.width
     * previewSize.width才是surfaceView的高度
     * 一般相机都是屏幕的宽度 这里设置为屏幕宽度 高度自适应 你也可以设置自己想要的大小
     */
    private void setupCamera(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
//        if (parameters.getSupportedFocusModes().contains(
//                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
//            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//        }
        //设置对焦模式
        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }

        //根据屏幕尺寸获取最佳 大小
        Camera.Size previewSize = cameraInstance.getPicPreviewSize(parameters.getSupportedPreviewSizes(),
                screenHeight, screenWidth);
        parameters.setPreviewSize(previewSize.width, previewSize.height);

        Camera.Size pictrueSize = cameraInstance.getPicPreviewSize(parameters.getSupportedPictureSizes(),
                previewSize.width, previewSize.height);
        parameters.setPictureSize(pictrueSize.width, pictrueSize.height);
        camera.setParameters(parameters);
//        picHeight = (screenWidth * pictrueSize.width) / pictrueSize.height;
        int picWidth = pictrueSize.width;
        int picHeight = pictrueSize.height;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(screenWidth,
                (screenWidth * picWidth) / picHeight);
        setLayoutParams(params);

        if (mCameraId == 1) {
            mSensorController.lockFocus();
        } else {
            mSensorController.unlockFocus();
        }

    }

    /**
     * 相机对焦
     *
     * @param point 坐标点
     * @return true 对焦中  false 锁定对焦
     */
    public boolean onCameraFocus(Point point) {
        if (!mSensorController.isFocusLocked()) {
            if (onFocus(point, autoFocusCallback)) {
                mSensorController.lockFocus();
                return true;
            }
        }
        return false;
    }

    /**
     * 手动聚焦
     *
     * @param point 触屏坐标
     */
    protected boolean onFocus(Point point, Camera.AutoFocusCallback callback) {
        if (mCamera == null) {
            return false;
        }

        Camera.Parameters parameters;
        try {
            parameters = mCamera.getParameters();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        // 不支持设置自定义聚焦，则使用自动聚焦，返回
        if (parameters.getMaxNumFocusAreas() <= 0) {
            return focus(callback);
        }

        Log.i(TAG, "onCameraFocus:" + point.x + "," + point.y);

        List<Camera.Area> areas = new ArrayList<>();
        int left = point.x - 300;
        int top = point.y - 300;
        int right = point.x + 300;
        int bottom = point.y + 300;
        left = left < -1000 ? -1000 : left;
        top = top < -1000 ? -1000 : top;
        right = right > 1000 ? 1000 : right;
        bottom = bottom > 1000 ? 1000 : bottom;
        areas.add(new Camera.Area(new Rect(left, top, right, bottom), 100));
        parameters.setFocusAreas(areas);
        try {
            mCamera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


        return focus(callback);
    }

    private boolean focus(Camera.AutoFocusCallback callback) {
        try {
            mCamera.autoFocus(callback);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void startPreview() {
        if (mCamera != null) {
            try {
                mSensorController.unlockFocus();
                mCamera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stopPreview() {
        if (mCamera != null) {
            try {
                mCamera.stopPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 开启闪光灯
     */
    public void setFlashModeTorch() {
        cameraInstance.turnLightOn(mCamera);
    }

    /**
     * 自动模式闪光灯
     */
    public void setFlashModeAuto() {
        cameraInstance.turnLightAuto(mCamera);
    }

    /**
     * 关闭闪光灯
     */
    public void setFlashModeOff() {
        cameraInstance.turnLightOff(mCamera);
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        if (mBackgroundHandler != null) {
            mBackgroundHandler.removeCallbacksAndMessages(null);
            mBackgroundHandler.getLooper().quitSafely();
            mBackgroundHandler = null;
        }
    }

    public void takePicture(PictureCallback callback) {
        mSensorController.lockFocus();
        mCamera.takePicture(null, null, (data, camera) -> {
            // 将data 转换为位图 或者你也可以直接保存为文件使用 FileOutputStream
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            // 将图片转正方向
            Bitmap saveBitmap = cameraInstance.setTakePictureOrientation(mCameraId, bitmap);
            saveBitmap = Bitmap.createScaledBitmap(saveBitmap, screenWidth, screenHeight, true);

            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            callback.onPictureTaken(data, saveBitmap);
        });
    }

    public void bindActivity(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * 获取Camera实例
     *
     * @return Camera
     */
    private Camera getCamera(int id) {
        Camera camera = null;
        try {
            camera = Camera.open(id);
        } catch (Exception e) {
            Log.e(TAG, "getCamera: " + e);
        }
        return camera;
    }

    /**
     * 聚焦监听
     */
    private final Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {

        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (focusCallback != null) {
                focusCallback.onFocus(success);
            }
            mHandler.postDelayed(() -> {
                // 一秒之后才能再次对焦
                mSensorController.unlockFocus();
            }, 1000);
        }
    };

    /**
     * 识别人脸
     *
     * @param data   预览帧数据
     * @param camera camera
     */
    private void onFaceDetector(byte[] data, Camera camera) {
        if (System.currentTimeMillis() - lastTime <= 200 || data == null || data.length == 0) {
            return;
        }
        Log.i(TAG, "onPreviewFrame " + data.length);
        getBackgroundHandler().post(new FaceThread(data, camera));
        lastTime = System.currentTimeMillis();
    }

    /**
     * 新建子线程并返回 Handler
     *
     * @return Handler
     */
    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    /**
     * 获取主线程 Handler
     *
     * @return Handler
     */
    private Handler getMainHandler() {
        return new Handler(Looper.getMainLooper());
    }

    private class FaceThread implements Runnable {
        private byte[] mData;
        private ByteArrayOutputStream mBitmapOutput;
        private Camera mCamera;

        FaceThread(byte[] data, Camera camera) {
            mData = data;
            mBitmapOutput = new ByteArrayOutputStream();
            mCamera = camera;
        }

        @Override
        public void run() {
            Bitmap bitmap = null;
            Bitmap roteBitmap = null;
            try {
                Camera.Parameters parameters = mCamera.getParameters();
                int width = parameters.getPreviewSize().width;
                int height = parameters.getPreviewSize().height;

                YuvImage yuv = new YuvImage(mData, parameters.getPreviewFormat(), width, height, null);
                mData = null;
                yuv.compressToJpeg(new Rect(0, 0, width, height), 100, mBitmapOutput);

                byte[] bytes = mBitmapOutput.toByteArray();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;//必须设置为565，否则无法检测
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

                mBitmapOutput.reset();
                roteBitmap = cameraInstance.setTakePictureOrientation(mCameraId, bitmap);
                List<Rect> rects = cameraInstance.detectionBitmap(roteBitmap, screenWidth, screenHeight);
                Log.e(TAG, "roteBitmap width：" + roteBitmap.getWidth() + " Height：" + roteBitmap.getHeight());
                if (null == rects || rects.size() == 0) {
                    Log.i(TAG, "没有检测到人脸哦");
                } else {
                    Log.i(TAG, "检测到有" + rects.size() + "人脸");
                    for (int i = 0; i < rects.size(); i++) {
                        //返回的rect就是在 SurfaceView 上面的人脸对应的实际坐标
                        int left = rects.get(i).left;
                        int top = rects.get(i).top;
                        int bottom = rects.get(i).bottom;
                        int right = rects.get(i).right;
                        Log.e(TAG, "rect : left：" + left + " top：" + top + "  right：" + right + "  bottom：" + bottom);
                        // 裁剪人脸图片
                        roteBitmap = Bitmap.createBitmap(roteBitmap, left, top, right - left, bottom - top);
                        Bitmap finalRoteBitmap = Bitmap.createBitmap(roteBitmap);
                        getMainHandler().post(() -> {
                            faceDetectorCallback.onFaceDetectorCallback(finalRoteBitmap);
                            mBackgroundHandler.removeCallbacksAndMessages(null);
                            stopPreview();
                        });
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bitmap != null) {
                    bitmap.recycle();
                }
                if (roteBitmap != null) {
                    roteBitmap.recycle();
                }

                if (mBitmapOutput != null) {
                    try {
                        mBitmapOutput.close();
                        mBitmapOutput = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public interface PictureCallback {
        /**
         * 返回拍照后图片
         *
         * @param data   图片原图
         * @param bitmap 修正后的图片
         */
        void onPictureTaken(byte[] data, Bitmap bitmap);
    }

    public interface FocusCallback {
        /**
         * 对焦监听
         *
         * @param success 对焦成功
         */
        void onFocus(boolean success);
    }

    public void setOnFocusCallback(FocusCallback callback) {
        this.focusCallback = callback;
    }

    public interface FaceDetectorCallback {
        /**
         * 人脸采集
         *
         * @param bitmap 采集后人脸图片
         */
        void onFaceDetectorCallback(Bitmap bitmap);
    }

    public void setOnFaceDetectorCallback(FaceDetectorCallback callback) {
        this.faceDetectorCallback = callback;
    }

}
