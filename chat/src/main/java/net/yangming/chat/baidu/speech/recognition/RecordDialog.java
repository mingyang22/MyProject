package net.yangming.chat.baidu.speech.recognition;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.yangming.chat.R;


public class RecordDialog extends Dialog {

    Activity context;
    private ImageView img;

    public RecordDialog(Context context) {
        super(context, R.style.TransparentProgressDialog);
        // TODO Auto-generated constructor stub
        this.context = (Activity) context;
        this.setCancelable(false);// 设置点击空白处不被关闭
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_gif);
        img = findViewById(R.id.img);
        initView();
    }

    private void initView() {
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float density = dm.density;
        density = 1;
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        params.width = (int) (width * density) * 1 / 2;
        params.height = (int) (width * density) * 1 / 2;
        params.dimAmount = 0f;
        window.setAttributes(params);
        Glide.with(context).load("file:///android_asset/xhd_open.gif").asGif().into(img);
    }

}
