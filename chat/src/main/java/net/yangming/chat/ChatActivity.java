package net.yangming.chat;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;


import java.util.ArrayList;

public abstract class ChatActivity extends Activity implements OnEditorActionListener {
    ListView listview;
    protected TextView txt_speak;
    protected EditText et_common_edit;
    protected ArrayList<MessageInfo> msgList = new ArrayList<>();
    protected ChatAdapter charAdapter;
    private HorizontalScrollView chat_face_new;
    private View chat_more_lay;
    boolean isSpeech = false;
    protected Handler handler;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        initData();
        initView();
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                handleMsg(msg);
            }

        };
        initPermission();
        initRecog();
    }

    protected abstract void initRecog();

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            LinearLayout chat_bottom = this.findViewById(R.id.chat_bottom);
            View v = getWindow().findViewById(Window.ID_ANDROID_CONTENT);
            float fTop = (chat_bottom.getTop() + v.getTop());
            if (event.getY() < fTop) {
                hideInput();
                chat_more_lay.setVisibility(View.GONE);
                hideFaceLayout();
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private void initData() {
        MessageInfo info = new MessageInfo();
        info.setFrom("system");
        info.setTextContent("你好啊");
        msgList.add(info);

    }

    protected abstract void handleMsg(Message msg);

    public void OnButtonClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_face:
                showFaceLayout();
                break;
            case R.id.iv_add_more:
                showMoreLayout();
                break;
            case R.id.et_common_edit:
                hideMoreLayout();
                hideFaceLayout();
                break;
            case R.id.linear_photo:
                // 图库
                Toast.makeText(this, "图库", Toast.LENGTH_SHORT).show();
                break;
            case R.id.linear_photograph:
                // 拍照
                Toast.makeText(this, "拍照", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_cut:
                // 语音、文字切换
                hideInput();
                if (isSpeech) {
                    isSpeech = false;
                    et_common_edit.setVisibility(View.VISIBLE);
                    txt_speak.setVisibility(View.GONE);
                } else {
                    isSpeech = true;
                    et_common_edit.setVisibility(View.GONE);
                    txt_speak.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    protected void initView() {
        chat_face_new = findViewById(R.id.chat_face_new);
        chat_more_lay = findViewById(R.id.chat_more_lay);
        listview = findViewById(R.id.rlv_chats);
        et_common_edit = findViewById(R.id.et_common_edit);
        et_common_edit.setOnEditorActionListener(this);
        txt_speak = findViewById(R.id.txt_speak);

        charAdapter = new ChatAdapter(msgList) {

            @Override
            public void OnIconClick(String sUid) {
                // TODO Auto-generated method stub

            }
        };
        listview.setAdapter(charAdapter);

        // 初始化表情
        setFaceNew();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        String content = et_common_edit.getText().toString();
        MessageInfo info = new MessageInfo();
        info.setFrom("user");
        info.setTextContent(content);
        msgList.add(info);

        MessageInfo info2 = new MessageInfo();
        info2.setFrom("system");
        info2.setTextContent("你好啊");
        msgList.add(info2);
        charAdapter.notifyDataSetChanged();
        return true;
    }

    // 显示表情
    private void showFaceLayout() {
        chat_more_lay.setVisibility(View.GONE);
        if (chat_face_new.getVisibility() == View.VISIBLE) {
            chat_face_new.setVisibility(View.GONE);
            updateSoftInputMethod(this, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            showInput();
        } else {
            updateSoftInputMethod(this, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

            Animation animation = AnimationUtils.loadAnimation(this, R.anim.in_from_down);
            chat_face_new.setAnimation(animation);

            chat_face_new.setVisibility(View.VISIBLE);
            hideInput();
        }
    }

    private void hideFaceLayout() {
        if (chat_face_new.getVisibility() == View.VISIBLE) {
            chat_face_new.setVisibility(View.GONE);
            updateSoftInputMethod(this, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }

    public static void updateSoftInputMethod(Activity activity, int softInputMode) {
        if (!activity.isFinishing()) {
            WindowManager.LayoutParams params = activity.getWindow().getAttributes();
            if (params.softInputMode != softInputMode) {
                params.softInputMode = softInputMode;
                activity.getWindow().setAttributes(params);
            }
        }
    }

    // 显示更多
    private void showMoreLayout() {
        chat_face_new.setVisibility(View.GONE);
        if (chat_more_lay.getVisibility() == View.VISIBLE) {
            chat_more_lay.setVisibility(View.GONE);
            updateSoftInputMethod(this, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            showInput();
        } else {
            chat_more_lay.setVisibility(View.VISIBLE);
            updateSoftInputMethod(this, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            hideInput();
        }
    }

    private void hideMoreLayout() {
        if (chat_more_lay.getVisibility() == View.VISIBLE) {
            chat_more_lay.setVisibility(View.GONE);
            updateSoftInputMethod(this, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }

    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) ChatActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(et_common_edit.getWindowToken(), 0);
        }

    }

    protected void showInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(et_common_edit, InputMethodManager.SHOW_FORCED);
        }
    }

    private void setFaceNew() {
        int iCount = faceImageGrobal.getImageCount();
        int iCols = (iCount + 3) / 4;
        LinearLayout ll_face_1 = chat_face_new.findViewById(R.id.ll_face_1);

        for (int i = 0; i < iCols; i++) {
            ImageView iv = (ImageView) View.inflate(this, R.layout.face_image_new, null);
            iv.setImageResource(faceImageGrobal.getImage(i));
            iv.setTag(i);
            iv.setOnClickListener(imageViewClick);
            ll_face_1.addView(iv);
        }

        LinearLayout ll_face_2 = chat_face_new.findViewById(R.id.ll_face_2);
        for (int i = 0; i < iCols; i++) {
            ImageView iv = (ImageView) View.inflate(this, R.layout.face_image_new, null);
            iv.setImageResource(faceImageGrobal.getImage(iCols + i));
            iv.setTag(iCols + i);
            iv.setOnClickListener(imageViewClick);
            ll_face_2.addView(iv);
        }

        LinearLayout ll_face_3 = chat_face_new.findViewById(R.id.ll_face_3);
        for (int i = 0; i < iCols; i++) {
            ImageView iv = (ImageView) View.inflate(this, R.layout.face_image_new, null);
            iv.setImageResource(faceImageGrobal.getImage(2 * iCols + i));
            iv.setTag(2 * iCols + i);
            iv.setOnClickListener(imageViewClick);
            ll_face_3.addView(iv);
        }

        LinearLayout ll_face_4 = chat_face_new.findViewById(R.id.ll_face_4);
        for (int i = 0; i < iCols && i < iCount - 3 * iCols; i++) {
            ImageView iv = (ImageView) View.inflate(this, R.layout.face_image_new, null);
            iv.setImageResource(faceImageGrobal.getImage(3 * iCols + i));
            iv.setTag(3 * iCols + i);
            iv.setOnClickListener(imageViewClick);
            ll_face_4.addView(iv);
        }
    }

    private View.OnClickListener imageViewClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            int index = et_common_edit.getSelectionStart();
            Editable editable = et_common_edit.getText();
            editable.insert(index, faceImageGrobal.createFace(ChatActivity.this, position));
            // et_common_edit.append(faceImageGrobal.createFace(ChatActivity.this,
            // position), index, index + 1);
        }
    };

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String[] permissions = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.

            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
    }

}

