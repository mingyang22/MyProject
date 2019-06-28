package com.yangming.myproject.databinding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.yangming.myproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 结合RecyclerView
 */
public class RecyclerViewBindingActivity extends AppCompatActivity {
    private ActivityRecyclerViewBindingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recycler_view_binding);

        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(RecyclerViewBindingActivity.this);
        binding.recycler.setLayoutManager(manager);
        SwordsmanAdapter adapter = new SwordsmanAdapter(getList());
        binding.recycler.setAdapter(adapter);
    }

    public List<Swordsman> getList() {
        List<Swordsman> list = new ArrayList<>();
        Swordsman swordsman1 = new Swordsman("郭靖", "SS");
        Swordsman swordsman2 = new Swordsman("黄蓉", "A");
        list.add(swordsman1);
        list.add(swordsman2);
        return list;
    }
}
