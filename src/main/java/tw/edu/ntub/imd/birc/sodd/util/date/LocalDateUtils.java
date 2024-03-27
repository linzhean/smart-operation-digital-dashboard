package tw.edu.ntub.imd.birc.sodd.util.date;

import tw.edu.ntub.imd.birc.sodd.exception.DateParseException;
import tw.edu.ntub.imd.birc.sodd.exception.NullParameterException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class LocalDateUtils {
    private LocalDateUtils() {

    }

    public static LocalDateWrapper createWrapper() {
        return new LocalDateWrapper();
    }

    public static LocalDateWrapper createWrapper(LocalDate localDate) throws NullParameterException {
        return new LocalDateWrapper(localDate);
    }

    public static LocalDate now() {
        return LocalDate.now();
    }

    public static String format(LocalDate localDate, DatePatternBuilder builder) {
        return localDate != null ? localDate.format(builder.buildFormatter()) : "";
    }

    public static LocalDate parseIgnoreException(String dateString) {
        try {
            return parse(dateString);
        } catch (DateParseException e) {
            return null;
        }
    }

    public static LocalDate parse(String dateString) throws DateParseException {
        for (DatePatternBuilder builder : DatePatternBuilder.DEFAULT_BUILDER_ARRAY) {
            try {
                return LocalDate.parse(dateString, builder.buildFormatter());
            } catch (DateTimeParseException ignored) {

            }
        }
        throw new DateParseException(dateString);
    }

    public static LocalDate parseIgnoreException(String dateString, DatePatternBuilder builder) {
        try {
            return parse(dateString, builder);
        } catch (DateParseException e) {
            return null;
        }
    }

    public static LocalDate parse(String dateString, DatePatternBuilder builder) throws DateParseException {
        try {
            return LocalDate.parse(dateString, builder.buildFormatter());
        } catch (DateTimeParseException ignored) {
            throw new DateParseException(dateString, builder.build());
        }
    }

    public static class LocalDateWrapper implements DateUtils.DateWrapper<LocalDate> {
        private LocalDate localDate;

        public LocalDateWrapper() {
            this.localDate = now();
        }

        public LocalDateWrapper(LocalDate localDate) throws NullParameterException {
            if (localDate == null) {
                throw new NullParameterException();
            }
            this.localDate = localDate;
        }

        private LocalDateWrapper newInstance(LocalDate localDate) {
            try {
                return new LocalDateWrapper(localDate);
            } catch (NullParameterException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public int getYear() {
            return localDate.getYear();
        }

        @Override
        public LocalDateWrapper setYear(int amount) {
            return newInstance(localDate.withYear(amount));
        }

        @Override
        public LocalDateWrapper addYear(int amount) {
            return newInstance(localDate.plusYears(amount));
        }

        @Override
        public int getMonth() {
            return localDate.getMonthValue();
        }

        @Override
        public LocalDateWrapper setMonth(int amount) {
            return newInstance(localDate.withMonth(amount));
        }

        @Override
        public LocalDateWrapper addMonth(int amount) {
            return newInstance(localDate.plusMonths(amount));
        }

        @Override
        public int getDay() {
            return localDate.getDayOfMonth();
        }

        @Override
        public LocalDateWrapper setDay(int amount) {
            return newInstance(localDate.withDayOfMonth(amount));
        }

        @Override
        public LocalDateWrapper addDay(int amount) {
            return newInstance(localDate.plusDays(amount));
        }

        @Override
        public long getFullMillisecond() {
            ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
            Instant instant = zonedDateTime.toInstant();
            return instant.toEpochMilli();
        }

        @Override
        public String format(DatePatternBuilder builder) {
            return LocalDateUtils.format(localDate, builder);
        }

        @Override
        public LocalDate get() {
            return localDate;
        }

        @Override
        public Date toDate() {
            ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
            return Date.from(zonedDateTime.toInstant());
        }

        @Override
        public boolean isBefore(LocalDate localDate) {
            return this.localDate.isBefore(localDate);
        }

        @Override
        public boolean isAfter(LocalDate localDate) {
            return this.localDate.isAfter(localDate);
        }

        @Override
        public boolean isEqual(LocalDate localDate) {
            return this.localDate.isEqual(localDate);
        }
    }
}
