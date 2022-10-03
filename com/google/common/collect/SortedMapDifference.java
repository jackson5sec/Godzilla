package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Map;
import java.util.SortedMap;

@GwtCompatible
public interface SortedMapDifference<K, V> extends MapDifference<K, V> {
  SortedMap<K, V> entriesOnlyOnLeft();
  
  SortedMap<K, V> entriesOnlyOnRight();
  
  SortedMap<K, V> entriesInCommon();
  
  SortedMap<K, MapDifference.ValueDifference<V>> entriesDiffering();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\SortedMapDifference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */