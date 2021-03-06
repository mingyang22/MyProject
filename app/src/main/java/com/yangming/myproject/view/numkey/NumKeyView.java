package com.yangming.myproject.view.numkey;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import com.yangming.myproject.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author yangming on 2019/4/1
 * 系统自带KeyboardView、Keyboard实现数字键盘
 */
public class NumKeyView extends KeyboardView implements KeyboardView.OnKeyboardActionListener {
    //用于区分左下角空白按键,(要与xml里设置的数值相同)
    private int KEYCODE_CANCEL = -10;
    //删除按键背景图片
    private Drawable mDeleteDrawable;
    //最下面两个灰色的按键（空白按键跟删除按键）
    private int mBgColor;


    public NumKeyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public NumKeyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
//        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.NumKeyView);
        mBgColor = context.getResources().getColor(R.color.white);
        mDeleteDrawable = context.getResources().getDrawable(R.mipmap.xhd_icon_cancel);
//        ta.recycle();
        //获取xml中的按键布局
        Keyboard keyboard = new Keyboard(context, R.xml.keyboard);
        setKeyboard(keyboard);
        setEnabled(true);
        setPreviewEnabled(false);
        setOnKeyboardActionListener(this);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for (Keyboard.Key key : keys) {
            //绘制空白键
            if (key.codes[0] == KEYCODE_CANCEL) {
                drawKeyBackGround(key, canvas);
            } else if (key.codes[0] == Keyboard.KEYCODE_DELETE) {
                //绘制删除键背景
                drawKeyBackGround(key, canvas);
                //绘制按键图片
                drawkeyDelete(key, canvas);
            }
        }
    }

    private void drawKeyBackGround(Keyboard.Key key, Canvas canvas) {
        ColorDrawable colordrawable = new ColorDrawable(mBgColor);
        colordrawable.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
        colordrawable.draw(canvas);
    }

    private void drawkeyDelete(Keyboard.Key key, Canvas canvas) {
        int intrinsicWidth = mDeleteDrawable.getIntrinsicWidth();
        int intrinsicHeight = mDeleteDrawable.getIntrinsicHeight();
        int drawWidth = key.width;
        int drawHeight = key.height;
        if (drawWidth < intrinsicWidth) {
            drawHeight = drawWidth * intrinsicHeight / intrinsicWidth;
        }
        drawWidth = drawWidth / 7;
        drawHeight = drawHeight / 5;
        int widthInterval = (key.width - drawWidth) / 2;
        int heightInterval = (key.height - drawHeight) / 2;

        mDeleteDrawable.setBounds(key.x + widthInterval, key.y + heightInterval, key.x + widthInterval + drawWidth, key.y + heightInterval + drawHeight);
        mDeleteDrawable.draw(canvas);
    }

    //回调接口
    public interface OnKeyPressListener {
        //添加数据回调
        void onNubKey(String text);

        //删除数据回调
        void onDeleteKey();

        //取消数据回调
        void onCancelKey();
    }

    private OnKeyPressListener mOnkeyPressListener;

    public void setOnKeyPressListener(OnKeyPressListener li) {
        mOnkeyPressListener = li;
    }

    @Override
    public void onKey(int i, int[] ints) {
        if (i == Keyboard.KEYCODE_DELETE && mOnkeyPressListener != null) {
            //删除数据回调
            mOnkeyPressListener.onDeleteKey();
        } else if (i != KEYCODE_CANCEL) {
            //添加数据回调
            mOnkeyPressListener.onNubKey(Character.toString((char) i));
        } else {
            mOnkeyPressListener.onCancelKey();
        }
    }

    @Override
    public void onPress(int i) {

    }

    @Override
    public void onRelease(int i) {

    }


    @Override
    public void onText(CharSequence charSequence) {

    }

    @Override
    public void swipeRight() {
        super.swipeRight();
    }

    @Override
    public void swipeDown() {
        super.swipeDown();
    }

    @Override
    public void swipeLeft() {
        super.swipeLeft();
    }

    @Override
    public void swipeUp() {
        super.swipeUp();
    }

    private List<Character> keylabels = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');

    public void setRandomKeyBoard(boolean isRandom) {
        if (isRandom) {
            Keyboard keyboard = getKeyboard();
            List<Keyboard.Key> keys = keyboard.getKeys();
            Collections.shuffle(keylabels);
            int i = 0;
            for (Keyboard.Key key : keys) {
                if (key.codes[0] != KEYCODE_CANCEL && key.codes[0] != Keyboard.KEYCODE_DELETE) {
                    key.label = keylabels.get(i).toString();
                    //自己定义code码需要跟之前布局里面的不同，如果不修改code码，对应的还是之前的label值
                    //可以自己试一下
                    key.codes[0] = keylabels.get(i);
                    i++;
                }
            }
            setKeyboard(keyboard);
        }

    }

}
