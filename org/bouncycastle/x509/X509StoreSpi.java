package org.bouncycastle.x509;

import java.util.Collection;
import org.bouncycastle.util.Selector;

public abstract class X509StoreSpi {
  public abstract void engineInit(X509StoreParameters paramX509StoreParameters);
  
  public abstract Collection engineGetMatches(Selector paramSelector);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\x509\X509StoreSpi.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */