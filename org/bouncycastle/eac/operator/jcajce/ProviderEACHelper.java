package org.bouncycastle.eac.operator.jcajce;

import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Signature;

class ProviderEACHelper extends EACHelper {
  private final Provider provider;
  
  ProviderEACHelper(Provider paramProvider) {
    this.provider = paramProvider;
  }
  
  protected Signature createSignature(String paramString) throws NoSuchAlgorithmException {
    return Signature.getInstance(paramString, this.provider);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\eac\operator\jcajce\ProviderEACHelper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */