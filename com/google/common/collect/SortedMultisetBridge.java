package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import java.util.Set;
import java.util.SortedSet;

@GwtIncompatible
interface SortedMultisetBridge<E> extends Multiset<E> {
  SortedSet<E> elementSet();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\SortedMultisetBridge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */