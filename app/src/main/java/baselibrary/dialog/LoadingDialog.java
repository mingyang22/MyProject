package baselibrary.dialog;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.yangming.myproject.R;

/**
 * @author yangming on 2018/8/20
 * 菊花加载框
 */
public class LoadingDialog extends Dialog {

    private TextView tvMsg;
    private boolean showText;

    public LoadingDialog(Context context, boolean showText) {
        super(context, R.style.LoadingDialogStyle);
        // 设置点击空白处不被关闭
        this.setCancelable(false);
        this.showText = showText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        initView();
    }

    private void initView() {
        Window window = getWindow();
        if (window != null) {
            window.getAttributes().gravity = Gravity.CENTER;
            window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        }
        tvMsg = findViewById(R.id.dialog_msg);
        tvMsg.setVisibility(showText ? View.VISIBLE : View.GONE);
    }

    public void setMessage(String strMessage) {
        tvMsg.setText(strMessage);
    }


}

