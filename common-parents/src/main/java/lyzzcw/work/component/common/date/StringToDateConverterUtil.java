package lyzzcw.work.component.common.date;


import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringToDateConverterUtil {
    private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private static final String shortDateFormat = "yyyy-MM-dd";
    private static final String shortMonthFormat = "yyyy-MM";
    private static final String shortYearFormat = "yyyy";
    private static final String dateFormat2 = "yyyy/MM/dd HH:mm:ss";
    private static final String shortDateFormat2 = "yyyy/MM/dd";

    public static Date convert(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        source = source.trim();
        try {
            SimpleDateFormat formatter;
            if (source.contains("-")) {
                if (source.contains(":")) {
                    formatter = new SimpleDateFormat(dateFormat);
                } else {
                    if(source.length() < 10){
                        formatter = new SimpleDateFormat(shortMonthFormat);
                    }else{
                        formatter = new SimpleDateFormat(shortDateFormat);
                    }
                }
                Date dtDate = formatter.parse(source);
                return dtDate;
            } else if (source.contains("/")) {
                if (source.contains(":")) {
                    formatter = new SimpleDateFormat(dateFormat2);
                } else {
                    formatter = new SimpleDateFormat(shortDateFormat2);
                }
                Date dtDate = formatter.parse(source);
                return dtDate;
            }else if(source.length() == 4){
                formatter = new SimpleDateFormat(shortYearFormat);
                Date dtDate = formatter.parse(source);
                return dtDate;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
