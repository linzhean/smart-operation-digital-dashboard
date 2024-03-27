package tw.edu.ntub.imd.birc.sodd.util.date;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Stream;

public class DatePatternBuilder {
    public static final DatePatternBuilder DEFAULT_DATE = createWithPattern("yyyy/MM/dd");
    public static final DatePatternBuilder DEFAULT_TIME = createWithPattern("HH:mm:ss");
    public static final DatePatternBuilder DEFAULT_DATE_TIME = createWithPattern("yyyy/MM/dd HH:mm:ss");
    public static final DatePatternBuilder TA_DATE_TIME = createWithPattern("yyyy/MM/dd HH:mm");
    public static final DatePatternBuilder[] DEFAULT_TIME_BUILDER_ARRAY = new DatePatternBuilder[]{
            DEFAULT_TIME,
            createWithPattern("HH:mm"),
            createWithPattern("HH:mm:ss.SSS")
    };
    public static final DatePatternBuilder[] DEFAULT_BUILDER_ARRAY = new DatePatternBuilder[]{
            DEFAULT_DATE, DEFAULT_DATE_TIME, TA_DATE_TIME,
            createWithPattern("yyyy-MM-dd"),
            createWithPattern("yyyy/MM/dd HH:mm"),
            createWithPattern("yyyy-MM-dd HH:mm"),
            createWithPattern("yyyy/MM/dd HH:mm:ss"),
            createWithPattern("yyyy-MM-dd HH:mm:ss"),
            createWithPattern("yyyy/MM/dd HH:mm:ss.SSS"),
            createWithPattern("yyyy-MM-dd HH:mm:ss.SSS"),
            createWithPattern("yyyy/MM/dd aKK:mm"),
            createWithPattern("yyyy-MM-dd aKK:mm"),
            createWithPattern("yyyy/MM/dd aKK:mm:ss"),
            createWithPattern("yyyy-MM-dd aKK:mm:ss"),
            createWithPattern("yyyy/MM/dd aKK:mm:ss.SSS"),
            createWithPattern("yyyy-MM-dd aKK:mm:ss.SSS")
    };
    public static final String[] DEFAULT_PATTERN_ARRAY =
            Stream.concat(Arrays.stream(DEFAULT_BUILDER_ARRAY), Arrays.stream(DEFAULT_TIME_BUILDER_ARRAY))
                    .map(DatePatternBuilder::build)
                    .toArray(String[]::new);

    private StringBuilder patternStringBuilder = new StringBuilder(20);

    private DatePatternBuilder() {

    }

    public static DatePatternBuilder create() {
        return new DatePatternBuilder();
    }

    private static DatePatternBuilder createWithPattern(String pattern) {
        DatePatternBuilder builder = new DatePatternBuilder();
        builder.patternStringBuilder.append(pattern);
        return builder;
    }

    public DatePatternBuilder appendYear(int textCount) {
        patternStringBuilder.append("y".repeat(textCount));
        return this;
    }

    public DatePatternBuilder appendMonth(int textCount) {
        patternStringBuilder.append("M".repeat(textCount));
        return this;
    }

    public DatePatternBuilder appendShortMonthName() {
        patternStringBuilder.append("MMM");
        return this;
    }

    public DatePatternBuilder appendFullMonthName() {
        patternStringBuilder.append("MMMM");
        return this;
    }

    public DatePatternBuilder appendDay(int textCount) {
        patternStringBuilder.append("d".repeat(textCount));
        return this;
    }

    public DatePatternBuilder appendShortWeekName() {
        patternStringBuilder.append("E");
        return this;
    }

    public DatePatternBuilder appendFullWeekName() {
        patternStringBuilder.append("EEEE");
        return this;
    }

    public DatePatternBuilder appendAMPM() {
        patternStringBuilder.append("a");
        return this;
    }

    public DatePatternBuilder append12Hour(int textCount) {
        patternStringBuilder.append("K".repeat(textCount));
        return this;
    }

    public DatePatternBuilder append24Hour(int textCount) {
        patternStringBuilder.append("H".repeat(textCount));
        return this;
    }

    public DatePatternBuilder appendMinute(int textCount) {
        patternStringBuilder.append("m".repeat(textCount));
        return this;
    }

    public DatePatternBuilder appendSecond(int textCount) {
        patternStringBuilder.append("s".repeat(textCount));
        return this;
    }

    public DatePatternBuilder appendMillisecondSecond(int textCount) {
        patternStringBuilder.append("S".repeat(textCount));
        return this;
    }

    public DatePatternBuilder appendSlash() {
        return appendNormalText("/");
    }

    public DatePatternBuilder appendNormalText(String text) {
        patternStringBuilder.append(text);
        return this;
    }

    public DatePatternBuilder appendDash() {
        return appendNormalText("-");
    }

    public DatePatternBuilder appendSpace() {
        return appendNormalText(" ");
    }

    public DatePatternBuilder appendSpace(int textCount) {
        return appendNormalText(" ".repeat(textCount));
    }

    public DatePatternBuilder appendColon() {
        return appendNormalText(":");
    }

    public String build() {
        return patternStringBuilder.toString();
    }

    public DateTimeFormatter buildFormatter() {
        return DateTimeFormatter.ofPattern(build());
    }
}
