package tw.edu.ntub.imd.birc.sodd.util.date;

import org.apache.commons.lang3.time.FastDateFormat;
import tw.edu.ntub.imd.birc.sodd.exception.DateParseException;
import tw.edu.ntub.imd.birc.sodd.exception.NullParameterException;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    private DateUtils() {

    }

    public static LocalDateTimeUtils.LocalDateTimeWrapper createWrapper() {
        return new LocalDateTimeUtils.LocalDateTimeWrapper();
    }

    public static LocalDateTimeUtils.LocalDateTimeWrapper createWrapper(Date date) throws NullParameterException {
        return new LocalDateTimeUtils.LocalDateTimeWrapper(date);
    }

    public static boolean isCurrentYear(int year) {
        LocalDate localDate = LocalDateUtils.now();
        return localDate.getYear() == year;
    }

    public static String format(Date date, DatePatternBuilder builder) {
        FastDateFormat dateFormat = FastDateFormat.getInstance(builder.build());
        return date != null ? dateFormat.format(date) : "";
    }

    public static Date parseIgnoreException(String dateString) {
        try {
            return parse(dateString);
        } catch (DateParseException e) {
            return null;
        }
    }

    public static Date parse(String dateString) throws DateParseException {
        return parse(dateString, DatePatternBuilder.DEFAULT_PATTERN_ARRAY);
    }

    private static Date parse(String dateString, String... patterArray) throws DateParseException {
        try {
            return org.apache.commons.lang3.time.DateUtils.parseDate(dateString, patterArray);
        } catch (ParseException ignore) {
            throw new DateParseException(dateString);
        }
    }

    public static Date parseIgnoreException(String dateString, DatePatternBuilder builder) {
        try {
            return parse(dateString, builder);
        } catch (DateParseException ignore) {
            return null;
        }
    }

    public static Date parse(String dateString, DatePatternBuilder builder) throws DateParseException {
        try {
            return org.apache.commons.lang3.time.DateUtils.parseDate(dateString, builder.build());
        } catch (ParseException e) {
            throw new DateParseException(dateString, builder.build());
        }
    }

    public interface DateWrapper<D> {
        int getYear();

        DateWrapper<D> setYear(int amount);

        DateWrapper<D> addYear(int amount);

        int getMonth();

        DateWrapper<D> setMonth(int amount);

        DateWrapper<D> addMonth(int amount);

        int getDay();

        DateWrapper<D> setDay(int amount);

        DateWrapper<D> addDay(int amount);

        long getFullMillisecond();

        String format(DatePatternBuilder builder);

        D get();

        Date toDate();

        boolean isBefore(D d);

        boolean isAfter(D d);

        boolean isEqual(D d);

        default Calendar toCalendar() {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(toDate());
            return calendar;
        }

        default boolean isLargeYear(int year) {
            return getYear() > year;
        }

        default boolean isLargeOrEqualYear(int year) {
            return getYear() >= year;
        }

        default boolean isBetweenYear(int firstYear, int lastYear) {
            return getYear() >= firstYear && getYear() <= lastYear;
        }

        default boolean isLargeMonth(int month) {
            return getMonth() > month;
        }

        default boolean isLargeOrEqualMonth(int month) {
            return getMonth() >= month;
        }

        default boolean isBetweenMonth(int firstMonth, int lastMonth) {
            return getMonth() >= firstMonth && getMonth() <= lastMonth;
        }

        default boolean isLargeDay(int day) {
            return getDay() > day;
        }

        default boolean isLargeOrEqualDay(int day) {
            return getDay() >= day;
        }

        default boolean isBetweenDay(int firstDay, int lastDay) {
            return getDay() >= firstDay && getDay() <= lastDay;
        }
    }

    public interface TimeWrapper<T> {
        int getHour();

        TimeWrapper<T> setHour(int amount);

        TimeWrapper<T> addHour(int amount);

        int getMinute();

        TimeWrapper<T> setMinute(int amount);

        TimeWrapper<T> addMinute(int amount);

        int getSecond();

        TimeWrapper<T> setSecond(int amount);

        TimeWrapper<T> addSecond(int amount);

        int getMillisecond();

        TimeWrapper<T> setMillisecond(int amount);

        TimeWrapper<T> addMillisecond(int amount);

        String format(DatePatternBuilder builder);

        T get();

        boolean isBefore(T t);

        boolean isAfter(T t);

        boolean isEqual(T t);

        default boolean isLargeHour(int hour) {
            return getHour() > hour;
        }

        default boolean isLargeOrEqualHour(int hour) {
            return getHour() >= hour;
        }

        default boolean isBetweenHour(int firstHour, int lastHour) {
            return getHour() >= firstHour && getHour() <= lastHour;
        }

        default boolean isLargeMinute(int minute) {
            return getMinute() > minute;
        }

        default boolean isLargeOrEqualMinute(int minute) {
            return getMinute() >= minute;
        }

        default boolean isBetweenMinute(int firstMinute, int lastMinute) {
            return getMinute() >= firstMinute && getMinute() <= lastMinute;
        }

        default boolean isLargeSecond(int second) {
            return getSecond() > second;
        }

        default boolean isLargeOrEqualSecond(int second) {
            return getSecond() >= second;
        }

        default boolean isBetweenSecond(int firstSecond, int lastSecond) {
            return getSecond() >= firstSecond && getSecond() <= lastSecond;
        }

        default boolean isLargeMillisecond(int millisecond) {
            return getMillisecond() > millisecond;
        }

        default boolean isLargeOrEqualMillisecond(int millisecond) {
            return getMillisecond() >= millisecond;
        }

        default boolean isBetweenMillisecond(int firstMillisecond, int lastMillisecond) {
            return getMillisecond() >= firstMillisecond && getMillisecond() <= lastMillisecond;
        }
    }
}
