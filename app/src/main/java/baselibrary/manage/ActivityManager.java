package baselibrary.manage;

import android.app.Activity;
import android.util.Log;

import java.util.Iterator;
import java.util.Stack;

/**
 * @author yangming on 2018/7/24
 */
public final class ActivityManager {
    /**
     * Activity 栈(后进先出)
     */
    private final static Stack<Activity> mActivityStack = new Stack<>();
    private static ActivityManager mActivityManager;

    private static final String TAG = ActivityManager.class.getSimpleName();

    private ActivityManager() {
    }

    /**
     * 单一实例
     */
    public static ActivityManager getInstance() {
        if (mActivityManager == null) {
            mActivityManager = new ActivityManager();
        }
        return mActivityManager;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        synchronized (mActivityStack) {
            mActivityStack.add(activity);
        }
        Log.i(TAG, "addActivity: " + mActivityStack.size());
    }

    /**
     * 获取栈顶Activity（堆栈中最后一个压入的）
     */
    private Activity getTopActivity() {
        return mActivityStack.lastElement();
    }

    /**
     * 结束栈顶Activity（堆栈中最后一个压入的）
     */
    public void finishTopActivity() {
        Activity activity = mActivityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的 Activity
     */
    public void finishActivity(Activity activity) {
        // 先移除Activity
        removeActivity(activity);
        // Activity 不为null,并且属于未销毁状态
        if (activity != null && !activity.isFinishing()) {
            // finish到Activity
            activity.finish();
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            if (null != mActivityStack.get(i)) {
                mActivityStack.get(i).finish();
            }
        }
        mActivityStack.clear();
    }

    public void finishAllActivityIgnoreTop() {
        Activity activity = getTopActivity();
        if (activity != null) {
            finishAllActivityToIgnore(activity.getClass());
        }
    }

    /**
     * 结束全部Activity 除忽略的页面外
     */
    public void finishAllActivityToIgnore(Class<?> cls) {
        synchronized (mActivityStack) {
            // 保存新的任务,防止出现同步问题
            Stack<Activity> aStacks = new Stack<>();
            aStacks.addAll(mActivityStack);
            // 清空全部,便于后续操作处理
            mActivityStack.clear();
            // 进行遍历移除
            Iterator<Activity> iterator = aStacks.iterator();
            while (iterator.hasNext()) {
                Activity activity = iterator.next();
                // 判断是否想要关闭的Activity
                if (activity != null) {
                    if ((activity.getClass() == cls)) {
                        continue;
                    }
                    // 如果页面没有finish 则进行finish
                    if (!activity.isFinishing()) {
                        activity.finish();
                    }
                    // 删除对应的Item
                    iterator.remove();
                } else {
                    // 删除对应的Item
                    iterator.remove();
                }
            }
            // 把不符合条件的保存回去
            mActivityStack.addAll(aStacks);
            // 移除,并且清空内存
            aStacks.clear();
            aStacks = null;
        }
    }

    /**
     * 移除 Activity
     */
    private void removeActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        int index = mActivityStack.indexOf(activity);
        if (index == -1) {
            return;
        }
        mActivityStack.remove(index);
    }

    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            finishAllActivity();
            // 退出JVM(java虚拟机),释放所占内存资源,0表示正常退出(非0的都为异常退出)
            System.exit(0);
            // 从操作系统中结束掉当前程序的进程
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            Log.e(TAG, "appExit", e);
            System.exit(-1);
        }
    }


}
