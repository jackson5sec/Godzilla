package org.bouncycastle.util;

import java.util.Collection;

public interface Store<T> {
  Collection<T> getMatches(Selector<T> paramSelector) throws StoreException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastl\\util\Store.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */