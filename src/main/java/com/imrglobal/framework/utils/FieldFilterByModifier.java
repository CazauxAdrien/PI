package com.imrglobal.framework.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Check the modifiers of the field and verify is ok with one of the specified modifier.
 * Allows to test the following modifiers:
 * <ul>
 * <li>Modifier.STATIC
 * <li>Modifier.PRIVATE
 * <li>Modifier.PROTECTED
 * <li>Modifier.PUBLIC
 * <li>Modifier.FINAL
 * <li>Modifier.TRANSIENT
 * </ul>
 *
 */
public class FieldFilterByModifier extends AbstractFilter {

  private int[] possibleModifiers;

  /**
   * Create a filter for this modifiers
   * @param modifiers an array of constants of Modifier
   * @see Modifier
   */
  public FieldFilterByModifier(int[] modifiers) {
    this.possibleModifiers = modifiers;
  }

  /**
   * Create a filter for this modifiers
   * @param modifiers an array of constants of Modifier
   * @see Modifier
   */
  public FieldFilterByModifier(Filter parent, int[] modifiers) {
    super(parent);
    this.possibleModifiers = modifiers;
  }

  /**
   * Create a filter for this modifiers
   * @param modifiers an array of constants of Modifier
   * @see Modifier
   */
  public FieldFilterByModifier(Filter parent, int[] modifiers, boolean reverse) {
    super(parent, reverse);
    this.possibleModifiers = modifiers;
  }

  protected boolean check(Object obj) {
    boolean ok = false;
    if (obj instanceof Field) {
      Field field = (Field) obj;
      ok = checkField(field);
    }
    return ok;
  }

  protected boolean checkField(Field field) {
    boolean ok = false;
    int modifiers = field.getModifiers();
    for (int i = 0; (i < possibleModifiers.length) && (!ok); i++) {
      int mod = possibleModifiers[i];
      ok = checkModifier(modifiers,mod);
    }
    return ok;
  }

  protected boolean checkModifier(int fieldModifier, int modifier) {
    boolean ok;
    if (Modifier.STATIC == modifier) {
      ok = Modifier.isStatic(fieldModifier);
    } else if (Modifier.PRIVATE == modifier) {
      ok = Modifier.isPrivate(fieldModifier);
    } else if (Modifier.PROTECTED == modifier) {
      ok = Modifier.isProtected(fieldModifier);
    } else if (Modifier.PUBLIC == modifier) {
      ok = Modifier.isPublic(fieldModifier);
    } else if (Modifier.FINAL == modifier) {
      ok = Modifier.isFinal(fieldModifier);
    } else if (Modifier.TRANSIENT == modifier) {
      ok = Modifier.isTransient(fieldModifier);
    } else {
      ok = false;
    }
    return ok;
  }

}

