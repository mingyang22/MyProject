package com.yangming.myproject.databinding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yangming.myproject.R;

import java.util.Date;

/**
 * 动态更新和数据绑定
 */
public class DataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDataBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_data);

        // 转换器
        binding.setTime(new Date());

        ObSwordsman obSwordsman = new ObSwordsman("逍遥子", "SSS");
        binding.setObswordsman(obSwordsman);

        binding.btUpdataObswordsman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obSwordsman.setName("石破天");
            }
        });

        ObFSwordsman obFSwordsman = new ObFSwordsman("风清扬","S");
        binding.setObfswordsman(obFSwordsman);
        binding.btUpdataObfswordsman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obFSwordsman.name.set("令狐冲");
            }
        });

        binding.setObswordsman(obSwordsman);
        binding.btUpdateBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obSwordsman.setName("逍遥子");
            }
        });
    }
}
