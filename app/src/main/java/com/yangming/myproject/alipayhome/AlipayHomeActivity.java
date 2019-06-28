package com.yangming.myproject.alipayhome;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.yangming.myproject.R;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlipayHomeActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;

    private View tl_expand, tl_collapse, life_pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_alipay_home);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tl_expand = findViewById(R.id.tl_expand);
        tl_collapse = findViewById(R.id.tl_collapse);
        life_pay = findViewById(R.id.life_pay);
        appBar.addOnOffsetChangedListener(this);
        setSupportActionBar(toolbar);
    }

    public static float StringForFloatSubtract(float str, float Str2) {
        BigDecimal big1 = new BigDecimal(String.valueOf(str));
        BigDecimal big2 = new BigDecimal(String.valueOf(Str2));
        return big1.subtract(big2).floatValue();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int offset = Math.abs(verticalOffset);
        int total = appBarLayout.getTotalScrollRange();
        int line = 100;//分界点
        float centerSize = total / 2;
        float alpha = 0;
        if (offset == 0) {
            //完全展开时
            tl_expand.setVisibility(View.VISIBLE);
            tl_collapse.setVisibility(View.GONE);
            tl_expand.setAlpha(1);
        } else if (offset == total) {
            //完全收缩时
            tl_expand.setVisibility(View.GONE);
            tl_collapse.setVisibility(View.VISIBLE);
            tl_collapse.setAlpha(1);
        } else {
            float upAlpha = (float) (1 - (offset * 0.01));
            if (upAlpha >= 0) {
                tl_expand.setVisibility(View.VISIBLE);
                tl_collapse.setVisibility(View.GONE);
                tl_expand.setAlpha(upAlpha);
            } else {
                tl_collapse.setAlpha(StringForFloatSubtract(offset, line) / StringForFloatSubtract(total, line));
                tl_expand.setVisibility(View.GONE);
                tl_collapse.setVisibility(View.VISIBLE);
            }
            life_pay.setAlpha(1 - offset / centerSize);
        }
    }
}
