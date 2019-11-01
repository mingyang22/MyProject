package baselibrary.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.util.Log;
import android.view.Surface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author yangming on 2019/5/24
 * 拍照工具类
 */
public class CameraUtil {
    private static final String TAG = "CameraUtil";
    /**
     * 降序
     */
    private CameraDropSizeComparator dropSizeComparator = new CameraDropSizeComparator();
    /**
     * 升序
     */
    private CameraAscendSizeComparator ascendSizeComparator = new CameraAscendSizeComparator();
    private CameraMinSizeComparator minSizeComparator = new CameraMinSizeComparator();
    private static CameraUtil instance = null;


    private CameraUtil() {

    }

    public static CameraUtil getInstance() {
        if (instance == null) {
            instance = new CameraUtil();
            return instance;
        } else {
            return instance;
        }
    }

    private int getRecorderRotation(int cameraId) {
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        return info.orientation;
    }

    /**
     * 获取所有支持的返回视频尺寸
     *
     * @param list      list
     * @param minHeight minHeight
     * @return Size
     */
    private Camera.Size getPropVideoSize(List<Camera.Size> list, int minHeight) {
        Collections.sort(list, ascendSizeComparator);

        int i = 0;
        for (Camera.Size s : list) {
            if ((s.height >= minHeight)) {
                break;
            }
            i++;
        }
        if (i == list.size()) {
            i = 0;
        }
        return list.get(i);
    }

    /**
     * 保证预览方向正确
     *
     * @param activity activity
     * @param cameraId cameraId
     * @param camera   camera
     */
    public void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        //设置预览角度
        camera.setDisplayOrientation(calculateDisplayOrientation(activity, cameraId));
    }

    /**
     * 计算相机角度
     *
     * @param activity
     * @param cameraId
     * @return
     */
    public int calculateDisplayOrientation(Activity activity, int cameraId) {
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
            default:
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // 前置摄像头作镜像翻转
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    /**
     * 设置图片方向
     *
     * @param activity activity
     * @param id       摄像头ID
     * @param bitmap   图片
     * @return 旋转后图片
     */
    public Bitmap setTakePictureOrientation(int id, Bitmap bitmap) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(id, info);
        bitmap = rotatingImageView(id, info.orientation, bitmap);
        return bitmap;
    }

    /**
     * 把相机拍照返回照片转正
     *
     * @param angle 旋转角度
     * @return bitmap 图片
     */
    private Bitmap rotatingImageView(int id, int angle, Bitmap bitmap) {
        //矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        //加入翻转 把相机拍照返回照片转正
        if (id == 1) {
            matrix.postScale(-1, 1);
        }
        // 创建新的图片
        return Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 获取所有支持的预览尺寸
     *
     * @param list     list
     * @param minWidth minWidth
     * @return Size
     */
    private Camera.Size getPropPreviewSize(List<Camera.Size> list, int minWidth) {
        Collections.sort(list, ascendSizeComparator);

        int i = 0;
        for (Camera.Size s : list) {
            if ((s.width >= minWidth)) {
                break;
            }
            i++;
        }
        if (i == list.size()) {
            i = 0;
        }
        return list.get(i);
    }

    /**
     * 获取所有支持的返回图片尺寸
     *
     * @param list     list
     * @param minWidth minWidth
     * @return Size
     */
    private Camera.Size getPropPictureSize(List<Camera.Size> list, int minWidth) {
        Collections.sort(list, ascendSizeComparator);
        int i = 0;
        for (Camera.Size s : list) {
            if ((s.width >= minWidth)) {
                break;
            }
            i++;
        }
        if (i == list.size()) {
            i = 0;
        }
        return list.get(i);
    }

    /**
     * 获取所有支持的返回视频尺寸
     *
     * @param list      list
     * @param minHeight minHeight
     * @return Size
     */
    public Camera.Size getPropSizeForHeight(List<Camera.Size> list, int minHeight) {
        Collections.sort(list, ascendSizeComparator);
        int i = 0;
        for (Camera.Size s : list) {
            if ((s.height >= minHeight)) {
                Log.e(TAG, "getPropSizeForHeight: s.height=" + s.height);
                break;
            }
            i++;
        }
        if (i == list.size()) {
            i = list.size();
        }
        return list.get(i);
    }

    /**
     * 根据 宽度和高度找到是否有相等的 尺寸  如果没有 就获取最小的 值
     *
     * @param list   摄像头支持像素集合
     * @param height 屏幕高度
     * @param width  屏幕宽度
     * @return size 预览分辨率
     */
    public Camera.Size getPicPreviewSize(List<Camera.Size> list, int height, int width) {
        Collections.sort(list, ascendSizeComparator);
        List<WrapCameraSize> wrapCameraSizes = new ArrayList<>(list.size());
        List<Camera.Size> addList = new ArrayList<>();
        for (int x = 0; x < list.size(); x++) {
            Camera.Size s = list.get(x);
            // camera 中的宽度和高度 相反。为适配更多分辨率，改为 ||
            if ((s.width == height) || (s.height == width)) {
                if ((s.width == height) && (s.height == width)) {
                    // 有完全匹配，则返回
                    return list.get(x);
                }
                addList.add(s);
            }
            WrapCameraSize wrapCameraSize = new WrapCameraSize();
            wrapCameraSize.setPosition(x);
            wrapCameraSize.setWidth(s.width);
            wrapCameraSize.setHeight(s.height);
            wrapCameraSize.setD(Math.abs((s.width - height)) + Math.abs((s.height - width)));
            wrapCameraSizes.add(wrapCameraSize);
        }
        // 宽高没有匹配，返回最高分辨率
        if (addList.size() == 0) {
            Collections.sort(wrapCameraSizes, minSizeComparator);
            return list.get(wrapCameraSizes.get(0).getPosition());
        }
        return addList.get(addList.size() - 1);
    }


    /*
     * */

    /**
     * 升序 按照高度
     */
    private class CameraAscendSizeComparatorForHeight implements Comparator<Camera.Size> {
        @Override
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            if (lhs.height == rhs.height) {
                return 0;
            } else if (lhs.height > rhs.height) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    private boolean equalRate(Camera.Size s, float rate) {
        float r = (float) (s.width) / (float) (s.height);
        return Math.abs(r - rate) <= 0.03;
    }

    /**
     * 降序
     */
    private class CameraDropSizeComparator implements Comparator<Camera.Size> {
        @Override
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            if (lhs.width == rhs.width) {
                return 0;
            } else if (lhs.width < rhs.width) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    /**
     * 升序
     */
    private class CameraAscendSizeComparator implements Comparator<Camera.Size> {
        @Override
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            if (lhs.width == rhs.width) {
                return 0;
            } else if (lhs.width > rhs.width) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    /**
     * 升序
     */
    private class CameraMinSizeComparator implements Comparator<WrapCameraSize> {
        @Override
        public int compare(WrapCameraSize lhs, WrapCameraSize rhs) {
            if (lhs.getD() == rhs.getD()) {
                return 0;
            } else if (lhs.getD() > rhs.getD()) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    /**
     * 打印支持的previewSizes
     *
     * @param params
     */
    private void printSupportPreviewSize(Camera.Parameters params) {
        List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
        for (int i = 0; i < previewSizes.size(); i++) {
            Camera.Size size = previewSizes.get(i);
        }

    }

    /**
     * 打印支持的pictureSizes
     *
     * @param params
     */
    private void printSupportPictureSize(Camera.Parameters params) {
        List<Camera.Size> pictureSizes = params.getSupportedPictureSizes();
        for (int i = 0; i < pictureSizes.size(); i++) {
            Camera.Size size = pictureSizes.get(i);
        }
    }

    /**
     * 打印支持的聚焦模式
     *
     * @param params params
     */
    private void printSupportFocusMode(Camera.Parameters params) {
        List<String> focusModes = params.getSupportedFocusModes();
        for (String mode : focusModes) {
            Log.e(TAG, "printSupportFocusMode: " + mode);
        }
    }

    /**
     * 打开闪关灯
     *
     * @param mCamera mCamera
     */
    public void turnLightOn(Camera mCamera) {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters == null) {
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        if (flashModes == null) {
            return;
        }
        String flashMode = parameters.getFlashMode();
        if (!Camera.Parameters.FLASH_MODE_ON.equals(flashMode)) {
            // Turn on the flash
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(parameters);
            }
        }
    }

    /**
     * 自动模式闪光灯
     *
     * @param mCamera mCamera
     */
    public void turnLightAuto(Camera mCamera) {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters == null) {
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        if (flashModes == null) {
            return;
        }
        String flashMode = parameters.getFlashMode();
        if (!Camera.Parameters.FLASH_MODE_AUTO.equals(flashMode)) {
            // Turn on the flash
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(parameters);
            }
        }
    }

    /**
     * 关闭闪光灯
     *
     * @param mCamera mCamera
     */
    public void turnLightOff(Camera mCamera) {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters == null) {
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        String flashMode = parameters.getFlashMode();
        if (flashModes == null) {
            return;
        }
        if (!Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(parameters);
            }
        }
    }

    public List<Rect> detectionBitmap(Bitmap bitmap, int screenWidth, int screenHeight) {
        // 设置你想检测的数量，数值越大错误率越高，所以需要置信度来判断,但有时候置信度也会出问题
        int MAX_FACES = 1;
        FaceDetector faceDet = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), MAX_FACES);
        // 将人脸数据存储到faceArray中
        FaceDetector.Face[] faceArray = new FaceDetector.Face[MAX_FACES];
        // 返回找到图片中人脸的数量，同时把返回的脸部位置信息放到faceArray中，过程耗时,图片越大耗时越久
        int findFaceNum = faceDet.findFaces(bitmap, faceArray);
        Log.e(TAG, "找到脸部数量:" + findFaceNum);
        if (findFaceNum == 0) {
            return null;
        }
        List<Rect> rects = new ArrayList<>();
        PointF pf = new PointF();
        Rect r = null;
        //这里需要 算出 SurfaceView 的宽度跟 抓取每一帧图像宽度的比例，主要是为了让图像的某个坐标转化实际 SurfaceView 的坐标
        float scaleX = screenWidth * 1.0f / bitmap.getWidth();
        //高度同理
        float scaleY = screenHeight * 1.0f / bitmap.getHeight();
        float scale = Math.max(scaleX, scaleY);
        Log.e(TAG, "Bitmap Width：" + bitmap.getWidth() + " Height：" + bitmap.getHeight() + " scaleX：" + scaleX + " scaleY：" + scaleY);
        for (FaceDetector.Face face : faceArray) {
            r = new Rect();
            if (face == null) {
                continue;
            }
            face.getMidPoint(pf);
            Log.i(TAG, "可信度 confidence:" + face.confidence());
            if (face.confidence() < 0.3) {
                continue;
            }
            // 这里的框，参数分别是：左上角的X,Y 右下角的X,Y
            // 也就是左上角（r.left,r.top），右下角( r.right,r.bottom)。
            // 该宽度是两眼珠黑色外边距
            float eyesDistance1 = face.eyesDistance();
            r.left = (int) (pf.x * scale - eyesDistance1);
            r.right = (int) (pf.x * scale + eyesDistance1);
            r.top = (int) (pf.y * scale - eyesDistance1);
            r.bottom = (int) (pf.y * scale + eyesDistance1 * 1.3f);
            Log.d(TAG, r.toString());
            rects.add(r);
        }
        return rects;
    }


    class WrapCameraSize {
        // 索引
        private int position;
        // 宽
        private int width;
        // 高
        private int height;
        // 宽的差的绝对值和高的差的绝对值之和
        private int d;

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getD() {
            return d;
        }

        public void setD(int d) {
            this.d = d;
        }

    }

}
