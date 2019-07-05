package com.yangming.myproject.thread;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.yangming.myproject.R;

import baselibrary.util.TimeUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author yangming 2019/05/16
 */
public class AsyncTaskActivity extends AppCompatActivity {
    private static final String TAG = "AsyncTaskActivity";

    @BindView(R.id.tv)
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);
        ButterKnife.bind(this);

        Intent intent = new Intent(this, LocalIntentService.class);
        intent.putExtra("task", "yang.task");
        startService(intent);

    }

    @OnClick(R.id.bt_start)
    public void onViewClicked() {
        // Android 3.0开始，默认情况下 AsyncTask 串行执行
//        new MyAsyncTask("AsyncTask#1").execute("");
//        new MyAsyncTask("AsyncTask#2").execute("");
//        new MyAsyncTask("AsyncTask#3").execute("");
//        new MyAsyncTask("AsyncTask#4").execute("");

        //  并行执行
        new MyAsyncTask("AsyncTask#5").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
        new MyAsyncTask("AsyncTask#6").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
        new MyAsyncTask("AsyncTask#7").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
        new MyAsyncTask("AsyncTask#8").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

    }

    private static class MyAsyncTask extends AsyncTask<String, Integer, String> {

        private String mName = "AsyncTask";

        public MyAsyncTask(String mName) {
            this.mName = mName;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return mName;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(TAG, s + "execute finish at " + TimeUtils.getCurrentDateTime(0));
        }
    }


}
