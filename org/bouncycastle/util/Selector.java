package org.bouncycastle.util;

public interface Selector<T> extends Cloneable {
  boolean match(T paramT);
  
  Object clone();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastl\\util\Selector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */