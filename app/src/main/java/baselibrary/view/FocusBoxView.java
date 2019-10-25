package baselibrary.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * @author yangming on 2019/10/24
 * 对焦框
 */
public class FocusBoxView extends AppCompatImageView {
    /**
     * 焦点附近设置矩形区域作为对焦区域
     */
    private Rect touchFocusRect;

    /**
     * 新建画笔
     */
    private Paint touchFocusPaint;

    public FocusBoxView(Context context) {
        this(context, null);
    }

    public FocusBoxView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FocusBoxView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 画笔设置
        touchFocusPaint = new Paint();
        touchFocusPaint.setColor(Color.WHITE);
        touchFocusPaint.setStyle(Paint.Style.STROKE);
        touchFocusPaint.setStrokeWidth(3);

    }

    /**
     * 对焦并绘制对焦矩形框
     */
    public void setTouchFocusRect(float x, float y) {
        animatorSet();
        //以焦点为中心，宽度为200的矩形框
        touchFocusRect = new Rect((int) (x - 100), (int) (y - 100), (int) (x + 100), (int) (y + 100));
        //刷新界面，调用onDraw(Canvas canvas)函数绘制矩形框
        postInvalidate();
    }

    /**
     * 对焦完成后，清除对焦矩形框
     */
    public void disDrawTouchFocusRect() {
        // 将对焦区域设置为null，刷新界面后对焦框消失
        touchFocusRect = null;
        // 刷新界面，调用onDraw(Canvas canvas)函数
        postInvalidate();
    }

    /**
     * 属性动画
     * AnimatorSet实现组合动画
     */
    private void animatorSet() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(this, "scaleX", 1.5f, 1.0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(this, "scaleY", 1.5f, 1.0f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(200);
        set.play(animator1).with(animator2);
        set.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 在画布上绘图，postInvalidate()后自动调用
        drawTouchFocusRect(canvas);
        super.onDraw(canvas);
    }

    private void drawTouchFocusRect(Canvas canvas) {
        if (touchFocusRect != null) {
            // 根据对焦区域targetFocusRect，绘制自己想要的对焦框样式，本文在矩形四个角取L形状
            // 左下角
            canvas.drawRect(touchFocusRect.left - 2, touchFocusRect.bottom,
                    touchFocusRect.left + 20, touchFocusRect.bottom + 2, touchFocusPaint);
            canvas.drawRect(touchFocusRect.left - 2, touchFocusRect.bottom - 20,
                    touchFocusRect.left, touchFocusRect.bottom, touchFocusPaint);
            // 左上角
            canvas.drawRect(touchFocusRect.left - 2, touchFocusRect.top - 2,
                    touchFocusRect.left + 20, touchFocusRect.top, touchFocusPaint);
            canvas.drawRect(touchFocusRect.left - 2, touchFocusRect.top,
                    touchFocusRect.left, touchFocusRect.top + 20, touchFocusPaint);
            // 右上角
            canvas.drawRect(touchFocusRect.right - 20, touchFocusRect.top - 2,
                    touchFocusRect.right + 2, touchFocusRect.top, touchFocusPaint);
            canvas.drawRect(touchFocusRect.right, touchFocusRect.top,
                    touchFocusRect.right + 2, touchFocusRect.top + 20, touchFocusPaint);
            // 右下角
            canvas.drawRect(touchFocusRect.right - 20, touchFocusRect.bottom,
                    touchFocusRect.right + 2, touchFocusRect.bottom + 2, touchFocusPaint);
            canvas.drawRect(touchFocusRect.right, touchFocusRect.bottom - 20,
                    touchFocusRect.right + 2, touchFocusRect.bottom, touchFocusPaint);
        }
    }

}
