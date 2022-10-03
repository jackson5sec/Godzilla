package org.bouncycastle.crypto.tls;

public class ECBasisType {
  public static final short ec_basis_trinomial = 1;
  
  public static final short ec_basis_pentanomial = 2;
  
  public static boolean isValid(short paramShort) {
    return (paramShort >= 1 && paramShort <= 2);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\ECBasisType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */