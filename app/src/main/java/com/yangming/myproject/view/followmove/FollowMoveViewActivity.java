package com.yangming.myproject.view.followmove;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.yangming.myproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author yangming on 2019/2/27
 * View的滑动
 */
public class FollowMoveViewActivity extends AppCompatActivity {

    @BindView(R.id.custom)
    FollowMoveView custom;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.text3)
    TextView text3;
    @BindView(R.id.text4)
    ScrollerView text4;
    @BindView(R.id.text5)
    TextView text5;
    @BindView(R.id.text6)
    TextView text6;
    @BindView(R.id.text7)
    TextView text7;
    @BindView(R.id.text8)
    TextView text8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_move_view);
        ButterKnife.bind(this);

        //使用scrollBy滑动
        text.post(() -> text.scrollBy(-100, 0));

        // View动画
        text1.setAnimation(AnimationUtils.loadAnimation(this, R.anim.translate));

        // 属性动画
        ObjectAnimator.ofFloat(text2, "translationX", 0, 300).setDuration(2000).start();

        // 改变布局参数
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) text3.getLayoutParams();
        params.width += 100;
        params.leftMargin += 100;
        text3.setLayoutParams(params);

        // 使用Scroller实现弹性滑动
        text4.smoothScrollTo(-100, 0);

        // 动画本质上没有作用于任何对象上
        valueAnimator();

        // handler延时策略
        handler.sendEmptyMessage(MESSAGE_SCROLL_TO);

        // postDelayed延时策略
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mCount1++;
                if (mCount1 <= FRAME_COUNT) {
                    float fraction = mCount1 / (float) FRAME_COUNT;
                    int scrollX = (int) (fraction * (100));
                    text7.scrollTo(scrollX, 0);
                    text7.postDelayed(this::run, DELAYED_TIME);
                }
            }
        };
        text7.postDelayed(runnable, DELAYED_TIME);

        // sleep延时策略
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mCount2 <= FRAME_COUNT) {
                    mCount2++;
                    float fraction = mCount2 / (float) FRAME_COUNT;
                    int scrollX = (int) (fraction * (100));
                    text8.scrollTo(scrollX, 0);
                    try {
                        Thread.currentThread();
                        Thread.sleep(DELAYED_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

    /**
     * ValueAnimator属性动画本身不提供任何一种动画，简单来说就是一个数值发生器
     */
    private void valueAnimator() {
        int startX = 0;
        int deltaX = -100;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 1).setDuration(1000);
        valueAnimator.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            // 滑动的是view的内容
            text5.scrollTo(startX + (int) (deltaX * fraction), 0);
        });
        valueAnimator.start();
    }

    public static final int MESSAGE_SCROLL_TO = 1;
    public static final int FRAME_COUNT = 30;
    public static final int DELAYED_TIME = 33;
    private int mCount = 0;
    private int mCount1 = 0;
    private int mCount2 = 0;


    /**
     * 使用延时策略实现弹性动画
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SCROLL_TO: {
                    mCount++;
                    if (mCount <= FRAME_COUNT) {
                        float fraction = mCount / (float) FRAME_COUNT;
                        int scrollX = (int) (fraction * (-100));
                        text6.scrollTo(scrollX, 0);
                        handler.sendEmptyMessageDelayed(MESSAGE_SCROLL_TO, DELAYED_TIME);
                    }
                }
                break;
                default:
                    super.handleMessage(msg);
            }

        }
    };

    @OnClick({R.id.custom, R.id.text, R.id.text1, R.id.text2, R.id.text3, R.id.text4, R.id.text5, R.id.text6, R.id.text7, R.id.text8})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.custom:
                Toast.makeText(this, "点击CustomView", Toast.LENGTH_SHORT).show();
                break;
            case R.id.text:
                Toast.makeText(this, "点击TextView", Toast.LENGTH_SHORT).show();
                break;
            case R.id.text1:
                Toast.makeText(this, "点击TextView1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.text2:
                Toast.makeText(this, "点击TextView2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.text3:
                Toast.makeText(this, "点击TextView3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.text4:
                Toast.makeText(this, "点击TextView4", Toast.LENGTH_SHORT).show();
                break;
            case R.id.text5:
                Toast.makeText(this, "点击TextView5", Toast.LENGTH_SHORT).show();
                break;
            case R.id.text6:
                Toast.makeText(this, "点击TextView6", Toast.LENGTH_SHORT).show();
                break;
            case R.id.text7:
                Toast.makeText(this, "点击TextView7", Toast.LENGTH_SHORT).show();
                break;
            case R.id.text8:
                Toast.makeText(this, "点击TextView8", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }


}
