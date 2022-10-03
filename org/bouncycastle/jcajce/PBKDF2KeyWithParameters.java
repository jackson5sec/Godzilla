package org.bouncycastle.jcajce;

import javax.crypto.interfaces.PBEKey;
import org.bouncycastle.crypto.CharToByteConverter;
import org.bouncycastle.util.Arrays;

public class PBKDF2KeyWithParameters extends PBKDF2Key implements PBEKey {
  private final byte[] salt;
  
  private final int iterationCount;
  
  public PBKDF2KeyWithParameters(char[] paramArrayOfchar, CharToByteConverter paramCharToByteConverter, byte[] paramArrayOfbyte, int paramInt) {
    super(paramArrayOfchar, paramCharToByteConverter);
    this.salt = Arrays.clone(paramArrayOfbyte);
    this.iterationCount = paramInt;
  }
  
  public byte[] getSalt() {
    return this.salt;
  }
  
  public int getIterationCount() {
    return this.iterationCount;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jcajce\PBKDF2KeyWithParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */