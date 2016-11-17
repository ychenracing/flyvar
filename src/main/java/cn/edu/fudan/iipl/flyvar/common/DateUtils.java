/**
 * ychen. Copyright (c) 2016年10月23日.
 */
package cn.edu.fudan.iipl.flyvar.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日期工具类
 * 
 * @author racing
 * @version $Id: DateUtils.java, v 0.1 2016年10月23日 下午4:05:29 racing Exp $
 */
public class DateUtils {

    private static final Logger logger           = LoggerFactory.getLogger(DateUtils.class);

    private static final String GENERAL_PATTERN  = "yyyy-MM-dd HH:mm:ss";

    /** 用于filename的日期格式 */
    private static final String FILENAME_PATTERN = "yyyyMMddHHmmss";

    public static String formatGeneral(Date date) {
        DateFormat df = new SimpleDateFormat(GENERAL_PATTERN);
        return df.format(date);
    }

    public static String formatFilename(Date date) {
        DateFormat df = new SimpleDateFormat(FILENAME_PATTERN);
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
            logger.error(
                "Parse date to certain pattern error! dataStr=" + dateStr + " pattern=" + pattern,
                e);
        }
        return result;
    }

    public static Date current() {
        return new Date();
    }

    public static Date addDay(Date date, int count) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, count);
        return c.getTime();
    }

    public static void main(String[] args) {
        System.out.println(formatGeneral(addDay(current(), -30)));
    }

}
