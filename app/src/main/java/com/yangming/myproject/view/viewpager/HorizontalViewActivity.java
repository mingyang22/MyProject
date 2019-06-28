package com.yangming.myproject.view.viewpager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.yangming.myproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yangming on 2019/2/28
 * 简化版 ViewPager
 */
public class HorizontalViewActivity extends AppCompatActivity {
    @BindView(R.id.list_one)
    ListView listOne;
    @BindView(R.id.list_two)
    ListView listTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_view);
        ButterKnife.bind(this);

        String[] strs1 = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, strs1);
        listOne.setAdapter(adapter1);

        String[] strs2 = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, strs2);
        listTwo.setAdapter(adapter2);

    }
}
