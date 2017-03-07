package com.project.tangaofeng.actionbar_demo.Manager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tangaofeng on 16/7/1.
 */
public class DateHelper {

    //把日期转为字符串
    public static String ConverToString(Date date,String format)
    {
        DateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }
    //把字符串转为日期
    public static Date ConverToDate(String strDate,String format) throws Exception
    {
        DateFormat df = new SimpleDateFormat(format);
        return df.parse(strDate);
    }
}
