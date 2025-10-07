package com.hyejin.counselor.core.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {

    /**
     * 날짜포맷에 따른 오늘날짜 반환
     * @param dateFormat
     * @return
     */
    public static String nowDateFormat(String dateFormat){
        Calendar cal = Calendar.getInstance();
        java.util.Date todate = cal.getTime();
        return new SimpleDateFormat(dateFormat).format(todate);
    }

    /**
     * 현재 시간 반환
     * yyyyMMddHHmmss
     * @return
     */
    public static String nowDate(){
        Calendar cal = Calendar.getInstance();
        java.util.Date todate = cal.getTime();
        return new SimpleDateFormat("yyyyMMddHHmmss").format(todate);
    }
}
