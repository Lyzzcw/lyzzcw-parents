package lyzzcw.work.component.common.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public class DateUtils {

    /**
     * DateTimeFormatter 代替 SimpleDateFormat，官方给出的解释：simple beautiful strong immutable thread-safe
     */

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static ThreadLocal<DateFormat>  DATE_FORMAT= new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private static Date parse(String date, ThreadLocal<DateFormat> df){
        try {
            return df.get().parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            df.remove();
        }
        return null;
    }

    /**
     * LocalDate -> Date
     * @param localDate
     * @return Date
     */
    public static Date LocalDateToDate(LocalDate localDate){
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date -> LocalDate
     * @param date
     * @return LocalDate
     */
    public static LocalDate DateToLocalDate(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * LocalDateTime -> Date
     * @param localDateTime
     * @return Date
     */
    public static Date LocalDateTimeToDate(LocalDateTime localDateTime){
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date -> LocalDateTime
     * @param date
     * @return LocalDateTime
     */
    public static LocalDateTime DataToLocalDateTime(Date date){
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * localDateTime -> millisecond
     * @param localDateTime
     * @return millisecond
     */
    public static Long LocalDateTimeToMS(LocalDateTime localDateTime){
        //方法一
        return localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        //方法二
//        return localDateTime.toEpochSecond(ZoneOffset.ofHours(8)) * 1000 + localDateTime.getNano() / 1000_000;
    }

    /**
     * millisecond -> localDateTime
     * @param millisecond
     * @return localDateTime
     */
    public static LocalDateTime MSToLocalDateTime(Long millisecond){
        //方法一
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millisecond), ZoneId.systemDefault());
        //方法二
//        return LocalDateTime.ofEpochSecond(millisecond / 1000, (int)(millisecond % 1000 * 1000_000), ZoneOffset.ofHours(8));
    }

    public static void main1(String[] args) {

        for(int i=0;i<10;i++){
            new Thread(
                    ()->{
                        System.out.println(Thread.currentThread().getName()+""+DateUtils.parse("2022-03-17 00:00:00",DateUtils.DATE_FORMAT));
                    }
            ).start();
        }
        for(int i=0;i<10;i++){
            new Thread(
                    ()->{
                        System.out.println(Thread.currentThread().getName()+""+DateUtils.parse("2021-03-17 00:00:00",DateUtils.DATE_FORMAT));
                    }
            ).start();
        }
        for(int i=0;i<10;i++){
            new Thread(
                    ()->{
                        System.out.println(Thread.currentThread().getName()+""+DateUtils.parse("2020-03-17 00:00:00",DateUtils.DATE_FORMAT));
                    }
            ).start();
        }

    }

    public static void main(String[] args) {
        LocalDate localDate = LocalDate.parse("2022-04-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.println(localDate);
        LocalDate endTime = localDate.with(TemporalAdjusters.lastDayOfMonth());
        System.out.println(endTime.toString());

        Date dateTime = Date.from(endTime.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        LocalDate date = LocalDate.parse("2020-07-07");
        //获取这个月的第一个周末的时间
        System.out.println(date.with(TemporalAdjusters.dayOfWeekInMonth(1, DayOfWeek.SUNDAY)));
        //输出: 2020-07-05

        //获取上个月的最后一周末的时间
        System.out.println(date.with(TemporalAdjusters.dayOfWeekInMonth(0, DayOfWeek.SUNDAY)));
        //输出: 2020-06-28

        //获取这个月的倒数第一个周末的时间
        System.out.println(date.with(TemporalAdjusters.dayOfWeekInMonth(-1, DayOfWeek.SUNDAY)));
        //输出: 2020-07-26

        //获取这个月的第一个周末的时间,上面的dayOfWeekInMonth更灵活,可以定义第几周
        System.out.println(date.with(TemporalAdjusters.firstInMonth(DayOfWeek.SUNDAY)));
        //输出: 2020-07-05

        //明年的第一天
        System.out.println(date.with(TemporalAdjusters.firstDayOfNextYear()));
        //输出: 2021-01-01

        //获取下个周5的时间
        System.out.println(date.with(TemporalAdjusters.next(DayOfWeek.FRIDAY)));
        //输出: 2020-07-10

        //获取本月最后一天
        System.out.println(date.with(TemporalAdjusters.lastDayOfMonth()));
        //输出: 2020-07-31

        //获取本月第一天
        System.out.println(date.with(TemporalAdjusters.firstDayOfMonth()));
        //输出: 2020-07-01

        System.out.println(MSToLocalDateTime(1650556800000l).toLocalDate());

        //年起始日期
        LocalDate startYear = localDate.with(TemporalAdjusters.firstDayOfYear());
        System.out.println("startDayOfYear:"+startYear);
        //年结束日期
        LocalDate endYear = localDate.with(TemporalAdjusters.lastDayOfYear());
        System.out.println("endDayOfYear:"+endYear);
    }
}
