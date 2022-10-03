package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Map;

@GwtCompatible
public interface MapDifference<K, V> {
  boolean areEqual();
  
  Map<K, V> entriesOnlyOnLeft();
  
  Map<K, V> entriesOnlyOnRight();
  
  Map<K, V> entriesInCommon();
  
  Map<K, ValueDifference<V>> entriesDiffering();
  
  boolean equals(Object paramObject);
  
  int hashCode();
  
  public static interface ValueDifference<V> {
    V leftValue();
    
    V rightValue();
    
    boolean equals(Object param1Object);
    
    int hashCode();
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\MapDifference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */