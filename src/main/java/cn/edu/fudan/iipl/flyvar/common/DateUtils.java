/**
 * ychen. Copyright (c) 2016年10月23日.
 */
package cn.edu.fudan.iipl.flyvar.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 * 
 * @author racing
 * @version $Id: DateUtils.java, v 0.1 2016年10月23日 下午4:05:29 racing Exp $
 */
public class DateUtils {

    private static final String GENERAL_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String formatGeneral(Date date) {
        DateFormat df = new SimpleDateFormat(GENERAL_PATTERN);
        return df.format(date);
    }

    public static String format(Date date, String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    public static Date parse(String dateStr, String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        Date result = new Date();
        try {
            result = df.parse(dateStr);
        } catch (ParseException e) {
        }
        return result;
    }

    public static Date currentDate() {
        return new Date();
    }

}
