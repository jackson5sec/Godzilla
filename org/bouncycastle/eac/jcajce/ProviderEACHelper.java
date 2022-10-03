package org.bouncycastle.eac.jcajce;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;

class ProviderEACHelper implements EACHelper {
  private final Provider provider;
  
  ProviderEACHelper(Provider paramProvider) {
    this.provider = paramProvider;
  }
  
  public KeyFactory createKeyFactory(String paramString) throws NoSuchAlgorithmException {
    return KeyFactory.getInstance(paramString, this.provider);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\eac\jcajce\ProviderEACHelper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */