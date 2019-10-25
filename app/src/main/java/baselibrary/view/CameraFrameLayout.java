package baselibrary.view;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * @author yangming on 2019/10/24
 * 相机控件
 */
public class CameraFrameLayout extends FrameLayout implements CameraView.FocusCallback {
    private Context mContext;

    /**
     * 相机绑定的SurfaceView
     */
    private CameraView mCameraView;
    /**
     * 对焦框
     */
    private FocusBoxView mFocusBoxView;

    public CameraFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public CameraFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        mCameraView = new CameraView(mContext);
        mFocusBoxView = new FocusBoxView(mContext);

        addView(mCameraView);
        addView(mFocusBoxView);

        mCameraView.setOnFocusCallback(this);
    }

    public void onStart() {
        mCameraView.onStart();
    }

    public void onStop() {
        mCameraView.onStop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                //设置聚焦
                Point point = new Point((int) event.getX(), (int) event.getY());
                mFocusBoxView.setTouchFocusRect(event.getX(), event.getY());
                if (!mCameraView.onCameraFocus(point)) {
                    mFocusBoxView.postDelayed(() -> mFocusBoxView.disDrawTouchFocusRect(), 1500);
                }
                break;
            default:
                break;
        }
        return true;
    }

    public void startPreview() {
        mCameraView.startPreview();
    }

    public void takePicture(CameraView.PictureCallback callback) {
        mCameraView.takePicture(callback);
    }

    @Override
    public void onFocus(boolean success) {
        mFocusBoxView.disDrawTouchFocusRect();
    }
}
