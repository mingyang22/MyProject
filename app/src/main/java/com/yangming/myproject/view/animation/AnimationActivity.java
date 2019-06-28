package com.yangming.myproject.view.animation;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yangming.myproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author yangming
 * View 动画
 */
public class AnimationActivity extends AppCompatActivity {

    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.text3)
    TextView text3;
    @BindView(R.id.text4)
    TextView text4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        ButterKnife.bind(this);

        xml();
        propertyValuesHolder();
        animatorSet();

    }

    /**
     * 属性动画
     * xml文件实现
     */
    private void xml() {
        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.scale);
        animator.setTarget(text);
        animator.start();
    }

    /**
     * 属性动画
     * AnimatorSet实现组合动画
     */
    private void propertyValuesHolder() {
        PropertyValuesHolder valuesHolder1 = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.5f);
        PropertyValuesHolder valuesHolder2 = PropertyValuesHolder.ofFloat("rotationX", 0.0f, 90.0f, 0.0F);
        PropertyValuesHolder valuesHolder3 = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.3f, 1.0F);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(text1, valuesHolder1, valuesHolder2, valuesHolder3);
        objectAnimator.setDuration(2000).start();
    }

    /**
     * 属性动画
     * AnimatorSet实现组合动画
     */
    private void animatorSet() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(text2, "translationX", 0.0f, 200.0f, 0.0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(text3, "scaleX", 1.0f, 2.0f);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(text4, "rotationX", 0.0f, 90.0f, 0.0F);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(1000);
        set.play(animator1).with(animator2).after(animator3);
        set.start();
    }

    @OnClick({R.id.text, R.id.text1, R.id.text2, R.id.text3, R.id.text4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
            default:
                break;
        }
    }
}
