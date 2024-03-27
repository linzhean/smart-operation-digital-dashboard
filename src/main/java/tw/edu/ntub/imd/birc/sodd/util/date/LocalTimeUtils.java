package tw.edu.ntub.imd.birc.sodd.util.date;

import tw.edu.ntub.imd.birc.sodd.exception.DateParseException;
import tw.edu.ntub.imd.birc.sodd.exception.NullParameterException;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

public class LocalTimeUtils {
    private LocalTimeUtils() {

    }

    public static LocalTimeWrapper createWrapper() {
        return new LocalTimeWrapper();
    }

    public static LocalTimeWrapper createWrapper(LocalTime localTime) throws NullParameterException {
        return new LocalTimeWrapper(localTime);
    }

    public static LocalTime now() {
        return LocalTime.now();
    }

    public static String format(LocalTime localTime, DatePatternBuilder builder) {
        return localTime != null ? localTime.format(builder.buildFormatter()) : "";
    }

    public static LocalTime parseIgnoreException(String timeString) {
        try {
            return parse(timeString);
        } catch (DateParseException e) {
            return null;
        }
    }

    public static LocalTime parse(String timeString) throws DateParseException {
        for (DatePatternBuilder builder : DatePatternBuilder.DEFAULT_TIME_BUILDER_ARRAY) {
            try {
                return LocalTime.parse(timeString, builder.buildFormatter());
            } catch (DateTimeParseException ignored) {

            }
        }
        throw new DateParseException(timeString);
    }

    public static LocalTime parseIgnoreException(String timeString, DatePatternBuilder builder) {
        try {
            return parse(timeString, builder);
        } catch (DateParseException e) {
            return null;
        }
    }

    public static LocalTime parse(String timeString, DatePatternBuilder builder) throws DateParseException {
        try {
            return LocalTime.parse(timeString, builder.buildFormatter());
        } catch (DateTimeParseException ignored) {
            throw new DateParseException(timeString, builder.build());
        }
    }

    public static class LocalTimeWrapper implements DateUtils.TimeWrapper<LocalTime> {
        private LocalTime localTime;

        public LocalTimeWrapper() {
            this.localTime = now();
        }

        public LocalTimeWrapper(LocalTime localTime) throws NullParameterException {
            if (localTime == null) {
                throw new NullParameterException();
            }
            this.localTime = localTime;
        }

        private LocalTimeWrapper newInstance(LocalTime localTime) {
            try {
                return new LocalTimeWrapper(localTime);
            } catch (NullParameterException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public int getHour() {
            return localTime.getHour();
        }

        @Override
        public LocalTimeWrapper setHour(int amount) {
            return newInstance(localTime.withHour(amount));
        }

        @Override
        public LocalTimeWrapper addHour(int amount) {
            return newInstance(localTime.plusHours(amount));
        }

        @Override
        public int getMinute() {
            return localTime.getMinute();
        }

        @Override
        public LocalTimeWrapper setMinute(int amount) {
            return newInstance(localTime.withMinute(amount));
        }

        @Override
        public LocalTimeWrapper addMinute(int amount) {
            return newInstance(localTime.plusMinutes(amount));
        }

        @Override
        public int getSecond() {
            return localTime.getSecond();
        }

        @Override
        public LocalTimeWrapper setSecond(int amount) {
            return newInstance(localTime.withSecond(amount));
        }

        @Override
        public LocalTimeWrapper addSecond(int amount) {
            return newInstance(localTime.plusSeconds(amount));
        }

        @Override
        public int getMillisecond() {
            return localTime.get(ChronoField.MILLI_OF_SECOND);
        }

        @Override
        public LocalTimeWrapper setMillisecond(int amount) {
            return newInstance(localTime.with(ChronoField.MILLI_OF_SECOND, amount));
        }

        @Override
        public LocalTimeWrapper addMillisecond(int amount) {
            return newInstance(localTime.plus(Duration.ofMillis(amount)));
        }

        @Override
        public String format(DatePatternBuilder builder) {
            return LocalTimeUtils.format(localTime, builder);
        }

        @Override
        public LocalTime get() {
            return localTime;
        }

        @Override
        public boolean isBefore(LocalTime localTime) {
            return this.localTime.isBefore(localTime);
        }

        @Override
        public boolean isAfter(LocalTime localTime) {
            return this.localTime.isAfter(localTime);
        }

        @Override
        public boolean isEqual(LocalTime localTime) {
            return this.localTime.equals(localTime);
        }
    }
}
