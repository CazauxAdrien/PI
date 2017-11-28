package com.efluid.ecore.temps.type;

import java.util.*;

import com.imrglobal.framework.type.*;
import com.imrglobal.framework.utils.DateUtils;

import com.hermes.arc.commun.type.HermesNLSEnumType;

import com.efluid.ecore.commun.type.ESigne;

/**
 * Cette classe représente pas de temps sous forme d'un énuméré statique.
 */
public class EPasTemps extends HermesNLSEnumType implements ITypeDuree {

  private static final int PLUS = 1;
  private static final int MOINS = -1;

  private static final int _UNE_MINUTE = 10;
  public static final EPasTemps UNE_MINUTE = new EPasTemps(_UNE_MINUTE);
  private static final int _DIX_MINUTES = 0;
  public static final EPasTemps DIX_MINUTES = new EPasTemps(_DIX_MINUTES);
  private static final int _TRENTE_MINUTES = 1;
  public static final EPasTemps TRENTE_MINUTES = new EPasTemps(_TRENTE_MINUTES);
  private static final int _HORAIRE = 2;
  public static final EPasTemps HORAIRE = new EPasTemps(_HORAIRE);
  private static final int _DEUX_HEURES = 11;
  public static final EPasTemps DEUX_HEURES = new EPasTemps(_DEUX_HEURES);
  private static final int _TROIS_HEURES = 14;
  public static final EPasTemps TROIS_HEURES = new EPasTemps(_TROIS_HEURES);
  private static final int _QUATRE_HEURES = 12;
  public static final EPasTemps QUATRE_HEURES = new EPasTemps(_QUATRE_HEURES);
  private static final int _HUIT_HEURES = 13;
  public static final EPasTemps HUIT_HEURES = new EPasTemps(_HUIT_HEURES);
  private static final int _JOURNALIER = 3;
  public static final EPasTemps JOURNALIER = new EPasTemps(_JOURNALIER);
  private static final int _HEBDOMADAIRE = 8;
  public static final EPasTemps HEBDOMADAIRE = new EPasTemps(_HEBDOMADAIRE);
  private static final int _BI_MENSUEL = 9;
  public static final EPasTemps BI_MENSUEL = new EPasTemps(_BI_MENSUEL);
  private static final int _MENSUEL = 4;
  public static final EPasTemps MENSUEL = new EPasTemps(_MENSUEL);
  private static final int _TRIMESTRIEL = 5;
  public static final EPasTemps TRIMESTRIEL = new EPasTemps(_TRIMESTRIEL);
  private static final int _SEMESTRIEL = 6;
  public static final EPasTemps SEMESTRIEL = new EPasTemps(_SEMESTRIEL);
  private static final int _ANNUEL = 7;
  public static final EPasTemps ANNUEL = new EPasTemps(_ANNUEL);

  private EPasTemps(int v) {
    setValue(v);
  }

  public static EnumType from_int(int value) {
    switch (value) {
      case _UNE_MINUTE:
        return UNE_MINUTE;
      case _DIX_MINUTES:
        return DIX_MINUTES;
      case _TRENTE_MINUTES:
        return TRENTE_MINUTES;
      case _HORAIRE:
        return HORAIRE;
      case _JOURNALIER:
        return JOURNALIER;
      case _HEBDOMADAIRE:
        return HEBDOMADAIRE;
      case _BI_MENSUEL:
        return BI_MENSUEL;
      case _MENSUEL:
        return MENSUEL;
      case _TRIMESTRIEL:
        return TRIMESTRIEL;
      case _SEMESTRIEL:
        return SEMESTRIEL;
      case _ANNUEL:
        return ANNUEL;
      case _DEUX_HEURES:
        return DEUX_HEURES;
      case _QUATRE_HEURES:
        return QUATRE_HEURES;
      case _HUIT_HEURES:
        return HUIT_HEURES;
      case _TROIS_HEURES:
        return TROIS_HEURES;
      default:
        return null;
    }
  }

  /**
   * Retourne le ratio de ce pas de temps par le pas de temps passé en paramètre. Par exemple, si ce pas de temps est journalier, et le pas de temps quotient donné est horaire, alors cette méthode
   * retourne 24.
   *
   * @param pasTempsQuotient le pas de temps par lequel il faut diviser.
   */
  public double getRatio(EPasTemps pasTempsQuotient) {
    return ((double) getDuree().getGap()) / ((double) pasTempsQuotient.getDuree().getGap());
  }

  /** @return le plus petit pas de temps disponible. */
  public static EPasTemps getPasTempsMin() {
    return UNE_MINUTE;
  }

  /**
   * Retourne la durée du pas. <br>
   * Utilisable pour calculer le pas optimale d'affichage
   *
   * @return la duréé du pas. Pour les pas {@link #BI_MENSUEL}, {@link #MENSUEL}, {@link #TRIMESTRIEL}, {@link #SEMESTRIEL} et {@link #ANNUEL}, il s'agit d'une durée approximative, respectivement, 15,
   *         30, 91, 182 et 365 jours.
   */
  @Override
  public TimeGap getDuree() {
    int value = getValue();
    switch (value) {
      case _UNE_MINUTE:
        return new TimeGap(0, 0, 1, 0, 0);
      case _DIX_MINUTES:
        return new TimeGap(0, 0, 10, 0, 0);
      case _TRENTE_MINUTES:
        return new TimeGap(0, 0, 30, 0, 0);
      case _HORAIRE:
        return new TimeGap(0, 1, 0, 0, 0);
      case _DEUX_HEURES:
        return new TimeGap(0, 2, 0, 0, 0);
      case _TROIS_HEURES:
        return new TimeGap(0, 3, 0, 0, 0);
      case _QUATRE_HEURES:
        return new TimeGap(0, 4, 0, 0, 0);
      case _HUIT_HEURES:
        return new TimeGap(0, 8, 0, 0, 0);
      case _JOURNALIER:
        return new TimeGap(1, 0, 0, 0, 0);
      case _HEBDOMADAIRE:
        return new TimeGap(7, 0, 0, 0, 0);
      case _BI_MENSUEL:
        return new TimeGap(15, 0, 0, 0, 0);
      case _MENSUEL:
        return new TimeGap(30, 0, 0, 0, 0);
      case _TRIMESTRIEL:
        return new TimeGap(91, 0, 0, 0, 0);
      case _SEMESTRIEL:
        return new TimeGap(181, 0, 0, 0, 0);
      case _ANNUEL:
        return new TimeGap(365, 0, 0, 0, 0);
      default:
        return null;
    }
  }

  @Override
  public int getNbMois() {
    int value = getValue();
    switch (value) {
      case _MENSUEL:
        return 1;
      case _TRIMESTRIEL:
        return 3;
      case _SEMESTRIEL:
        return 6;
      case _ANNUEL:
        return 12;
      default:
        return 0;
    }
  }

  @Override
  public int getNbAnnees() {
    int value = getValue();
    switch (value) {
      case _ANNUEL:
        return 1;
      default:
        return 0;
    }
  }

  public int getNbSemaines() {
    int value = getValue();
    switch (value) {
      case _HEBDOMADAIRE:
        return 1;
      case _BI_MENSUEL:
        return 2;
      default:
        return 0;
    }
  }

  @Override
  public Iterator<EPasTemps> iterator() {
    List<EPasTemps> l = new ArrayList<>();

    l.add(UNE_MINUTE);
    l.add(DIX_MINUTES);
    l.add(TRENTE_MINUTES);
    l.add(HORAIRE);
    l.add(DEUX_HEURES);
    l.add(TROIS_HEURES);
    l.add(QUATRE_HEURES);
    l.add(HUIT_HEURES);
    l.add(JOURNALIER);
    l.add(HEBDOMADAIRE);
    l.add(BI_MENSUEL);
    l.add(MENSUEL);
    l.add(TRIMESTRIEL);
    l.add(SEMESTRIEL);
    l.add(ANNUEL);

    return l.iterator();
  }

  @Override
  public int compareTo(ITypeDuree o) {
    TimeGap gap1 = this.getDuree();
    int i;
    if (o != null) {
      i = gap1.compareTo(o.getDuree());
    } else {
      i = 1;
    }
    return i;
  }

  /**
   * Retourne de maniére exacte la prochaine date pour ce pas de temps, sauf dans le cas de {@link #BI_MENSUEL}.
   *
   */
  public Date obtenirProchaineDate(Date date) {
    return obtenirDate(date, PLUS);
  }

  /**
   * Retourne de maniére exacte la prochaine date pour ce pas de temps fois nombreApplicationPasTemps, sauf dans le cas de {@link #BI_MENSUEL}.
   *
   * @param date
   * @return
   */
  public Date obtenirProchaineDate(Date date, int nombreApplicationPasTemps) {
    return obtenirDate(date, PLUS, nombreApplicationPasTemps);
  }

  /**
   * Retourne de maniére exacte la date précédente pour ce pas de temps, sauf dans le cas de {@link #BI_MENSUEL}.
   *
   */
  public Date obtenirDatePrecedente(Date date) {
    return obtenirDate(date, MOINS);
  }

  /**
   * Retourne de maniére exacte la date précédente ou suivante pour ce pas de temps suivant le signe donné, sauf dans le cas de {@link #BI_MENSUEL}.
   *
   */
  private Date obtenirDate(Date date, int signe) {
    return obtenirDate(date, signe, 1);
  }

  /**
   * Retourne de maniére exacte la date précédente ou suivante pour ce pas de temps fois nombreApplicationPasTemps suivant le signe donné, sauf dans le cas de {@link #BI_MENSUEL}.
   *
   * @param date
   * @return
   */
  private Date obtenirDate(Date date, int signe, int nombreApplicationPasTemps) {
    switch (getValue()) {
      case _UNE_MINUTE:
        return addMinute(date, signe * nombreApplicationPasTemps);
      case _DIX_MINUTES:
        return addMinute(date, 10 * signe * nombreApplicationPasTemps);
      case _TRENTE_MINUTES:
        return addMinute(date, 30 * signe * nombreApplicationPasTemps);
      case _HORAIRE:
        return ajouterHeures(date, 1 * signe * nombreApplicationPasTemps);
      case _DEUX_HEURES:
        return ajouterHeures(date, 2 * signe * nombreApplicationPasTemps);
      case _TROIS_HEURES:
        return ajouterHeures(date, 3 * signe * nombreApplicationPasTemps);
      case _QUATRE_HEURES:
        return ajouterHeures(date, 4 * signe * nombreApplicationPasTemps);
      case _HUIT_HEURES:
        return ajouterHeures(date, 8 * signe * nombreApplicationPasTemps);
      case _JOURNALIER:
        return DateUtils.addDays(date, 1 * signe * nombreApplicationPasTemps);
      case _HEBDOMADAIRE:
        return DateUtils.addDays(date, 7 * signe * nombreApplicationPasTemps);
      case _BI_MENSUEL:
        // TODO on ne sait pas faire ça de manière exacte !
        if (signe > 0 && DateUtils.getDay(date) == 16 && DateUtils.getHour(date) == 0 && DateUtils.getMinute(date) == 0) {
          date = DateUtils.setDay(date, 1);
          date = MENSUEL.obtenirProchaineDate(date);
        } else if (signe < 0 && DateUtils.getDay(date) == 1 && DateUtils.getHour(date) == 0 && DateUtils.getMinute(date) == 0) {
          date = MENSUEL.obtenirDatePrecedente(date);
          date = obtenirDate(date, ESigne.SIGNE_PLUS.getValue());
        } else {
          date = DateUtils.addDays(date, 15 * signe);
        }
        if (nombreApplicationPasTemps > 1) {
          return obtenirDate(date, signe, nombreApplicationPasTemps - 1);
        } else {
          return date;
        }
      case _MENSUEL:
        return DateUtils.addMonths(date, 1 * signe * nombreApplicationPasTemps);
      case _TRIMESTRIEL:
        return DateUtils.addMonths(date, 3 * signe * nombreApplicationPasTemps);
      case _SEMESTRIEL:
        return DateUtils.addMonths(date, 6 * signe * nombreApplicationPasTemps);
      case _ANNUEL:
        return DateUtils.addYears(date, 1 * signe * nombreApplicationPasTemps);
      default:
        return null;
    }
  }

  private Date ajouterHeures(Date date, long heures) {
    if (date != null) {
      return new Date(date.getTime() + heures * 3600000l);
    }
    return null;
  }

  private Date addMinute(Date date, long minutes) {
    if (date != null) {
      return new Date(date.getTime() + minutes * 60000l);
    }
    return null;
  }

  /**
   * Retourne la date du début de la période. Par exemple, pour un pas de temps {@link #HORAIRE}, une date à un tel jour à 16:30 retournera la méme date mais à 16:00.
   *
   * <p>
   * TODO factoriser dans un utilitaire avec {@link com.efluid.ecore.courbe.type.EDureePartition}
   *
   */
  public Date obtenirDateDebutPeriode(Date date) {
    switch (getValue()) {
      case _UNE_MINUTE:
        return obtenirDateDebutPeriodeMinute(date);
      case _DIX_MINUTES:
        return obtenirDateDebutPeriodeDixMinute(date);
      case _TRENTE_MINUTES:
        return obtenirDateDebutPeriodeTrenteMinute(date);
      case _HORAIRE:
        return obtenirDateDebutPeriodeHoraire(date, 1, HORAIRE);
      case _DEUX_HEURES:
        return obtenirDateDebutPeriodeHoraire(date, 2, DEUX_HEURES);
      case _TROIS_HEURES:
        return obtenirDateDebutPeriodeHoraire(date, 3, TROIS_HEURES);
      case _QUATRE_HEURES:
        return obtenirDateDebutPeriodeHoraire(date, 4, QUATRE_HEURES);
      case _HUIT_HEURES:
        return obtenirDateDebutPeriodeHoraire(date, 8, HUIT_HEURES);
      case _JOURNALIER:
        return obtenirDateDebutPeriodeJournalier(date);
      case _HEBDOMADAIRE:
        return obtenirDateDebutPeriodeHebdomadaire(date);
      case _BI_MENSUEL:
        return obtenirDateDebutPeriodeBiMensuel(date);
      case _MENSUEL:
        return obtenirDateDebutPeriodeMensuel(date);
      case _TRIMESTRIEL:
        return obtenirDateDebutPeriodeTrimestriel(date);
      case _SEMESTRIEL:
        return obtenirDateDebutPeriodeSemestriel(date);
      case _ANNUEL:
        return obtenirDateDebutPeriodeAnnuel(date);
      default:
        return null;
    }
  }

  /**
   * Retourne vrai si la date est conforme au pas de temps. Par exemple, pour un pas de temps {@link #HORAIRE}, une date à un tel jour à 16:30 sera fausse mais 16:00sera vrai.
   *
   * @return boolean
   */
  public boolean isDateComplete(Date date) {
    switch (getValue()) {
      case _UNE_MINUTE:
        return isDateCompleteMinute(date);
      case _DIX_MINUTES:
        return isDateCompleteDixMinute(date);
      case _TRENTE_MINUTES:
        return isDateCompleteTrenteMinute(date);
      case _HORAIRE:
        return isDateCompleteHoraire(date, 1);
      case _DEUX_HEURES:
        return isDateCompleteHoraire(date, 2);
      case _TROIS_HEURES:
        return isDateCompleteHoraire(date, 3);
      case _QUATRE_HEURES:
        return isDateCompleteHoraire(date, 4);
      case _HUIT_HEURES:
        return isDateCompleteHoraire(date, 8);
      default:
        return true;
    }
  }

  /**
   * Retourne la date de début de la période pour le pas de temps {@link #MINUTE}.
   *
   */
  private static Date obtenirDateDebutPeriodeMinute(Date date) {
    return getDate(DateUtils.getYear(date), DateUtils.getMonth(date), DateUtils.getDay(date), DateUtils.getHour(date), DateUtils.getMinute(date), 0, date, UNE_MINUTE);
  }

  /**
   * Retourne vrai si la date est conforme au pas de temps {@link #MINUTE}.
   *
   */
  private static boolean isDateCompleteMinute(Date date) {
    return DateUtils.getMinute(date) == 0;
  }

  /**
   * Retourne la date de début de la période pour le pas de temps {@link #DIX_MINUTES}.
   *
   */
  private static Date obtenirDateDebutPeriodeDixMinute(Date date) {
    if (DateUtils.getMinute(date) <= 9) {
      return getDate(DateUtils.getYear(date), DateUtils.getMonth(date), DateUtils.getDay(date), DateUtils.getHour(date), 0, 0, date, DIX_MINUTES);
    } else if (DateUtils.getMinute(date) <= 19) {
      return getDate(DateUtils.getYear(date), DateUtils.getMonth(date), DateUtils.getDay(date), DateUtils.getHour(date), 10, 0, date, DIX_MINUTES);
    } else if (DateUtils.getMinute(date) <= 29) {
      return getDate(DateUtils.getYear(date), DateUtils.getMonth(date), DateUtils.getDay(date), DateUtils.getHour(date), 20, 0, date, DIX_MINUTES);
    } else if (DateUtils.getMinute(date) <= 39) {
      return getDate(DateUtils.getYear(date), DateUtils.getMonth(date), DateUtils.getDay(date), DateUtils.getHour(date), 30, 0, date, DIX_MINUTES);
    } else if (DateUtils.getMinute(date) <= 49) {
      return getDate(DateUtils.getYear(date), DateUtils.getMonth(date), DateUtils.getDay(date), DateUtils.getHour(date), 40, 0, date, DIX_MINUTES);
    }
    return getDate(DateUtils.getYear(date), DateUtils.getMonth(date), DateUtils.getDay(date), DateUtils.getHour(date), 50, 0, date, DIX_MINUTES);
  }

  /**
   * Retourne vrai si la date est conforme au pas de temps {@link #DIX_MINUTES}.
   *
   */
  private static boolean isDateCompleteDixMinute(Date date) {
    int minute = DateUtils.getMinute(date);
    if (0 == minute || 10 == minute || 20 == minute || 30 == minute || 40 == minute || 50 == minute) {
      return true;
    }
    return false;
  }

  /**
   * Retourne la date de début de la période pour le pas de temps {@link #DIX_MINUTES}.
   *
   */
  private static Date obtenirDateDebutPeriodeTrenteMinute(Date date) {
    if (DateUtils.getMinute(date) <= 29) {
      return getDate(DateUtils.getYear(date), DateUtils.getMonth(date), DateUtils.getDay(date), DateUtils.getHour(date), 0, 0, date, TRENTE_MINUTES);
    }
    return getDate(DateUtils.getYear(date), DateUtils.getMonth(date), DateUtils.getDay(date), DateUtils.getHour(date), 30, 0, date, TRENTE_MINUTES);
  }

  /**
   * Retourne vrai si la date est conforme au pas de temps {@link #DIX_MINUTES}.
   *
   */
  private static boolean isDateCompleteTrenteMinute(Date date) {
    int minute = DateUtils.getMinute(date);
    if (0 == minute || 30 == minute) {
      return true;
    }
    return false;
  }

  /**
   * permet de contourner le probleme des changement d'heure car par exemple calendar.set(Calendar.MINUTE, 0); sur la date Sun Oct 25 02:00:00 CEST 2009 donne Sun Oct 25 02:00:00 CET 2009 et donc
   * enleve une heure pour rien
   */
  private static Date getDate(int annee, int mois, int jour, int heure, int minute, int seconde, Date date, EPasTemps pasTemps) {
    Date dateCree = DateUtils.getDate(annee, mois, jour, heure, minute, seconde);

    if (dateCree.after(date)) {
      dateCree = EPasTemps.HORAIRE.obtenirDatePrecedente(dateCree);
    } else if (date.getTime() - dateCree.getTime() >= pasTemps.getDuree().getGap()) {
      dateCree = EPasTemps.HORAIRE.obtenirProchaineDate(dateCree);
    }

    return dateCree;
  }

  /**
   * Retourne vrai si la date est conforme au pas de temps {@link #HORAIRE}.
   *
   */
  private static Date obtenirDateDebutPeriodeHoraire(Date date, int nbHeurePasTemps, EPasTemps pasTemps) {
    if (HORAIRE.equals(pasTemps)) {
      return getDate(DateUtils.getYear(date), DateUtils.getMonth(date), DateUtils.getDay(date), DateUtils.getHour(date), 0, 0, date, pasTemps);
    }
    return DateUtils.getDate(DateUtils.getYear(date), DateUtils.getMonth(date), DateUtils.getDay(date), getNbHeuresDateDebutPeriode(nbHeurePasTemps, date, pasTemps), 0, 0);
  }

  private static int getNbHeuresDateDebutPeriode(int nbHeurePasTemps, Date date, EPasTemps pasTemps) {
    int nbHeures = DateUtils.getHour(date);
    if (nbHeures / nbHeurePasTemps < 2) {
      int nbheuresEntreDateDebutJourEtDate = (int) ((date.getTime() - DateUtils.getDayDate(date).getTime()) / 3600000);
      if (nbHeures != nbheuresEntreDateDebutJourEtDate) {
        if (nbheuresEntreDateDebutJourEtDate / nbHeurePasTemps >= 1) {
          return DateUtils.getHour(pasTemps.obtenirDatePrecedente(DateUtils.getDate(DateUtils.getYear(date), DateUtils.getMonth(date), DateUtils.getDay(date), nbHeurePasTemps * 2, 0, 0)));
        } else if (DateUtils.getHour(pasTemps.obtenirProchaineDate(date)) == nbHeurePasTemps * 2) {
          return nbHeures;
        }
        return 0;
      }
    }
    return (nbHeures / nbHeurePasTemps) * nbHeurePasTemps;
  }

  /**
   * Retourne la date de début de la période pour le pas de temps {@link #HORAIRE}.
   *
   */
  private static boolean isDateCompleteHoraire(Date date, int nbHeure) {
    if (DateUtils.getHour(date) % nbHeure == 0 && DateUtils.getMinute(date) == 0 && DateUtils.getSecond(date) == 0) {
      return true;
    }
    return false;
  }

  /**
   * Retourne la date de début de la période pour le pas de temps {@link #JOURNALIER}.
   *
   */
  private static Date obtenirDateDebutPeriodeJournalier(Date date) {
    return DateUtils.getDate(DateUtils.getYear(date), DateUtils.getMonth(date), DateUtils.getDay(date), 0, 0, 0);
  }

  /**
   * Retourne la date de début de la période pour le pas de temps {@link #HEBDOMADAIRE}.
   *
   * <p>
   * Les périodes commencent le lundi : on enlève donc le jour de la semaine à la date (et +1 parce que ça commence à 1, et +1 encore parce c'est basé sur les dates américaines, et le premier jour de
   * la semaine c'est le dimanche pour eux (nous le lundi)).
   *
   */
  private static Date obtenirDateDebutPeriodeHebdomadaire(Date date) {
    Date dateLundi = DateUtils.addDays(date, -DateUtils.getWeekDay(date) + 1 + 1);
    return DateUtils.getDate(DateUtils.getYear(dateLundi), DateUtils.getMonth(dateLundi), DateUtils.getDay(dateLundi), 0, 0, 0);
  }

  /**
   * Retourne la date de début de la période pour le pas de temps {@link #BI_MENSUEL}.
   *
   */
  private static Date obtenirDateDebutPeriodeBiMensuel(Date date) {
    // TODO on ne sait pas faire ça de manière exacte !
    if (DateUtils.getDay(date) <= 15) {
      return DateUtils.getDate(DateUtils.getYear(date), DateUtils.getMonth(date), 1, 0, 0, 0);
    }
    return DateUtils.getDate(DateUtils.getYear(date), DateUtils.getMonth(date), 16, 0, 0, 0);
  }

  /**
   * Retourne la date de début de la période pour le pas de temps {@link #MENSUEL}.
   *
   */
  private static Date obtenirDateDebutPeriodeMensuel(Date date) {
    return DateUtils.getDate(DateUtils.getYear(date), DateUtils.getMonth(date), 1, 0, 0, 0);
  }

  /**
   * Retourne la date de début de la période pour le pas de temps {@link #TRIMESTRIEL}.
   *
   */
  private static Date obtenirDateDebutPeriodeTrimestriel(Date date) {
    if (DateUtils.getMonth(date) <= 3) {
      return DateUtils.getDate(DateUtils.getYear(date), 1, 1, 0, 0, 0);
    } else if (DateUtils.getMonth(date) <= 6) {
      return DateUtils.getDate(DateUtils.getYear(date), 4, 1, 0, 0, 0);
    } else if (DateUtils.getMonth(date) <= 9) {
      return DateUtils.getDate(DateUtils.getYear(date), 7, 1, 0, 0, 0);
    }
    return DateUtils.getDate(DateUtils.getYear(date), 10, 1, 0, 0, 0);
  }

  /**
   * Retourne la date de début de la période pour le pas de temps {@link #SEMESTRIEL}.
   *
   */
  private static Date obtenirDateDebutPeriodeSemestriel(Date date) {
    /* on met le début aux années paires */
    if (DateUtils.getMonth(date) <= 6) {
      return DateUtils.getDate(DateUtils.getYear(date), 1, 1, 0, 0, 0);
    }
    return DateUtils.getDate(DateUtils.getYear(date), 7, 1, 0, 0, 0);
  }

  /**
   * Retourne la date de début de la période pour le pas de temps {@link #ANNUEL}.
   *
   */
  private static Date obtenirDateDebutPeriodeAnnuel(Date date) {
    return DateUtils.getDate(DateUtils.getYear(date), 1, 1, 0, 0, 0);
  }

}