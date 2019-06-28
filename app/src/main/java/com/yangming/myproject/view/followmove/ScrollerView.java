package com.yangming.myproject.view.followmove;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Scroller;

/**
 * @author yangming on 2019/4/25
 * 通过Scroller实现弹性滑动
 */
public class ScrollerView extends android.support.v7.widget.AppCompatTextView {
    private Scroller scroller;

    public ScrollerView(Context context) {
        super(context);
    }

    public ScrollerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
    }

    public ScrollerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }

    /**
     * 使用Scroller实现弹性滑动
     * @param destX 目标X
     * @param destY 目标Y
     */
    public void smoothScrollTo(int destX, int destY) {
        int scrollX = getScrollX();
        int delta = destX - scrollX;
        scroller.startScroll(scrollX, 0, delta, 0, 3000);
        invalidate();
    }

}
