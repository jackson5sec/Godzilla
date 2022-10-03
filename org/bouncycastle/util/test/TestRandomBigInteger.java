package org.bouncycastle.util.test;

import java.math.BigInteger;
import org.bouncycastle.util.BigIntegers;

public class TestRandomBigInteger extends FixedSecureRandom {
  public TestRandomBigInteger(String paramString) {
    this(paramString, 10);
  }
  
  public TestRandomBigInteger(String paramString, int paramInt) {
    super(new FixedSecureRandom.Source[] { new FixedSecureRandom.BigInteger(BigIntegers.asUnsignedByteArray(new BigInteger(paramString, paramInt))) });
  }
  
  public TestRandomBigInteger(byte[] paramArrayOfbyte) {
    super(new FixedSecureRandom.Source[] { new FixedSecureRandom.BigInteger(paramArrayOfbyte) });
  }
  
  public TestRandomBigInteger(int paramInt, byte[] paramArrayOfbyte) {
    super(new FixedSecureRandom.Source[] { new FixedSecureRandom.BigInteger(paramInt, paramArrayOfbyte) });
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastl\\util\test\TestRandomBigInteger.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */