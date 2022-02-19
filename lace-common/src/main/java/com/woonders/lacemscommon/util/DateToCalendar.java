package com.woonders.lacemscommon.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class DateToCalendar {

    // Convert Date to Calendar
    public static Calendar dateToCalendar(final Date date) {

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;

    }

    public static Age calculateAge(final Date birthDate) {
        int years = 0;
        int months = 0;
        int days = 0;
        // create calendar object for birth day
        final Calendar birthDay = Calendar.getInstance();

        birthDay.setTimeInMillis(birthDate.getTime());
        // create calendar object for current day
        final long currentTime = System.currentTimeMillis();
        final Calendar now = Calendar.getInstance();
        now.setTimeInMillis(currentTime);
        // Get difference between years
        years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        final int currMonth = now.get(Calendar.MONTH) + 1;
        final int birthMonth = birthDay.get(Calendar.MONTH) + 1;
        // Get difference between months
        months = currMonth - birthMonth;
        // if month difference is in negative then reduce years by one and
        // calculate the number of months.
        if (months < 0) {
            years--;
            months = 12 - birthMonth + currMonth;
            if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
                months--;
        } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            years--;
            months = 11;
        }
        // Calculate the days
        if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
            days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
        else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            final int today = now.get(Calendar.DAY_OF_MONTH);
            now.add(Calendar.MONTH, -1);
            days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
        } else {
            days = 0;
            if (months == 12) {
                years++;
                months = 0;
            }
        }
        // Create new Age object
        return new Age(days, months, years);
    }

    public static long differenceDatesInDays(final Date start, final Date end) {
        if (start != null && end != null) {
            final long diffInMillies = Math.abs(end.getTime() - start.getTime());
            return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        }
        return 0;
    }

    public static int diffeDateMonth(final Date dateInizio, final Date dateFine) {
        if (dateInizio != null && dateFine != null) {
            final Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(dateInizio);
            final Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(dateFine);

            final int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
            final int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);

            return diffMonth;
        }
        return 0;
    }

    public static int diffeDateYear(final Date dateInizio, final Date dateFine) {
        if (dateInizio != null && dateFine != null) {
            final Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(dateInizio);
            final Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(dateFine);

            final int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);

            return diffYear;
        }
        return 0;
    }

    public static Date inizioMese(final Date date, final int range) {
        if (date != null) {
            final Calendar cal = dateToCalendar(date);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            cal.add(Calendar.MONTH, range);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTime();
        }
        return null;
    }

    public static Date fineMese(final Date date, final int range) {
        if (date != null) {
            final Calendar cal = dateToCalendar(date);
            cal.add(Calendar.MONTH, range);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 59);
            return cal.getTime();
        }
        return null;
    }

    public static Date addDays(final Date date, final int days) {
        if (date != null) {
            final Calendar cal = dateToCalendar(date);
            cal.add(Calendar.DAY_OF_MONTH, days);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 59);
            return cal.getTime();
        }
        return null;
    }

    public static Date settaGiorno(final Date date, final int hh, final int mm, final int sec) {
        if (date != null) {
            final Calendar cal = dateToCalendar(date);
            cal.set(Calendar.HOUR_OF_DAY, hh);
            cal.set(Calendar.MINUTE, mm);
            cal.set(Calendar.SECOND, sec);

            return cal.getTime();
        }
        return null;
    }

    @Deprecated
    public static Double arrotondamento(Double x) {
        if (x != null) {
            x = (double) Math.round(x * 100);
            x = x / 100;
            return x;
        }
        return null;
    }

    @Deprecated
    public static Double arrotondamentoSeiCifre(Double x) {
        if (x != null) {
            x = (double) Math.round(x * 1000000);
            x = x / 1000000;
            return x;
        }
        return null;
    }

    public static Date getDateWithoutTime(final Date date) {
        if (date != null) {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTime();
        }
        return null;
    }

    public static Date getTomorrowDate(final Date date) {
        if (date != null) {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, 1);
            return cal.getTime();
        }
        return null;
    }

    public static Date setMonth(final Date date, final int range) {
        if (date != null) {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MONTH, range);
            return cal.getTime();
        }
        return null;
    }

    public static Date getStarYear(final Date date) {
        if (date != null) {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH, 0);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTime();
        }
        return null;
    }

    public static Date getEndYear(final Date date) {
        if (date != null) {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.DAY_OF_MONTH, 31);
            cal.set(Calendar.MONTH, 11);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 59);
            return cal.getTime();
        }
        return null;
    }

    public static String patternData(final Date data) {
        if (data == null)
            return "";

        final SimpleDateFormat daten = new SimpleDateFormat("dd/MM/yyyy");
        final String dateStr = daten.format(data);

        return dateStr;
    }

    public static String patternDataOra(final Date data) {
        if (data == null)
            return "";

        final SimpleDateFormat daten = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        final String dateStr = daten.format(data);

        return dateStr;
    }

    @Deprecated
    public static String NumberToText(final BigDecimal importo) {
        // metodo wrapper
        if (importo != null) {
            final int n = importo.multiply(new BigDecimal("100")).intValue();
            final int parteIntera = n / 100;
            final String parteDecimale = String.format("%02d", n % 100);

            if (parteIntera == 0) {
                return "zero/" + parteDecimale;
            } else {
                return NumberToTextRicorsiva(parteIntera) + "/" + parteDecimale;
            }
        }
        return "";
    }

    private static String NumberToTextRicorsiva(final int n) {
        if (n < 0) {
            return "meno " + NumberToTextRicorsiva(-n);
        } else if (n == 0) {
            return "";
        } else if (n <= 19) {
            return new String[]{"uno", "due", "tre", "quattro", "cinque", "sei", "sette", "otto", "nove", "dieci",
                    "undici", "dodici", "tredici", "quattordici", "quindici", "sedici", "diciassette", "diciotto",
                    "diciannove"}[n - 1];
        } else if (n <= 99) {
            final String[] vettore = {"venti", "trenta", "quaranta", "cinquanta", "sessanta", "settanta", "ottanta",
                    "novanta"};
            String letter = vettore[n / 10 - 2];
            final int t = n % 10; // t e' la prima cifra di n
            // se e' 1 o 8 va tolta la vocale finale di letter
            if (t == 1 || t == 8) {
                letter = letter.substring(0, letter.length() - 1);
            }
            return letter + NumberToTextRicorsiva(n % 10);
        } else if (n <= 199) {
            return "cento" + NumberToTextRicorsiva(n % 100);
        } else if (n <= 999) {
            int m = n % 100;
            m /= 10; // divisione intera per 10 della variabile
            String letter = "cent";
            if (m != 8) {
                letter = letter + "o";
            }
            return NumberToTextRicorsiva(n / 100) + letter + NumberToTextRicorsiva(n % 100);
        } else if (n <= 1999) {
            return "mille" + NumberToTextRicorsiva(n % 1000);
        } else if (n <= 999999) {
            return NumberToTextRicorsiva(n / 1000) + "mila" + NumberToTextRicorsiva(n % 1000);
        } else if (n <= 1999999) {
            return "unmilione" + NumberToTextRicorsiva(n % 1000000);
        } else if (n <= 999999999) {
            return NumberToTextRicorsiva(n / 1000000) + "milioni" + NumberToTextRicorsiva(n % 1000000);
        } else if (n <= 1999999999) {
            return "unmiliardo" + NumberToTextRicorsiva(n % 1000000000);
        } else {
            return NumberToTextRicorsiva(n / 1000000000) + "miliardi" + NumberToTextRicorsiva(n % 1000000000);
        }
    }

    // Convert Calendar to Date
    public Date calendarToDate(final Calendar calendar) {
        return calendar.getTime();
    }

}
