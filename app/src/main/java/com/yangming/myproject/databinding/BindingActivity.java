package com.yangming.myproject.databinding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yangming.myproject.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class BindingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_basic, R.id.btn_update, R.id.btn_recyclerView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_basic:
                startActivity(new Intent(this, DataBindingActivity.class));
                break;
            case R.id.btn_update:
                startActivity(new Intent(this, DataActivity.class));
                break;
            case R.id.btn_recyclerView:
                startActivity(new Intent(this, RecyclerViewBindingActivity.class));

                break;
            default:
                break;
        }
    }
}
