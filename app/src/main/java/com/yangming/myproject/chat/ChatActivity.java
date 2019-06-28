package com.yangming.myproject.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.yangming.myproject.R;

import java.util.ArrayList;

import baselibrary.recycleview.DividerItemDecoration;
import baselibrary.recycleview.MultiItemTypeAdapter;

/**
 * RecyclerView实现聊天界面
 */
public class ChatActivity extends AppCompatActivity {
    protected EditText etCommonEdit;
    protected RecyclerView recycleView;
    protected MultiItemTypeAdapter adapter;
    protected ArrayList<MessageInfo> msgList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initView();

    }

    private void initView() {
        etCommonEdit = (EditText) findViewById(R.id.et_common_edit);
        recycleView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);
        recycleView.setLayoutManager(linearLayoutManager);
        //设置Item增加、移除动画
        recycleView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST,
                10, getResources().getColor(R.color.transparent));
        recycleView.addItemDecoration(dividerItemDecoration);
        adapter = new MultiItemTypeAdapter(this, msgList);
        adapter.addItemViewDelegate(new SystemMsg());
        adapter.addItemViewDelegate(new ProfileMsg());
        adapter.addItemViewDelegate(new TimeMsg());
        recycleView.setAdapter(adapter);
    }

    public void OnButtonClick(View view) {
        switch (view.getId()) {
            case R.id.img_send:
                String content = etCommonEdit.getText().toString().trim();
                addMessage(2, content);
                etCommonEdit.setText("");
                addMessage(1, "测试测试");
                addMessage(3, "");
                break;
            default:
                break;

        }
    }

    private void addMessage(int fromRobot, String msg) {
        if (msg != null) {
            MessageInfo info = new MessageInfo();
            info.setFromRobot(fromRobot);
            if (fromRobot == 1) {
                info.setTitle("机器人小信");
            } else if (fromRobot == 2) {
                info.setTitle("荆轲");
            }
            info.setContent(msg);
            info.setDate("2018-05-04");
            info.setTime("15:32");
            adapter.addData(adapter.getItemCount(), info);
            recycleView.scrollToPosition(adapter.getItemCount() - 1);
        }
    }
}
