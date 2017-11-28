package com.efluid.ecore.commun.utils;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.imrglobal.framework.nls.Language;
import com.imrglobal.framework.utils.*;

/**
 * Cette classe étend la classe d'utilitaire liées aux dates pour ajouter quelques méthodes.
 */
public class EcoreDateUtils extends DateUtils {

  /**
   * TODO: à déplacer dans l'archi.
   * 
   * @return la date au format ISO en utilisant {@link DateTimeFormatter} et la langue de l'utilisateur pour retrouver la {@link ZoneId} et la {@link Locale}.
   */
  public static String dateTimeFormatAsIsoDate(Date date) {
    Language langueUtilisateur = Language.FRENCH;
    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE
        .withLocale(langueUtilisateur.toLocale())
        .withZone(fromLanguage(langueUtilisateur));
    return formatter.format(date.toInstant());
  }

  /**
   * TODO: à déplacer dans {@link Language}.
   */
  private static ZoneId fromLanguage(Language language) {
    if (language.equals(Language.FRENCH)) {
      return ZoneId.of("Europe/Paris");
    }
    return ZoneId.of("Europe/Paris");
  }

  /**
   * Retourne la comparaison des deux dates selon {@link Date#compareTo(Date)} en gérant les cas nuls.
   * 
   * @param date1 peut être nulle
   * @param date2 peut être nulle
   */
  public static int compareTo(Date date1, Date date2) {
    if (null == date1 && null == date2) {
      return 0;
    } else if (null == date1) {
      return 1;
    } else if (null == date2) {
      return -1;
    } else if (date2 instanceof Timestamp) {
      return -date2.compareTo(date1);
    }
    return date1.compareTo(date2);
  }

  /**
   * Retourne une date à partir de ces arguments.
   * 
   * @deprecated : à ne pas utiliser. Méthode à supprimer.
   */
  @Deprecated
  public static Date constructDateGMT(Date date, int heures, int minutes, int secondes) {
    Calendar cal = Calendar.getInstance();
    cal.clear();
    cal.setTimeZone(TimeZone.getTimeZone("GMT"));
    cal.set(getYear(date), getMonth(date) - 1, getDay(date), heures, minutes, secondes);
    return cal.getTime();
  }

  /** @return la première année bissextile parmi les années n, n-1, n-2, n-3... */
  public static int getPremiereAnneeBissextile() {
    int annee = getCurrentYear();
    while (!isALeapYear(annee)) {
      annee--;
    }
    return annee;
  }

  /** @return la première année non bissextile parmi les années n, n-1... */
  public static int getPremiereAnneeNonBissextile() {
    int annee = getCurrentYear();
    while (isALeapYear(annee)) {
      annee--;
    }
    return annee;
  }

  /**
   * @return le format de date par défaut (si non spécifié, le format est dd/MM/yyyy).
   *
   * @deprecated Utiliser plutôt {@link DateUtils#getDateFormat(com.imrglobal.framework.nls.Language)} pour la langue de l'utilisateur courant.
   */
  @Deprecated
  public static String getDefaultDateFormat() {
    return DEFAULT_DATE_FORMAT;
  }

  /**
   * @return le format de date time par défaut (si non spécifié, le format est dd/MM/yyyy HH:mm).
   *
   * @deprecated Utiliser plutôt {@link DateUtils#getDateTimeFormat(com.imrglobal.framework.nls.Language)} pour la langue de l'utilisateur courant.
   */
  @Deprecated
  public static String getDefaultDateTimeFormat() {
    return DEFAULT_DATE_TIME_FORMAT;
  }

  /** Cette méthode retourne une nouvelle date issue de la fusion de la date et l'heure. Si la date ou l'heure est null, la date retournée est null. */
  public static Date getFusionDateHeure(Date date, Date heure) {
    return (date != null && heure != null) ? getDate(getYear(date), getMonth(date), getDay(date), getHour(heure), getMinute(heure), getSecond(heure)) : null;
  }

  /**
   * Returns the year that contains or begins with the instant in time represented by this Date object, as interpreted in the given time zone.
   * 
   * @param date whose year one wants to know
   * @param defCalendar Calendar
   * @return int, year
   */
  public static int getYear(Date date, Calendar defCalendar) {
    if (date != null && defCalendar != null) {
      defCalendar.setTime(date);
      return (defCalendar.get(Calendar.YEAR));
    }
    return 2000;
  }

  /**
   * Returns a number representing the month that contains or begins with the instant in time represented by this Date object. The value returned is between 1 and 12.
   * 
   * @param date whose month one wants to know
   * @param defCalendar Calendar
   * @return month number
   */
  public static int getMonth(Date date, Calendar defCalendar) {
    if (date != null && defCalendar != null) {
      defCalendar.setTime(date);
      return (defCalendar.get(Calendar.MONTH) + 1);
    }
    return 1;
  }

  /**
   * Returns the day of the month represented by this Date object. <BR>
   * The value returned is between 1 and 31 representing the day of the month that contains or begins with the instant in time represented by this Date object, as interpreted in the given time zone.
   * 
   * @param date whose day one wants to know
   * @param defCalendar Calendar
   * @return day number
   */
  public static int getDay(Date date, Calendar defCalendar) {
    if (date != null && defCalendar != null) {
      defCalendar.setTime(date);
      return defCalendar.get(Calendar.DAY_OF_MONTH);
    }
    return 1;
  }

  /**
   * Returns the hour represented by this Date object. The returned value is a number (0 through 23) representing the hour within the day that contains or begins with the instant in time represented
   * by this Date object
   * 
   * @param date given date
   * @param defCalendar Calendar
   * @return hour between 0 and 23
   */
  public static int getHour(Date date, Calendar defCalendar) {
    if (date != null && defCalendar != null) {
      defCalendar.setTime(date);
      return (defCalendar.get(Calendar.HOUR_OF_DAY));
    }
    return 0;
  }

  /**
   * Returns the number of minutes past the hour represented by this date, as interpreted in the given time zone. The value returned is between 0 and 59.
   * 
   * @param date la date dont on extrait l'année
   * @param defCalendar Calendar
   * @return the number of minutes
   */
  public static int getMinute(Date date, Calendar defCalendar) {
    if (date != null && defCalendar != null) {
      defCalendar.setTime(date);
      return (defCalendar.get(Calendar.MINUTE));
    }
    return 0;
  }

  /**
   * Returns the number of seconds past the minute represented by this date. The value returned is between 0 and 61. The values 60 and 61 can only occur on those Java Virtual Machines that take leap
   * seconds into account.
   * 
   * @param date la date dont on extrait l'année
   * @param defCalendar Calendar
   * @return the number of seconds
   */
  public static int getSecond(Date date, Calendar defCalendar) {
    if (date != null && defCalendar != null) {
      defCalendar.setTime(date);
      return (defCalendar.get(Calendar.SECOND));
    }
    return 0;
  }

  /**
   * Retourne vrai si le pattern de format est valide. Le truc est pas très robuste et ne compare qu'avec les patterns de format par défaut. Permet de détecter si un string est un pattern de format.
   * 
   * @param formatPattern ne peut être null.
   */
  public static boolean isValidFormatPattern(String formatPattern) {

    return formatPattern.equals(DEFAULT_DATE_FORMAT) || formatPattern.equals(DEFAULT_DATE_TIME_FORMAT);
  }

  /**
   * Cette méthode retourne le nombre de jours entre 2 dates sous forme d'un nombre décimal positif.
   * <p/>
   * Elle retourne 0 si l'une des deux dates est null.
   */
  public static double getNombreJours(Date date1, Date date2) {
    double result;
    if (date1 == null || date2 == null) {
      result = 0;
    } else {
      long dif = Math.abs(date1.getTime() - date2.getTime());
      result = NumberUtils.divide(dif, (24.0 * 60.0 * 60.0 * 1000), 16);
    }
    return result;
  }




  /**
   * Renvoie {@code true} :
   * <ul>
   * <li>si inclus est vrai et dateAComparer € [dateDebut,dateFin] à l'aide de {@link DateUtils#isSameDay(Date, Date)} ;</li>
   * <li>si inclus est false et dateAComparer € ]dateDebut,dateFin[ ;</li>
   * <li>si dateFin est null alors l'ensemble devient : [dateDebut,+oo[ si inclus est true et ]dateDebut,+oo[ si inclus est false ;</li>
   * </ul>
   * {@code false} sinon.
   */
  public static boolean isBetween(Date dateDebut, Date dateFin, Date dateATester, boolean inclu) {
    if (dateFin != null) {
      return dateDebut.before(dateATester) && dateFin.after(dateATester) || (inclu && (isSameDay(dateDebut, dateATester) || isSameDay(dateFin, dateATester)));
    }
    return dateDebut.before(dateATester) || inclu && isSameDay(dateDebut, dateATester);
  }
}