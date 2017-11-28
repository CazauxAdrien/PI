package com.imrglobal.framework.utils;

import java.lang.reflect.Field;

/**
 *  Filter on java.lang.reflect.Field according to the type of the field.
 * 
 */
public class FieldFilterByType  extends AbstractFilter {

    private Class[] possibleTypes;

    public FieldFilterByType(Class[] possibleTypes) {
      this.possibleTypes = possibleTypes;
    }

    public FieldFilterByType(Filter parent, Class[] possibleTypes) {
      super(parent);
      this.possibleTypes = possibleTypes;
    }

    public FieldFilterByType(Filter parent, Class[] possibleTypes, boolean reverse) {
      super(parent, reverse);
      this.possibleTypes = possibleTypes;
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
      for (int i = 0; (i < possibleTypes.length) && (!ok); i++) {
        Class type = possibleTypes[i];
        if ((type != null) && (type.isAssignableFrom(field.getType()))) {
          ok = true;
        }
      }
      return ok;
    }

}
