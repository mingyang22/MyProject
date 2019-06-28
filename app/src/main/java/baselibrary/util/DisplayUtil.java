package baselibrary.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * @author yangming on 2018/7/20
 */
public class DisplayUtil {

    /** 获取屏幕宽度 */
    public static int getDisplayWidth(Context context) {
        if (context != null) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            int w_screen = dm.widthPixels;
            // int h_screen = dm.heightPixels;
            return w_screen;
        }
        return 720;
    }

    /**
     * 获取屏幕高度
     */
    public static int getDisplayHeight(Context context) {
        if (context != null) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            int w_screen = dm.heightPixels;
            // int h_screen = dm.heightPixels;
            return w_screen;
        }
        return 1280;
    }

	 /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     * 
     * @param pxValue
     *            （DisplayMetrics类中属性density）
     * @return
     */ 
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int) (pxValue / scale + 0.5f); 
    } 
   
    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     * 
     * @param dipValue
     *            （DisplayMetrics类中属性density）
     * @return
     */ 
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int) (dipValue * scale + 0.5f); 
    } 
   
    /**
     * 将px值转换为sp值，保证文字大小不变
     * 
     * @param pxValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */ 
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (pxValue / fontScale + 0.5f); 
    } 
   
    /**
     * 将sp值转换为px值，保证文字大小不变
     * 
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */ 
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (spValue * fontScale + 0.5f); 
    } 
    
    
}
