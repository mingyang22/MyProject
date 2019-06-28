package com.yangming.myproject.view.followmove;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author yangming on 2019/3/29
 * 自定义view，随手指滑动
 */
public class FollowMoveView extends View {

    private int lastX;
    private int lastY;

    public FollowMoveView(Context context) {
        super(context);
    }

    public FollowMoveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FollowMoveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = x - lastX;
                int offsetY = y - lastY;
//                // 调用layout方法来重新放置它的位置
//                layout(getLeft() + offsetX, getTop() + offsetY,
//                        getRight() + offsetX, getBottom() + offsetY);

                // 对left和right进行偏移
                offsetLeftAndRight(offsetX);
                // 对top和bottom进行偏移
                offsetTopAndBottom(offsetY);

//                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
//                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
//                layoutParams.leftMargin = getLeft() + offsetX;
//                layoutParams.topMargin = getTop() + offsetY;
//                setLayoutParams(layoutParams);

//                // 在viewGroup中，是移动其所有的子view
//                ((View) getParent()).scrollBy(-offsetX, -offsetY);

                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }

}
