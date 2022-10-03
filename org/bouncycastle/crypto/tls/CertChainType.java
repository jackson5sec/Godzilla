package org.bouncycastle.crypto.tls;

public class CertChainType {
  public static final short individual_certs = 0;
  
  public static final short pkipath = 1;
  
  public static boolean isValid(short paramShort) {
    return (paramShort >= 0 && paramShort <= 1);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\CertChainType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */