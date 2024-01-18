package tech.wedev.wecom.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public final static String FORMAT_DATE = "yyyy-MM-dd HH:mm:ss";
    public final static String FORMAT_DAY = "yyyy-MM-dd";
    public final static String FORMAT_TIME_NO_SPLIT = "yyyyMMddHHmmss";

    public static String formatDateToStr(Date date, String formatstr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatstr);
        return sdf.format(date);
    }

    public static Date formatStrToDate(String dateStr) throws ParseException {
        SimpleDateFormat sdf = null;
        if (dateStr.contains("-")) {
            if (dateStr.contains(":")) {
                sdf = new SimpleDateFormat(FORMAT_DATE);
            } else {
                sdf = new SimpleDateFormat(FORMAT_DAY);
            }
        }
        return sdf.parse(dateStr);
    }

    /**
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @return      返回值以秒为单位
     * @description 计算两个时间的差值（结束时间-开始时间）
     */
    public static String printDiffer(Date startDate, Date endDate) {
        long differ = endDate.getTime() - startDate.getTime();
        differ /= 1000;

        return differ >= 0 ? String.valueOf(differ) : "0";
    }

    /**
     * @param startDate  开始时间，精确到毫秒
     * @param endDate    结束时间，精确到毫秒
     * @return      返回值以天为单位
     * @description 计算两个时间的差值（结束时间-开始时间）
     */

    public static String printDays(Date startDate, Date endDate) {
        long differ = endDate.getTime() - startDate.getTime();
        differ /= 1000L * 60 * 60 * 24;

        return differ >= 0 ? String.valueOf(differ) : "0";
    }

    public static Date currentDate() {
        return new Date();
    }
}
