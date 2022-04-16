package com.ylesb.demo.utils;
/**
 * @title: TimeUtil
 * @projectName demo
 * @description: TODO
 * @author White
 * @site : [www.ylesb.com]
 * @date 2022/4/159:48
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @className    : TimeUtil
 * @description  : [描述说明该类的功能]  
 * @author       : [XuGuangchao]
 * @site         : [www.ylesb.com]
 * @version      : [v1.0]
 * @createTime   : [2022/4/15 9:48]
 * @updateUser   : [XuGuangchao]
 * @updateTime   : [2022/4/15 9:48]
 * @updateRemark : [描述说明本次修改内容] 
 */
public class TimeUtil {

    public static String getUtcTime() {
        DateFormat dtf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dtf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String format = dtf.format(new Date());
        return format;
    }

    public static String getGmtTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        Calendar calendar = Calendar.getInstance();
        sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // 设置时区为GMT
        String dateStr = sdf.format(calendar.getTime());
        return dateStr;
    }

}
