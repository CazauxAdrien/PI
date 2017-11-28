package com.imrglobal.framework.type;

import java.util.Collection;

/**
 * A selection of item in a specified Enum or Persistent Enum type which can not be modified
 */
public final class UnmodifiableListEnum extends ListEnum {

  private static final long serialVersionUID = 5260964543319679364L;
  private boolean init;

  public UnmodifiableListEnum(Collection enums) {
    super(enums);
    this.init = true;
  }

  public UnmodifiableListEnum(EnumerationType ... enums) {
    super(enums);
    this.init = true;
  }

  public UnmodifiableListEnum(Class enumType, String pattern) {
    super(enumType, pattern);
    this.init = true;
  }

  @Override
  public boolean add(EnumerationType item) {
    if (init) {
      throw new UnsupportedOperationException();
    } else {
      return super.add(item);
    }
  }

  @Override
  public boolean add(String itemCode) {
    if (init) {
      throw new UnsupportedOperationException();
    } else {
      return super.add(itemCode);
    }
  }

  @Override
  public boolean add(int itemValue) {
    if (init) {
      throw new UnsupportedOperationException();
    } else {
      return super.add(itemValue);
    }
  }

  @Override
  public void clear() {
    if (init) {
      throw new UnsupportedOperationException();
    } else {
      super.clear();
    }
  }

  @Override
  public boolean remove(EnumerationType item) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setSelection(int ... codes) {
    if (init) {
      throw new UnsupportedOperationException();
    } else {
      super.setSelection(codes);
    }
  }

  @Override
  public void setSelection(String ... codes) {
    if (init) {
      throw new UnsupportedOperationException();
    } else {
      super.setSelection(codes);
    }
  }

  @Override
  public void setSelection(Collection enums) {
    if (init) {
      throw new UnsupportedOperationException();
    } else {
      super.setSelection(enums);
    }
  }

  @Override
  public void setSelection(String selString) {
    if (init) {
      throw new UnsupportedOperationException();
    } else {
      super.setSelection(selString);
    }
  }

  @Override
  public void setSelection(EnumerationType ... enums) {
    if (init) {
      throw new UnsupportedOperationException();
    } else {
      super.setSelection(enums);
    }
  }

  @Override
  public void setSelection(String selString, String separator) {
    if (init) {
      throw new UnsupportedOperationException();
    } else {
      super.setSelection(selString, separator);
    }
  }

  @Override
  protected void setEnumType(Class enumType) {
    if (init) {
      throw new UnsupportedOperationException();
    } else {
      super.setEnumType(enumType);
    }
  }

  @Override
  public boolean addAll(EnumerationType... items) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean addAll(Collection items) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean addAll(ListEnum selection) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean removeAll(EnumerationType... items) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean removeAll(Collection items) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean removeAll(ListEnum selection) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return new UnmodifiableListEnum(getEnumSet());
  }
}
