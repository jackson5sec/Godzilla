package org.bouncycastle.jcajce.spec;

import javax.crypto.SecretKey;

public class RepeatedSecretKeySpec implements SecretKey {
  private String algorithm;
  
  public RepeatedSecretKeySpec(String paramString) {
    this.algorithm = paramString;
  }
  
  public String getAlgorithm() {
    return this.algorithm;
  }
  
  public String getFormat() {
    return null;
  }
  
  public byte[] getEncoded() {
    return null;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jcajce\spec\RepeatedSecretKeySpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */