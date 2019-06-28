package com.yangming.myproject.view.numkey;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.yangming.myproject.R;

public class NumKeyActivity extends AppCompatActivity implements NumKeyView.OnKeyPressListener, CustomNumKeyView.CallBack {
    private EditText mEditText;
    private NumKeyView mKeyView;
    private CustomNumKeyView mCustomKeyView;
    private LinearLayout mLinearlayout;
    private PopupWindow mPop;
    private View mPopView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_key);
        initView();
    }

    private void initView() {
        mLinearlayout = (LinearLayout) findViewById(R.id.linear);
        mEditText = (EditText) findViewById(R.id.edit);
        mEditText.setInputType(InputType.TYPE_NULL);

        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPop.showAtLocation(mLinearlayout, Gravity.BOTTOM, 0, 0);
            }
        });
        mPop = new PopupWindow();
//        mCustomKeyView=new CustomNumKeyView(this);
        mPopView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_keyboardview, null);
        mPop.setContentView(mPopView);
        mPop.setTouchable(true);
        mPop.setFocusable(true);
        mPop.setBackgroundDrawable(new ColorDrawable());
        mPop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mKeyView = (NumKeyView) mPopView.findViewById(R.id.numkey);
        //设置回调，并进行文本的插入与删除
        mKeyView.setOnKeyPressListener(this);

        mCustomKeyView = (CustomNumKeyView) mPopView.findViewById(R.id.keyboardview);
        mCustomKeyView.setOnCallBack(this);
    }

    public void OnButtonClick(View view) {
        switch (view.getId()) {
            case R.id.btn_default:
                mCustomKeyView.setRandomKeyBoard(false);
                break;
            case R.id.btn_random:
                mCustomKeyView.setRandomKeyBoard(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNubKey(String text) {
        if (mEditText.getText().length() < 6) {
            mEditText.append(text);
            //文本长度为6时隐藏键盘
            if (mEditText.getText().length() == 6) {
                mPop.dismiss();
            }
        }
    }

    @Override
    public void onDeleteKey() {
        int last = mEditText.getText().length();
        if (last > 0) {
            //删除最后一位
            mEditText.getText().delete(last - 1, last);
        }
    }

    @Override
    public void onCancelKey() {
        mPop.dismiss();
    }

    @Override
    public void clickNum(String num) {
        if (mEditText.getText().length() < 6) {
            mEditText.append(num);
            //文本长度为6时隐藏键盘
            if (mEditText.getText().length() == 6) {
                mPop.dismiss();
            }
        }
    }

    @Override
    public void onMesureCellHeight(float mCellWidth, float mCellHight) {

    }

    @Override
    public void deleteNum() {
        int last = mEditText.getText().length();
        if (last > 0) {
            //删除最后一位
            mEditText.getText().delete(last - 1, last);
        }
    }
}
