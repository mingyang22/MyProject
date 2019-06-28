package com.yangming.myproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.yangming.myproject.R;
import com.yangming.myproject.view.animation.AnimationActivity;
import com.yangming.myproject.view.followmove.FollowMoveViewActivity;
import com.yangming.myproject.view.mask.MaskActivity;
import com.yangming.myproject.view.numkey.NumKeyActivity;
import com.yangming.myproject.view.viewpager.HorizontalViewActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 自定义view入口
 */
public class ViewActivity extends AppCompatActivity {
    private String TAG = "ViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: ");
        setContentView(R.layout.activity_view);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btn_move, R.id.btn_vp, R.id.btn_numkey, R.id.btn_animation, R.id.btn_mask})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_move:
                startActivity(new Intent(this, FollowMoveViewActivity.class));
                break;
            case R.id.btn_vp:
                startActivity(new Intent(this, HorizontalViewActivity.class));
                break;
            case R.id.btn_numkey:
                startActivity(new Intent(this, NumKeyActivity.class));
                break;
            case R.id.btn_animation:
                startActivity(new Intent(this, AnimationActivity.class));
                break;
            case R.id.btn_mask:
                startActivity(new Intent(this, MaskActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG, "onNewIntent: ");
    }

}
