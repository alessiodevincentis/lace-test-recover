package com.woonders.lacemscommon.util;

import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Emanuele on 12/02/2017.
 */
@Component
public class DateConversionUtil {

    public static final String NAME = "dateConversionUtil";
    public static final String JSF_NAME = "#{" + NAME + "}";

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public LocalDateTime calcUTCLocalDateTimeFromTimestamp(final long timestamp) {
        if (timestamp == 0) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), TimeZone.getTimeZone("UTC").toZoneId());
    }

    public String calcStringFromLocalDateTime(final LocalDateTime dateTime) {
        final ZonedDateTime utcDateTime = dateTime.atZone(ZoneId.of("GMT"));
        final ZonedDateTime romeDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("Europe/Rome"));
        return dateTimeFormatter.format(romeDateTime);
    }

    public String calcStringFromLocalDate(final LocalDate date) {
        return date.format(dateFormatter);
    }

    public Date calcDateFromLocalDate(final LocalDate date) {
        if (date != null) {
            return Date.from(Instant.from(date.atStartOfDay(ZoneId.of("GMT"))));
        }
        return null;
    }

    public Date calcDateFromLocalDateTime(final LocalDateTime dateTime) {
        if (dateTime != null) {
            return Date.from(dateTime.atZone(ZoneId.of("GMT")).toInstant());
        }
        return null;
    }

    public LocalDate calcLocalDateFromDate(final Date date) {
        if (date != null) {
            return date.toInstant().atZone(ZoneId.of("UTC")).toLocalDate();
        }
        return null;
    }

    public Date fromYearsToDate(final Integer age) {
        if (age != null) {
            final LocalDate now = LocalDate.now();
            return Date.from(Instant.from((now.minusYears(age)).atStartOfDay(ZoneId.of("GMT"))));
        }
        return null;
    }

    public LocalDateTime calcLocalDateTimeFromDate(final Date date) {
        if (date != null) {
            return date.toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime();
        }
        return null;
    }

    public Date addMonths(final Integer month) {
        if (month != null) {
            final LocalDate now = LocalDate.now();
            return Date.from(Instant.from((now.plusMonths(month)).atStartOfDay(ZoneId.of("GMT"))));
        }
        return null;
    }

    /**
     * Prende un LocalDateTime, ne crea un Zoned con quell'ora sul fuso orario
     * di Roma, dopodiche lo converte in UTC
     */
    public LocalDateTime moveLocalDateTimeToRomeTimeZone(final LocalDateTime localDateTime) {
        if (localDateTime != null) {
            return localDateTime.atZone(ZoneId.of("Europe/Rome")).withZoneSameInstant(ZoneId.of("UTC"))
                    .toLocalDateTime();
        }
        return null;
    }

    public LocalDateTime convertToLocalDateTimeInRome(final LocalDateTime utcLocalDateTime) {
        final ZonedDateTime utcDateTime = utcLocalDateTime.atZone(ZoneId.of("GMT"));
        final ZonedDateTime romeDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("Europe/Rome"));
        return romeDateTime.toLocalDateTime();
    }

    public LocalDateTime convertToLocalDateTimeInUtc(final LocalDateTime romeLocalDateTime) {
        final ZonedDateTime romeDateTime = romeLocalDateTime.atZone(ZoneId.of("Europe/Rome"));
        final ZonedDateTime utcDateTime = romeDateTime.withZoneSameInstant(ZoneId.of("GMT"));
        return utcDateTime.toLocalDateTime();
    }

    public String formatDate(final LocalDateTime localDateTime) {
        return dateFormatter.format(localDateTime);
    }

    public String formatTime(final LocalDateTime localDateTime) {
        return timeFormatter.format(localDateTime);
    }

    /**
     * Passare data in UTC, ritorna la data formattata in Roma Timezone
     */
    public String formatDateInRomeTimeZone(final LocalDateTime utcLocalDateTime) {
        return formatDate(convertToLocalDateTimeInRome(utcLocalDateTime));
    }

    /**
     * Passare data in UTC, ritorna l ora formattata in Roma Timezone
     */
    public String formatTimeInRomeTimeZone(final LocalDateTime utcLocalDateTime) {
        return formatTime(convertToLocalDateTimeInRome(utcLocalDateTime));
    }

    public LocalDate startMonth(final LocalDate from, final int offset) {
        return from.plusMonths(offset).withDayOfMonth(LocalDate.MIN.getDayOfMonth());
    }

    public LocalDate endMonth(final LocalDate from, final int offset) {
        final LocalDate initialDate = from.plusMonths(offset);
        return initialDate.withDayOfMonth(initialDate.lengthOfMonth());
    }

    public LocalDate startYear(final LocalDate from, final int offset) {
        return from.withMonth(LocalDate.MIN.getMonthValue()).withDayOfMonth(LocalDate.MIN.getDayOfMonth()).plusYears(offset);
    }

    public LocalDate endYear(final LocalDate from, final int offset) {
        return from.withMonth(LocalDate.MAX.getMonthValue()).withDayOfMonth(LocalDate.MAX.getDayOfMonth()).plusYears(offset);
    }

    public LocalDate addOrSubstractDays(final LocalDate from, final int days) {
        return from.plusDays(days);
    }
}
