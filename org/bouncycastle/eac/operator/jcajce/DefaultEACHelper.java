package org.bouncycastle.eac.operator.jcajce;

import java.security.NoSuchAlgorithmException;
import java.security.Signature;

class DefaultEACHelper extends EACHelper {
  protected Signature createSignature(String paramString) throws NoSuchAlgorithmException {
    return Signature.getInstance(paramString);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\eac\operator\jcajce\DefaultEACHelper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */