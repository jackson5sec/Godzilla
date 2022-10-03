package org.bouncycastle.eac.jcajce;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

interface EACHelper {
  KeyFactory createKeyFactory(String paramString) throws NoSuchProviderException, NoSuchAlgorithmException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\eac\jcajce\EACHelper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */