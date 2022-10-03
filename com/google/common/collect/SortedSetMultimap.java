package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

@GwtCompatible
public interface SortedSetMultimap<K, V> extends SetMultimap<K, V> {
  SortedSet<V> get(K paramK);
  
  @CanIgnoreReturnValue
  SortedSet<V> removeAll(Object paramObject);
  
  @CanIgnoreReturnValue
  SortedSet<V> replaceValues(K paramK, Iterable<? extends V> paramIterable);
  
  Map<K, Collection<V>> asMap();
  
  Comparator<? super V> valueComparator();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\SortedSetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */