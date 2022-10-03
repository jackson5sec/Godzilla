package org.bouncycastle.eac.jcajce;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;

class DefaultEACHelper implements EACHelper {
  public KeyFactory createKeyFactory(String paramString) throws NoSuchAlgorithmException {
    return KeyFactory.getInstance(paramString);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\eac\jcajce\DefaultEACHelper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */