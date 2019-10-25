package baselibrary.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

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
    }

    public void takePicture(PictureCallback callback) {
        mSensorController.lockFocus();
        mCamera.takePicture(null, null, (data, camera) -> {
            // 将data 转换为位图 或者你也可以直接保存为文件使用 FileOutputStream
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            // 将图片转正方向
            Bitmap saveBitmap = cameraInstance.setTakePictureOrientation(mActivity, mCameraId, bitmap);
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

}
