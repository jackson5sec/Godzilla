package org.bouncycastle.crypto.tls;

public class NameType {
  public static final short host_name = 0;
  
  public static boolean isValid(short paramShort) {
    return (paramShort == 0);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\NameType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */