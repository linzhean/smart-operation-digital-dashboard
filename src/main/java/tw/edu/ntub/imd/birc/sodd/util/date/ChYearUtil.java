package tw.edu.ntub.imd.birc.sodd.util.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.MinguoChronology;
import java.time.chrono.MinguoDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DecimalStyle;
import java.util.Locale;

public class ChYearUtil {
    private ChYearUtil() {
    }

    /**
     * Transfer AD date to minguo date.
     * 西元年 yyyyMMdd 轉 民國年 yyyMMdd
     *
     * @param localDateTime the LocalDateTime localDateTime
     * @return the string
     */
    public static String transferADDateToMinguoDate(LocalDateTime localDateTime) {
        LocalDate localDate = localDateTime.toLocalDate();
        return MinguoDate.from(localDate).format(DateTimeFormatter.ofPattern("民國yyy年MM月dd日"));
    }

    /**
     * Transfer minguo date to AD date.
     * 民國年 yyyMMdd 轉 西元年 yyyyMMdd
     *
     * @param dateString the String dateString
     * @return the string
     */
    public static LocalDateTime transferMinguoDateToADDate(String dateString) {
        Chronology chrono = MinguoChronology.INSTANCE;
        DateTimeFormatter df = new DateTimeFormatterBuilder().parseLenient()
                .appendPattern("民國yyy年MM月dd日")
                .toFormatter()
                .withChronology(chrono)
                .withDecimalStyle(DecimalStyle.of(Locale.getDefault()));

        ChronoLocalDate chDate = chrono.date(df.parse(dateString));
        return LocalDate.from(chDate).atStartOfDay();
    }
}
