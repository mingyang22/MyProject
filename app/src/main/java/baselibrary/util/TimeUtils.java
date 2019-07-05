package baselibrary.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author yangming on 2018/7/27
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtils {

    /**
     * 获取当前日期+时间
     */
    public static String getCurrentDateTime(int tag) {
        String strFormatter;
        switch (tag) {
            case 1:
                strFormatter = "yyyy-MM-dd HH:mm";
                break;
            case 2:
                strFormatter = "yyyy/MM/dd HH:mm:ss";
                break;
            case 3:
                strFormatter = "yyyy/MM/dd HH:mm";
                break;
            case 4:
                strFormatter = "yyyy-MM-dd-HH-mm-ss";
                break;
            default:
                strFormatter = "yyyy-MM-dd HH:mm:ss";
                break;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(strFormatter);
        // 获取当前时间
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    /**
     * 获取当天日期 yyyy-MM-dd
     */
    public static String getTodayDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 获取当天日期 yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 获取当天之前26天日期
     */
    public static String getBefore26DateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        //过去七天
        c.add(Calendar.MONTH, 0);
        //设置为1号,当前日期既为本月第一天
        c.set(Calendar.DAY_OF_MONTH, 1);
        Date d = c.getTime();
        return sdf.format(new Date(d.getTime() - 3 * 24 * 3600 * 1000));
    }

    /**
     * 获取当天之后3天日期
     */
    public static String getAfter3DateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(date.getTime() + 3 * 24 * 3600 * 1000));
    }


    /**
     * 获取当天之前6天日期
     */
    public static String getBefore7DateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(date.getTime() - 6 * 24 * 3600 * 1000));
    }

    /**
     * 格式化日期
     */
    public static String getTodayDate(Date lastDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(lastDate);
    }

    /**
     * 将毫秒级时间装换为规定格式
     */
    public static String convertLongTimeToDateTime(long time) {
        String str = "";
        if (time > 100) {
            String strFomatter = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat formatter = new SimpleDateFormat(strFomatter);
            Date curDate = new Date(time);
            str = formatter.format(curDate);
            return str;
        } else {
            return str;
        }
    }

    /**
     * 将毫秒转化为 分钟：秒 的格式
     */
    public static String formatTime(long millisecond) {
        int hour;//小时
        int minute;//分钟
        int second;//秒数
        hour = (int) ((millisecond / 1000) / 60 / 60);
        minute = (int) ((millisecond / 1000 / 60) % 60);
        second = (int) ((millisecond / 1000) % 60);
        if (minute < 10) {
            if (second < 10) {
                return "0" + hour + ":" + "0" + minute + ":" + "0" + second;
            } else {
                return "0" + hour + ":" + "0" + minute + ":" + second;
            }
        } else {
            if (second < 10) {
                return "0" + hour + ":" + minute + ":" + "0" + second;
            } else {
                return "0" + hour + ":" + minute + ":" + second;
            }
        }
    }

    /**
     * 将时间戳转换为日期时间字符串
     */
    public static String formatTimeToDateTime(long timeStamp, int tag) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        switch (tag) {
            case 0:
                pattern = "yyyy-MM-dd HH:mm:ss";
                break;
            case 1:
                pattern = "yyyy-MM-dd HH:mm";
                break;
            case 2:
                pattern = "yyyy-MM-dd";
                break;
            case 3:
                pattern = "HH:mm:ss";
                break;
            case 4:
                pattern = "HH:mm";
                break;
            default:
                break;
        }
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(timeStamp);

    }

    /**
     * 两次点击间隔不能少于1000ms
     */
    private static long lastClickTime;
    private static final int MIN_DELAY_TIME = 1000;

    /**
     * 是否快速点击
     */
    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    /**
     * 获取前一天日期
     */
    public static Date getLastDate(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //今天的时间减一天
        calendar.add(Calendar.DAY_OF_MONTH, -i);
        date = calendar.getTime();
        return date;
    }

    /**
     * 获取当前秒
     */
    public static int getCurrentSecond() {
        return (int) (System.currentTimeMillis() / 1000);
    }

}
