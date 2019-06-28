package com.yangming.myproject.databinding;

import android.databinding.BindingConversion;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yangming on 2019/4/1
 */
public class Utils {
    public static String getName(Swordsman swordsman) {
        return swordsman.getName();
    }

    @BindingConversion
    public static String convertDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

}
