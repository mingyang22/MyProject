package baselibrary.util;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.yangming.myproject.MyApplication;

/**
 * @author yangming on 2019/6/20
 */
public class SpfUtils {

    private static volatile SpfUtils spfUtils;
    private SharedPreferences spf;
    private Editor editor;

    @SuppressLint("CommitPrefEdits")
    private SpfUtils() {
        spf = PreferenceManager.getDefaultSharedPreferences(MyApplication.getApplication());
        editor = spf.edit();
    }

    public static SpfUtils getInstance() {
        if (spfUtils == null) {
            synchronized (SpfUtils.class) {
                if (spfUtils == null) {
                    spfUtils = new SpfUtils();
                }
            }
        }
        return spfUtils;
    }

    /**
     * 保存 String
     */
    public void save(String key, String value) {
        editor.putString(key, value).apply();
    }

    /**
     * 保存 int
     */
    public void save(String key, int value) {
        editor.putInt(key, value).apply();
    }

    /**
     * 保存 float
     */
    public void save(String key, float value) {
        editor.putFloat(key, value).apply();
    }

    /**
     * 保存 boolean
     */
    public void save(String key, boolean value) {
        editor.putBoolean(key, value).apply();
    }

    /**
     * 保存 long
     */
    public void save(String key, long value) {
        editor.putLong(key, value).apply();
    }

    /**
     * 读取缓存文件中某指定节点的数据
     */
    public String readString(String key, String defValue) {
        return spf.getString(key, defValue);
    }

    /**
     * 读取缓存文件中某指定节点的数据
     */
    public float readFloat(String key, float defValue) {
        return spf.getFloat(key, defValue);
    }

    /**
     * 读取缓存文件中某指定节点的数据
     */
    public long readLong(String key, long defValue) {
        return spf.getLong(key, defValue);
    }

    /**
     * 读取缓存文件中某指定节点的数据
     */
    public boolean readBoolean(String key, boolean defValue) {
        return spf.getBoolean(key, defValue);
    }

    /**
     * 读取缓存文件中某指定节点的数据
     */
    public int readInt(String key, int defValue) {
        return spf.getInt(key, defValue);
    }


    /**
     * 清除当前缓存文件的全部数据
     */
    public void clear() {
        editor.clear().apply();
    }

    /**
     * 移除某指定的key
     */
    public void remove(String key) {
        editor.remove(key).apply();
    }
}
