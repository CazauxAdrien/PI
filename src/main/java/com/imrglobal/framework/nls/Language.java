package com.imrglobal.framework.nls;

import java.util.*;

import com.imrglobal.framework.type.*;

/**
 * Enum type for Languages (FRENCH, GERMAN, ENGLISH, SPANISH, PORTUGUESE, ITALIAN).<br/>
 *
 * TODO Revoir l'implémention pour utiliser {@code allLanguageList} (ou un map ?) dans les API {@link #toString()} {@link #toString(Language language)} {@link #from_int(int value)} TODO Est-ce qu'on
 * ne devrait pas dépréciser les langues inutiles pour efluid ?
 */
public class Language extends EnumType implements Internationalizable {

  private static ListEnum allLanguageList = new ListEnum(Language.class);

  /** Les classes définies ci-dessous n'existent pas dans la classe {@link Locale} proposée par le JDK. */
  private static final Locale SPANISH_LOCALE = new Locale("sp", "", "");
  private static final Locale PORTUGUESE_LOCALE = new Locale("pt", "", "");
  private static final Locale DUTCH_LOCALE = new Locale("nl", "", "");
  private static final Locale SWEDISH_LOCALE = new Locale("sv", "", "");
  private static final Locale NORWEGIAN_LOCALE = new Locale("no", "", "");
  private static final Locale DANISH_LOCALE = new Locale("da", "", "");
  private static final Locale FINNISH_LOCALE = new Locale("fi", "", "");
  private static final Locale ICELANDIC_LOCALE = new Locale("is", "", "");
  private static final Locale POLISH_LOCALE = new Locale("pl", "", "");
  private static final Locale CZECH_LOCALE = new Locale("cs", "", "");
  private static final Locale SLOVAK_LOCALE = new Locale("sk", "", "");
  private static final Locale HUNGARIAN_LOCALE = new Locale("hu", "", "");
  private static final Locale ROMANIAN_LOCALE = new Locale("ro", "", "");

  /** Maximum number of languages to manage */
  public static final int MAX_LANGUAGES = 17;

  /** @deprecated Ne pas utiliser directement la valeur interne ; passer par l'énuméré. A passer en private. */
  @Deprecated
  public static final int _FRENCH = 0;

  /** Instance of the class for french */
  public static final Language FRENCH = new Language(_FRENCH, Locale.FRENCH);

  /** @deprecated Ne pas utiliser directement la valeur interne ; passer par l'énuméré. A passer en private. */
  @Deprecated
  public static final int _ENGLISH = 1;
  /** Instance of the class for english */
  public static final Language ENGLISH = new Language(_ENGLISH, Locale.ENGLISH);

  /** @deprecated Ne pas utiliser directement la valeur interne ; passer par l'énuméré. A passer en private. */
  @Deprecated
  public static final int _SPANISH = 2;
  /** Instance of the class for spanish */
  public static final Language SPANISH = new Language(_SPANISH, SPANISH_LOCALE);

  /** @deprecated Ne pas utiliser directement la valeur interne ; passer par l'énuméré. A passer en private. */
  @Deprecated
  public static final int _GERMAN = 3;
  /** Instance of the class for german */
  public static final Language GERMAN = new Language(_GERMAN, Locale.GERMAN);

  /** @deprecated Ne pas utiliser directement la valeur interne ; passer par l'énuméré. A passer en private. */
  @Deprecated
  public static final int _ITALIAN = 4;
  /** Instance of the class for italian */
  public static final Language ITALIAN = new Language(_ITALIAN, Locale.ITALIAN);

  /** @deprecated Ne pas utiliser directement la valeur interne ; passer par l'énuméré. A passer en private. */
  @Deprecated
  public static final int _PORTUGUESE = 5;
  /** Instance of the class for portuguese */
  public static final Language PORTUGUESE = new Language(_PORTUGUESE, PORTUGUESE_LOCALE);

  /** @deprecated Ne pas utiliser directement la valeur interne ; passer par l'énuméré. A passer en private. */
  @Deprecated
  public static final int _DUTCH = 6;
  /** Instance of the class for dutch */
  public static final Language DUTCH = new Language(_DUTCH, DUTCH_LOCALE);

  /** @deprecated Ne pas utiliser directement la valeur interne ; passer par l'énuméré. A passer en private. */
  @Deprecated
  public static final int _SWEDISH = 7;
  /** Instance of the class for swedish */
  public static final Language SWEDISH = new Language(_SWEDISH, SWEDISH_LOCALE);

  /** @deprecated Ne pas utiliser directement la valeur interne ; passer par l'énuméré. A passer en private. */
  @Deprecated
  public static final int _NORWEGIAN = 8;
  /** Instance of the class for norwegian */
  public static final Language NORWEGIAN = new Language(_NORWEGIAN, NORWEGIAN_LOCALE);

  /** @deprecated Ne pas utiliser directement la valeur interne ; passer par l'énuméré. A passer en private. */
  @Deprecated
  public static final int _DANISH = 9;
  /** Instance of the class for danish */
  public static final Language DANISH = new Language(_DANISH, DANISH_LOCALE);

  /** @deprecated Ne pas utiliser directement la valeur interne ; passer par l'énuméré. A passer en private. */
  @Deprecated
  public static final int _FINNISH = 10;
  /** Instance of the class for finnish */
  public static final Language FINNISH = new Language(_FINNISH, FINNISH_LOCALE);

  /** @deprecated Ne pas utiliser directement la valeur interne ; passer par l'énuméré. A passer en private. */
  @Deprecated
  public static final int _ICELANDIC = 11;
  /** Instance of the class foricelandic */
  public static final Language ICELANDIC = new Language(_ICELANDIC, ICELANDIC_LOCALE);

  /** @deprecated Ne pas utiliser directement la valeur interne ; passer par l'énuméré. A passer en private. */
  @Deprecated
  public static final int _POLISH = 12;
  /** Instance of the class for polish */
  public static final Language POLISH = new Language(_POLISH, POLISH_LOCALE);

  /** @deprecated Ne pas utiliser directement la valeur interne ; passer par l'énuméré. A passer en private. */
  @Deprecated
  public static final int _CZECH = 13;
  /** Instance of the class for czech */
  public static final Language CZECH = new Language(_CZECH, CZECH_LOCALE);

  /** @deprecated Ne pas utiliser directement la valeur interne ; passer par l'énuméré. A passer en private. */
  @Deprecated
  public static final int _SLOVAK = 14;
  /** Instance of the class for slovak */
  public static final Language SLOVAK = new Language(_SLOVAK, SLOVAK_LOCALE);

  /** @deprecated Ne pas utiliser directement la valeur interne ; passer par l'énuméré. A passer en private. */
  @Deprecated
  public static final int _HUNGARIAN = 15;
  /** Instance of the class for hungarian */
  public static final Language HUNGARIAN = new Language(_HUNGARIAN, HUNGARIAN_LOCALE);

  /** @deprecated Ne pas utiliser directement la valeur interne ; passer par l'énuméré. A passer en private. */
  @Deprecated
  public static final int _ROMANIAN = 16;
  /** Instance of the class for romanian */
  public static final Language ROMANIAN = new Language(_ROMANIAN, ROMANIAN_LOCALE);

  /**
   * Default language for the application
   */
  public static final Language DEFAULT;

  /**
   * List of language to use
   */
  private static List languageList;

  static {
    languageList = getLanguageList();
    DEFAULT = (Language) from_int(0);
  }

  private Locale locale;
  private static final long serialVersionUID = 6823577508936878281L;

  /**
   * Protected constructor of the class. It cannot be accessed outside of the class and the children classes. Thus, the number of instances present in the JVM can be controlled.
   */
  protected Language(int value, Locale locale) {
    this.setValue(value);
    this.locale = locale;
    allLanguageList.add(this);
  }

  /**
   * Converts an integer into the unique corresponding Language instance
   * 
   * @param value int value of the enum type
   * @return corresponding Language instance
   */
  public static EnumType from_int(int value) {
    switch (value) {
      case _FRENCH:
        return FRENCH;
      case _ENGLISH:
        return ENGLISH;
      case _SPANISH:
        return SPANISH;
      case _GERMAN:
        return GERMAN;
      case _ITALIAN:
        return ITALIAN;
      case _PORTUGUESE:
        return PORTUGUESE;
      case _DUTCH:
        return DUTCH;
      case _SWEDISH:
        return SWEDISH;
      case _NORWEGIAN:
        return NORWEGIAN;
      case _DANISH:
        return DANISH;
      case _FINNISH:
        return FINNISH;
      case _ICELANDIC:
        return ICELANDIC;
      case _POLISH:
        return POLISH;
      case _CZECH:
        return CZECH;
      case _SLOVAK:
        return SLOVAK;
      case _HUNGARIAN:
        return HUNGARIAN;
      case _ROMANIAN:
        return ROMANIAN;
      default:
        return null;
    }
  }

  /**
   * Internationalisation
   * 
   * @return Gets the bundle used to internalionalised this class
   */
  @Override
  public String getBundleBaseName() {
    return "";
  }

  /**
   * Returns an iterator on the possible values of Language
   */
  @Override
  public Iterator iterator() {
    return getLanguageList().iterator();
  }

  /**
   * Gets the Locale corresponding to the Language
   * 
   * @return corresponding Locale
   */
  public Locale toLocale() {
    return this.locale;
  }

  /**
   * Converts the instance into its string value
   * 
   * @return the name of this language in this language.
   */
  @Override
  public String toString() {
    switch (this.getValue()) {
      case _FRENCH:
        return "français";
      case _ENGLISH:
        return "english";
      case _SPANISH:
        return "español";
      case _GERMAN:
        return "deutsch";
      case _ITALIAN:
        return "italiano";
      case _PORTUGUESE:
        return "português";
      case _DUTCH:
        return "nederlands";
      case _SWEDISH:
        return "svenska";
      case _NORWEGIAN:
        return "norsk";
      case _DANISH:
        return "dansk";
      case _FINNISH:
        return "suomi";
      case _ICELANDIC:
        return "íslenska";
      case _POLISH:
        return "polski";
      case _CZECH:
        return "cestina";
      case _SLOVAK:
        return "slovakian";
      case _HUNGARIAN:
        return "magyar";
      case _ROMANIAN:
        return "româna";
      default: {
        return null;
      }
    }
  }

  /**
   * Converts the instance into its string value
   * 
   * @return Label corresponding to the language
   */
  @Override
  public String toString(Language language) {
    try {
      switch (this.getValue()) {
        case _FRENCH:
          return NLS.getResource(getBundleBaseName(), "french", language);
        case _ENGLISH:
          return NLS.getResource(getBundleBaseName(), "english", language);
        case _SPANISH:
          return NLS.getResource(getBundleBaseName(), "spanish", language);
        case _GERMAN:
          return NLS.getResource(getBundleBaseName(), "german", language);
        case _ITALIAN:
          return NLS.getResource(getBundleBaseName(), "italian", language);
        case _PORTUGUESE:
          return NLS.getResource(getBundleBaseName(), "portuguese", language);
        case _DUTCH:
          return NLS.getResource(getBundleBaseName(), "dutch", language);
        case _SWEDISH:
          return NLS.getResource(getBundleBaseName(), "swedish", language);
        case _NORWEGIAN:
          return NLS.getResource(getBundleBaseName(), "norwegian", language);
        case _DANISH:
          return NLS.getResource(getBundleBaseName(), "danish", language);
        case _FINNISH:
          return NLS.getResource(getBundleBaseName(), "finnish", language);
        case _ICELANDIC:
          return NLS.getResource(getBundleBaseName(), "icelandic", language);
        case _POLISH:
          return NLS.getResource(getBundleBaseName(), "polish", language);
        case _CZECH:
          return NLS.getResource(getBundleBaseName(), "czech", language);
        case _SLOVAK:
          return NLS.getResource(getBundleBaseName(), "slovak", language);
        case _HUNGARIAN:
          return NLS.getResource(getBundleBaseName(), "hungarian", language);
        case _ROMANIAN:
          return NLS.getResource(getBundleBaseName(), "romanian", language);
        default: {
          return null;
        }
      }
    } catch (Throwable ex) {
      return (toString());
    }
  }

  /**
   * Returns the list of Languages effectively used in the application This is parameterised via the parameter Parameters.LANGUAGE_LIST;
   * 
   * @return Vector, list of Languages in use
   */
  public static List getLanguageList() {
    if (languageList == null) {
      ListEnum lgList = new ListEnum(Language.class);

      languageList = allLanguageList.getEnumList();

    }
    return languageList;
  }

  public static ListEnum getAllLanguageList() {
    return allLanguageList;
  }

  /**
   * Checks if two languages are equals, eg their representation as int are the same
   * 
   * @param language the Language to compare
   * @return true if Languages are the same
   */
  public boolean equals(Language language) {
    if (language == null) {
      throw new RuntimeException("Language null in method Language.equals ");
    }
    if (this.to_int() == language.to_int()) {
      return true;
    }
    return false;
  }
}