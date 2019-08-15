package baselibrary.util;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import java.lang.reflect.Field;

/**
 * @author yangming on 2019/6/11
 * 屏幕适配方案
 */
public class DensityUtils {

    /**
     * 根据需要以高或者宽去适配
     */
    private static final int WIDTH = 1;
    private static final int HEIGHT = 2;
    /**
     * 根据UI设计图去更改
     */
    private static final float DEFAULT_WIDTH = 375f;
    private static final float DEFAULT_HEIGHT = 640f;

    /**
     * 系统默认Density
     */
    private static float appDensity;
    /**
     * 字体的缩放因子，正常情况下和density相等，但是调节系统字体大小后会改变这个值
     */
    private static float appScaledDensity;

    private static DisplayMetrics appDisplayMetrics;

    /**
     * 在 Application 中调用，存储默认屏幕密度
     */
    public static void initAppDensity(@NonNull Application application) {
        // 获取application的DisplayMetrics
        appDisplayMetrics = application.getResources().getDisplayMetrics();

        if (appDensity == 0) {
            // 初始化屏幕密度和字体密度
            appDensity = appDisplayMetrics.density;
            appScaledDensity = appDisplayMetrics.scaledDensity;

            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    // 字体改变后,将appScaledDensity重新赋值
                    if (newConfig != null && newConfig.fontScale > 0) {
                        appScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {
                }
            });
        }
    }

    /**
     * 默认情况下根据宽度去适配
     */
    public static void setDefault(Activity activity) {
        setOrientation(activity, WIDTH);
    }

    public static void setOrientation(@NonNull Activity activity, int orientation) {
        float targetDensity;
        if (orientation == WIDTH) {
            // 根据宽去适配
            targetDensity = appDisplayMetrics.widthPixels / DEFAULT_WIDTH;
        } else {
            targetDensity = appDisplayMetrics.heightPixels / DEFAULT_HEIGHT;
        }

        float targetScaledDensity = targetDensity * (appScaledDensity / appDensity);
        int targetDensityDpi = (int) (targetDensity * 160);

        // 最后在这里将修改过后的值赋给系统参数,只修改Activity的density值
        DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
        setBitmapDefaultDensity(activityDisplayMetrics.densityDpi);
    }

    /**
     * 设置 Bitmap 的默认屏幕密度
     * 由于 Bitmap 的屏幕密度是读取配置的，导致修改未被启用
     * 所有，放射方式强行修改
     *
     * @param defaultDensity 屏幕密度
     */
    private static void setBitmapDefaultDensity(int defaultDensity) {
        //获取单个变量的值
        Class clazz;
        try {
            clazz = Class.forName("android.graphics.Bitmap");
            Field field = clazz.getDeclaredField("sDefaultDensity");
            field.setAccessible(true);
            field.set(null, defaultDensity);
            field.setAccessible(false);
        } catch (ClassNotFoundException e) {
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置屏幕密度
     */
    public static void resetAppOrientation(@NonNull Activity activity) {
        DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = appDensity;
        activityDisplayMetrics.scaledDensity = appScaledDensity;
        activityDisplayMetrics.densityDpi = (int) (appDensity * 160);
        setBitmapDefaultDensity(activityDisplayMetrics.densityDpi);
    }

}
