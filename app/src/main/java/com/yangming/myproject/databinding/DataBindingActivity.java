package com.yangming.myproject.databinding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.yangming.myproject.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataBindingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDataBindingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding);
        // 基础使用
        Swordsman swordsman = new Swordsman("张无忌", "S");
        binding.setSwordsman(swordsman);

        // 事件处理
        binding.setOnclicklistener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DataBindingActivity.this, "点击", Toast.LENGTH_SHORT).show();
            }
        });

        // 基本数据类型变量定义
        binding.setName("风清扬");
        binding.setAge(22);
        binding.setMan(true);

        // 集合类型
        ArrayList list = new ArrayList();
        list.add("0");
        list.add("1");
        binding.setList(list);

        Map map = new HashMap();
        map.put("age", "25");
        binding.setMap(map);

        String[] arrays = {"张无忌", "慕容龙城"};
        binding.setArrays(arrays);

        Swordsman swordsman2 = new Swordsman("独孤求败", "SS");
        binding.setSwordsman(swordsman2);

    }
}
