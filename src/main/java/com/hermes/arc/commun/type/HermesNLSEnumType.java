package com.hermes.arc.commun.type;

import java.lang.reflect.*;
import java.util.*;

import com.imrglobal.framework.nls.*;
import com.imrglobal.framework.type.EnumType;
import com.imrglobal.framework.utils.ReflectUtils;

import com.hermes.arc.commun.util.HermesEnumComparator;

/**
 * Classe mère des types énuméré multilingues Chaque valeur possède deux libellés :
 * <ul>
 * <li>Le libellé long retourné par la méthode toString
 * <li>Le libellé court retourné par la méthode getShortLabel
 * </ul>
 * La traduction des deux libellés est définie dans un fichier properties dont le nom correspond au nom de la classe du type. Les clé utilisées sont déduites du nom de la constantes définissant la
 * valeur du type à traduire :
 * <ul>
 * <li><Nom de la constante> pour le libellé long
 * <li><Nom de la constante>_SHORT pour le libellé court
 * </ul>
 * Si la clé pour le libellé court n'est pas renseigné, la méthode getShortLabel retourne le libellé long.
 */
public abstract class HermesNLSEnumType extends EnumType implements IHermesNLSEnumType {

  private String nlsKey;
  private String shortNlsKey;

  public static final String SHORT_KEY_SUFFIX = "_SHORT";
  /**
   * Permet une recherche d'implémentations supplémentaires d'énumérés dans des plugins. <br>
   * S'utilise de la façon suivante : <br>
   * <code>PluginsAccess.getInstance().searchImplemsClassInBundleFromAllPlugin(ETypeAffaire.class, ENUM_TYPE_IMPLEMS_BUNDLE_NAME_PREFIX, "ETYPEAFFAIRE_IMPLEMS", Locale.getDefault());</code>
   */
  public static final String ENUM_TYPE_IMPLEMS_BUNDLE_NAME_PREFIX = "enumTypeImplems";

  @Override
  public String getNlsKey() {
    if (nlsKey == null) {
      nlsKey = defaultNlsKey();
    }
    return nlsKey;
  }

  protected String defaultNlsKey() {
    String key = null;
    final List list = ReflectUtils.getFields(getClass());
    if (list != null) {
      final Iterator itOnFields = list.iterator();
      while ((key == null) && (itOnFields.hasNext())) {
        final Field field = (Field) itOnFields.next();
        final int fieldModifiers = field.getModifiers();
        if ((Modifier.isStatic(fieldModifiers)) && (field.getType().equals(getClass()))) {
          try {
            Object value = field.get(null);
            if (this.equals(value)) {
              key = field.getName();
            }
          } catch (IllegalArgumentException e) {
            
          } catch (IllegalAccessException e) {
           }
        }
      }
    }
    return key;
  }

  protected void setNlsKey(String nlsKey) {
    this.nlsKey = nlsKey;
  }

  @Override
  public String getShortNlsKey() {
    if (this.shortNlsKey == null) {
      this.shortNlsKey = defaultShortNlsKey();
    }
    return shortNlsKey;
  }

  protected String defaultShortNlsKey() {
    return getNlsKey() + SHORT_KEY_SUFFIX;
  }

  protected void setShortNlsKey(String shortNlsKey) {
    this.shortNlsKey = shortNlsKey;
  }

  protected String getBundle() {
    return getClass().getName();
  }

  /**
   * Retourne le libellé long de cette valeur du type énuméré.
   * 
   * @return le libellé long pour le language par défaut.
   */
  @Override
  public String toString() {
    return toString(Language.DEFAULT);
  }

  /**
   * Retourne le libellé long de cette valeur du type énuméré.
   * 
   * @return le libellé long pour le language spécifié.
   */
  @Override
  public String toString(Language language) {
    String s;
    String nlsKey = getNlsKey();
    if (nlsKey != null) {
      try {
        s = NLS.getResource(getBundle(), nlsKey, language);
      } catch (Exception e) {
        s = nlsKey;
      }
    } else {
      s = "???";
    }
    return s;
  }

  /**
   * Retourne le libellé court de cette valeur si il existe. Sinon retourne le libellé long.
   * 
   * @param lg la langue
   * @return le libellé court de cette élément d'énuméré
   */
  @Override
  public String getShortLabel(Language lg) {
    String s;
    String nlsKey = getShortNlsKey();
    if (nlsKey != null) {
      try {
        s = NLS.getResource(getBundle(), nlsKey, lg != null ? lg : Language.DEFAULT);
      } catch (Exception e) {
        s = toString(lg);
      }
    } else {
      s = toString(lg);
    }
    return s;
  }

  @Override
  public String getLibelle() {
    return toString();
  }

  @Override
  public String getLibelleCourt() {
    return getShortLabel(Language.FRENCH);
  }

  @Override
  public int getOrdre() {
    return to_int();
  }

  @Override
  public Iterator sort(int mode, Language lg) {
    if (mode == HermesEnumComparator.SHORT_ALPHABETIC_ASC || mode == HermesEnumComparator.SHORT_ALPHABETIC_DESC) {
      return super.sort(new HermesEnumComparator(mode));
    }
    return super.sort(mode, lg);
  }

}
