package com.yangming.myproject.thread;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.yangming.myproject.R;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yangming 2019/10/15
 * 三种线程创建方式、Handler创建方式
 */
public class ThreadActivity extends AppCompatActivity {

    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv1.setText("我是继承Thread方式创建的线程");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        ButterKnife.bind(this);

        // 继承Thread类创建线程
        MyThread thread1 = new MyThread();
        thread1.start();

        // 实现Runnable接口创建线程
        MyRunnable runnable = new MyRunnable();
        Thread thread2 = new Thread(runnable);
        thread2.start();

        MyCallable callable = new MyCallable();
        FutureTask<Integer> futureTask = new FutureTask<>(callable);
        new Thread(futureTask).start();
        try {
            tv3.setText("我是Callable方式创建的线程，返回值：" + futureTask.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            handler.sendEmptyMessage(0);
        }
    }

    public class MyRunnable implements Runnable {

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    tv2.setText("我是实现Runnable接口方式创建的线程");
                }
            });
        }
    }

    public class MyCallable implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            return 20;
        }
    }

}
